package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public class Ellipse extends AbstractShape {
    protected int width;
    protected int height;
    protected Point center;

    public Ellipse(Point center) {
        setCompleted(false);
        this.center = center;
    }

    @Override
    public void setPosition(Point position) {
        width = Math.abs(center.x - position.x);
        height = Math.abs(center.y - position.y);
    }

    @Override
    public Point getPosition() {
        return null;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {

    }

    @Override
    public Map<String, Double> getProperties() {
        return null;
    }

    @Override
    public void draw(Graphics canvas) {
        Graphics2D g2 = (Graphics2D) canvas;
        g2.setColor(isSelected() ? STATIC_VARS.SELECTION_COLOR : getColor());
        g2.drawOval(center.x - width, center.y - height, 2 * width, 2 * height);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }

    @Override
    public boolean isOnBoarder(Point c) {
        double q = Math.pow((c.getX() - center.x), 2) / Math.pow(Math.max(width, 1), 2)
                + Math.pow((c.getY() - center.y), 2) / Math.pow(Math.max(height, 1), 2);
        return Math.abs(q - 1) <= STATIC_VARS.ELLIPSE_SELECTION_PRECISION;
    }

}
