package sample;

import javafx.scene.shape.Polygon;

public class GEGeometry extends Polygon {

    public GEGeometry(double[] _points){
        super(_points);
    }

    public void setScaleXY(double factor){
        this.setScaleX(factor);
        this.setScaleY(factor);
    }

}
