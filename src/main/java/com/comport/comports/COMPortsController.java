package com.comport.comports;

import com.comport.comports.view.NumberTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextFlow;

public class COMPortsController {

    @FXML
    public TextArea serialPort1Text1;
    @FXML
    public TextArea serialPort1Text2;
    @FXML
    public TextArea serialPort2Text1;
    @FXML
    public TextArea serialPort2Text2;
    @FXML
    public Label pairLabel1;
    @FXML
    public Label pairLabel2;
    @FXML
    public NumberTextField pairNumDataBits1;
    @FXML
    public NumberTextField pairNumDataBits2;
    @FXML
    public Button sendButton1;
    @FXML
    public Button sendButton2;
    @FXML
    public TextFlow pairLabel12;
    @FXML
    public TextFlow pairLabel22;
}