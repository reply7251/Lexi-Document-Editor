package me.sa_g6.ui.widgets;

import java.awt.event.MouseEvent;

public interface IMouseEventSubject {
    public void subscribe(MouseObserver mouseOberserver);
    public void notifyAll(MouseEvent e);

}
