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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;
 private final static double EPSILON = 0.01;

 @Before
 public void setUp() {
 display = new HuntDisplay();
 }

 @After
 public void tearDown() {
 display = null;
 }

 @Test
 public void testDefaultValues() {
 assertEquals(50, display.getMinSize().width);
 assertEquals(50, display.getMinSize().height);
 assertEquals(200, display.getPrefSize().width);
 assertEquals(200, display.getPrefSize().height);
 assertEquals(Color.WHITE, display.getBackground());
 assertTrue(display.points.isEmpty());
 assertTrue(display.landmarks.isEmpty());
 }

 @Test
 public void testAddPoint() {
 TrigPoint trigPoint = new TrigPoint(5.0, 10.0, 45.0, true);
 display.addPoint(trigPoint);
 assertEquals(1, display.points.size());
 assertEquals(trigPoint.getX(), display.getX(0), EPSILON);
 assertEquals(trigPoint.getY(), display.getY(0), EPSILON);
 assertEquals(trigPoint.getTheta(), display.getTheta(0), EPSILON);
 assertEquals(trigPoint.isFox(), display.isFox(0));
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(5.0, 10.0, new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)));
 display.addLandmark(landmark);
 assertEquals(1, display.landmarks.size());
 assertEquals(landmark.getLat(), display.getLat(0), EPSILON);
 assertEquals(landmark.getLon(), display.getLon(0), EPSILON);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullIcon() {
 Landmark landmark = new Landmark(5.0, 10.0, null);
 display.addLandmark(landmark);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBounds() {
 TrigPoint trigPoint = new TrigPoint(5.0, 10.0, 45.0, true);
 display.addPoint(trigPoint);
 assertEquals(1, display.points.size());
 try {
 display.getX(-1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testPaintComponent() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.add(display);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(() -> {
 Graphics2D g = display.createGraphics();
 final int width = display.getWidth();
 final int height = display.getHeight();
 final int numPoints = 10;
 final TrigPoint[] points = new TrigPoint[numPoints];
 for (int i = 0; i < numPoints; ++i) {
 points[i] = new TrigPoint((float)Math.random(), (float)Math.random(), (float)Math.random(), false);
 display.addPoint(points[i]);
 }
 display.repaint();
 int x, y;
 for (int i = 0; i < numPoints; ++i) {
 x = (int)Math.round(points[i].getX() * width);
 y = (int)Math.round(points[i].getY() * height);
 assertTrue("Point [" + i + "] not painted at expected location", g.getColor().equals(points[i].isFox() ? Color.GREEN : Color.BLACK) && g.fillOval(x, y, 5, 5).width > 0);
 }
 frame.dispose();
 });
 }
}