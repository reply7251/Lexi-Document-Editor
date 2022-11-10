package me.sa_g6.ui.widgets;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class MouseEventBus extends MouseAdapter implements MouseMotionListener, MouseListener,IMouseEventSubject {
    private ArrayList<MouseObserver> observerList = new ArrayList<MouseObserver>();

    public void subscribe(MouseObserver mouseObserver){
        observerList.add(mouseObserver);
    }
    public void unsubscribe(MouseObserver mouseObserver){
        observerList.remove(mouseObserver);
    }
    public void notifyAll(MouseEvent e){
        for (MouseObserver observer: observerList ) {
            observer.update(e);
        }
    }

    /*When mouse event */
    @Override
    public void mouseClicked(MouseEvent e) {
        notifyAll(e);
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        notifyAll(e);
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        notifyAll(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        notifyAll(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        notifyAll(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        notifyAll(e);
    }

}
