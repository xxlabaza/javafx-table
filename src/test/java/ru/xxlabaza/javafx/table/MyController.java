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

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import lombok.Data;

/**
 * @author Artem Labazin <xxlabaza@gmail.com>
 * @since 24.04.2016
 */
public class MyController implements Initializable {

    @FXML
    private FilteredTable<MyClass> table;

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        ObservableList<TableColumn<MyClass, ?>> columns = table.getColumns();
        ((TableColumn<MyClass, String>) columns.get(0)).setCellValueFactory(
                cell -> cell.getValue().nameProperty()
        );
        ((TableColumn<MyClass, Date>) columns.get(1)).setCellValueFactory(
                cell -> cell.getValue().createdProperty()
        );
        ((TableColumn<MyClass, Number>) columns.get(2)).setCellValueFactory(
                cell -> cell.getValue().ageProperty()
        );
        ((TableColumn<MyClass, Boolean>) columns.get(3)).setCellValueFactory(
                cell -> cell.getValue().visibleProperty()
        );

        table.setItems(FXCollections.observableArrayList(
                new MyClass("Artem Labazin", new Date(), true, 45),
                new MyClass("Artem Labazin", new Date(), false, 16),
                new MyClass("Artem Labazin", new Date(), true, 25),
                new MyClass("Artem Labazin", new Date(), false, 76),
                new MyClass("Artem Labazin", new Date(), false, 9)
        ));
        table.setOnSubmitFiltersAction(event -> {
            table.getFilters().entrySet().stream().forEach(it -> {
                System.out.println(it.getKey() + ": " + it.getValue());
            });
        });
//        table.getFilters().addListener((MapChangeListener.Change<? extends String, ? extends Object> change) -> {
//            change.getMap().entrySet().stream().forEach(it -> {
//                System.out.println(it.getKey() + ": " + it.getValue());
//            });
//        });
    }

    @Data
    public class MyClass {

        private final StringProperty name;

        private final ObjectProperty<Date> created;

        private final BooleanProperty visible;

        private final IntegerProperty age;

        public MyClass (String name, Date created, boolean visible, int age) {
            this.name = new SimpleStringProperty(name);
            this.created = new SimpleObjectProperty<>(created);
            this.visible = new SimpleBooleanProperty(visible);
            this.age = new SimpleIntegerProperty(age);
        }

        public void setName (String name) {
            this.name.setValue(name);
        }

        public String getName () {
            return name.getValue();
        }

        public StringProperty nameProperty () {
            return name;
        }

        public void setCreated (Date created) {
            this.created.setValue(created);
        }

        public Date getCreated () {
            return created.getValue();
        }

        public ObjectProperty<Date> createdProperty () {
            return created;
        }

        public void setVisible (boolean visible) {
            this.visible.setValue(visible);
        }

        public boolean getVisible () {
            return visible.getValue();
        }

        public BooleanProperty visibleProperty () {
            return visible;
        }

        public void setAge (int age) {
            this.age.setValue(age);
        }

        public int getAge () {
            return age.getValue();
        }

        public IntegerProperty ageProperty () {
            return age;
        }
    }
}
