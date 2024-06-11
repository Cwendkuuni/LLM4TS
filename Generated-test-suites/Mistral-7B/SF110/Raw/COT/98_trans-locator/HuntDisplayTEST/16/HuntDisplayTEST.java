package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

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
 display = new HuntDisplay();
 points = new Vector<>();
 landmarks = new Vector<>();
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
 points.add(point);
 assertEquals(1, points.size());
 assertEquals(1, display.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointNullPoint() {
 display.addPoint(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/icon.png")));
 display.addLandMark(landmark);
 landmarks.add(landmark);
 assertEquals(1, landmarks.size());
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandMarkNullLandmark() {
 display.addLandMark(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 Graphics g = SwingUtilities.createImageGraphics(new JFrame().getContentPane());
 display.paintComponent(g);
 assertTrue(g.getColor().equals(Color.WHITE));
 }

 @Test
 public void testPaintComponentWithPoints() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 points.add(point);
 display.addPoint(point);
 TrigPoint fox = new TrigPoint(3.0, 4.0, 0.0, true);
 points.add(fox);
 display.addPoint(fox);
 display.repaint();
 graphicsTest(points.size(), Color.BLACK, Color.GREEN);
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/icon.png")));
 landmarks.add(landmark);
 display.addLandMark(landmark);
 display.repaint();
 graphicsTest(1, Color.BLACK, null);
 graphicsTest(1, null, new ImageIcon(getClass().getResource("/icon.png")));
 }

 @Test
 public void testPaintComponentWithBothPointsAndLandmarks() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 points.add(point);
 display.addPoint(point);
 TrigPoint fox = new TrigPoint(3.0, 4.0, 0.0, true);
 points.add(fox);
 display.addPoint(fox);
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/icon.png")));
 landmarks.add(landmark);
 display.addLandMark(landmark);
 display.repaint();
 graphicsTest(points.size(), Color.BLACK, Color.GREEN);
 graphicsTest(landmarks.size(), Color.BLACK, null);
 graphicsTest(points.size() + landmarks.size(), Color.BLACK, null);
 }

 private void graphicsTest(int expectedNumberOfDrawings, Color expectedPointColor, Color expectedLandmarkColor) {
 Graphics g = SwingUtilities.createImageGraphics(new JFrame().getContentPane());
 display.paintComponent(g);
 int numberOfDrawings = 0;
 Color pointColor = g.getColor();
 Color landmarkColor = null;
 if (expectedLandmarkColor != null) {
 for (int i = 0; i < display.landmarks.size(); i++) {
 Landmark l = display.landmarks.elementAt(i);
 final int X = (int)((l.getLon() + display.trigPointAvgX) * display.getWidth());
 final int Y = (int)((l.getLat() + display.trigPointAvgY) * display.getHeight());
 final Image image = l.getIcon().getImage();
 g.drawImage(image, X, Y, 10, 10, display.getBackground(), display);
 if (g.getColor().equals(expectedLandmarkColor)) {
 landmarkColor = expectedLandmarkColor;
 }
 else {
 fail("Landmark color does not match expected color");
 }
 numberOfDrawings++;
 }
 }
 for (int i = 0; i < display.points.size(); i++) {
 TrigPoint p = display.points.elementAt(i);
 final int X = (int)((p.getX() + display.trigPointAvgX) * display.getWidth());
 final int Y = (int)((p.getY() + display.trigPointAvgY) * display.getHeight());
 if (p.isFox()) {
 if (!expectedPointColor.equals(Color.GREEN)) {
 fail("Fox point color does not match expected color");
 }
 }
 else {
 if (!expectedPointColor.equals(Color.BLACK)) {
 fail("Normal point color does not match expected color");
 }
 }
 g.fillOval(X, Y, 5, 5);
 numberOfDrawings++;
 }
 assertEquals(expectedNumberOfDrawings, numberOfDrawings);
 assertEquals(expectedPointColor, pointColor);
 if (expectedLandmarkColor != null) {
 assertEquals(expectedLandmarkColor, landmarkColor);
 }
 }
}