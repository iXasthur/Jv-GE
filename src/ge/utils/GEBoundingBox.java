package ge.utils;

import java.awt.*;

public class GEBoundingBox {

    private Point point1;
    private Point point2;

    public GEBoundingBox(double x1, double y1, double x2, double y2){
        point1 = new Point();
        point2 = new Point();

        point1.setLocation(x1, y1);
        point2.setLocation(x2, y2);
    }

    public void setPoint1(double x, double y){
        point1.setLocation(x, y);
    }

    public void setPoint2(double x, double y){
        point2.setLocation(x, y);
    }

    public Point getPoint1(){
        return point1;
    }

    public Point getPoint2(){
        return point2;
    }

    public Point getCenterPoint(){
        return new Point((point1.x + point2.x)/2, (point1.y + point2.y)/2);
    }

    public double getWidth(){
        return Math.abs(point2.x - point1.x);
    }

    public double getHeight(){
        return Math.abs(point2.y - point1.y);
    }

    public double getWidthNoModulo(){
        return point2.x - point1.x;
    }

    public double getHeightNoModulo(){
        return point2.y - point1.y;
    }

}
