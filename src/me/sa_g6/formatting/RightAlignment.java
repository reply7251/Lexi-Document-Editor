package me.sa_g6.formatting;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class RightAlignment implements Alignment {
    public void perform(StyledDocument document){
        SimpleAttributeSet right = new SimpleAttributeSet();
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        document.setParagraphAttributes(0, document.getLength(), right, false);
    }
}
