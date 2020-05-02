import ge.geometry.GEGeometry;
import ge.geometry.GESquare;
import ge.scene.GENode;
import ge.scene.GEScene;
import ge.utils.*;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


import java.awt.*;
import java.util.Vector;

public class PolygonCreatorScene extends GEScene {

    public enum SceneState {
        WAITING_FOR_UNVEILING,
        WAITING_FOR_POINT
    }
    private SceneState state = null;

    private final String hintFirstText = "Click to create polygon by points!";
    private final String hintPolygonPointsText = "Polygon points:";

    private Vector<Point2D> points = null;
    private GENode polygonPreview = null;

    private Text mainHintText = null;
    private GENode mainHintNode = null;
    private Text keyHintText = null;
    private GENode keyHintNode = null;

    private boolean showDynamicPreview = true;
    private Point2D lastMouseMovePos = null;

    public PolygonCreatorScene(Scene rootScene){
        super(rootScene);
        setState(SceneState.WAITING_FOR_UNVEILING);

        points = new Vector<>(0);
        lastMouseMovePos = new Point2D(0, 0);
    }

    public void resetPolygon(){
        points = new Vector<>(0);
        lastMouseMovePos = new Point2D(0, 0);
        if (polygonPreview != null) {
            removeNodeFromMainLayer(polygonPreview);
        }

        polygonPreview =  new GENode(new GEGeometry(getPointsFlat()));
        polygonPreview.setStrokeWidth(3);
        polygonPreview.setColor(GEColor.stdUINodeColor);
        polygonPreview.setFillColor(GEColor.stdBackgroundColor);
        polygonPreview.addClickEvent(backgroundClickHandler);
        polygonPreview.addMoveEvent(backgroundMoveHandler);
        addNodeToMainLayer(polygonPreview);

        updateMainHintText();
    }

    public void createUI(EventHandler<MouseEvent> exitClickHandler) {
        createBackgroundNode();
        createExitButton(exitClickHandler);
        createMainHint(GEUIConstraints.safeAreaX, GEUIConstraints.safeAreaX/2.0 + GEUIConstraints.fontSize/4);
        createKeyHint();
    }

    private void updateMainHintText() {
        StringBuilder pointsString = new StringBuilder(hintPolygonPointsText);
        for (int i = 0; i < points.size(); i++) {
            pointsString.append('\n');
            pointsString.append("(");
            pointsString.append((int) points.elementAt(i).getX());
            pointsString.append(", ");
            pointsString.append((int) points.elementAt(i).getY());
            pointsString.append(")");
        }
        mainHintText.setText(pointsString.toString());
    }

    private void createKeyHint(){
        keyHintText = new Text();
        keyHintText.setFont(new Font(GEUIConstraints.fontSize));

        keyHintNode = new GENode(new GEGeometry(keyHintText));
        keyHintNode.moveTo(200, 150);
        keyHintNode.setColor(GEColor.stdUINodeColor);
        keyHintNode.addClickEvent(backgroundClickHandler);
        keyHintNode.addMoveEvent(backgroundMoveHandler);
        addNodeToUILayer(keyHintNode);
    }

    private void createMainHint(double posX, double posY){
        mainHintText = new Text();
        mainHintText.setText(hintFirstText);
        mainHintText.setFont(new Font(GEUIConstraints.fontSize));

        mainHintNode = new GENode(new GEGeometry(mainHintText));
        mainHintNode.moveTo(posX, posY);
        mainHintNode.setColor(GEColor.stdUINodeColor);
        mainHintNode.addClickEvent(backgroundClickHandler);
        mainHintNode.addMoveEvent(backgroundMoveHandler);
        addNodeToUILayer(mainHintNode);
    }

    private double[] createExitButtonPoints() {
        double[] points = new double[8*2];
        points[0] = 0;
        points[1] = 0;

        points[2] = -GEUIConstraints.buttonWidth/2.0;
        points[3] = -GEUIConstraints.buttonWidth/2.0;

        points[4] = 0;
        points[5] = 0;

        points[6] = -GEUIConstraints.buttonWidth/2.0;
        points[7] = GEUIConstraints.buttonWidth/2.0;

        points[8] = 0;
        points[9] = 0;

        points[10] = GEUIConstraints.buttonWidth/2.0;
        points[11] = GEUIConstraints.buttonWidth/2.0;

        points[12] = 0;
        points[13] = 0;

        points[14] = GEUIConstraints.buttonWidth/2.0;
        points[15] = -GEUIConstraints.buttonWidth/2.0;
        return points;
    }

