package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 this.frame = new FoxHuntFrame();
 }

 @Test
 public void testAddPointWithValidInput() {
 // Set up input fields with valid values
 this.frame.theta.setText("45");
 this.frame.lat.setText("42.34567890");
 this.frame.lon.setText("-123.45678901");

 // Simulate button click
 ActionEvent event = new ActionEvent(this.frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 this.frame.actionPerformed(event);

 // Check if point was added to the points vector
 assertEquals(2, this.frame.points.size());

 // Check if point was added to the display
 assertTrue(this.frame.display.getPoints().contains(this.frame.points.lastElement()));
 }

 @Test
 public void testAddPointWithInvalidInput() {
 // Set up input fields with invalid values
 this.frame.theta.setText("abc");
 this.frame.lat.setText("ddd");
 this.frame.lon.setText("xxx");

 // Simulate button click
 ActionEvent event = new ActionEvent(this.frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 this.frame.actionPerformed(event);

 // Check if nothing was added to the points vector
 assertEquals(1, this.frame.points.size());

 // Check if an error message was displayed
 assertTrue(this.frame.hasErrorMessage("Invalid input"));
 }

 @Test
 public void testAddPointWithZeroLatitude() {
 // Set up input fields with zero latitude
 this.frame.theta.setText("45");
 this.frame.lat.setText("0");
 this.frame.lon.setText("123.45678901");

 // Simulate button click
 ActionEvent event = new ActionEvent(this.frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 this.frame.actionPerformed(event);

 // Check if point was added to the points vector
 assertEquals(2, this.frame.points.size());

 // Check if fox was calculated correctly
 assertEquals(new TrigPoint(0.0, 123.45678901, 45), this.frame.fox.lastElement());
 }

 @Test
 public void testAddPointWithDuplicateLatitude() {
 // Set up input fields with duplicate latitude
 this.frame.theta.setText("45");
 this.frame.lat.setText("42.34567890");
 this.frame.lon.setText("-123.45678901");
 this.frame.addPoint(); // Add first point
 this.frame.theta.setText("45");
 this.frame.lat.setText("42.34567890");
 this.frame.lon.setText("123.45678901"); // Different longitude
 this.frame.addPoint(); // Add second point

 // Check if two points have different longitudes
 assertNotEquals(this.frame.points.lastElement(), this.frame.points.elementAt(this.frame.points.size() - 2));
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLatitudeFormat() {
 // Set up input fields with invalid latitude format
 this.frame.theta.setText("45");
 this.frame.lat.setText("ddddddddd.dddddddd");
 this.frame.lon.setText("123.45678901");

 // Simulate button click
 ActionEvent event = new ActionEvent(this.frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 this.frame.actionPerformed(event);

 // Check if an error message was displayed
 assertTrue(this.frame.hasErrorMessage("Invalid latitude format"));
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLongitudeFormat() {
 // Set up input fields with invalid longitude format
 this.frame.theta.setText("45");
 this.frame.lat.setText("42.34567890");
 this.frame.lon.setText("ddddddddd.dddddddd");

 // Simulate button click
 ActionEvent event = new ActionEvent(this.frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 this.frame.actionPerformed(event);

 // Check if an error message was displayed
 assertTrue(this.frame.hasErrorMessage("Invalid longitude format"));
 }
}