import ge.utils.GEUIConstraints;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    private Stage primaryStage;
    private EditorScene editorScene = null;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("^_^");

        final double windowSizeFactor = 1.0f/3.0f;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension sceneSize = new Dimension();
        sceneSize.setSize(screenSize.width*windowSizeFactor, screenSize.height*windowSizeFactor);

        GEUIConstraints.safeAreaX = sceneSize.width/10;
        GEUIConstraints.offsetY = GEUIConstraints.safeAreaX*3/4;
        GEUIConstraints.buttonWidth = GEUIConstraints.safeAreaX - 2*GEUIConstraints.safeAreaX/4;
        GEUIConstraints.fontSize = GEUIConstraints.safeAreaX/4.0;

        System.out.println("Screen size:\n" + screenSize);
        System.out.println("Scene size:\n" + sceneSize);

        Group rootNode = new Group();
        Color bgColor = Color.rgb(255,255,255);
        Scene scene = new Scene(rootNode, sceneSize.getWidth(), sceneSize.getHeight(), bgColor);
        primaryStage.setMinHeight(scene.getHeight());
        primaryStage.setMinWidth(scene.getWidth());

        editorScene = new EditorScene(scene);
        editorScene.createBackgroundNode();
        editorScene.createUI();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
