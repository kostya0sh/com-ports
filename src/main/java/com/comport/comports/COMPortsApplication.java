package com.comport.comports;

import com.comport.comports.listener.CharInputListener;
import com.comport.comports.listener.CharOutputListener;
import com.comport.comports.listener.NumberOfDataBitsListener;
import com.comport.comports.portwrapper.PairChooser;
import com.comport.comports.portwrapper.SerialPortWrapper;
import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Arrays;

public class COMPortsApplication extends Application {

    private SerialPortWrapper outputPortWrapper1;
    private SerialPortWrapper inputPortWrapper1;

    private SerialPortWrapper outputPortWrapper2;
    private SerialPortWrapper inputPortWrapper2;

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
        comPortsController.pairLabel1.setText(inputPortWrapper1.getName() + "->" + outputPortWrapper1.getName());

        CharOutputListener charOutputListener = new CharOutputListener(inputPortWrapper1, outputPortWrapper1,
                comPortsController.serialPortInput1, comPortsController.pairLabel1);

        inputPortWrapper1.initInputThread(charOutputListener).start();
        outputPortWrapper1.initOutputThread().start();

        comPortsController.serialPortOutput1.textProperty().addListener(new CharInputListener(outputPortWrapper1));

        comPortsController.pairNumDataBits1.textProperty().setValue(
                String.valueOf(SerialPortWrapper.DEFAULT_NUMBER_OF_DATA_BITS)
        );
        NumberOfDataBitsListener numberOfDataBitsListener = new NumberOfDataBitsListener(inputPortWrapper1,
                outputPortWrapper1, comPortsController.pairNumDataBits1);
        comPortsController.pairNumDataBits1.textProperty().addListener(numberOfDataBitsListener);
    }

    private void setupSecondPair(COMPortsController comPortsController) {
        comPortsController.pairLabel2.setText(inputPortWrapper2.getName() + "->" + outputPortWrapper2.getName());

        CharOutputListener charOutputListener = new CharOutputListener(inputPortWrapper2, outputPortWrapper2,
                comPortsController.serialPortInput2, comPortsController.pairLabel2);

        inputPortWrapper2.initInputThread(charOutputListener).start();
        outputPortWrapper2.initOutputThread().start();

        comPortsController.serialPortOutput2.textProperty().addListener(new CharInputListener(outputPortWrapper2));

        comPortsController.pairNumDataBits2.textProperty().setValue(
                String.valueOf(SerialPortWrapper.DEFAULT_NUMBER_OF_DATA_BITS)
        );
        NumberOfDataBitsListener numberOfDataBitsListener = new NumberOfDataBitsListener(inputPortWrapper2,
                outputPortWrapper2, comPortsController.pairNumDataBits2);
        comPortsController.pairNumDataBits2.textProperty().addListener(numberOfDataBitsListener);
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
        outputPortWrapper1 = new SerialPortWrapper(portsPair1.getKey());
        inputPortWrapper1 = new SerialPortWrapper(portsPair1.getValue());

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
        outputPortWrapper2 = new SerialPortWrapper(portsPair2.getKey());
        inputPortWrapper2 = new SerialPortWrapper(portsPair2.getValue());
    }

    public static void main(String[] args) {
        launch();
    }
}