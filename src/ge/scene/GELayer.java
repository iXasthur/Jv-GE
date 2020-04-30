package ge.scene;

import javafx.scene.Group;

import java.util.Vector;

public class GELayer {

    private final Vector<GENode> layerNodes;
    private final Group group;
    private final int z; // Used to sort layers, will be implemented soon

    public GELayer(int z){
        this.z = z;

        group = new Group();
        layerNodes = new Vector<>(0);
    }

    public Group getNodesGroup(){
        return group;
    }

    public void addNode(GENode node){
        layerNodes.addElement(node);
        group.getChildren().add(node.getGeometry().getGroup());
    }

    public void removeNode(GENode node){
        layerNodes.removeElement(node);
        group.getChildren().remove(node.getGeometry().getGroup());
    }

    public Vector<GENode> getNodes(){
        return layerNodes;
    }
}
