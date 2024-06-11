package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

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
 HuntDisplay huntDisplay = new HuntDisplay();
 Dimension preferredSize = huntDisplay.getPreferredSize();
 assertEquals(new Dimension(200, 200), preferredSize);
 }

 @Test
 public void testGetMinimumSize() {
 HuntDisplay huntDisplay = new HuntDisplay();
 Dimension minimumSize = huntDisplay.getMinimumSize();
 assertEquals(new Dimension(50, 50), minimumSize);
 }

 @Test
 public void testAddPoint() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 3.0, true);
 huntDisplay.addPoint(point);
 assertEquals(1, huntDisplay.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullPoint() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint point = null;
 huntDisplay.addPoint(point);
 fail("Should have thrown an IllegalArgumentException with null point");
 }

 @Test
 public void testAddLandMark() {
 HuntDisplay huntDisplay = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/icon.png")));
 huntDisplay.addLandMark(landmark);
 assertEquals(1, huntDisplay.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandMarkWithNullLandmark() {
 HuntDisplay huntDisplay = new HuntDisplay();
 Landmark landmark = null;
 huntDisplay.addLandMark(landmark);
 fail("Should have thrown an IllegalArgumentException with null landmark");
 }

 @Test
 public void testPaintComponent() {
 HuntDisplay huntDisplay = new HuntDisplay();
 Graphics graphics = huntDisplay.createGraphics();
 huntDisplay.paintComponent(graphics);
 assertTrue(huntDisplay.getWidth() > 0 && huntDisplay.getHeight() > 0);
 }

 @Test(expected = NullPointerException.class)
 public void testPaintComponentWithNullGraphics() {
 HuntDisplay huntDisplay = new HuntDisplay();
 Graphics graphics = null;
 huntDisplay.paintComponent(graphics);
 fail("Should have thrown a NullPointerException with null graphics");
 }

 @Test
 public void testAddPointsAndLandMarks() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 3.0, true);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 3.0, false);
 Landmark landmark1 = new Landmark(1.5, 2.5, new ImageIcon(getClass().getResource("/icon.png")));
 huntDisplay.addPoint(point1);
 huntDisplay.addPoint(point2);
 huntDisplay.addLandMark(landmark1);
 int xAdd = 0.5 - ((point1.getX() + point2.getX()) / 2.0);
 int yAdd = 0.5 - ((point1.getY() + point2.getY()) / 2.0);
 ArrayList<TrigPoint> expectedPoints = new ArrayList<TrigPoint>();
 expectedPoints.add(new TrigPoint(point1.getX() + xAdd, point1.getY() + yAdd, point1.getTheta(), point1.isFox()));
 expectedPoints.add(new TrigPoint(point2.getX() + xAdd, point2.getY() + yAdd, point2.getTheta(), point2.isFox()));
 ArrayList<Landmark> expectedLandmarks = new ArrayList<Landmark>();
 expectedLandmarks.add(new Landmark(landmark1.getLon() + xAdd, landmark1.getLat() + yAdd, landmark1.getIcon()));
 assertEquals(expectedPoints.size(), huntDisplay.points.size());
 assertEquals(expectedLandmarks.size(), huntDisplay.landmarks.size());
 for (int i = 0; i < huntDisplay.points.size(); i++) {
 TrigPoint point = (TrigPoint) huntDisplay.points.get(i);
 assertEquals(expectedPoints.get(i).getX(), point.getX(), 0.01);
 assertEquals(expectedPoints.get(i).getY(), point.getY(), 0.01);
 assertEquals(expectedPoints.get(i).getTheta(), point.getTheta(), 0.01);
 assertEquals(expectedPoints.get(i).isFox(), point.isFox());
 }
 for (int i = 0; i < huntDisplay.landmarks.size(); i++) {
 Landmark landmark = (Landmark) huntDisplay.landmarks.get(i);
 ImageIcon icon = (ImageIcon) landmark.getIcon();
 assertEquals(expectedLandmarks.get(i).getLon(), landmark.getLon(), 0.01);
 assertEquals(expectedLandmarks.get(i).getLat(), landmark.getLat(), 0.01);
 assertEquals(icon.getImage(), landmark.getIcon().getImage());
 }
 }
}