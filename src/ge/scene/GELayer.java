package ge.scene;

import javafx.scene.Group;

import java.util.Vector;

public class GELayer {

    private Vector<GENode> layerNodes;
    private Group group;
    private int z; // Used to sort layers, will be implemented soon

    public GELayer(){
        super();
        layerNodes = new Vector<>(0);
        group = new Group();
        z = 0;
    }

    public Group getNodesGroup(){
        return group;
    }

    public void addNode(GENode node){
        if (node.getShape() != null) {
            layerNodes.addElement(node);
            group.getChildren().add(node.getShape());
        } else {
            System.out.print(">Unable to add node to the layer: ");
            System.out.println(this);
            System.out.println(node);
        }
    }

    public void removeNode(GENode node){
        if (node.getShape() != null) {
            layerNodes.removeElement(node);
            group.getChildren().remove(node.getShape());
        } else {
            System.out.print(">Unable to remove node from the scene: ");
            System.out.println(this);
            System.out.println(node);
        }
    }
}
