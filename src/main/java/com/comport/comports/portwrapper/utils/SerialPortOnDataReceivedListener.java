package com.comport.comports.portwrapper.utils;

import com.comport.comports.portwrapper.Packet;

public interface SerialPortOnDataReceivedListener {

    void onDataReceived(Packet packet);
}
