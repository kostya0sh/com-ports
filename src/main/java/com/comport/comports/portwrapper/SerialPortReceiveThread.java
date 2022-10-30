package com.comport.comports.portwrapper;

import com.comport.comports.portwrapper.utils.ByteUtils;
import com.comport.comports.portwrapper.utils.DataDecoder;
import com.comport.comports.portwrapper.utils.SerialPortOnDataReceivedListener;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SerialPortReceiveThread extends BasicSerialPortThread {

    private final SerialPortOnDataReceivedListener inputListener;
    private final AtomicInteger bytesReceived = new AtomicInteger(0);

    public SerialPortReceiveThread(SerialPort serialPort, SerialPortOnDataReceivedListener inputListener) {
        super(serialPort);
        this.inputListener = inputListener;
    }

    @Override
    protected void onOpen(SerialPort serialPort) {
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        try {
            InputStream inputStream = serialPort.getInputStream();
            List<Integer> receivedByteValues = new ArrayList<>(256);
            while (true) {
                try {
                    int receivedByte = inputStream.read();
                    bytesReceived.incrementAndGet();
                    receivedByteValues.add(receivedByte);

                    if (receivedByte == DataDecoder.DELIMITER_BYTE) {
                        byte[] rawBytes = ByteUtils.intListToByteArray(receivedByteValues);
                        byte[] packetBytes = DataDecoder.decode(rawBytes);
                        Packet packet = Packet.from(packetBytes);
                        inputListener.onDataReceived(packet);
                        receivedByteValues.clear();
                    }

                    if (isInterrupted()) {
                        throw new InterruptedException();
                    }
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
