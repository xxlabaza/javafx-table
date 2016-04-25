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
package ru.xxlabaza.javafx.table.column;

import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static javafx.geometry.Pos.BOTTOM_CENTER;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.layout.Priority.ALWAYS;
import static javafx.scene.layout.Priority.NEVER;

/**
 * @author Artem Labazin <xxlabaza@gmail.com>
 * @since 24.04.2016
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractFilteredColumn<S, T> extends TableColumn<S, T> {

    private String field;

    public void initializeEditor (Map<String, Object> filters) {
        Node editor = createEditor(filters);
        HBox hBox = new HBox(new Label(getText()));
        hBox.setAlignment(CENTER);

        VBox vBox = new VBox(hBox, editor);
        VBox.setVgrow(editor, ALWAYS);
        VBox.setVgrow(hBox, NEVER);
        vBox.setAlignment(BOTTOM_CENTER);
        vBox.setPadding(new Insets(1, 1, 3, 1));

        setText(null);
        setGraphic(vBox);
    }

    public abstract Node createEditor (Map<String, Object> filters);

    public abstract void resetEditor ();
}
