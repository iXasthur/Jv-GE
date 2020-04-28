package ge.utils;

import ge.geometry.GEGeometry;
import ge.geometry.GESquare;
import ge.scene.GENode;
import ge.scene.GEScene;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

public class GEPolygonCreator {

    public enum creatorStates {
        WAITING_FOR_UI_CREATION,
        WAITING_FOR_POINT
    }

    private GEScene scene = null;
    private creatorStates state = null;
    private Vector<Point2D> points = null;
    private GENode polygonPreview = null;

    public GEPolygonCreator(GEScene _scene){
        scene = _scene;
        state = creatorStates.WAITING_FOR_UI_CREATION;
        points = new Vector<>(0);
    }

    public void createUI(EventHandler<MouseEvent> exitClickHandler) {
        createBackgroundNode(backgroundClickHandler);
        createExitButton(exitClickHandler);
        state = creatorStates.WAITING_FOR_POINT;
    }

    private void createExitButton(EventHandler<MouseEvent> clickHandler){
        double posX = GEUIConstraints.safeAreaX/2;
        double posY = GEUIConstraints.safeAreaX/2;

        GENode buffButton = new GENode();
        buffButton.setGeometry(new GESquare(new GERegularBoundingBox(GEUIConstraints.buttonWidth)));
        buffButton.setStrokeWidth(3);
        buffButton.setColor(GEColor.stdUINodeColor);
        buffButton.moveTo(posX, posY);
        buffButton.addClickEvent(clickHandler);
        scene.addNodeToSelectedLayer(buffButton);
    }

    private void createBackgroundNode(EventHandler<MouseEvent> clickHandler){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        javafx.scene.paint.Color bgColor = Color.rgb(24,24,24);

        GENode bgRectangle = new GENode();
        GERegularBoundingBox boundingBox = new GERegularBoundingBox(screenSize.width*2);
        bgRectangle.setGeometry(new GESquare(boundingBox));
        bgRectangle.moveTo(screenSize.width/2.0,screenSize.height/2.0);
        bgRectangle.setColor(bgColor);
        bgRectangle.addClickEvent(clickHandler);
        scene.addNodeToSelectedLayer(bgRectangle);
    }

    private void updatePreview() {
        if (polygonPreview != null) {
            scene.removeNodeFromSelectedLayer(polygonPreview);
        }
        polygonPreview = new GENode();
        polygonPreview.setGeometry(new GEGeometry(getPointsFlat()));
        polygonPreview.setStrokeWidth(3);
        polygonPreview.setColor(GEColor.stdUINodeColor);
        scene.addNodeToSelectedLayer(polygonPreview);
    }

    private EventHandler<MouseEvent> backgroundClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            switch (state) {
                case WAITING_FOR_UI_CREATION:
                    break;
                case WAITING_FOR_POINT:
                    addPoint(e.getSceneX(), e.getSceneY());
                    updatePreview();
                    break;
            }
            System.out.println("CLICKED BG IN POLYGONCREATOR");
        }
    };

    public creatorStates getState() {
        return state;
    }

    public void setState(creatorStates _state) {
        state = _state;
    }

    public void addPoint(double x, double y) {
        points.add(new Point2D(x, y));
    }

    public static double[] getSamplePolygonPoints(GERegularBoundingBox boundingBox){
        double[] buffPoints = new double[5*2];

        buffPoints[0] = 0;
        buffPoints[1] = 0;

        buffPoints[2] = -boundingBox.size/2;
        buffPoints[3] = 0;

        buffPoints[4] = 0;
        buffPoints[5] = -boundingBox.size/2;

        buffPoints[6] = boundingBox.size/2;
        buffPoints[7] = 0;

        buffPoints[8] = 0;
        buffPoints[9] = boundingBox.size/2;

        return buffPoints;
    }

    public double[] getNormalizedPointsFlat() {
        double[] buffPoints = getPointsFlat();

        double minX = buffPoints[0];
        double maxX = buffPoints[0];
        double minY = buffPoints[1];
        double maxY = buffPoints[1];

        for (int i = 0; i < buffPoints.length; i++) {
            if (i % 2 == 0) {
                // X
                if (buffPoints[i] < minX) {
                    minX = buffPoints[i];
                } else
                    if (buffPoints[i] > maxX) {
                        maxX = buffPoints[i];
                    }
            } else {
                // Y
                if (buffPoints[i] < minY) {
                    minY = buffPoints[i];
                } else
                if (buffPoints[i] > maxY) {
                    maxY = buffPoints[i];
                }
            }
        }

        double width = maxX - minX;
        double height = maxY - minY;

        double scaleFactor = 1;

        if (width >= height) {
            scaleFactor = GEUIConstraints.buttonWidth/(width);
        } else {
            scaleFactor = GEUIConstraints.buttonWidth/(height);
        }

        double offsetX = -(minX + width/2);
        double offsetY = -(minY + height/2);

        // Apply offset then scale
        for (int i = 0; i < buffPoints.length; i++) {
            if (i % 2 == 0) {
                // X
                buffPoints[i] = buffPoints[i] + offsetX;
                buffPoints[i] = buffPoints[i]*scaleFactor;
            } else {
                // Y
                buffPoints[i] = buffPoints[i] + offsetY;
                buffPoints[i] = buffPoints[i]*scaleFactor;
            }
        }

        return buffPoints;
    }

    public double[] getPointsFlat() {
        double[] buffPoints = new double[points.size()*2];
        for (int i = 0; i < points.size(); i++) {
            buffPoints[i*2] = points.elementAt(i).getX();
            buffPoints[i*2+1] = points.elementAt(i).getY();
        }
        return buffPoints;
    }
}
