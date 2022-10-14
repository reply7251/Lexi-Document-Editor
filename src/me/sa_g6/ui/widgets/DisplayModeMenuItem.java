package me.sa_g6.ui.widgets;

import me.sa_g6.formatting.DisplayMode;
import me.sa_g6.formatting.Font.Font;
import me.sa_g6.ui.MainWindow;

public class DisplayModeMenuItem extends IMenuItem {
        public DisplayModeMenuItem(MainWindow mw, String text, DisplayMode displayMode) {
            super(text, (e)->{
                if(mw.getCurrentTab() instanceof Tab tab){
                    displayMode.perform(tab.getEditor());
                }
            });
        }

}
