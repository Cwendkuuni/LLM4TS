package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.*;
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
 assertNotNull(display);
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

 @Test
 public void testAddPoint() {
 display = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 3.0, false);
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
 Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
 display.addLandmark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullLandmark() {
 display = new HuntDisplay();
 display.addLandmark(null);
 }

 @Test
 public void testPaintComponentNormalCase() {
 display = new HuntDisplay();
 TrigPoint trigPoint1 = new TrigPoint(1.0, 1.0, 0, false);
 TrigPoint trigPoint2 = new TrigPoint(2.0, 2.0, 0, false);
 Landmark landmark = new Landmark(3.0, 3.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
 display.addPoint(trigPoint1);
 display.addPoint(trigPoint2);
 display.addLandmark(landmark);
 BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 display.paintComponent(g);
 g.dispose();
 // Add assertions for checking the correct position and color of points and landmark
 }

 @Test
 public void testPaintComponentEmptyCase() {
 display = new HuntDisplay();
 BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 display.paintComponent(g);
 g.dispose();
 // No points or landmarks should be drawn
 }

 @Test
 public void testPaintComponentFoxCase() {
 display = new HuntDisplay();
 TrigPoint trigPoint1 = new TrigPoint(1.0, 1.0, 0, true);
 TrigPoint trigPoint2 = new TrigPoint(2.0, 2.0, 0, false);
 Landmark landmark = new Landmark(3.0, 3.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
 display.addPoint(trigPoint1);
 display.addPoint(trigPoint2);
 display.addLandmark(landmark);
 BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 display.paintComponent(g);
 g.dispose();
 // The fox point should be drawn in green color
 }
}