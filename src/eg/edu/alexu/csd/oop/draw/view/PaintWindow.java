package eg.edu.alexu.csd.oop.draw.view;

import eg.edu.alexu.csd.oop.draw.controller.ColorModifierListener;
import eg.edu.alexu.csd.oop.draw.controller.PanelController;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;
import eg.edu.alexu.csd.oop.draw.controller.Engine;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;


public class PaintWindow extends JFrame {
    private JPanel paintPanel;
    Engine engine;
    Container container;
    PanelController panelController;

    public PaintWindow() {
        super(STATIC_VARS.WINDOW_TITLE);
        getContentPane().setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelController = new PanelController();
        container = getContentPane();
        engine = Engine.getInstance();
        paintPanel = new PaintPanel(panelController);
        paintPanel.setBackground(STATIC_VARS.PANEL_BACKGROUND_COLOR);
        container.setBackground(Color.lightGray);
        BorderLayout layout = new BorderLayout(2, 0);
        container.setLayout(layout);
        container.add(paintPanel, BorderLayout.CENTER);
        initShapeButtons();
        initColorChooser();
        paintPanel.setPreferredSize(new Dimension((int) getContentPane().getSize().getWidth(), 450));
        repaint();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initShapeButtons() {
        JPanel buttons = addShapesButtons();
        JButton deleteBtn = new JButton(STATIC_VARS.PANEL_BUTTON_NAME_DELETE);
        buttons.add(deleteBtn);
        deleteBtn.addActionListener(e -> {
            engine.deleteSelectedShapes();
            repaint();
        });

        container.add(BorderLayout.EAST, buttons);
    }

    private JPanel addShapesButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(6, 1));
        for(PanelController.ShapeButton shape : PanelController.ShapeButton.values()){
            JButton btn = new JButton(shape.toString());
            buttons.add(btn);
            btn.addActionListener(e -> {
                panelController.curButton = shape;
                engine.unSelectAll();
            });
        }
        return buttons;
    }

    private void initColorChooser(){
        JColorChooser chooser = new JColorChooser();
        AbstractColorChooserPanel[] oldPanels = chooser.getChooserPanels();
        for (int i = 0; i < oldPanels.length; i++) {
            String clsName = oldPanels[i].getClass().getName();
            if (!(clsName
                    .equals("javax.swing.colorchooser.DefaultSwatchChooserPanel"))) {
                chooser.removeChooserPanel(oldPanels[i]);
            }
        }
        chooser.getSelectionModel().addChangeListener(new ColorModifierListener(this, chooser));
        AbstractColorChooserPanel colorPanel = chooser.getChooserPanels()[0];
        JPanel chooserPanel = (JPanel) colorPanel.getComponent(0);
        container.add(BorderLayout.SOUTH, chooserPanel);
    }
}
