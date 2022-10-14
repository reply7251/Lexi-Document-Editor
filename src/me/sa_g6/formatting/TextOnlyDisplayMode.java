package me.sa_g6.formatting;

import me.sa_g6.formatting.DisplayMode;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class TextOnlyDisplayMode implements DisplayMode {
    JTextPane curJTextPane;
    @Override

    public void perform(JTextPane editor) {
        curJTextPane=editor;
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setSpaceAbove(attr,1);
        StyleConstants.setSpaceBelow(attr,1);
        int docunmentLength = editor.getComponentCount();
        editor.getStyledDocument().setParagraphAttributes(0,Integer.MAX_VALUE,attr,false);
    }
    public void perform() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setSpaceAbove(attr,8);
        StyleConstants.setSpaceBelow(attr,8);
        curJTextPane.getStyledDocument().setParagraphAttributes(0,Integer.MAX_VALUE,attr,false);
    }
}
