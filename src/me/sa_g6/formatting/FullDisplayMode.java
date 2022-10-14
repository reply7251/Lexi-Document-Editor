package me.sa_g6.formatting;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class FullDisplayMode implements DisplayMode{
    JTextPane curJTextPane ;
    @Override
    public void perform(JTextPane editor) {
        curJTextPane=editor;
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setSpaceAbove(attr,8);
        StyleConstants.setSpaceBelow(attr,8);

        editor.getStyledDocument().setParagraphAttributes(0,Integer.MAX_VALUE,attr,false);
    }
    public void perform() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setSpaceAbove(attr,8);
        StyleConstants.setSpaceBelow(attr,8);
        curJTextPane.getStyledDocument().setParagraphAttributes(0,Integer.MAX_VALUE,attr,false);
    }
}
