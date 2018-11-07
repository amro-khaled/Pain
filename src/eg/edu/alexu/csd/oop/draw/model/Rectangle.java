package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public class Rectangle extends AbstractShape {
    Point center;
    int width, height;

    public Rectangle(Point position) {
        super();
        center = (Point) position.clone();
    }

    @Override
    public void setPosition(Point position) {
        width = center.x - position.x;
        height = center.y - position.y;
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
        g2.setColor(isSelected()? STATIC_VARS.SELECTION_COLOR : getColor());
        g2.drawRect(Math.min(center.x, center.x - width), Math.min(center.y, center.y - height), Math.abs(width), Math.abs(height));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return this;
    }

    @Override
    public boolean isOnBoarder(Point point) {
        int leftX = Math.min(center.x, center.x - width);
        int topY = Math.min(center.y, center.y - height);
        if((Math.abs(point.x - leftX) <= STATIC_VARS.SELECTION_PRECISION || Math.abs(point.x - (leftX + Math.abs(width))) <= STATIC_VARS.SELECTION_PRECISION)
                && point.y >= topY && point.y <= topY + Math.abs(height))return true;
        if((Math.abs(point.y - topY) <= STATIC_VARS.SELECTION_PRECISION || Math.abs(point.y - (topY + Math.abs(height))) <= STATIC_VARS.SELECTION_PRECISION)
                && point.x >= leftX && point.x <= leftX + Math.abs(width))return true;
        return false;
    }
}
