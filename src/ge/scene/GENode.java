package ge.scene;

import ge.geometry.GEGeometry;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.awt.*;
import java.util.Vector;

public class GENode {

    private GEGeometry geometry;
    private String name;
    private Color fillColor;
    private Color strokeColor;
    private Vector<EventHandler<MouseEvent>> clickEvents;
    private Bounds initialBounds;
    private double[] initialPoints;
    private double strokeWidth;
    private double posX;
    private double posY;
    private double scaleX;
    private double scaleY;

    public GENode(){
        geometry = null;
        initialBounds = null;
        initialPoints = null;
        posX = 0;
        posY = 0;
        scaleX = 1;
        scaleY = 1;
        clickEvents = new Vector<EventHandler<MouseEvent>>(0);
    }

    public void setGeometry(GEGeometry _geometry){
        geometry = _geometry;
        initialBounds = geometry.getShape().getLayoutBounds();
        initialPoints = geometry.getPoints().clone();
        clickEvents = new Vector<EventHandler<MouseEvent>>(0);
        scaleX = 1;
        scaleY = 1;
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

    private void setFillColor(Color color){
        getGeometry().getShape().setFill(color);
        fillColor = color;
    }

    private void setStrokeColor(Color color){
        getGeometry().getShape().setStroke(color);
        strokeColor = color;
    }
    
    public void setColor(Color color){
        setFillColor(color);
        setStrokeColor(color);
    }

    public void setStrokeWidth(double w){
        getGeometry().getShape().setStrokeWidth(w);
        strokeWidth = w;
    }

    public void moveTo(double x, double y){
        Shape buffShape = getGeometry().getShape();
        if (buffShape != null) {
            buffShape.setLayoutX(x);
            buffShape.setLayoutY(y);
            posX = x;
            posY = y;
        } else {
            System.out.print(">Unable to move node (geometry is null) : ");
            System.out.println(this);
        }
    }

    public void addClickEvent(EventHandler<MouseEvent> h){
        getGeometry().getShape().addEventHandler(MouseEvent.MOUSE_CLICKED,h);
        if (!clickEvents.contains(h)) {
            clickEvents.addElement(h);
        }
    }

    private void updateShape(){
        setFillColor(fillColor);
        setStrokeColor(strokeColor);
        setStrokeWidth(strokeWidth);
        moveTo(posX, posY);
        for (EventHandler<MouseEvent> e: clickEvents){
            addClickEvent(e);
        }
    }

    public void scaleTo(GEScene scene, double x, double y){
        if (initialPoints != null) {
            double[] buffPoints = initialPoints.clone();
            for (int i = 0; i < buffPoints.length; i++) {
                if (i % 2 == 0){
                    buffPoints[i] = buffPoints[i]*x;
                } else {
                    buffPoints[i] = buffPoints[i]*y;
                }
            }
            scene.removeNodeFromSelectedLayer(this);
            geometry = new GEGeometry(buffPoints);
            updateShape();
            scene.addNodeToSelectedLayer(this);

            scaleX = x;
            scaleY = y;
        } else {
            getGeometry().getShape().setScaleX(x);
            getGeometry().getShape().setScaleY(y);
        }
    }

    public double getInitialWidth(){
        return initialBounds.getWidth();
    }

    public double getInitialHeight(){
        return initialBounds.getHeight();
    }
}
