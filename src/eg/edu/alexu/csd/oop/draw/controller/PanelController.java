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
            case STATIC_VARS.PANEL_BUTTON_NAME_LINE:
                return curShape = new Line(point);
            case STATIC_VARS.PANEL_BUTTON_NAME_CIRCLE:
                return curShape = new Circle(point);
            case STATIC_VARS.PANEL_BUTTON_NAME_ELLIPSE:
                return curShape = new Ellipse(point);
            case STATIC_VARS.PANEL_BUTTON_NAME_RECTANGLE:
                return curShape = new Rectangle(point);
            case STATIC_VARS.PANEL_BUTTON_NAME_SQUARE:
                return curShape = new Square(point);
            case STATIC_VARS.PANEL_BUTTON_NAME_TRIANGLE:
                return curShape = new Triangle(point);
            default:
                return curShape = Engine.getInstance().createLoadedClassShape(curButton);
        }
    }

    public Shape getShape(){
        return curShape;
    }
    Point movingCenter = null;

    public void release() {
        if(curShape != null){
            curShape.release();
            if(curShape.isCompleted()) {
                curShape = null;
                curButton = null;
            }
        }
    }

    public Point getMovingCenter() {
        return movingCenter;
    }
    public void setMovingCenter(Point movingCenter) {
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

    public String curButton = null;

    AbstractShape curShape = null;

}
