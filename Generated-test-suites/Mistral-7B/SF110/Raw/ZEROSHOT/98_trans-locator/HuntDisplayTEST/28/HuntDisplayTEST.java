package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest
{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private JFrame frame;
    private HuntDisplay display;

    @Before
    public void setUp()
    {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);

        display = new HuntDisplay();
        display.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.add(display);
        frame.setVisible(true);
    }

    @After
    public void tearDown()
    {
        frame.dispose();
    }

    @Test
    public void testHuntDisplayConstruction()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
        assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
        assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
        assertEquals(Color.WHITE, huntDisplay.getBackground());
    }

    @Test
    public void testAddPoint()
    {
        display.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
        display.addPoint(new TrigPoint(3.0, 4.0, 0.0, true));

        Graphics g = display.getGraphics();
        int pointCount = 0;

        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                if (g.getColor().equals(Color.GREEN))
                {
                    if (pointCount == 1)
                    {
                        Point foxPoint = new Point(x, y);
                        assertEquals(new Point(400 * 3.0 / 180.0, 400 * 1.0 / 90.0), foxPoint);
                    }
                    else if (pointCount == 0)
                    {
                        Point point = new Point(x, y);
                        assertEquals(new Point(400 * 1.0 / 180.0, 400 * 1.0 / 90.0), point);
                        pointCount++;
                    }
                }
                g.setColor(Color.WHITE);
            }
            g.setColor(Color.BLACK);
            g.drawLine(0, y, WIDTH, y);
        }
    }

    @Test
    public void testAddLandMark()
    {
        ImageIcon icon = new ImageIcon("landmarkIcon.png");
        display.addLandMark(new Landmark(5.0, 6.0, icon));

        Graphics g = display.getGraphics();

        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                if (g.getColor().equals(Color.WHITE) && g.getImage() != null)
                {
                    Image image = g.getImage();
                    if (image.getWidth(null) > 0 && image.getHeight(null) > 0)
                    {
                        Point landMarkPoint = new Point(x, y);
                        Point expectedLandMarkPoint = new Point(400 * 6.0 / 180.0, 400 * 5.0 / 90.0);
                        assertEquals(expectedLandMarkPoint, landMarkPoint);
                    }
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullPoint()
    {
        display.addPoint(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullLandMark()
    {
        display.addLandMark(null);
    }
}