import java.awt.*;
import java.awt.geom.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Utility class for drawing functions and finding intersections.
 */
public class GraphUtils {

    /**
     * Draws a function on the given Graphics2D object.
     *
     * @param g2 the Graphics2D object
     * @param color the color to draw the function
     * @param function the function to draw
     * @param points the list to store the points of the function
     * @param width the width of the drawing area
     * @param height the height of the drawing area
     * @param scale the scale of the drawing
     */
    public static void drawFunction(Graphics2D g2, Color color, Function<Double, Double> function, List<Point> points, int width, int height, double scale) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2.0f));

        int numPoints = width * 50;
        Point prevPoint = null;
        for (int i = 0; i < numPoints; i++) {
            double x = (i - width / 2) / scale;
            double y = function.apply(x);
            int xPos = i;
            int yPos = (int) (height / 2 - scale * y);
            points.add(new Point(xPos, yPos));
            if (prevPoint != null && Math.abs(yPos - prevPoint.y) < height) {
                g2.draw(new Line2D.Double(prevPoint.x, prevPoint.y, xPos, yPos));
            }
            prevPoint = new Point(xPos, yPos);
        }
    }

    /**
     * Draws the ticks on the axes.
     *
     * @param g2 the Graphics2D object
     * @param width the width of the drawing area
     * @param height the height of the drawing area
     * @param scale the scale of the drawing
     */
    public static void drawTicks(Graphics2D g2, int width, int height, double scale) {
        int centerX = width / 2;
        int centerY = height / 2;
        int pixelsPerTick = (int) (50 * scale);

        int startOffsetX = centerX % pixelsPerTick;
        for (int x = startOffsetX; x < width; x += pixelsPerTick) {
            g2.drawLine(x, centerY - 5, x, centerY + 5);
            int tickLabel = (x - centerX) / pixelsPerTick * 50;
            g2.drawString(Integer.toString(tickLabel), x - 10, centerY + 20);
        }

        int startOffsetY = centerY % pixelsPerTick;
        for (int y = startOffsetY; y < height; y += pixelsPerTick) {
            g2.drawLine(centerX - 5, y, centerX + 5, y);
            int tickLabel = (centerY - y) / pixelsPerTick * 50;
            g2.drawString(Integer.toString(tickLabel), centerX + 10, y + 5);
        }
    }

    /**
     * Finds all intersections between pairs of functions.
     *
     * @param intersections the list to store the intersection points
     * @param functionPoints the list of lists of points for each function
     * @param functions the list of functions
     * @param width the width of the drawing area
     * @param height the height of the drawing area
     * @param scale the scale of the drawing
     */
    public static void findAllIntersections(List<GraphPanel.IntersectionPoint> intersections, List<List<Point>> functionPoints, List<Function<Double, Double>> functions, int width, int height, double scale) {
        for (int i = 0; i < functionPoints.size(); i++) {
            for (int j = i + 1; j < functionPoints.size(); j++) {
                findIntersections(intersections, functionPoints.get(i), functionPoints.get(j), functions.get(i), functions.get(j), width, height, scale);
            }
        }
    }

    /**
     * Finds intersections between two functions.
     *
     * @param intersections the list to store the intersection points
     * @param first the points of the first function
     * @param second the points of the second function
     * @param firstFunction the first function
     * @param secondFunction the second function
     * @param width the width of the drawing area
     * @param height the height of the drawing area
     * @param scale the scale of the drawing
     */
    public static void findIntersections(List<GraphPanel.IntersectionPoint> intersections, List<Point> first, List<Point> second, Function<Double, Double> firstFunction, Function<Double, Double> secondFunction, int width, int height, double scale) {
        for (int i = 1; i < first.size(); i++) {
            if ((first.get(i-1).y - second.get(i-1).y) * (first.get(i).y - second.get(i).y) <= 0) {
                int midX = (first.get(i).x + first.get(i-1).x) / 2;
                double xValue = (midX - width / 2) / scale;
                double firstY = firstFunction.apply(xValue);
                double secondY = secondFunction.apply(xValue);
                double yValue = (firstY + secondY) / 2;
                int midY = (int) (height / 2 - scale * yValue);
                intersections.add(new GraphPanel().new IntersectionPoint(midX, midY, xValue, yValue));
            }
        }
    }

    /**
     * Draws an ellipse on the given Graphics2D object.
     *
     * @param g2 the Graphics2D object
     * @param points the list to store the points of the ellipse
     * @param width the width of the drawing area
     * @param height the height of the drawing area
     * @param sinA the semi-major axis of the ellipse
     * @param sinB the semi-minor axis of the ellipse
     * @param scale the scale of the drawing
     */
    public static void drawEllipse(Graphics2D g2, List<Point> points, int width, int height, double sinA, double sinB, double scale) {
        g2.setColor(Color.PINK);
        int x = width / 2 - (int) (sinA * scale);
        int y = height / 2 - (int) (sinB * scale);
        int ellipseWidth = 2 * (int) (sinA * scale);
        int ellipseHeight = 2 * (int) (sinB * scale);
        g2.drawOval(x, y, ellipseWidth, ellipseHeight);
        points.add(new Point(x, y));
    }

    /**
     * Draws a circle on the given Graphics2D object.
     *
     * @param g2 the Graphics2D object
     * @param points the list to store the points of the circle
     * @param width the width of the drawing area
     * @param height the height of the drawing area
     * @param sinA the x-coordinate of the circle center
     * @param sinB the y-coordinate of the circle center
     * @param radius the radius of the circle
     * @param scale the scale of the drawing
     */
    public static void drawCircle(Graphics2D g2, List<Point> points, int width, int height, double sinA, double sinB, double radius, double scale) {
        g2.setColor(Color.BLACK);
        int r = (int) (radius * scale);
        int x = width / 2 - r + (int) (sinA * scale);
        int y = height / 2 - r + (int) (sinB * scale);
        g2.drawOval(x, y, 2 * r, 2 * r);
        points.add(new Point(x + r, y + r));
    }
}
