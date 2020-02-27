package sample;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class GEGeometry {

    private Shape shape = null;

    public GEGeometry(double[] points){
        shape = new Polygon(points);
    }

    public GEGeometry(Shape _shape){
        shape = _shape;
    }

    public Shape getShape() {
        return shape;
    }
}
