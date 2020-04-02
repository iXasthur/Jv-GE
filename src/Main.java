import ge.geometry.*;
import ge.utils.GEClassFinder;
import ge.utils.GERegularBoundingBox;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
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

    private GEScene mainScene = null;
    private GENode buffPlacementNode = null;
    private Color stdUINodeColor = Color.rgb(253,216,53);
    private Color stdSceneNodeColor = Color.rgb(153,216,53);

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
//        scene.addEventHandler(MouseEvent.MOUSE_MOVED, sceneMoveHandler);

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
                buffPlacementNode.setColor(stdSceneNodeColor);

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
                    buffPlacementNode.addClickEvent(uiNodeClickHandler);
                    mainScene.addNodeToSelectedLayer(buffPlacementNode);
                    buffPlacementNode = null;
                }

                mainScene.setState(GEScene.sceneStates.WAITING_FOR_SELECTION);
            }
        }
    };

    private EventHandler<MouseEvent> sceneMoveHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            System.out.println("SCENE MOUSE MOVED: x:" + e.getSceneX() + " y:" + e.getSceneY());
        }
    };

    public static void main(String[] args) {
        launch(args);
    }
}
