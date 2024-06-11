package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;
 private BufferedImage testImage;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 testImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
 }

 @Test
 public void testHuntDisplayConstructor() {
 HuntDisplay huntDisplay = new HuntDisplay();
 assertNotNull(huntDisplay);
 }

 @Test
 public void testGetPreferredSize() {
 Dimension expectedPreferredSize = new Dimension(200, 200);
 Dimension actualPreferredSize = huntDisplay.getPreferredSize();
 assertEquals(expectedPreferredSize, actualPreferredSize);
 }

 @Test
 public void testGetMinimumSize() {
 Dimension expectedMinimumSize = new Dimension(50, 50);
 Dimension actualMinimumSize = huntDisplay.getMinimumSize();
 assertEquals(expectedMinimumSize, actualMinimumSize);
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(0.5, 0.5, 0, true);
 huntDisplay.addPoint(point);
 assertNotSame(0, huntDisplay.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullPoint() {
 huntDisplay.addPoint(null);
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(0.5, 0.5, new ImageIcon(testImage));
 huntDisplay.addLandmark(landmark);
 assertNotSame(0, huntDisplay.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullLandmark() {
 huntDisplay.addLandmark(null);
 }

 @Test
 public void testPaintComponentWithNoPointsAndLandmarks() {
 Graphics g = testImage.getGraphics();
 huntDisplay.paintComponent(g);
 assertNull(g.getColor());
 }

 @Test
 public void testPaintComponentWithPoints() {
 TrigPoint point1 = new TrigPoint(0.1, 0.1, 0, false);
 TrigPoint point2 = new TrigPoint(0.3, 0.3, 0, false);
 TrigPoint point3 = new TrigPoint(0.5, 0.5, 0, true);
 huntDisplay.addPoint(point1);
 huntDisplay.addPoint(point2);
 huntDisplay.addPoint(point3);
 huntDisplay.paintComponent(testImage.getGraphics());
 assertColor(Color.BLACK, testImage, 100, 100);
 assertColor(Color.GREEN, testImage, 150, 150);
 assertColor(Color.BLACK, testImage, 200, 200);
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 TrigPoint point1 = new TrigPoint(0.1, 0.1, 0, false);
 TrigPoint point2 = new TrigPoint(0.3, 0.3, 0, false);
 TrigPoint point3 = new TrigPoint(0.5, 0.5, 0, true);
 Landmark landmark1 = new Landmark(0.2, 0.2, new ImageIcon(testImage));
 Landmark landmark2 = new Landmark(0.4, 0.4, new ImageIcon(testImage));
 huntDisplay.addPoint(point1);
 huntDisplay.addPoint(point2);
 huntDisplay.addLandmark(landmark1);
 huntDisplay.addLandmark(landmark2);
 huntDisplay.paintComponent(testImage.getGraphics());
 assertColor(Color.BLACK, testImage, 100, 100);
 assertColor(Color.GREEN, testImage, 150, 150);
 assertColor(Color.BLACK, testImage, 200, 200);
 assertImage(testImage, landmark1.getIcon(), 80, 80);
 assertImage(testImage, landmark2.getIcon(), 160, 160);
 }

 private void assertColor(Color expectedColor, BufferedImage image, int x, int y) {
 Color actualColor = new Color(ImageUtils.getRGB(image, x, y));
 assertEquals(expectedColor, actualColor);
 }

 private void assertImage(BufferedImage testImage, Icon icon, int x, int y) {
 BufferedImage expectedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics graphics = expectedImage.getGraphics();
 icon.paintIcon(null, graphics, 0, 0);
 Graphics2D g2 = (Graphics2D) graphics;
 g2.dispose();
 for (int width = 0; width < icon.getIconWidth(); width++) {
 for (int height = 0; height < icon.getIconHeight(); height++) {
 int expectedPixel = expectedImage.getRGB(width, height);
 int actualPixel = ImageUtils.getRGB(testImage, x + width, y + height);
 assertEquals(expectedPixel, actualPixel);
 }
 }
 }

}