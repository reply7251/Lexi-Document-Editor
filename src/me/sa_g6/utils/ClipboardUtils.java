package me.sa_g6.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;

public class ClipboardUtils {
    public static Clipboard getClipboard(){
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }
}
