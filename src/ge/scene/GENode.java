package ge.scene;

import ge.geometry.GEGeometry;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
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

//    public Shape getShape(){
//        if (geometry != null) {
//            return geometry.getShape();
//        } else {
//            return null;
//        }
//    }

    public GEGeometry getGeometry(){
        return geometry;
    }

    public void setFillColor(Color color){
        getGeometry().getShape().setFill(color);
    }

    public void setStrokeColor(Color color){
        getGeometry().getShape().setStroke(color);
    }
    
    public void setColor(Color color){
        setFillColor(color);
        setStrokeColor(color);
    }

    public void setStrokeWidth(double w){
        getGeometry().getShape().setStrokeWidth(w);
    }

    public void moveTo(double x, double y){
        Shape buffShape = getGeometry().getShape();
        if (buffShape != null) {
            buffShape.setLayoutX(x);
            buffShape.setLayoutY(y);
        } else {
            System.out.print(">Unable to move node (geometry is null) : ");
            System.out.println(this);
        }
    }

    public void addClickEvent(EventHandler<MouseEvent> h){
        getGeometry().getShape().addEventHandler(MouseEvent.MOUSE_CLICKED,h);
    }

}
