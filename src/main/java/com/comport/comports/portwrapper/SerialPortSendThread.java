package com.comport.comports.portwrapper;

import com.comport.comports.portwrapper.utils.DataEncoder;
import com.comport.comports.portwrapper.utils.SerialPortOnDataSendListener;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.SynchronousQueue;

public class SerialPortSendThread extends BasicSerialPortThread {

    private final SynchronousQueue<String> sendQueue = new SynchronousQueue<>();
    private final SerialPortOnDataSendListener onDataSendListener;

    public SerialPortSendThread(SerialPort serialPort, SerialPortOnDataSendListener onDataSendListener) {
        super(serialPort);
        this.onDataSendListener = onDataSendListener;
    }

    @Override
    protected void onOpen(SerialPort serialPort) {
        try {
            OutputStream outputStream = serialPort.getOutputStream();
            int serialPortNum = Integer.parseInt(serialPort.getSystemPortName().substring(3));
            while (true) {
                try {
                    String dataToSend = sendQueue.take();
                    System.out.println("Data to send: " + dataToSend);
                    Packet packet = new Packet(dataToSend.getBytes(StandardCharsets.UTF_8), serialPortNum);
                    byte[] encodedData = DataEncoder.encode(packet.asByteArray());
                    onDataSendListener.onDataSend(encodedData);
                    outputStream.write(encodedData);
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

    public void send(String data) {
        try {
            sendQueue.put(data);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
