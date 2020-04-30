//package ge.utils;
//
//import ge.geometry.GEGeometry;
//import ge.geometry.GESquare;
//import ge.scene.GENode;
//import ge.scene.GEScene;
//import javafx.event.EventHandler;
//import javafx.geometry.Point2D;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//
//
//import java.awt.*;
//import java.util.Vector;
//
//public class GEPolygonCreator {
//
//    public enum creatorStates {
//        WAITING_FOR_UI_CREATION,
//        WAITING_FOR_POINT
//    }
//
//    private final String hintFirstText = "Click to create polygon by points!";
//    private final String hintPolygonPointsText = "Polygon points:";
//
//    private creatorStates state = null;
//    private Vector<Point2D> points = null;
//    private GENode polygonStaticPreview = null;
////    private GENode polygonDynamicPreview = null;
//
//    private Text hintText = null;
//
//    public GEPolygonCreator(int z){
//        scene = _scene;
//        state = creatorStates.WAITING_FOR_UI_CREATION;
//        points = new Vector<>(0);
//    }
//
//    public void createUI(EventHandler<MouseEvent> exitClickHandler) {
//        createBackgroundNode();
//        createExitButton(exitClickHandler);
//        createHint(GEUIConstraints.safeAreaX, GEUIConstraints.safeAreaX/2.0 + GEUIConstraints.fontSize/4);
//        state = creatorStates.WAITING_FOR_POINT;
//    }
//
//    private void updateHintText() {
//        StringBuilder pointsString = new StringBuilder(hintPolygonPointsText);
//        for (int i = 0; i < points.size(); i++) {
//            pointsString.append('\n');
//            pointsString.append("(");
//            pointsString.append(points.elementAt(i).getX());
//            pointsString.append(", ");
//            pointsString.append(points.elementAt(i).getY());
//            pointsString.append(")");
//        }
//        hintText.setText(pointsString.toString());
//    }
//
//    private void createHint(double posX, double posY){
//        hintText = new Text();
//        hintText.setText(hintFirstText);
//        hintText.setFont(new Font(GEUIConstraints.fontSize));
//
//        GENode buff = new GENode();
//        buff.setGeometry(new GEGeometry(hintText));
//        buff.moveTo(posX, posY);
//        buff.setColor(GEColor.stdUINodeColor);
//        buff.addClickEvent(backgroundClickHandler);
//        buff.addMoveEvent(backgroundMoveHandler);
//        scene.addNodeToSelectedLayer(buff);
//    }
//
//    private void createExitButton(EventHandler<MouseEvent> clickHandler){
//        double posX = GEUIConstraints.safeAreaX/2;
//        double posY = GEUIConstraints.safeAreaX/2;
//
//        GENode buffButton = new GENode();
//        buffButton.setGeometry(new GESquare(new GERegularBoundingBox(GEUIConstraints.buttonWidth)));
//        buffButton.setStrokeWidth(3);
//        buffButton.setColor(GEColor.stdUINodeColor);
//        buffButton.moveTo(posX, posY);
//        buffButton.addClickEvent(clickHandler);
//        buffButton.addMoveEvent(backgroundMoveHandler);
//        scene.addNodeToSelectedLayer(buffButton);
//    }
//
//    private void createBackgroundNode(){
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//
//        javafx.scene.paint.Color bgColor = GEColor.stdBackgroundColor;
//
//        GENode bgRectangle = new GENode();
//        GERegularBoundingBox boundingBox = new GERegularBoundingBox(screenSize.width*2);
//        bgRectangle.setGeometry(new GESquare(boundingBox));
//        bgRectangle.moveTo(screenSize.width/2.0,screenSize.height/2.0);
//        bgRectangle.setColor(bgColor);
//        bgRectangle.addClickEvent(backgroundClickHandler);
//        bgRectangle.addMoveEvent(backgroundMoveHandler);
//        scene.addNodeToSelectedLayer(bgRectangle);
//    }
//
//    private void updatePreview() {
//        if (polygonStaticPreview != null) {
//            scene.removeNodeFromSelectedLayer(polygonStaticPreview);
//        }
//        polygonStaticPreview = new GENode();
//        polygonStaticPreview.setGeometry(new GEGeometry(getPointsFlat()));
//        polygonStaticPreview.setStrokeWidth(3);
//        polygonStaticPreview.setColor(GEColor.stdUINodeColor);
//        polygonStaticPreview.setFillColor(GEColor.stdBackgroundColor);
//        polygonStaticPreview.addClickEvent(backgroundClickHandler);
//        polygonStaticPreview.addMoveEvent(backgroundMoveHandler);
//        scene.addNodeToSelectedLayer(polygonStaticPreview);
//    }
//
//    public creatorStates getState() {
//        return state;
//    }
//
//    public void setState(creatorStates _state) {
//        state = _state;
//    }
//
//    public void addPoint(double x, double y) {
//        points.add(new Point2D(x, y));
//    }
//
//    private void removeLastPoint() {
//        points.remove(points.lastElement());
//    }
//
//    public static double[] getSamplePolygonPoints(GERegularBoundingBox boundingBox){
//        double[] buffPoints = new double[5*2];
//
//        buffPoints[0] = 0;
//        buffPoints[1] = 0;
//
//        buffPoints[2] = -boundingBox.size/2;
//        buffPoints[3] = 0;
//
//        buffPoints[4] = 0;
//        buffPoints[5] = -boundingBox.size/2;
//
//        buffPoints[6] = boundingBox.size/2;
//        buffPoints[7] = 0;
//
//        buffPoints[8] = 0;
//        buffPoints[9] = boundingBox.size/2;
//
//        return buffPoints;
//    }
//
//    public double[] getNormalizedPointsFlat() {
//        double[] buffPoints = getPointsFlat();
//
//        if (buffPoints.length < 6) {
//            return buffPoints;
//        }
//
//        double minX = buffPoints[0];
//        double maxX = buffPoints[0];
//        double minY = buffPoints[1];
//        double maxY = buffPoints[1];
//
//        for (int i = 0; i < buffPoints.length; i++) {
//            if (i % 2 == 0) {
//                // X
//                if (buffPoints[i] < minX) {
//                    minX = buffPoints[i];
//                } else
//                    if (buffPoints[i] > maxX) {
//                        maxX = buffPoints[i];
//                    }
//            } else {
//                // Y
//                if (buffPoints[i] < minY) {
//                    minY = buffPoints[i];
//                } else
//                if (buffPoints[i] > maxY) {
//                    maxY = buffPoints[i];
//                }
//            }
//        }
//
//        double width = maxX - minX;
//        double height = maxY - minY;
//
//        double scaleFactor = 1;
//
//        if (width >= height) {
//            scaleFactor = GEUIConstraints.buttonWidth/(width);
//        } else {
//            scaleFactor = GEUIConstraints.buttonWidth/(height);
//        }
//
//        double offsetX = -(minX + width/2);
//        double offsetY = -(minY + height/2);
//
//        // Apply offset then scale
//        for (int i = 0; i < buffPoints.length; i++) {
//            if (i % 2 == 0) {
//                // X
//                buffPoints[i] = buffPoints[i] + offsetX;
//                buffPoints[i] = buffPoints[i]*scaleFactor;
//            } else {
//                // Y
//                buffPoints[i] = buffPoints[i] + offsetY;
//                buffPoints[i] = buffPoints[i]*scaleFactor;
//            }
//        }
//
//        return buffPoints;
//    }
//
//    public double[] getPointsFlat() {
//        double[] buffPoints = new double[points.size()*2];
//        for (int i = 0; i < points.size(); i++) {
//            buffPoints[i*2] = points.elementAt(i).getX();
//            buffPoints[i*2+1] = points.elementAt(i).getY();
//        }
//        return buffPoints;
//    }
//
//    private EventHandler<MouseEvent> backgroundClickHandler = new EventHandler<MouseEvent>() {
//        @Override
//        public void handle(MouseEvent e) {
//            switch (state) {
//                case WAITING_FOR_UI_CREATION:
//                    break;
//                case WAITING_FOR_POINT:
//                    addPoint(e.getSceneX(), e.getSceneY());
//                    updatePreview();
//                    updateHintText();
//                    break;
//            }
//            System.out.println("CLICKED BG IN POLYGONCREATOR");
//        }
//    };
//
//    private EventHandler<MouseEvent> backgroundMoveHandler = new EventHandler<MouseEvent>() {
//        @Override
//        public void handle(MouseEvent e) {
//            switch (state) {
//                case WAITING_FOR_UI_CREATION:
//                    break;
//                case WAITING_FOR_POINT:
//                    addPoint(e.getSceneX(), e.getSceneY());
//                    updatePreview();
//                    updateHintText();
//                    removeLastPoint();
//                    break;
//            }
//        }
//    };
//}
