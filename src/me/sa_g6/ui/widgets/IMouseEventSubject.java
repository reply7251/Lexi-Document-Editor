package me.sa_g6.ui.widgets;

import java.awt.event.MouseEvent;

public interface IMouseEventSubject {
    void subscribe(MouseObserver mouseObserver);
    void notifyAll(MouseEvent e);

}
