package com.comport.comports.portwrapper;

import java.nio.charset.StandardCharsets;

public class Packet {

    private static final int OFFSET_FLAG = 0;
    private static final int OFFSET_DESTINATION_ADDRESS = 1;
    private static final int OFFSET_SOURCE_ADDRESS = 2;
    private static final int OFFSET_DATA = 3;

    private byte flag;
    private byte destinationAddress;
    private byte sourceAddress;
    private byte[] data;
    private byte fcs;

    public static Packet from(byte[] packetBytes) {
        Packet packet = new Packet();
        packet.flag = packetBytes[OFFSET_FLAG];
        packet.destinationAddress = packetBytes[OFFSET_DESTINATION_ADDRESS];
        packet.sourceAddress = packetBytes[OFFSET_SOURCE_ADDRESS];

        byte[] data = new byte[packetBytes.length - 4];
        for (int i = 0, j = OFFSET_DATA; i < data.length; i++, j++) {
            data[i] = packetBytes[j];
        }
        packet.data = data;

        packet.fcs = packetBytes[packetBytes.length - 1];

        return packet;
    }

    private Packet() {

    }

    public Packet(byte[] data, int port) {
        this.flag = (byte) ('a' + 1);
        this.destinationAddress = 0x00;
        this.sourceAddress = (byte) port;
        this.data = data;
        this.fcs = 0x00;
    }

    public byte[] asByteArray() {
        byte[] output = new byte[data.length + 4];

        output[OFFSET_FLAG] = flag;
        output[OFFSET_DESTINATION_ADDRESS] = destinationAddress;
        output[OFFSET_SOURCE_ADDRESS] = sourceAddress;
        for (int i = 0, j = OFFSET_DATA; i < data.length; i++, j++) {
            output[j] = data[i];
        }
        output[output.length - 1] = fcs;

        return output;
    }

    public byte getFlag() {
        return flag;
    }

    public byte getDestinationAddress() {
        return destinationAddress;
    }

    public byte getSourceAddress() {
        return sourceAddress;
    }

    public byte[] getData() {
        return data;
    }

    public byte getFcs() {
        return fcs;
    }

    @Override
    public String toString() {
        return new String(data, StandardCharsets.UTF_8);
    }
}
