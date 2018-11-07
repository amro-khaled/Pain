package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.model.Line;
import eg.edu.alexu.csd.oop.draw.view.PanelState;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PaintPanelMouseListener implements MouseListener {

    private PanelState panelState;
    private DrawingEngine engine;
    private JPanel paintPanel;

    public PaintPanelMouseListener(DrawingEngine engine, JPanel paintPanel, PanelState panelState) {
        super();
        this.engine = engine;
        this.paintPanel = paintPanel;
        this.panelState = panelState;
    }

    Line x;

    @Override
    public void mouseClicked(MouseEvent e) {
        if(panelState.getShape() == null){
            engine.selectShapes(e.getPoint());
        }else {
            panelState.release();
        }
        paintPanel.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(panelState.getShape() != null && !panelState.getShape().isCompleted())return;
        Shape shape = panelState.createAndGetShape(e.getPoint());
        if (shape == null) return;
        engine.addShape(shape);
        paintPanel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        panelState.release();
        paintPanel.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
