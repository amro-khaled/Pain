package eg.edu.alexu.csd.oop.draw.model;

import java.awt.*;

public class Square extends Rectangle {
    public Square(Point position) {
        super(position);
    }

    @Override
    public void setPosition(Point position) {
        if(Math.abs(headPoint.x - position.x) > Math.abs(headPoint.y - position.y)){
            height = width = headPoint.x - position.x;
            if((headPoint.x - position.x < 0) ^ (headPoint.y - position.y < 0)) {
                height *= -1;
            }
        }else{
            height = width = headPoint.y - position.y;
            if((headPoint.x - position.x < 0) ^ (headPoint.y - position.y < 0)) {
                width *= -1;
            }
        }
        buildCenters();
    }
}
