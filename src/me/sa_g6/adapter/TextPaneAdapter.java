package me.sa_g6.adapter;

import javax.swing.*;

public class TextPaneAdapter extends JTextPane implements ViewUpdater {

    @Override
    public void updateView() {
        if(ui instanceof ViewUpdater eUI){
            eUI.updateView();
        }else{
            ui = new TextPaneUIAdapter();
            ui.installUI(this);
            ((ViewUpdater)ui).updateView();
        }
    }
}
