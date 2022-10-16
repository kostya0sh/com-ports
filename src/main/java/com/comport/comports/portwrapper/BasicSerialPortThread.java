package com.comport.comports.portwrapper;

import com.fazecast.jSerialComm.SerialPort;

public abstract class BasicSerialPortThread extends Thread {

    private final SerialPort serialPort;

    public BasicSerialPortThread(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    protected abstract void onOpen(SerialPort serialPort);

    protected abstract void onClose(SerialPort serialPort);

    @Override
    public void run() {
        if (!serialPort.isOpen()) {
            boolean opened = serialPort.openPort();
            if (!opened) {
                throw new RuntimeException("Can not open port");
            }
        }

        onOpen(serialPort);

        while (true) {
            boolean closed = serialPort.closePort();
            if (!closed) {
                try {
                    sleep(100);
                    continue;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            onClose(serialPort);

            return;
        }
    }
}
