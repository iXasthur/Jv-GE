package sample;

import javafx.scene.shape.Line;

public class GELine extends GEGeometry {
    public GELine(int X1, int Y1, int X2, int Y2){
        super(new Line(X1,Y1,X2,Y2));
    }
}
