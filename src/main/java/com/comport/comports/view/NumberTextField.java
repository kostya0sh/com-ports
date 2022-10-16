package com.comport.comports.view;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {

    private boolean isInvalid = false;


    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (validate(text)) {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text) {
        return text.matches("[0-9]*");
    }

    public void setInvalid(boolean newValue) {
        if (isInvalid != newValue) {
            if (newValue) {
                setStyle("-fx-text-fill: red");
            } else {
                setStyle("-fx-text-fill: black");
            }
        }
        isInvalid = newValue;
    }
}
