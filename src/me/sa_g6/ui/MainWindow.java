package me.sa_g6.ui;

import me.sa_g6.formatting.Alignment;
import me.sa_g6.formatting.CenterAlignment;
import me.sa_g6.formatting.LeftAlignment;
import me.sa_g6.formatting.RightAlignment;
import me.sa_g6.ui.widgets.AlignmentMenuItem;
import me.sa_g6.ui.widgets.Tab;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

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

        //tab1.getEditor().setContentType("text/html");
        TableDocument doc = (TableDocument) tab1.getEditor().getDocument();
        doc.insertTable(0, 2, new int[] {200, 100, 150});
        doc.insertTable(4, 2, new int[] {100, 50});

        GlyphView view;


        try {
            doc.insertString(10, "Paragraph after table.\nYou can set caret in table cell and start typing.", null);
            doc.insertString(4, "Inner Table", null);
            doc.insertString(3, "Cell with a nested table", null);
            doc.insertString(0, "Table\nCell", null);

            //final SimpleAttributeSet attrs=new SimpleAttributeSet();
            //ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("images/logo-ms-social.png"));
            //StyleConstants.setIcon(attrs, icon);
            //doc.insertString(9,")", attrs);


            //tab1.getEditor().insertIcon(new ImageIcon(ClassLoader.getSystemResource("images/logo-ms-social.png")));


        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public Component getCurrentTab(){
        return tabs.getSelectedComponent();
    }
}


/*
* http://java-sl.com/JEditorPaneTables.html
* http://java-sl.com/src/JEditorPaneTables_src.html
*
*
*
* */