package me.sa_g6.formatting;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class RightAlignment implements Alignment {
    public void perform(JTextPane editor){
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_RIGHT);
        int start = editor.getSelectionStart();
        int end = editor.getSelectionEnd();
        editor.getStyledDocument().setParagraphAttributes(start,end-start,attr,false);
    }
}
