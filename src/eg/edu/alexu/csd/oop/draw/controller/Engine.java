package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.model.AbstractShape;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Engine implements DrawingEngine {

    private static Engine instance = null;

    List<AbstractShape> shapes;
    private Color color;

    private Engine(){
        shapes = new ArrayList<>();
        color = Color.BLACK;
    }

    @Override
    public void refresh(Graphics canvas) {
        for(Shape shape : shapes)
            shape.draw(canvas);
    }

    @Override
    public void addShape(Shape shape) {
        shapes.add((AbstractShape)shape);
        shape.setColor(color);
    }

    @Override
    public void removeShape(Shape shape) {
        for(Shape s : shapes) {
            if(shape.equals(s)) {
                shapes.remove(shape);
                return;
            }
        }
    }

    @Override
    public void updateShape(Shape oldShape, Shape newShape) {
//        shapes.remove(oldShape);
//        shapes.add(newShape);

    }

    @Override
    public Shape[] getShapes() {
        return (Shape[])shapes.toArray();
    }

    @Override
    public List<Class<? extends Shape>> getSupportedShapes() {
        return null;
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }

    @Override
    public void save(String path) {

    }

    @Override
    public void load(String path) {

    }

    public void selectShapes(Point point) {
        for (AbstractShape shape : shapes){
            if (!shape.isSelected() && shape.isOnBoarder(point)) {
                shape.select();
                return;
            }
        }
        unSelectAll();
    }

    public void unSelectAll(){
        for(AbstractShape shape : shapes)shape.unSelect();
    }

    public void deleteSelectedShapes() {
        shapes.removeIf(AbstractShape::isSelected);
    }

    public void setColor(Color color) {
        this.color = color;
        for (AbstractShape shape : shapes){
            if (shape.isSelected()) {
                shape.setColor(color);
            }
        }
        unSelectAll();
    }
    public synchronized static Engine getInstance(){
        return instance == null? instance = new Engine() : instance;
    }
}
