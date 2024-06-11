package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import org.junit.*;
import Joshua.FoxHunt.*;

public class HuntDisplayTest
{
    @Test
    public void testHuntDisplayConstructor()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
    }

    @Test
    public void testGetPreferredSize()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension size = huntDisplay.getPreferredSize();
        assertEquals(200, size.getWidth());
        assertEquals(200, size.getHeight());
    }

    @Test
    public void testGetMinimumSize()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension size = huntDisplay.getMinimumSize();
        assertEquals(50, size.getWidth());
        assertEquals(50, size.getHeight());
    }

    @Test
    public void testAddPoint()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, true);
        huntDisplay.addPoint(trigPoint);
        assertEquals(1, huntDisplay.points.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPointWithNullTrigPoint()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addPoint(null);
    }

    @Test
    public void testAddLandMark()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLandMarkWithNullLandmark()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addLandMark(null);
    }

    @Test
    public void testPaintComponentWithNoPointsOrLandmarks()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        Graphics g = mock(Graphics.class);
        huntDisplay.paintComponent(g);
        // empty test, no expected errors
    }

    @Test
    public void testPaintComponentWithPoints()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, true);
        TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
        huntDisplay.points.add(trigPoint1);
        huntDisplay.points.add(trigPoint2);
        huntDisplay.repaint();

        Graphics g = mock(Graphics.class);
        huntDisplay.paintComponent(g);

        int x1 = (int)(1.0 * huntDisplay.getWidth());
        int y1 = (int)(2.0 * huntDisplay.getHeight());
        int x2 = (int)(3.0 * huntDisplay.getWidth());
        int y2 = (int)(4.0 * huntDisplay.getHeight());

        verify(g).fillOval(x1, y1, 5, 5);
        verify(g).fillOval(x2, y2, 5, 5);
    }

    @Test
    public void testPaintComponentWithLandmarks()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon("icon1.png"));
        Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon("icon2.png"));
        huntDisplay.landmarks.add(landmark1);
        huntDisplay.landmarks.add(landmark2);
        huntDisplay.repaint();

        Graphics g = mock(Graphics.class);
        huntDisplay.paintComponent(g);

        int x1 = (int)(1.0 * huntDisplay.getWidth());
        int y1 = (int)(2.0 * huntDisplay.getHeight());
        int x2 = (int)(3.0 * huntDisplay.getWidth());
        int y2 = (int)(4.0 * huntDisplay.getHeight());

        verify(g).drawImage(eq(new ImageIcon("icon1.png")), x1, y1, 10, 10, eq(huntDisplay.getBackground()), eq(huntDisplay));
        verify(g).drawImage(eq(new ImageIcon("icon2.png")), x2, y2, 10, 10, eq(huntDisplay.getBackground()), eq(huntDisplay));
    }

    @Test
    public void testPaintComponentWithBothPointsAndLandmarks()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, true);
        TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
        Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon("icon1.png"));
        Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon("icon2.png"));
        huntDisplay.points.add(trigPoint1);
        huntDisplay.points.add(trigPoint2);
        huntDisplay.landmarks.add(landmark1);
        huntDisplay.landmarks.add(landmark2);
        huntDisplay.repaint();

        Graphics g = mock(Graphics.class);
        huntDisplay.paintComponent(g);

        int x1 = (int)(1.0 * huntDisplay.getWidth());
        int y1 = (int)(2.0 * huntDisplay.getHeight());
        int x2 = (int)(3.0 * huntDisplay.getWidth());
        int y2 = (int)(4.0 * huntDisplay.getHeight());

        verify(g).fillOval(x1, y1, 5, 5);
        verify(g).fillOval(x2, y2, 5, 5);
        verify(g).drawImage(eq(new ImageIcon("icon1.png")), x1, y1, 10, 10, eq(huntDisplay.getBackground()), eq(huntDisplay));
        verify(g).drawImage(eq(new ImageIcon("icon2.png")), x2, y2, 10, 10, eq(huntDisplay.getBackground()), eq(huntDisplay));
    }
}