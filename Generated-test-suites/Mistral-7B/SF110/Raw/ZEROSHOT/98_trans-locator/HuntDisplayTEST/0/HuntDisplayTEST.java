package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Vector;

import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest
{
    @Test
    public void testHuntDisplayConstruction()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(50, huntDisplay.getMinimumSize().width);
        assertEquals(50, huntDisplay.getMinimumSize().height);
        assertEquals(200, huntDisplay.getPreferredSize().width);
        assertEquals(200, huntDisplay.getPreferredSize().height);
    }

    @Test
    public void testAddPoint()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
        huntDisplay.addPoint(point);
        assertEquals(1, huntDisplay.points.size());
        huntDisplay.repaint();
        Graphics g = huntDisplay.getGraphics();
        int x = (int)((point.getX() + 0.5) * huntDisplay.getWidth());
        int y = (int)((point.getY() + 0.5) * huntDisplay.getHeight());
        assertEquals(Color.BLACK, g.getColor());
        g.fillOval(x, y, 5, 5);
    }

    @Test
    public void testAddLandMark()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(3.0, 4.0, Toolkit.getDefaultToolkit().getImage("path/to/icon.png"));
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
        huntDisplay.repaint();
        Graphics g = huntDisplay.getGraphics();
        int x = (int)((landmark.getLon() + 0.5) * huntDisplay.getWidth());
        int y = (int)((landmark.getLat() + 0.5) * huntDisplay.getHeight());
        Image image = landmark.getIcon().getImage();
        g.drawImage(image, x, y, 10, 10, huntDisplay.getBackground(), huntDisplay);
    }

    @Test
    public void testAddPointAndLandMark()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
        huntDisplay.addPoint(point);
        Landmark landmark = new Landmark(3.0, 4.0, Toolkit.getDefaultToolkit().getImage("path/to/icon.png"));
        huntDisplay.addLandMark(landmark);
        assertEquals(2, huntDisplay.points.size() + huntDisplay.landmarks.size());
        huntDisplay.repaint();
        Graphics g = huntDisplay.getGraphics();
        int xPoint = (int)((point.getX() + 0.5) * huntDisplay.getWidth());
        int yPoint = (int)((point.getY() + 0.5) * huntDisplay.getHeight());
        int xLandmark = (int)((landmark.getLon() + 0.5) * huntDisplay.getWidth());
        int yLandmark = (int)((landmark.getLat() + 0.5) * huntDisplay.getHeight());
        assertEquals(Color.BLACK, g.getColor());
        g.fillOval(xPoint, yPoint, 5, 5);
        g.setColor(Color.GREEN);
        Image image = landmark.getIcon().getImage();
        g.drawImage(image, xLandmark, yLandmark, 10, 10, huntDisplay.getBackground(), huntDisplay);
    }

    @Test
    public void testPaintComponent()
    {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
        TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
        Landmark landmark = new Landmark(5.0, 6.0, Toolkit.getDefaultToolkit().getImage("path/to/icon.png"));
        huntDisplay.addPoint(point1);
        huntDisplay.addPoint(point2);
        huntDisplay.addLandMark(landmark);
        huntDisplay.repaint();
        Graphics g = huntDisplay.getGraphics();
        int xPoint1 = (int)((point1.getX() + 0.5) * huntDisplay.getWidth());
        int yPoint1 = (int)((point1.getY() + 0.5) * huntDisplay.getHeight());
        int xPoint2 = (int)((point2.getX() + 0.5) * huntDisplay.getWidth());
        int yPoint2 = (int)((point2.getY() + 0.5) * huntDisplay.getHeight());
        int xLandmark = (int)((landmark.getLon() + 0.5) * huntDisplay.getWidth());
        int yLandmark = (int)((landmark.getLat() + 0.5) * huntDisplay.getHeight());
        assertEquals(Color.BLACK, g.getColor());
        g.fillOval(xPoint1, yPoint1, 5, 5);
        g.fillOval(xPoint2, yPoint2, 5, 5);
        g.setColor(Color.GREEN);
        Image image = landmark.getIcon().getImage();
        g.drawImage(image, xLandmark, yLandmark, 10, 10, huntDisplay.getBackground(), huntDisplay);
    }
}