package ge.geometry;

import ge.utils.GERegularBoundingBox;

public class GESquare extends GERegularPolygon {
//    private final static double[] vertices = {
//             1.0,  1.0,
//            -1.0,  1.0,
//            -1.0, -1.0,
//             1.0, -1.0,
//    };
//
//    private static double[] transformVertices(double sideLength){
//        double[] buffVertices = vertices.clone();
//        for(int i=0; i<buffVertices.length; i++){
//            buffVertices[i] *= sideLength/2;
//        }
//        return buffVertices;
//    }
//
//    public GESquare(double sideLength){
//        super(transformVertices(sideLength));
//    }

    public GESquare(GERegularBoundingBox boundingBox){
        super(boundingBox.size/2, 4, Math.PI/4.0);
    }
}
