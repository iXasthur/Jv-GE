package ge.scene;

import com.sun.tools.javac.Main;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;

import java.util.Vector;

public class GEScene {

    private final Scene rootScene;
    private final Group rootNode;

    public enum SceneStates {
        WAITING_FOR_SELECTION,
        WAITING_FOR_NODE_PLACEMENT_POINT1,
        WAITING_FOR_NODE_PLACEMENT_POINT2
    }
    private SceneStates sceneState;

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
        sceneState = SceneStates.WAITING_FOR_SELECTION;
        sceneLayers = new SceneLayers(rootNode);
    }

    public SceneStates getSceneState(){
        return sceneState;
    }

    public void setState(SceneStates state){
        sceneState = state;
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
}
