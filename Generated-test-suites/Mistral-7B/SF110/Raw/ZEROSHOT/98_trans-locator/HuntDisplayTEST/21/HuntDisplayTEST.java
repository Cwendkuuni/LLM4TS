package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Before
 public void setup() {
 display = new HuntDisplay();
 }

 @Test
 public void testHuntDisplayConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertNotNull(hd);
 }

 @Test
 public void testGetPreferredSize() {
 Dimension size = display.getPreferredSize();
 assertEquals(200, size.getHeight());
 assertEquals(200, size.getWidth());
 }

 @Test
 public void testGetMinimumSize() {
 Dimension size = display.getMinimumSize();
 assertEquals(50, size.getHeight());
 assertEquals(50, size.getWidth());
 }

 @Test
 public void testAddPoint() {
 display.addPoint(new TrigPoint(0.0, 0.0, 0.0, false));
 display.addPoint(new TrigPoint(1.0, 1.0, 0.0, true));
 int pointCount = display.points.size();
 assertEquals(2, pointCount);
 }

 @Test
 public void testAddLandmark() {
 BufferedImage landmarkImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 display.addLandmark(new Landmark(0.0, 0.0, landmarkImage));
 Landmark landmark = (Landmark) display.landmarks.lastElement();
 assertNotNull(landmark);
 }

 @Test
 public void testPaintComponent() {
 Graphics2D g = (Graphics2D) display.createGraphics();
 display.paintComponent(g);

 // Check if points are drawn
 TrigPoint point = new TrigPoint(0.5, 0.5, 0.0, false);
 display.addPoint(point);
 int x = (int)(point.getX() * display.getWidth());
 int y = (int)(point.getY() * display.getHeight());
 assertEquals(Color.BLACK.getRGB(), g.getColor().getRGB());
 g.fillOval(x, y, 5, 5);

 // Check if fox point is drawn green
 TrigPoint foxPoint = new TrigPoint(0.5, 0.5, 0.0, true);
 display.addPoint(foxPoint);
 x = (int)(foxPoint.getX() * display.getWidth());
 y = (int)(foxPoint.getY() * display.getHeight());
 assertEquals(Color.GREEN.getRGB(), g.getColor().getRGB());
 g.fillOval(x, y, 5, 5);

 // Check if landmarks are drawn
 BufferedImage landmarkImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 Landmark landmark = new Landmark(0.5, 0.5, landmarkImage);
 display.addLandmark(landmark);
 int landmarkX = (int)(landmark.getLon() * display.getWidth());
 int landmarkY = (int)(landmark.getLat() * display.getHeight());
 assertEquals(Color.WHITE.getRGB(), g.getColor().getRGB());
 g.drawImage(landmarkImage, landmarkX, landmarkY, 10, 10, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPoint() {
 display.addPoint(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullLandmark() {
 BufferedImage landmarkImage = null;
 display.addLandmark(new Landmark(0.0, 0.0, landmarkImage));
 }
}