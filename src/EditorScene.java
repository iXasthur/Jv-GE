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
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EditorScene extends GEScene {

    public enum SceneState {
        WAITING_FOR_SELECTION,
        WAITING_FOR_NODE_PLACEMENT_POINT1,
        WAITING_FOR_NODE_PLACEMENT_POINT2
    }
    private SceneState sceneState = null;

    private final String hintSelectText = "<- Tap on figure to select it";
    private final String hintFirstPointText = "Set 1 point of the figure";
    private final String hintSecondPointText = "Set 2 point of the figure";

    private GENode buffPlacementNode = null;
    private GEBoundingBox buffPlacementBoundingBox = null;

    private Text hintText = null;

    private PolygonCreatorScene polygonCreatorScene = null;

    public EditorScene(Scene rootScene) {
        super(rootScene);
        setState(SceneState.WAITING_FOR_SELECTION);

        polygonCreatorScene = new PolygonCreatorScene(rootScene);
        polygonCreatorScene.createUI(uiPolygonCreatorClickHandler);
        polygonCreatorScene.hide();
    }

    private String createHintText() {
        StringBuilder buff = new StringBuilder();
        Point p1;
        Point p2;

        switch (getSceneState()){
            case WAITING_FOR_SELECTION:
                buff.append(hintSelectText);
                break;
            case WAITING_FOR_NODE_PLACEMENT_POINT1:
                p1 = buffPlacementBoundingBox.getPoint1();

                buff.append(hintFirstPointText);

                buff.append('\n');
                buff.append('(');
                buff.append(p1.x);
                buff.append(", ");
                buff.append(p1.y);
                buff.append(')');
                break;
            case WAITING_FOR_NODE_PLACEMENT_POINT2:
                p1 = buffPlacementBoundingBox.getPoint1();
                p2 = buffPlacementBoundingBox.getPoint2();

                buff.append(hintSecondPointText);

                buff.append('\n');
                buff.append('(');
                buff.append(p1.x);
                buff.append(", ");
                buff.append(p1.y);
                buff.append(')');

                buff.append('\n');
                buff.append('(');
                buff.append(p2.x);
                buff.append(", ");
                buff.append(p2.y);
                buff.append(')');
                break;
        }
        return buff.toString();
    }

    public void createUI(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        double hintPosX = GEUIConstraints.safeAreaX * 2.25;
        double hintPosY = GEUIConstraints.safeAreaX/2.0 + GEUIConstraints.fontSize/4;

        createMainShapesMenu(GEUIConstraints.safeAreaX/2, GEUIConstraints.safeAreaX/2);
        createPolygonCreatorIcon(GEUIConstraints.safeAreaX*3/2, GEUIConstraints.safeAreaX/2);
        createHint(hintPosX, hintPosY);
    }

    private void createHint(double posX, double posY){
        hintText = new Text();
        hintText.setFont(new Font(GEUIConstraints.fontSize));

        GENode buff = new GENode(new GEGeometry(hintText));
        buff.moveTo(posX, posY);
        buff.setColor(GEColor.stdUINodeColor);
        buff.addMoveEvent(sceneMoveHandler);
        addNodeToUILayer(buff);

        hintText.setText(createHintText());
    }

    private void createPolygonCreatorIcon(int posX, int posY){
        double[] buttonPoints = PolygonCreatorScene.getSamplePolygonPoints(new GERegularBoundingBox(GEUIConstraints.buttonWidth));

        GENode buffButton = new GENode(new GEGeometry(buttonPoints));
        buffButton.setStrokeWidth(3);
        buffButton.setColor(GEColor.stdUINodeColor);
        buffButton.moveTo(posX, posY);
        buffButton.addClickEvent(uiPolygonCreatorClickHandler);
        buffButton.addMoveEvent(sceneMoveHandler);
        addNodeToUILayer(buffButton);
    }

    public void createBackgroundNode(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Color bgColor = GEColor.stdBackgroundColor;
        GERegularBoundingBox boundingBox = new GERegularBoundingBox(screenSize.width*2);

        GENode bgRectangle = new GENode(new GESquare(boundingBox));
        bgRectangle.moveTo(screenSize.width/2.0,screenSize.height/2.0);
        bgRectangle.setColor(bgColor);
        bgRectangle.addClickEvent(sceneClickHandler);
        bgRectangle.addMoveEvent(sceneMoveHandler);
        addNodeToBackgroundLayer(bgRectangle);
    }

    private void createMainShapesMenu(int posX, int posY){
        GEClassFinder classFinder = new GEClassFinder("ge/geometry");
        Class<?>[] availableGeometryClasses = classFinder.getClassesArray();

        System.out.println("Available geometry classes:");

        if (availableGeometryClasses.length > 0) {
            Class<?> neededParameterType = GERegularBoundingBox.class;

            for (Class<?> cl : availableGeometryClasses) {
                System.out.println(cl);

                Constructor<?>[] constructors = cl.getConstructors();
                for (Constructor<?> constructor : constructors) {
                    if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0] == neededParameterType) {

                        GEGeometry geometry;
                        try {
                            geometry = (GEGeometry)constructor.newInstance(new GERegularBoundingBox(GEUIConstraints.buttonWidth));

                            GENode buffButton = new GENode(geometry);
                            buffButton.setStrokeWidth(3);
                            buffButton.setColor(GEColor.stdUINodeColor);
                            buffButton.moveTo(posX, posY);
                            buffButton.addClickEvent(uiNodeSpawnerClickHandler);
                            buffButton.addMoveEvent(sceneMoveHandler);
                            addNodeToUILayer(buffButton);

                            posY = posY + GEUIConstraints.offsetY;
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        break;
                    }
                }
            }
        }
    }

    private void startNewShapeCreation(double[] points, double x, double y){
        buffPlacementNode = new GENode(new GEGeometry(points));
        buffPlacementNode.setStrokeWidth(3);
        buffPlacementNode.setColor(GEColor.stdPreviewNodeColor);
        buffPlacementNode.addClickEvent(sceneClickHandler);
        buffPlacementNode.addMoveEvent(sceneMoveHandler);
        addNodeToMainLayer(buffPlacementNode);

        buffPlacementNode.moveTo(x, y);
        buffPlacementBoundingBox = new GEBoundingBox(x, y, x, y);
    }

    private EventHandler<MouseEvent> uiNodeSpawnerClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            switch (getSceneState()) {
                case WAITING_FOR_SELECTION:
                    javafx.scene.shape.Shape clickedShape = (Shape) e.getSource();

                    double[] points = getNodeByShape(clickedShape).getGeometry().getPoints();

                    startNewShapeCreation(points, e.getSceneX(), e.getSceneY());

                    setState(SceneState.WAITING_FOR_NODE_PLACEMENT_POINT1);

                    if (hintText != null) {
                        hintText.setText(createHintText());
                    }
                    break;
                case WAITING_FOR_NODE_PLACEMENT_POINT1:
                    break;
                case WAITING_FOR_NODE_PLACEMENT_POINT2:
                    break;
            }
        }
    };

    private EventHandler<MouseEvent> sceneNodeClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            Object clickedNode = e.getSource();
            System.out.println("SCENE NODE:" + getNodeByShape((Shape)clickedNode));
        }
    };

    private EventHandler<MouseEvent> sceneClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            switch (getSceneState()) {
                case WAITING_FOR_SELECTION:
                    break;
                case WAITING_FOR_NODE_PLACEMENT_POINT1:
                    buffPlacementBoundingBox.setPoint1(e.getSceneX(), e.getSceneY());
                    buffPlacementBoundingBox.setPoint2(e.getSceneX(), e.getSceneY());

                    setState(SceneState.WAITING_FOR_NODE_PLACEMENT_POINT2);

                    if (hintText != null) {
                        hintText.setText(createHintText());
                    }

                    break;
                case WAITING_FOR_NODE_PLACEMENT_POINT2:
                    buffPlacementBoundingBox.setPoint2(e.getSceneX(), e.getSceneY());

                    if (buffPlacementNode != null) {
                        Point p = buffPlacementBoundingBox.getCenterPoint();
                        buffPlacementNode.moveTo(p.x, p.y);
                        buffPlacementNode.setColor(GEColor.stdSceneNodeColor);
                        buffPlacementNode.addClickEvent(sceneNodeClickHandler);
                        buffPlacementNode = null;
                    }

                    buffPlacementBoundingBox = null;

                    setState(SceneState.WAITING_FOR_SELECTION);

                    if (hintText != null) {
                        hintText.setText(createHintText());
                    }

                    break;
            }
        }
    };

    private EventHandler<MouseEvent> sceneMoveHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
