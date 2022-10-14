package me.sa_g6.ui;

import me.sa_g6.formatting.*;
import me.sa_g6.formatting.Font.Bold;
import me.sa_g6.formatting.Font.Italic;
import me.sa_g6.formatting.Font.Underline;
import me.sa_g6.ui.widgets.*;
//import me.sa_g6.ui.widgets.EditMenuItem;
import me.sa_g6.utils.BetterAction;
import me.sa_g6.utils.Prov;

import java.awt.event.KeyEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.ServiceLoader;

import static javax.swing.JOptionPane.showMessageDialog;

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
        JMenuItem item = new JMenuItem("Open");
        JMenuItem item2 = new JMenuItem("Insert Picture");
        fileMenu.add(item);
        fileMenu.add(item2);
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

        JMenu DisplayModeMenu = new JMenu("DisplayMode");
        DisplayModeMenu.add(new DisplayModeMenuItem(this,"FULL",new FullDisplayMode()));
        DisplayModeMenu.add(new DisplayModeMenuItem(this,"TextOnly",new TextOnlyDisplayMode()));
        menuBar.add(DisplayModeMenu);
        
        setJMenuBar(menuBar);

        tab1.insertTable(tab1.getEditor().getCaretPosition(),2,3);

        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File((System.getProperty("user.home"))));
                int d = fileChooser.showSaveDialog(null);
                if (d == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String filepath = file.getPath();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));
                        String str1 = "", str2 = "";
                        while ((str1 = bufferedReader.readLine())!= null) {
                            str2 = str2 + str1 + "\n";
                        }
                        if(getCurrentTab() instanceof Tab tab){
                            BetterAction.insertHtml(tab.getEditor(), tab.getEditor().getCaretPosition(), str2);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    showMessageDialog(null, "Unable to insert file");
                }
            }
        });
        item2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser2 = new JFileChooser();
                fileChooser2.setCurrentDirectory(new File(System.getProperty("user.home")));
                int i = fileChooser2.showSaveDialog(null);
                if (i == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser2.getSelectedFile();
                    try {
                        BufferedImage image = ImageIO.read((file)); //new BufferedImage(filepath);
                        ImageIO.write(image,"png", file);
                        if(getCurrentTab() instanceof Tab tab){
                            BetterAction.insertImage(tab.getEditor(), tab.getEditor().getCaretPosition(), image);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace(); //throw new RuntimeException(ex);
                    }
                } else {
                    showMessageDialog(null, "Unable to insert picture");
                }
            }
        });
    }

    public Component getCurrentTab(){
        return tabs.getSelectedComponent();
    }
}