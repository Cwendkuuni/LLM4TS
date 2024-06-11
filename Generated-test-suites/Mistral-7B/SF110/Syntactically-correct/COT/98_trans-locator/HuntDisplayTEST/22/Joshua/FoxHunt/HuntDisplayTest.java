package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Test
 public void testHuntDisplayConstructor() {
 display = new HuntDisplay();
 assertEquals(new Dimension(200, 200), display.getPrefSize());
 assertEquals(new Dimension(50, 50), display.getMinSize());
 }

 @Test
 public void testAddPoint() {
 display = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0 / 90.0, 2.0 / 180.0, 0.0, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullPoint() {
 display = new HuntDisplay();
 display.addPoint(null);
 }

 @Test
 public void testAddLandmark() {
 display = new HuntDisplay();
 Landmark landmark = new Landmark(1.0 / 90.0, 2.0 / 180.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
 display.addLandmark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullLandmark() {
 display = new HuntDisplay();
 display.addLandmark(null);
 }

 @Test
 public void testPaintComponent() {
 display = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(1.0 / 90.0, 2.0 / 180.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0 / 90.0, 4.0 / 180.0, 0.0, false);
 Landmark landmark = new Landmark(5.0 / 90.0, 6.0 / 180.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandmark(landmark);
 Graphics graphics = createMockGraphics();
 display.paintComponent(graphics);
 Point2D expectedPoint1 = new Point2D.Double(point1.getX(), point1.getY());
 Point2D expectedPoint2 = new Point2D.Double(point2.getX(), point2.getY());
 assertEquals(true, graphics.getColor().equals(Color.GREEN));
 graphics.setColor(Color.BLACK);
 int x1 = (int)(expectedPoint1.getX() * display.getWidth());
 int y1 = (int)(expectedPoint1.getY() * display.getHeight());
 int x2 = (int)(expectedPoint2.getX() * display.getWidth());
 int y2 = (int)(expectedPoint2.getY() * display.getHeight());
 graphics.fillOval(x1, y1, 5, 5);
 graphics.fillOval(x2, y2, 5, 5);
 Landmark landmarkObject = display.landmarks.elementAt(0);
 BufferedImage icon = landmarkObject.getIcon().getImage();
 int width = icon.getWidth();
 int height = icon.getHeight();
 int x3 = (int)(landmarkObject.getLon() * display.getWidth());
 int y3 = (int)(landmarkObject.getLat() * display.getHeight());
 graphics.drawImage(icon, x3, y3, width, height, graphics, null);
 }

 @Test
 public void testGetPreferredSize() {
 display = new HuntDisplay();
 assertEquals(new Dimension(200, 200), display.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 display = new HuntDisplay();
 assertEquals(new Dimension(50, 50), display.getMinimumSize());
 }
}