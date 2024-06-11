package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Before
 public void setUp() {
 this.display = new HuntDisplay();
 }

 @Test
 public void testGetPreferredSize() {
 Dimension expected = new Dimension(200, 200);
 Dimension actual = this.display.getPreferredSize();
 assertEquals(expected.getWidth(), actual.getWidth());
 assertEquals(expected.getHeight(), actual.getHeight());
 }

 @Test
 public void testGetMinimumSize() {
 Dimension expected = new Dimension(50, 50);
 Dimension actual = this.display.getMinimumSize();
 assertEquals(expected.getWidth(), actual.getWidth());
 assertEquals(expected.getHeight(), actual.getHeight());
 }

 @Test
 public void testAddPoint_ValidTrigPoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 this.display.addPoint(point);
 Vector<TrigPoint> expectedPoints = new Vector<TrigPoint>(Arrays.asList(point));
 Vector<TrigPoint> actualPoints = this.display.points;
 assertEquals(expectedPoints, actualPoints);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPoint_NullTrigPoint() {
 this.display.addPoint(null);
 }

 @Test
 public void testAddLandmark_ValidLandmark() {
 Landmark landmark = new Landmark(3.0, 4.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 this.display.addLandmark(landmark);
 Vector<Landmark> expectedLandmarks = new Vector<Landmark>(Arrays.asList(landmark));
 Vector<Landmark> actualLandmarks = this.display.landmarks;
 assertEquals(expectedLandmarks, actualLandmarks);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmark_NullLandmark() {
 this.display.addLandmark(null);
 }

 @Test
 public void testPaintComponent_Empty() {
 Graphics2D g = this.createMockGraphics();
 this.display.paintComponent(g);
 assertFalse(g.getColor().equals(Color.GREEN));
 }

 @Test
 public void testPaintComponent_OnePoint() {
 this.display.addPoint(new TrigPoint(0.5, 0.5, 0.0, false));
 Graphics2D g = this.createMockGraphics();
 this.display.paintComponent(g);
 int pointX = this.display.getWidth() / 2;
 int pointY = this.display.getHeight() / 2;
 assertTrue(g.getColor().equals(Color.BLACK));
 g.fillOval(pointX - 2, pointY - 2, 4, 4);
 }

 @Test
 public void testPaintComponent_OneLandmark() {
 Landmark landmark = new Landmark(0.5, 0.5, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 this.display.addLandmark(landmark);
 Graphics2D g = this.createMockGraphics();
 this.display.paintComponent(g);
 int landmarkX = this.display.getWidth() / 2;
 int landmarkY = this.display.getHeight() / 2;
 BufferedImage image = ((ImageIcon) landmark.getIcon()).getImage();
 assertTrue(g.getColor().equals(Color.getColor(image.getRGB(0, 0))));
 g.drawImage(image, landmarkX - 5, landmarkY - 5, 10, 10, null);
 }

 @Test
 public void testPaintComponent_MultiplePointsAndLandmarks() {
 TrigPoint point1 = new TrigPoint(0.25, 0.25, 0.0, false);
 TrigPoint point2 = new TrigPoint(0.75, 0.75, 0.0, false);
 Landmark landmark = new Landmark(0.5, 0.5, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 this.display.addPoint(point1);
 this.display.addPoint(point2);
 this.display.addLandmark(landmark);
 Graphics2D g = this.createMockGraphics();
 this.display.paintComponent(g);
 int point1X = (int) (point1.getX() * this.display.getWidth());
 int point1Y = (int) (point1.getY() * this.display.getHeight());
 int point2X = (int) (point2.getX() * this.display.getWidth());
 int point2Y = (int) (point2.getY() * this.display.getHeight());
 int landmarkX = (int) (landmark.getLon() * this.display.getWidth());
 int landmarkY = (int) (landmark.getLat() * this.display.getHeight());
 assertTrue(g.getColor().equals(Color.BLACK));
 g.fillOval(point1X - 2, point1Y - 2, 4, 4);
 g.fillOval(point2X - 2, point2Y - 2, 4, 4);
 g.setColor(Color.getColor(image.getRGB(0, 0)));
 g.drawImage(image, landmarkX - 5, landmarkY - 5, 10, 10, null);
 }

 @Test
 public void testPaintComponent_FoxPoint() {
 TrigPoint point = new TrigPoint(0.5, 0.5, 0.0, true);
 this.display.addPoint(point);
 Graphics2D g = this.createMockGraphics();
 this.display.paintComponent(g);
 int pointX = (int) (point.getX() * this.display.getWidth());
 int pointY = (int) (point.getY() * this.display.getHeight());
 assertTrue(g.getColor().equals(Color.GREEN));
 g.fillOval(pointX - 2, pointY - 2, 4, 4);
 }

 private Graphics2D createMockGraphics() {
 Graphics2D g = mock(Graphics2D.class);
 return g;
 }
}