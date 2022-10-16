package com.comport.comports.portwrapper;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.SynchronousQueue;

public class SerialPortOutputThread extends BasicSerialPortThread {

    private final SynchronousQueue<Character> sendQueue = new SynchronousQueue<>();

    public SerialPortOutputThread(SerialPort serialPort) {
        super(serialPort);
    }

    @Override
    protected void onOpen(SerialPort serialPort) {
        try {
            OutputStream outputStream = serialPort.getOutputStream();
            while (true) {
                try {
                    Character dataToSend = sendQueue.take();
                    System.out.println("Data to send: " + dataToSend);
                    outputStream.write(dataToSend);
                } catch (InterruptedException ie) {
                    break;
                }
            }
            outputStream.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    @Override
    protected void onClose(SerialPort serialPort) {
        System.out.println("Port " + serialPort.getSystemPortName() + " closed");
    }

    public void send(Character c) {
        try {
            sendQueue.put(c);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
