package sample;

import javafx.scene.shape.Circle;

public class GECircle extends GEGeometry {

    private static double[] getPlatonicPoints(double radius, int divisionCount, double alphaOffset){
        double[] buffVertices = new double[divisionCount*2];
        double alpha = alphaOffset;
        for(int i=0; i<buffVertices.length; i+=2){
            buffVertices[i] = Math.cos(alpha)*radius;
            buffVertices[i+1] = Math.sin(alpha)*radius;
            alpha = alpha + 2*Math.PI/divisionCount;
        }
        return buffVertices;
    }

    public GECircle(double radius){
        super(new Circle(radius));
    }

    public GECircle(double radius, int divisionCount, double alphaOffset){
        super(getPlatonicPoints(radius, divisionCount, alphaOffset));
    }
}
