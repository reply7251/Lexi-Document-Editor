package me.sa_g6.ui;

import me.sa_g6.formatting.CenterAlignment;
import me.sa_g6.formatting.LeftAlignment;
import me.sa_g6.formatting.RightAlignment;
import me.sa_g6.ui.widgets.AlignmentMenuItem;
import me.sa_g6.ui.widgets.Tab;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.IOException;

public class MainWindow extends JFrame {
    JTabbedPane tabs = new JTabbedPane();
    JMenuBar menuBar = new JMenuBar();
    public MainWindow(){
        super("Lexi Document Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(tabs);

        init();
        setSize(800,600);
        setLocationRelativeTo(null);
    }

    void init(){
        Tab tab1 = new Tab();
        tabs.add("new 1", tab1);
        tabs.add("new 2", new Tab());

        JMenu fileMenu = new JMenu("File");
        JMenuItem item = new JMenuItem("Open...");
        fileMenu.add(item);
        menuBar.add(fileMenu);

        JMenu formatMenu = new JMenu("Format");
        formatMenu.add(new AlignmentMenuItem(this,"Align left", new LeftAlignment()));
        formatMenu.add(new AlignmentMenuItem(this,"Align Right", new RightAlignment()));
        formatMenu.add(new AlignmentMenuItem(this,"Align Center", new CenterAlignment()));
        menuBar.add(formatMenu);

        setJMenuBar(menuBar);

        tab1.insertTable(tab1.getEditor().getCaretPosition(),2,3);
    }

    public Component getCurrentTab(){
        return tabs.getSelectedComponent();
    }
}