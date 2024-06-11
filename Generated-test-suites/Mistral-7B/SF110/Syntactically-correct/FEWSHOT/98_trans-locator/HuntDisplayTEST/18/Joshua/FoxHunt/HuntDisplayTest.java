package Joshua.FoxHunt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.Icon;

import org.junit.Test;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayCreation() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
        assertEquals(Color.WHITE, huntDisplay.getBackground());
        assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
        assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = mock(TrigPoint.class);
        huntDisplay.addPoint(trigPoint);
        huntDisplay.repaint();

        TrigPoint[] trigpoints = new TrigPoint[1];
        trigpoints[0] = trigPoint;
        Graphics graphics = mock(Graphics.class);
        double xAdd = 0.5;
        double yAdd = 0.5;

        when(huntDisplay.getWidth()).thenReturn(10.0);
        when(huntDisplay.getHeight()).thenReturn(10.0);

        huntDisplay.paintComponent(graphics);

        verify(graphics).fillOval((int) (trigPoint.getX() * 10.0 + xAdd) * 10, (int) (trigPoint.getY() * 10.0 + yAdd) * 10, 5, 5);
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = mock(Landmark.class);
        Image icon = mock(BufferedImage.class);
        huntDisplay.addLandmark(landmark);
        huntDisplay.repaint();

        Landmark[] landMarks = new Landmark[1];
        landMarks[0] = landmark;
        Graphics graphics = mock(Graphics.class);

        when(huntDisplay.getWidth()).thenReturn(10.0);
        when(huntDisplay.getHeight()).thenReturn(10.0);

        huntDisplay.paintComponent(graphics);

        verify(graphics).drawImage(icon, (int) (landmark.getLon() * 10.0), (int) (landmark.getLat() * 10.0), 10, 10, eq(huntDisplay.getBackground()), eq(huntDisplay));
    }
}