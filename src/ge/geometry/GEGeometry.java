package ge.geometry;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class GEGeometry {

    private Shape shape = null;
    private double[] points = new double[]{};

    public GEGeometry(double[] points){
        shape = new Polygon(points);
        this.points = points;
    }

//    public GEGeometry(Shape _shape){
//        shape = _shape;
//    }

    public Shape getShape() {
        return shape;
    }

    public double[] getPoints(){
        return points;
    }
}
