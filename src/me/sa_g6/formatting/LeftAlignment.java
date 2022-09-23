package me.sa_g6.formatting;

import javax.swing.text.*;

public class LeftAlignment implements Alignment {
    public void perform(StyledDocument document){
        SimpleAttributeSet left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        document.setParagraphAttributes(0, document.getLength(), left, false);
    }
}
