package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class HuntDisplayTest
{
    @Test
    public void testHuntDisplayConstruction()
    {
        HuntDisplay hd = new HuntDisplay();
        assertNotNull(hd);
        assertEquals(new Dimension(200, 200), hd.getPrefSize());
        assertEquals(new Dimension(50, 50), hd.getMinSize());
        assertNotNull(hd.points);
        assertNotNull(hd.landmarks);
    }

    @Test
    public void testAddPoint()
    {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint tp = new TrigPoint(1.0, 2.0, 0.0, true);
        hd.addPoint(tp);
        assertEquals(1, hd.points.size());
        assertEquals(tp.getX(), hd.points.elementAt(0).getX(), 0.01);
        assertEquals(tp.getY(), hd.points.elementAt(0).getY(), 0.01);
        assertEquals(tp.getTheta(), hd.points.elementAt(0).getTheta(), 0.01);
        assertEquals(tp.isFox(), hd.points.elementAt(0).isFox());
        hd.repaint();
        BufferedImage image = Mockito.mock(BufferedImage.class);
        Mockito.when(image.getWidth()).thenReturn(5);
        Mockito.when(image.getHeight()).thenReturn(5);
        Landmark landmark = new Landmark(1.0, 2.0, image);
        hd.addLandMark(landmark);
        assertEquals(2, hd.landmarks.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPointWithNullTrigPoint()
    {
        HuntDisplay hd = new HuntDisplay();
        hd.addPoint(null);
    }

    @Test
    public void testPaintComponent()
    {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint tp1 = new TrigPoint(1.0, 2.0, 0.0, true);
        TrigPoint tp2 = new TrigPoint(3.0, 4.0, 0.0, false);
        hd.addPoint(tp1);
        hd.addPoint(tp2);
        Graphics2D g = Mockito.mock(Graphics2D.class);
        hd.paintComponent(g);
        double trigPointAvgX = 2.0;
        double trigPointAvgY = 2.5;
        for (int i = 0; i < hd.points.size(); ++i)
        {
            TrigPoint trigpoint = hd.points.elementAt(i);
            assertEquals(trigpoint.isFox(), g.getColor().equals(Color.GREEN));
            if (trigpoint.isFox())
            {
                int x = (int)((trigpoint.getX() + trigPointAvgX) * hd.getWidth());
                int y = (int)((trigpoint.getY() + trigPointAvgY) * hd.getHeight());
                Mockito.verify(g, Mockito.times(1)).fillOval(x, y, 5, 5);
            }
            else
            {
                int x = (int)((trigpoint.getX() + trigPointAvgX) * hd.getWidth());
                int y = (int)((trigpoint.getY() + trigPointAvgY) * hd.getHeight());
                Mockito.verify(g, Mockito.times(1)).fillOval(x, y, 5, 5);
            }
        }
    }

    @Test
    public void testPaintComponentWithNoPoints()
    {
        HuntDisplay hd = new HuntDisplay();
        Graphics2D g = Mockito.mock(Graphics2D.class);
        hd.paintComponent(g);
        Mockito.verify(g, Mockito.never()).fillOval(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testPaintComponentWithNoLandmarks()
    {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint tp1 = new TrigPoint(1.0, 2.0, 0.0, true);
        TrigPoint tp2 = new TrigPoint(3.0, 4.0, 0.0, false);
        hd.addPoint(tp1);
        hd.addPoint(tp2);
        Graphics2D g = Mockito.mock(Graphics2D.class);
        hd.paintComponent(g);
        Mockito.verify(g, Mockito.never()).drawImage(Mockito.any(Image.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any(Component.class), Mockito.any(Graphics2D.class));
    }
}