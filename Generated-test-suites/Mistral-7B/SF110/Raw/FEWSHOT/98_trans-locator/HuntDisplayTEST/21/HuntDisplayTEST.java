package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstruction() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
        assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
        assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
        assertTrue(huntDisplay.getBackground().equals(Color.WHITE));
    }

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
        huntDisplay.addPoint(trigPoint);
        assertEquals(1, huntDisplay.points.size());
        huntDisplay.repaint();
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/landmarkIcon.png")));
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
        huntDisplay.repaint();
    }

    @Test
    public void testPaintComponentWithoutPoints() {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.paintComponent(mockGraphics());
    }

    @Test
    public void testPaintComponentWithPoints() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
        TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
        huntDisplay.addPoint(trigPoint1);
        huntDisplay.addPoint(trigPoint2);
        huntDisplay.paintComponent(mockGraphics());
    }

    @Test
    public void testPaintComponentWithLandMarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/landmarkIcon.png")));
        Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(getClass().getResource("/landmarkIcon.png")));
        huntDisplay.addLandMark(landmark1);
        huntDisplay.addLandMark(landmark2);
        huntDisplay.paintComponent(mockGraphics());
    }

    @Test
    public void testPaintComponentWithPointsAndLandMarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
        TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
        Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/landmarkIcon.png")));
        Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(getClass().getResource("/landmarkIcon.png")));
        huntDisplay.addPoint(trigPoint1);
        huntDisplay.addPoint(trigPoint2);
        huntDisplay.addLandMark(landmark1);
        huntDisplay.addLandMark(landmark2);
        huntDisplay.paintComponent(mockGraphics());
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
    }

    private Graphics mockGraphics() {
        return new Graphics() {};
    }
}