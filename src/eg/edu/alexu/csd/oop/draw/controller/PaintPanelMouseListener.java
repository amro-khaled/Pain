package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.command.ActionCommand;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PaintPanelMouseListener implements MouseListener {

    private PanelController panelController;
    private DrawingEngine engine;
    private JPanel paintPanel;

    public PaintPanelMouseListener(JPanel paintPanel, PanelController panelController) {
        super();
        this.engine = Engine.getInstance();
        this.paintPanel = paintPanel;
        this.panelController = panelController;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (panelController.getShape() == null) {
            engine.selectShapes(e.getPoint());
        } else {
            panelController.release();
        }
        paintPanel.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (panelController.getShape() != null && !((Shape) panelController.getShape()).isCompleted()) {
            return;
        }
        Shape shape = panelController.createAndGetShape(e.getPoint());
        if (shape == null) {
            panelController.setMovingCenter(engine.getMovingCenter(e.getPoint()));
            return;
        }
        engine.addShape(shape);
        paintPanel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        engine.actionMovedShapes();
        panelController.setMovingCenter(null);
        panelController.release();
        paintPanel.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
