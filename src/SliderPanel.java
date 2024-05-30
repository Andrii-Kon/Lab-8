import javax.swing.*;
import java.awt.*;

/**
 * Utility class for creating slider panels.
 */
public class SliderPanel {

    /**
     * Creates a slider panel for the given function.
     *
     * @param function the function name
     * @param graphPanel the GraphPanel object
     * @return the created slider panel
     */
    public static JPanel createSliderPanel(String function, GraphPanel graphPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(function));

        JSlider sliderA = new JSlider(-10, 10, 1);
        sliderA.addChangeListener(e -> graphPanel.setA(function, sliderA.getValue()));
        panel.add(new JLabel("A"));
        panel.add(sliderA);

        JSlider sliderB = new JSlider(-10, 10, 10);
        sliderB.addChangeListener(e -> graphPanel.setB(function, sliderB.getValue()));
        panel.add(new JLabel("B"));
        panel.add(sliderB);

        JSlider sliderC = new JSlider(-10, 10, 1);
        sliderC.addChangeListener(e -> graphPanel.setC(function, sliderC.getValue()));
        panel.add(new JLabel("C"));
        panel.add(sliderC);

        if (function.equals("circle")) {
            sliderA.setVisible(false);
            sliderB.setVisible(false);
            sliderC.setVisible(false);
            JSlider sliderRadius = new JSlider(0, 10, 5);
            sliderRadius.addChangeListener(e -> graphPanel.setRadius(sliderRadius.getValue()));
            panel.add(new JLabel("Radius"));
            panel.add(sliderRadius);
        }

        graphPanel.functionPanels.put(function, panel);
        panel.setVisible(false);
        return panel;
    }
}
