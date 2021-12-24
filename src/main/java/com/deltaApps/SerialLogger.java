package com.deltaApps;

import javax.swing.*;

public class SerialLogger {
    public static void main(String[] args) {
        UserInterface userInterface = new UserInterface();
        userInterface.setSize(650,100);
        userInterface.setTitle("USB Serial logger");
        userInterface.setVisible(true);
        userInterface.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
