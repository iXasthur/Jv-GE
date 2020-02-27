package sample;

public class GESquare extends GEGeometry {
    private final static double[] vertices = {
             1.0,  1.0,
            -1.0,  1.0,
            -1.0, -1.0,
             1.0, -1.0,
    };

    public GESquare(double sideLength){
        super(vertices);
        this.getShape().setScaleX(sideLength/2);
        this.getShape().setScaleY(sideLength/2);
    }
}
