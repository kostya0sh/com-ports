package com.comport.comports;

import com.comport.comports.view.NumberTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class COMPortsController {

    @FXML
    public TextArea serialPortOutput1;
    @FXML
    public TextArea serialPortInput1;
    @FXML
    public TextArea serialPortInput2;
    @FXML
    public TextArea serialPortOutput2;
    @FXML
    public Label pairLabel1;
    @FXML
    public Label pairLabel2;
    @FXML
    public NumberTextField pairNumDataBits1;
    @FXML
    public NumberTextField pairNumDataBits2;
}