package com.comport.comports.listener;

import com.comport.comports.portwrapper.SerialPortWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class CharInputListener implements ChangeListener<String> {

    private final SerialPortWrapper serialPortWrapper;

    public CharInputListener(SerialPortWrapper serialPortWrapper) {
        this.serialPortWrapper = serialPortWrapper;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        System.out.println("Old value: " + oldValue + ", new value: " + newValue);

        if (oldValue.length() >= newValue.length()) {
            return;
        }

        char diff = newValue.charAt(newValue.length() - 1);

        serialPortWrapper.getSendThread().send(String.valueOf(diff));
    }
}
