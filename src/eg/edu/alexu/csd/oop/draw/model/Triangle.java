package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.CalculationHelper;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public class Triangle extends AbstractShape {
    private Point firstPoint, secondPoint, thirdPoint;
    public boolean readyForThirdPoint;


    public Triangle(Point firstPoint) {
        this.firstPoint = firstPoint;
        readyForThirdPoint = false;
    }

    @Override
    public void setPosition(Point position) {
        if (!readyForThirdPoint) {
            secondPoint = position;
        } else {
            thirdPoint = position;
        }
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
        g2.setColor(isSelected() ? STATIC_VARS.SELECTION_COLOR : getColor());
        g2.drawLine(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y);
        if (thirdPoint != null) {
            g2.drawLine(thirdPoint.x, thirdPoint.y, secondPoint.x, secondPoint.y);
            g2.drawLine(firstPoint.x, firstPoint.y, thirdPoint.x, thirdPoint.y);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
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

}
