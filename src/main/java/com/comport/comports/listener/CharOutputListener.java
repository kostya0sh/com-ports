package com.comport.comports.listener;

import com.comport.comports.portwrapper.SerialPortWrapper;
import com.comport.comports.portwrapper.utils.SerialPortInputStreamListener;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class CharOutputListener implements SerialPortInputStreamListener {

    private final SerialPortWrapper inputPortWrapper;
    private final SerialPortWrapper outputPortWrapper;

    private final TextArea textArea;
    private final Label label;

    public CharOutputListener(SerialPortWrapper inputPortWrapper, SerialPortWrapper outputPortWrapper,
                              TextArea textArea, Label label) {
        this.inputPortWrapper = inputPortWrapper;
        this.outputPortWrapper = outputPortWrapper;
        this.textArea = textArea;
        this.label = label;
    }

    @Override
    public void onDataReceived(int data) {
        Character c = (char) data;
        System.out.println("Data received: " + c);
        textArea.appendText(c + "");
        Platform.runLater(
                () -> label.textProperty().setValue(inputPortWrapper.getName() + "->" + outputPortWrapper.getName() +
                        " (" + inputPortWrapper.getInputThread().getBytesReceived() + " bytes)")
        );
    }
}
