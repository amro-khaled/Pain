package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.CalculationHelper;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public class Triangle extends AbstractShape {
    public boolean readyForThirdPoint;

    public Triangle(Point firstPoint) {
        super();
        this.firstPoint = firstPoint;
        readyForThirdPoint = false;
    }

    public Triangle(Point firstPoint, Point secondPoint, Point thirdPoint, Color color, Color fillColor, int thickness, int UUID, int scale) {
        super(color, fillColor, thickness, UUID, scale);
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.thirdPoint = thirdPoint;
        buildCenters();
    }

    @Override
    public void setPosition(Point position) {
        if (!readyForThirdPoint) {
            secondPoint = position;
        } else {
            thirdPoint = position;
            buildCenters();
        }
    }

    private void buildCenters() {
        if (centers == null) centers = new Center();
        centers.setCenterPoint((firstPoint.x + secondPoint.x + thirdPoint.x) / 3, (firstPoint.y + secondPoint.y + thirdPoint.y) / 3);
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
        if (firstPoint == null || secondPoint == null) return;
        Graphics2D g2 = (Graphics2D) canvas;
        g2.setColor(getColor());
        if (isSelected()) {
            Stroke dashed = new BasicStroke(getThickness(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            g2.setStroke(dashed);
        } else {
            g2.setStroke(new BasicStroke(getThickness()));
        }
        if (thirdPoint != null) {
            Point center = centers.circle.centerPoint;
            g2.drawLine(getScaledPoint(center.x, firstPoint.x), getScaledPoint(center.y, firstPoint.y), getScaledPoint(center.x, secondPoint.x), getScaledPoint(center.y, secondPoint.y));
            g2.drawLine(getScaledPoint(center.x, thirdPoint.x), getScaledPoint(center.y, thirdPoint.y), getScaledPoint(center.x, secondPoint.x), getScaledPoint(center.y, secondPoint.y));
            g2.drawLine(getScaledPoint(center.x, firstPoint.x), getScaledPoint(center.y, firstPoint.y), getScaledPoint(center.x, thirdPoint.x), getScaledPoint(center.y, thirdPoint.y));
            super.draw(canvas);
        } else {
            g2.drawLine(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Triangle(firstPoint, secondPoint, thirdPoint, getColor(), getFillColor(), getThickness(), getUUID(), getScale());
    }

    @Override
    public boolean isOnBoarder(Point point) {
        return CalculationHelper.isSameLine(firstPoint, point, secondPoint)
                || CalculationHelper.isSameLine(secondPoint, point, thirdPoint)
                || CalculationHelper.isSameLine(firstPoint, point, thirdPoint);
    }

    @Override
    public void release() {
        if (readyForThirdPoint) {
            setCompleted(true);
            select();
        }
        readyForThirdPoint = true;
    }

    @Override
    public void moveCenter(int deltaX, int deltaY) {
        firstPoint.x += deltaX;
        firstPoint.y += deltaY;
        secondPoint.x += deltaX;
        secondPoint.y += deltaY;
        thirdPoint.x += deltaX;
        thirdPoint.y += deltaY;
        buildCenters();
    }

    @Override
    public void resize() {
        buildPoints();
        scale = STATIC_VARS.ORIGINAL_SHAPE_SCALE;
    }

    private void buildPoints() {
        Point center = centers.circle.centerPoint;
        firstPoint.x = getScaledPoint(center.x, firstPoint.x);
        firstPoint.y = getScaledPoint(center.y, firstPoint.y);
        secondPoint.x = getScaledPoint(center.x, secondPoint.x);
        secondPoint.y = getScaledPoint(center.y, secondPoint.y);
        thirdPoint.x = getScaledPoint(center.x, thirdPoint.x);
        thirdPoint.y = getScaledPoint(center.y, thirdPoint.y);
    }

    public int getScaledPoint(int centerVal, int pointVal) {
        double ratio =  ((double) scale / STATIC_VARS.ORIGINAL_SHAPE_SCALE);
        return centerVal - (int)((centerVal - pointVal) * ratio);
    }
}
