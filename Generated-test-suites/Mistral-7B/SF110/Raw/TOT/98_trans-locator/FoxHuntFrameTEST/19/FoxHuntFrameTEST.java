package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.util.Vector;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testAddPointWithValidValues() {
 // Set up text fields with valid values
 frame.theta.setText("45");
 frame.lat.setText("37.412345");
 frame.lon.setText("-122.035678");
 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(frame.add, 0, "Click");
 frame.actionPerformed(actionEvent);
 // Check if points and fox vectors have expected size and content
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 TrigPoint expectedPoint = new TrigPoint(37.412345, -122.035678, 45);
 assertEquals(expectedPoint, frame.points.elementAt(0));
 TrigPoint expectedFox = FoxCalc.calcFox(expectedPoint, frame.points.elementAt(0));
 assertEquals(expectedFox, frame.fox.elementAt(0));
 }

 @Test
 public void testAddPointWithEmptyTextFields() {
 // Set up text fields with empty values
 frame.theta.setText("");
 frame.lat.setText("");
 frame.lon.setText("");
 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(frame.add, 0, "Click");
 frame.actionPerformed(actionEvent);
 // Check if points and fox vectors have expected size and are empty
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());
 }

 @Test
 public void testAddPointWithInvalidTheta() {
 // Set up text fields with invalid values
 frame.theta.setText("abc");
 frame.lat.setText("37.412345");
 frame.lon.setText("-122.035678");
 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(frame.add, 0, "Click");
 frame.actionPerformed(actionEvent);
 // Check if no new elements were added to the points or fox vectors
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());
 // Check if an error message is displayed
 assertEquals("Invalid value for theta", frame.errorMessage);
 }

 @Test
 public void testAddPointWithInvalidLat() {
 // Set up text fields with invalid values
 frame.theta.setText("45");
 frame.lat.setText("xyz");
 frame.lon.setText("-122.035678");
 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(frame.add, 0, "Click");
 frame.actionPerformed(actionEvent);
 // Check if no new elements were added to the points or fox vectors
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());
 // Check if an error message is displayed
 assertEquals("Invalid value for lat", frame.errorMessage);
 }

 @Test
 public void testAddPointWithInvalidLon() {
 // Set up text fields with invalid values
 frame.theta.setText("45");
 frame.lat.setText("37.412345");
 frame.lon.setText("abc");
 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(frame.add, 0, "Click");
 frame.actionPerformed(actionEvent);
 // Check if no new elements were added to the points or fox vectors
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());
 // Check if an error message is displayed
 assertEquals("Invalid value for lon", frame.errorMessage);
 }
}