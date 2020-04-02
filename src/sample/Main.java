package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.Vector;

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
        int offsetX = separatorLineX/4;
        int buttonPosX = separatorLineX/2;
        int buttonPosY = separatorLineX/2;
        int buttonWidth = separatorLineX - 2*offsetX;

        Color buttonColor = Color.rgb(253,216,53);

        GENode separatorLine = new GENode();
        separatorLine.setGeometry(new GELine(separatorLineX,0,separatorLineX, screenSize.height));
        separatorLine.setColor(buttonColor);
        separatorLine.setStrokeWidth(3);
        mainScene.addNodeToSelectedLayer(separatorLine);

        GENode square = new GENode();
        square.setGeometry(new GESquare(buttonWidth));
        square.setColor(buttonColor);
        square.moveTo(buttonPosX, buttonPosY);
        square.addClickEvent(nodeClickHandler);
        mainScene.addNodeToSelectedLayer(square);
        buttonPosY = buttonPosY + (int)(buttonPosX*1.5);

        GENode triangle = new GENode();
        triangle.setGeometry(new GETriangle(buttonWidth));
        triangle.setColor(buttonColor);
        triangle.moveTo(buttonPosX, buttonPosY);
        triangle.addClickEvent(nodeClickHandler);
        mainScene.addNodeToSelectedLayer(triangle);
        buttonPosY = buttonPosY + (int)(buttonPosX*1.5);
    }

    private void createPreview(){
        GENode square = new GENode();
        square.setGeometry(new GESquare(50));
        square.setColor(Color.rgb(253,216,53));
        square.moveTo(100,100);
        square.addClickEvent(nodeClickHandler);
        mainScene.addNodeToSelectedLayer(square);

        GENode line = new GENode();
        line.setGeometry(new GELine(100,150,500,150));
        line.setColor(Color.rgb(253,216,53));
        line.setStrokeWidth(3);
        line.addClickEvent(nodeClickHandler);
        mainScene.addNodeToSelectedLayer(line);
    }

    EventHandler<MouseEvent> nodeClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            Object clickedNode = e.getSource();
            System.out.println(clickedNode);
        }
    };


    public static void main(String[] args) {
        launch(args);
    }
}
