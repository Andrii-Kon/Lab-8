import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * GraphPanel is a JPanel that draws various mathematical functions and finds their intersection points.
 */
public class GraphPanel extends JPanel {
    private double sinA = 1.0, sinB = 10.0, sinC = 0.1;
    private double cosA = 1.0, cosB = 10.0, cosC = 0.1;
    private double tanA = 1.0, tanB = 10.0, tanC = 0.1;
    private double ctanA = 1.0, ctanB = 10.0, ctanC = 0.1;
    private double parabolaA = 1.0, parabolaB = 10.0, parabolaC = 0.1;
    private double hyperbolaA = 1.0, hyperbolaB = 10.0, hyperbolaC = 0.1;
    private double expA = 1.0, expB = 10.0, expC = 0.1;
    private double logA = 1.0, logB = 10.0, logC = 0.1;
    private double radius = 5.0;
    private double scale = 2;

    public boolean drawSin = false;
    public boolean drawCos = false;
    public boolean drawTan = false;
    public boolean drawCtan = false;
    public boolean drawParabola = false;
    public boolean drawHyperbola = false;
    public boolean drawEllipse = false;
    public boolean drawCircle = false;
    public boolean drawExp = false;
    public boolean drawLog = false;

    private List<IntersectionPoint> intersections = new ArrayList<>();
    private IntersectionPoint hoveredPoint;

    public Map<String, JPanel> functionPanels = new HashMap<>();

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

    /**
     * Inner class representing an intersection point of two functions.
     */
    class IntersectionPoint extends Point {
        double xValue, yValue;

        IntersectionPoint(int x, int y, double xValue, double yValue) {
            super(x, y);
            this.xValue = xValue;
            this.yValue = yValue;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
        g2.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());

        GraphUtils.drawTicks(g2, getWidth(), getHeight(), scale);

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
            GraphUtils.drawFunction(g2, Color.RED, x -> sinA + sinB * Math.sin(sinC * x), sinPoints, getWidth(), getHeight(), scale);
            functionPoints.add(sinPoints);
            functions.add(x -> sinA + sinB * Math.sin(sinC * x));
        }
        if (drawCos) {
            GraphUtils.drawFunction(g2, Color.BLUE, x -> cosA + cosB * Math.cos(cosC * x), cosPoints, getWidth(), getHeight(), scale);
            functionPoints.add(cosPoints);
            functions.add(x -> cosA + cosB * Math.cos(cosC * x));
        }
        if (drawTan) {
            GraphUtils.drawFunction(g2, Color.GREEN, x -> tanA + tanB * Math.tan(tanC * x), tanPoints, getWidth(), getHeight(), scale);
            functionPoints.add(tanPoints);
            functions.add(x -> tanA + tanB * Math.tan(tanC * x));
        }
        if (drawCtan) {
            GraphUtils.drawFunction(g2, Color.ORANGE, x -> ctanA + ctanB / Math.tan(ctanC * x), ctanPoints, getWidth(), getHeight(), scale);
            functionPoints.add(ctanPoints);
            functions.add(x -> ctanA + ctanB / Math.tan(ctanC * x));
        }
        if (drawParabola) {
            GraphUtils.drawFunction(g2, Color.MAGENTA, x -> parabolaA * x * x + parabolaB * x + parabolaC, parabolaPoints, getWidth(), getHeight(), scale);
            functionPoints.add(parabolaPoints);
            functions.add(x -> parabolaA * x * x + parabolaB * x + parabolaC);
        }
        if (drawHyperbola) {
            GraphUtils.drawFunction(g2, Color.CYAN, x -> hyperbolaA / (hyperbolaB * x), hyperbolaPoints, getWidth(), getHeight(), scale);
            functionPoints.add(hyperbolaPoints);
            functions.add(x -> hyperbolaA / (hyperbolaB * x));
        }
        if (drawEllipse) {
            GraphUtils.drawEllipse(g2, ellipsePoints, getWidth(), getHeight(), sinA, sinB, scale);
        }
        if (drawCircle) {
            GraphUtils.drawCircle(g2, circlePoints, getWidth(), getHeight(), sinA, sinB, radius, scale);
        }
        if (drawExp) {
            GraphUtils.drawFunction(g2, Color.YELLOW, x -> expA * Math.exp(expB * x), expPoints, getWidth(), getHeight(), scale);
            functionPoints.add(expPoints);
            functions.add(x -> expA * Math.exp(expB * x));
        }
        if (drawLog) {
            GraphUtils.drawFunction(g2, new Color(100, 50, 200), x -> logA * Math.log(logB * x), logPoints, getWidth(), getHeight(), scale);
            functionPoints.add(logPoints);
            functions.add(x -> logA * Math.log(logB * x));
        }

