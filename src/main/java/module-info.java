module com.comport.comports {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.fazecast.jSerialComm;

    opens com.comport.comports to javafx.fxml;
    exports com.comport.comports;
    exports com.comport.comports.portwrapper;
    exports com.comport.comports.portwrapper.utils;
    exports com.comport.comports.view;
    opens com.comport.comports.view to javafx.fxml;
    exports com.comport.comports.listener;
    opens com.comport.comports.listener to javafx.fxml;
}