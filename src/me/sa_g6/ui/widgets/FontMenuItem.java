package me.sa_g6.ui.widgets;

import me.sa_g6.formatting.Font.Font;
import me.sa_g6.ui.MainWindow;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class FontMenuItem extends IMenuItem{
    public FontMenuItem(String text, Font font) {
        super(text, (e)->{
            font.perform(getTab().getEditor());
        });
    }
}

