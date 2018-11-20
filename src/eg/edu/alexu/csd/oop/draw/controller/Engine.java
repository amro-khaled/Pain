package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.command.ActionCommand;
import eg.edu.alexu.csd.oop.draw.exceptions.ShapeNotFoundException;
import eg.edu.alexu.csd.oop.draw.model.AbstractShape;
import eg.edu.alexu.csd.oop.draw.model.Circle;
import eg.edu.alexu.csd.oop.draw.model.Line;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Engine implements DrawingEngine {

    private static Engine instance = null;

    List<AbstractShape> shapes;
    private Color color;
    private Stack<ActionCommand> redoStack;
    private Stack<ActionCommand> undoStack;
    private List<AbstractShape> capturedShapes;

    private Engine() {
        redoStack = new Stack<>();
        undoStack = new Stack<>();
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
    }

    @Override
    public void removeShape(Shape shape) {
        // Comparing references so that overlapping identical shapes are distinguished.
        if (!shapes.removeIf(k -> k.equals(shape))) {
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
        if(undoStack.empty()) return;
        ActionCommand command = undoStack.pop();
        command.undo();
        redoStack.push(command);
    }

    @Override
    public void redo() {
        if(redoStack.empty()) return;
        ActionCommand command = redoStack.pop();
        command.redo();
        undoStack.push(command);
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
        List<AbstractShape> deletedShapes = getSelectedShapes();
        List<AbstractShape> deletedShapesClone = new ArrayList<>(deletedShapes);
        addAction(new ActionCommand(()->shapes.addAll(deletedShapesClone), ()->shapes.removeAll(deletedShapesClone)));
        shapes.removeAll(deletedShapes);
        return deletedShapes;
    }

    public void setColor(Color color) throws CloneNotSupportedException {
        List<AbstractShape> selectedShapes = getSelectedShapes();
        List<AbstractShape> coloredShapes = new ArrayList<>();
        this.color = color;
        unSelectAll();
        for(AbstractShape shape : selectedShapes){
            AbstractShape coloredShape = (AbstractShape) shape.clone();
            coloredShape.setColor(color);
            coloredShapes.add(coloredShape);
        }
        shapes.removeAll(selectedShapes);
        shapes.addAll(coloredShapes);
        addAction(new ActionCommand(()->{
            shapes.removeAll(coloredShapes);
            shapes.addAll(selectedShapes);
        }, ()->{
            shapes.removeAll(selectedShapes);
            shapes.addAll(coloredShapes);
        }));
    }

    public synchronized static Engine getInstance() {
        return instance == null ? instance = new Engine() : instance;
    }

    public Circle getMovingCenter(Point point) {
        for (Circle circle : shapes.stream().filter(AbstractShape::isSelected).map(k -> k.getCenters().getCircle()).collect(Collectors.toList())) {
            if (circle.fallInside(point)) {
                try {
                    captureSelectedShapes();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                return circle;
            }
        }
        return null;
    }

    private void captureSelectedShapes() throws CloneNotSupportedException {
        if(capturedShapes != null)return;
        List<AbstractShape> selectedShapes = getSelectedShapes();
        capturedShapes = new ArrayList<>();
        for (AbstractShape selectedShape : selectedShapes) {
            capturedShapes.add((AbstractShape) selectedShape.clone());
        }
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
        List<AbstractShape> resizedShapes = new ArrayList<>();
        List<AbstractShape> originalShapes = shapes.stream()
                .filter(AbstractShape::isSelected)
                .filter(k -> k.getScale() != STATIC_VARS.ORIGINAL_SHAPE_SCALE)
                .collect(Collectors.toList());
        if(originalShapes.isEmpty())return;

        for(AbstractShape shape : originalShapes) {
            try {
                AbstractShape clonedShape = (AbstractShape) shape.clone();
                clonedShape.setScale(shape.getScale());
                shape.clearScale();
                resizedShapes.add(clonedShape);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        shapes.removeAll(originalShapes);
        resizedShapes.forEach(AbstractShape::resize);
        shapes.addAll(resizedShapes);
        addAction(new ActionCommand(
                ()->{
                    shapes.removeAll(resizedShapes);
                    shapes.addAll(originalShapes);
                },
                ()->{
                    shapes.removeAll(originalShapes);
                    shapes.addAll(resizedShapes);
                }));
    }

    public List<AbstractShape> getSelectedShapes() {
        return shapes.stream().filter(AbstractShape::isSelected).collect(Collectors.toList());
    }

    public void addAction(ActionCommand actionCommand) {
        redoStack.clear();
        undoStack.push(actionCommand);
    }

    public Color getColor() {
        return color;
    }

    public void actionMovedShapes() {
        if(capturedShapes == null) return;
        List<AbstractShape> selectedShapes = getSelectedShapes();

        List<AbstractShape> capturedShapesClone = new ArrayList<>(capturedShapes);
        addAction(new ActionCommand(
                ()->{
                    shapes.removeAll(selectedShapes);
                    shapes.addAll(capturedShapesClone);
                },
                ()->{
                    shapes.removeAll(capturedShapesClone);
                    shapes.addAll(selectedShapes);
                })
        );
        capturedShapes = null;
    }
}
