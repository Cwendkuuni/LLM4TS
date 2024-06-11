package Joshua.FoxHunt.Tests;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import Joshua.FoxHunt.*;

@RunWith(JUnitRunner.class)
public class HuntDisplayTest {

 private HuntDisplay display;

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
 TrigPoint point = new TrigPoint(45.0 / 90.0, 123.0 / 180.0, 0.0, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullTrigPoint() {
 display.addPoint(null);
 fail("An IllegalArgumentException should be thrown");
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(50.0 / 90.0, 150.0 / 180.0, new ImageIcon(getClass().getResource("/icon.png")));
 display.addLandmark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullLandmark() {
 display.addLandmark(null);
 fail("An IllegalArgumentException should be thrown");
 }

 @Test
 public void testPaintComponent() {
 Graphics2D g = (Graphics2D) display.createGraphics();
 display.paintComponent(g);
 assertNotNull(g);
 }

 @Test
 public void testDrawingPoints() {
 TrigPoint point1 = new TrigPoint(45.0 / 90.0, 123.0 / 180.0, 0.0, false);
 display.addPoint(point1);
 TrigPoint point2 = new TrigPoint(46.0 / 90.0, 123.5 / 180.0, 0.0, false);
 display.addPoint(point2);

 BufferedImage image = new BufferedImage(display.getWidth(), display.getHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 display.paintComponent(g);
 g.dispose();

 int x1 = (int) ((point1.getX() + 0.5 - display.getPointsAverageX()) * display.getWidth());
 int y1 = (int) ((point1.getY() + 0.5 - display.getPointsAverageY()) * display.getHeight());
 int x2 = (int) ((point2.getX() + 0.5 - display.getPointsAverageX()) * display.getWidth());
 int y2 = (int) ((point2.getY() + 0.5 - display.getPointsAverageY()) * display.getHeight());

 Point point1Location = new Point(x1, y1);
 Point point2Location = new Point(x2, y2);

 assertTrue(image.getRGB(point1Location.x, point1Location.y) == point1.isFox() ? Color.GREEN.getRGB() : Color.BLACK.getRGB());
 assertTrue(image.getRGB(point2Location.x, point2Location.y) == point2.isFox() ? Color.GREEN.getRGB() : Color.BLACK.getRGB());
 }

 @Test
 public void testDrawingLandmarks() {
 Landmark landmark = new Landmark(50.0 / 90.0, 150.0 / 180.0, new ImageIcon(getClass().getResource("/icon.png")));
 display.addLandmark(landmark);

 BufferedImage image = new BufferedImage(display.getWidth(), display.getHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 display.paintComponent(g);
 g.dispose();

 int x = (int) ((landmark.getLon() + 0.5 - display.getPointsAverageX()) * display.getWidth());
 int y = (int) ((landmark.getLat() + 0.5 - display.getPointsAverageY()) * display.getHeight());

 Point landmarkLocation = new Point(x, y);

 BufferedImage iconImage = landmark.getIcon().getImage();
 int iconWidth = iconImage.getWidth(null);
 int iconHeight = iconImage.getHeight(null);

 Rectangle expectedIconRectangle = new Rectangle(landmarkLocation.x - iconWidth / 2, landmarkLocation.y - iconHeight / 2, iconWidth, iconHeight);

 for (int xPixel = 0; xPixel < iconWidth; xPixel++) {
 for (int yPixel = 0; yPixel < iconHeight; yPixel++) {
 int rgb = iconImage.getRGB(xPixel, yPixel);
 int expectedRgb = landmark.getIcon().getImage().getRGB(xPixel, yPixel);
 assertEquals(rgb, expectedRgb);
 }
 }

 assertTrue(image.getRGB(landmarkLocation.x, landmarkLocation.y) == Color.WHITE.getRGB());
 }
}