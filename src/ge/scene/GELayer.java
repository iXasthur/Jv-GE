package ge.scene;

import javafx.scene.Group;

import java.util.Vector;

public class GELayer {

    private final Vector<GENode> layerNodes;
    private final Group group;
    private final int z;

    public GELayer(int z){
        this.z = z;

        group = new Group();
        group.managedProperty().bind(group.visibleProperty());

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

    public void show(){
        group.setVisible(true);
    }

    public void hide(){
        group.setVisible(false);
    }

    public Vector<GENode> getNodes(){
        return layerNodes;
    }
}
