package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Engine implements DrawingEngine {

    List<Shape> shapes;
    private Color color;
    public Engine(){
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
        shapes.add(shape);
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

    @Override
    public void selectShapes(Point point) {
        for (Shape shape : shapes){
            if (!shape.isSelected() && shape.isOnBoarder(point)) {
                shape.select();
                return;
            }
        }
        unSelectAll();
    }

    @Override
    public void unSelectAll(){
        for(Shape shape : shapes)shape.unSelect();
    }
    @Override
    public void deleteSelectedShapes() {
        shapes.removeIf(Shape::isSelected);
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        for (Shape shape : shapes){
            if (shape.isSelected()) {
                shape.setColor(color);
            }
        }
        unSelectAll();
    }
}
