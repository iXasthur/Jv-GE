import ge.geometry.*;
import ge.utils.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import ge.scene.GENode;
import ge.scene.GEScene;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main extends Application {

    private final String hintSelectText = "<- Tap on figure to select it";
    private final String hintFirstPointText = "Set 1 point of the figure";
    private final String hintSecondPointText = "Set 2 point of the figure";

    private GEScene mainScene = null;
    private GENode buffPlacementNode = null;
    private GEBoundingBox buffPlacementBoundingBox = null;

    private Text hintText = null;

    private GEPolygonCreator polygonCreator = null;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("^_^");

        final double windowSizeFactor = 1.0f/3.0f;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension sceneSize = new Dimension();
        sceneSize.setSize(screenSize.width*windowSizeFactor, screenSize.height*windowSizeFactor);

        GEUIConstraints.safeAreaX = sceneSize.width/10;
        GEUIConstraints.offsetX = GEUIConstraints.safeAreaX/4;
        GEUIConstraints.offsetY = GEUIConstraints.safeAreaX*3/4;
        GEUIConstraints.buttonWidth = GEUIConstraints.safeAreaX - 2*GEUIConstraints.offsetX;
        GEUIConstraints.fontSize = GEUIConstraints.safeAreaX/4.0;

        System.out.println("Screen size:\n" + screenSize);
        System.out.println("Scene size:\n" + sceneSize);

        Group rootNode = new Group();
        Color bgColor = Color.rgb(255,255,255);
        Scene scene = new Scene(rootNode, sceneSize.getWidth(), sceneSize.getHeight(), bgColor);
        primaryStage.setMinHeight(scene.getHeight());
        primaryStage.setMinWidth(scene.getWidth());

        mainScene = new GEScene(scene);

//        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, sceneClickHandler);
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, sceneMoveHandler);
//        scene.addEventHandler(ScrollEvent.SCROLL, sceneWheelHandler);

        primaryStage.setScene(scene);
        primaryStage.show();

        createBackgroundNode();
        mainScene.createAndSelectNewLayer();
        createUI(sceneSize);
        mainScene.createAndSelectNewLayer();
    }

    private void createUI(Dimension sceneSize){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        double hintPosX = GEUIConstraints.safeAreaX * 2.25;
        double hintPosY = GEUIConstraints.safeAreaX/2.0 + GEUIConstraints.fontSize/4;

        createMainShapesMenu(GEUIConstraints.safeAreaX/2, GEUIConstraints.safeAreaX/2);
        createAdditionalShapesMenu(GEUIConstraints.safeAreaX*3/2, GEUIConstraints.safeAreaX/2);
//        createHints(hintPosX, hintPosY, fontSize);
    }

    private void createAdditionalShapesMenu(int posX, int posY){
        double[] buttonPoints = GEPolygonCreator.getSamplePolygonPoints(new GERegularBoundingBox(GEUIConstraints.buttonWidth));

        GENode buffButton = new GENode();
        buffButton.setGeometry(new GEGeometry(buttonPoints));
        buffButton.setStrokeWidth(3);
        buffButton.setColor(GEColor.stdUINodeColor);
        buffButton.moveTo(posX, posY);
        buffButton.addClickEvent(uiPolygonCreatorClickHandler);
        mainScene.addNodeToSelectedLayer(buffButton);
    }

    private void createHints(double posX, double posY , double fontSize){
        hintText = new Text();
        hintText.setText(hintSelectText);
        hintText.setFont(new Font(fontSize));

        GENode buff = new GENode();
        buff.setGeometry(new GEGeometry(hintText));
        buff.moveTo(posX, posY);
        buff.setColor(GEColor.stdUINodeColor);
        mainScene.addNodeToSelectedLayer(buff);
    }

    private void createBackgroundNode(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Color bgColor = Color.rgb(24,24,24);
        GENode bgRectangle = new GENode();
        GERegularBoundingBox boundingBox = new GERegularBoundingBox(screenSize.width*2);
        bgRectangle.setGeometry(new GESquare(boundingBox));
        bgRectangle.moveTo(screenSize.width/2.0,screenSize.height/2.0);
        bgRectangle.setColor(bgColor);
//        bgRectangle.setStrokeWidth(3);
        bgRectangle.addClickEvent(sceneClickHandler);
        mainScene.addNodeToSelectedLayer(bgRectangle);
    }

    private void createMainShapesMenu(int posX, int posY){
        GEClassFinder classFinder = new GEClassFinder("ge/geometry");
        Class<?>[] availableGeometryClasses = classFinder.getClassesArray();
        System.out.println("Available geometry classes:");

        if (availableGeometryClasses.length > 0) {
            try {
                Class<?> neededParameterType = Class.forName("ge.utils.GERegularBoundingBox");

                for (Class<?> cl : availableGeometryClasses) {
                    System.out.println(cl);

                    Constructor<?>[] constructors = cl.getConstructors();
                    for (Constructor<?> constructor : constructors) {
                        if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0] == neededParameterType) {

                            GEGeometry geometry;
                            try {
                                geometry = (GEGeometry)constructor.newInstance(new GERegularBoundingBox(GEUIConstraints.buttonWidth));

                                GENode buffButton = new GENode();
                                buffButton.setGeometry(geometry);
                                buffButton.setStrokeWidth(3);
                                buffButton.setColor(GEColor.stdUINodeColor);
                                buffButton.moveTo(posX, posY);
                                buffButton.addClickEvent(uiNodeSpawnerClickHandler);
                                mainScene.addNodeToSelectedLayer(buffButton);
                                posY = posY + GEUIConstraints.offsetY;
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }

                            break;
                        }
                    }
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private EventHandler<MouseEvent> uiPolygonCreatorClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            switch (mainScene.getSceneState()) {
                case WAITING_FOR_SELECTION:
                    if (polygonCreator == null) {
                        polygonCreator = new GEPolygonCreator(mainScene);
                    }

                    switch (polygonCreator.getState()) {
                        case WAITING_FOR_UI_CREATION:
                            mainScene.createAndSelectNewLayer();
                            polygonCreator.createUI(uiPolygonCreatorClickHandler);
                            break;
                        case WAITING_FOR_POINT:
                            mainScene.removeSelectedLayerAndSelectLast();

                            double[] points = polygonCreator.getNormalizedPointsFlat();
                            polygonCreator = null;

                            buffPlacementNode = new GENode();
                            buffPlacementNode.setGeometry(new GEGeometry(points));
                            buffPlacementNode.setStrokeWidth(3);
                            buffPlacementNode.setColor(GEColor.stdPreviewNodeColor);
                            buffPlacementNode.moveTo(e.getSceneX(), e.getSceneY());
                            buffPlacementNode.addClickEvent(sceneClickHandler);
                            mainScene.addNodeToSelectedLayer(buffPlacementNode);

                            buffPlacementBoundingBox = new GEBoundingBox(e.getSceneX(), e.getSceneY(), e.getSceneX(), e.getSceneY());

                            if (hintText != null) {
                                hintText.setText(hintFirstPointText);
                            }

                            mainScene.setState(GEScene.sceneStates.WAITING_FOR_NODE_PLACEMENT_POINT1);
                            break;
                    }
                    break;
                case WAITING_FOR_NODE_PLACEMENT_POINT1:
                    break;
                case WAITING_FOR_NODE_PLACEMENT_POINT2:
                    break;
            }
        }
    };

    private EventHandler<MouseEvent> uiNodeSpawnerClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            switch (mainScene.getSceneState()) {
                case WAITING_FOR_SELECTION:
                    Shape clickedShape = (Shape) e.getSource();

                    double[] points = mainScene.getNodeByShape(clickedShape).getGeometry().getPoints();

                    buffPlacementNode = new GENode();
                    buffPlacementNode.setGeometry(new GEGeometry(points));
                    buffPlacementNode.setStrokeWidth(3);
                    buffPlacementNode.setColor(GEColor.stdPreviewNodeColor);
                    buffPlacementNode.moveTo(e.getSceneX(), e.getSceneY());
                    buffPlacementNode.addClickEvent(sceneClickHandler);
                    mainScene.addNodeToSelectedLayer(buffPlacementNode);

                    buffPlacementBoundingBox = new GEBoundingBox(e.getSceneX(), e.getSceneY(), e.getSceneX(), e.getSceneY());

                    if (hintText != null) {
                        hintText.setText(hintFirstPointText);
                    }

                    mainScene.setState(GEScene.sceneStates.WAITING_FOR_NODE_PLACEMENT_POINT1);
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
            System.out.println("SCENE NODE:" + clickedNode);
        }
    };

    private EventHandler<MouseEvent> sceneClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            switch (mainScene.getSceneState()) {
                case WAITING_FOR_SELECTION:
                    break;
                case WAITING_FOR_NODE_PLACEMENT_POINT1:
                    buffPlacementBoundingBox.setPoint1(e.getSceneX(), e.getSceneY());
                    buffPlacementBoundingBox.setPoint2(e.getSceneX(), e.getSceneY());

                    Point point1 = buffPlacementBoundingBox.getPoint1();
                    if (hintText != null) {
                        hintText.setText(hintSecondPointText + "\n(" + point1.x + ", " + point1.y + ")");
                    }

                    mainScene.setState(GEScene.sceneStates.WAITING_FOR_NODE_PLACEMENT_POINT2);
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

                    if (hintText != null) {
                        hintText.setText(hintSelectText);
                    }

                    mainScene.setState(GEScene.sceneStates.WAITING_FOR_SELECTION);
                    break;
            }
        }
    };

    private EventHandler<MouseEvent> sceneMoveHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