    private void createExitButton(EventHandler<MouseEvent> clickHandler){
        double posX = GEUIConstraints.safeAreaX/2.0;
        double posY = GEUIConstraints.safeAreaX/2.0;

        GENode buffButton = new GENode(new GEGeometry(createExitButtonPoints()));
        buffButton.setStrokeWidth(5);
        buffButton.setColor(GEColor.stdUINodeColor);
        buffButton.moveTo(posX, posY);
        buffButton.addClickEvent(clickHandler);
        buffButton.addMoveEvent(backgroundMoveHandler);
        addNodeToUILayer(buffButton);
    }

    private void createBackgroundNode(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Color bgColor = GEColor.stdBackgroundColor;

        GERegularBoundingBox boundingBox = new GERegularBoundingBox(screenSize.width*2);

        GENode bgRectangle = new GENode(new GESquare(boundingBox));
        bgRectangle.moveTo(screenSize.width/2.0,screenSize.height/2.0);
        bgRectangle.setColor(bgColor);
        bgRectangle.addClickEvent(backgroundClickHandler);
        bgRectangle.addMoveEvent(backgroundMoveHandler);
        addNodeToBackgroundLayer(bgRectangle);
    }

    private void updatePreview() {
        polygonPreview.getGeometry().updateShapePoints(getPointsFlat());
    }

    public SceneState getState() {
        return state;
    }

    public void setState(SceneState _state) {
        state = _state;
    }

    public void addPoint(double x, double y) {
        points.add(new Point2D(x, y));
    }

    private void removeLastPoint() {
        points.remove(points.lastElement());
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

        if (buffPoints.length < 6) {
            return buffPoints;
        }

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

    public void updateKeyHint() {
        StringBuilder stringBuilder = new StringBuilder();
        if (showDynamicPreview) {
            stringBuilder.append("[T]: Disable dynamic preview");
        } else {
            stringBuilder.append("[T]: Enable dynamic preview");
        }
        if (points.size() > 0) {
            stringBuilder.append("\n[R]: Remove last point");
        }
        keyHintText.setText(stringBuilder.toString());

        Bounds textBounds = keyHintNode.getGeometry().getBounds();
        double offsetX = textBounds.getWidth() + GEUIConstraints.fontSize + GEUIConstraints.safeAreaX/4.0;
        double offsetY = textBounds.getHeight() + GEUIConstraints.fontSize + GEUIConstraints.safeAreaX/4.0;
        double posX = GEResizeListener.getWidth() - offsetX;
        double posY = GEResizeListener.getHeight() - offsetY;
        keyHintNode.moveTo(posX, posY);
    }

    public void configureKeyHint() {
        updateKeyHint();
        GEResizeListener.resizeAction = (width, height) -> {
            updateKeyHint();
        };
    }

    public void configureKeyListener(){
        GEKeyListener.pressAction = (keyEvent) -> {
            switch (keyEvent.getCode()) {
                case T:
                    showDynamicPreview = !showDynamicPreview;
                    if (showDynamicPreview) {
                        addPoint(lastMouseMovePos.getX(), lastMouseMovePos.getY());
                        updatePreview();
                        updateMainHintText();
                        updateKeyHint();
                        removeLastPoint();
                    } else {
                        updatePreview();
                        updateMainHintText();
                        updateKeyHint();
                    }
                    break;
                case R:
                    if (points.size() > 0) {
                        removeLastPoint();
                        updatePreview();
                        updateMainHintText();
                        updateKeyHint();
                    }
                    break;
            }
        };
        GEKeyListener.releaseAction = (keyEvent) -> {
        };
    }

    private EventHandler<MouseEvent> backgroundClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            switch (state) {
                case WAITING_FOR_UNVEILING:
                    break;
                case WAITING_FOR_POINT:
                    addPoint(e.getSceneX(), e.getSceneY());
                    updatePreview();
                    updateMainHintText();
                    updateKeyHint();
                    break;
            }
        }
    };

    private EventHandler<MouseEvent> backgroundMoveHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            switch (state) {
                case WAITING_FOR_UNVEILING:
                    break;
                case WAITING_FOR_POINT:
                    lastMouseMovePos = new Point2D(e.getSceneX(), e.getSceneY());
                    if (showDynamicPreview) {
                        addPoint(e.getSceneX(), e.getSceneY());
                        updatePreview();
                        updateMainHintText();
                        removeLastPoint();
                    }
                    break;
            }
        }
    };
}
