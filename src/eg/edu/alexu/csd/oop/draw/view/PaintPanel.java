package eg.edu.alexu.csd.oop.draw.view;

import eg.edu.alexu.csd.oop.draw.controller.Engine;
import eg.edu.alexu.csd.oop.draw.controller.PaintPanelMouseListener;
import eg.edu.alexu.csd.oop.draw.controller.PaintPanelMouseMotionListener;
import eg.edu.alexu.csd.oop.draw.controller.PanelController;

import javax.swing.*;
import java.awt.*;

public class PaintPanel extends JPanel {
    Engine engine;
    public PaintPanel(PanelController panelController) {
        this.engine = Engine.getInstance();
        addMouseListener(new PaintPanelMouseListener(this, panelController));
        addMouseMotionListener(new PaintPanelMouseMotionListener(this, panelController));
    }
    @Override
    public void paint(Graphics canvas){
        super.paint(canvas);
        engine.refresh(canvas);
    }
}
