package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import javax.swing.*;

import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

public class HuntDisplayTest {

 // Test constructor
 @Test
 public void testHuntDisplayConstructor() {
 HuntDisplay huntDisplay = new HuntDisplay();
 assertNotNull(huntDisplay);
 }

 // Test getPreferredSize method
 @Test
 public void testGetPreferredSize() {
 HuntDisplay huntDisplay = new HuntDisplay();
 Dimension preferredSize = huntDisplay.getPreferredSize();
 assertEquals(200, preferredSize.getHeight());
 assertEquals(200, preferredSize.getWidth());
 }

 // Test getMinimumSize method
 @Test
 public void testGetMinimumSize() {
 HuntDisplay huntDisplay = new HuntDisplay();
 Dimension minimumSize = huntDisplay.getMinimumSize();
 assertEquals(50, minimumSize.getHeight());
 assertEquals(50, minimumSize.getWidth());
 }

 // Test addPoint method
 @Test
 public void testAddPoint() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 assertTrue(huntDisplay.points.size() == 1);
 }

 // Test addLandMark method
 @Test
 public void testAddLandMark() {
 HuntDisplay huntDisplay = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("Image.jpg"));
 huntDisplay.addLandMark(landmark);
 assertTrue(huntDisplay.landmarks.size() == 1);
 }

 // Test paintComponent method with normal points
 @Test
 public void testPaintComponentWithNormalPoints() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint trigPoint1 = new TrigPoint(1.0, 1.0, 0.0, false);
 TrigPoint trigPoint2 = new TrigPoint(2.0, 2.0, 0.0, false);
 TrigPoint trigPoint3 = new TrigPoint(3.0, 3.0, 0.0, false);
 huntDisplay.addPoint(trigPoint1);
 huntDisplay.addPoint(trigPoint2);
 huntDisplay.addPoint(trigPoint3);

 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(400, 400);
 frame.setVisible(true);
 frame.add(huntDisplay);

 BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 huntDisplay.paintComponent(g);
 g.dispose();

 int x1 = (int)((trigPoint1.getX() + 0.5) * frame.getWidth());
 int y1 = (int)((trigPoint1.getY() + 0.5) * frame.getHeight());
 int x2 = (int)((trigPoint2.getX() + 0.5) * frame.getWidth());
 int y2 = (int)((trigPoint2.getY() + 0.5) * frame.getHeight());
 int x3 = (int)((trigPoint3.getX() + 0.5) * frame.getWidth());
 int y3 = (int)((trigPoint3.getY() + 0.5) * frame.getHeight());

 Point point1 = new Point(x1, y1);
 Point point2 = new Point(x2, y2);
 Point point3 = new Point(x3, y3);

 List<Point> points = Arrays.asList(point1, point2, point3);
 List<Point> expectedPoints = Arrays.asList(new Point(x1, y1), new Point(x2, y2), new Point(x3, y3));

 assertEquals(expectedPoints, points);
 }

 // Test paintComponent method with fox point
 @Test
 public void testPaintComponentWithFoxPoint() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint trigPoint1 = new TrigPoint(1.0, 1.0, 0.0, true);
 TrigPoint trigPoint2 = new TrigPoint(2.0, 2.0, 0.0, false);
 TrigPoint trigPoint3 = new TrigPoint(3.0, 3.0, 0.0, false);
 huntDisplay.addPoint(trigPoint1);
 huntDisplay.addPoint(trigPoint2);
 huntDisplay.addPoint(trigPoint3);

 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(400, 400);
 frame.setVisible(true);
 frame.add(huntDisplay);

 BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 huntDisplay.paintComponent(g);
 g.dispose();

 int x1 = (int)((trigPoint1.getX() + 0.5) * frame.getWidth());
 int y1 = (int)((trigPoint1.getY() + 0.5) * frame.getHeight());
 int x2 = (int)((trigPoint2.getX() + 0.5) * frame.getWidth());
 int y2 = (int)((trigPoint2.getY() + 0.5) * frame.getHeight());
 int x3 = (int)((trigPoint3.getX() + 0.5) * frame.getWidth());
 int y3 = (int)((trigPoint3.getY() + 0.5) * frame.getHeight());
 int x4 = (int)((trigPoint1.getX() + 0.5) * frame.getWidth());
 int y4 = (int)((trigPoint1.getY() + 0.5) * frame.getHeight());

 Point point1 = new Point(x1, y1);
 Point point2 = new Point(x2, y2);
 Point point3 = new Point(x3, y3);
 Point point4 = new Point(x4, y4);

 List<Point> points = Arrays.asList(point1, point2, point3, point4);
 List<Point> expectedPoints = Arrays.asList(new Point(x1, y1), new Point(x2, y2), new Point(x3, y3), new Point(x4, y4));

 assertEquals(expectedPoints, points);

 // Check if the green fox point is present
 for (Point point : points) {
 if (point.getX() == x4 && point.getY() == y4) {
 assertEquals(Color.GREEN, huntDisplay.getColorAt(point.getX(), point.getY()));
 break;
 }
 }
 }

 // Test paintComponent method with landmarks
 @Test(expected = NullPointerException.class)
 public void testPaintComponentWithNullLandmarks() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint trigPoint1 = new TrigPoint(1.0, 1.0, 0.0, false);
 TrigPoint trigPoint2 = new TrigPoint(2.0, 2.0, 0.0, false);
 TrigPoint trigPoint3 = new TrigPoint(3.0, 3.0, 0.0, false);
 huntDisplay.addPoint(trigPoint1);
 huntDisplay.addPoint(trigPoint2);
 huntDisplay.addPoint(trigPoint3);

 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(400, 400);
 frame.setVisible(true);
 frame.add(huntDisplay);

 BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
 huntDisplay.paintComponent(image.getGraphics());

 huntDisplay.landmarks.clear();
 huntDisplay.paintComponent(image.getGraphics());
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint trigPoint1 = new TrigPoint(1.0, 1.0, 0.0, false);
 TrigPoint trigPoint2 = new TrigPoint(2.0, 2.0, 0.0, false);
 TrigPoint trigPoint3 = new TrigPoint(3.0, 3.0, 0.0, false);
 Landmark landmark1 = new Landmark(1.0, 1.0, new ImageIcon("Image1.jpg"));
 Landmark landmark2 = new Landmark(2.0, 2.0, new ImageIcon("Image2.jpg"));
 Landmark landmark3 = new Landmark(3.0, 3.0, new ImageIcon("Image3.jpg"));
 huntDisplay.addPoint(trigPoint1);
 huntDisplay.addPoint(trigPoint2);
 huntDisplay.addPoint(trigPoint3);
 huntDisplay.addLandMark(landmark1);
 huntDisplay.addLandMark(landmark2);
 huntDisplay.addLandMark(landmark3);

 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(400, 400);
 frame.setVisible(true);
 frame.add(huntDisplay);

 BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
 huntDisplay.paintComponent(image.getGraphics());

 int x1 = (int)((trigPoint1.getX() + 0.5) * frame.getWidth());
 int y1 = (int)((trigPoint1.getY() + 0.5) * frame.getHeight());
 int x2 = (int)((trigPoint2.getX() + 0.5) * frame.getWidth());
 int y2 = (int)((trigPoint2.getY() + 0.5) * frame.getHeight());
 int x3 = (int)((trigPoint3.getX() + 0.5) * frame.getWidth());
 int y3 = (int)((trigPoint3.getY() + 0.5) * frame.getHeight());
 int x4 = (int)((landmark1.getLon() + 0.5) * frame.getWidth());
 int y4 = (int)((landmark1.getLat() + 0.5) * frame.getHeight());
 int x5 = (int)((landmark2.getLon() + 0.5) * frame.getWidth());
 int y5 = (int)((landmark2.getLat() + 0.5) * frame.getHeight());
 int x6 = (int)((landmark3.getLon() + 0.5) * frame.getWidth());
 int y6 = (int)((landmark3.getLat() + 0.5) * frame.getHeight());

 Point point1 = new Point(x1, y1);
 Point point2 = new Point(x2, y2);
 Point point3 = new Point(x3, y3);
 Point point4 = new Point(x4, y4);
 Point point5 = new Point(x5, y5);
 Point point6 = new Point(x6, y6);

 List<Point> points = Arrays.asList(point1, point2, point3, point4, point5, point6);
 List<Point> expectedPoints = Arrays.asList(new Point(x1, y1), new Point(x2, y2), new Point(x3, y3), new Point(x4, y4), new Point(x5, y5), new Point(x6, y6));

 assertEquals(expectedPoints, points);

 // Check if the landmarks are present and displayed correctly
 for (int i = 0; i < huntDisplay.landmarks.size(); i++) {
 Landmark landmark = huntDisplay.landmarks.get(i);
 int x = (int)((landmark.getLon() + 0.5) * frame.getWidth());
 int y = (int)((landmark.getLat() + 0.5) * frame.getHeight());
 Image image = landmark.getIcon().getImage();

 assertEquals(image.getWidth(null), 10);
 assertEquals(image.getHeight(null), 10);

 BufferedImage imageFromComponent = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
 huntDisplay.paintComponent(imageFromComponent.getGraphics());

 int x2 = imageFromComponent.getRGB(x, y) >> 16;
 int y2 = imageFromComponent.getRGB(x, y) >> 8 & 0xFF;
 int r = imageFromComponent.getRGB(x, y) & 0xFF;

 assertEquals(image.getRGB(0, 0) >> 16, x2);
 assertEquals(image.getRGB(0, 0) >> 8 & 0xFF, y2);
 assertEquals(image.getRGB(0, 0) & 0xFF, r);
 }
 }

 private static class Point {
 int x, y;

 Point(int x, int y) {
 this.x = x;
 this.y = y;
 }
 }
}