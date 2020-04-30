package ge.utils;

import javafx.scene.paint.Color;

import java.util.Random;
import java.util.Vector;

public class GEColor {
    public static final Color purple = Color.rgb(193, 88, 220);
    public static final Color blue = Color.rgb(115, 232, 255);
    public static final Color green = Color.rgb(176, 255, 87);
    public static final Color red = Color.rgb(244, 67, 54);
    public static final Color orange = Color.rgb(255, 145, 0);
    public static final Color yellow = Color.rgb(253,216,53);
    public static final Color gray = Color.rgb(24, 24, 24);

    public static final Color stdUINodeColor = yellow;
    public static final Color stdSceneNodeColor = blue;
    public static final Color stdPreviewNodeColor = withAlpha(stdSceneNodeColor,0.3);
    public static final Color stdBackgroundColor = gray;

    public static Color withAlpha(Color color, double alpha) {
        int r = (int)(color.getRed()*100);
        int g = (int)(color.getGreen()*100);
        int b = (int)(color.getBlue()*100);
        return Color.rgb(r, g, b, alpha);
    };

    public static Color random() {
        Vector<Color> colors = new Vector<>(0);
        // gray is not included
        colors.add(purple);
        colors.add(blue);
        colors.add(green);
        colors.add(red);
        colors.add(orange);
        colors.add(green);

        int i = new Random().nextInt(colors.size());
        return colors.elementAt(i);
    }
}
