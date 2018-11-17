package eg.edu.alexu.csd.oop.draw.model;

import java.awt.*;

public class Square extends Rectangle {
    public Square(Point position) {
        super(position);
    }

    @Override
    public void setPosition(Point position) {
        if(Math.abs(centerPoint.x - position.x) > Math.abs(centerPoint.y - position.y)){
            height = width = centerPoint.x - position.x;
            if((centerPoint.x - position.x < 0) ^ (centerPoint.y - position.y < 0)) {
                height *= -1;
            }
        }else{
            height = width = centerPoint.y - position.y;
            if((centerPoint.x - position.x < 0) ^ (centerPoint.y - position.y < 0)) {
                width *= -1;
            }
        }
        buildCenters();
    }
}
