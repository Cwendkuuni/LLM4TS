package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

public class HuntDisplayTest {
    @Test
    public void testHuntDisplayInitialization() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
        assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
        assertNotNull(huntDisplay.points);
        assertNotNull(huntDisplay.landmarks);
        assertFalse(huntDisplay.isOpaque());
    }

    @Test
    public void testAddingPoints() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, true);
        TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);

        huntDisplay.addPoint(trigPoint1);
        huntDisplay.addPoint(trigPoint2);

        assertEquals(2, huntDisplay.points.size());
        huntDisplay.repaint();

        // Check if points are added correctly and painted
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(huntDisplay);
        frame.pack();
        frame.setVisible(true);

        int greenPointCount = 0;
        int blackPointCount = 0;
        for (Component component : frame.getContentPane()) {
            if (component instanceof JComponent && component.getBackground() instanceof Color && component.getBackground().equals(Color.GREEN)) {
                greenPointCount++;
            } else if (component instanceof JComponent && component.getBackground() instanceof Color && component.getBackground().equals(Color.BLACK)) {
                blackPointCount++;
            }
        }

        assertEquals(1, greenPointCount);
        assertEquals(1, blackPointCount);
    }

    @Test
    public void testAddingLandmarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/image1.png")));
        Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(getClass().getResource("/image2.png")));

        huntDisplay.addLandMark(landmark1);
        huntDisplay.addLandMark(landmark2);

        assertEquals(2, huntDisplay.landmarks.size());
        huntDisplay.repaint();

        // Check if landmarks are added correctly and painted
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(huntDisplay);
        frame.pack();
        frame.setVisible(true);

        for (Component component : frame.getContentPane()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                ImageIcon icon = label.getIcon();
                if (icon != null) {
                    Image image = icon.getImage();
                    if (image != null) {
                        assertEquals("/image1.png", image.toString().substring(image.toString().lastIndexOf('/') + 1));
                    }
                }
            }
        }
    }
}