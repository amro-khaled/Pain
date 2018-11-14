package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.model.AbstractShape;
import eg.edu.alexu.csd.oop.draw.model.Line;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PaintPanelMouseListener implements MouseListener {

    private PanelController panelController;
    private Engine engine;
    private JPanel paintPanel;

    public PaintPanelMouseListener(JPanel paintPanel, PanelController panelController) {
        super();
        this.engine = Engine.getInstance();
        this.paintPanel = paintPanel;
        this.panelController = panelController;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!panelController.isPaintingMode()){
            engine.selectShapes(e.getPoint());
        }else {
            panelController.release();
        }
        paintPanel.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!panelController.isPaintingMode()) return;
        Shape shape = panelController.createAndGetShape(e.getPoint());
        engine.addShape(shape);
        paintPanel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
