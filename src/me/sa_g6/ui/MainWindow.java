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
    private static MainWindow mw = null;
    JTabbedPane tabs = new JTabbedPane();
    JMenuBar menuBar = new JMenuBar();
    public final FileChooser fileChooser;

    private MainWindow(){
        super("Lexi Document Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(tabs);

        init();
        setSize(800,600);
        setLocationRelativeTo(null);
        fileChooser = new FileChooser(this);
    }

    void init(){
        //Tab tab1 = new Tab(true);
        addTab("debug", true);

        JMenu fileMenu = new JMenu("File");
        JMenuItem item = new JMenuItem("Open");
        fileMenu.add(item);

        IMenuItem save = new IMenuItem("Save", (e) -> {
            fileChooser.setLoadMode(false);
            showWindow(fileChooser);
            //BetterAction.saveDocument(getCurrentTab());
        });
        fileMenu.add(save);
        IMenuItem load = new IMenuItem("Load", (e) -> {
            fileChooser.setLoadMode(true);
            showWindow(fileChooser);
            //BetterAction.loadDocument(getCurrentTab(), 774); //327 382 412 434 456 478 500 530 552 580 610
        });
        fileMenu.add(load);
        IMenuItem newTab = new IMenuItem("New", (e) ->{
            int max = 0;
            for(int i = 0; i < tabs.getTabCount(); i++){
                String title = tabs.getTitleAt(i);
                if(title.startsWith("new ")){
                    try{
                        max = Math.max(max, Integer.parseInt(title.substring(4)));
                    }catch (NumberFormatException ignored){ }
                }
            }
            addTab("new " + (max+1));
        });
        fileMenu.add(newTab);

        menuBar.add(fileMenu);

        JMenu insert = new JMenu("Insert");
        JMenuItem insertPicture = new JMenuItem("Insert Picture");
        insert.add(insertPicture);
        menuBar.add(insert);

        JMenu formatMenu = new JMenu("Format");
        formatMenu.add(new AlignmentMenuItem("Align left", new LeftAlignment()));
        formatMenu.add(new AlignmentMenuItem("Align Right", new RightAlignment()));
        formatMenu.add(new AlignmentMenuItem("Align Center", new CenterAlignment()));
        menuBar.add(formatMenu);

        JMenu fontMenu = new JMenu("Font");
        fontMenu.add(new FontMenuItem("Bold", new Bold()));
        fontMenu.add(new FontMenuItem("italic", new Italic()));
        fontMenu.add(new FontMenuItem("underline", new Underline()));
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
        FontColorMenu.add(new FontColorMenuItem("Red",Color.red));
        FontColorMenu.add(new FontColorMenuItem("Green",Color.green));
        FontColorMenu.add(new FontColorMenuItem("Blue",Color.blue));
        FontColorMenu.add(new FontColorMenuItem("Black",Color.black));
        FontColorMenu.add(new FontColorMenuItem("White",Color.white));
        menuBar.add(FontColorMenu);
        
        JMenu BackgroundColorMenu = new JMenu("Background Color");
        BackgroundColorMenu.add(new BackgroundColorMenuItem("Red",Color.red));
        BackgroundColorMenu.add(new BackgroundColorMenuItem("Green",Color.green));
        BackgroundColorMenu.add(new BackgroundColorMenuItem("Blue",Color.blue));
        BackgroundColorMenu.add(new BackgroundColorMenuItem("Yellow",Color.yellow));
        BackgroundColorMenu.add(new BackgroundColorMenuItem("Orange",Color.orange));
        menuBar.add(BackgroundColorMenu);

        JMenu DisplayModeMenu = new JMenu("DisplayMode");
        DisplayModeMenu.add(new DisplayModeMenuItem("FULL",new FullDisplayMode()));
        DisplayModeMenu.add(new DisplayModeMenuItem("TextOnly",new TextOnlyDisplayMode()));
        menuBar.add(DisplayModeMenu);
        
        setJMenuBar(menuBar);

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
                        BetterAction.insertHtml(getCurrentTab().getEditor(), getCurrentTab().getEditor().getCaretPosition(), str2);
                    } catch (Exception ex) {
                        showMessageDialog(null, "Unable to insert file");
                        ex.printStackTrace();
                    }
                } else {
                }
            }
        });
        insertPicture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser2 = new JFileChooser();
                fileChooser2.setCurrentDirectory(new File(System.getProperty("user.home")));
                int i = fileChooser2.showSaveDialog(null);
                if (i == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser2.getSelectedFile();
                    try {
                        BufferedImage image = ImageIO.read((file)); //new BufferedImage(filepath);
                        ImageIO.write(image,"png", file);
                            BetterAction.insertImage(getCurrentTab().getEditor(), getCurrentTab().getEditor().getCaretPosition(), image);
                    } catch (IOException ex) {
                        showMessageDialog(null, "Unable to insert picture");
                        ex.printStackTrace(); //throw new RuntimeException(ex);
                    }
                } else {
                }
            }
        });
    }

    public Tab getCurrentTab(){
        return (Tab) tabs.getSelectedComponent();
    }

    public static MainWindow getInstance(){
        if(mw == null) mw = new MainWindow();
        return mw;
    }

    public void addTab(String name){
        addTab(name, false);
    }

    public void addTab(String name, boolean debug){
        tabs.addTab(name, new Tab(debug));
        tabs.setTabComponentAt(tabs.getTabCount()-1, new ButtonTabComponent(tabs));
        tabs.setSelectedIndex(tabs.getTabCount()-1);
    }

    public JTabbedPane getTabs(){
        return tabs;
    }

    public void showWindow(Window window){
        SwingUtilities.invokeLater(()->{
            window.setVisible(true);
        });
    }
}