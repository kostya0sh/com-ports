package com.comport.comports.portwrapper.utils;

import java.util.ArrayList;
import java.util.List;

public class DataDecoder {

    public static final int DELIMITER_BYTE = 0x00;
    private static final int UNSIGNED_BYTE_SIZE = 256;

    public static byte[] decode(byte[] data) {
        byte[] tmp = data.clone();
        int i = 0;
        while (true) {
            int nextZero = ByteUtils.byteToInt(tmp[i]);
            if (nextZero == 0) {
                break;
            }

            tmp[i] = 0x00;
            i = i + nextZero;
        }

        List<Integer> indicesToDelete = controlBytesPositions(tmp);
        byte[] decoded = new byte[tmp.length - indicesToDelete.size()];
        for (int k = 0, n = 0; k < tmp.length; k++) {
            if (indicesToDelete.contains(k)) {
                continue;
            }

            decoded[n] = tmp[k];
            n++;
        }

        return decoded;
    }

    public static List<Integer> controlBytesPositions(byte[] data) {
        List<Integer> positions = new ArrayList<>();
        positions.add(data.length - 1);
        int j = 0;
        do {
            positions.add(j);
            j += UNSIGNED_BYTE_SIZE - 1;
        } while (j <= data.length - 1);

        return positions;
    }

    public static List<Integer> convertedBytesPositions(byte[] data) {
        List<Integer> controlBytesPositions = controlBytesPositions(data);

        List<Integer> convertedBytesPositions = new ArrayList<>();
        int i = 0;
        while (true) {
            int nextZero = ByteUtils.byteToInt(data[i]);
            if (nextZero == 0) {
                break;
            }

            convertedBytesPositions.add(i);
            i = i + nextZero;
        }

        return convertedBytesPositions.stream()
                .filter(pos -> !controlBytesPositions.contains(pos))
                .toList();
    }
}
