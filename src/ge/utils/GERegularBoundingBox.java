package ge.utils;

public class GERegularBoundingBox extends GEBoundingBox {

    public final double size;

    public GERegularBoundingBox(double size){
        super(-size/2,-size/2,size/2,size/2);
        this.size = size;
    }

}
