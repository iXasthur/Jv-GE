package sample;

public class GETriangle extends GEGeometry {
    private final static double[] geometryPoints = {
             0.0,   1.0,
            -1.0,  -1.0,
             1.0,  -1.0,
    };

    public GETriangle(double sideLength){
        super(geometryPoints);
        this.setScaleXY(sideLength/2);
    }
}
