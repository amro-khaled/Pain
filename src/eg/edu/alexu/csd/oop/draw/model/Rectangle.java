package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public class Rectangle extends AbstractShape {
    Point headPoint;
    int scaledWidth, scaledHeight;

    public Rectangle(Point position) {
        super();
        headPoint = (Point) position.clone();
    }

    @Override
    public void setPosition(Point position) {
        scaledWidth = width = headPoint.x - position.x;
        scaledHeight = height = headPoint.y - position.y;
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
        g2.setColor(getColor());
        if (isSelected()) {
            Stroke dashed = new BasicStroke(getThickness(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            g2.setStroke(dashed);
        } else {
            g2.setStroke(new BasicStroke(getThickness()));
        }

        scaledWidth = (int) (width * ((double) scale / STATIC_VARS.ORIGINAL_SHAPE_SCALE));
        scaledHeight = (int) (height * ((double) scale / STATIC_VARS.ORIGINAL_SHAPE_SCALE));
        if (isCompleted()) {
            centerPoint = centers.circle.centerPoint;
            headPoint.x = (headPoint.x > centerPoint.x) ? Math.max(centerPoint.x + scaledWidth / 2, centerPoint.x - scaledWidth / 2)
                    : Math.min(centerPoint.x + scaledWidth / 2, centerPoint.x - scaledWidth / 2);
            headPoint.y = (headPoint.y > centerPoint.y) ? Math.max(centerPoint.y + scaledHeight / 2, centerPoint.y - scaledHeight / 2)
                    : Math.min(centerPoint.y + scaledHeight / 2, centerPoint.y - scaledHeight / 2);
        }

        g2.drawRect(Math.min(headPoint.x, headPoint.x - scaledWidth)
                , Math.min(headPoint.y, headPoint.y - scaledHeight)
                , Math.abs(scaledWidth)
                , Math.abs(scaledHeight));
        super.draw(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return this;
    }

    protected void buildCenters() {
        if (centers == null) centers = new Center();
        if (centerPoint == null)
            centers.setCenterPoint(Math.min(headPoint.x, headPoint.x - width) + Math.abs(width) / 2, Math.min(headPoint.y, headPoint.y - height) + Math.abs(height) / 2);
        else
            centers.setCenterPoint(centerPoint.x, centerPoint.y);
    }

    @Override
    public boolean isOnBoarder(Point point) {
        int leftX = Math.min(headPoint.x, headPoint.x - width);
        int topY = Math.min(headPoint.y, headPoint.y - height);
        if ((Math.abs(point.x - leftX) <= STATIC_VARS.SELECTION_PRECISION || Math.abs(point.x - (leftX + Math.abs(width))) <= STATIC_VARS.SELECTION_PRECISION)
                && point.y >= topY && point.y <= topY + Math.abs(height)) return true;
        if ((Math.abs(point.y - topY) <= STATIC_VARS.SELECTION_PRECISION || Math.abs(point.y - (topY + Math.abs(height))) <= STATIC_VARS.SELECTION_PRECISION)
                && point.x >= leftX && point.x <= leftX + Math.abs(width)) return true;
        return false;
    }

    @Override
    public void moveCenter(int deltaX, int deltaY) {
        centerPoint.x += deltaX;
        centerPoint.y += deltaY;
        buildCenters();
    }

    @Override
    public void resize() {
        width = scaledWidth;
        height = scaledHeight;
        scale = STATIC_VARS.INIT_SLIDER_VAL;
    }

}
