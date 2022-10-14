package me.sa_g6.ui.widgets;

import me.sa_g6.ui.MainWindow;
import java.awt.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class FontColorMenuItem extends IMenuItem{
	public FontColorMenuItem(String text, Color color) {
		super(text, (e) -> {
            Tab tab = getTab();
            if (tab.editor == null) return;
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, color);
            tab.getEditor().setCharacterAttributes(attr, false);
        });
	}

}
