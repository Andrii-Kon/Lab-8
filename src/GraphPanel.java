import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GraphPanel extends JPanel {

    private double A = 1.0;
    private double B = 10.0;
    private double C = 0.1;
    private double scale = 2;
    private double radius = 5.0;

    private boolean drawSin = false;
    private boolean drawCos = false;
    private boolean drawTan = false;
    private boolean drawCtan = false;
    private boolean drawParabola = false;
    private boolean drawHyperbola = false;
    private boolean drawEllipse = false;
    private boolean drawCircle = false;
    private boolean drawExp = false;
    private boolean drawLog = false;

    private final List<IntersectionPoint> intersections = new ArrayList<>();
    private IntersectionPoint hoveredPoint;

    public GraphPanel() {
        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            if (notches < 0) {
                scale *= 1.1;
            } else {
                scale /= 1.1;
            }
            repaint();
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hoveredPoint = null;
                for (IntersectionPoint p : intersections) {
                    if (Math.abs(e.getX() - p.x) < 10 && Math.abs(e.getY() - p.y) < 10) {
                        hoveredPoint = p;
                        break;
                    }
                }
                repaint();
            }
        });
    }

    static class IntersectionPoint extends Point {
        double xValue, yValue;

        IntersectionPoint(int x, int y, double xValue, double yValue) {
            super(x, y);
            this.xValue = xValue;
            this.yValue = yValue;
        }
    }

    /**
     * Draws a function on the panel.
     * @param g2 The Graphics2D object for drawing.
     * @param color The color of the function curve.
     * @param function The mathematical function to draw.
     * @param points List of points for calculating intersections.
     */
    private void drawFunction(Graphics2D g2, Color color, Function<Double, Double> function, List<Point> points) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2.0f));

        int numPoints = getWidth() * 50;
        Point prevPoint = null;
        for (int i = 0; i < numPoints; i++) {
            double x = (i - (double) getWidth() / 2) / scale;
            double y = function.apply(x);
            int yPos = (int) (getHeight() / 2 - scale * y);
            points.add(new Point(i, yPos));
            if (prevPoint != null && Math.abs(yPos - prevPoint.y) < getHeight()) {
                g2.draw(new Line2D.Double(prevPoint.x, prevPoint.y, i, yPos));
            }
            prevPoint = new Point(i, yPos);
        }
    }

    /**
     * Finds all intersection points between the functions drawn.
     * @param functionPoints A list of lists of points for each function.
     * @param functions A list of the functions being drawn.
     */
    private void findAllIntersections(List<List<Point>> functionPoints, List<Function<Double, Double>> functions) {
        for (int i = 0; i < functionPoints.size(); i++) {
            for (int j = i + 1; j < functionPoints.size(); j++) {
                findIntersections(functionPoints.get(i), functionPoints.get(j), functions.get(i), functions.get(j));
            }
        }
    }

    /**
     * Finds intersection points between two sets of points that represent functions.
     * @param first List of points for the first function.
     * @param second List of points for the second function.
     * @param firstFunction Mathematical function for the first set of points.
     * @param secondFunction Mathematical function for the second set of points.
     */
    private void findIntersections(List<Point> first, List<Point> second, Function<Double, Double> firstFunction, Function<Double, Double> secondFunction) {
        for (int i = 1; i < first.size(); i++) {
            if ((first.get(i-1).y - second.get(i-1).y) * (first.get(i).y - second.get(i).y) <= 0) {
                int midX = (first.get(i).x + first.get(i-1).x) / 2;
                double xValue = (midX - (double) getWidth() / 2) / scale;
                double firstY = firstFunction.apply(xValue);
                double secondY = secondFunction.apply(xValue);
                double yValue = (firstY + secondY) / 2;
                int midY = (int) (getHeight() / 2 - scale * yValue);
                intersections.add(new IntersectionPoint(midX, midY, xValue, yValue));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
        g2.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());

        drawTicks(g2);

        intersections.clear();
        List<Point> sinPoints = new ArrayList<>();
        List<Point> cosPoints = new ArrayList<>();
        List<Point> tanPoints = new ArrayList<>();
        List<Point> ctanPoints = new ArrayList<>();
        List<Point> parabolaPoints = new ArrayList<>();
        List<Point> hyperbolaPoints = new ArrayList<>();
        List<Point> ellipsePoints = new ArrayList<>();
        List<Point> circlePoints = new ArrayList<>();
        List<Point> expPoints = new ArrayList<>();
        List<Point> logPoints = new ArrayList<>();
        List<List<Point>> functionPoints = new ArrayList<>();
        List<Function<Double, Double>> functions = new ArrayList<>();

        if (drawSin) {
            drawFunction(g2, Color.RED, x -> A + B * Math.sin(C * x), sinPoints);
            functionPoints.add(sinPoints);
            functions.add(x -> A + B * Math.sin(C * x));
        }
        if (drawCos) {
            drawFunction(g2, Color.BLUE, x -> A + B * Math.cos(C * x), cosPoints);
            functionPoints.add(cosPoints);
            functions.add(x -> A + B * Math.cos(C * x));
        }
        if (drawTan) {
            drawFunction(g2, Color.GREEN, x -> A + B * Math.tan(C * x), tanPoints);
            functionPoints.add(tanPoints);
            functions.add(x -> A + B * Math.tan(C * x));
        }
        if (drawCtan) {
            drawFunction(g2, Color.ORANGE, x -> A + B / Math.tan(C * x), ctanPoints);
            functionPoints.add(ctanPoints);
            functions.add(x -> A + B / Math.tan(C * x));
        }
        if (drawParabola) {
            drawFunction(g2, Color.MAGENTA, x -> A * x * x + B * x + C, parabolaPoints);
            functionPoints.add(parabolaPoints);
            functions.add(x -> A * x * x + B * x + C);
        }
        if (drawHyperbola) {
            drawFunction(g2, Color.CYAN, x -> B / (C * x), hyperbolaPoints);
            functionPoints.add(hyperbolaPoints);
            functions.add(x -> B / (C * x));
        }
        if (drawEllipse) {
            drawEllipse(g2, ellipsePoints);
        }
        if (drawCircle) {
            drawCircle(g2, circlePoints);
        }
        if (drawExp) {
            drawFunction(g2, Color.YELLOW, x -> A * Math.exp(B * x), expPoints);
            functionPoints.add(expPoints);
            functions.add(x -> A * Math.exp(B * x));
        }
        if (drawLog) {
            drawFunction(g2, new Color(100, 50, 200), x -> A * Math.log(B * x), logPoints);
            functionPoints.add(logPoints);
            functions.add(x -> A * Math.log(B * x));
        }

        findAllIntersections(functionPoints, functions);

        if (hoveredPoint != null) {
            g2.setColor(Color.GRAY);
            g2.fillOval(hoveredPoint.x - 5, hoveredPoint.y - 5, 10, 10);
            String text = String.format("X: %.2f, Y: %.2f", hoveredPoint.xValue, hoveredPoint.yValue);
            g2.drawString(text, hoveredPoint.x + 10, hoveredPoint.y - 10);
        }
    }

    /**
     * Draws tick marks along the axes.
     * @param g2 The Graphics2D object for drawing.
     */
    private void drawTicks(Graphics2D g2) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int pixelsPerTick = (int) (50 * scale);

        int startOffsetX = centerX % pixelsPerTick;
        for (int x = startOffsetX; x < getWidth(); x += pixelsPerTick) {
            g2.drawLine(x, centerY - 5, x, centerY + 5);
            int tickLabel = (x - centerX) / pixelsPerTick * 50;
            g2.drawString(Integer.toString(tickLabel), x - 10, centerY + 20);
        }

        int startOffsetY = centerY % pixelsPerTick;
        for (int y = startOffsetY; y < getHeight(); y += pixelsPerTick) {
            g2.drawLine(centerX - 5, y, centerX + 5, y);
            int tickLabel = (centerY - y) / pixelsPerTick * 50;
            g2.drawString(Integer.toString(tickLabel), centerX + 10, y + 5);
        }
    }

    /**
     * Draws an ellipse centered at the panel's center, adjusted for scale and coefficients A and B.
     * @param g2 The Graphics2D object for drawing.
     * @param points The list of points for intersection logic.
     */
    private void drawEllipse(Graphics2D g2, List<Point> points) {
        g2.setColor(Color.PINK);
        int x = getWidth() / 2 - (int) (A * scale);
        int y = getHeight() / 2 - (int) (B * scale);
        int width = 2 * (int) (A * scale);
        int height = 2 * (int) (B * scale);
        g2.drawOval(x, y, width, height);
        points.add(new Point(x, y));
    }

    /**
     * Draws a circle based on the radius specified by the 'radius' variable, adjusted for scale.
     * @param g2 The Graphics2D object for drawing.
     * @param points The list of points for intersection logic.
     */
    private void drawCircle(Graphics2D g2, List<Point> points) {
        g2.setColor(Color.BLACK);
        int r = (int) (radius * scale);
        int x = getWidth() / 2 - r + (int) (A * scale);
        int y = getHeight() / 2 - r + (int) (B * scale);
        g2.drawOval(x, y, 2 * r, 2 * r);
        points.add(new Point(x + r, y + r));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GraphPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        GraphPanel graphPanel = new GraphPanel();
        frame.add(graphPanel, BorderLayout.CENTER);

        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));

        JSlider sliderA = new JSlider(-10, 10, 1);
        sliderA.addChangeListener(e -> {
            graphPanel.A = sliderA.getValue();
            graphPanel.repaint();
        });
        sliderPanel.add(new JLabel("A"));
        sliderPanel.add(sliderA);

        JSlider sliderB = new JSlider(-10, 10, 10);
        sliderB.addChangeListener(e -> {
            graphPanel.B = sliderB.getValue();
            graphPanel.repaint();
        });
        sliderPanel.add(new JLabel("B"));
        sliderPanel.add(sliderB);

        JSlider sliderC = new JSlider(-10, 10, 1);
        sliderC.addChangeListener(e -> {
            graphPanel.C = sliderC.getValue();
            graphPanel.repaint();
        });
        sliderPanel.add(new JLabel("C"));
        sliderPanel.add(sliderC);

        JSlider sliderRadius = new JSlider(0, 10, 5);
        sliderRadius.addChangeListener(e -> {
            graphPanel.radius = sliderRadius.getValue();
            graphPanel.repaint();
        });
        sliderPanel.add(new JLabel("Radius"));
        sliderPanel.add(sliderRadius);

        JCheckBox checkBoxSin = new JCheckBox("sin", false);
        checkBoxSin.addActionListener(e -> {
            graphPanel.drawSin = checkBoxSin.isSelected();
            graphPanel.repaint();
        });
        sliderPanel.add(checkBoxSin);

        JCheckBox checkBoxCos = new JCheckBox("cos", false);
        checkBoxCos.addActionListener(e -> {
            graphPanel.drawCos = checkBoxCos.isSelected();
            graphPanel.repaint();
        });
        sliderPanel.add(checkBoxCos);

        JCheckBox checkBoxTan = new JCheckBox("tan", false);
        checkBoxTan.addActionListener(e -> {
            graphPanel.drawTan = checkBoxTan.isSelected();
            graphPanel.repaint();
        });
        sliderPanel.add(checkBoxTan);

        JCheckBox checkBoxCtan = new JCheckBox("ctan", false);
        checkBoxCtan.addActionListener(e -> {
            graphPanel.drawCtan = checkBoxCtan.isSelected();
            graphPanel.repaint();
        });
        sliderPanel.add(checkBoxCtan);

        JCheckBox checkBoxParabola = new JCheckBox("Parabola", false);
        checkBoxParabola.addActionListener(e -> {
            graphPanel.drawParabola = checkBoxParabola.isSelected();
            graphPanel.repaint();
        });
        sliderPanel.add(checkBoxParabola);

        JCheckBox checkBoxHyperbola = new JCheckBox("Hyperbola", false);
        checkBoxHyperbola.addActionListener(e -> {
            graphPanel.drawHyperbola = checkBoxHyperbola.isSelected();
            graphPanel.repaint();
        });
        sliderPanel.add(checkBoxHyperbola);

        JCheckBox checkBoxEllipse = new JCheckBox("Ellipse", false);
        checkBoxEllipse.addActionListener(e -> {
            graphPanel.drawEllipse = checkBoxEllipse.isSelected();
            graphPanel.repaint();
        });
        sliderPanel.add(checkBoxEllipse);

        JCheckBox checkBoxCircle = new JCheckBox("Circle", false);
        checkBoxCircle.addActionListener(e -> {
            graphPanel.drawCircle = checkBoxCircle.isSelected();
            graphPanel.repaint();
        });
        sliderPanel.add(checkBoxCircle);

        JCheckBox checkBoxExp = new JCheckBox("Exponential", false);
        checkBoxExp.addActionListener(e -> {
            graphPanel.drawExp = checkBoxExp.isSelected();
            graphPanel.repaint();
        });
        sliderPanel.add(checkBoxExp);

        JCheckBox checkBoxLog = new JCheckBox("Logarithmic", false);
        checkBoxLog.addActionListener(e -> {
            graphPanel.drawLog = checkBoxLog.isSelected();
            graphPanel.repaint();
        });
        sliderPanel.add(checkBoxLog);

        frame.add(sliderPanel, BorderLayout.EAST);
        frame.setVisible(true);
    }

    @FunctionalInterface
    interface Function<T, R> {
        R apply(T t);
    }
}