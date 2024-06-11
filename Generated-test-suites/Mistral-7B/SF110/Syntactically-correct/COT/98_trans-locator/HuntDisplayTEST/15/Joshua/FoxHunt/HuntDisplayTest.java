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
 TrigPoint point = new TrigPoint(1.0, 2.0, 0, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointNullPoint() {
 display.addPoint(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointInvalidLatitude() {
 TrigPoint point = new TrigPoint(-1.0, 2.0, 0, false);
 display.addPoint(point);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointInvalidLongitude() {
 TrigPoint point = new TrigPoint(1.0, -2.0, 0, false);
 display.addPoint(point);
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/testImage.png")));
 display.addLandmark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkNullLandmark() {
 display.addLandmark(null);
 }

 @Test
 public void testPaintComponent() {
 Graphics g = display.createGraphics();
 display.paintComponent(g);
 assertNotNull(g);
 }

 @Test(expected = NullPointerException.class)
 public void testPaintComponentNullGraphics() {
 display.paintComponent(null);
 }

 @Test
 public void testGetBackgroundColor() {
 assertEquals(Color.WHITE, display.getBackground());
 }

 @Test
 public void testAddPointAndPaint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0, false);
 display.addPoint(point);
 display.repaint();
 Point pointLocation = new Point((int)((point.getX() + 0.5) * display.getWidth()), (int)((point.getY() + 0.5) * display.getHeight()));
 assertTrue(display.contains(pointLocation.x, pointLocation.y));
 }

 @Test
 public void testAddLandmarkAndPaint() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/testImage.png")));
 display.addLandmark(landmark);
 display.repaint();
 Point landmarkLocation = new Point((int)((landmark.getLon() + 0.5) * display.getWidth()), (int)((landmark.getLat() + 0.5) * display.getHeight()));
 assertTrue(display.contains(landmarkLocation.x, landmarkLocation.y));
 }

}