package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.model.Triangle;
import eg.edu.alexu.csd.oop.draw.view.PaintPanel;
import eg.edu.alexu.csd.oop.draw.view.PanelState;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class PaintPanelMouseMotionListener implements MouseMotionListener {
    private PanelState panelState;
    private DrawingEngine engine;
    private JPanel paintPanel;

    public PaintPanelMouseMotionListener(DrawingEngine engine, PaintPanel paintPanel, PanelState panelState) {
        super();
        this.engine = engine;
        this.paintPanel = paintPanel;
        this.panelState = panelState;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(panelState.getShape() == null) return;

        panelState.getShape().setPosition(e.getPoint());

        paintPanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(panelState.getShape() != null && !panelState.getShape().isCompleted()){
            panelState.getShape().setPosition(e.getPoint());
            paintPanel.repaint();
        }
    }
}
