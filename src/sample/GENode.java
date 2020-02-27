package sample;

import javafx.scene.shape.Shape;

public class GENode {

    private GEGeometry geometry;

    public GENode(){
        geometry = null;
    }

    public void setGeometry(GEGeometry _geometry){
        geometry = _geometry;
    }

    public Shape getShape(){
        return geometry.getShape();
    }

    public void moveTo(double x, double y){
        geometry.getShape().setLayoutX(x);
        geometry.getShape().setLayoutY(y);
    }

}
