import ge.geometry.GEGeometry;
import ge.geometry.GESquare;
import ge.scene.GELayer;
import ge.scene.GENode;
import ge.scene.GEScene;
import ge.utils.*;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.Vector;

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

    private Text mainHintText = null;
    private GENode mainHintNode = null;
    private Text keyHintText = null;
    private GENode keyHintNode = null;

    private PolygonCreatorScene polygonCreatorScene = null;

    public EditorScene(Scene rootScene) {
        super(rootScene);
        setState(SceneState.WAITING_FOR_SELECTION);

        polygonCreatorScene = new PolygonCreatorScene(rootScene);
        polygonCreatorScene.createUI(uiPolygonCreatorClickHandler);
        polygonCreatorScene.hide();
    }

    private String createMainHintText() {
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

        double mainHintPosX = GEUIConstraints.safeAreaX * 2.25;
        double mainHintPosY = GEUIConstraints.safeAreaX/2.0 + GEUIConstraints.fontSize/4;

        createMainShapesMenu(GEUIConstraints.safeAreaX/2, GEUIConstraints.safeAreaX/2);
        createPolygonCreatorIcon(GEUIConstraints.safeAreaX*3/2, GEUIConstraints.safeAreaX/2);
        createMainHint(mainHintPosX, mainHintPosY);
        createKeyHint();
    }

    private void createKeyHint(){
        keyHintText = new Text();
        keyHintText.setFont(new Font(GEUIConstraints.fontSize));

        keyHintNode = new GENode(new GEGeometry(keyHintText));
        keyHintNode.moveTo(200, 150);
        keyHintNode.setColor(GEColor.stdUINodeColor);
        keyHintNode.addClickEvent(sceneClickHandler);
        keyHintNode.addMoveEvent(sceneMoveHandler);
        addNodeToUILayer(keyHintNode);
    }

    private void createMainHint(double posX, double posY){
        mainHintText = new Text();
        mainHintText.setFont(new Font(GEUIConstraints.fontSize));

        mainHintNode = new GENode(new GEGeometry(mainHintText));
        mainHintNode.moveTo(posX, posY);
        mainHintNode.setColor(GEColor.stdUINodeColor);
        mainHintNode.addClickEvent(sceneClickHandler);
        mainHintNode.addMoveEvent(sceneMoveHandler);
        addNodeToUILayer(mainHintNode);

        mainHintText.setText(createMainHintText());
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

    public void updateKeyHint() {
        StringBuilder stringBuilder = new StringBuilder();
        switch (getSceneState()) {
            case WAITING_FOR_SELECTION:
                stringBuilder.append("[CTRL+LMB]: Recolor\n");
                stringBuilder.append("[CTRL+RMB]: Remove\n");
                stringBuilder.append("[O]: Open\n");
                stringBuilder.append("[S]: Save As\n");
                stringBuilder.append("[P]: Load plugin");
                break;
            case WAITING_FOR_NODE_PLACEMENT_POINT1:
            case WAITING_FOR_NODE_PLACEMENT_POINT2:
                stringBuilder.append("[ESC]: Cancel");
                break;
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
            switch (getSceneState()) {
                case WAITING_FOR_SELECTION:
                    switch (keyEvent.getCode()) {
                        case O:
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("Open Stage File");
                            fileChooser.getExtensionFilters().addAll(
                                    new FileChooser.ExtensionFilter("JvGE Stage", "*.jvs"),
                                    new FileChooser.ExtensionFilter("Any", "*")
                            );
                            File fileToOpen = fileChooser.showOpenDialog(Main.primaryStage);

                            // Handle opened stage
                            if (fileToOpen != null) {
                                if (fileToOpen.exists()) {
                                    try {
                                        String string = Files.readString(fileToOpen.toPath());
                                        GELayer deserializedLayer = GESerializer.deserialize(string);

                                        if (deserializedLayer != null) {
                                            Vector<GENode> nodes = deserializedLayer.getNodes();
                                            for (int i = 0; i < nodes.size(); i++) {
                                                nodes.elementAt(i).addClickEvent(sceneNodeClickHandler);
                                                nodes.elementAt(i).addMoveEvent(sceneMoveHandler);
                                            }
                                            replaceMainLayer(deserializedLayer);
                                        } else {
                                            System.out.println("Invalid scene file");
                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            break;
                        case S:
                            FileChooser fileSaver = new FileChooser();
                            fileSaver.setTitle("Choose directory to save stage");
                            fileSaver.getExtensionFilters().addAll(
                                    new FileChooser.ExtensionFilter("JvGE Stage", "*.jvs"),
                                    new FileChooser.ExtensionFilter("Any", "*")
                            );
                            File fileToSaveTo = fileSaver.showSaveDialog(Main.primaryStage);

                            // Handle folder
                            if (fileToSaveTo != null) {
                                String serializedLayer = GESerializer.serialize(getMainLayer());
                                try {
                                    fileToSaveTo.createNewFile();

                                    FileWriter myWriter = new FileWriter(fileToSaveTo);
                                    myWriter.write(serializedLayer);
                                    myWriter.close();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case P:
                            FileChooser pluginChooser = new FileChooser();
                            pluginChooser.setTitle("Choose plugin file");
                            pluginChooser.getExtensionFilters().addAll(
                                    new FileChooser.ExtensionFilter("JvGE Plugin", "*.jvp"),
                                    new FileChooser.ExtensionFilter("Java Compiled Class", "*.class"),
                                    new FileChooser.ExtensionFilter("Any", "*")
                            );
                            File plugin = pluginChooser.showOpenDialog(Main.primaryStage);

                            // Handle opened plugin
                            break;
                    }
                    break;
                case WAITING_FOR_NODE_PLACEMENT_POINT1:
                case WAITING_FOR_NODE_PLACEMENT_POINT2:
                    if (keyEvent.getCode() == KeyCode.ESCAPE) {
                        removeNodeFromMainLayer(buffPlacementNode);
                        buffPlacementNode = null;
                        buffPlacementBoundingBox = null;
                        setState(SceneState.WAITING_FOR_SELECTION);
                        if (mainHintText != null) {
                            mainHintText.setText(createMainHintText());
                        }
                        updateKeyHint();
                    }
                    break;
            }
        };
        GEKeyListener.releaseAction = (keyEvent) -> {};
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

                    if (mainHintText != null) {
                        mainHintText.setText(createMainHintText());
                    }

                    updateKeyHint();
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
            GENode node = getNodeByShape((Shape)clickedNode);

            switch (e.getButton()) {
                case PRIMARY:
                    if (e.isControlDown()) {
                        Color newColor = GEColor.random();
                        while (node.getFillColor() == newColor) {
                            newColor = GEColor.random();
                        }

                        node.setColor(newColor);
                    }
                    break;
                case SECONDARY:
                    if (e.isControlDown()) {
                        removeNodeFromMainLayer(node);
                    }
                    break;
            }
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

                    if (mainHintText != null) {
                        mainHintText.setText(createMainHintText());
                    }

                    updateKeyHint();
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

                    if (mainHintText != null) {
                        mainHintText.setText(createMainHintText());
                    }

                    updateKeyHint();
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

                    if (mainHintText != null) {
                        mainHintText.setText(createMainHintText());
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

                    if (mainHintText != null) {
                        mainHintText.setText(createMainHintText());
                    }
                    break;
            }
        }
    };

    private EventHandler<MouseEvent> uiPolygonCreatorClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            switch (getSceneState()) {
                case WAITING_FOR_SELECTION:
                    switch (polygonCreatorScene.getState()) {
                        case WAITING_FOR_UNVEILING:
                            polygonCreatorScene.resetPolygon();
                            polygonCreatorScene.show();
                            polygonCreatorScene.configureKeyHint();
                            polygonCreatorScene.configureKeyListener();
                            polygonCreatorScene.setState(PolygonCreatorScene.SceneState.WAITING_FOR_POINT);
                            break;
                        case WAITING_FOR_POINT:
                            polygonCreatorScene.hide();
                            configureKeyHint();
                            configureKeyListener();
                            polygonCreatorScene.setState(PolygonCreatorScene.SceneState.WAITING_FOR_UNVEILING);

                            double[] points = polygonCreatorScene.getNormalizedPointsFlat();
                            if (points.length >= 6) {
                                startNewShapeCreation(points, e.getSceneX(), e.getSceneY());

                                setState(SceneState.WAITING_FOR_NODE_PLACEMENT_POINT1);

                                if (mainHintText != null) {
                                    mainHintText.setText(hintFirstPointText);
                                }
                            }
                            updateKeyHint();
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


    public SceneState getSceneState(){
        return sceneState;
    }

    public void setState(SceneState state){
        sceneState = state;
    }
}
