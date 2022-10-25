package me.sa_g6.ui.widgets;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class MouseEventBus extends MouseAdapter implements MouseMotionListener, MouseListener,IMouseEventSubject {
    private ArrayList<MouseOberserver> oberserverList = new ArrayList<MouseOberserver>();

    public void subscribe(MouseOberserver mouseOberserver){
        oberserverList.add(mouseOberserver);
    }
    public void unsubscribe(MouseOberserver mouseOberserver){
        oberserverList.remove(mouseOberserver);
    }
    public void notifiAll(MouseEvent e){
        for (MouseOberserver oberserver: oberserverList ) {
            oberserver.update(e);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

        notifiAll(e);
        //System.out.println(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        notifiAll(e);
        //System.out.println(e);
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
