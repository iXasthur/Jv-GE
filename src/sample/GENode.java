package sample;

import javafx.scene.Group;

public class GENode extends Group {

    private GEGeometry geometry = null;

    public GENode(){
        super();
    }

    public void setGeometry(GEGeometry g){
        if (geometry != null) {
            this.getChildren().remove(geometry);
        }
        geometry = g;
        this.getChildren().add(g);
    }

    public GEGeometry getGeometry(){
        return geometry;
    }

}
