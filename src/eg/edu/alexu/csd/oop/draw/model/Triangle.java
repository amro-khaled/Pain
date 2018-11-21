package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.CalculationHelper;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public class Triangle extends AbstractShape {
    public boolean readyForThirdPoint;
    protected Point thirdPoint;

    public Triangle(Point firstPoint) {
        super();
        this.firstPoint = firstPoint;
        readyForThirdPoint = false;
    }

    public Triangle(Point firstPoint, Point secondPoint, Point thirdPoint, Color color, Color fillColor, int thickness, int UUID, int scale, int deltaX, int deltaY) {
        super(color, fillColor, thickness, UUID, scale, deltaX, deltaY);
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

    protected void buildCenters() {
        centerPoint = new Point((firstPoint.x + secondPoint.x + thirdPoint.x) / 3, (firstPoint.y + secondPoint.y + thirdPoint.y) / 3);
    }

    @Override
    public void applyMovement() {
        firstPoint = new Point(firstPoint.x + deltaX, firstPoint.y + deltaY);
        secondPoint = new Point(secondPoint.x + deltaX, secondPoint.y + deltaY);
        thirdPoint = new Point(thirdPoint.x + deltaX, thirdPoint.y + deltaY);
        centerPoint = new Point(centerPoint.x + deltaX, centerPoint.y + deltaY);
        deltaY = deltaX = 0;
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
            g2.drawLine(deltaX + getScaledPoint(centerPoint.x, firstPoint.x), deltaY + getScaledPoint(centerPoint.y, firstPoint.y), deltaX + getScaledPoint(centerPoint.x, secondPoint.x), deltaY + getScaledPoint(centerPoint.y, secondPoint.y));
            g2.drawLine(deltaX + getScaledPoint(centerPoint.x, thirdPoint.x), deltaY + getScaledPoint(centerPoint.y, thirdPoint.y), deltaX + getScaledPoint(centerPoint.x, secondPoint.x), deltaY + getScaledPoint(centerPoint.y, secondPoint.y));
            g2.drawLine(deltaX + getScaledPoint(centerPoint.x, firstPoint.x), deltaY + getScaledPoint(centerPoint.y, firstPoint.y), deltaX + getScaledPoint(centerPoint.x, thirdPoint.x), deltaY + getScaledPoint(centerPoint.y, thirdPoint.y));
            super.draw(canvas);
        } else {
            g2.drawLine(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Triangle(firstPoint, secondPoint, thirdPoint, getColor(), getFillColor(), getThickness(), getUUID(), getScale(), getDeltaX(), getDeltaY());
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
    public void resize() {
        buildPoints();
        scale = STATIC_VARS.ORIGINAL_SHAPE_SCALE;
    }

    private void buildPoints() {
        Point center = centerPoint;
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
