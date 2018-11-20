package eg.edu.alexu.csd.oop.draw.view;


import eg.edu.alexu.csd.oop.draw.controller.ColorModifierListener;
import eg.edu.alexu.csd.oop.draw.controller.PanelController;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;
import eg.edu.alexu.csd.oop.draw.controller.Engine;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.util.Hashtable;


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
        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel buttonsPanel = initButtons();
        JSlider sizeSlider = initSizeSlider();
        rightPanel.add(buttonsPanel, BorderLayout.CENTER);
        rightPanel.add(sizeSlider, BorderLayout.SOUTH);
        container.add(rightPanel, BorderLayout.EAST);
        initColorChooser(rightPanel);
        paintPanel.setPreferredSize(new Dimension((int) getContentPane().getSize().getWidth(), 450));
        repaint();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JSlider initSizeSlider() {
        JSlider sizeSlider = new JSlider(JSlider.HORIZONTAL,
                STATIC_VARS.MIN_SLIDER_VAL, STATIC_VARS.MAX_SLIDER_VAL, STATIC_VARS.INIT_SLIDER_VAL);
        sizeSlider.setPaintLabels(true);
        sizeSlider.addChangeListener(e -> {
            if (sizeSlider.getValue() == STATIC_VARS.ORIGINAL_SHAPE_SCALE && sizeSlider.getValueIsAdjusting()) {
            }
            engine.scaleSelectedShapes(sizeSlider.getValue());
            if (!sizeSlider.getValueIsAdjusting()) {
                engine.resizeSelectedShapes();
                sizeSlider.setValue(STATIC_VARS.INIT_SLIDER_VAL);
            }
            Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
            labelTable.put(sizeSlider.getValue(), new JLabel(String.format("%d%%", sizeSlider.getValue())));
            sizeSlider.setLabelTable(labelTable);
            repaint();
        });
        return sizeSlider;
    }

    private JPanel initButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(7, 1));
        for (PanelController.ShapeButton shape : PanelController.ShapeButton.values()) {
            JButton btn = new JButton(shape.toString());
            buttons.add(btn);
            btn.addActionListener(e -> {
                panelController.curButton = shape;
                engine.unSelectAll();
            });
        }
        buttons.add(initDelButton());
        return buttons;
    }

    private JButton initDelButton() {
        JButton deleteBtn = new JButton(STATIC_VARS.PANEL_BUTTON_NAME_DELETE);
        deleteBtn.addActionListener(e -> {
            engine.deleteSelectedShapes();
            repaint();
        });
        return deleteBtn;
    }

    private void initColorChooser(JPanel panel) {
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
        panel.add(BorderLayout.NORTH, chooserPanel);
    }
}
