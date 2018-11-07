package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.Shape;

import java.awt.*;
import java.util.Map;

public abstract class AbstractShape implements Shape {
    private static Color defaultColor;
    private Color color;
    private Color fillColor;
    protected boolean completed;
    private boolean selected;

    AbstractShape(){
        color = Color.BLACK;
        fillColor = Color.WHITE;
        selected = false;
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
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    @Override
    public Color getFillColor() {
        return fillColor;
    }

    @Override
    public abstract void draw(Graphics canvas);

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }

    @Override
    public abstract boolean isOnBoarder(Point point);

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean c) {
        completed = c;
    }

    @Override
    public void release() {
        completed = true;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void select() {
        selected = true;
    }

    @Override
    public void unSelect() {
        selected = false;
    }
}
