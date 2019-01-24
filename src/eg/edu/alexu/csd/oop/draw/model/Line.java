package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.CalculationHelper;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public class Line extends AbstractShape {

    public Line(Point firstPoint) {
        super();
        this.firstPoint = (Point) firstPoint.clone();
    }
    public Line() {}

    public Line(Point firstPoint, Point secondPoint, Color color, Color fillColor, int thickness, int UUID, int scale, int deltaX, int deltaY) {
        super(color, fillColor, thickness, UUID, scale, deltaX, deltaY);
        this.firstPoint = (Point) firstPoint.clone();
        this.secondPoint = (Point) secondPoint.clone();
        buildCenters();
    }

    @Override
    public void setPosition(Point position) {
        this.secondPoint = (Point) position.clone();
        buildCenters();
    }

    @Override
    public Point getPosition() {
        return null;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {

    }

    protected void buildCenters() {
        centerPoint = new Point((firstPoint.x + secondPoint.x) / 2, (firstPoint.y + secondPoint.y) / 2);
    }

    @Override
    public void applyMovement() {
        firstPoint = new Point(firstPoint.x + deltaX, firstPoint.y + deltaY);
        secondPoint = new Point(secondPoint.x + deltaX, secondPoint.y + deltaY);
        buildCenters();
        deltaY = deltaX = 0;
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
        int width = (int) ((firstPoint.x - secondPoint.x) * 0.5 * scale / STATIC_VARS.ORIGINAL_SHAPE_SCALE);
        int height = (int) ((firstPoint.y - secondPoint.y) * 0.5 * scale / STATIC_VARS.ORIGINAL_SHAPE_SCALE);
        g2.drawLine(deltaX + centerPoint.x - width, deltaY + centerPoint.y - height, deltaX + centerPoint.x + width, deltaY + centerPoint.y + height);
        super.draw(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Line(firstPoint, secondPoint, getColor(), getFillColor(), getThickness(), getUUID(), getScale(), getDeltaX(), getDeltaY());
    }

    @Override
    public boolean isOnBoarder(Point point) {
        if (CalculationHelper.isSameLine(firstPoint, point, secondPoint))
            return true;
        return false;
    }

    @Override
    public void resize() {
        int width = firstPoint.x - secondPoint.x;
        int height = firstPoint.y - secondPoint.y;
        this.width = (width * scale) / STATIC_VARS.ORIGINAL_SHAPE_SCALE;
        this.height = (height * scale) / STATIC_VARS.ORIGINAL_SHAPE_SCALE;
        firstPoint.x = centerPoint.x + this.width / 2;
        secondPoint.x = centerPoint.x - this.width / 2;
        firstPoint.y = centerPoint.y + this.height / 2;
        secondPoint.y = centerPoint.y - this.height / 2;
        scale = STATIC_VARS.ORIGINAL_SHAPE_SCALE;
    }
    @Override
    public String toString() {
        return "Line";
    }
}
