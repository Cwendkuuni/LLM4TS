package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;
    private Vector<TrigPoint> trigPoints;
    private Vector<Landmark> landmarks;

    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
        trigPoints = new Vector<>();
        landmarks = new Vector<>();
    }

    @Test
    public void testConstructor() {
        assertNotNull(huntDisplay);
        assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
        assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
        assertEquals(Color.WHITE, huntDisplay.getBackground());
    }

    @Test
    public void testGetPreferredSize() {
        assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
    }

    @Test
    public void testGetMinimumSize() {
        assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
    }

    @Test
    public void testAddPoint() {
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
        huntDisplay.addPoint(trigPoint);
        assertEquals(1, huntDisplay.points.size());
        huntDisplay.repaint();
        verify(huntDisplay).repaint();
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
        huntDisplay.repaint();
        verify(huntDisplay).repaint();
    }

    @Test
    public void testPaintComponentWithNoPointsOrLandmarks() {
        Graphics mockGraphics = mock(Graphics.class);
        huntDisplay.paintComponent(mockGraphics);
        verifyNoMoreInteractions(mockGraphics);
    }

    @Test
    public void testPaintComponentWithPoints() {
        TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
        TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
        trigPoints.add(trigPoint1);
        trigPoints.add(trigPoint2);
        huntDisplay.points = trigPoints;

        Graphics mockGraphics = mock(Graphics.class);

        huntDisplay.paintComponent(mockGraphics);

        verify(mockGraphics).fillOval(anyInt(), anyInt(), anyInt(), anyInt(), eq(Color.BLACK));
        verify(mockGraphics).fillOval(anyInt(), anyInt(), anyInt(), anyInt(), eq(Color.GREEN));
    }

    @Test
    public void testPaintComponentWithLandmarks() {
        TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
        Landmark landmark1 = new Landmark(1.5, 3.5, new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));
        TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
        trigPoints.add(trigPoint1);
        trigPoints.add(trigPoint2);
        Landmark landmark2 = new Landmark(2.0, 2.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));
        landmarks.add(landmark1);
        landmarks.add(landmark2);
        huntDisplay.points = trigPoints;
        huntDisplay.landmarks = landmarks;

        Graphics mockGraphics = mock(Graphics.class);

        huntDisplay.paintComponent(mockGraphics);

        verify(mockGraphics).fillOval(anyInt(), anyInt(), anyInt(), anyInt(), eq(Color.BLACK));
        verify(mockGraphics).fillOval(anyInt(), anyInt(), anyInt(), anyInt(), eq(Color.GREEN));
        verify(mockGraphics).drawImage(any(BufferedImage.class), anyInt(), anyInt(), any(Graphics.class), any(ImageObserver.class), eq(this.getBackground()));
        verify(mockGraphics, times(2)).drawImage(any(BufferedImage.class), anyInt(), anyInt(), 10, 10, this.getBackground(), this);
    }
}