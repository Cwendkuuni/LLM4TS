package Joshua.FoxHunt.Test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Vector;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 }

 @After
 public void tearDown() {
 huntDisplay = null;
 }

 @Test
 public void testAddPoint() {
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 int[] expectedPointsSize = {5, 5};
 BufferedImage expectedPointImage = ImageIO.read(getClass().getResource("/point.png"));
 int x = (int)(trigPoint.getX() * huntDisplay.getWidth());
 int y = (int)(trigPoint.getY() * huntDisplay.getHeight());
 int[] actualPointsSize = {5, 5};
 BufferedImage actualPointImage = huntDisplay.getImage();
 // Verify point was added and painted correctly
 assertEquals(expectedPointsSize[0], actualPointsSize[0]);
 assertEquals(expectedPointsSize[1], actualPointsSize[1]);
 assertEquals(expectedPointImage, actualPointImage);
 // Verify paintComponent was called to paint the point
 assertTrue(huntDisplay.wasPaintComponentCalled());
 // Verify point coordinates were used to paint point
 assertTrue(Math.abs(x - actualPointImage.getWidth() / 2) < 1);
 assertTrue(Math.abs(y - actualPointImage.getHeight() / 2) < 1);
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
 huntDisplay.addLandmark(landmark);
 int[] expectedLandmarkSize = {10, 10};
 BufferedImage expectedLandmarkImage = landmark.getIcon().getImage();
 int x = (int)(landmark.getLon() * huntDisplay.getWidth());
 int y = (int)(landmark.getLat() * huntDisplay.getHeight());
 int[] actualLandmarkSize = {10, 10};
 BufferedImage actualLandmarkImage = huntDisplay.getImage();
 // Verify landmark was added and painted correctly
 assertEquals(expectedLandmarkSize[0], actualLandmarkSize[0]);
 assertEquals(expectedLandmarkSize[1], actualLandmarkSize[1]);
 assertEquals(expectedLandmarkImage, actualLandmarkImage);
 // Verify paintComponent was called to paint the landmark
 assertTrue(huntDisplay.wasPaintComponentCalled());
 // Verify landmark coordinates were used to paint landmark
 assertTrue(Math.abs(x - actualLandmarkImage.getWidth() / 2) < 1);
 assertTrue(Math.abs(y - actualLandmarkImage.getHeight() / 2) < 1);
 }

 @Test
 public void testAddPointAndLandmark() {
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
 huntDisplay.addPoint(trigPoint);
 huntDisplay.addLandmark(landmark);
 int[] expectedPointsSize = {5, 5};
 BufferedImage expectedPointImage = ImageIO.read(getClass().getResource("/point.png"));
 int[] expectedLandmarkSize = {10, 10};
 BufferedImage expectedLandmarkImage = landmark.getIcon().getImage();
 int xPoint = (int)(trigPoint.getX() * huntDisplay.getWidth());
 int yPoint = (int)(trigPoint.getY() * huntDisplay.getHeight());
 int xLandmark = (int)(landmark.getLon() * huntDisplay.getWidth());
 int yLandmark = (int)(landmark.getLat() * huntDisplay.getHeight());
 int[] actualPointsSize = {5, 5};
 BufferedImage actualPointImage = huntDisplay.getImage();
 int[] actualLandmarkSize = {10, 10};
 BufferedImage actualLandmarkImage = huntDisplay.getImage();
 // Verify both point and landmark were added and painted correctly
 assertEquals(expectedPointsSize[0], actualPointsSize[0]);
 assertEquals(expectedPointsSize[1], actualPointsSize[1]);
 assertEquals(expectedPointImage, actualPointImage);
 assertEquals(expectedLandmarkSize[0], actualLandmarkSize[0]);
 assertEquals(expectedLandmarkSize[1], actualLandmarkSize[1]);
 assertEquals(expectedLandmarkImage, actualLandmarkImage);
 // Verify paintComponent was called to paint both point and landmark
 assertTrue(huntDisplay.wasPaintComponentCalled());
 // Verify points and landmarks coordinates were used to paint correctly
 assertTrue(Math.abs(xPoint - actualPointImage.getWidth() / 2) < 1);
 assertTrue(Math.abs(yPoint - actualPointImage.getHeight() / 2) < 1);
 assertTrue(Math.abs(xLandmark - actualLandmarkImage.getWidth() / 2) < 1);
 assertTrue(Math.abs(yLandmark - actualLandmarkImage.getHeight() / 2) < 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullPoint() {
 huntDisplay.addPoint(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullLandmark() {
 huntDisplay.addLandmark(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullIcon() {
 Landmark landmark = new Landmark(1.0, 2.0, null);
 huntDisplay.addLandmark(landmark);
 }
}