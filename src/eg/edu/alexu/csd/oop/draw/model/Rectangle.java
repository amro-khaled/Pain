package eg.edu.alexu.csd.oop.draw.model;

import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.Map;

public class Rectangle extends AbstractShape {

    public Rectangle(Point headPoint) {
        super();
        this.firstPoint = (Point) headPoint.clone();
        centerPoint = firstPoint;
    }

    public Rectangle(Point firstPoint, Point secondPoint, Point center, int width, int height, Color color, Color fillColor, int thickness, int UUID, int scale, int deltaX, int deltaY) {
        super(color, fillColor, thickness, UUID, scale, deltaX, deltaY);
        this.firstPoint = (Point) firstPoint.clone();
        this.secondPoint = (Point) secondPoint.clone();
        this.centerPoint = (Point) center.clone();
        this.width = width;
        this.height = height;
    }

    @Override
    public void setPosition(Point position) {
        this.secondPoint = position;
        width = firstPoint.x - secondPoint.x;
        height = firstPoint.y - secondPoint.y;
        centerPoint = new Point((firstPoint.x + secondPoint.x) / 2, (firstPoint.y + secondPoint.y)/2);
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
        if(centerPoint == null)
            centerPoint = new Point((firstPoint.x + secondPoint.x) / 2, (firstPoint.y + secondPoint.y)/2);

        g2.drawRect(deltaX + Math.min(centerPoint.x + getScaledWidth()/2, centerPoint.x - getScaledWidth()/2)
                , deltaY + Math.min(centerPoint.y + getScaledHeight()/2, centerPoint.y - getScaledHeight()/2)
                , Math.abs(getScaledWidth())
                , Math.abs(getScaledHeight()));
        super.draw(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Rectangle(firstPoint, secondPoint, centerPoint, width, height, getColor(), getFillColor(), getThickness(), getUUID(), getScale(), getDeltaX(), getDeltaY());
    }

    protected void buildCenters() {
    }

    @Override
    public void applyMovement() {
        firstPoint = new Point(firstPoint.x + deltaX, firstPoint.y + deltaY);
        secondPoint = new Point(secondPoint.x + deltaX, secondPoint.y + deltaY);
        centerPoint = new Point(centerPoint.x + deltaX, centerPoint.y + deltaY);
        deltaY = deltaX = 0;
    }

    @Override
    public boolean isOnBoarder(Point point) {
        int leftX = (int) (centerPoint.x - .5 * Math.abs(width));
        int topY = (int) (centerPoint.y - .5 * Math.abs(height));
        if ((Math.abs(point.x - leftX) <= STATIC_VARS.SELECTION_PRECISION || Math.abs(point.x - (leftX + Math.abs(width))) <= STATIC_VARS.SELECTION_PRECISION)
                && point.y >= topY && point.y <= topY + Math.abs(height)) return true;
        if ((Math.abs(point.y - topY) <= STATIC_VARS.SELECTION_PRECISION || Math.abs(point.y - (topY + Math.abs(height))) <= STATIC_VARS.SELECTION_PRECISION)
                && point.x >= leftX && point.x <= leftX + Math.abs(width)) return true;
        return false;
    }

    @Override
    public void resize() {
        width = Math.abs(getScaledWidth());
        height = Math.abs(getScaledHeight());
        scale = STATIC_VARS.INIT_SLIDER_VAL;
        // HACK TO MAKE IT WORK
        firstPoint = new Point(centerPoint.x - width/2, centerPoint.y - height/2);
        secondPoint = new Point(centerPoint.x + width/2, centerPoint.y + height/2);
        buildCenters();
    }

    private int getScaledWidth(){
        return (int) (width * ((double) scale / STATIC_VARS.ORIGINAL_SHAPE_SCALE));
    }
    private int getScaledHeight(){
        return (int) (height * ((double) scale / STATIC_VARS.ORIGINAL_SHAPE_SCALE));
    }


}
