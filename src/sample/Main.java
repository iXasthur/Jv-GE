package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.*;

public class Main extends Application {

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

        GEScene scn = new GEScene(scene);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
