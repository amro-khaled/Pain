package eg.edu.alexu.csd.oop.draw.model;

import java.awt.*;

public class Square extends Rectangle {
    public Square(Point position) {
        super(position);
    }

    public Square(Point firstPoint, Point secondPoint, Point center, int width, int height, Color color, Color fillColor, int thickness, int UUID, int scale, int deltaX, int deltaY) {
        super(firstPoint, secondPoint, center, width, height, color, fillColor, thickness, UUID, scale, deltaX, deltaY);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Square(firstPoint, secondPoint, centerPoint, width, height, getColor(), getFillColor(), getThickness(), getUUID(), getScale(), getDeltaX(), getDeltaY());
    }

    @Override
    public void setPosition(Point position) {
        secondPoint = position;
        if(Math.abs(firstPoint.x - position.x) > Math.abs(firstPoint.y - position.y)){
            height = width = firstPoint.x - position.x;
            if((firstPoint.x - position.x < 0) ^ (firstPoint.y - position.y < 0)) {
                height *= -1;
            }
        }else{
            height = width = firstPoint.y - position.y;
            if((firstPoint.x - position.x < 0) ^ (firstPoint.y - position.y < 0)) {
                width *= -1;
            }
        }
        buildCenters();
    }
}
