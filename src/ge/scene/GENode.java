package ge.scene;

import ge.geometry.GEGeometry;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GENode {

    private final GEGeometry geometry;
//    private double posX;
//    private double posY;

    public GENode(GEGeometry _geometry){
        geometry = _geometry;
//        posX = 0;
//        posY = 0;
    }

//    public void updateGeometry(double[] points){
//
//    }

    public GEGeometry getGeometry(){
        return geometry;
    }

    public void setFillColor(Color color){
        geometry.setFillColor(color);
    }

    public void setStrokeColor(Color color){
        geometry.setStrokeColor(color);
    }
    
    public void setColor(Color color){
        setFillColor(color);
        setStrokeColor(color);
    }

    public void setStrokeWidth(double w){
        geometry.setStrokeWidth(w);
    }

    public void moveTo(double x, double y){
        geometry.moveTo(x, y);
//        posX = x;
//        posY = y;
    }

    public void scaleTo(double x, double y){
        geometry.scaleTo(x, y);
    }

    public void addClickEvent(EventHandler<MouseEvent> h){
        geometry.addMouseEvent(MouseEvent.MOUSE_CLICKED, h);
    }

    public void addMoveEvent(EventHandler<MouseEvent> h){
        geometry.addMouseEvent(MouseEvent.MOUSE_MOVED, h);
    }
}
