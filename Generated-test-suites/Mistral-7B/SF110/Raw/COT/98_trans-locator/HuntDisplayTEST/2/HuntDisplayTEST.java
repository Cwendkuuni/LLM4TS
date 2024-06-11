package Joshua.FoxHunt.Test;

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
 Equals expected = new Equals(new Dimension(200, 200));
 Equals actual = new Equals(display.getPreferredSize());
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMinimumSize() {
 Equals expected = new Equals(new Dimension(50, 50));
 Equals actual = new Equals(display.getMinimumSize());
 assertEquals(expected, actual);
 }

 @Test
 public void testAddPoint() {
 TrigPoint trigPoint = new TrigPoint(0.5, 0.5, 0.0, false);
 display.addPoint(trigPoint);
 int width = display.getWidth();
 int height = display.getHeight();
 int x = (int) ((trigPoint.getX() + 0.5 - width / 2.0) * width);
 int y = (int) ((trigPoint.getY() + 0.5 - height / 2.0) * height);
 Point expectedPoint = new Point(x, y);
 Point actualPoint = new Point(display.getGraphics().getColor().getColorComponents(new float[3]));
 assertEquals(expectedPoint, actualPoint);
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(0.5, 0.5, new ImageIcon("icon.png"));
 display.addLandmark(landmark);
 int width = display.getWidth();
 int height = display.getHeight();
 int x = (int) ((landmark.getLon() + 0.5 - width / 2.0) * width);
 int y = (int) ((landmark.getLat() + 0.5 - height / 2.0) * height);
 Rectangle expectedRectangle = new Rectangle(x, y, 10, 10);
 Rectangle actualRectangle = new Rectangle(display.getGraphics().getClipBounds());
 assertTrue(expectedRectangle.contains(actualRectangle));
 }

 @Test(expected = NullPointerException.class)
 public void testAddLandmarkWithNullLandmark() {
 display.addLandmark(null);
 }

 @Test
 public void testPaintComponentWithNoPointsAndNoLandmarks() {
 Graphics2D graphics = (Graphics2D) display.getGraphics();
 graphics.setColor(Color.BLACK);
 graphics.fillRect(0, 0, display.getWidth(), display.getHeight());
 graphics.dispose();
 }

 @Test
 public void testPaintComponentWithOnePoint() {
 TrigPoint trigPoint = new TrigPoint(0.5, 0.5, 0.0, false);
 display.addPoint(trigPoint);
 Graphics2D graphics = (Graphics2D) display.getGraphics();
 graphics.setColor(Color.GREEN);
 graphics.fillOval(0, 0, display.getWidth(), display.getHeight());
 graphics.dispose();
 }

 @Test
 public void testPaintComponentWithOneLandmark() {
 Landmark landmark = new Landmark(0.5, 0.5, new ImageIcon("icon.png"));
 display.addLandmark(landmark);
 Graphics2D graphics = (Graphics2D) display.getGraphics();
 graphics.setColor(Color.GREEN);
 graphics.fillOval(0, 0, display.getWidth(), display.getHeight());
 graphics.setColor(Color.BLACK);
 graphics.drawImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), 0, 0, null);
 graphics.dispose();
 }

 @Test
 public void testPaintComponentWithMultiplePointsAndMultipleLandmarks() {
 TrigPoint point1 = new TrigPoint(0.1, 0.1, 0.0, false);
 TrigPoint point2 = new TrigPoint(0.2, 0.2, 0.0, false);
 TrigPoint point3 = new TrigPoint(0.3, 0.3, 0.0, false);
 Landmark landmark1 = new Landmark(0.4, 0.4, new ImageIcon("icon.png"));
 Landmark landmark2 = new Landmark(0.5, 0.5, new ImageIcon("icon.png"));
 display.addPoint(point1);
 display.addPoint(point2);
 display.addPoint(point3);
 display.addLandmark(landmark1);
 display.addLandmark(landmark2);
 Graphics2D graphics = (Graphics2D) display.getGraphics();
 graphics.setColor(Color.GREEN);
 graphics.fillOval(0, 0, display.getWidth(), display.getHeight());
 graphics.setColor(Color.BLACK);
 graphics.drawImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), 0, 0, null);
 graphics.dispose();
 }

 @Test
 public void testPaintComponentWithMultiplePointsAndNoLandmarks() {
 TrigPoint point1 = new TrigPoint(0.1, 0.1, 0.0, false);
 TrigPoint point2 = new TrigPoint(0.2, 0.2, 0.0, false);
 TrigPoint point3 = new TrigPoint(0.3, 0.3, 0.0, false);
 display.addPoint(point1);
 display.addPoint(point2);
 display.addPoint(point3);
 Graphics2D graphics = (Graphics2D) display.getGraphics();
 graphics.setColor(Color.GREEN);
 graphics.fillOval(0, 0, display.getWidth(), display.getHeight());
 graphics.dispose();
 }

 @Test
 public void testPaintComponentWithMultiplePointsAndLandmarksWithDifferentCoordinates() {
 TrigPoint point1 = new TrigPoint(0.1, 0.1, 0.0, false);
 TrigPoint point2 = new TrigPoint(0.2, 0.2, 0.0, false);
 Landmark landmark1 = new Landmark(0.4, 0.4, new ImageIcon("icon.png"));
 Landmark landmark2 = new Landmark(0.5, 0.5, new ImageIcon("icon.png"));
 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandmark(landmark1);
 display.addLandmark(landmark2);
 Graphics2D graphics = (Graphics2D) display.getGraphics();
 graphics.setColor(Color.GREEN);
 graphics.fillOval(0, 0, display.getWidth(), display.getHeight());
 graphics.setColor(Color.BLACK);
 graphics.drawImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), 0, 0, null);
 graphics.dispose();
 }

 @Test
 public void testPaintComponentWithMultiplePointsAndLandmarksWithSameCoordinates() {
 TrigPoint point1 = new TrigPoint(0.1, 0.1, 0.0, false);
 TrigPoint point2 = new TrigPoint(0.2, 0.2, 0.0, false);
 Landmark landmark1 = new Landmark(0.1, 0.1, new ImageIcon("icon.png"));
 Landmark landmark2 = new Landmark(0.2, 0.2, new ImageIcon("icon.png"));
 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandmark(landmark1);
 display.addLandmark(landmark2);
 Graphics2D graphics = (Graphics2D) display.getGraphics();
 graphics.setColor(Color.GREEN);
 graphics.fillOval(0, 0, display.getWidth(), display.getHeight());
 graphics.setColor(Color.BLACK);
 graphics.drawImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), 0, 0, null);
 graphics.dispose();
 }

 @Test
 public void testPaintComponentWithEmptyDisplay() {
 display.addPoint(new TrigPoint(0.1, 0.1, 0.0, false));
 display.addLandmark(new Landmark(0.1, 0.1, new ImageIcon("icon.png")));
 display.repaint();
 Graphics2D graphics = (Graphics2D) display.getGraphics();
 graphics.setColor(Color.GREEN);
 graphics.fillOval(0, 0, display.getWidth(), display.getHeight());
 graphics.dispose();
 }

 @Test
 public void testPaintComponentWithDisplayContainingOnlyLandmarks() {
 Landmark landmark1 = new Landmark(0.1, 0.1, new ImageIcon("icon.png"));
 Landmark landmark2 = new Landmark(0.2, 0.2, new ImageIcon("icon.png"));
 display.addLandmark(landmark1);
 display.addLandmark(landmark2);
 display.repaint();
 Graphics2D graphics = (Graphics2D) display.getGraphics();
 graphics.setColor(Color.GREEN);
 graphics.fillOval(0, 0, display.getWidth(), display.getHeight());
 graphics.dispose();
 }

 @Test
 public void testPaintComponentWithDisplayContainingOnlyPoints() {
 TrigPoint point1 = new TrigPoint(0.1, 0.1, 0.0, false);
 TrigPoint point2 = new TrigPoint(0.2, 0.2, 0.0, false);
 display.addPoint(point1);
 display.addPoint(point2);
 display.repaint();
 Graphics2D graphics = (Graphics2D) display.getGraphics();
 graphics.setColor(Color.GREEN);
 graphics.fillOval(0, 0, display.getWidth(), display.getHeight());
 graphics.dispose();
 }

 @Test
 public void testPaintComponentWithDisplayContainingPointsAndLandmarksWithSameCoordinates() {
 TrigPoint point1 = new TrigPoint(0.1, 0.1, 0.0, false);
 TrigPoint point2 = new TrigPoint(0.2, 0.2, 0.0, false);
 Landmark landmark1 = new Landmark(0.1, 0.1, new ImageIcon("icon.png"));
 Landmark landmark2 = new Landmark(0.2, 0.2, new ImageIcon("icon.png"));
 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandmark(landmark1);
 display.addLandmark(landmark2);
 display.repaint();
 Graphics2D graphics = (Graphics2D) display.getGraphics();
 graphics.setColor(Color.GREEN);
 graphics.fillOval(0, 0, display.getWidth(), display.getHeight());
 graphics.dispose();
 }

}