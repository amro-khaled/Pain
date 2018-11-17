package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public class Ellipse extends AbstractShape {
    protected int width;
    protected int height;
    protected Point centerPoint;

    public Ellipse(Point centerPoint) {
        setCompleted(false);
        this.centerPoint = centerPoint;
    }

    @Override
    public void setPosition(Point position) {
        width = Math.abs(centerPoint.x - position.x);
        height = Math.abs(centerPoint.y - position.y);
        buildCenters();
    }

    protected void buildCenters() {
        centers = new Center();
        centers.add(centerPoint.x, centerPoint.y);
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
        g2.setColor(isSelected() ? STATIC_VARS.SELECTION_COLOR : getColor());
        g2.drawOval(centerPoint.x - width, centerPoint.y - height, 2 * width, 2 * height);
        super.draw(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }

    @Override
    public boolean isOnBoarder(Point c) {
        double q = Math.pow((c.getX() - centerPoint.x), 2) / Math.pow(Math.max(width, 1), 2)
                + Math.pow((c.getY() - centerPoint.y), 2) / Math.pow(Math.max(height, 1), 2);
        return Math.abs(q - 1) <= STATIC_VARS.ELLIPSE_SELECTION_PRECISION;
    }

}
