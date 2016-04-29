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
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static javafx.geometry.Pos.CENTER;

/**
 * @author Artem Labazin <xxlabaza@gmail.com>
 * @since 25.04.2016
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TextFilteredColumn<S, T extends String> extends AbstractFilteredColumn<S, T> {

    private String promptText;

    private TextField textField;

    @Override
    public Node createEditor (Map<String, Object> filters) {
        textField = new TextField();
        textField.setPromptText(promptText);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                filters.remove(getField());
            } else {
                filters.put(getField(), newValue);
            }
        });

        VBox vBox = new VBox(textField);
        vBox.setAlignment(CENTER);
        return vBox;
    }

    @Override
    public void resetEditor () {
        textField.clear();
    }
}
