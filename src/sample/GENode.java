package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class GENode {

    private GEGeometry geometry;
    private String name;

    public GENode(){
        geometry = null;
    }

    public void setGeometry(GEGeometry _geometry){
        geometry = _geometry;
    }

    public Shape getShape(){
        if (geometry != null) {
            return geometry.getShape();
        } else {
            return null;
        }
    }

    public void setColor(Color color){
        getShape().setFill(color);
    }

    public void moveTo(double x, double y){
        if (geometry != null) {
            geometry.getShape().setLayoutX(x);
            geometry.getShape().setLayoutY(y);
        } else {
            System.out.print(">Unable to move node (geometry is null) : ");
            System.out.println(this);
        }
    }

}
