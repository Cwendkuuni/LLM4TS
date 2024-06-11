package Joshua.FoxHunt.Test;

import junit.framework.Assert;
import org.junit.Test;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Test
 public void testHuntDisplayConstructor() {
 huntDisplay = new HuntDisplay();
 Assert.assertNotNull(huntDisplay);
 }

 @Test
 public void testGetPreferredSize() {
 huntDisplay = new HuntDisplay();
 Assert.assertEquals(huntDisplay.getPreferredSize().width, 200);
 Assert.assertEquals(huntDisplay.getPreferredSize().height, 200);
 }

 @Test
 public void testGetMinimumSize() {
 huntDisplay = new HuntDisplay();
 Assert.assertEquals(huntDisplay.getMinimumSize().width, 50);
 Assert.assertEquals(huntDisplay.getMinimumSize().height, 50);
 }

 @Test
 public void testAddPoint() {
 huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 Assert.assertEquals(huntDisplay.points.size(), 1);
 }

 @Test(expected = NullPointerException.class)
 public void testAddPointNullPoint() {
 huntDisplay = new HuntDisplay();
 huntDisplay.addPoint(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointInvalidLatitude() {
 huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(-1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointInvalidLongitude() {
 huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(1.0, -1.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 }

 @Test
 public void testAddLandmark() {
 huntDisplay = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("path/to/image.jpg"));
 huntDisplay.addLandmark(landmark);
 Assert.assertEquals(huntDisplay.landmarks.size(), 1);
 }

 @Test(expected = NullPointerException.class)
 public void testAddLandmarkNullLandmark() {
 huntDisplay = new HuntDisplay();
 huntDisplay.addLandmark(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkInvalidLatitude() {
 huntDisplay = new HuntDisplay();
 Landmark landmark = new Landmark(-1.0, 2.0, new ImageIcon("path/to/image.jpg"));
 huntDisplay.addLandmark(landmark);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkInvalidLongitude() {
 huntDisplay = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, -2.0, new ImageIcon("path/to/image.jpg"));
 huntDisplay.addLandmark(landmark);
 }

 @Test
 public void testPaintComponent() {
 huntDisplay = new HuntDisplay();
 Graphics2D g = (Graphics2D) huntDisplay.createGraphics();
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("path/to/image.jpg"));
 huntDisplay.addPoint(trigPoint);
 huntDisplay.addLandmark(landmark);
 huntDisplay.paintComponent(g);
 // Verify the point and landmark have been drawn
 // Add assertions to check the drawing of points and landmarks
 g.dispose();
 }
}