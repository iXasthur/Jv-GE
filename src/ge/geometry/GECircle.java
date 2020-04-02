package ge.geometry;

import ge.utils.GERegularBoundingBox;
import javafx.scene.shape.Circle;

public class GECircle extends GEGeometry {

    public GECircle(double radius){
        super(new Circle(radius));
    }

    public GECircle(GERegularBoundingBox boundingBoxSideSize){
        super(new Circle(boundingBoxSideSize.size/2));
    }
}
