package Joshua.FoxHunt.Tests;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

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
 Dimension preferredSize = display.getPreferredSize();
 assertEquals(200, preferredSize.getHeight());
 assertEquals(200, preferredSize.getWidth());
 }

 @Test
 public void testGetMinimumSize() {
 display = new HuntDisplay();
 Dimension minimumSize = display.getMinimumSize();
 assertEquals(50, minimumSize.getHeight());
 assertEquals(50, minimumSize.getWidth());
 }

 @Test
 public void testAddPoint() {
 display = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(trigPoint);
 assertTrue(display.points.contains(trigPoint));
 }

 @Test(expected = NullPointerException.class)
 public void testAddPointNullPoint() {
 display = new HuntDisplay();
 display.addPoint(null);
 }

 @Test
 public void testAddLandMark() {
 display = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
 display.addLandMark(landmark);
 assertTrue(display.landmarks.contains(landmark));
 }

 @Test(expected = NullPointerException.class)
 public void testAddLandMarkNullLandmark() {
 display = new HuntDisplay();
 display.addLandMark(null);
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 Image image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 display = new HuntDisplay();
 display.paintComponent(g);
 assertEmpty(image);
 }

 @Test
 public void testPaintComponentWithPoints() {
 Image image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 display = new HuntDisplay();
 display.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
 display.addPoint(new TrigPoint(3.0, 4.0, 0.0, false));
 display.addPoint(new TrigPoint(5.0, 6.0, 0.0, false));
 display.paintComponent(g);
 assertNotSame(image.getRGB(0, 0), image.getRGB(2, 2));
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 Image image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon("icon1.png"));
 Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon("icon2.png"));
 display = new HuntDisplay();
 display.addLandMark(landmark1);
 display.addLandMark(landmark2);
 display.paintComponent(g);
 assertNotSame(image.getRGB(0, 0), image.getRGB(3, 3));
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 Image image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 display = new HuntDisplay();
 display.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
 display.addPoint(new TrigPoint(3.0, 4.0, 0.0, false));
 display.addLandMark(new Landmark(1.5, 2.5, new ImageIcon("icon1.png")));
 display.addLandMark(new Landmark(3.5, 4.5, new ImageIcon("icon2.png")));
 display.paintComponent(g);
 assertNotSame(image.getRGB(0, 0), image.getRGB(4, 4));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointOutsideRange() {
 display = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(10.0, 20.0, 0.0, false);
 display.addPoint(trigPoint);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandMarkOutsideRange() {
 display = new HuntDisplay();
 Landmark landmark = new Landmark(10.0, 20.0, new ImageIcon("icon.png"));
 display.addLandMark(landmark);
 }

}