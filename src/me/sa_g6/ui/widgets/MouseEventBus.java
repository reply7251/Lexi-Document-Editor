package me.sa_g6.ui.widgets;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class MouseEventBus extends MouseAdapter implements MouseMotionListener, MouseListener,IMouseEventSubject {
    private ArrayList<MouseObserver> observerList = new ArrayList<MouseObserver>();

    public void subscribe(MouseObserver mouseOberserver){
        observerList.add(mouseOberserver);
    }
    public void unsubscribe(MouseObserver mouseOberserver){
        observerList.remove(mouseOberserver);
    }
    public void notifiAll(MouseEvent e){
        for (MouseObserver oberserver: observerList ) {
            oberserver.update(e);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        notifiAll(e);
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        notifiAll(e);
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        notifiAll(e);
        //System.out.println(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        notifiAll(e);
        System.out.println(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        notifiAll(e);
        //System.out.println(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        notifiAll(e);
        //System.out.println(e);
    }

}
