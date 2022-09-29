package me.sa_g6.formatting;

import javax.swing.*;
import javax.swing.text.*;

public class LeftAlignment implements Alignment {
    public void perform(JTextPane editor){
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
        int start = editor.getSelectionStart();
        int end = editor.getSelectionEnd();
        editor.getStyledDocument().setParagraphAttributes(start,end-start,attr,false);
    }
}