//            System.out.println("SCENE MOUSE MOVED: x:" + e.getSceneX() + " y:" + e.getSceneY());
            switch (mainScene.getSceneState()) {
                case WAITING_FOR_SELECTION:
                    break;
                case WAITING_FOR_NODE_PLACEMENT_POINT1:
                    buffPlacementNode.moveTo(e.getSceneX(), e.getSceneY());
                    if (hintText != null) {
                        hintText.setText(hintFirstPointText + "\n" + "(" + e.getSceneX() + ", " + e.getSceneY() + ")");
                    }
                    break;
                case WAITING_FOR_NODE_PLACEMENT_POINT2:
                    buffPlacementBoundingBox.setPoint2(e.getSceneX(), e.getSceneY());
                    Point point1 = buffPlacementBoundingBox.getPoint1();
                    if (hintText != null) {
                        hintText.setText(hintSecondPointText + "\n(" + point1.x + ", " + point1.y + ")" + " (" + e.getSceneX() + ", " + e.getSceneY() + ")");
                    }
                    if (buffPlacementNode != null) {
                        Point p = buffPlacementBoundingBox.getCenterPoint();
                        buffPlacementNode.moveTo(p.x, p.y);
                        double scaleX = buffPlacementBoundingBox.getWidthNoModulo()/buffPlacementNode.getInitialWidth();
                        double scaleY = buffPlacementBoundingBox.getHeightNoModulo()/buffPlacementNode.getInitialHeight();
                        buffPlacementNode.scaleTo(mainScene,scaleX,scaleY);
                    }
                    break;
            }
        }
    };

    private void rotateBuffNodeBy(double angle){
        if (buffPlacementNode != null) {
            Shape shape = buffPlacementNode.getGeometry().getShape();
            shape.setRotate(shape.getRotate() + angle);
        }
    }

    private void addToScaleBuffNode(double d){
        if (buffPlacementNode != null) {
            Shape shape = buffPlacementNode.getGeometry().getShape();
            shape.setScaleX(shape.getScaleX() + d);
            shape.setScaleY(shape.getScaleY() + d);
//            buffPlacementNode.getShape().setStrokeWidth(buffPlacementNode.getShape().getStrokeWidth() - buffPlacementNode.getShape().getStrokeWidth()*d);
        }
    }

//    private EventHandler<ScrollEvent> sceneWheelHandler = new EventHandler<ScrollEvent>() {
//        @Override
//        public void handle(ScrollEvent e) {
//            if (mainScene.getSceneState() == GEScene.sceneStates.WAITING_FOR_NODE_PLACEMENT) {
//                double d = 15;
//                if (e.getDeltaY()<0) {
//                    d = -d;
//                }
//
//                if (!e.isControlDown()) {
//                    rotateBuffNodeBy(d);
//                } else {
//                    addToScaleBuffNode(d/100.0);
//                }
//            }
//        }
//    };

    public static void main(String[] args) {
        launch(args);
    }
}
