package me.sa_g6.utils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ClipboardUtils {
    public static Clipboard getClipboard(){
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    public static class HTMLTransferable implements Transferable {
        static DataFlavor[] dataFlavors = {
                DataFlavor.stringFlavor
        };

        String html;

        public HTMLTransferable(String html){
            this.html = html;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return dataFlavors;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor == dataFlavors[0];
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if(isDataFlavorSupported(flavor)){
                return html;
            }
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
