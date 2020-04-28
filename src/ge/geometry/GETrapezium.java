package ge.geometry;

import ge.utils.GEBoundingBox;
import ge.utils.GERegularBoundingBox;

public class GETrapezium extends GEGeometry {
    private static double[] getTrapeziumPoints(GEBoundingBox boundingBox, double upperSideScale, double heightScale){
        double[] buffVertices = new double[8]; // [x, y] 4 times

        buffVertices[0] = -boundingBox.getWidth()/2.0 * upperSideScale;
        buffVertices[1] = -boundingBox.getHeight()/2.0 * heightScale;

        buffVertices[2] = boundingBox.getWidth()/2.0 * upperSideScale;
        buffVertices[3] = -boundingBox.getHeight()/2.0 * heightScale;

        buffVertices[4] = boundingBox.getWidth()/2.0;
        buffVertices[5] = boundingBox.getHeight()/2.0 * heightScale;

        buffVertices[6] = -boundingBox.getWidth()/2.0;
        buffVertices[7] = boundingBox.getHeight()/2.0 * heightScale;

        return buffVertices;
    }

    public GETrapezium(GEBoundingBox boundingBox, double upperSideScale, double heightScale){
        super(getTrapeziumPoints(boundingBox, upperSideScale, heightScale));
    }

    public GETrapezium(GERegularBoundingBox boundingBox){
        super(getTrapeziumPoints(boundingBox, 0.5, 0.65));
    }
}
