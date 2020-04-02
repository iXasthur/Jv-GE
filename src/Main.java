import ge.geometry.*;
import ge.utils.GEClassFinder;
import ge.utils.GERegularBoundingBox;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import ge.scene.GENode;
import ge.scene.GEScene;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Main extends Application {

    private GEScene mainScene = null;

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("^_^");

        final double windowSizeFactor = 1.0f/3.0f;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension sceneSize = new Dimension();
        sceneSize.setSize(screenSize.width*windowSizeFactor, screenSize.height*windowSizeFactor);
        System.out.println("Screen size:\n" + screenSize);
        System.out.println("Scene size:\n" + sceneSize);


        Group rootNode = new Group();
        Color bgColor = Color.rgb(24,24,24);
        Scene scene = new Scene(rootNode, sceneSize.getWidth(), sceneSize.getHeight(), bgColor);
        primaryStage.setMinHeight(scene.getHeight());
        primaryStage.setMinWidth(scene.getWidth());

        mainScene = new GEScene(scene);

        primaryStage.setScene(scene);
        primaryStage.show();

        createUI(screenSize, sceneSize);
    }

    private void createUI(Dimension screenSize, Dimension sceneSize){
        createShapesMenu(screenSize, sceneSize);
//        createPreview();
    }

    private void createShapesMenu(Dimension screenSize, Dimension sceneSize){
        int separatorLineX = sceneSize.width/10;
        int buttonPosX = separatorLineX/2;
        int buttonPosY = separatorLineX/2;
        int offsetX = separatorLineX/4;
        int offsetY = (int)(buttonPosX*1.5);
        int buttonWidth = separatorLineX - 2*offsetX;

        Color buttonColor = Color.rgb(253,216,53);

        GENode separatorLine = new GENode();
        separatorLine.setGeometry(new GELine(separatorLineX,0,separatorLineX, screenSize.height));
        separatorLine.setColor(buttonColor);
        separatorLine.setStrokeWidth(3);
        separatorLine.addClickEvent(uiNodeClickHandler);
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

                            GEGeometry geometry = null;
                            try {
                                geometry = (GEGeometry)constructor.newInstance(new GERegularBoundingBox(buttonWidth));

                                GENode buffButton = new GENode();
                                buffButton.setGeometry(geometry);
                                buffButton.setStrokeWidth(3);
                                buffButton.setColor(buttonColor);
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

    private void createPreview(){
        GENode square = new GENode();
        square.setGeometry(new GESquare(50));
        square.setColor(Color.rgb(253,216,53));
        square.moveTo(100,100);
        square.addClickEvent(sceneNodeClickHandler);
        mainScene.addNodeToSelectedLayer(square);

        GENode line = new GENode();
        line.setGeometry(new GELine(100,150,500,150));
        line.setColor(Color.rgb(253,216,53));
        line.setStrokeWidth(3);
        line.addClickEvent(sceneNodeClickHandler);
        mainScene.addNodeToSelectedLayer(line);
    }

    EventHandler<MouseEvent> uiNodeClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            Object clickedNode = e.getSource();
            System.out.println("UI NODE:" + clickedNode);
        }
    };

    EventHandler<MouseEvent> sceneNodeClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            Object clickedNode = e.getSource();
            System.out.println("SCENE NODE:" + clickedNode);
        }
    };


    public static void main(String[] args) {
        launch(args);
    }
}
