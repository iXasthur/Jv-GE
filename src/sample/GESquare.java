package sample;

public class GESquare extends GEGeometry {
    private final static double[] geometryPoints = {
             1.0,  1.0,
            -1.0,  1.0,
            -1.0, -1.0,
             1.0, -1.0,
    };

    public GESquare(double sideLength){
        super(geometryPoints);
        this.setScaleXY(sideLength/2);
    }
}
