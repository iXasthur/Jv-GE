package sample;

public class GETriangle extends GEGeometry {
    private final static double[] vertices = {
             0.0,   1.0,
            -1.0,  -1.0,
             1.0,  -1.0,
    };

    public GETriangle(double sideLength){
        super(vertices);
        this.getShape().setScaleX(sideLength/2);
        this.getShape().setScaleY(sideLength/2);
    }
}
