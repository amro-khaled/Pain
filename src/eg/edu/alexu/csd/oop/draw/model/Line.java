package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.CalculationHelper;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public class Line extends AbstractShape {
    private Point firstPoint;
    private Point secondPoint;


    public Line(Point firstPoint) {
        super();
        setCompleted(false);
        this.firstPoint = (Point) firstPoint.clone();
    }

    @Override
    public void setPosition(Point position) {
        this.secondPoint = (Point) position.clone();
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
        g2.setColor(isSelected()? STATIC_VARS.SELECTION_COLOR : getColor());
        g2.drawLine(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return this;
    }

    @Override
    public boolean isOnBoarder(Point point) {
        if(CalculationHelper.isSameLine(firstPoint, point, secondPoint))
            return true;
        return false;
    }


}
