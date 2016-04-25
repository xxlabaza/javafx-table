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

import java.util.Map;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import static javafx.geometry.Orientation.VERTICAL;
import static javafx.geometry.Pos.CENTER;

/**
 * @author Artem Labazin <xxlabaza@gmail.com>
 * @since 25.04.2016
 */
class FilteredToolBar extends ToolBar {

    private final Button previousPage;

    private final NumberTextField pageNumber;

    private final Button nextPage;

    private final Label totalPages;

    private final ComboBox<Integer> pageSize;

    private final Label totalItems;

    private final Button reset;

    private final Button filter;

    FilteredToolBar (Map<String, String> toolbar) {
        super();

        pageNumber = new NumberTextField("1");
        pageNumber.setPrefColumnCount(3);
        pageNumber.setAlignment(CENTER);

        totalPages = new Label("1");

        previousPage = new Button("<");
        previousPage.disableProperty().bind(pageNumber.textProperty().isEqualTo("1"));
        previousPage.setOnAction(event -> pageNumber.decriment());

        nextPage = new Button(">");
        nextPage.disableProperty().bind(pageNumber.textProperty().isEqualTo(totalPages.getText()));
        nextPage.setOnAction(event -> pageNumber.increment());

        pageSize = new ComboBox<>(FXCollections.observableArrayList(25, 50, 100));
        pageSize.getSelectionModel().select(0);

        totalItems = new Label("0");

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        reset = new Button(toolbar.getOrDefault("reset", "Reset"));
        filter = new Button(toolbar.getOrDefault("filter", "Filter"));

        getItems().addAll(
                new Label(toolbar.getOrDefault("page", "Page")),
                previousPage,
                pageNumber,
                nextPage,
                new Label(toolbar.getOrDefault("of", "of")),
                totalPages,
                new Separator(VERTICAL),
                new Label(toolbar.getOrDefault("view", "View")), pageSize,
                new Separator(VERTICAL),
                new Label(toolbar.getOrDefault("total", "Found total")),
                totalItems,
                new Label(toolbar.getOrDefault("records", "records")),
                spacer,
                reset, filter
        );
    }

    StringProperty pageNumberProperty () {
        return pageNumber.textProperty();
    }

    void setTotalPages (int totalPages) {
        this.totalPages.setText(String.valueOf(totalPages));
    }

    void setTotalItems (int totalItems) {
        this.totalItems.setText(String.valueOf(totalItems));
    }

    ObjectProperty<SingleSelectionModel<Integer>> pageSizeSelectionProperty () {
        return pageSize.selectionModelProperty();
    }

    void setOnResetAction (EventHandler<ActionEvent> event) {
        reset.setOnAction(event);
    }

    void setOnSubmitAction (EventHandler<ActionEvent> event) {
        filter.setOnAction(event);
    }
}
