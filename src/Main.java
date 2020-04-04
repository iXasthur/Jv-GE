import ge.geometry.*;
import ge.utils.GEClassFinder;
import ge.utils.GERegularBoundingBox;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.*;
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
import java.util.Arrays;

public class Main extends Application {

    private final Color stdUINodeColor = Color.rgb(253,216,53);
    private final Color stdPreviewNodeColor = Color.rgb(153,216,53,0.2);
    private final Color stdSceneNodeColor = Color.rgb(153,216,53);

    private String hintSelectText = "<- Tap on figure to select it";
    private String hintRotationScaleText = "Use wheel to rotate new figure\nand ctrl+wheel to scale it";

    private final Boolean showHintOnlyOneTime = false;

    private GEScene mainScene = null;
    private GENode buffPlacementNode = null;

    private Text hintText = null;

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
//        scene.addEventHandler(ScrollEvent.SCROLL, sceneWheelHandler);

        primaryStage.setScene(scene);
        primaryStage.show();

        createBackgroundNode(screenSize);
        mainScene.createAndSelectNewLayer();
        createUI(screenSize, sceneSize);
        mainScene.createAndSelectNewLayer();
    }

    private void createUI(Dimension screenSize, Dimension sceneSize){
        int safeAreaX = sceneSize.width/10;
        createShapesMenu(screenSize, safeAreaX);
//        createHints(safeAreaX);
//        createPreview();
    }

    private void createPreview(){
        Path p = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.setX(200);
        moveTo.setY(200);

        //Creating an object of the class LineTo
        LineTo lineTo = new LineTo();

        //Setting the Properties of the line element
        lineTo.setX(100);
        lineTo.setY(100);

        //Adding the path elements to Observable list of the Path class
        p.getElements().add(moveTo);
        p.getElements().add(lineTo);

        GENode buff = new GENode();
        buff.setGeometry(new GERegularPolygon(100,100,0));
        buff.setColor(stdUINodeColor);
        mainScene.addNodeToSelectedLayer(buff);

    }

    private void createHints(int posX){
        double fontSize = posX/4.0;

        hintText = new Text();
        hintText.setText(hintSelectText);
        hintText.setFont(new Font(fontSize));

//        GENode buff = new GENode();
//        buff.setGeometry(new GEGeometry(hintText));
//        buff.moveTo(posX*1.25,posX/2.0 + fontSize/4);
//        buff.setColor(stdUINodeColor);
//        mainScene.addNodeToSelectedLayer(buff);
    }

    private void createBackgroundNode(Dimension screenSize){
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

    private void createShapesMenu(Dimension screenSize, int safeAreaX){
        int buttonPosX = safeAreaX/2;
        int buttonPosY = safeAreaX/2;
        int offsetX = safeAreaX/4;
        int offsetY = (int)(buttonPosX*1.5);
        int buttonWidth = safeAreaX - 2*offsetX;

//        GENode separatorLine = new GENode();
//        separatorLine.setGeometry(new GELine(safeAreaX,0,safeAreaX, screenSize.height));
//        separatorLine.setColor(stdUINodeColor);
//        separatorLine.setStrokeWidth(3);
////        separatorLine.addClickEvent(mouseHandler.uiNodeClickHandler);
//        mainScene.addNodeToSelectedLayer(separatorLine);

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

//                double buffX = clickedShape.getLayoutX();
//                double buffY = clickedShape.getLayoutY();
//                clickedShape.setLayoutX(0);
//                clickedShape.setLayoutY(0);
//                Shape geometryShape = Shape.union(clickedShape, new Circle(0));
//                clickedShape.setLayoutX(buffX);
//                clickedShape.setLayoutY(buffY);
                double[] points = mainScene.getNodeByShape(clickedShape).getGeometry().getPoints();

                buffPlacementNode = new GENode();
                buffPlacementNode.setGeometry(new GEGeometry(points));
                buffPlacementNode.setStrokeWidth(3);
                buffPlacementNode.setColor(stdPreviewNodeColor);
                buffPlacementNode.moveTo(e.getSceneX(), e.getSceneY());
                buffPlacementNode.addClickEvent(sceneClickHandler);
                mainScene.addNodeToSelectedLayer(buffPlacementNode);

//                hintText.setText(hintRotationScaleText);
//                if (showHintOnlyOneTime) {
//                    hintSelectText = "";
//                    hintRotationScaleText = "";
//                }

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

//                hintText.setText(hintSelectText);

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
