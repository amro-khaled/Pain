package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.command.ActionCommand;
import eg.edu.alexu.csd.oop.draw.exceptions.ShapeNotFoundException;
import eg.edu.alexu.csd.oop.draw.model.AbstractShape;
import eg.edu.alexu.csd.oop.draw.model.Circle;
import eg.edu.alexu.csd.oop.draw.utils.CalculationHelper;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Engine implements DrawingEngine {

    private static Engine instance = null;

    List<AbstractShape> shapes;
    private Color color;
    private Stack<ActionCommand> redoStack;
    private Stack<ActionCommand> undoStack;
    private Map<String, Class> loadedClasses;
    private List<AbstractShape> capturedShapes;

    private Engine() {
        redoStack = new Stack<>();
        undoStack = new Stack<>();
        shapes = new ArrayList<>();
        color = Color.BLACK;
        loadedClasses = new HashMap<>();
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
        try
        {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeInt(shapes.size());
            for(AbstractShape i : shapes)
                oos.writeObject(i);
            oos.close();
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void load(String path) {
        try
        {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            int n = ois.readInt();
            while(n-- > 0){
                shapes.add((AbstractShape) ois.readObject());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    public Point getMovingCenter(Point point) {
        for (Point centerPoint : shapes.stream().filter(AbstractShape::isSelected).map(k -> k.getMovedCenterPoint()).collect(Collectors.toList())) {
            if (CalculationHelper.dist(centerPoint, point) <= STATIC_VARS.CENTERS_RADIUS) {
                captureSelectedShapes();
                return centerPoint;
            }
        }
        return null;
    }


    public void moveSelectedShapes(Point point, Point circle) {
        if (circle == null) return;
        int deltaX = point.x - circle.x;
        int deltaY = point.y - circle.y;

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

    public String loadClasses(File file) {
        URL myUrl;
        try {
            String nameOfClass = "";
            myUrl = file.toURL();
            String str = myUrl + "";// full path
            int i = 0;
            for (i = str.length() - 1; i >= 0; i--) {
                if (str.charAt(i) == '/') {
                    break;
                }
            }
            nameOfClass = str.substring(++i, str.length() - 6);
            URL name;

            name = new URL(str.substring(0, i));

            return loadClass(name, nameOfClass);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void captureSelectedShapes() {
        if(capturedShapes != null)return;
        capturedShapes = getSelectedShapes();
    }

    private String loadClass(URL name, String nameOfClass) {
        System.out.println(name.toString());
        URL[] my = { name };
        URLClassLoader classloader = new URLClassLoader(my);
        try {
            Class myClass = classloader.loadClass( STATIC_VARS.PACKAGE_NAME + nameOfClass);
            if (loadedClasses.keySet().contains(nameOfClass)) {
                System.out.println("u already loaded such a class");
                return null;
            }
            loadedClasses.put(nameOfClass, myClass);
            return nameOfClass;
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            System.out.println("Cant load from this path " + name);
        }
        return null;
    }

    public AbstractShape createLoadedClassShape(String curButton) {
        try {
            return (AbstractShape) loadedClasses.get(curButton).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void actionMovedShapes() {
        if(capturedShapes == null)return;
        capturedShapes = null;
        List<AbstractShape> originalShapes = getSelectedShapes();
        List<AbstractShape> movedShapes = new ArrayList<>();
        for(AbstractShape shape : originalShapes){
            try {
                movedShapes.add((AbstractShape) shape.clone());
                shape.resetMovement();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        originalShapes.forEach(AbstractShape::unSelect);
        movedShapes.forEach(AbstractShape::applyMovement);
        shapes.removeAll(originalShapes);
        shapes.addAll(movedShapes);
        addAction(new ActionCommand(()->{
            shapes.removeAll(movedShapes);
            shapes.addAll(originalShapes);
        },
        ()->{
            shapes.removeAll(originalShapes);
            shapes.addAll(movedShapes);
        }));
    }
}
