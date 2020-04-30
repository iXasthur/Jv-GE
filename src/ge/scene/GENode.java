package ge.scene;

import ge.geometry.GEGeometry;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GENode {

    private final GEGeometry geometry;

    public GENode(GEGeometry _geometry){
        geometry = _geometry;
    }

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

    public Color getFillColor(){
        return geometry.getFillColor();
    }

    public Color getStrokeColor(){
        return geometry.getStrokeColor();
    }

    public void setStrokeWidth(double w){
        geometry.setStrokeWidth(w);
    }

    public void moveTo(double x, double y){
        geometry.moveTo(x, y);
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
