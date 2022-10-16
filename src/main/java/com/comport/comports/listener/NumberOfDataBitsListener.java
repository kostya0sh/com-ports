package com.comport.comports.listener;

import com.comport.comports.portwrapper.SerialPortWrapper;
import com.comport.comports.view.NumberTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class NumberOfDataBitsListener implements ChangeListener<String> {

    private final SerialPortWrapper inputPortWrapper;
    private final SerialPortWrapper outputPortWrapper;
    private final NumberTextField numberTextField;

    public NumberOfDataBitsListener(SerialPortWrapper inputPortWrapper, SerialPortWrapper outputPortWrapper,
                                    NumberTextField numberTextField) {
        this.inputPortWrapper = inputPortWrapper;
        this.outputPortWrapper = outputPortWrapper;
        this.numberTextField = numberTextField;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue.isBlank()) {
            return;
        }

        boolean inputUpdated = inputPortWrapper.setNumDataBits(Integer.parseInt(newValue));
        if (!inputUpdated) {
            int currentInputNumDataBits = inputPortWrapper.getNumDataBits();
            System.out.println("Input serial port (" + inputPortWrapper.getName()
                    + ") number of data bits not updated!" +
                    " Current number of data bits: " + currentInputNumDataBits);
            numberTextField.setInvalid(true);
        }

        boolean outputUpdated = outputPortWrapper.setNumDataBits(Integer.parseInt(newValue));
        if (!outputUpdated) {
            int currentOutputNumDataBits = outputPortWrapper.getNumDataBits();
            System.out.println("Output serial port (" + outputPortWrapper.getName()
                    + ") number of data bits not updated!" +
                    " Current number of data bits: " + currentOutputNumDataBits);
            numberTextField.setInvalid(true);
        }

        if (inputUpdated && outputUpdated) {
            numberTextField.setInvalid(false);
        }
    }
}
