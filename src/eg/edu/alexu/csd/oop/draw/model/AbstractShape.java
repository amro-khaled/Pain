package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public abstract class AbstractShape implements Shape {
    private static Color defaultColor;
    private Color color;
    private Color fillColor;
    protected boolean completed;
    private boolean selected;
    protected Center centers;

    private int thickness;

    AbstractShape(){
        thickness = STATIC_VARS.DEFAULT_THICKNESS;
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
    public void draw(Graphics canvas){
        if(isSelected())
            centers.draw(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }

    public abstract boolean isOnBoarder(Point point);

    public final boolean isCompleted() {
        return completed;
    }

    public final void setCompleted(boolean c) {
        completed = c;
    }

    public void release() {
        completed = true;
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

}
