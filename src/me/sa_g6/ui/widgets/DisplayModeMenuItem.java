package me.sa_g6.ui.widgets;

import me.sa_g6.formatting.DisplayMode;
import me.sa_g6.formatting.Font.Font;
import me.sa_g6.ui.MainWindow;

public class DisplayModeMenuItem extends IMenuItem {
        public DisplayModeMenuItem(String text, DisplayMode displayMode) {
            super(text, (e)->{
                    displayMode.perform(MainWindow.getInstance().getCurrentTab().getEditor());
            });
        }

}