        GraphUtils.findAllIntersections(intersections, functionPoints, functions, getWidth(), getHeight(), scale);

        if (hoveredPoint != null) {
            g2.setColor(Color.GRAY);
            g2.fillOval(hoveredPoint.x - 5, hoveredPoint.y - 5, 10, 10);
            String text = String.format("X: %.2f, Y: %.2f", hoveredPoint.xValue, hoveredPoint.yValue);
            g2.drawString(text, hoveredPoint.x + 10, hoveredPoint.y - 10);
        }
    }

    public void updateSliders(String function, JSlider sliderA, JSlider sliderB, JSlider sliderC) {
        switch (function) {
            case "sin":
                sliderA.setValue((int) sinA);
                sliderB.setValue((int) sinB);
                sliderC.setValue((int) sinC);
                break;
            case "cos":
                sliderA.setValue((int) cosA);
                sliderB.setValue((int) cosB);
                sliderC.setValue((int) cosC);
                break;
            case "tan":
                sliderA.setValue((int) tanA);
                sliderB.setValue((int) tanB);
                sliderC.setValue((int) tanC);
                break;
            case "ctan":
                sliderA.setValue((int) ctanA);
                sliderB.setValue((int) ctanB);
                sliderC.setValue((int) ctanC);
                break;
            case "parabola":
                sliderA.setValue((int) parabolaA);
                sliderB.setValue((int) parabolaB);
                sliderC.setValue((int) parabolaC);
                break;
            case "hyperbola":
                sliderA.setValue((int) hyperbolaA);
                sliderB.setValue((int) hyperbolaB);
                sliderC.setValue((int) hyperbolaC);
                break;
            case "exp":
                sliderA.setValue((int) expA);
                sliderB.setValue((int) expB);
                sliderC.setValue((int) expC);
                break;
            case "log":
                sliderA.setValue((int) logA);
                sliderB.setValue((int) logB);
                sliderC.setValue((int) logC);
                break;
            case "circle":
                sliderA.setVisible(false);
                sliderB.setVisible(false);
                sliderC.setVisible(false);
                break;
        }
    }

    public void setA(String function, double a) {
        switch (function) {
            case "sin":
                this.sinA = a;
                break;
            case "cos":
                this.cosA = a;
                break;
            case "tan":
                this.tanA = a;
                break;
            case "ctan":
                this.ctanA = a;
                break;
            case "parabola":
                this.parabolaA = a;
                break;
            case "hyperbola":
                this.hyperbolaA = a;
                break;
            case "exp":
                this.expA = a;
                break;
            case "log":
                this.logA = a;
                break;
        }
        repaint();
    }

    public void setB(String function, double b) {
        switch (function) {
            case "sin":
                this.sinB = b;
                break;
            case "cos":
                this.cosB = b;
                break;
            case "tan":
                this.tanB = b;
                break;
            case "ctan":
                this.ctanB = b;
                break;
            case "parabola":
                this.parabolaB = b;
                break;
            case "hyperbola":
                this.hyperbolaB = b;
                break;
            case "exp":
                this.expB = b;
                break;
            case "log":
                this.logB = b;
                break;
        }
        repaint();
    }

    public void setC(String function, double c) {
        switch (function) {
            case "sin":
                this.sinC = c;
                break;
            case "cos":
                this.cosC = c;
                break;
            case "tan":
                this.tanC = c;
                break;
            case "ctan":
                this.ctanC = c;
                break;
            case "parabola":
                this.parabolaC = c;
                break;
            case "hyperbola":
                this.hyperbolaC = c;
                break;
            case "exp":
                this.expC = c;
                break;
            case "log":
                this.logC = c;
                break;
        }
        repaint();
    }

    public void setRadius(double radius) {
        this.radius = radius;
        repaint();
    }
}
