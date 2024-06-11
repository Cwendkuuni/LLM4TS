package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Before
 public void setup() {
 huntDisplay = new HuntDisplay();
 }

 @Test
 public void testHuntDisplayConstructor() {
 HuntDisplay huntDisplay = new HuntDisplay();
 assertNotNull(huntDisplay);
 }

 @Test
 public void testGetPreferredSize() {
 Dimension expected = new Dimension(200, 200);
 Dimension actual = huntDisplay.getPreferredSize();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMinimumSize() {
 Dimension expected = new Dimension(50, 50);
 Dimension actual = huntDisplay.getMinimumSize();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, true);
 huntDisplay.addPoint(point);
 assertEquals(1, huntDisplay.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointNullPoint() {
 huntDisplay.addPoint(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointInvalidX() {
 TrigPoint point = new TrigPoint(-1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(point);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointInvalidY() {
 TrigPoint point = new TrigPoint(1.0, -1.0, 0.0, false);
 huntDisplay.addPoint(point);
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(3.0, 4.0, new Icon() {
 @Override
 public int getIconWidth() {
 return 10;
 }

 @Override
 public int getIconHeight() {
 return 10;
 }

 @Override
 public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
 g.drawOval(x, y, 10, 10);
 }
 });
 huntDisplay.addLandmark(landmark);
 assertEquals(1, huntDisplay.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkNullLandmark() {
 huntDisplay.addLandmark(null);
 }

 @Test
 public void testPaintComponent() {
 Graphics g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 assertNotNull(g);
 huntDisplay.repaint();
 }

 @Test
 public void testPaintComponentEmpty() {
 Graphics g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 assertNull(g);
 }

 @Test
 public void testPaintComponentWithPoints() {
 TrigPoint[] points = new TrigPoint[5];
 for (int i = 0; i < points.length; i++) {
 points[i] = new TrigPoint(i % 2 == 0 ? 1.0 : 2.0, i % 2 == 0 ? 1.0 : 2.0, 0.0, false);
 huntDisplay.addPoint(points[i]);
 }
 huntDisplay.repaint();

 Graphics g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 int pointCount = 0;
 for (Object obj : huntDisplay.points) {
 TrigPoint trigPoint = (TrigPoint) obj;
 int x = (int)((trigPoint.getX() + 0.5 - huntDisplay.getWidth() / 2) * huntDisplay.getWidth());
 int y = (int)((trigPoint.getY() + 0.5 - huntDisplay.getHeight() / 2) * huntDisplay.getHeight());
 Point expectedPoint = new Point(x, y);
 Point actualPoint = new Point((int)trigPoint.getX() * huntDisplay.getWidth(), (int)trigPoint.getY() * huntDisplay.getHeight());
 assertEquals(expectedPoint, actualPoint);
 if (trigPoint.isFox()) {
 assertEquals(Color.GREEN, g.getColor());
 } else {
 assertEquals(Color.BLACK, g.getColor());
 }
 g.setColor(Color.BLACK);
 g.fillOval(x, y, 5, 5);
 pointCount++;
 }
 assertEquals(5, pointCount);
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 Landmark[] landmarks = new Landmark[5];
 for (int i = 0; i < landmarks.length; i++) {
 Landmark landmark = new Landmark(i % 2 == 0 ? 1.0 : 2.0, i % 2 == 0 ? 1.0 : 2.0, new Icon() {
 @Override
 public int getIconWidth() {
 return 10;
 }

 @Override
 public int getIconHeight() {
 return 10;
 }

 @Override
 public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
 g.drawOval(x, y, 10, 10);
 }
 });
 huntDisplay.addLandmark(landmark);
 }
 huntDisplay.repaint();

 Graphics g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 int landmarkCount = 0;
 for (Object obj : huntDisplay.landmarks) {
 Landmark landmark = (Landmark) obj;
 int x = (int)((landmark.getLon() + 0.5 - huntDisplay.getWidth() / 2) * huntDisplay.getWidth());
 int y = (int)((landmark.getLat() + 0.5 - huntDisplay.getHeight() / 2) * huntDisplay.getHeight());
 Image expectedImage = landmark.getIcon().getImage();
 Image actualImage = g.getImage();
 assertNotNull(actualImage);
 assertArrayEquals(expectedImage.getPixelBuffer(0, 0, expectedImage.getWidth(), expectedImage.getHeight()), actualImage.getSubimage(x, y, 10, 10).getRGB(0, 0, 10, 10));
 landmarkCount++;
 }
 assertEquals(5, landmarkCount);
 }
}