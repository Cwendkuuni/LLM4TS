package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

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
 public void testHuntDisplayConstructor() {
 HuntDisplay huntDisplay = new HuntDisplay();
 assertNotNull(huntDisplay);
 }

 @Test
 public void testGetPreferredSize() {
 Dimension expected = new Dimension(200, 200);
 Dimension actual = display.getPreferredSize();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMinimumSize() {
 Dimension expected = new Dimension(50, 50);
 Dimension actual = display.getMinimumSize();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullPoint() {
 display.addPoint(null);
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 display.addLandMark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandMarkWithNullLandmark() {
 display.addLandMark(null);
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 // No points or landmarks, so no drawing should occur
 }

 @Test
 public void testPaintComponentWithPoints() {
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 display.addPoint(point1);
 display.addPoint(point2);
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 int numPointsDrawn = 0;
 for (int i = 0; i < display.points.size(); i++) {
 TrigPoint point = (TrigPoint) display.points.elementAt(i);
 if (point.isFox()) {
 g.setColor(Color.GREEN);
 } else {
 g.setColor(Color.BLACK);
 }
 int x = (int) ((point.getX() + 0.5 - display.getTrigPointAvgX()) * display.getWidth());
 int y = (int) ((point.getY() + 0.5 - display.getTrigPointAvgY()) * display.getHeight());
 assertTrue(g.fillOval(x, y, 5, 5).contains(x, y));
 numPointsDrawn++;
 }
 assertEquals(2, numPointsDrawn);
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 display.addLandmark(landmark1);
 display.addLandmark(landmark2);
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 int numLandmarksDrawn = 0;
 for (int i = 0; i < display.landmarks.size(); i++) {
 Landmark landmark = (Landmark) display.landmarks.elementAt(i);
 int x = (int) ((landmark.getLon() + 0.5 - display.getTrigPointAvgX()) * display.getWidth());
 int y = (int) ((landmark.getLat() + 0.5 - display.getTrigPointAvgY()) * display.getHeight());
 BufferedImage image = landmark.getIcon().getImage();
 assertTrue(g.drawImage(image, x, y, 10, 10, display.getBackground(), display).getWidth() > 0);
 numLandmarksDrawn++;
 }
 assertEquals(2, numLandmarksDrawn);
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandmark(landmark1);
 display.addLandmark(landmark2);
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 int numPointsDrawn = 0;
 int numLandmarksDrawn = 0;
 for (int i = 0; i < display.points.size(); i++) {
 TrigPoint point = (TrigPoint) display.points.elementAt(i);
 if (point.isFox()) {
 g.setColor(Color.GREEN);
 } else {
 g.setColor(Color.BLACK);
 }
 int x = (int) ((point.getX() + 0.5 - display.getTrigPointAvgX()) * display.getWidth());
 int y = (int) ((point.getY() + 0.5 - display.getTrigPointAvgY()) * display.getHeight());
 assertTrue(g.fillOval(x, y, 5, 5).contains(x, y));
 numPointsDrawn++;
 }
 for (int i = 0; i < display.landmarks.size(); i++) {
 Landmark landmark = (Landmark) display.landmarks.elementAt(i);
 int x = (int) ((landmark.getLon() + 0.5 - display.getTrigPointAvgX()) * display.getWidth());
 int y = (int) ((landmark.getLat() + 0.5 - display.getTrigPointAvgY()) * display.getHeight());
 BufferedImage image = landmark.getIcon().getImage();
 assertTrue(g.drawImage(image, x, y, 10, 10, display.getBackground(), display).getWidth() > 0);
 numLandmarksDrawn++;
 }
 assertEquals(2, numPointsDrawn);
 assertEquals(2, numLandmarksDrawn);
 }

 private Graphics createMockGraphics() {
 Graphics g = mock(Graphics.class);
 return g;
 }
}