package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplay() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
        assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
        assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0 / 90.0, 2.0 / 180.0, 0.0, false);
        huntDisplay.addPoint(trigPoint);
        assertEquals(1, huntDisplay.points.size());
        huntDisplay.repaint();
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(3.0 / 90.0, 4.0 / 180.0, new ImageIcon(getClass().getResource("/icon.png")));
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
        huntDisplay.repaint();
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0 / 90.0, 2.0 / 180.0, 0.0, false);
        Landmark landmark = new Landmark(3.0 / 90.0, 4.0 / 180.0, new ImageIcon(getClass().getResource("/icon.png")));
        huntDisplay.addPoint(trigPoint);
        huntDisplay.addLandMark(landmark);

        Graphics2D graphics = (Graphics2D) huntDisplay.createGraphics();
        huntDisplay.paintComponent(graphics);

        assertEquals(Color.BLACK, graphics.getColor());
        huntDisplay.addPoint(new TrigPoint(5.0 / 90.0, 6.0 / 180.0, 0.0, true));
        huntDisplay.repaint();

        graphics = (Graphics2D) huntDisplay.createGraphics();
        huntDisplay.paintComponent(graphics);

        assertEquals(Color.GREEN, graphics.getColor());

        graphics.setColor(Color.BLACK);
        huntDisplay.removeAll();
        huntDisplay.repaint();

        graphics = (Graphics2D) huntDisplay.createGraphics();
        huntDisplay.paintComponent(graphics);

        assertEquals(0, huntDisplay.points.size());
        assertEquals(0, huntDisplay.landmarks.size());
    }
}