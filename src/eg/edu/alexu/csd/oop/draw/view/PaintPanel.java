package eg.edu.alexu.csd.oop.draw.view;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.controller.PaintPanelMouseListener;
import eg.edu.alexu.csd.oop.draw.controller.PaintPanelMouseMotionListener;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PaintPanel extends JPanel {
    DrawingEngine engine;
    public PaintPanel(DrawingEngine engine, PanelState panelState) {
        this.engine = engine;
        addMouseListener(new PaintPanelMouseListener(engine, this, panelState));
        addMouseMotionListener(new PaintPanelMouseMotionListener(engine, this, panelState));
    }
    @Override
    public void paint(Graphics canvas){
        super.paint(canvas);
        engine.refresh(canvas);
    }
}
