package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;
 private BufferedImage landmarkImage;

 @Before
 public void setUp() {
 display = new HuntDisplay();
 landmarkImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 landmarkImage.setRGB(0, 0, 0xFF00FF); // Set the landmark image to a red color for testing
 }

 @Test
 public void testConstructor() {
 HuntDisplay newDisplay = new HuntDisplay();
 assertNotNull(newDisplay);
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
 TrigPoint point = new TrigPoint(3.0, -4.0, 0.0, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullPoint() {
 HuntDisplay newDisplay = new HuntDisplay();
 newDisplay.addPoint(null);
 fail("IllegalArgumentException was expected");
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(3.0, -4.0, landmarkImage);
 display.addLandMark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandMarkWithNullLandmark() {
 HuntDisplay newDisplay = new HuntDisplay();
 newDisplay.addLandMark(null);
 fail("IllegalArgumentException was expected");
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 assertEquals(0, display.points.size());
 assertEquals(0, display.landmarks.size());
 assertFalse(g.getColor().equals(Color.GREEN));
 }

 @Test
 public void testPaintComponentWithPoints() {
 TrigPoint point1 = new TrigPoint(3.0, -4.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.5, -3.5, 0.0, false);
 TrigPoint point3 = new TrigPoint(2.5, -3.0, 0.0, true);
 display.addPoint(point1);
 display.addPoint(point2);
 display.addPoint(point3);
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 assertEquals(3, display.points.size());
 assertThat(g.getColor(), is(instanceOf(Color.class)));
 assertFalse(g.getColor().equals(Color.GREEN));
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 Landmark landmark1 = new Landmark(3.0, -4.0, landmarkImage);
 Landmark landmark2 = new Landmark(3.5, -3.5, landmarkImage);
 display.addLandMark(landmark1);
 display.addLandMark(landmark2);
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 assertEquals(2, display.landmarks.size());
 }

 @Test
 public void testPaintComponentWithBothPointsAndLandmarks() {
 TrigPoint point1 = new TrigPoint(3.0, -4.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.5, -3.5, 0.0, false);
 TrigPoint point3 = new TrigPoint(2.5, -3.0, 0.0, true);
 Landmark landmark1 = new Landmark(3.0, -4.0, landmarkImage);
 Landmark landmark2 = new Landmark(3.5, -3.5, landmarkImage);
 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandMark(landmark1);
 display.addLandMark(landmark2);
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 assertEquals(4, display.points.size() + display.landmarks.size());
 }

 private Graphics createMockGraphics() {
 Graphics g = Mockito.mock(Graphics.class);
 return g;
 }

}