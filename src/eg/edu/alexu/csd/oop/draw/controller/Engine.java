package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.exceptions.ShapeNotFoundException;
import eg.edu.alexu.csd.oop.draw.model.AbstractShape;
import eg.edu.alexu.csd.oop.draw.model.Circle;
import eg.edu.alexu.csd.oop.draw.model.Line;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Engine implements DrawingEngine {

    private static Engine instance = null;

    List<AbstractShape> shapes;
    private Color color;

    private Engine() {
        shapes = new ArrayList<>();
        color = Color.BLACK;
    }

    @Override
    public void refresh(Graphics canvas) {
        for (Shape shape : shapes)
            shape.draw(canvas);
    }

    @Override
    public void addShape(Shape shape) {
        shapes.add((AbstractShape) shape);
        shape.setColor(color);
    }

    @Override
    public void removeShape(Shape shape) {
        // Comparing references so that overlapping identical shapes are distinguished.
        if (!shapes.removeIf(k -> k == shape)) {
            throw new ShapeNotFoundException("Removing non-existing Shape");
        }
    }

    @Override
    public void updateShape(Shape oldShape, Shape newShape) {
        removeShape(oldShape);
        addShape(newShape);
    }

    @Override
    public Shape[] getShapes() {
        AbstractShape[] returnedShapes = new AbstractShape[shapes.size()];
        int count = 0;
        for (AbstractShape shape : shapes) {
            returnedShapes[count++] = shape;
        }
        return returnedShapes;
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
        for (AbstractShape shape : shapes) {
            if (!shape.isSelected() && shape.isOnBoarder(point)) {
                shape.select();
                return;
            }
        }
        unSelectAll();
    }

    public void unSelectAll() {
        for (AbstractShape shape : shapes) shape.unSelect();
    }

    public List<AbstractShape> deleteSelectedShapes() {
        List<AbstractShape> deletedShapes = new ArrayList<>();
        for (AbstractShape shape : shapes) {
            if (shape.isSelected()) deletedShapes.add(shape);
        }
        ;
        shapes.removeIf(AbstractShape::isSelected);
        return deletedShapes;
    }

    public void setColor(Color color) {
        this.color = color;
        for (AbstractShape shape : shapes) {
            if (shape.isSelected()) {
                shape.setColor(color);
            }
        }
        unSelectAll();
    }

    public synchronized static Engine getInstance() {
        return instance == null ? instance = new Engine() : instance;
    }

    public Circle getMovingCenter(Point point) {
        for (Circle circle : shapes.stream().filter(AbstractShape::isSelected).map(k -> k.getCenters().getCircle()).collect(Collectors.toList())) {
            if (circle.fallInside(point)) {
                return circle;
            }
        }
        return null;
    }

    public void moveSelectedShapes(Point point, Circle circle) {
        if (circle == null) return;
        int deltaX = point.x - circle.getCenterPoint().x;
        int deltaY = point.y - circle.getCenterPoint().y;
        shapes.stream()
                .filter(AbstractShape::isSelected)
                .forEach(k -> k.moveCenter(deltaX, deltaY));
    }

    public void scaleSelectedShapes(int sliderVal) {
        shapes.stream()
                .filter(AbstractShape::isSelected)
                .forEach(k -> k.setScale(sliderVal));
    }

    public void resizeSelectedShapes() {
        shapes.stream()
                .filter(AbstractShape::isSelected)
                .filter(k -> k.getScale() != STATIC_VARS.ORIGINAL_SHAPE_SCALE)
                .forEach(k -> k.resize());
    }

    public List<AbstractShape> getSelectedShapes() {
        List<AbstractShape> selectedShapes = new ArrayList<>();
        for (AbstractShape shape : shapes) {
            if (shape.isSelected()) selectedShapes.add(shape);
        }
        return selectedShapes;
    }
}
