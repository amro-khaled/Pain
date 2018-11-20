package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.view.PaintWindow;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorModifierListener implements ChangeListener {

    private final Engine engine;
    private final JColorChooser chooser;
    private final PaintWindow paintWindow;


    public ColorModifierListener(PaintWindow paintWindow, JColorChooser chooser){
        this.paintWindow = paintWindow;
        this.engine = Engine.getInstance();
        this.chooser = chooser;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // TODO Auto-generated method stub
        try {
            engine.setColor(chooser.getColor());
        } catch (CloneNotSupportedException e1) {
            e1.printStackTrace();
        }
        paintWindow.repaint();
    }

}
