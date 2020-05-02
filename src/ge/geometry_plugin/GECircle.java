package ge.geometry_plugin;

import ge.geometry.GERegularPolygon;
import ge.utils.GERegularBoundingBox;

public class GECircle extends GERegularPolygon {

    public GECircle(double radius){
        super(radius, (int)radius, 0);
    }

    public GECircle(GERegularBoundingBox boundingBoxSideSize){
        super(boundingBoxSideSize.size/2, 360, 0);
    }
}
