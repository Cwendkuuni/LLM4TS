package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Test
 public void testHuntDisplayConstructor() {
 huntDisplay = new HuntDisplay();
 assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
 assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
 }

 @Test
 public void testAddPoint() {
 huntDisplay = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(point);
 huntDisplay.repaint();

 assertEquals(1, huntDisplay.points.size());

 Graphics graphics = huntDisplay.createGraphics();
 BufferedImage image = new BufferedImage(huntDisplay.getWidth(), huntDisplay.getHeight(), BufferedImage.TYPE_INT_RGB);
 graphics.dispose();
 graphics = image.getGraphics();
 huntDisplay.paintComponent(graphics);
 graphics.dispose();

 int x = (int)((point.getX() + 0.5) * huntDisplay.getWidth());
 int y = (int)((point.getY() + 0.5) * huntDisplay.getHeight());

 assertEquals(Color.BLACK.getRGB(), image.getRGB(x, y));
 }

 @Test
 public void testAddLandmark() {
 huntDisplay = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("test.jpg"));
 huntDisplay.addLandmark(landmark);
 huntDisplay.repaint();

 assertEquals(0, huntDisplay.points.size());
 assertEquals(1, huntDisplay.landmarks.size());

 Graphics graphics = huntDisplay.createGraphics();
 BufferedImage image = new BufferedImage(huntDisplay.getWidth(), huntDisplay.getHeight(), BufferedImage.TYPE_INT_RGB);
 graphics.dispose();
 graphics = image.getGraphics();
 huntDisplay.paintComponent(graphics);
 graphics.dispose();

 int x = (int)((landmark.getLon() + 0.5) * huntDisplay.getWidth());
 int y = (int)((landmark.getLat() + 0.5) * huntDisplay.getHeight());

 BufferedImage iconImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
 graphics = iconImage.getGraphics();
 landmark.getIcon().paintIcon(null, graphics);
 graphics.dispose();

 int expectedIconColor1 = iconImage.getRGB(0, 0);
 int expectedIconColor2 = iconImage.getRGB(0, 9);
 int expectedIconColor3 = iconImage.getRGB(9, 0);
 int expectedIconColor4 = iconImage.getRGB(9, 9);

 int actualIconColor1 = image.getRGB(x - 5, y - 5);
 int actualIconColor2 = image.getRGB(x, y - 5);
 int actualIconColor3 = image.getRGB(x + 5, y - 5);
 int actualIconColor4 = image.getRGB(x, y);

 assertEquals(expectedIconColor1, actualIconColor1);
 assertEquals(expectedIconColor2, actualIconColor2);
 assertEquals(expectedIconColor3, actualIconColor3);
 assertEquals(expectedIconColor4, actualIconColor4);
 }

 @Test
 public void testAddPointsAndLandmarks() {
 huntDisplay = new HuntDisplay();

 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(point1);

 Landmark landmark1 = new Landmark(3.0, 4.0, new ImageIcon("test.jpg"));
 huntDisplay.addLandmark(landmark1);

 assertEquals(1, huntDisplay.points.size());
 assertEquals(1, huntDisplay.landmarks.size());

 Graphics graphics = huntDisplay.createGraphics();
 BufferedImage image = new BufferedImage(huntDisplay.getWidth(), huntDisplay.getHeight(), BufferedImage.TYPE_INT_RGB);
 graphics.dispose();
 graphics = image.getGraphics();
 huntDisplay.paintComponent(graphics);
 graphics.dispose();

 int x1 = (int)((point1.getX() + 0.5) * huntDisplay.getWidth());
 int y1 = (int)((point1.getY() + 0.5) * huntDisplay.getHeight());
 int x2 = (int)((landmark1.getLon() + 0.5) * huntDisplay.getWidth());
 int y2 = (int)((landmark1.getLat() + 0.5) * huntDisplay.getHeight());

 assertEquals(Color.BLACK.getRGB(), image.getRGB(x1, y1));
 assertEquals(Color.BLACK.getRGB(), image.getRGB(x2, y2));
 }

 @Test
 public void testAddMultipleLandmarks() {
 huntDisplay = new HuntDisplay();

 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(point1);

 Landmark landmark1 = new Landmark(3.0, 4.0, new ImageIcon("test.jpg"));
 huntDisplay.addLandmark(landmark1);

 Landmark landmark2 = new Landmark(6.0, 8.0, new ImageIcon("test.jpg"));
 huntDisplay.addLandmark(landmark2);

 assertEquals(1, huntDisplay.points.size());
 assertEquals(2, huntDisplay.landmarks.size());

 Graphics graphics = huntDisplay.createGraphics();
 BufferedImage image = new BufferedImage(huntDisplay.getWidth(), huntDisplay.getHeight(), BufferedImage.TYPE_INT_RGB);
 graphics.dispose();
 graphics = image.getGraphics();
 huntDisplay.paintComponent(graphics);
 graphics.dispose();

 int x1 = (int)((point1.getX() + 0.5) * huntDisplay.getWidth());
 int y1 = (int)((point1.getY() + 0.5) * huntDisplay.getHeight());
 int x2 = (int)((landmark1.getLon() + 0.5) * huntDisplay.getWidth());
 int y2 = (int)((landmark1.getLat() + 0.5) * huntDisplay.getHeight());
 int x3 = (int)((landmark2.getLon() + 0.5) * huntDisplay.getWidth());
 int y3 = (int)((landmark2.getLat() + 0.5) * huntDisplay.getHeight());

 assertEquals(Color.BLACK.getRGB(), image.getRGB(x1, y1));
 assertEquals(Color.BLACK.getRGB(), image.getRGB(x2, y2));
 assertEquals(Color.BLACK.getRGB(), image.getRGB(x3, y3));
 }
}