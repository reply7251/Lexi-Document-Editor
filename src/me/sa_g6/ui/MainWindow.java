package me.sa_g6.ui;

import me.sa_g6.formatting.CenterAlignment;
import me.sa_g6.formatting.Font.Bold;
import me.sa_g6.formatting.Font.Italic;
import me.sa_g6.formatting.Font.Underline;
import me.sa_g6.formatting.LeftAlignment;
import me.sa_g6.formatting.RightAlignment;
import me.sa_g6.ui.widgets.AlignmentMenuItem;
import me.sa_g6.ui.widgets.BackgroundColorMenuItem;
import me.sa_g6.ui.widgets.FontColorMenuItem;
//import me.sa_g6.ui.widgets.EditMenuItem;
import me.sa_g6.ui.widgets.FontMenuItem;
import me.sa_g6.ui.widgets.Tab;
import me.sa_g6.utils.BetterAction;
import me.sa_g6.utils.Prov;

import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.ServiceLoader;

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
        Tab tab1 = new Tab(true);
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

        JMenu fontMenu = new JMenu("Font");
        fontMenu.add(new FontMenuItem(this,"Bold", new Bold()));
        fontMenu.add(new FontMenuItem(this,"italic", new Italic()));
        fontMenu.add(new FontMenuItem(this,"underline", new Underline()));
        menuBar.add(fontMenu);


        Prov<JTextPane> prov = ()-> ((Tab)tabs.getSelectedComponent()).getEditor();

        JMenuItem menuItem ;
        JMenu editmenu = new JMenu("Edit");
        editmenu.setMnemonic(KeyEvent.VK_E);

        menuItem = new JMenuItem(new DefaultEditorKit.CutAction());
        menuItem.setText("Cut");
        menuItem.setMnemonic(KeyEvent.VK_T);
        editmenu.add(menuItem);

        menuItem = new JMenuItem(new BetterAction.CopyAction(prov));
        menuItem.setText("Copy");
        menuItem.setMnemonic(KeyEvent.VK_C);
        editmenu.add(menuItem);

        menuItem = new JMenuItem(new BetterAction.PasteAction(prov));
        menuItem.setText("Paste");
        menuItem.setMnemonic(KeyEvent.VK_P);
        editmenu.add(menuItem);
        menuBar.add(editmenu);

        JMenu FontColorMenu = new JMenu("Font color");
        FontColorMenu.add(new FontColorMenuItem(this,"Red",Color.red));
        FontColorMenu.add(new FontColorMenuItem(this,"Green",Color.green));
        FontColorMenu.add(new FontColorMenuItem(this,"Blue",Color.blue));
        FontColorMenu.add(new FontColorMenuItem(this,"Black",Color.black));
        FontColorMenu.add(new FontColorMenuItem(this,"White",Color.white));
        menuBar.add(FontColorMenu);
        
        JMenu BackgroundColorMenu = new JMenu("Background Color");
        BackgroundColorMenu.add(new BackgroundColorMenuItem(this,"Red",Color.red));
        BackgroundColorMenu.add(new BackgroundColorMenuItem(this,"Green",Color.green));
        BackgroundColorMenu.add(new BackgroundColorMenuItem(this,"Blue",Color.blue));
        BackgroundColorMenu.add(new BackgroundColorMenuItem(this,"Yellow",Color.yellow));
        BackgroundColorMenu.add(new BackgroundColorMenuItem(this,"Orange",Color.orange));
        menuBar.add(BackgroundColorMenu);
        
        setJMenuBar(menuBar);

        tab1.insertTable(tab1.getEditor().getCaretPosition(),2,3);
    }

    public Component getCurrentTab(){
        return tabs.getSelectedComponent();
    }
}