package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.controller.Engine;
import eg.edu.alexu.csd.oop.draw.controller.UUIDGenerator;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractShape implements Shape, Serializable {
    private Color color;
    private Color fillColor;
    protected boolean completed;
    private boolean selected;
    protected int scale;
    private int thickness;
    private final int UUID; // Universal unique ID

    protected Point firstPoint;
    protected Point secondPoint;
    protected Point centerPoint;


    protected int deltaX;
    protected int deltaY;
    protected int width;
    protected int height;

    public AbstractShape(Color color, Color fillColor, int thickness, int UUID, int scale, int deltaX, int deltaY) {
        this.color = color;
        this.fillColor = fillColor;
        this.thickness = thickness;
        this.scale = STATIC_VARS.INIT_SLIDER_VAL;
        this.selected = false;
        this.completed = false;
        this.UUID = UUID;
        this.scale = scale;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    AbstractShape() {
        scale = STATIC_VARS.INIT_SLIDER_VAL;
        thickness = STATIC_VARS.DEFAULT_THICKNESS;
        color = Engine.getInstance().getColor();
        fillColor = Color.WHITE;
        selected = false;
        completed = false;
        UUID = UUIDGenerator.getInstance().generate();
    }

    @Override
    public abstract void setPosition(Point position);

    @Override
    public abstract Point getPosition();

    @Override
    public void setProperties(Map<String, Double> properties) {

    }

    @Override
    public Map<String, Double> getProperties() {
        HashMap<String, Double> property = new HashMap<>();
        property.put("scale", scale + 0.0);
        return property;
    }

    @Override
    public final void setColor(Color color) {
        this.color = color;
    }

    @Override
    public final Color getColor() {
        return color;
    }

    @Override
    public final void setFillColor(Color color) {
        this.fillColor = color;
    }

    @Override
    public final Color getFillColor() {
        return fillColor;
    }

    @Override
    public void draw(Graphics canvas) {
        if (isSelected()) {
            Graphics2D g2 = (Graphics2D) canvas;
            g2.setColor(STATIC_VARS.CENTERS_COLOR);
            g2.setStroke(new BasicStroke(STATIC_VARS.CENTER_THICKNESS));
            g2.drawLine(deltaX + centerPoint.x, deltaY + centerPoint.y, deltaX + centerPoint.x, deltaY + centerPoint.y);
            int rad = STATIC_VARS.CENTERS_RADIUS;
            g2.drawOval(deltaX + centerPoint.x - rad, deltaY + centerPoint.y - rad, 2 * rad, 2 * rad);
        }
    }

    @Override
    public abstract Object clone() throws CloneNotSupportedException;

    public abstract boolean isOnBoarder(Point point);

    public final boolean isCompleted() {
        return completed;
    }

    public final void setCompleted(boolean c) {
        completed = c;
    }

    public void release() {
        setCompleted(true);
    }

    public final boolean isSelected() {
        return selected;
    }

    public final void select() {
        selected = true;
    }

    public final void unSelect() {
        selected = false;
    }

    public final int getThickness() {
        return thickness;
    }

    public final void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public abstract void resize();

    public final void setScale(int sliderScaleVal) {
        this.scale = sliderScaleVal;
    }

    public final int getScale() {
        return this.scale;
    }

    protected int getUUID() {
        return this.UUID;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AbstractShape && ((AbstractShape) o).UUID == this.UUID;
    }

    public final void clearScale() {
        this.scale = STATIC_VARS.ORIGINAL_SHAPE_SCALE;
    }

    public final void moveCenter(int deltaX, int deltaY) {
        this.deltaY = deltaY;
        this.deltaX = deltaX;
        buildCenters();
    }

    protected abstract void buildCenters();

    public final Point getMovedCenterPoint() {
        return new Point(centerPoint.x + deltaX, centerPoint.y + deltaY);
    }

    public final void resetMovement() {
        deltaY = deltaX = 0;
    }

    @Override
    public abstract String toString();
}
