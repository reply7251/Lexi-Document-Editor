package me.sa_g6.ui;

import me.sa_g6.formatting.Alignment;
import me.sa_g6.formatting.CenterAlignment;
import me.sa_g6.formatting.LeftAlignment;
import me.sa_g6.formatting.RightAlignment;
import me.sa_g6.ui.widgets.Tab;

import javax.swing.*;
import javax.swing.text.*;

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

        JMenu menu = new JMenu("File");
        JMenuItem item = new JMenuItem("Open...");
        menu.add(item);
        menuBar.add(menu);
        JMenu formatMenu = new JMenu("Format");
        JMenuItem alignLeft = new JMenuItem("Align left");
        Alignment leftAlignment = new LeftAlignment();
        alignLeft.addActionListener((e)->{
            if(tabs.getSelectedComponent() instanceof Tab tab){
                leftAlignment.perform(tab.getEditor().getStyledDocument());
            }
        });
        formatMenu.add(alignLeft);
        JMenuItem alignRight = new JMenuItem("Align Right");
        Alignment rightAlignment = new RightAlignment();
        alignRight.addActionListener((e)->{
            if(tabs.getSelectedComponent() instanceof Tab tab){
                rightAlignment.perform(tab.getEditor().getStyledDocument());
            }
        });
        formatMenu.add(alignRight);
        JMenuItem alignCenter = new JMenuItem("Align Center");
        Alignment centerAlignment = new CenterAlignment();
        alignCenter.addActionListener((e)->{
            if(tabs.getSelectedComponent() instanceof Tab tab){
                centerAlignment.perform(tab.getEditor().getStyledDocument());
            }
        });
        formatMenu.add(alignCenter);

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
}


/*
* http://java-sl.com/JEditorPaneTables.html
* http://java-sl.com/src/JEditorPaneTables_src.html
*
*
*
* */