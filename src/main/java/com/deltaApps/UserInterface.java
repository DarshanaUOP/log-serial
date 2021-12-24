package com.deltaApps;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.*;

public class UserInterface extends JFrame{

    static SerialPort chosenPort;

    private JPanel backPanel, usbPanel, filePanel;
    private JComboBox listPorts;
    private JTextArea fileName;
    private JLabel btnConnect,btnRefresh,btnSelectFile;

    public UserInterface(){
        usbPanel = new JPanel(new FlowLayout());
        listPorts = new JComboBox();
        btnRefresh = new JLabel("Refresh");
        btnConnect = new JLabel("Connect");
        // add components to the usb panel
        usbPanel.add(listPorts);
        usbPanel.add(btnRefresh);
        usbPanel.add(btnConnect);

        // create file panel
        fileName = new JTextArea("File Select");
        btnSelectFile = new JLabel("Choose file");
        filePanel = new JPanel(new FlowLayout());
        filePanel.add(fileName);
        filePanel.add(btnSelectFile);

        //add components to the back panel
        backPanel = new JPanel();
        backPanel.add(usbPanel);
        backPanel.add(filePanel);

        add(backPanel);

        // call methods
        loadPortsAvailable();
    }

    public void loadPortsAvailable(){
        listPorts.removeAllItems();
        SerialPort[] portNames = SerialPort.getCommPorts();
        for (int i = 0; i < portNames.length; i++)
            listPorts.addItem(portNames[i].getSystemPortName() + "-" + portNames[i].getDescriptivePortName());

        if (portNames.length > 0) {
            btnConnect.setEnabled(true);
        } else {
            btnConnect.setEnabled(false);
        }
    }
}
