package me.sa_g6.ui.widgets;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class MouseEventBus extends MouseAdapter implements MouseMotionListener, MouseListener,IMouseEventSubject {
    private ArrayList<MouseObserver> observers = new ArrayList<>();

    public void subscribe(MouseObserver mouseObserver){
        observers.add(mouseObserver);
    }
    public void unsubscribe(MouseObserver mouseObserver){
        observers.remove(mouseObserver);
    }
    public void notifyAll(MouseEvent e){
        for (MouseObserver observer: observers) {
            observer.update(e);
        }
    }


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
