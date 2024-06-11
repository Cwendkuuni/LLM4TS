package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;
 private Vector<TrigPoint> points;
 private Vector<Landmark> landmarks;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 points = new Vector<TrigPoint>();
 landmarks = new Vector<Landmark>();
 }

 @Test
 public void testConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertNotNull(hd);
 }

 @Test
 public void testGetPreferredSize() {
 assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(point);
 points.add(point);
 assertEquals(1, huntDisplay.getPoints().size());
 assertEquals(1, points.size());
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(3.0, 4.0, new ImageIcon(BufferedImage.TYPE_INT_RGB, 10, 10, Color.RED.getRGB()));
 huntDisplay.addLandmark(landmark);
 landmarks.add(landmark);
 assertEquals(0, huntDisplay.getLandmarks().size());
 assertEquals(1, landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkNullIcon() {
 Landmark landmark = new Landmark(3.0, 4.0, null);
 huntDisplay.addLandmark(landmark);
 fail("Expected IllegalArgumentException was not thrown.");
 }

 @Test
 public void testPaintComponent() {
 Graphics g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 assertNotNull(g);
}

 @Test
 public void testRepaintAfterAddingPoints() {
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 huntDisplay.addPoint(point1);
 huntDisplay.addPoint(point2);
 huntDisplay.paintComponent(huntDisplay.getGraphics());
 assertTrue(huntDisplay.isOpaque());
}

 @Test
 public void testRepaintAfterAddingLandmarks() {
 Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(BufferedImage.TYPE_INT_RGB, 10, 10, Color.BLUE.getRGB()));
 Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(BufferedImage.TYPE_INT_RGB, 10, 10, Color.GREEN.getRGB()));
 huntDisplay.addLandmark(landmark1);
 huntDisplay.addLandmark(landmark2);
 huntDisplay.paintComponent(huntDisplay.getGraphics());
 assertTrue(huntDisplay.isOpaque());
}

 @Test
 public void testDrawPoints() {
 Graphics g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 huntDisplay.addPoint(point1);
 huntDisplay.addPoint(point2);
 huntDisplay.paintComponent(g);
 int x1 = (int) (point1.getX() * huntDisplay.getWidth());
 int y1 = (int) (point1.getY() * huntDisplay.getHeight());
 int x2 = (int) (point2.getX() * huntDisplay.getWidth());
 int y2 = (int) (point2.getY() * huntDisplay.getHeight());
 assertTrue(x1 >= 0 && x1 < huntDisplay.getWidth() && y1 >= 0 && y1 < huntDisplay.getHeight());
 assertTrue(x2 >= 0 && x2 < huntDisplay.getWidth() && y2 >= 0 && y2 < huntDisplay.getHeight());
}

 @Test
 public void testDrawLandmarks() {
 Graphics g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(BufferedImage.TYPE_INT_RGB, 10, 10, Color.BLUE.getRGB()));
 Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(BufferedImage.TYPE_INT_RGB, 10, 10, Color.GREEN.getRGB()));
 huntDisplay.addLandmark(landmark1);
 huntDisplay.addLandmark(landmark2);
 huntDisplay.paintComponent(g);
 int x1 = (int) (landmark1.getLon() * huntDisplay.getWidth());
 int y1 = (int) (landmark1.getLat() * huntDisplay.getHeight());
 int x2 = (int) (landmark2.getLon() * huntDisplay.getWidth());
 int y2 = (int) (landmark2.getLat() * huntDisplay.getHeight());
 assertTrue(x1 >= 0 && x1 < huntDisplay.getWidth() && y1 >= 0 && y1 < huntDisplay.getHeight());
 assertTrue(x2 >= 0 && x2 < huntDisplay.getWidth() && y2 >= 0 && y2 < huntDisplay.getHeight());
}

 @Test
 public void testCalculateCenter() {
 Vector<TrigPoint> testPoints = new Vector<TrigPoint>();
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 TrigPoint point3 = new TrigPoint(5.0, 6.0, 0.0, false);
 testPoints.add(point1);
 testPoints.add(point2);
 testPoints.add(point3);
 double avgX = huntDisplay.calculateCenter(testPoints);
 double avgY = huntDisplay.calculateCenter(testPoints);
 double centerX = (avgX + huntDisplay.getWidth() * 0.5) / huntDisplay.getWidth();
 double centerY = (avgY + huntDisplay.getHeight() * 0.5) / huntDisplay.getHeight();
 assertEquals(centerX, 3.0, 0.01);
 assertEquals(centerY, 4.0, 0.01);
}

 @Test
 public void testCalculateCenterWithFox() {
 Vector<TrigPoint> testPoints = new Vector<TrigPoint>();
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, true);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 TrigPoint point3 = new TrigPoint(5.0, 6.0, 0.0, false);
 testPoints.add(point1);
 testPoints.add(point2);
 testPoints.add(point3);
 double avgX = huntDisplay.calculateCenter(testPoints);
 double avgY = huntDisplay.calculateCenter(testPoints);
 double centerX = (avgX + huntDisplay.getWidth() * 0.5) / huntDisplay.getWidth();
 double centerY = (avgY + huntDisplay.getHeight() * 0.5) / huntDisplay.getHeight();
 assertEquals(centerX, 3.0, 0.01);
 assertEquals(centerY, 4.0, 0.01);
}

 @Test
 public void testCalculateCenterWithSinglePoint() {
 Vector<TrigPoint> testPoints = new Vector<TrigPoint>();
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 testPoints.add(point);
 double avgX = huntDisplay.calculateCenter(testPoints);
 double avgY = huntDisplay.calculateCenter(testPoints);
 double centerX = (avgX + huntDisplay.getWidth() * 0.5) / huntDisplay.getWidth();
 double centerY = (avgY + huntDisplay.getHeight() * 0.5) / huntDisplay.getHeight();
 assertEquals(centerX, 1.0, 0.01);
 assertEquals(centerY, 2.0, 0.01);
}

 @After
 public void tearDown() {
 huntDisplay = null;
 points.clear();
 landmarks.clear();
}

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 JFrame frame = new JFrame();
 frame.add(huntDisplay);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 huntDisplay.paintComponent(huntDisplay.getGraphics());
 }
 });
 frame.dispose();
}

 @Test
 public void testPaintComponentWithPointsOnly() {
 JFrame frame = new JFrame();
 frame.add(huntDisplay);
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 huntDisplay.addPoint(point1);
 huntDisplay.addPoint(point2);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 huntDisplay.paintComponent(huntDisplay.getGraphics());
 }
 });
 frame.dispose();
}

 @Test
 public void testPaintComponentWithLandmarksOnly() {
 JFrame frame = new JFrame();
 frame.add(huntDisplay);
 Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(BufferedImage.TYPE_INT_RGB, 10, 10, Color.BLUE.getRGB()));
 Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(BufferedImage.TYPE_INT_RGB, 10, 10, Color.GREEN.getRGB()));
 huntDisplay.addLandmark(landmark1);
 huntDisplay.addLandmark(landmark2);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 huntDisplay.paintComponent(huntDisplay.getGraphics());
 }
 });
 frame.dispose();
}

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 JFrame frame = new JFrame();
 frame.add(huntDisplay);
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(BufferedImage.TYPE_INT_RGB, 10, 10, Color.BLUE.getRGB()));
 Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(BufferedImage.TYPE_INT_RGB, 10, 10, Color.GREEN.getRGB()));
 huntDisplay.addPoint(point1);
 huntDisplay.addPoint(point2);
 huntDisplay.addLandmark(landmark1);
 huntDisplay.addLandmark(landmark2);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 huntDisplay.paintComponent(huntDisplay.getGraphics());
 }
 });
 frame.dispose();
}
}