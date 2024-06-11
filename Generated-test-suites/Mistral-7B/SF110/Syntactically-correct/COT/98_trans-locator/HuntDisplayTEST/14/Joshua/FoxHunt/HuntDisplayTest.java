package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt. Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 }

 @Test
 public void testHuntDisplayConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertNotNull(hd);
 }

 @Test
 public void testGetPreferredSize() {
 HuntDisplay hd = new HuntDisplay();
 assertEquals(new Dimension(200, 200), hd.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 HuntDisplay hd = new HuntDisplay();
 assertEquals(new Dimension(50, 50), hd.getMinimumSize());
 }

 @Test
 public void testAddPoint() {
 HuntDisplay hd = new HuntDisplay();
 TrigPoint point = new TrigPoint(2.0, 1.0, 0.0, false);
 hd.addPoint(point);
 List<TrigPoint> points = (List<TrigPoint>) hd.points.clone();
 TrigPoint addedPoint = points.get(points.size() - 1);
 assertEquals(2.0 / 180.0, addedPoint.getX(), 0.01);
 assertEquals(1.0 / 90.0, addedPoint.getY(), 0.01);
 assertEquals(0.0, addedPoint.getTheta(), 0.01);
 assertFalse(addedPoint.isFox());
 }

 @Test
 public void testAddPointWithNullPoint() {
 HuntDisplay hd = new HuntDisplay();
 assertFalse(hd.addPoint(null));
 }

 @Test
 public void testAddLandmark() {
 HuntDisplay hd = new HuntDisplay();
 Landmark landmark = new Landmark(3.0, 2.0, new ImageIcon("test.jpg"));
 hd.addLandmark(landmark);
 List<Landmark> landmarks = (List<Landmark>) hd.landmarks.clone();
 Landmark addedLandmark = landmarks.get(landmarks.size() - 1);
 assertEquals(3.0 / 180.0, addedLandmark.getLon(), 0.01);
 assertEquals(2.0 / 90.0, addedLandmark.getLat(), 0.01);
 }

 @Test
 public void testAddLandmarkWithNullLandmark() {
 HuntDisplay hd = new HuntDisplay();
 assertFalse(hd.addLandmark(null));
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 HuntDisplay hd = new HuntDisplay();
 Graphics g = SwingUtilities.createGraphics(new JFrame());
 hd.paintComponent(g);
 assertTrue(hd.getWidth() > 0 && hd.getHeight() > 0);
 int width = hd.getWidth();
 int height = hd.getHeight();
 for (int x = 0; x < width; ++x) {
 for (int y = 0; y < height; ++y) {
 if (g.getColor().equals(Color.GREEN) || g.getColor().equals(Color.BLACK)) {
 continue;
 }
 Point point = new Point(x, y);
 assertTrue(hd.pointInside(point));
 g.setColor(Color.BLACK);
 g.drawString("X:" + x + ", Y:" + y, x, y);
 }
 }
 }

 @Test
 public void testPaintComponentWithPoints() {
 HuntDisplay hd = new HuntDisplay();
 List<TrigPoint> points = new ArrayList<>();
 points.add(new TrigPoint(1.0, 1.0, 0.0, false));
 points.add(new TrigPoint(2.0, 2.0, 0.0, false));
 points.add(new TrigPoint(3.0, 3.0, 0.0, true));
 for (TrigPoint point : points) {
 hd.addPoint(point);
 }
 Graphics g = SwingUtilities.createGraphics(new JFrame());
 hd.paintComponent(g);
 int width = hd.getWidth();
 int height = hd.getHeight();
 for (int x = 0; x < width; ++x) {
 for (int y = 0; y < height; ++y) {
 if (g.getColor().equals(Color.GREEN)) {
 assertEquals(3, Collections.frequency(points, new TrigPoint(x / (double) width, y / (double) height, 0.0, true)));
 continue;
 }
 if (g.getColor().equals(Color.BLACK)) {
 assertEquals(2, Collections.frequency(points, new TrigPoint(x / (double) width, y / (double) height, 0.0, false)));
 continue;
 }
 Point point = new Point(x, y);
 assertTrue(hd.pointInside(point));
 g.setColor(Color.BLACK);
 g.drawString("X:" + x + ", Y:" + y, x, y);
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithInvalidThetaValue() {
 HuntDisplay hd = new HuntDisplay();
 TrigPoint point = new TrigPoint(2.0, 1.0, Math.PI, false);
 assertFalse(hd.addPoint(point));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullIcon() {
 HuntDisplay hd = new HuntDisplay();
 Landmark landmark = new Landmark(2.0, 1.0, null);
 assertFalse(hd.addLandmark(landmark));
 }

}