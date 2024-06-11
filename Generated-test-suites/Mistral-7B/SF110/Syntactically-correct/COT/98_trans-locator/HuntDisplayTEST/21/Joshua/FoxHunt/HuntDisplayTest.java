package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

public class HuntDisplayTest
{
    @org.junit.Before
    public void setUp()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
    }

    // Test for constructor
    @org.junit.Test
    public void testHuntDisplayConstructor()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
    }

    // Test for getPreferredSize method
    @org.junit.Test
    public void testGetPreferredSize()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension preferredSize = huntDisplay.getPreferredSize();
        assertEquals(200, preferredSize.getWidth());
        assertEquals(200, preferredSize.getHeight());
    }

    // Test for getMinimumSize method
    @org.junit.Test
    public void testGetMinimumSize()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension minimumSize = huntDisplay.getMinimumSize();
        assertEquals(50, minimumSize.getWidth());
        assertEquals(50, minimumSize.getHeight());
    }

    // Test for addPoint method with valid arguments
    @org.junit.Test
    public void testAddPointValidArguments()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
        huntDisplay.addPoint(trigPoint);
        assertEquals(1, huntDisplay.points.size());
    }

    // Test for addPoint method with null argument
    @org.junit.Test(expected = NullPointerException.class)
    public void testAddPointNullArgument()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addPoint(null);
    }

    // Test for addLandMark method with valid arguments
    @org.junit.Test
    public void testAddLandMarkValidArguments()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
    }

    // Test for addLandMark method with null argument
    @org.junit.Test(expected = NullPointerException.class)
    public void testAddLandMarkNullArgument()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addLandMark(null);
    }

    // Test for paintComponent method with empty points vector
    @org.junit.Test
    public void testPaintComponentEmptyPoints()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        Graphics2D graphics = mock(Graphics2D.class);
        huntDisplay.paintComponent(graphics);
        verifyNoMoreInteractions(graphics);
    }

    // Test for paintComponent method with non-empty points vector
    @org.junit.Test
    public void testPaintComponentNonEmptyPoints()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint[] trigpoints = {new TrigPoint(1.0, 2.0, 0.0, false), new TrigPoint(3.0, 4.0, 0.0, false), new TrigPoint(5.0, 6.0, 0.0, false)};
        huntDisplay.points = new Vector(Arrays.asList(trigpoints));
        Graphics2D graphics = mock(Graphics2D.class);
        huntDisplay.paintComponent(graphics);
        verify(graphics).setColor(eq(Color.BLACK));
        verify(graphics).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
        verify(graphics).setColor(eq(Color.GREEN));
        verify(graphics).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
        verify(graphics).setColor(eq(huntDisplay.getForeground()));
        verifyNoMoreInteractions(graphics);
    }

    // Test for paintComponent method with empty and non-empty landmarks vectors
    @org.junit.Test
    public void testPaintComponentEmptyAndNonEmptyLandmarks()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint[] trigpoints = {new TrigPoint(1.0, 2.0, 0.0, false), new TrigPoint(3.0, 4.0, 0.0, false), new TrigPoint(5.0, 6.0, 0.0, false)};
        huntDisplay.points = new Vector(Arrays.asList(trigpoints));
        Landmark[] landmarks = {new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB))), new Landmark(3.0, 4.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)))};
        huntDisplay.landmarks = new Vector(Arrays.asList(landmarks));
        Graphics2D graphics = mock(Graphics2D.class);
        huntDisplay.paintComponent(graphics);
        verify(graphics).setColor(eq(Color.BLACK));
        verify(graphics).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
        verify(graphics).setColor(eq(Color.GREEN));
        verify(graphics).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
        verify(graphics).setColor(eq(huntDisplay.getForeground()));
        verify(graphics).drawImage(any(Image.class), anyInt(), anyInt(), any(Component.class), any(ImageObserver.class));
        verify(graphics).drawImage(any(Image.class), anyInt(), anyInt(), anyInt(), anyInt(), any(Component.class), any(ImageObserver.class));
        verifyNoMoreInteractions(graphics);
    }
}