package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Before
 public void setup() {
 display = new HuntDisplay();
 }

 @Test
 public void testHuntDisplayConstruction() {
 HuntDisplay hd = new HuntDisplay();
 assertNotNull(hd);
 }

 @Test
 public void testSizeAfterConstruction() {
 assertEquals(new Dimension(200, 200), display.getPreferredSize());
 assertEquals(new Dimension(50, 50), display.getMinimumSize());
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 3.0, true);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointNullPoint() {
 display.addPoint(null);
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("landmark.png"));
 display.addLandmark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkNullLandmark() {
 display.addLandmark(null);
 }

 @Test
 public void testDrawPoints() {
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 3.0, true);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 5.0, false);
 display.addPoint(point1);
 display.addPoint(point2);
 BufferedImage image = new BufferedImage(display.getWidth(), display.getHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 display.paintComponent(g);
 int x1 = (int)((point1.getX() + 0.5 - display.points.firstElement().getX()) * display.getWidth());
 int y1 = (int)((point1.getY() + 0.5 - display.points.firstElement().getY()) * display.getHeight());
 int x2 = (int)((point2.getX() + 0.5 - display.points.firstElement().getX()) * display.getWidth());
 int y2 = (int)((point2.getY() + 0.5 - display.points.firstElement().getY()) * display.getHeight());
 assertTrue(image.getRGB(x1, y1) == point1.isFox() ? Color.GREEN.getRGB() : Color.BLACK.getRGB());
 assertTrue(image.getRGB(x2, y2) == Color.BLACK.getRGB());
 }

 @Test
 public void testDrawLandmarks() {
 Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon("landmark1.png"));
 Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon("landmark2.png"));
 display.addLandmark(landmark1);
 display.addLandmark(landmark2);
 BufferedImage image = new BufferedImage(display.getWidth(), display.getHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 display.paintComponent(g);
 int x1 = (int)((landmark1.getLon() + 0.5 - display.points.firstElement().getX()) * display.getWidth());
 int y1 = (int)((landmark1.getLat() + 0.5 - display.points.firstElement().getY()) * display.getHeight());
 int x2 = (int)((landmark2.getLon() + 0.5 - display.points.firstElement().getX()) * display.getWidth());
 int y2 = (int)((landmark2.getLat() + 0.5 - display.points.firstElement().getY()) * display.getHeight());
 BufferedImage landmarkImage1 = ImageIO.read(getClass().getResource("/landmark1.png"));
 BufferedImage landmarkImage2 = ImageIO.read(getClass().getResource("/landmark2.png"));
 for (int w = 0; w < landmarkImage1.getWidth(); w++) {
 for (int h = 0; h < landmarkImage1.getHeight(); h++) {
 int rgb1 = landmarkImage1.getRGB(w, h);
 int rgb2 = image.getRGB(x1 + w, y1 + h);
 assertEquals(rgb1, rgb2);
 }
 }
 for (int w = 0; w < landmarkImage2.getWidth(); w++) {
 for (int h = 0; h < landmarkImage2.getHeight(); h++) {
 int rgb1 = landmarkImage2.getRGB(w, h);
 int rgb2 = image.getRGB(x2 + w, y2 + h);
 assertEquals(rgb1, rgb2);
 }
 }
 }
}