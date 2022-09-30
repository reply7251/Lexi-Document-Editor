package me.sa_g6.formatting.Font;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Underline implements Font{
    public void perform(JTextPane editor){
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setUnderline(attr, true);
        editor.setCharacterAttributes(attr, true);
    }
}
