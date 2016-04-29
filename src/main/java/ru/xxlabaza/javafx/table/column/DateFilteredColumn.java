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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static javafx.geometry.Pos.CENTER;

/**
 * @author Artem Labazin <xxlabaza@gmail.com>
 * @since 25.04.2016
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DateFilteredColumn<S, T extends TemporalAccessor> extends AbstractFilteredColumn<S, T> {

    private String fromPrompt;

    private String toPrompt;

    private String datePattern;

    private DatePicker from;

    private DatePicker to;

    public void setDateTimeFormatter (DateTimeFormatter dateTimeFormatter) {
        setCellFactory(column -> {
            return new TableCell<S, T>() {

                @Override
                protected void updateItem (T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(dateTimeFormatter.format(item));
                    }
                }
            };
        });
    }

    @Override
    public Node createEditor (Map<String, Object> filters) {
        from = new DatePicker();
        from.setPromptText(fromPrompt);
        from.setConverter(new MyConverter(datePattern));
        from.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                filters.remove(getField() + ".from");
            } else {
                filters.put(getField() + ".from", newValue);
            }
        });

        to = new DatePicker();
        to.setPromptText(toPrompt);
        to.setConverter(new MyConverter(datePattern));
        to.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                filters.remove(getField() + ".to");
            } else {
                filters.put(getField() + ".to", newValue);
            }
        });

        VBox vBox = new VBox(from, to);
        vBox.setAlignment(CENTER);
        return vBox;
    }

    @Override
    public void resetEditor () {
        from.setValue(null);
        to.setValue(null);
    }

    private class MyConverter extends StringConverter<LocalDate> {

        private final DateTimeFormatter dateTimeFormatter;

        MyConverter (String pattern) {
            this.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        }

        @Override
        public String toString (LocalDate localDate) {
            return localDate != null
                   ? dateTimeFormatter.format(localDate)
                   : "";
        }

        @Override
        public LocalDate fromString (String string) {
            return string != null && !string.isEmpty()
                   ? LocalDate.parse(string, dateTimeFormatter)
                   : null;
        }
    }
}
