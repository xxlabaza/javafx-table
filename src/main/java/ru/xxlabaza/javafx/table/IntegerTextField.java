/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.xxlabaza.javafx.table;

import java.util.regex.Pattern;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;

/**
 * @author Artem Labazin <xxlabaza@gmail.com>
 * @since 27.04.2016
 */
public class IntegerTextField extends TextField {

    private static final Pattern INTEGER_PATTERN;

    static {
        INTEGER_PATTERN = Pattern.compile("\\d+");
    }

    private final IntegerProperty valueProperty;

    {
        valueProperty = new SimpleIntegerProperty();
        textProperty().addListener((observable, oldValue, newValue) -> {
            valueProperty.setValue(Integer.parseInt(newValue));
        });
    }

    public IntegerTextField () {
    }

    public IntegerTextField (String text) {
        super(text);
    }

    public void increment () {
        Integer currentValue = getValue();
        setValue(currentValue + 1);
    }

    public void decriment () {
        Integer currentValue = getValue();
        setValue(currentValue - 1);
    }

    public void setValue (Integer value) {
        setText(value.toString());
        valueProperty.setValue(value);
    }

    public Integer getValue () {
        return valueProperty.getValue();
    }

    public IntegerProperty valueProperty () {
        return valueProperty;
    }

    @Override
    public void replaceSelection (String replacement) {
        if (isValidValue(replacement)) {
            super.replaceSelection(replacement);
        }
    }

    @Override
    public void replaceText (int start, int end, String text) {
        if (isValidValue(text)) {
            super.replaceText(start, end, text);
        }
    }

    private boolean isValidValue (String text) {
        return text.isEmpty() || INTEGER_PATTERN.matcher(text).matches();
    }
}
