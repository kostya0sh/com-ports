package com.comport.comports.portwrapper.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DataEncoder {

    private static final int MAX_DATA_BYTES = 254;

    public static byte[] encode(byte[] data) {
        List<List<Byte>> parts = split(data);

        for (int i = 0; i < parts.size(); i++) {
            List<Byte> partBytes = parts.get(i);
            partBytes.add((byte) 0x00);

            if (i == 0) {
                byte firstByte = (byte) (encodeZero(0, partBytes) + 1);
                partBytes.add(0, firstByte);
            }
        }

        List<Byte> merged = parts.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        int iterator = ByteUtils.byteToInt(merged.get(0));
        while (true) {
            byte encodedZero = encodeZero(iterator, merged);
            if (encodedZero == 0) {
                break;
            }

            merged.set(iterator, encodedZero);
            iterator += encodedZero;
        }


        return ByteUtils.byteListToByteArray(merged);
    }

    private static byte encodeZero(int zeroIndex, List<Byte> data) {
        int i = 1;
        int ub;
        do  {
            if (zeroIndex + i > data.size() - 1) {
                break;
            }

            ub = ByteUtils.byteToInt(data.get(zeroIndex + i));
            i++;
        } while (ub != 0);

        return (byte) (i - 1);
    }

    private static List<List<Byte>> split(byte[] data) {
        List<List<Byte>> parts = new ArrayList<>();
        for (int i = 0; i < data.length; i += MAX_DATA_BYTES) {
            int endIndex = data.length > i + MAX_DATA_BYTES ? MAX_DATA_BYTES : data.length;
            byte[] tmp = Arrays.copyOfRange(data, i, endIndex);
            List<Byte> partBytes = ByteUtils.toByteList(tmp);
            parts.add(partBytes);
        }

        return parts;
    }
}