//            System.out.println("SCENE MOUSE MOVED: x:" + e.getSceneX() + " y:" + e.getSceneY());

            switch (getSceneState()) {
                case WAITING_FOR_SELECTION:
                    break;
                case WAITING_FOR_NODE_PLACEMENT_POINT1:
                    buffPlacementBoundingBox.setPoint1(e.getSceneX(), e.getSceneY());

                    buffPlacementNode.moveTo(e.getSceneX(), e.getSceneY());

                    if (hintText != null) {
                        hintText.setText(createHintText());
                    }
                    break;
                case WAITING_FOR_NODE_PLACEMENT_POINT2:
                    buffPlacementBoundingBox.setPoint2(e.getSceneX(), e.getSceneY());

                    if (buffPlacementNode != null) {
                        Point p = buffPlacementBoundingBox.getCenterPoint();
                        buffPlacementNode.moveTo(p.x, p.y);

                        Bounds initialBounds = buffPlacementNode.getGeometry().getInitialBounds();
                        double scaleX = buffPlacementBoundingBox.getWidthNoModulo()/initialBounds.getWidth();
                        double scaleY = buffPlacementBoundingBox.getHeightNoModulo()/initialBounds.getHeight();
                        buffPlacementNode.scaleTo(scaleX, scaleY);
                    }

                    if (hintText != null) {
                        hintText.setText(createHintText());
                    }
                    break;
            }
        }
    };

    private EventHandler<MouseEvent> uiPolygonCreatorClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            switch (polygonCreatorScene.getState()) {
                case WAITING_FOR_UNVEILING:
                    polygonCreatorScene.resetPolygon();
                    polygonCreatorScene.show();
                    polygonCreatorScene.setState(PolygonCreatorScene.PolygonCreatorSceneState.WAITING_FOR_POINT);
                    break;
                case WAITING_FOR_POINT:
                    polygonCreatorScene.hide();
                    polygonCreatorScene.setState(PolygonCreatorScene.PolygonCreatorSceneState.WAITING_FOR_UNVEILING);

                    double[] points = polygonCreatorScene.getNormalizedPointsFlat();
                    if (points.length >= 6) {
                        startNewShapeCreation(points, e.getSceneX(), e.getSceneY());

                        setState(SceneState.WAITING_FOR_NODE_PLACEMENT_POINT1);

                        if (hintText != null) {
                            hintText.setText(hintFirstPointText);
                        }
                    }
                    break;
            }
        }
    };


//    private EventHandler<ScrollEvent> sceneWheelHandler = new EventHandler<ScrollEvent>() {
//        @Override
//        public void handle(ScrollEvent e) {
//        }
//    };

    public SceneState getSceneState(){
        return sceneState;
    }

    public void setState(SceneState state){
        sceneState = state;
    }
}
