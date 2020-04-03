import ge.geometry.*;
import ge.utils.GEClassFinder;
import ge.utils.GERegularBoundingBox;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import ge.scene.GENode;
import ge.scene.GEScene;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main extends Application {

    private final Color stdUINodeColor = Color.rgb(253,216,53);
    private final Color stdPreviewNodeColor = Color.rgb(153,216,53,0.2);
    private final Color stdSceneNodeColor = Color.rgb(153,216,53);

    private GEScene mainScene = null;
    private GENode buffPlacementNode = null;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("^_^");

        final double windowSizeFactor = 1.0f/3.0f;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension sceneSize = new Dimension();
        sceneSize.setSize(screenSize.width*windowSizeFactor, screenSize.height*windowSizeFactor);
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
        scene.addEventHandler(ScrollEvent.SCROLL, sceneWheelHandler);

        primaryStage.setScene(scene);
        primaryStage.show();

        createBackgroundNode(screenSize);
        createUI(screenSize, sceneSize);
        mainScene.createAndSelectNewLayer();
    }

    private void createUI(Dimension screenSize, Dimension sceneSize){
        createShapesMenu(screenSize, sceneSize);
//        createPreview();
    }

    private void createBackgroundNode(Dimension screenSize){
        Color bgColor = Color.rgb(24,24,24);
        GENode bgRectangle = new GENode();
        bgRectangle.setGeometry(new GESquare(screenSize.width));
        bgRectangle.moveTo(screenSize.width/2.0,screenSize.height/2.0);
        bgRectangle.setColor(bgColor);
        bgRectangle.setStrokeWidth(3);
        bgRectangle.addClickEvent(sceneClickHandler);
        mainScene.addNodeToSelectedLayer(bgRectangle);
    }

    private void createShapesMenu(Dimension screenSize, Dimension sceneSize){
        int separatorLineX = sceneSize.width/10;
        int buttonPosX = separatorLineX/2;
        int buttonPosY = separatorLineX/2;
        int offsetX = separatorLineX/4;
        int offsetY = (int)(buttonPosX*1.5);
        int buttonWidth = separatorLineX - 2*offsetX;

        GENode separatorLine = new GENode();
        separatorLine.setGeometry(new GELine(separatorLineX,0,separatorLineX, screenSize.height));
        separatorLine.setColor(stdUINodeColor);
        separatorLine.setStrokeWidth(3);
//        separatorLine.addClickEvent(mouseHandler.uiNodeClickHandler);
        mainScene.addNodeToSelectedLayer(separatorLine);

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
                                geometry = (GEGeometry)constructor.newInstance(new GERegularBoundingBox(buttonWidth));

                                GENode buffButton = new GENode();
                                buffButton.setGeometry(geometry);
                                buffButton.setStrokeWidth(3);
                                buffButton.setColor(stdUINodeColor);
                                buffButton.moveTo(buttonPosX, buttonPosY);
                                buffButton.addClickEvent(uiNodeClickHandler);
                                mainScene.addNodeToSelectedLayer(buffButton);
                                buttonPosY = buttonPosY + offsetY;
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

    private EventHandler<MouseEvent> uiNodeClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (mainScene.getSceneState() == GEScene.sceneStates.WAITING_FOR_SELECTION) {
                Shape clickedShape = (Shape)e.getSource();

                double buffX = clickedShape.getLayoutX();
                double buffY = clickedShape.getLayoutY();
                clickedShape.setLayoutX(0);
                clickedShape.setLayoutY(0);
                Shape geometryShape = Shape.union(clickedShape, new Circle(0));
                clickedShape.setLayoutX(buffX);
                clickedShape.setLayoutY(buffY);

                buffPlacementNode = new GENode();
                buffPlacementNode.setGeometry(new GEGeometry(geometryShape));
                buffPlacementNode.setStrokeWidth(3);
                buffPlacementNode.setColor(stdPreviewNodeColor);
                buffPlacementNode.moveTo(e.getSceneX(), e.getSceneY());
                buffPlacementNode.addClickEvent(sceneClickHandler);
                mainScene.addNodeToSelectedLayer(buffPlacementNode);

                mainScene.setState(GEScene.sceneStates.WAITING_FOR_NODE_PLACEMENT);
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
            if (mainScene.getSceneState() == GEScene.sceneStates.WAITING_FOR_NODE_PLACEMENT) {
                if (buffPlacementNode != null) {
                    buffPlacementNode.moveTo(e.getSceneX(), e.getSceneY());
                    buffPlacementNode.setColor(stdSceneNodeColor);
//                    buffPlacementNode.addClickEvent(sceneNodeClickHandler);
                    buffPlacementNode = null;
                }

                mainScene.setState(GEScene.sceneStates.WAITING_FOR_SELECTION);
            }
        }
    };

    private EventHandler<MouseEvent> sceneMoveHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (mainScene.getSceneState() == GEScene.sceneStates.WAITING_FOR_NODE_PLACEMENT) {
//                System.out.println("SCENE MOUSE MOVED: x:" + e.getSceneX() + " y:" + e.getSceneY());
                if (buffPlacementNode != null) {
                    buffPlacementNode.moveTo(e.getSceneX(), e.getSceneY());
                }
            }
        }
    };

    private void rotateBuffNodeBy(double angle){
        if (buffPlacementNode != null) {
            buffPlacementNode.getShape().setRotate(buffPlacementNode.getShape().getRotate() + angle);
        }
    }

    private void addToScaleBuffNode(double d){
        if (buffPlacementNode != null) {
            buffPlacementNode.getShape().setScaleX(buffPlacementNode.getShape().getScaleX() + d);
            buffPlacementNode.getShape().setScaleY(buffPlacementNode.getShape().getScaleY() + d);
//            buffPlacementNode.getShape().setStrokeWidth(buffPlacementNode.getShape().getStrokeWidth() - buffPlacementNode.getShape().getStrokeWidth()*d);
        }
    }

    private EventHandler<ScrollEvent> sceneWheelHandler = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent e) {
            if (mainScene.getSceneState() == GEScene.sceneStates.WAITING_FOR_NODE_PLACEMENT) {
                double d = 15;
                if (e.getDeltaY()<0) {
                    d = -d;
                }

                if (!e.isControlDown()) {
                    rotateBuffNodeBy(d);
                } else {
                    addToScaleBuffNode(d/100.0);
                }
            }
        }
    };

    public static void main(String[] args) {
        launch(args);
    }
}
