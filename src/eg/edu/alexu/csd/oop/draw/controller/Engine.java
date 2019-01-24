package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.command.ActionCommand;
import eg.edu.alexu.csd.oop.draw.exceptions.ShapeNotFoundException;
import eg.edu.alexu.csd.oop.draw.model.AbstractShape;
import eg.edu.alexu.csd.oop.draw.utils.CalculationHelper;
import eg.edu.alexu.csd.oop.draw.utils.STATIC_VARS;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class Engine implements DrawingEngine {

    private static Engine instance = null;

    List<Shape> shapes;
    private Color color;
    private Stack<ActionCommand> redoStack;
    private Stack<ActionCommand> undoStack;
    private Map<String, Class> loadedClasses;
    private List<Shape> capturedShapes;

    private Engine() {
        redoStack = new Stack<>();
        undoStack = new Stack<>();
        shapes = new ArrayList<>();
        color = Color.BLACK;
        loadedClasses = new HashMap<>();
        loadedClasses.put("Line", null);
        loadedClasses.put("Circle", null);
        loadedClasses.put("Ellipse", null);
        loadedClasses.put("Rectangle", null);
        loadedClasses.put("Square", null);
        loadedClasses.put("Triangle", null);

    }

    @Override
    public void refresh(Graphics canvas) {
        for (Shape shape : shapes)
            shape.draw(canvas);
    }

    @Override
    public void addShape(Shape shape) {
        shapes.add(shape);
        addAction(new ActionCommand(() -> {
            shapes.remove(shape);
        }, () -> {
            shapes.add(shape);
        }));
    }

    @Override
    public void removeShape(Shape shape) {
        // Comparing references so that overlapping identical shapes are distinguished.
        if (!shapes.removeIf(k -> k.equals(shape))) {
            throw new ShapeNotFoundException("Removing non-existing Shape");
        }
        addAction(new ActionCommand(() -> {
            shapes.add(shape);
        }, () -> {
            shapes.remove(shape);
        }));
    }

    @Override
    public void updateShape(Shape oldShape, Shape newShape) {
        shapes.remove(oldShape);
        shapes.add(newShape);
        addAction(new ActionCommand(() -> {
            shapes.remove(newShape);
        }, () -> {
            shapes.add(oldShape);
        }));
    }

    @Override
    public Shape[] getShapes() {
        Shape[] returnedShapes = new Shape[shapes.size()];
        int count = 0;
        for (Shape shape : shapes) {
            returnedShapes[count++] = shape;
        }
        return returnedShapes;
    }

    @Override
    public List<Class<? extends Shape>> getSupportedShapes() {
        List<Class<? extends Shape>> ans = new ArrayList<Class<? extends Shape>>();
        JarFile jarFile;
        try {

            BufferedReader reader = new BufferedReader(new FileReader("jarPath.txt"));
            String path = reader.readLine();
            reader.close();

            jarFile = new JarFile(path);

            Enumeration<JarEntry> e = jarFile.entries();

            URL[] urls = {new URL("jar:file:" + path + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0, je.getName().length() - 6);
                String[] tmp = className.split("/");
                if (!(tmp[tmp.length - 2].equals("model") && tmp[tmp.length - 3].equals("draw"))) continue;
                className = className.replace('/', '.');
                @SuppressWarnings("unchecked")
                Class<? extends Shape> c = (Class<? extends Shape>) cl.loadClass(className);
                if (className.equals("eg.edu.alexu.csd.oop.draw.model.AbstractShape")) continue;
                Object x = c.newInstance();
                if (x instanceof Shape) {
                    ans.add(c);
                }
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return ans;
    }

    @Override
    public void undo() {
        if (undoStack.size() > 20) {
            Stack<ActionCommand> tmp = new Stack<>();
            while (!undoStack.isEmpty()) tmp.push(undoStack.pop());
            while (tmp.size() > 20) tmp.pop();
            while (!tmp.isEmpty()) undoStack.push(tmp.pop());
        }
        if (undoStack.empty()) return;
        ActionCommand command = undoStack.pop();
        command.undo();
        redoStack.push(command);
        if (redoStack.size() == 21) {
            Stack<ActionCommand> tmp = new Stack<>();
            while (!redoStack.isEmpty()) tmp.push(redoStack.pop());
            tmp.pop();
            while (!tmp.isEmpty()) redoStack.push(tmp.pop());
        }
    }

    @Override
    public void redo() {
        if (redoStack.empty()) return;
        ActionCommand command = redoStack.pop();
        command.redo();
        undoStack.push(command);
    }

    @Override
    public void save(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeInt(shapes.size());
            for (Shape i : shapes)
                oos.writeObject(i);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            int n = ois.readInt();
            shapes = new ArrayList<>();
            while (n-- > 0) {
                shapes.add((Shape) ois.readObject());
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
        for (Shape shape : shapes) {
            if (!shape.isSelected() && shape.isOnBoarder(point)) {
                shape.select();
                return;
            }
        }
        unSelectAll();
    }

    public void unSelectAll() {
        for (Shape shape : shapes) shape.unSelect();
    }

    public List<Shape> deleteSelectedShapes() {
        List<Shape> deletedShapes = getSelectedShapes();
        List<Shape> deletedShapesClone = new ArrayList<>(deletedShapes);
        addAction(new ActionCommand(() -> shapes.addAll(deletedShapesClone), () -> shapes.removeAll(deletedShapesClone)));
        shapes.removeAll(deletedShapes);
        return deletedShapes;
    }

    public void setColor(Color color) throws CloneNotSupportedException {
        List<Shape> selectedShapes = getSelectedShapes();
        List<Shape> coloredShapes = new ArrayList<>();
        this.color = color;
        unSelectAll();
        for (Shape shape : selectedShapes) {
            Shape coloredShape = (Shape) shape.clone();
            coloredShape.setColor(color);
            coloredShapes.add(coloredShape);
        }
        shapes.removeAll(selectedShapes);
        shapes.addAll(coloredShapes);
        addAction(new ActionCommand(() -> {
            shapes.removeAll(coloredShapes);
            shapes.addAll(selectedShapes);
        }, () -> {
            shapes.removeAll(selectedShapes);
            shapes.addAll(coloredShapes);
        }));
    }

    public synchronized static Engine getInstance() {
        return instance == null ? instance = new Engine() : instance;
    }

    public Point getMovingCenter(Point point) {
        for (Object centerPoint : shapes.stream().filter(Shape::isSelected).map(k -> k.getMovedCenterPoint()).collect(Collectors.toList())) {
            if (CalculationHelper.dist((Point) centerPoint, point) <= STATIC_VARS.CENTERS_RADIUS) {
                captureSelectedShapes();
                return (Point) centerPoint;
            }
        }
        return null;
    }


    public void moveSelectedShapes(Point point, Point circle) {
        if (circle == null) return;
        int deltaX = point.x - circle.x;
        int deltaY = point.y - circle.y;

        shapes.stream()
                .filter(Shape::isSelected)
                .forEach(k -> k.moveCenter(deltaX, deltaY));
    }

    public void scaleSelectedShapes(int sliderVal) {
        shapes.stream()
                .filter(Shape::isSelected)
                .forEach(k -> k.setScale(sliderVal));
    }

    public void resizeSelectedShapes() {
        List<Shape> resizedShapes = new ArrayList<>();
        List<Shape> originalShapes = shapes.stream()
                .filter(Shape::isSelected)
                .filter(k -> k.getScale() != STATIC_VARS.ORIGINAL_SHAPE_SCALE)
                .collect(Collectors.toList());
        if (originalShapes.isEmpty()) return;

        for (Shape shape : originalShapes) {
            try {
                Shape clonedShape = (Shape) shape.clone();
                shape.clearScale();
                resizedShapes.add(clonedShape);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        shapes.removeAll(originalShapes);
        resizedShapes.forEach(Shape::resize);
        shapes.addAll(resizedShapes);
        addAction(new ActionCommand(
                () -> {
                    shapes.removeAll(resizedShapes);
                    shapes.addAll(originalShapes);
                },
                () -> {
                    shapes.removeAll(originalShapes);
                    shapes.addAll(resizedShapes);
                }));
    }

    public List<Shape> getSelectedShapes() {
        return shapes.stream().filter(Shape::isSelected).collect(Collectors.toList());
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
            nameOfClass = str.substring(++i, str.lastIndexOf('.'));
            URL name;

            name = new URL(str.substring(0, i));

            return loadClass(name, nameOfClass);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void captureSelectedShapes() {
        if (capturedShapes != null) return;
        capturedShapes = getSelectedShapes();
    }

    private String loadClass(URL name, String nameOfClass) {
        System.out.println(name.toString());
        URL[] my = {name};
        URLClassLoader classloader = new URLClassLoader(my);
        try {
            Class myClass = classloader.loadClass(STATIC_VARS.PACKAGE_NAME + nameOfClass);
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

    public Shape createLoadedClassShape(String curButton) {
        try {
            return (Shape) loadedClasses.get(curButton).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void actionMovedShapes() {
        if (capturedShapes == null) return;
        capturedShapes = null;
        List<Shape> originalShapes = getSelectedShapes();
        List<Shape> movedShapes = new ArrayList<>();
        for (Shape shape : originalShapes) {
            try {
                movedShapes.add((Shape) shape.clone());
                shape.resetMovement();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        originalShapes.forEach(Shape::unSelect);
        movedShapes.forEach(Shape::applyMovement);
        shapes.removeAll(originalShapes);
        shapes.addAll(movedShapes);
        addAction(new ActionCommand(() -> {
            shapes.removeAll(movedShapes);
            shapes.addAll(originalShapes);
        },
                () -> {
                    shapes.removeAll(originalShapes);
                    shapes.addAll(movedShapes);
                }));
    }
}
