package eg.edu.alexu.csd.oop.draw.view;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.model.*;
import eg.edu.alexu.csd.oop.draw.model.Rectangle;

import java.awt.*;

public class PanelState {
    public Shape createAndGetShape(Point point) {
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
    public void setShape(ShapeButton sb){
        curButton = sb;
    }

    public void release() {
        if(curShape != null){
            curShape.release();
            if(curShape.isCompleted()) {
                curShape = null;
                curButton = ShapeButton.NONE;
            }
        }
    }

    public void selectShapes(Point point) {

    }

    public enum ShapeButton{
        NONE, LINE, CIRCLE, ELLIPSE, TRIANGLE, RECTANGLE, SQUARE;
    }
    ShapeButton curButton = ShapeButton.NONE;
    Shape curShape = null;
}
