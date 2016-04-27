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

import java.util.List;
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
import javafx.scene.image.ImageView;
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

    private final IntegerTextField pageNumber;

    private final Button nextPage;

    private final Label totalPages;

    private final ComboBox<Integer> pageSize;

    private final Label totalItems;

    private final Button buttonRefreshTable;

    private final Button buttonResetFilters;

    private Button buttonSubmitFilters;

    FilteredToolBar (Map<String, String> localization, List<Integer> pageSizes,
                     boolean showSubmitButton
    ) {
        super();

        pageNumber = new IntegerTextField("1");
        pageNumber.setPrefColumnCount(2);
        pageNumber.setAlignment(CENTER);

        totalPages = new Label("0");

        previousPage = new Button("<");
        previousPage.disableProperty().bind(
                pageNumber.textProperty().isEqualTo("1")
                .or(pageNumber.textProperty().isEmpty())
        );
        previousPage.setOnAction(event -> pageNumber.decriment());

        nextPage = new Button(">");
        nextPage.disableProperty().bind(
                pageNumber.textProperty().isEqualTo(totalPages.textProperty())
                .or(pageNumber.textProperty().isEmpty())
                .or(totalPages.textProperty().isEqualTo("0"))
        );
        nextPage.setOnAction(event -> {
            pageNumber.increment();
        });

        pageSize = new ComboBox<>(FXCollections.observableArrayList(pageSizes));

        pageSize.getSelectionModel().select(0);

        totalItems = new Label("0");

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonRefreshTable = new Button(null, new ImageView("images/refresh.png"));
        buttonResetFilters = new Button(localization.getOrDefault("reset", "Reset"));

        getItems().addAll(
                new Label(localization.getOrDefault("page", "Page")),
                previousPage,
                pageNumber,
                nextPage,
                new Label(localization.getOrDefault("of", "of")),
                totalPages,
                new Separator(VERTICAL),
                new Label(localization.getOrDefault("view", "View")), pageSize,
                new Separator(VERTICAL),
                new Label(localization.getOrDefault("total", "Found total")),
                totalItems,
                new Label(localization.getOrDefault("records", "records")),
                spacer,
                buttonRefreshTable,
                buttonResetFilters
        );

        if (showSubmitButton) {
            buttonSubmitFilters = new Button(localization.getOrDefault("filter", "Filter"));
            getItems().add(buttonSubmitFilters);
        }
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

    Integer getPageSize () {
        return pageSize.getValue();
    }

    void setOnResetFiltersAction (EventHandler<ActionEvent> event) {
        buttonResetFilters.setOnAction(event);
    }

    void resetFilters () {
        buttonResetFilters.fire();
    }

    void setOnSubmitFiltersAction (EventHandler<ActionEvent> event) {
        buttonSubmitFilters.setOnAction(event);
    }

    void setOnRefreshTableAction (EventHandler<ActionEvent> event) {
        buttonRefreshTable.setOnAction(event);
    }
}
