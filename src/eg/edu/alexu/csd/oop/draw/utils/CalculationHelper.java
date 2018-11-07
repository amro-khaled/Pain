package eg.edu.alexu.csd.oop.draw.utils;

import java.awt.*;

public class CalculationHelper {
    public static double slop(Point a, Point b){
        return 0.0;
    }
    public static boolean isSameLine(Point firstPoint, Point middlePoint, Point secondPoint){
        return Math.abs(dist(firstPoint, middlePoint) + dist(middlePoint, secondPoint) - dist(firstPoint, secondPoint)) <= STATIC_VARS.SELECTION_PRECISION;
    }
    public static double dist(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

}
