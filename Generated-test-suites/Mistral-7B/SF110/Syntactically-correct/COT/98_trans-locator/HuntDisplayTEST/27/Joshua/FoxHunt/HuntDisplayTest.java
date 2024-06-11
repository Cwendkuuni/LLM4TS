package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
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
 assertNotNull(huntDisplay);
 }

 @Test
 public void testGetPreferredSize() {
 huntDisplay = new HuntDisplay();
 Dimension expectedSize = new Dimension(200, 200);
 Dimension actualSize = huntDisplay.getPreferredSize();
 assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testGetMinimumSize() {
 huntDisplay = new HuntDisplay();
 Dimension expectedSize = new Dimension(50, 50);
 Dimension actualSize = huntDisplay.getMinimumSize();
 assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testAddPoint() {
 huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(0.0, 0.0, 0.0, true);
 huntDisplay.addPoint(trigPoint);
 assertEquals(1, huntDisplay.points.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddPointNullPoint() {
 huntDisplay = new HuntDisplay();
 huntDisplay.addPoint(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointInvalidTheta() {
 huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(0.0, 0.0, Math.PI, true);
 huntDisplay.addPoint(trigPoint);
 }

 @Test
 public void testAddLandMark() {
 huntDisplay = new HuntDisplay();
 Landmark landmark = new Landmark(0.0, 0.0, new ImageIcon(getClass().getResource("/icon.png")));
 huntDisplay.addLandMark(landmark);
 assertEquals(1, huntDisplay.landmarks.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddLandMarkNullLandmark() {
 huntDisplay = new HuntDisplay();
 huntDisplay.addLandMark(null);
 }

 @Test
 public void testPaintComponent() {
 huntDisplay = new HuntDisplay();
 Graphics g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 assertNotNull(g);
 }

 @Test(expected = NullPointerException.class)
 public void testPaintComponentNullGraphics() {
 huntDisplay = new HuntDisplay();
 huntDisplay.paintComponent(null);
 }

 @Test
 public void testAddPointAndLandMark() {
 huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(0.0, 0.0, 0.0, true);
 Landmark landmark = new Landmark(0.0, 0.0, new ImageIcon(getClass().getResource("/icon.png")));
 huntDisplay.addPoint(trigPoint);
 huntDisplay.addLandMark(landmark);

 assertEquals(2, huntDisplay.points.size());
 assertEquals(1, huntDisplay.landmarks.size());
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 huntDisplay = new HuntDisplay();
 Vector<TrigPoint> points = new Vector<>();
 points.add(new TrigPoint(0.0, 0.0, 0.0, true));
 points.add(new TrigPoint(1.0, 1.0, 0.0, false));
 huntDisplay.points = points;

 Landmark landmark = new Landmark(0.5, 0.5, new ImageIcon(getClass().getResource("/icon.png")));
 huntDisplay.landmarks = new Vector<>(Arrays.asList(landmark));

 Graphics g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);

 Point expectedFoxPoint = new Point((int)(0.5 * huntDisplay.getWidth()), (int)(0.5 * huntDisplay.getHeight()));
 Point expectedLandmarkPoint = new Point((int)(0.5 * huntDisplay.getWidth()), (int)(0.5 * huntDisplay.getHeight()));

 Point foxPoint = new Point();
 Point landmarkPoint = new Point();

 g.getTransform().translate(huntDisplay.getWidth() / 2, huntDisplay.getHeight() / 2);
 Point mousePoint = new Point(0, 0);
 g.transformPoint(mousePoint);
 g.getGraphicsConfiguration().translate(-mousePoint.x, -mousePoint.y);
 g.getGraphicsConfiguration().translate(-huntDisplay.getWidth() / 2, -huntDisplay.getHeight() / 2);

 g.getGraphicsConfig().getDevice().drawString("X: " + mousePoint.x, 10, 20);
 g.getGraphicsConfig().getDevice().drawString("Y: " + mousePoint.y, 10, 40);

 g.getGraphics().getTransform().getTranslation(foxPoint.x, foxPoint.y);
 g.getGraphics().getTransform().inverse();
 g.getGraphics().getTransform().translate(-huntDisplay.getWidth() / 2, -huntDisplay.getHeight() / 2);

 g.getGraphics().getGraphicsConfig().getDevice().drawString("Fox X: " + foxPoint.x, 10, 60);
 g.getGraphics().getGraphicsConfig().getDevice().drawString("Fox Y: " + foxPoint.y, 10, 80);

 g.getGraphics().getTransform().getTranslation(landmarkPoint.x, landmarkPoint.y);
 g.getGraphics().getTransform().inverse();
 g.getGraphics().getTransform().translate(-huntDisplay.getWidth() / 2, -huntDisplay.getHeight() / 2);

 g.getGraphics().getGraphicsConfig().getDevice().drawString("Landmark X: " + landmarkPoint.x, 10, 100);
 g.getGraphics().getGraphicsConfig().getDevice().drawString("Landmark Y: " + landmarkPoint.y, 10, 120);

 assertNotNull(g);
 assertNotNull(foxPoint);
 assertNotNull(landmarkPoint);
 assertTrue(new Point(expectedFoxPoint.x, expectedFoxPoint.y).equals(foxPoint));
 assertTrue(new Point(expectedLandmarkPoint.x, expectedLandmarkPoint.y).equals(landmarkPoint));
 }
}