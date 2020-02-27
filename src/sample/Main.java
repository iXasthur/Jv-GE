package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        Scene scene = new Scene(rootNode, screenSize.width*windowSizeFactor, screenSize.height*windowSizeFactor);
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
        square.moveTo(200,200);
        mainScene.addNodeToSelecetedLayer(square);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
