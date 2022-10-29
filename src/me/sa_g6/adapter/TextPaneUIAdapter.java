package me.sa_g6.adapter;

import me.sa_g6.adapter.ViewUpdater;

import javax.swing.plaf.basic.BasicTextPaneUI;

public class TextPaneUIAdapter extends BasicTextPaneUI implements ViewUpdater {
    @Override
    public void updateView() {
        modelChanged();
    }
}
