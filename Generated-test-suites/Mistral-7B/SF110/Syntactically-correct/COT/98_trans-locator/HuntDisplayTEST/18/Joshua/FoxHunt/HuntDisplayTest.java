package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointNullPoint() {
 display = new HuntDisplay();
 display.addPoint(null);
 }

 @Test
 public void testAddLandMark() {
 display = new HuntDisplay();
 BufferedImage image = ImageIO.read(getClass().getResource("/testIcon.jpg"));
 Landmark landmark = new Landmark(1.0, 2.0, image);
 display.addLandMark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandMarkNullLandmark() {
 display = new HuntDisplay();
 display.addLandMark(null);
 }

 @Test
 public void testPaintComponent() {
 display = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 Landmark landmark = new Landmark(1.5, 2.5, ImageIO.read(getClass().getResource("/testIcon.jpg")));
 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandMark(landmark);
 Graphics2D g = (Graphics2D) display.createGraphics();
 display.paintComponent(g);
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 display = new HuntDisplay();
 Graphics2D g = (Graphics2D) display.createGraphics();
 display.paintComponent(g);
 }

 @Test
 public void testPaintComponentWithOnlyPoints() {
 display = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 display.addPoint(point1);
 display.addPoint(point2);
 Graphics2D g = (Graphics2D) display.createGraphics();
 display.paintComponent(g);
 }

 @Test
 public void testPaintComponentWithOnlyLandmarks() {
 display = new HuntDisplay();
 Landmark landmark1 = new Landmark(1.0, 2.0, ImageIO.read(getClass().getResource("/testIcon.jpg")));
 Landmark landmark2 = new Landmark(3.0, 4.0, ImageIO.read(getClass().getResource("/testIcon.jpg")));
 display.addLandMark(landmark1);
 display.addLandMark(landmark2);
 Graphics2D g = (Graphics2D) display.createGraphics();
 display.paintComponent(g);
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 display = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 Landmark landmark1 = new Landmark(1.5, 2.5, ImageIO.read(getClass().getResource("/testIcon.jpg")));
 Landmark landmark2 = new Landmark(3.5, 4.5, ImageIO.read(getClass().getResource("/testIcon.jpg")));
 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandMark(landmark1);
 display.addLandMark(landmark2);
 Graphics2D g = (Graphics2D) display.createGraphics();
 display.paintComponent(g);
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarksDifferentSizes() {
 display = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 Landmark landmark1 = new Landmark(1.5, 2.5, ImageIO.read(getClass().getResource("/testIcon.jpg")));
 Landmark landmark2 = new Landmark(1.5, 2.5, ImageIO.read(getClass().getResource("/testIcon2.jpg")));
 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandMark(landmark1);
 display.addLandMark(landmark2);
 Graphics2D g = (Graphics2D) display.createGraphics();
 display.paintComponent(g);
 }

 @Test
 public void testPaintComponentWithFoxPoint() {
 display = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, true);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 Landmark landmark = new Landmark(1.5, 2.5, ImageIO.read(getClass().getResource("/testIcon.jpg")));
 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandMark(landmark);
 Graphics2D g = (Graphics2D) display.createGraphics();
 display.paintComponent(g);
 }
}