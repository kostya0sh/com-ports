package com.comport.comports;

import com.comport.comports.listener.CharInputListener;
import com.comport.comports.listener.CharOutputListener;
import com.comport.comports.listener.NumberOfDataBitsListener;
import com.comport.comports.portwrapper.PairChooser;
import com.comport.comports.portwrapper.SerialPortWrapper;
import com.comport.comports.portwrapper.utils.ByteUtils;
import com.comport.comports.portwrapper.utils.DataDecoder;
import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class COMPortsApplication extends Application {

    private SerialPortWrapper sendPortWrapper1;
    private SerialPortWrapper receivePortWrapper1;

    private SerialPortWrapper sendPortWrapper2;
    private SerialPortWrapper receivePortWrapper2;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(COMPortsApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("COM Ports");
        stage.setScene(scene);
        stage.show();

        initSerialPorts();

        COMPortsController comPortsController = fxmlLoader.getController();
        setupFirstPair(comPortsController);
        setupSecondPair(comPortsController);
    }

    private void setupFirstPair(COMPortsController comPortsController) {
        comPortsController.pairLabel1.setText(receivePortWrapper1.getName() + "->" + sendPortWrapper1.getName());

        CharOutputListener charOutputListener = new CharOutputListener(receivePortWrapper1, sendPortWrapper1,
                comPortsController.serialPort1Text2, comPortsController.pairLabel1);

        receivePortWrapper1.initReceiveThread(charOutputListener).start();
        sendPortWrapper1.initSendThread(data ->
                Platform.runLater(() ->  {
                    comPortsController.pairLabel12.getChildren().clear();
                    comPortsController.pairLabel12.getChildren().addAll(makeDataBytesText(data));
                })
        ).start();

        comPortsController.serialPort1Text1.textProperty().addListener(new CharInputListener(sendPortWrapper1));

        comPortsController.pairNumDataBits1.textProperty().setValue(
                String.valueOf(SerialPortWrapper.DEFAULT_NUMBER_OF_DATA_BITS)
        );
        NumberOfDataBitsListener numberOfDataBitsListener = new NumberOfDataBitsListener(receivePortWrapper1,
                sendPortWrapper1, comPortsController.pairNumDataBits1);
        comPortsController.pairNumDataBits1.textProperty().addListener(numberOfDataBitsListener);


        comPortsController.sendButton1.setOnAction(action -> {
            String data = comPortsController.serialPort1Text1.getText();
            sendPortWrapper1.getSendThread().send(data);
        });
    }

    private void setupSecondPair(COMPortsController comPortsController) {
        comPortsController.pairLabel2.setText(receivePortWrapper2.getName() + "->" + sendPortWrapper2.getName());

        CharOutputListener charOutputListener = new CharOutputListener(receivePortWrapper2, sendPortWrapper2,
                comPortsController.serialPort2Text1, comPortsController.pairLabel2);

        receivePortWrapper2.initReceiveThread(charOutputListener).start();
        sendPortWrapper2.initSendThread(data ->
                Platform.runLater(() -> {
                    comPortsController.pairLabel22.getChildren().clear();
                    comPortsController.pairLabel22.getChildren().addAll(makeDataBytesText(data));
                })
        ).start();

        comPortsController.serialPort2Text2.textProperty().addListener(new CharInputListener(sendPortWrapper2));

        comPortsController.pairNumDataBits2.textProperty().setValue(
                String.valueOf(SerialPortWrapper.DEFAULT_NUMBER_OF_DATA_BITS)
        );
        NumberOfDataBitsListener numberOfDataBitsListener = new NumberOfDataBitsListener(receivePortWrapper2,
                sendPortWrapper2, comPortsController.pairNumDataBits2);
        comPortsController.pairNumDataBits2.textProperty().addListener(numberOfDataBitsListener);

        comPortsController.sendButton2.setOnAction(action -> {
            String data = comPortsController.serialPort2Text2.getText();
            sendPortWrapper2.getSendThread().send(data);
        });
    }

    private List<Text> makeDataBytesText(byte[] data) {
        List<Integer> controlBytes = DataDecoder.controlBytesPositions(data);
        List<Integer> zeroBytes = DataDecoder.convertedBytesPositions(data);
        List<Text> output = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            Text byteText = new Text(ByteUtils.byteToInt(data[i]) + " ");
            if (controlBytes.contains(i)) {
                byteText.setFill(Color.RED);
            } else if (zeroBytes.contains(i)) {
                byteText.setFill(Color.GREEN);
            } else {
                byteText.setFill(Color.BLACK);
            }
            output.add(byteText);
        }

        return output;
    }

    private void initSerialPorts() {
        System.out.println("Available ports:");
        SerialPort[] ports = SerialPort.getCommPorts();
        Arrays.stream(ports)
                .forEach(p -> {
                    System.out.println(p.getSystemPortName());
                    System.out.println(p.getPortDescription());
                    System.out.println(p.getDescriptivePortName());
                    System.out.println(p.getPortLocation());
                    System.out.println(p.getSystemPortPath());
                    System.out.println("\n");
                });
        System.out.println("----------------------");

        // first ports pair
        Pair<SerialPort, SerialPort> portsPair1 = PairChooser.choose("COM1");
        if (portsPair1 == null) {
            throw new RuntimeException("Ports pair wasn't found");
        } else {
            System.out.println("Pair found: " +
                    portsPair1.getKey().getSystemPortName() +
                    "->" +
                    portsPair1.getValue().getSystemPortName());
        }
        sendPortWrapper1 = new SerialPortWrapper(portsPair1.getKey());
        receivePortWrapper1 = new SerialPortWrapper(portsPair1.getValue());

        // second ports pair
        Pair<SerialPort, SerialPort> portsPair2 = PairChooser.choose("COM3");
        if (portsPair2 == null) {
            throw new RuntimeException("Ports pair wasn't found");
        } else {
            System.out.println("Pair found: " +
                    portsPair2.getKey().getSystemPortName() +
                    "->" +
                    portsPair2.getValue().getSystemPortName());
        }
        sendPortWrapper2 = new SerialPortWrapper(portsPair2.getKey());
        receivePortWrapper2 = new SerialPortWrapper(portsPair2.getValue());
    }

    public static void main(String[] args) {
        launch();
    }
}