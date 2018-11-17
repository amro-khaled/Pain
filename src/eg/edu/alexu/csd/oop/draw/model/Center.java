package eg.edu.alexu.csd.oop.draw.model;


import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Center {
    List<Circle> circles;
    Center(){
        circles = new ArrayList<>();
    }

    public void draw(Graphics canvas){
        for(Circle circle : circles){
            circle.draw(canvas);
            canvas.drawLine(circle.centerPoint.x, circle.centerPoint.y, circle.centerPoint.x, circle.centerPoint.y);
        }
    }

    public void add(int x, int y) {
        Point point = new Point(x, y);
        Circle circle = new Circle(point);
        circle.setRadius(STATIC_VARS.CENTERS_RADIUS);
        circle.setColor(STATIC_VARS.CENTERS_COLOR);
        circle.setThickness(STATIC_VARS.CENTER_THICKNESS);
        circles.add(circle);
    }
}
