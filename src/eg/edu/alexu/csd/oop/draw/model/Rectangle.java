package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public class Rectangle extends AbstractShape {
    Point centerPoint;
    int width, height;

    public Rectangle(Point position) {
        super();
        centerPoint = (Point) position.clone();
    }

    @Override
    public void setPosition(Point position) {
        width = centerPoint.x - position.x;
        height = centerPoint.y - position.y;
        buildCenters();
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
        g2.setStroke(new BasicStroke(getThickness()));
        g2.setColor(isSelected()? STATIC_VARS.SELECTION_COLOR : getColor());
        g2.drawRect(Math.min(centerPoint.x, centerPoint.x - width), Math.min(centerPoint.y, centerPoint.y - height), Math.abs(width), Math.abs(height));
        super.draw(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return this;
    }

    protected void buildCenters() {
        centers = new Center();
        centers.add(Math.min(centerPoint.x, centerPoint.x - width) + Math.abs(width)/2, Math.min(centerPoint.y, centerPoint.y - height) + Math.abs(height)/2);
    }
    @Override
    public boolean isOnBoarder(Point point) {
        int leftX = Math.min(centerPoint.x, centerPoint.x - width);
        int topY = Math.min(centerPoint.y, centerPoint.y - height);
        if((Math.abs(point.x - leftX) <= STATIC_VARS.SELECTION_PRECISION || Math.abs(point.x - (leftX + Math.abs(width))) <= STATIC_VARS.SELECTION_PRECISION)
                && point.y >= topY && point.y <= topY + Math.abs(height))return true;
        if((Math.abs(point.y - topY) <= STATIC_VARS.SELECTION_PRECISION || Math.abs(point.y - (topY + Math.abs(height))) <= STATIC_VARS.SELECTION_PRECISION)
                && point.x >= leftX && point.x <= leftX + Math.abs(width))return true;
        return false;
    }
}
