package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

    private HuntDisplay display;
    private JFrame frame;

    @Before
    public void setup() {
        display = new HuntDisplay();
        frame = new JFrame();
        frame.getContentPane().add(display);
        frame.pack();
        frame.setSize(new Dimension(300, 300));
        frame.setVisible(true);
    }

    @Test
    public void testHuntDisplayConstruction() {
        assertNotNull(display);
        assertEquals(new Dimension(200, 200), display.getPrefSize());
        assertEquals(new Dimension(50, 50), display.getMinSize());
        assertEquals(Color.WHITE, display.getBackground());
    }

    @Test
    public void testAddPoint() {
        display.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
        assertEquals(1, display.points.size());
        display.repaint();
        Graphics g = display.getGraphics();
        assertNotNull(g);
        g.dispose();
        display.addPoint(new TrigPoint(1.1, 2.1, Math.PI / 2.0, true));
        assertEquals(2, display.points.size());
        display.repaint();
        g = display.getGraphics();
        assertNotNull(g);
        g.dispose();
    }

    @Test
    public void testAddLandmark() {
        BufferedImage icon = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        icon.setRGB(0, 0, 10, 10, 0xFF0000, 0);
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(icon));
        assertNotNull(landmark);
        display.addLandmark(landmark);
        assertEquals(0, display.landmarks.size());
        display.repaint();
        Graphics g = display.getGraphics();
        assertNull(g);
        display.addLandmark(new Landmark(1.1, 2.1, new ImageIcon(icon)));
        assertEquals(1, display.landmarks.size());
        display.repaint();
        g = display.getGraphics();
        assertNotNull(g);
        g.dispose();
    }

    @Test
    public void testPaintComponent() {
        display.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
        display.addPoint(new TrigPoint(3.0, 4.0, 0.0, true));
        display.addLandmark(new Landmark(2.0, 3.0, new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB))));

        Graphics g = display.getGraphics();
        assertNotNull(g);

        for (int x = 0; x < display.getWidth(); x++) {
            for (int y = 0; y < display.getHeight(); y++) {
                if (g.getColor().equals(Color.GREEN)) {
                    assertEquals(3.0, (display.points.get(1).getX() + 0.5 - x / (double) display.getWidth()), 0.01);
                    assertEquals(4.0, (display.points.get(1).getY() + 0.5 - y / (double) display.getHeight()), 0.01);
                } else if (g.getColor().equals(Color.BLACK)) {
                    TrigPoint trigPoint = (TrigPoint) display.points.get(0);
                    assertEquals(trigPoint.getX(), (x / (double) display.getWidth()) + 0.5, 0.01);
                    assertEquals(trigPoint.getY(), (y / (double) display.getHeight()) + 0.5, 0.01);
                }
                g.dispose();
            }
        }
    }

    @Test
    public void testGetPreferredSize() {
        assertEquals(new Dimension(200, 200), display.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize() {
        assertEquals(new Dimension(50, 50), display.getMinimumSize());
    }
}