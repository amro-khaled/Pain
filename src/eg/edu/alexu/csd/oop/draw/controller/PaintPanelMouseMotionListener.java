package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.model.AbstractShape;
import eg.edu.alexu.csd.oop.draw.view.PaintPanel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class PaintPanelMouseMotionListener implements MouseMotionListener {
    private PanelController panelController;
    private JPanel paintPanel;

    public PaintPanelMouseMotionListener(PaintPanel paintPanel, PanelController panelController) {
        super();
        this.paintPanel = paintPanel;
        this.panelController = panelController;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if(panelController.getShape() == null)
            Engine.getInstance().moveSelectedShapes(e.getPoint(), panelController.getMovingCenter());
        else
            panelController.getShape().setPosition(e.getPoint());

        paintPanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(panelController.getShape() != null && !((AbstractShape) panelController.getShape()).isCompleted()){
            panelController.getShape().setPosition(e.getPoint());
            paintPanel.repaint();
        }
    }
}
