package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HuntDisplayTest {

    @Mock
    private BufferedImage mockImage;

    private HuntDisplay display;

    @Test
    public void testHuntDisplayInitialization() {
        display = new HuntDisplay();
        assertEquals(Color.WHITE, display.getBackground());
        assertEquals(new Dimension(50, 50), display.getMinSize());
        assertEquals(new Dimension(200, 200), display.getPrefSize());
        assertTrue(display.points.isEmpty());
        assertTrue(display.landmarks.isEmpty());
    }

    @Test
    public void testAddPoint() {
        display = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 3.0, true);
        display.addPoint(trigPoint);
        assertEquals(1, display.points.size());
        assertTrue(display.points.contains(trigPoint));
        display.repaint(); // Verification should be done in a mocking framework like Mockito
    }

    @Test
    public void testAddLandmark() {
        display = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, mockImage);
        display.addLandMark(landmark);
        assertEquals(1, display.landmarks.size());
        assertTrue(display.landmarks.contains(landmark));
        display.repaint(); // Verification should be done in a mocking framework like Mockito
    }

    @Test
    public void testPaintComponentWithNoPointsOrLandmarks() {
        display = new HuntDisplay();
        MockitoAnnotations.initMocks(this);
        Graphics2D g = mock(Graphics2D.class);
        when(display.getGraphics()).thenReturn(g);
        display.paintComponent(g);
        verifyNoMoreInteractions(g);
    }

    @Test
    public void testPaintComponentWithPoints() {
        display = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(1.0, 2.0, 3.0, false);
        TrigPoint point2 = new TrigPoint(3.0, 4.0, 5.0, false);
        display.points.add(point1);
        display.points.add(point2);
        MockitoAnnotations.initMocks(this);
        Graphics2D g = mock(Graphics2D.class);
        when(display.getGraphics()).thenReturn(g);
        display.paintComponent(g);
        for (int i = 0; i < display.points.size(); i++) {
            TrigPoint trigPoint = display.points.elementAt(i);
            if (trigPoint.isFox()) {
                verify(g).setColor(Color.GREEN);
            } else {
                verify(g).setColor(Color.BLACK);
            }
            final int X = (int) ((trigPoint.getX() + 0.5) * display.getWidth());
            final int Y = (int) ((trigPoint.getY() + 0.5) * display.getHeight());
            verify(g).fillOval(X, Y, 5, 5);
        }
    }

    @Test
    public void testPaintComponentWithLandmarks() {
        display = new HuntDisplay();
        Landmark landmark1 = new Landmark(1.0, 2.0, mockImage);
        Landmark landmark2 = new Landmark(3.0, 4.0, mockImage);
        display.landmarks.add(landmark1);
        display.landmarks.add(landmark2);
        MockitoAnnotations.initMocks(this);
        Graphics2D g = mock(Graphics2D.class);
        when(display.getGraphics()).thenReturn(g);
        display.paintComponent(g);
        for (int i = 0; i < display.landmarks.size(); i++) {
            Landmark landmark = display.landmarks.elementAt(i);
            final int X = (int) ((landmark.getLon() + 0.5) * display.getWidth());
            final int Y = (int) ((landmark.getLat() + 0.5) * display.getHeight());
            verify(g).drawImage(landmark.getIcon().getImage(), X, Y, 10, 10, eq(display.getBackground()), eq(display));
        }
    }
}