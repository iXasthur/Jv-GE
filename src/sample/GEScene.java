package sample;

import javafx.scene.Group;
import javafx.scene.Scene;

import java.util.Vector;

public class GEScene {

    private final Group rootNode;
    private Vector<GELayer> sceneLayers;
    private GELayer selectedLayer;

    public GEScene(Scene rootScene){
        rootNode = (Group) rootScene.getRoot();
        sceneLayers = new Vector<>(0);
        selectedLayer = null;

        createNewLayer();
    }

    private void createNewLayer(){
        GELayer newLayer = new GELayer();
        sceneLayers.addElement(newLayer);
        rootNode.getChildren().add(newLayer.getNodesGroup());
        // Add handle of Z value

        selectedLayer = newLayer;
    }

    private void addNodeToTheLayer(GELayer layer, GENode node){
        if (layer != null && node != null) {
            layer.addNode(node);
        } else {
            System.out.print(">Unable to add node to the layer: ");
            System.out.println(layer);
            System.out.println(node);
        }
    }

    private void removeNodeFromTheLayer(GELayer layer, GENode node){
        if (layer != null && node != null) {
            layer.removeNode(node);
        } else {
            System.out.print(">Unable to remove node from the layer: ");
            System.out.println(layer);
            System.out.println(node);
        }
    }

    public void addNodeToSelectedLayer(GENode node){
        addNodeToTheLayer(selectedLayer, node);
    }

    public void removeNodeFromSelectedLayer(GENode node){
        removeNodeFromTheLayer(selectedLayer, node);
    }

}
