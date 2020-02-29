package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
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
        square.moveTo(200,200);
        mainScene.addNodeToSelecetedLayer(square);

        GENode triangle = new GENode();
        triangle.setGeometry(new GETriangle(50));
        triangle.setColor(Color.rgb(253,216,53));
        triangle.moveTo(400,200);
        mainScene.addNodeToSelecetedLayer(triangle);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
