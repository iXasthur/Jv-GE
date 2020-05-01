package ge.utils;

import ge.geometry.GEGeometry;
import ge.scene.GELayer;
import ge.scene.GENode;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class GESerializer {

    public static String serialize(GELayer layer) {
        StringBuilder serializer = new StringBuilder();
        Vector<GENode> nodes = layer.getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            GEGeometry geometry = nodes.elementAt(i).getGeometry();
            Color fillColor = geometry.getFillColor();
            Color strokeColor = geometry.getStrokeColor();
            double strokeWidth = geometry.getStrokeWidth();

            serializer.append("node[\n");

            serializer.append("points[\n");
            double[] points = geometry.getPoints();
            for (double point : points) {
                serializer.append(point + "\n");
            }
            serializer.append("]\n");

            serializer.append("position[\n");
            serializer.append(geometry.getShape().getLayoutX() + "\n");
            serializer.append(geometry.getShape().getLayoutY() + "\n");
            serializer.append("]\n");

            serializer.append("fcolor[\n");
            serializer.append(fillColor.getRed() + "\n");
            serializer.append(fillColor.getGreen() + "\n");
            serializer.append(fillColor.getBlue() + "\n");
            serializer.append("]\n");

            serializer.append("scolor[\n");
            serializer.append(strokeColor.getRed() + "\n");
            serializer.append(strokeColor.getGreen() + "\n");
            serializer.append(strokeColor.getBlue() + "\n");
            serializer.append("]\n");

            serializer.append("swidth[\n");
            serializer.append(strokeWidth + "\n");
            serializer.append("]\n");

            serializer.append("]\n");
        }
        return serializer.toString();
    }

    private static Vector<String> parse(String string, String pattern) {
        Vector<String> nodeStrings = new Vector<>(0);
        int startIndex  = string.indexOf(pattern);
        while (startIndex != -1) {
            boolean skipBracket = true;
            int bracketCount = 1;
            int i = startIndex;

            while (i < string.length() && bracketCount > 0) {
                if (string.charAt(i) == '[') {
                    if (!skipBracket) {
                        bracketCount++;
                    } else {
                        skipBracket = false;
                    }
                } else if (string.charAt(i) == ']') {
                    bracketCount--;
                }

                i++;
            }

            if (bracketCount > 0) {
                return null;
            }
            nodeStrings.add(string.substring(startIndex, i));
            startIndex  = string.indexOf(pattern, i);
        }

        return nodeStrings;
    }

    private static double[] parseDoubleArray(String string) {
        if (string.indexOf("[") == string.lastIndexOf("[") && string.indexOf("]") == string.lastIndexOf("]")) {
            Vector<Double> values = new Vector<>(0);
            int startIndex = string.indexOf("[");
            int endIndex = string.lastIndexOf("]");

            string = string.substring(startIndex, endIndex + 1);
            String[] strings = string.split("\n");

            for (String s : strings) {
                if (!(s.contains("[") || s.contains("]"))) {
                    double v;
                    try {
                        v = Double.parseDouble(s);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                    values.add(v);
                }
            }

            double[] arr = new double[values.size()];
            for (int i = 0; i < values.size(); i++) {
                arr[i] = values.elementAt(i);
            }
            return arr;
        } else {
            return null;
        }
    }

    private static double[] findBlockAndParseToArray(String string, String pattern) {
        Vector<String> v = parse(string, pattern);
        String vString = null;
        if (v != null && v.size() == 1) {
            vString = v.firstElement();
        } else {
            return null;
        }
        double[] values = parseDoubleArray(vString);
        if (values == null) {
            return null;
        }
        return values;
    }

    public static GELayer deserialize(String string) {
        Vector<String> nodeStrings = parse(string, "node[");
        if (nodeStrings == null) {
            return null;
        }
        GELayer layer = new GELayer(1);

        for (int i = 0; i < nodeStrings.size(); i++) {
            double[] points = findBlockAndParseToArray(nodeStrings.elementAt(i), "points[");
            double[] fillColorRGB = findBlockAndParseToArray(nodeStrings.elementAt(i), "fcolor[");
            double[] strokeColorRGB = findBlockAndParseToArray(nodeStrings.elementAt(i), "scolor[");
            double[] strokeWidthArr = findBlockAndParseToArray(nodeStrings.elementAt(i), "swidth[");
            double[] positionArr = findBlockAndParseToArray(nodeStrings.elementAt(i), "position[");

            if (points != null && fillColorRGB != null && strokeColorRGB != null && strokeWidthArr != null && positionArr != null) {
                if (points.length % 2 == 1) {
                    return null;
                }
                if (fillColorRGB.length != 3) {
                    return null;
                }
                if (strokeColorRGB.length != 3) {
                    return null;
                }
                if (strokeWidthArr.length != 1) {
                    return null;
                }
                if (positionArr.length != 2) {
                    return null;
                }

                GENode node = new GENode(new GEGeometry(points));
                node.setFillColor(new Color(fillColorRGB[0], fillColorRGB[1], fillColorRGB[2], 1));
                node.setStrokeColor(new Color(strokeColorRGB[0], strokeColorRGB[1], strokeColorRGB[2], 1));
                node.setStrokeWidth(strokeWidthArr[0]);
                node.moveTo(positionArr[0], positionArr[1]);
                layer.addNode(node);
            } else {
                return null;
            }
        }

        return layer;
    }
}
