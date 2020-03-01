package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.awt.*;

public class Main extends Application {

    private GEScene mainScene = null;

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("^_^");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(screenSize);
        final float windowSizeFactor = 1.0f/3.0f;

        Group rootNode = new Group();
        Color bgColor = Color.rgb(24,24,24);
        Scene scene = new Scene(rootNode, screenSize.width*windowSizeFactor, screenSize.height*windowSizeFactor, bgColor);
        primaryStage.setMinHeight(scene.getHeight());
        primaryStage.setMinWidth(scene.getWidth());

        mainScene = new GEScene(scene);

        primaryStage.setScene(scene);
        primaryStage.show();

        createPreview();
    }

    private void createPreview(){
        GENode square = new GENode();
        square.setGeometry(new GESquare(50));
        square.setColor(Color.rgb(253,216,53));
        square.moveTo(100,100);
        square.addClickEvent(nodeClickHandler);
        mainScene.addNodeToSelectedLayer(square);

        GENode triangle = new GENode();
        triangle.setGeometry(new GETriangle(50));
        triangle.setColor(Color.rgb(253,216,53));
        triangle.moveTo(200,100);
        triangle.addClickEvent(nodeClickHandler);
        mainScene.addNodeToSelectedLayer(triangle);

        GENode circle = new GENode();
        circle.setGeometry(new GECircle(25));
        circle.setColor(Color.rgb(253,216,53));
        circle.moveTo(300,100);
        circle.addClickEvent(nodeClickHandler);
        mainScene.addNodeToSelectedLayer(circle);

        GENode pentagon = new GENode();
        pentagon.setGeometry(new GERegularPolygon(25,5));
        pentagon.setColor(Color.rgb(253,216,53));
        pentagon.moveTo(400,100);
        pentagon.addClickEvent(nodeClickHandler);
        mainScene.addNodeToSelectedLayer(pentagon);

        GENode hexagon = new GENode();
        hexagon.setGeometry(new GERegularPolygon(25,6));
        hexagon.setColor(Color.rgb(253,216,53));
        hexagon.moveTo(500,100);
        hexagon.addClickEvent(nodeClickHandler);
        mainScene.addNodeToSelectedLayer(hexagon);

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
