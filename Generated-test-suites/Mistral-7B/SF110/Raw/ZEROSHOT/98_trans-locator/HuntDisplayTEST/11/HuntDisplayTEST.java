package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.*;
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
 assertNotNull(huntDisplay);
 }

 @Test
 public void testGetPreferredSize() {
 huntDisplay = new HuntDisplay();
 assertEquals(200, huntDisplay.getPreferredSize().getWidth());
 assertEquals(200, huntDisplay.getPreferredSize().getHeight());
 }

 @Test
 public void testGetMinimumSize() {
 huntDisplay = new HuntDisplay();
 assertEquals(50, huntDisplay.getMinimumSize().getWidth());
 assertEquals(50, huntDisplay.getMinimumSize().getHeight());
 }

 @Test
 public void testAddPoint() {
 huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, true);
 huntDisplay.addPoint(trigPoint);
 Vector<TrigPoint> expectedPoints = new Vector<>();
 expectedPoints.add(trigPoint);
 assertEquals(expectedPoints, huntDisplay.points);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullTrigPoint() {
 huntDisplay = new HuntDisplay();
 huntDisplay.addPoint(null);
 }

 @Test
 public void testAddLandmark() {
 huntDisplay = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
 huntDisplay.addLandmark(landmark);
 Vector<Landmark> expectedLandmarks = new Vector<>();
 expectedLandmarks.add(landmark);
 assertEquals(expectedLandmarks, huntDisplay.landmarks);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullLandmark() {
 huntDisplay = new HuntDisplay();
 huntDisplay.addLandmark(null);
 }

 @Test
 public void testPaintComponent() {
 huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, true);
 Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
 huntDisplay.addPoint(trigPoint);
 huntDisplay.addLandmark(landmark);
 Graphics2D graphics = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB).createGraphics();
 huntDisplay.paintComponent(graphics);
 assertNotNull(graphics);
 }

 @Test
 public void testPaintComponentWithEmptyDisplay() {
 huntDisplay = new HuntDisplay();
 Graphics2D graphics = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB).createGraphics();
 huntDisplay.paintComponent(graphics);
 assertNotNull(graphics);
 }
}