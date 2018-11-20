package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.controller.Engine;
import eg.edu.alexu.csd.oop.draw.controller.UUIDGenerator;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public abstract class AbstractShape implements Shape {
    private Color color;
    private Color fillColor;
    protected boolean completed;
    private boolean selected;
    protected Center centers;
    protected int scale;
    private int thickness;
    private final int UUID; // Universal unique ID

    protected int width;
    protected int height;
    protected Point centerPoint;

    public AbstractShape(Color color, Color fillColor, int thickness, int UUID, int scale) {
        this.color = color;
        this.fillColor = fillColor;
        this.thickness = thickness;
        this.scale = STATIC_VARS.INIT_SLIDER_VAL;
        this.selected = false;
        this.completed = false;
        this.UUID = UUID;
        this.scale = scale;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public Point getFirstPoint() {
        return firstPoint;
    }

    public Point getSecondPoint() {
        return secondPoint;
    }

    public Point getThirdPoint() {
        return thirdPoint;
    }

    protected Point firstPoint;
    protected Point secondPoint;
    protected Point thirdPoint;

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
        return null;
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
        if (isSelected())
            centers.draw(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return this;
    }

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

    public Center getCenters() {
        return centers;
    }

    public abstract void moveCenter(int deltaX, int deltaY);

    public abstract void resize();

    public final void setScale(int sliderScaleVal) {
        this.scale = sliderScaleVal;
    }

    public final int getScale() {
        return this.scale;
    }

    public void setProp(int width, int height, Point centerPoint, Point firstPoint
            , Point secondPoint, Point thirdPoint) {
            this.width = width;
            this.height = height;
            this.centerPoint = centerPoint;
            this.firstPoint = firstPoint;
            this.secondPoint = secondPoint;
            this.thirdPoint = thirdPoint;
    }

    protected int getUUID() {
        return this.UUID;
    }
    @Override
    public boolean equals(Object o){
        return o instanceof AbstractShape && ((AbstractShape) o).UUID == this.UUID;
    }

    public final void clearScale(){
        this.scale = STATIC_VARS.ORIGINAL_SHAPE_SCALE;
    }
}
