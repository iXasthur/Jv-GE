package ge.geometry;

import ge.utils.GERegularBoundingBox;

public class GECircle extends GERegularPolygon {

    public GECircle(double radius){
        super(radius, (int)radius, 0);
    }

    public GECircle(GERegularBoundingBox boundingBoxSideSize){
        super(boundingBoxSideSize.size/2, (int)boundingBoxSideSize.size, 0);
    }
}
