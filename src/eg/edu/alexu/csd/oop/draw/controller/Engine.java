package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.command.ActionCommand;
import eg.edu.alexu.csd.oop.draw.exceptions.ShapeNotFoundException;
import eg.edu.alexu.csd.oop.draw.model.AbstractShape;
import eg.edu.alexu.csd.oop.draw.model.Circle;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import javax.swing.*;
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
        shapes.forEach(AbstractShape::resize);
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
            // JOptionPane.showMessageDialog("u cannot load class " + str,wa);
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
}
