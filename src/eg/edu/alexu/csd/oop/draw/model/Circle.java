package eg.edu.alexu.csd.oop.draw.model;

import java.awt.*;
import java.util.Map;

public class Circle extends Ellipse {

    public Circle(Point center) {
        super(center);
    }

    @Override
    public void setPosition(Point position) {
        int radius = (int) Math.sqrt(Math.pow(center.x - position.x, 2)  + Math.pow(center.y - position.y, 2));
        width = height = radius;
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
    public Object clone() throws CloneNotSupportedException {
        return null;
    }
}
