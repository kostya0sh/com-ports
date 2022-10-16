package com.comport.comports.portwrapper;

import com.fazecast.jSerialComm.SerialPort;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PairChooser {

    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("\\((.*?)\\)");

    public static Pair<SerialPort, SerialPort> choose(String comPortName) {
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports == null) {
            return null;
        }

        return findByName(comPortName)
                .map(p -> {
                    Matcher matcher = DESCRIPTION_PATTERN.matcher(p.getDescriptivePortName());
                    boolean found = matcher.find();
                    if (!found) {
                        throw new RuntimeException("Port description pattern not found");
                    }

                    String pairInfo = matcher.group(1);
                    String[] pairInfoParts = pairInfo.split("->");
                    String pairedSystemPortName;
                    if (pairInfoParts[0].equals(p.getSystemPortName())) {
                        pairedSystemPortName = pairInfoParts[1];
                    } else {
                        pairedSystemPortName = pairInfoParts[0];
                    }

                    SerialPort pairedPort = findByName(pairedSystemPortName).orElseThrow();

                    return new Pair<>(p, pairedPort);
                })
                .orElseThrow();
    }

    public static Optional<SerialPort> findByName(String name) {
        if (name == null) {
            return Optional.empty();
        }

        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports != null) {
            return Arrays.stream(ports)
                    .filter(p -> name.equals(p.getSystemPortName()))
                    .findFirst();
        } else {
            return Optional.empty();
        }
    }
}
