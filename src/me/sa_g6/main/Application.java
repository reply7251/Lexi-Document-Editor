package me.sa_g6.main;

import me.sa_g6.ui.MainWindow;

import javax.swing.*;

public class Application {
    public static void main(String[] args){

        SwingUtilities.invokeLater(()->{
            JFrame mw = new MainWindow();
            mw.setVisible(true);
        });
    }
}
