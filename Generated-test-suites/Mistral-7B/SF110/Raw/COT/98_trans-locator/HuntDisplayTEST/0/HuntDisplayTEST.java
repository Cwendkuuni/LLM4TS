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
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, true);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullPoint() {
 display.addPoint(null);
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));
 display.addLandMark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandMarkWithNullLandmark() {
 display.addLandMark(null);
 }

 @Test
 public void testPaintComponentWithEmptyList() {
 Graphics g = display.createGraphics();
 display.paintComponent(g);
 assertTrue(display.getWidth() * display.getHeight() > 0);
 }

 @Test
 public void testPaintComponentWithOnePoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 Graphics g = display.createGraphics();
 display.paintComponent(g);
 int width = display.getWidth();
 int height = display.getHeight();
 int x = (int)((point.getX() + 0.5 - display.getWidth() / 2) * width);
 int y = (int)((point.getY() + 0.5 - display.getHeight() / 2) * height);
 assertTrue(g.getColor().equals(Color.BLACK));
 g.fillOval(x, y, 5, 5);
 }

 @Test
 public void testPaintComponentWithOneFoxAndOneLandmark() {
 TrigPoint fox = new TrigPoint(1.0, 2.0, 0.0, true);
 Landmark landmark = new Landmark(3.0, 4.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));
 display.addPoint(fox);
 display.addLandMark(landmark);
 Graphics g = display.createGraphics();
 display.paintComponent(g);
 int width = display.getWidth();
 int height = display.getHeight();
 int xFox = (int)((fox.getX() + 0.5 - display.getWidth() / 2) * width);
 int yFox = (int)((fox.getY() + 0.5 - display.getHeight() / 2) * height);
 int xLandmark = (int)((landmark.getLon() + 0.5 - display.getWidth() / 2) * width);
 int yLandmark = (int)((landmark.getLat() + 0.5 - display.getHeight() / 2) * height);
 assertTrue(g.getColorAt(xFox, yFox).equals(Color.GREEN));
 assertTrue(g.getColorAt(xLandmark, yLandmark).equals(Color.LIGHT_GRAY));
 }

}