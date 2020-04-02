package ge.geometry;


import ge.utils.GERegularBoundingBox;

public class GETriangle extends GERegularPolygon {

    public GETriangle(double sideLength){
        super(sideLength*Math.sqrt(3)/3, 3, -Math.PI/2);
    }

    public GETriangle(GERegularBoundingBox boundingBox){
        super(boundingBox.size/2, 3, -Math.PI/2);
    }
}
