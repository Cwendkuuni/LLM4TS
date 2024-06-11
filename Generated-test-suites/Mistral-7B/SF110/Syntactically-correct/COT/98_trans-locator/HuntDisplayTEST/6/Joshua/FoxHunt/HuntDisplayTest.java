package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

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

 @Test
 public void testAddLandMark() {
 display = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
 display.addLandMark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 display = new HuntDisplay();
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 assertFalse(g.getColor().equals(Color.GREEN));
 }

 @Test
 public void testPaintComponentWithPointsButNoLandmarks() {
 display = new HuntDisplay();
 addPointsToDisplay(display, 5);
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 assertEquals(5, display.points.size());
 for (int i = 0; i < 5; i++) {
 g.setColor(Color.BLACK);
 int x = (int)((display.points.get(i)).getX() * display.getWidth());
 int y = (int)((display.points.get(i)).getY() * display.getHeight());
 g.fillOval(x, y, 5, 5);
 }
 assertFalse(g.getColor().equals(Color.GREEN));
 }

 @Test
 public void testPaintComponentWithLandmarksButNoPoints() {
 display = new HuntDisplay();
 addLandmarksToDisplay(display, 5);
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 assertEquals(5, display.landmarks.size());
 for (int i = 0; i < 5; i++) {
 Landmark landmark = display.landmarks.get(i);
 int x = (int)((landmark.getLon()) * display.getWidth());
 int y = (int)((landmark.getLat()) * display.getHeight());
 BufferedImage image = (BufferedImage)landmark.getIcon().getImage();
 g.drawImage(image, x, y, 10, 10, display.getBackground(), display);
 }
 }

 @Test
 public void testPaintComponentWithBothPointsAndLandmarks() {
 display = new HuntDisplay();
 addPointsAndLandmarksToDisplay(display, 3, 2);
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 assertEquals(3, display.points.size());
 assertEquals(2, display.landmarks.size());
 for (int i = 0; i < 3; i++) {
 TrigPoint point = (TrigPoint)display.points.get(i);
 g.setColor(point.isFox() ? Color.GREEN : Color.BLACK);
 int x = (int)((point.getX()) * display.getWidth());
 int y = (int)((point.getY()) * display.getHeight());
 g.fillOval(x, y, 5, 5);
 }
 for (int i = 0; i < 2; i++) {
 Landmark landmark = (Landmark)display.landmarks.get(i);
 int x = (int)((landmark.getLon()) * display.getWidth());
 int y = (int)((landmark.getLat()) * display.getHeight());
 BufferedImage image = (BufferedImage)landmark.getIcon().getImage();
 g.drawImage(image, x, y, 10, 10, display.getBackground(), display);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullPoint() {
 display = new HuntDisplay();
 display.addPoint(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandMarkWithNullLandmark() {
 display = new HuntDisplay();
 display.addLandMark(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 private void addPointsAndLandmarksToDisplay(HuntDisplay display, int numPoints, int numLandmarks) {
 for (int i = 0; i < numPoints; i++) {
 TrigPoint point = new TrigPoint(i / 100.0, i / 200.0, 0.0, i % 2 == 0);
 display.addPoint(point);
 }
 for (int i = 0; i < numLandmarks; i++) {
 Landmark landmark = new Landmark(i / 100.0, i / 200.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
 display.addLandMark(landmark);
 }
 }

 private void addLandmarksToDisplay(HuntDisplay display, int numLandmarks) {
 for (int i = 0; i < numLandmarks; i++) {
 Landmark landmark = new Landmark(i / 100.0, i / 200.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
 display.addLandMark(landmark);
 }
 }

 private void addPointsToDisplay(HuntDisplay display, int numPoints) {
 for (int i = 0; i < numPoints; i++) {
 TrigPoint point = new TrigPoint(i / 100.0, i / 200.0, 0.0, false);
 display.addPoint(point);
 }
 }

 private Graphics createMockGraphics() {
 Graphics g = Mockito.mock(Graphics.class);
 return g;
 }
}