package sample;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;

import java.util.Vector;

public class GEScene {

    private final Group rootNode;
    private Vector<GENode> sceneNodes;

    public GEScene(Scene rootScene){
        rootNode = (Group) rootScene.getRoot();
        sceneNodes = new Vector<>(0);

        //Test nodes
        GENode square = new GENode();
        square.setGeometry(new GESquare(50));
        square.moveTo(200,200);
        addNode(square);
    }

    public void addNode(GENode node){
        if (node.getShape() != null) {
            sceneNodes.addElement(node);
            rootNode.getChildren().add(node.getShape());
        } else {
            System.out.print(">Unable to add node to the scene: ");
            System.out.println(node);
        }
    }

    public void removeNode(GENode node){
        if (node.getShape() != null) {
            sceneNodes.removeElement(node);
            rootNode.getChildren().remove(node.getShape());
        } else {
            System.out.print(">Unable to remove node from the scene: ");
            System.out.println(node);
        }
    }
}
