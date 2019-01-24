package eg.edu.alexu.csd.oop.draw.model;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundRectangle extends Rectangle {
    public RoundRectangle(Point headPoint) {
        super(headPoint);
    }

    public RoundRectangle(Point firstPoint, Point secondPoint, Point center, int width, int height, Color color, Color fillColor, int thickness, int UUID, int scale, int deltaX, int deltaY) {
        super(firstPoint, secondPoint, center, width, height, color, fillColor, thickness, UUID, scale, deltaX, deltaY);
    }
    public RoundRectangle() {

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
        if (centerPoint == null)
            centerPoint = new Point((firstPoint.x + secondPoint.x) / 2, (firstPoint.y + secondPoint.y) / 2);

        g2.draw(new RoundRectangle2D.Double(
                deltaX + Math.min(centerPoint.x + getScaledWidth() / 2, centerPoint.x - getScaledWidth() / 2)
                , deltaY + Math.min(centerPoint.y + getScaledHeight() / 2, centerPoint.y - getScaledHeight() / 2)
                , Math.abs(getScaledWidth())
                , Math.abs(getScaledHeight()), 50, 50));
        callSuperDraw(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new RoundRectangle(firstPoint, secondPoint, centerPoint, width, height, getColor(), getFillColor(), getThickness(), getUUID(), getScale(), getDeltaX(), getDeltaY());
    }
}
