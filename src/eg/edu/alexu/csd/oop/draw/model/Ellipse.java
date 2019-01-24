package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public class Ellipse extends AbstractShape {
    public Ellipse(Point centerPoint) {
        super();
        this.centerPoint = centerPoint;
    }

    public Ellipse() {}

    protected Ellipse(Point centerPoint, int width, int height, Color color, Color fillColor, int thickness, int UUID, int scale, int deltaX, int deltaY) {
        super(color, fillColor, thickness, UUID, scale, deltaX, deltaY);
        this.centerPoint = (Point) centerPoint.clone();
        this.width = width;
        this.height = height;
    }

    @Override
    public void setPosition(Point position) {
        width = Math.abs(centerPoint.x - position.x);
        height = Math.abs(centerPoint.y - position.y);
        buildCenters();
    }

    protected void buildCenters() {
    }

    @Override
    public void applyMovement() {
        centerPoint = new Point(centerPoint.x + deltaX, centerPoint.y + deltaY);
        deltaY = deltaX = 0;
    }

    @Override
    public String toString() {
        return "Ellipse";
    }

    @Override
    public Point getPosition() {
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
        buildCenters();

        int scaledWidth = (width * scale) / STATIC_VARS.ORIGINAL_SHAPE_SCALE;
        int scaledHeight = (height * scale) / STATIC_VARS.ORIGINAL_SHAPE_SCALE;

        g2.drawOval(centerPoint.x - scaledWidth + deltaX, centerPoint.y - scaledHeight + deltaY, 2 * scaledWidth, 2 * scaledHeight);
        super.draw(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Ellipse(this.centerPoint, this.width, this.height, getColor(), getFillColor(), getThickness(), getUUID(), getScale(), getDeltaX(), getDeltaY());
    }

    @Override
    public boolean isOnBoarder(Point c) {
        double q = Math.pow((c.getX() - centerPoint.x), 2) / Math.pow(Math.max(width, 1), 2)
                + Math.pow((c.getY() - centerPoint.y), 2) / Math.pow(Math.max(height, 1), 2);
        return Math.abs(q - 1) <= STATIC_VARS.ELLIPSE_SELECTION_PRECISION;
    }

    @Override
    public void resize() {
        width = (width * scale) / STATIC_VARS.ORIGINAL_SHAPE_SCALE;
        height = (height * scale) / STATIC_VARS.ORIGINAL_SHAPE_SCALE;
        scale = STATIC_VARS.ORIGINAL_SHAPE_SCALE;
    }

}
