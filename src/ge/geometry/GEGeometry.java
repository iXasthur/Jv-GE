package ge.geometry;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class GEGeometry {

    private Shape shape = null;
    private double[] points = null;

    public GEGeometry(double[] points){
        shape = new Polygon(points);
        this.points = points;
    }

    public GEGeometry(Text text){
        shape = text;
        points = new double[]{};
    }

    public Shape getShape() {
        return shape;
    }

    public double[] getPoints(){
        return points;
    }
}
