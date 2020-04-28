package ge.geometry;

import ge.utils.GEBoundingBox;
import ge.utils.GERegularBoundingBox;

public class GELine extends GEGeometry {

    private static double[] getLinePoints(GEBoundingBox boundingBox){
        double[] buffVertices = new double[4]; // [x, y] 4 times

        buffVertices[0] = boundingBox.getPoint1().x;
        buffVertices[1] = boundingBox.getPoint1().y;

        buffVertices[2] = boundingBox.getPoint2().x;
        buffVertices[3] = boundingBox.getPoint2().y;

        return buffVertices;
    }

    public GELine(GEBoundingBox boundingBox){
        super(getLinePoints(boundingBox));
    }

    public GELine(GERegularBoundingBox boundingBox){
        super(getLinePoints(boundingBox));
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
