package eg.edu.alexu.csd.oop.draw.view;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.controller.ColorModifierListener;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;
import eg.edu.alexu.csd.oop.draw.controller.Engine;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;


public class PaintWindow extends JFrame {
    private JPanel paintPanel;
    DrawingEngine engine;
    Container container;
    PanelState panelState;

    public PaintWindow() {
        super(STATIC_VARS.WINDOW_TITLE);
        getContentPane().setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelState = new PanelState();
        container = getContentPane();
        engine = new Engine();
        paintPanel = new PaintPanel(engine, panelState);
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
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(6, 1));
        JButton lineBtn = new JButton(STATIC_VARS.PANEL_BUTTON_NAME_LINE);
        buttons.add(lineBtn);
        lineBtn.addActionListener(e -> {
            panelState.curButton = PanelState.ShapeButton.LINE;
            engine.unSelectAll();
        });
        JButton circleBtn = new JButton(STATIC_VARS.PANEL_BUTTON_NAME_CIRCLE);
        buttons.add(circleBtn);
        circleBtn.addActionListener(e -> {
            panelState.curButton = PanelState.ShapeButton.CIRCLE;
            engine.unSelectAll();
        });
        JButton ellipseBtn = new JButton(STATIC_VARS.PANEL_BUTTON_NAME_ELLIPSE);
        buttons.add(ellipseBtn);
        ellipseBtn.addActionListener(e -> {
            panelState.curButton = PanelState.ShapeButton.ELLIPSE;
            engine.unSelectAll();
        });
        JButton triangleBtn = new JButton(STATIC_VARS.PANEL_BUTTON_NAME_TRIANGLE);
        buttons.add(triangleBtn);
        triangleBtn.addActionListener(e -> {
            panelState.curButton = PanelState.ShapeButton.TRIANGLE;
            engine.unSelectAll();
        });
        JButton rectangleBtn = new JButton(STATIC_VARS.PANEL_BUTTON_NAME_RECTANGLE);
        buttons.add(rectangleBtn);
        rectangleBtn.addActionListener(e -> {
            panelState.curButton = PanelState.ShapeButton.RECTANGLE;
            engine.unSelectAll();
        });
        JButton squareBtn = new JButton(STATIC_VARS.PANEL_BUTTON_NAME_SQUARE);
        buttons.add(squareBtn);
        squareBtn.addActionListener(e -> {
            panelState.curButton = PanelState.ShapeButton.SQUARE;
            engine.unSelectAll();
        });
        JButton deleteBtn = new JButton(STATIC_VARS.PANEL_BUTTON_NAME_DELETE);
        buttons.add(deleteBtn);
        deleteBtn.addActionListener(e -> {
            engine.deleteSelectedShapes();
            repaint();
        });

        container.add(BorderLayout.EAST, buttons);
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
        chooser.getSelectionModel().addChangeListener(new ColorModifierListener(this, engine, chooser));
        AbstractColorChooserPanel colorPanel = chooser.getChooserPanels()[0];
        JPanel chooserPanel = (JPanel) colorPanel.getComponent(0);
        container.add(BorderLayout.SOUTH, chooserPanel);
    }
}
