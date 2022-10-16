package com.comport.comports.portwrapper;

import com.comport.comports.portwrapper.utils.SerialPortInputStreamListener;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class SerialPortInputThread extends BasicSerialPortThread {

    private final SerialPortInputStreamListener inputListener;
    private final AtomicInteger bytesReceived = new AtomicInteger(0);

    public SerialPortInputThread(SerialPort serialPort, SerialPortInputStreamListener inputListener) {
        super(serialPort);
        this.inputListener = inputListener;
    }

    @Override
    protected void onOpen(SerialPort serialPort) {
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        try {
            InputStream inputStream = serialPort.getInputStream();
            while (true) {
                try {
                    int data = inputStream.read();
                    bytesReceived.incrementAndGet();
                    inputListener.onDataReceived(data);
                    sleep(1);
                } catch (InterruptedException e) {
                    break;
                }
            }
            inputStream.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    @Override
    protected void onClose(SerialPort serialPort) {
        System.out.println("Port " + serialPort.getSystemPortName() + " closed");
    }

    public int getBytesReceived() {
        return bytesReceived.get();
    }
}
