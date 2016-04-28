/*
 * Copyright 2016 Artem Labazin <xxlabaza@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.xxlabaza.javafx.table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.NamedArg;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import ru.xxlabaza.javafx.table.column.AbstractFilteredColumn;

import static javafx.scene.layout.Priority.ALWAYS;
import static javafx.scene.layout.Priority.NEVER;

/**
 * @author Artem Labazin <xxlabaza@gmail.com>
 * @since 24.04.2016
 */
public class FilteredTable<T> extends VBox {

    private FilteredToolBar toolBar;

    private TableView<T> table;

    private final ObservableMap<String, Object> filters;

    public FilteredTable () {
        this(new HashMap<>(0), Arrays.asList(25, 50, 100), true);
    }

    public FilteredTable (@NamedArg("toolbar") Map<String, String> toolbar) {
        this(toolbar, Arrays.asList(25, 50, 100), true);
    }

    public FilteredTable (@NamedArg("showSubmitButton") Boolean showSubmitButton) {
        this(new HashMap<>(0), Arrays.asList(25, 50, 100), showSubmitButton);
    }

    public FilteredTable (@NamedArg("pageSizes") List<Integer> pageSizes) {
        this(new HashMap<>(0), pageSizes, true);
    }

    public FilteredTable (@NamedArg("pageSizes") List<Integer> pageSizes,
                          @NamedArg("showSubmitButton") Boolean showSubmitButton) {
        this(new HashMap<>(0), pageSizes, showSubmitButton);
    }

    public FilteredTable (@NamedArg("toolbar") Map<String, String> toolbar,
                          @NamedArg("pageSizes") List<Integer> pageSizes) {
        this(toolbar, pageSizes, true);
    }

    public FilteredTable (@NamedArg("toolbar") Map<String, String> toolbar,
                          @NamedArg("showSubmitButton") Boolean showSubmitButton) {
        this(toolbar, Arrays.asList(25, 50, 100), showSubmitButton);
    }

    public FilteredTable (@NamedArg("toolbar") Map<String, String> toolbar,
                          @NamedArg("pageSizes") List<Integer> pageSizes,
                          @NamedArg("showSubmitButton") Boolean showSubmitButton) {
        filters = FXCollections.observableHashMap();

        toolBar = new FilteredToolBar(toolbar, pageSizes, showSubmitButton);

        table = new TableView<>();
        table.getColumns().addListener((ListChangeListener.Change<? extends TableColumn<T, ?>> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream()
                            .filter(column -> column instanceof AbstractFilteredColumn)
                            .map(column -> (AbstractFilteredColumn<T, ?>) column)
                            .forEach(column -> column.initializeEditor(filters));
                } else if (change.wasRemoved()) {
                    change.getRemoved().stream()
                            .filter(column -> column instanceof AbstractFilteredColumn)
                            .map(column -> (AbstractFilteredColumn<T, ?>) column)
                            .forEach(column -> filters.remove(column.getField()));
                }
            }
        });

        getChildren().addAll(toolBar, table);
        VBox.setVgrow(toolBar, NEVER);
        VBox.setVgrow(table, ALWAYS);
    }

    public TableView.TableViewSelectionModel<T> getSelectionModel () {
        return table.getSelectionModel();
    }

    public void setPlaceholder (Node value) {
        table.setPlaceholder(value);
    }

    public Node getPlaceholder () {
        return table.getPlaceholder();
    }

    public Callback<TableView.ResizeFeatures, Boolean> getColumnResizePolicy () {
        return table.getColumnResizePolicy();
    }

    public void setColumnResizePolicy (Callback<TableView.ResizeFeatures, Boolean> callback) {
        table.setColumnResizePolicy(callback);
    }

    public ObjectProperty<TableView.TableViewSelectionModel<T>> selectionModelProperty () {
        return table.selectionModelProperty();
    }

    public ObservableMap<String, Object> getFilters () {
        return filters;
    }

    public ObservableList<TableColumn<T, ?>> getColumns () {
        return table.getColumns();
    }

    public void setItems (ObservableList<T> value) {
        table.setItems(value);
    }

    public ObservableList<T> getItems () {
        return table.getItems();
    }

    public Integer getPageNumber () {
        return toolBar.getPageNumber();
    }

    public void setPageNumber (Integer page) {
        toolBar.setPageNumber(page);
    }

    public IntegerProperty pageNumberProperty () {
        return toolBar.pageNumberProperty();
    }

    public void setTotalPages (int totalPages) {
        toolBar.setTotalPages(totalPages);
    }

    public void setTotalItems (int totalItems) {
        toolBar.setTotalItems(totalItems);
    }

    public ObjectProperty<Integer> pageSizeValueProperty () {
        return toolBar.pageSizeValueProperty();
    }

    public Integer getPageSize () {
        return toolBar.getPageSize();
    }

    public void setOnSubmitFiltersAction (EventHandler<ActionEvent> event) {
        toolBar.setOnSubmitFiltersAction(event);
    }

    public void setOnResetFiltersAction (EventHandler<ActionEvent> eventHandler) {
        toolBar.setOnResetFiltersAction(event -> {
            table.getColumns().stream()
                    .filter(column -> column instanceof AbstractFilteredColumn)
                    .forEach(column -> ((AbstractFilteredColumn) column).resetEditor());

            eventHandler.handle(event);
        });
    }

    public void setOnRefreshTableAction (EventHandler<ActionEvent> eventHandler) {
        toolBar.setOnRefreshTableAction(eventHandler);
    }

    public void resetFilters () {
        toolBar.resetFilters();
    }
}
