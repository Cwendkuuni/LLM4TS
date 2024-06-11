package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstructor() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension expectedSize = new Dimension(200, 200);
        Dimension actualSize = huntDisplay.getPreferredSize();
        assertEquals(expectedSize.getWidth(), actualSize.getWidth());
        assertEquals(expectedSize.getHeight(), actualSize.getHeight());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension expectedSize = new Dimension(50, 50);
        Dimension actualSize = huntDisplay.getMinimumSize();
        assertEquals(expectedSize.getWidth(), actualSize.getWidth());
        assertEquals(expectedSize.getHeight(), actualSize.getHeight());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = Mockito.mock(TrigPoint.class);
        huntDisplay.addPoint(trigPoint);
        Mockito.verify(huntDisplay).repaint();
        Mockito.verify(huntDisplay, times(1)).points.add(trigPoint);
    }

    @Test(expected = NullPointerException.class)
    public void testAddPointWithNullTrigPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addPoint(null);
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = Mockito.mock(Landmark.class);
        BufferedImage image = Mockito.mock(BufferedImage.class);
        Mockito.when(landmark.getIcon()).thenReturn(new ImageIcon(image));
        huntDisplay.addLandmark(landmark);
        Mockito.verify(huntDisplay).repaint();
        Mockito.verify(huntDisplay, times(1)).landmarks.add(landmark);
    }

    @Test(expected = NullPointerException.class)
    public void testAddLandmarkWithNullLandmark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addLandmark(null);
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Graphics2D graphics = Mockito.mock(Graphics2D.class);
        huntDisplay.paintComponent(graphics);
        Mockito.verify(huntDisplay, times(1)).super.paintComponent(graphics);
    }

    @Test
    public void testCalculateAverageCoordinates() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint[] trigPoints = {new TrigPoint(1.0, 1.0), new TrigPoint(2.0, 2.0), new TrigPoint(3.0, 3.0)};
        double expectedAvgX = 2.0;
        double expectedAvgY = 2.0;
        huntDisplay.points = new Vector<>(Arrays.asList(trigPoints));
        double actualAvgX = huntDisplay.calculateAverageCoordinates()[0];
        double actualAvgY = huntDisplay.calculateAverageCoordinates()[1];
        assertEquals(expectedAvgX, actualAvgX, 0.01);
        assertEquals(expectedAvgY, actualAvgY, 0.01);
    }
}