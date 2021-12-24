package com.deltaApps;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class UserInterface extends JFrame{

    static SerialPort choosenPort;

    private JFileChooser fileChooser = new JFileChooser();

    private JPanel backPanel, usbPanel, filePanel;
    private JComboBox listPorts;
    private JTextArea fileName;
    private JLabel btnConnect,btnRefresh,btnSelectFile;

    public UserInterface(){
        usbPanel = new JPanel(new FlowLayout());
        listPorts = new JComboBox();
        btnRefresh = new JLabel("Refresh");
        btnRefresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadPortsAvailable();
            }
        });
        btnConnect = new JLabel("Connect");
        btnConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                connectToThePort();
            }
        });
        // add components to the usb panel
        usbPanel.add(listPorts);
        usbPanel.add(btnRefresh);
        usbPanel.add(btnConnect);

        // create file panel
        fileName = new JTextArea("File Select");
        btnSelectFile = new JLabel("Choose file");
        btnSelectFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fileChooser.showSaveDialog(null);
                String fileNameDir = String.valueOf(fileChooser.getSelectedFile());
                fileName.setText(fileNameDir);
            }
        });
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

    public void connectToThePort(){
        if (btnConnect.getText().equals("Connect")) {
            btnConnect.setText("Connecting...");
            String port = listPorts.getSelectedItem().toString();

            try {
                choosenPort = SerialPort.getCommPort(port.split("-")[0]);
                choosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "Error in connect to Device \n" + e1.getMessage(), "Communication Error", JOptionPane.PLAIN_MESSAGE);
                btnConnect.setText("Connect");
            }
            if (choosenPort.openPort()) {
                btnConnect.setText("Disconnect");
//                lbGRAPHClear.setEnabled(true);
//                lbGRAPHRun.setEnabled(true);
                readData(true);
            } else {
                btnConnect.setText("Connect");
            }

        } else if (btnConnect.getText().equals("Disconnect")) {
            if (choosenPort.closePort()) {
                btnConnect.setText("Connect");
//                lbGRAPHRun.setEnabled(false);
//                lbGRAPHClear.setEnabled(false);
            }
        }
    }

    public void readData(boolean onOrOff){
        Thread thread = new Thread() {
            @Override
            public void run() {
                Scanner scanner = null;
                try {
                    scanner = new Scanner(choosenPort.getInputStream());
                } catch (Exception e) {
                    System.out.println("Scanner Exception : " + e.getMessage());
                    JOptionPane.showMessageDialog(null,"Scanner Exception : " + e.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
                }
                try{
                    while (scanner.hasNextLine()) {
                        String rec = scanner.nextLine();
//                        System.out.println(rec);
                        writeOnFile(rec);
                    }
                }catch (Exception e1){
                    System.out.println("Scanner Exception (No2) : " + e1.getMessage());
                }

            }
        };
        thread.start();
    }

    public void writeOnFile(String str){
        try {
            String file = fileName.getText();
            System.out.println(file);
            FileWriter fileWriter = new FileWriter(file,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            printWriter.print(str);
            printWriter.print('\n');
            printWriter.flush();
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
