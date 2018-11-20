package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.CalculationHelper;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public class Triangle extends AbstractShape {
    private Point firstOffset, secondOffset, thirdOffset;
    public boolean readyForThirdPoint;

    public Triangle(Point firstPoint) {
        super();
        this.firstPoint = firstPoint;
        readyForThirdPoint = false;
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
            buildOffset();
            Point center = centers.circle.centerPoint;
            g2.drawLine(center.x - firstOffset.x, center.y - firstOffset.y, center.x - secondOffset.x, center.y - secondOffset.y);
            g2.drawLine(center.x - thirdOffset.x, center.y - thirdOffset.y, center.x - secondOffset.x, center.y - secondOffset.y);
            g2.drawLine(center.x - firstOffset.x, center.y - firstOffset.y, center.x - thirdOffset.x, center.y - thirdOffset.y);
            super.draw(canvas);
        } else {
            g2.drawLine(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y);
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

    private void buildOffset() {
        Point center = centers.circle.centerPoint;
        double ratio = (double) scale / STATIC_VARS.ORIGINAL_SHAPE_SCALE;
        firstOffset = new Point();
        secondOffset = new Point();
        thirdOffset = new Point();
        firstOffset.x = (int) ((center.x - firstPoint.x) * ratio);
        firstOffset.y = (int) ((center.y - firstPoint.y) * ratio);
        secondOffset.x = (int) ((center.x - secondPoint.x) * ratio);
        secondOffset.y = (int) ((center.y - secondPoint.y) * ratio);
        thirdOffset.x = (int) ((center.x - thirdPoint.x) * ratio);
        thirdOffset.y = (int) ((center.y - thirdPoint.y) * ratio);
    }

    private void buildPoints() {
        Point center = centers.circle.centerPoint;
        firstPoint.x = center.x - firstOffset.x;
        firstPoint.y = center.y - firstOffset.y;
        secondPoint.x = center.x - secondOffset.x;
        secondPoint.y = center.y - secondOffset.y;
        thirdPoint.x = center.x - thirdOffset.x;
        thirdPoint.y = center.y - thirdOffset.y;
    }
}
