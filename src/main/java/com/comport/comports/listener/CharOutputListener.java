package com.comport.comports.listener;

import com.comport.comports.portwrapper.SerialPortWrapper;
import com.comport.comports.portwrapper.Packet;
import com.comport.comports.portwrapper.utils.SerialPortOnDataReceivedListener;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class CharOutputListener implements SerialPortOnDataReceivedListener {

    private final SerialPortWrapper receivePortWrapper;
    private final SerialPortWrapper sendPortWrapper;

    private final TextArea textArea;
    private final Label label;

    public CharOutputListener(SerialPortWrapper receivePortWrapper, SerialPortWrapper sendPortWrapper,
                              TextArea textArea, Label label) {
        this.receivePortWrapper = receivePortWrapper;
        this.sendPortWrapper = sendPortWrapper;
        this.textArea = textArea;
        this.label = label;
    }

    @Override
    public void onDataReceived(Packet packet) {
        String data = packet.toString();
        System.out.println("Data received: " + data);
        textArea.appendText(data);
        Platform.runLater(
                () -> label.textProperty().setValue(sendPortWrapper.getName() + "->" + receivePortWrapper.getName() +
                        " (" + receivePortWrapper.getReceiveThread().getBytesReceived() + " bytes)")
        );
    }
}
