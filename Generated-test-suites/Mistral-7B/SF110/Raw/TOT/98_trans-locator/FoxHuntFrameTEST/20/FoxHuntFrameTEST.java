package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.Vector;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testAddPointWithValidValues() {
 // Given: User enters valid values for Theta, Lat, and Lon
 frame.theta.setText("30");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.1278");

 // When: User clicks "Add It!!!!" button
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));

 // Then: The new point should be added to the points vector and displayed on the map
 assertEquals(2, frame.points.size());
 assertTrue(frame.display.getPoints().contains(new TrigPoint(51.5074, -0.1278, 30)));
 }

 @Test
 public void testAddPointWithInvalidThetaValue() {
 // Given: User enters an invalid value for Theta
 frame.theta.setText("abc");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.1278");

 // When: User clicks "Add It!!!!" button
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));

 // Then: An error dialog should be displayed and no new point should be added
 assertEquals(1, frame.points.size());
 assertFalse(frame.display.getPoints().contains(new TrigPoint(-1, -1, 30)));
 }

 @Test
 public void testAddPointWithInvalidLatValue() {
 // Given: User enters an invalid value for Lat
 frame.theta.setText("30");
 frame.lat.setText("abcd");
 frame.lon.setText("-0.1278");

 // When: User clicks "Add It!!!!" button
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));

 // Then: An error dialog should be displayed and no new point should be added
 assertEquals(1, frame.points.size());
 assertFalse(frame.display.getPoints().contains(new TrigPoint(-1, -1, 30)));
 }

 @Test
 public void testAddPointWithInvalidLonValue() {
 // Given: User enters an invalid value for Lon
 frame.theta.setText("30");
 frame.lat.setText("51.5074");
 frame.lon.setText("abcd");

 // When: User clicks "Add It!!!!" button
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));

 // Then: An error dialog should be displayed and no new point should be added
 assertEquals(1, frame.points.size());
 assertFalse(frame.display.getPoints().contains(new TrigPoint(51.5074, -1, 30)));
 }

 @Test
 public void testCalculateFoxWithTwoPoints() {
 // Given: Two valid points have been added
 frame.theta.setText("30");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.1278");
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));
 frame.theta.setText("120");
 frame.lat.setText("52.0019");
 frame.lon.setText("-1.1234");
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));

 // When: The second point is added and the fox point is calculated
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));

 // Then: A new fox point should be calculated and displayed
 assertEquals(3, frame.points.size());
 assertEquals(3, frame.fox.size());
 TrigPoint expectedFox = new TrigPoint(51.7183, -0.8941, 156.5328);
 assertTrue(frame.fox.contains(expectedFox));
 }

 @Test
 public void testLoadLandmarksFromFile() {
 // Given: The landmarks.cfg file exists and contains valid data
 // ... (Write a test method to load the landmarks.cfg file and check that the landmarks have been loaded correctly)
 }
}