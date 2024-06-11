package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

public class HuntDisplayTest {

 private HuntDisplay display;
 private Vector<TrigPoint> points;
 private Vector<Landmark> landmarks;

 @Before
 public void setUp() {
 display = new HuntDisplay();
 points = new Vector<>();
 landmarks = new Vector<>();
 }

 @Test
 public void testHuntDisplayConstruction() {
 HuntDisplay hd = new HuntDisplay();
 assertEquals(new Dimension(200, 200), hd.getPreferredSize());
 assertEquals(new Dimension(50, 50), hd.getMinimumSize());
 }

 @Test
 public void testAddPoint() {
 points.add(new TrigPoint(1.0, 2.0, 0.0, false));
 display.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
 assertEquals(1, display.points.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPointNullPoint() {
 display.addPoint(null);
 }

 @Test
 public void testAddLandMark() {
 BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
 landmarks.add(new Landmark(1.0, 2.0, new ImageIcon(image), "testIcon"));
 display.addLandMark(new Landmark(1.0, 2.0, image, "testIcon"));
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddLandMarkNullLandMark() {
 display.addLandMark(null);
 }

 @Test
 public void testPaintComponent() {
 Graphics g = display.createGraphics();
 display.paintComponent(g);
 assertNotNull(g);
 }

 @Test
 public void testGetBackground() {
 assertEquals(Color.WHITE, display.getBackground());
 }

 @Test
 public void testGetForeground() {
 assertEquals(Color.BLACK, display.getForeground());
 }

 @After
 public void tearDown() {
 display = null;
 points = null;
 landmarks = null;
 }

 @Test
 public void testCalculateAverageCoordinates() {
 points.add(new TrigPoint(1.0, 2.0, 0.0, false));
 points.add(new TrigPoint(3.0, 4.0, 0.0, false));
 double trigPointAvgX = display.calculateAverageX(points);
 double trigPointAvgY = display.calculateAverageY(points);
 assertEquals(2.0, trigPointAvgX, 0.01);
 assertEquals(3.0, trigPointAvgY, 0.01);
 }

 @Test
 public void testCalculateAverageCoordinatesEmptyVector() {
 double trigPointAvgX = display.calculateAverageX(points);
 double trigPointAvgY = display.calculateAverageY(points);
 assertEquals(0.0, trigPointAvgX, 0.01);
 assertEquals(0.0, trigPointAvgY, 0.01);
 }
}