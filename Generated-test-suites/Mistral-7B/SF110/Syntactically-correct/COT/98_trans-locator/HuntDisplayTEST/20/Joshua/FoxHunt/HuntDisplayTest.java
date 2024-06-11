package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 }

 @Test
 public void testHuntDisplayConstructor() {
 HuntDisplay huntDisplay = new HuntDisplay();
 assertNotNull(huntDisplay);
 }

 @Test
 public void testGetPreferredSize() {
 Dimension preferredSize = new Dimension(200, 200);
 assertEquals(preferredSize, huntDisplay.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 Dimension minimumSize = new Dimension(50, 50);
 assertEquals(minimumSize, huntDisplay.getMinimumSize());
 }

 @Test
 public void testAddPoint() {
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 3.0, true);
 huntDisplay.addPoint(trigPoint);
 assertEquals(1, huntDisplay.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointNullPoint() {
 huntDisplay.addPoint(null);
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(3.0, 4.0, new ImageIcon("icon.png"));
 huntDisplay.addLandMark(landmark);
 assertEquals(1, huntDisplay.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandMarkNullLandmark() {
 huntDisplay.addLandMark(null);
 }

 @Test
 public void testPaintComponent() {
 Graphics graphics = huntDisplay.createGraphics();
 huntDisplay.paintComponent(graphics);
 }

 @Test(expected = NullPointerException.class)
 public void testPaintComponentNullGraphics() {
 huntDisplay.paintComponent(null);
 }

 @Test
 public void testCalculateAverageCoordinates() {
 TrigPoint[] trigPoints = new TrigPoint[5];
 for (int i = 0; i < trigPoints.length; i++) {
 trigPoints[i] = new TrigPoint(i, i, i, false);
 huntDisplay.points.add(trigPoints[i]);
 }
 double trigPointAvgX = huntDisplay.calculateXAverage();
 double trigPointAvgY = huntDisplay.calculateYAverage();
 assertEquals(2.5, trigPointAvgX, 0.01);
 assertEquals(2.5, trigPointAvgY, 0.01);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetPoints() {
 huntDisplay.getPoints();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLandmarks() {
 huntDisplay.getLandmarks();
 }

 @Test
 public void testRepaint() {
 Graphics graphics = huntDisplay.createGraphics();
 huntDisplay.repaint();
 huntDisplay.paintComponent(graphics);
 }

 @Test
 public void testGetBackground() {
 assertEquals(Color.WHITE, huntDisplay.getBackground());
 }
}