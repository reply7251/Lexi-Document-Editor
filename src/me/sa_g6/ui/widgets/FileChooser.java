package me.sa_g6.ui.widgets;

import me.sa_g6.ui.MainWindow;
import me.sa_g6.utils.BetterAction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileChooser extends JDialog {
    JTable table = new JTable(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    JButton jButton;
    JTextField searchBar;
    DefaultTableModel model;
    boolean loadMode = false;

    public FileChooser(JFrame parent){
        super(parent, true);
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"id", "filename"});
        setSize(500,600);
        GroupLayout layout = new GroupLayout(this.getContentPane());
        getContentPane().setLayout(layout);

        //table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        table.setModel(model);
        JScrollPane tablePane = new JScrollPane(table);

        table.getSelectionModel().addListSelectionListener(e ->{
            if(e.getValueIsAdjusting() || table.getSelectionModel().isSelectionEmpty()) {
                return;
            }
            int[] rows = table.getSelectedRows();
            if(rows.length == 1){
                searchBar.setText((String) model.getValueAt(rows[0], 1));
            }
        });

        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                    List<Integer> rows = Arrays.stream(table.getSelectedRows()).boxed().sorted(Collections.reverseOrder()).toList();
                    if(rows.size() == 0){
                        return;
                    }
                    BetterAction.dbManager.begin();
                    for(int row : rows){
                        long id = (long) model.getValueAt(row, 0);
                        BetterAction.dbManager.removeHtml(id);
                        model.removeRow(row);
                    }
                    BetterAction.dbManager.commit();
                }
            }
        });

        searchBar = new JTextField("");
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    searchByName();
                    return;
                }
                super.keyReleased(e);
            }
        });
        jButton = new JButton("done");

        jButton.addActionListener(e -> {
            if(loadMode){
                int[] rows = table.getSelectedRows();
                if(rows.length == 0){
                    return;
                }
                for(int row : table.getSelectedRows()){
                    long id = (long) model.getValueAt(row, 0);
                    String name = (String) model.getValueAt(row, 1);
                    MainWindow.getInstance().addTab(name);
                    BetterAction.loadDocument(MainWindow.getInstance().getCurrentTab(), id);
                }
                setVisible(false);
            }else{
                if(searchBar.getText().isEmpty()){
                    return;
                }
                BetterAction.saveDocument(MainWindow.getInstance().getCurrentTab(), searchBar.getText());
                setVisible(false);
            }
        });
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(tablePane).addGap(5)
                        .addGroup(
                                layout.createParallelGroup()
                                        .addComponent(searchBar,30,30,30)
                                        .addComponent(jButton,30,30,30)
                        )
        );
        layout.setHorizontalGroup(
                layout.createParallelGroup().addComponent(tablePane)
                        .addGroup(
                                layout.createSequentialGroup()
                                        .addComponent(searchBar,300,400,1000)
                                        .addGap(1)
                                        .addComponent(jButton,50,100,150)
                        )
        );
    }

    @Override
    public void setVisible(boolean b) {
        if(b){
            searchBar.setText("");
            table.getSelectionModel().clearSelection();
            searchByName();
            if(!loadMode){
                JTabbedPane tabs = MainWindow.getInstance().getTabs();
                searchBar.setText(tabs.getTitleAt(tabs.getSelectedIndex()));
            }
        }
        super.setVisible(b);
    }

    public void setLoadMode(boolean mode){
        loadMode = mode;
        if(loadMode){
            jButton.setText("Load");
            table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }else{
            jButton.setText("Save");
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
    }

    public void searchByName(){
        model.setRowCount(0);

        BetterAction.dbManager.listDocument().forEach(adapter -> {
            if(adapter.getName() == null || adapter.getName().contains(searchBar.getText())){
                model.addRow(adapter.toTableRow());
            }
        });
    }
}
