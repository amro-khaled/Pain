package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.model.*;
import eg.edu.alexu.csd.oop.draw.model.Rectangle;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import javax.swing.*;
import java.awt.*;

public class PanelController {


    public Shape createAndGetShape(Point point) {
        if(curButton == null) return null;
        switch (curButton){
            case LINE:
                return curShape = new Line(point);
            case CIRCLE:
                return curShape = new Circle(point);
            case ELLIPSE:
                return curShape = new Ellipse(point);
            case RECTANGLE:
                return curShape = new Rectangle(point);
            case SQUARE:
                return curShape = new Square(point);
            case TRIANGLE:
                return curShape = new Triangle(point);
        }
        return null;
    }

    public Shape getShape(){
        return curShape;
    }
    Circle movingCenter = null;

    public void release() {
        if(curShape != null){
            curShape.release();
            if(curShape.isCompleted()) {
                curShape = null;
                curButton = null;
            }
        }
    }

    public Circle getMovingCenter() {
        return movingCenter;
    }
    public void setMovingCenter(Circle movingCenter) {
        this.movingCenter = movingCenter;
    }

    public enum ShapeButton{

        LINE(STATIC_VARS.PANEL_BUTTON_NAME_LINE),
        CIRCLE(STATIC_VARS.PANEL_BUTTON_NAME_CIRCLE),
        ELLIPSE(STATIC_VARS.PANEL_BUTTON_NAME_ELLIPSE),
        TRIANGLE(STATIC_VARS.PANEL_BUTTON_NAME_TRIANGLE),
        RECTANGLE(STATIC_VARS.PANEL_BUTTON_NAME_RECTANGLE),
        SQUARE(STATIC_VARS.PANEL_BUTTON_NAME_SQUARE);

        private final String text;
        ShapeButton(String text){
            this.text = text;
        }
        @Override
        public String toString(){
            return this.text;
        }
    }

    public ShapeButton curButton = null;

    AbstractShape curShape = null;

}
