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

 private HuntDisplay huntDisplay;

 @Test
 public void testHuntDisplayConstructor() {
 huntDisplay = new HuntDisplay();
 assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
 assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
 assertNotNull(huntDisplay.getBackground());
 }

 @Test
 public void testAddPointMethod() {
 huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 assertEquals(1, huntDisplay.points.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddPointMethodWithNullTrigPoint() {
 huntDisplay = new HuntDisplay();
 huntDisplay.addPoint(null);
 }

 @Test
 public void testPaintComponentMethod() {
 huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
 Landmark landmark = new Landmark(1.0, 2.0, image);
 huntDisplay.addLandMark(landmark);
 Graphics g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 int expectedX = (int)((trigPoint.getX() + 0.5) * huntDisplay.getWidth());
 int expectedY = (int)((trigPoint.getY() + 0.5) * huntDisplay.getHeight());
 int landmarkX = (int)((landmark.getLon() + 0.5) * huntDisplay.getWidth());
 int landmarkY = (int)((landmark.getLat() + 0.5) * huntDisplay.getHeight());
 assertTrue(g.getColor() == Color.BLACK || g.getColor() == Color.GREEN);
 g.dispose();
 }

 @Test
 public void testPaintComponentMethodWithEmptyPointsVector() {
 huntDisplay = new HuntDisplay();
 Graphics g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 g.dispose();
 }

 @Test
 public void testPaintComponentMethodWithEmptyLandmarksVector() {
 huntDisplay = new HuntDisplay();
 huntDisplay.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
 Graphics g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 g.dispose();
 }

 @Test
 public void testGetPreferredSizeMethod() {
 huntDisplay = new HuntDisplay();
 assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
 }

 @Test
 public void testGetMinimumSizeMethod() {
 huntDisplay = new HuntDisplay();
 assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
 }

}