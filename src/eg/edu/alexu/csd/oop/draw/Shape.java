package eg.edu.alexu.csd.oop.draw;

import java.awt.*;

public interface Shape {

    /*
     * This is a college assignment, I believe using an abstract class for Shape would help to reduce the code
     * so I implemented this interface by an abstract class called AbstractShape.
     * That makes the implementation easier as well as passes the tests for the college.
     */

    public void setPosition(java.awt.Point position);

    public java.awt.Point getPosition();

    // update shape specific properties (e.g., radius)
    public void setProperties(java.util.Map<String, Double> properties);

    public java.util.Map<String, Double> getProperties();

    public void setColor(java.awt.Color color);

    public java.awt.Color getColor();

    public void setFillColor(java.awt.Color color);

    public java.awt.Color getFillColor();

    public void draw(java.awt.Graphics canvas); // redraw the shape on the canvas

    public Object clone() throws CloneNotSupportedException; // create a deep clone of the shape

    boolean isSelected();

    boolean isOnBoarder(Point point);

    void select();

    void applyMovement();

    void unSelect();

    Object getMovedCenterPoint();

    void moveCenter(int deltaX, int deltaY);

    void setScale(int sliderVal);

    int getScale();

    void clearScale();

    static void resize(Shape shape) {
    }

    void resetMovement();

    boolean isCompleted();

    void release();

    public String toString();
}