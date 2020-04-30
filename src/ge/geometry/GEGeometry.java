package ge.geometry;

import ge.scene.GENode;
import ge.scene.GEScene;
import ge.utils.GEMouseEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.Vector;

public class GEGeometry {

    private Group group;
    private Shape shape;

    private double[] points;
    private double[] initialPoints;

    private Bounds initialBounds;

    private Color fillColor;
    private Color strokeColor;
    private double strokeWidth;

//    private double scaleX;
//    private double scaleY;

    private Vector<GEMouseEvent> mouseEvents;

    public GEGeometry(double[] _points){
        points = _points;

        shape = new Polygon(points);

        group = new Group();
        group.getChildren().add(shape);

        initialBounds = shape.getLayoutBounds();
        initialPoints = points.clone();
//        scaleX = 1;
//        scaleY = 1;
        mouseEvents = new Vector<GEMouseEvent>(0);
    }

    public GEGeometry(Text text){
        points = null;

        shape = text;

        group = new Group();
        group.getChildren().add(shape);

        initialBounds = shape.getLayoutBounds();
        initialPoints = null;
//        scaleX = 1;
//        scaleY = 1;
        mouseEvents = new Vector<GEMouseEvent>(0);
    }

    public void updateShapePoints(double[] _points){
        points = _points;

        double posX = shape.getLayoutX();
        double posY = shape.getLayoutY();

        group.getChildren().remove(shape);
        shape = new Polygon(points);
        moveTo(posX, posY);
        setFillColor(fillColor);
        setStrokeColor(strokeColor);
        setStrokeWidth(strokeWidth);
        // Don't use addMouseEvent because mouseEvents will contain duplicate events after it if used here
        for (int i = 0; i < mouseEvents.size(); i++) {
            GEMouseEvent event = mouseEvents.elementAt(i);
            shape.addEventHandler(event.type, event.handler);
        }
        group.getChildren().add(shape);
    }

    public void scaleTo(double x, double y){
        if (initialPoints != null) {
            double[] scaledPoints = initialPoints.clone();
            for (int i = 0; i < scaledPoints.length; i++) {
                if (i % 2 == 0){
                    scaledPoints[i] = scaledPoints[i]*x;
                } else {
                    scaledPoints[i] = scaledPoints[i]*y;
                }
            }

            updateShapePoints(scaledPoints);

        } else {
            shape.setScaleX(x);
            shape.setScaleY(y);
        }

//        scaleX = x;
//        scaleY = y;
    }

    public void setFillColor(Color color){
        shape.setFill(color);
        fillColor = color;
    }

    public void setStrokeColor(Color color){
        shape.setStroke(color);
        strokeColor = color;
    }

    public void setStrokeWidth(double w){
        shape.setStrokeWidth(w);
        strokeWidth = w;
    }

    public void moveTo(double x, double y){
        shape.setLayoutX(x);
        shape.setLayoutY(y);
    }

    public void addMouseEvent(EventType<MouseEvent> type, EventHandler<MouseEvent> handler){
        GEMouseEvent event = new GEMouseEvent(type, handler);
        shape.addEventHandler(type, handler);
        mouseEvents.addElement(event);
    }

    public void addMouseEvent(GEMouseEvent event){
        shape.addEventHandler(event.type, event.handler);
        mouseEvents.addElement(event);
    }

    // - Use only if you know what you are doing
    public Group getGroup(){
        return group;
    }
    public Shape getShape(){
        return shape;
    }
    // - ---------------------------------------

    public double[] getPoints(){
        return points.clone();
    }

    public Bounds getInitialBounds(){
        return initialBounds;
    }

    public Bounds getBounds(){
        return shape.getLayoutBounds();
    }
}
