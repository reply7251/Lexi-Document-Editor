package me.sa_g6.ui.widgets;

import me.sa_g6.ui.MainWindow;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;


public class BackgroundColorMenuItem extends IMenuItem {

    public BackgroundColorMenuItem(MainWindow mw, String text, Color color) {
        super(text, (e) -> {
            if (mw.getCurrentTab() instanceof Tab tab) {
                if (tab.editor == null) return;
                SimpleAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setBackground(attr, color);
                tab.getEditor().setCharacterAttributes(attr, false);
            }
        });

    }
}
