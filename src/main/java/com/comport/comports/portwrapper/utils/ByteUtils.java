package com.comport.comports.portwrapper.utils;

import java.util.ArrayList;
import java.util.List;

public class ByteUtils {

    public static int byteToInt(byte b) {
        return b & 0xff;
    }

    public static byte intToByte(int i) {
        return (byte) i;
    }

    public static List<Byte> toByteList(byte[] bytes) {
        List<Byte> list = new ArrayList<>();
        for (byte aByte : bytes) {
            list.add(aByte);
        }

        return list;
    }

    public static byte[] byteListToByteArray(List<Byte> byteList) {
        byte[] output = new byte[byteList.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = byteList.get(i);
        }

        return output;
    }

    public static byte[] intListToByteArray(List<Integer> byteList) {
        byte[] output = new byte[byteList.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = byteList.get(i).byteValue();
        }

        return output;
    }

    public static String printBytes(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte aByte : bytes) {
            builder.append(byteToInt(aByte));
            builder.append(" ");
        }

        return builder.toString();
    }
}
