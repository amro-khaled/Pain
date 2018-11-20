package eg.edu.alexu.csd.oop.draw.model;


import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;

public class Center {
    Circle circle;
    Center(){
        circle = new Circle(new Point(-200, -200));
        circle.setRadius(STATIC_VARS.CENTERS_RADIUS);
        circle.setColor(STATIC_VARS.CENTERS_COLOR);
        circle.setThickness(STATIC_VARS.CENTER_THICKNESS);
    }

    public void draw(Graphics canvas){
            circle.draw(canvas);
            canvas.drawLine(circle.centerPoint.x, circle.centerPoint.y, circle.centerPoint.x, circle.centerPoint.y);
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCenterPoint(int x, int y) {
        circle.setCenter(x, y);
    }
}
