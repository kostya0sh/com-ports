package com.comport.comports.portwrapper;

import com.comport.comports.portwrapper.utils.SerialPortInputStreamListener;
import com.fazecast.jSerialComm.SerialPort;

public class SerialPortWrapper {

    public static final int DEFAULT_NUMBER_OF_DATA_BITS = 8;

    private final SerialPort serialPort;

    private SerialPortInputThread serialPortInputThread;
    private SerialPortOutputThread serialPortOutputThread;

    public SerialPortWrapper(SerialPort serialPort) {
        this.serialPort = serialPort;
        this.serialPort.setNumDataBits(8);
    }

    public SerialPortOutputThread initOutputThread() {
        if (serialPortOutputThread == null) {
            serialPortOutputThread = new SerialPortOutputThread(serialPort);
        }

        return serialPortOutputThread;
    }

    public SerialPortInputThread initInputThread(SerialPortInputStreamListener inputListener) {
        if (serialPortInputThread == null) {
            serialPortInputThread = new SerialPortInputThread(serialPort, inputListener);
        }

        return serialPortInputThread;
    }

    public SerialPortInputThread getInputThread() {
        return serialPortInputThread;
    }

    public SerialPortOutputThread getOutputThread() {
        return serialPortOutputThread;
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
