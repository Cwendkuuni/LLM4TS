package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Vector;

import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Test
 public void testHuntDisplayConstruction() {
 display = new HuntDisplay();
 assertEquals(new Dimension(200, 200), display.getPrefSize());
 assertEquals(new Dimension(50, 50), display.getMinSize());
 assertNotNull(display.getBackground());
 }

 @Test
 public void testAddPoint() {
 display = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullPoint() {
 display = new HuntDisplay();
 try {
 display.addPoint(null);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException, but got: " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithInvalidTheta() {
 display = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 361.0, false); // invalid theta
 try {
 display.addPoint(point);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException, but got: " + e.getMessage());
 }
 }

 @Test
 public void testAddLandmark() {
 display = new HuntDisplay();
 Landmark landmark = new Landmark(3.0, 4.0, null);
 display.addLandmark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullLandmark() {
 display = new HuntDisplay();
 try {
 display.addLandmark(null);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException, but got: " + e.getMessage());
 }
 }

 @Test
 public void testPaintComponent() {
 display = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 Landmark landmark = new Landmark(5.0, 6.0, new ImageIcon("testImage.png"));
 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandmark(landmark);

 Graphics g = createMockGraphics();
 display.paintComponent(g);

 int width = display.getWidth();
 int height = display.getHeight();
 Point point1Location = new Point((int) (point1.getX() * width + 0.5 - point1.getX() * width / 2.0),
 (int) (point1.getY() * height + 0.5 - point1.getY() * height / 2.0));
 Point point2Location = new Point((int) (point2.getX() * width + 0.5 - point2.getX() * width / 2.0),
 (int) (point2.getY() * height + 0.5 - point2.getY() * height / 2.0));
 Point landmarkLocation = new Point((int) (landmark.getLon() * width + 0.5 - landmark.getLon() * width / 2.0),
 (int) (landmark.getLat() * height + 0.5 - landmark.getLat() * height / 2.0));

 verify(g).setColor(Color.BLACK);
 verify(g).fillOval(point1Location.x, point1Location.y, 5, 5);
 verify(g).setColor(Color.GREEN);
 verify(g).fillOval(point2Location.x, point2Location.y, 5, 5);
 verify(g).setColor(Color.BLACK);

 Image testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
 g.drawImage(testImage, landmarkLocation.x, landmarkLocation.y, 10, 10, (Image) anyObject(), any(Graphics.class));
}

 private Graphics createMockGraphics() {
 return mock(Graphics.class);
 }

}