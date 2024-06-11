package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 display = new HuntDisplay();
 }

 @Test
 public void testConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertNotNull(hd);
 }

 @Test
 public void testGetPreferredSize() {
 assertEquals(new Dimension(200, 200), display.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 assertEquals(new Dimension(50, 50), display.getMinimumSize());
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddPointWithNullTrigPoint() {
 display.addPoint(null);
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
 display.addLandMark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddLandMarkWithNullLandmark() {
 display.addLandMark(null);
 }

 @Test
 public void testPaintComponent() {
 Graphics2D g = display.createGraphics();
 display.paintComponent(g);
 assertNotNull(g);
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 Graphics2D g = display.createGraphics();
 display.paintComponent(g);
 assertFalse(g.getColor().equals(Color.GREEN));
 }

 @Test
 public void testPaintComponentWithPoints() {
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point1);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 display.addPoint(point2);
 Graphics2D g = display.createGraphics();
 display.paintComponent(g);
 int point1X = (int)((point1.getX() + 0.5 - display.getPointsAverageX()) * display.getWidth());
 int point1Y = (int)((point1.getY() + 0.5 - display.getPointsAverageY()) * display.getHeight());
 int point2X = (int)((point2.getX() + 0.5 - display.getPointsAverageX()) * display.getWidth());
 int point2Y = (int)((point2.getY() + 0.5 - display.getPointsAverageY()) * display.getHeight());
 assertTrue(g.getColor().equals(Color.BLACK));
 g.setColor(Color.BLACK);
 g.fillOval(point1X, point1Y, 5, 5);
 g.fillOval(point2X, point2Y, 5, 5);
 assertFalse(g.getColor().equals(Color.GREEN));
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point1);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 display.addPoint(point2);
 Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
 display.addLandMark(landmark);
 Graphics2D g = display.createGraphics();
 display.paintComponent(g);
 int point1X = (int)((point1.getX() + 0.5 - display.getPointsAverageX()) * display.getWidth());
 int point1Y = (int)((point1.getY() + 0.5 - display.getPointsAverageY()) * display.getHeight());
 int point2X = (int)((point2.getX() + 0.5 - display.getPointsAverageX()) * display.getWidth());
 int point2Y = (int)((point2.getY() + 0.5 - display.getPointsAverageY()) * display.getHeight());
 int landmarkX = (int)((landmark.getLon() + 0.5 - display.getPointsAverageX()) * display.getWidth());
 int landmarkY = (int)((landmark.getLat() + 0.5 - display.getPointsAverageY()) * display.getHeight());
 assertTrue(g.getColor().equals(Color.BLACK));
 g.setColor(Color.BLACK);
 g.fillOval(point1X, point1Y, 5, 5);
 g.fillOval(point2X, point2Y, 5, 5);
 g.setColor(Color.GREEN);
 g.fillOval(landmarkX, landmarkY, 10, 10);
 }
}