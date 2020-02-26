package sample;

import javafx.scene.Group;
import javafx.scene.Scene;

import java.util.Vector;

public class GEScene {
    private final Group rootNode;

    private Vector<GENode> sceneNodes;

    public GEScene(Scene rootScene){
        rootNode = (Group) rootScene.getRoot();
        sceneNodes = new Vector<>(0);

        // Sample object
        GENode testNode = new GENode();
        testNode.setGeometry(new GESquare(50));
        testNode.relocate(150,150);
        addNode(testNode);

        GENode testNode1 = new GENode();
        testNode1.setGeometry(new GETriangle(50));
        testNode1.relocate(350,150);
        addNode(testNode1);
    }

    public void addNode(GENode node){
        sceneNodes.addElement(node);
        rootNode.getChildren().add(node);
    }

    public void removeNode(GENode node){
        sceneNodes.removeElement(node);
        rootNode.getChildren().remove(node);
    }
}
