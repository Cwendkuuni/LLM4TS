package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import org.junit.*;
import org.junit. Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

@RunWith(JUnit4.class)
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
        assertEquals(huntDisplay.prefSize, huntDisplay.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(huntDisplay.minSize, huntDisplay.getMinimumSize());
    }

    @Test
    public void testAddPoint()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
        huntDisplay.addPoint(trigPoint);
        assertTrue(huntDisplay.points.size() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testAddPointWithNullTrigPoint()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addPoint(null);
    }

    @Test
    public void testAddLandmark()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
        huntDisplay.addLandmark(landmark);
        assertTrue(huntDisplay.landmarks.size() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testAddLandmarkWithNullLandmark()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addLandmark(null);
    }

    @Test
    public void testPaintComponentWithEmptyPointsVector()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        Graphics2D g = mock(Graphics2D.class);
        huntDisplay.paintComponent(g);
        verifyNoMoreInteractions(g);
    }

    @Test
    public void testPaintComponentWithNonEmptyPointsVector()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
        TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
        huntDisplay.points.add(trigPoint1);
        huntDisplay.points.add(trigPoint2);
        Graphics2D g = mock(Graphics2D.class);
        huntDisplay.paintComponent(g);
        verify(g, times(trigPoint1.isFox() ? 2 : 4)).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
        verify(g).setColor(same(huntDisplay.getForeground()));
        if (huntDisplay.landmarks.size() > 0) {
            for (Object landmark : huntDisplay.landmarks) {
                Landmark landmarkObj = (Landmark) landmark;
                verify(g, times(1)).drawImage(eq(landmarkObj.getIcon().getImage()), anyInt(), anyInt(), anyInt(), anyInt(), eq(huntDisplay.getBackground()), eq(huntDisplay));
            }
        }
    }

    @Test
    public void testPaintComponentWithEmptyLandmarksVector()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
        huntDisplay.points.add(trigPoint);
        Graphics2D g = mock(Graphics2D.class);
        huntDisplay.paintComponent(g);
        verifyNoMoreInteractions(g);
    }
}