package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.CalculationHelper;

import java.awt.*;
import java.util.Map;

public class Circle extends Ellipse {

    public Circle(Point centerPoint) {
        super(centerPoint);
    }

    @Override
    public void setPosition(Point position) {
        int radius = (int) Math.sqrt(Math.pow(centerPoint.x - position.x, 2) + Math.pow(centerPoint.y - position.y, 2));
        width = height = radius;
        buildCenters();
    }


    protected void setRadius(int radius) {
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

    public boolean fallInside(Point point) {
        return Math.abs(CalculationHelper.dist(point, centerPoint)) <= width;
    }


    public Point getCenterPoint() {
        return centerPoint;
    }

    public void setCenter(int x, int y) {
        this.centerPoint = new Point(x, y);
    }
}
