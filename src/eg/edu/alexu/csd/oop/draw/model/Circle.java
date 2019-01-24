package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.CalculationHelper;

import java.awt.*;
import java.util.Map;

public class Circle extends Ellipse {

    public Circle(Point centerPoint) {
        super(centerPoint);
    }
    public Circle() {}

    protected Circle(Point centerPoint, int width, int height, Color color, Color fillColor, int thickness, int UUID, int scale, int deltaX, int deltaY) {
        super(centerPoint, width, height, color, fillColor, thickness, UUID, scale, deltaX, deltaY);
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
    public Object clone() throws CloneNotSupportedException {
        return new Circle(this.centerPoint, this.width, this.height, getColor(), getFillColor(), getThickness(), getUUID(), getScale(), getDeltaX(), getDeltaY());
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

    @Override
    public String toString() {
        return "Circle";
    }
}
