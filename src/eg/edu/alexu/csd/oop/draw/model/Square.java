package eg.edu.alexu.csd.oop.draw.model;

import java.awt.*;

public class Square extends Rectangle {
    public Square(Point position) {
        super(position);
    }

    @Override
    public void setPosition(Point position) {
        if(Math.abs(center.x - position.x) > Math.abs(center.y - position.y)){
            height = width = center.x - position.x;
            if((center.x - position.x < 0) ^ (center.y - position.y < 0)) {
                height *= -1;
            }
        }else{
            height = width = center.y - position.y;
            if((center.x - position.x < 0) ^ (center.y - position.y < 0)) {
                width *= -1;
            }
        }
    }
}
