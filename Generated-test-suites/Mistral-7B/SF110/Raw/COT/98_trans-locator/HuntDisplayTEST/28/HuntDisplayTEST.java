package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;
 private Vector<TrigPoint> points;
 private Vector<Landmark> landmarks;

 @Before
 public void setUp() {
 this.display = new HuntDisplay();
 this.points = new Vector<>();
 this.landmarks = new Vector<>();
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
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));
 display.addLandMark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandMarkWithNullLandmark() {
 display.addLandMark(null);
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 Graphics g = mock(Graphics.class);
 display.paintComponent(g);
 assertNull(g.getColor());
 }

 @Test
 public void testPaintComponentWithPointsButNoLandmarks() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 Graphics g = mock(Graphics.class);
 display.paintComponent(g);
 assertEquals(Color.BLACK, g.getColor());
 }

 @Test
 public void testPaintComponentWithLandmarksButNoPoints() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));
 display.addLandMark(landmark);
 Graphics g = mock(Graphics.class);
 display.paintComponent(g);
 assertEquals(Color.BLACK, g.getColor());
 }

 @Test
 public void testPaintComponentWithBothPointsAndLandmarks() {
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(2.0, 3.0, 0.0, false);
 Landmark landmark = new Landmark(1.5, 2.5, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));

 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandMark(landmark);

 Graphics g = mock(Graphics.class);
 display.paintComponent(g);

 assertEquals(Color.BLACK, g.getColor()); // Point color
 assertEquals(Color.GREEN, g.getColor()); // Fox point color
 }

}