package com.comport.comports.portwrapper;

import com.comport.comports.portwrapper.utils.SerialPortOnDataReceivedListener;
import com.comport.comports.portwrapper.utils.SerialPortOnDataSendListener;
import com.fazecast.jSerialComm.SerialPort;

public class SerialPortWrapper {

    public static final int DEFAULT_NUMBER_OF_DATA_BITS = 8;

    private final SerialPort serialPort;

    private SerialPortReceiveThread serialPortReceiveThread;
    private SerialPortSendThread serialPortSendThread;

    public SerialPortWrapper(SerialPort serialPort) {
        this.serialPort = serialPort;
        this.serialPort.setNumDataBits(DEFAULT_NUMBER_OF_DATA_BITS);
    }

    public SerialPortSendThread initSendThread(SerialPortOnDataSendListener onDataSendListener) {
        if (serialPortSendThread == null) {
            serialPortSendThread = new SerialPortSendThread(serialPort, onDataSendListener);
        }

        return serialPortSendThread;
    }

    public SerialPortReceiveThread initReceiveThread(SerialPortOnDataReceivedListener inputListener) {
        if (serialPortReceiveThread == null) {
            serialPortReceiveThread = new SerialPortReceiveThread(serialPort, inputListener);
        }

        return serialPortReceiveThread;
    }

    public SerialPortReceiveThread getReceiveThread() {
        return serialPortReceiveThread;
    }

    public SerialPortSendThread getSendThread() {
        return serialPortSendThread;
    }

    public boolean setNumDataBits(int numDataBits) {
        return this.serialPort.setNumDataBits(numDataBits);
    }

    public int getNumDataBits() {
        return this.serialPort.getNumDataBits();
    }

    public String getName() {
        return serialPort.getSystemPortName();
    }
}
