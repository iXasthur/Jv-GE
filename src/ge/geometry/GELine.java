package ge.geometry;

import ge.utils.GERegularBoundingBox;

public class GELine extends GERegularPolygon {

    public GELine(GERegularBoundingBox boundingBox){
        super(boundingBox.size/2, 2, Math.PI/4);
    }

}

//public class GELine extends GEGeometry {
//
//    public GELine(int X1, int Y1, int X2, int Y2){
//        super(new Line(X1,Y1,X2,Y2));
//    }
//
//    public GELine(GERegularBoundingBox boundingBox){
//        super(new Line(-boundingBox.size/2,-boundingBox.size/2,boundingBox.size/2,boundingBox.size/2));
//    }
//}
