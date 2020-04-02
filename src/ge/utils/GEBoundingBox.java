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

    public double getWidth(){
        return point2.x - point1.x;
    }

    public double getHeight(){
        return point2.y - point1.y;
    }

}
