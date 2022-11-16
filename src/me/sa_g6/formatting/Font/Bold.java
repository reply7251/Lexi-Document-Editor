package me.sa_g6.formatting.Font;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Bold implements Font{
    public void perform(JTextPane editor){
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, !StyleConstants.isBold(
                editor.getStyledDocument().getCharacterElement(editor.getSelectionStart()).getAttributes()
        ));
        editor.setCharacterAttributes(attr, false);
    }
}

