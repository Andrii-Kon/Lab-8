import javax.swing.*;
import java.awt.*;

/**
 * Main class to set up the JFrame and add components.
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GraphPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        GraphPanel graphPanel = new GraphPanel();
        frame.add(graphPanel, BorderLayout.CENTER);

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridLayout(10, 1));

        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));

        JCheckBox checkBoxSin = new JCheckBox("sin", false);
        JCheckBox checkBoxCos = new JCheckBox("cos", false);
        JCheckBox checkBoxTan = new JCheckBox("tan", false);
        JCheckBox checkBoxCtan = new JCheckBox("ctan", false);
        JCheckBox checkBoxParabola = new JCheckBox("Parabola", false);
        JCheckBox checkBoxHyperbola = new JCheckBox("Hyperbola", false);
        JCheckBox checkBoxEllipse = new JCheckBox("Ellipse", false);
        JCheckBox checkBoxCircle = new JCheckBox("Circle", false);
        JCheckBox checkBoxExp = new JCheckBox("Exponential", false);
        JCheckBox checkBoxLog = new JCheckBox("Logarithmic", false);

        checkBoxPanel.add(checkBoxSin);
        checkBoxPanel.add(checkBoxCos);
        checkBoxPanel.add(checkBoxTan);
        checkBoxPanel.add(checkBoxCtan);
        checkBoxPanel.add(checkBoxParabola);
        checkBoxPanel.add(checkBoxHyperbola);
        checkBoxPanel.add(checkBoxEllipse);
        checkBoxPanel.add(checkBoxCircle);
        checkBoxPanel.add(checkBoxExp);
        checkBoxPanel.add(checkBoxLog);

        sliderPanel.add(SliderPanel.createSliderPanel("sin", graphPanel));
        sliderPanel.add(SliderPanel.createSliderPanel("cos", graphPanel));
        sliderPanel.add(SliderPanel.createSliderPanel("tan", graphPanel));
        sliderPanel.add(SliderPanel.createSliderPanel("ctan", graphPanel));
        sliderPanel.add(SliderPanel.createSliderPanel("parabola", graphPanel));
        sliderPanel.add(SliderPanel.createSliderPanel("hyperbola", graphPanel));
        sliderPanel.add(SliderPanel.createSliderPanel("exp", graphPanel));
        sliderPanel.add(SliderPanel.createSliderPanel("log", graphPanel));
        sliderPanel.add(SliderPanel.createSliderPanel("ellipse", graphPanel)); // Add slider for ellipse
        sliderPanel.add(SliderPanel.createSliderPanel("circle", graphPanel));  // Add slider for circle

        checkBoxSin.addActionListener(e -> {
            graphPanel.drawSin = checkBoxSin.isSelected();
            graphPanel.functionPanels.get("sin").setVisible(checkBoxSin.isSelected());
            graphPanel.repaint();
        });
        checkBoxCos.addActionListener(e -> {
            graphPanel.drawCos = checkBoxCos.isSelected();
            graphPanel.functionPanels.get("cos").setVisible(checkBoxCos.isSelected());
            graphPanel.repaint();
        });
        checkBoxTan.addActionListener(e -> {
            graphPanel.drawTan = checkBoxTan.isSelected();
            graphPanel.functionPanels.get("tan").setVisible(checkBoxTan.isSelected());
            graphPanel.repaint();
        });
        checkBoxCtan.addActionListener(e -> {
            graphPanel.drawCtan = checkBoxCtan.isSelected();
            graphPanel.functionPanels.get("ctan").setVisible(checkBoxCtan.isSelected());
            graphPanel.repaint();
        });
        checkBoxParabola.addActionListener(e -> {
            graphPanel.drawParabola = checkBoxParabola.isSelected();
            graphPanel.functionPanels.get("parabola").setVisible(checkBoxParabola.isSelected());
            graphPanel.repaint();
        });
        checkBoxHyperbola.addActionListener(e -> {
            graphPanel.drawHyperbola = checkBoxHyperbola.isSelected();
            graphPanel.functionPanels.get("hyperbola").setVisible(checkBoxHyperbola.isSelected());
            graphPanel.repaint();
        });
        checkBoxEllipse.addActionListener(e -> {
            graphPanel.drawEllipse = checkBoxEllipse.isSelected();
            graphPanel.functionPanels.get("ellipse").setVisible(checkBoxEllipse.isSelected());
            graphPanel.repaint();
        });
        checkBoxCircle.addActionListener(e -> {
            graphPanel.drawCircle = checkBoxCircle.isSelected();
            graphPanel.functionPanels.get("circle").setVisible(checkBoxCircle.isSelected());
            graphPanel.repaint();
        });
        checkBoxExp.addActionListener(e -> {
            graphPanel.drawExp = checkBoxExp.isSelected();
            graphPanel.functionPanels.get("exp").setVisible(checkBoxExp.isSelected());
            graphPanel.repaint();
        });
        checkBoxLog.addActionListener(e -> {
            graphPanel.drawLog = checkBoxLog.isSelected();
            graphPanel.functionPanels.get("log").setVisible(checkBoxLog.isSelected());
            graphPanel.repaint();
        });

        frame.add(checkBoxPanel, BorderLayout.WEST);
        frame.add(sliderPanel, BorderLayout.EAST);
        frame.setVisible(true);
    }
}
