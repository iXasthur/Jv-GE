package ge.scene;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;

import java.util.Vector;

public class GEScene {

    private final Scene rootScene;
    private final Group rootNode;

    private class SceneLayers {
        public GELayer backgroundLayer;
        public GELayer mainLayer;
        public GELayer uiLayer;

        public SceneLayers(Group root) {
            backgroundLayer = new GELayer(0);
            mainLayer = new GELayer(1);
            uiLayer = new GELayer(2);

            root.getChildren().add(backgroundLayer.getNodesGroup());
            root.getChildren().add(mainLayer.getNodesGroup());
            root.getChildren().add(uiLayer.getNodesGroup());
        }
    }
    private final SceneLayers sceneLayers;


    public GEScene(Scene rootScene){
        this.rootScene = rootScene;
        rootNode = (Group) rootScene.getRoot();

        sceneLayers = new SceneLayers(rootNode);
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

    public void addNodeToBackgroundLayer(GENode node) {
        addNodeToTheLayer(sceneLayers.backgroundLayer, node);
    }

    public void addNodeToMainLayer(GENode node) {
        addNodeToTheLayer(sceneLayers.mainLayer, node);
    }

    public void addNodeToUILayer(GENode node) {
        addNodeToTheLayer(sceneLayers.uiLayer, node);
    }

    public void removeNodeFromBackgroundLayer(GENode node){
        removeNodeFromTheLayer(sceneLayers.backgroundLayer, node);
    }

    public void removeNodeFromMainLayer(GENode node){
        removeNodeFromTheLayer(sceneLayers.mainLayer, node);
    }

    public void removeNodeFromUILayer(GENode node){
        removeNodeFromTheLayer(sceneLayers.uiLayer, node);
    }

    public GENode getNodeByShape(Shape shape){
        GELayer[] layers = new GELayer[3];
        layers[0] = sceneLayers.backgroundLayer;
        layers[1] = sceneLayers.mainLayer;
        layers[2] = sceneLayers.uiLayer;

        for (GELayer layer : layers){
            Vector<GENode> nodes = layer.getNodes();
            for (GENode node: nodes){
                if (node.getGeometry().getShape() == shape){
                    return node;
                }
            }
        }
        return null;
    }

    public void show(){
        sceneLayers.uiLayer.show();
        sceneLayers.mainLayer.show();
        sceneLayers.backgroundLayer.show();
    }

    public void hide(){
        sceneLayers.uiLayer.hide();
        sceneLayers.mainLayer.hide();
        sceneLayers.backgroundLayer.hide();
    }
}
