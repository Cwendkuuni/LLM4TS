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
 this.frame = new FoxHuntFrame();
 }

 @Test
 public void testAddPointValidInput() {
 // Set up input fields with valid values
 this.frame.theta.setText("45");
 this.frame.lat.setText("40.7128");
 this.frame.lon.setText("-74.0060");

 // Simulate button click event
 ActionEvent actionEvent = new ActionEvent(this.frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 this.frame.actionPerformed(actionEvent);

 // Check if point was added to points and fox vectors and displayed correctly
 assertEquals(2, this.frame.points.size());
 assertEquals(2, this.frame.fox.size());
 assertNotNull(this.frame.posModel);
 assertFalse(this.frame.pos.isEmpty());
 }

 @Test
 public void testAddPointInvalidInput() {
 // Set up input fields with invalid values
 this.frame.theta.setText("abc");
 this.frame.lat.setText("ddd.ddd.ddd");
 this.frame.lon.setText("xxxxxxxxxxx");

 // Simulate button click event
 ActionEvent actionEvent = new ActionEvent(this.frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 this.frame.actionPerformed(actionEvent);

 // Check if no point was added and error dialog was displayed
 assertEquals(1, this.frame.points.size());
 assertTrue(this.frame.isErrorDialogDisplayed());
 }

 @Test
 public void testAddPointEmptyInputs() {
 // Set up input fields with empty values
 this.frame.theta.setText("");
 this.frame.lat.setText("");
 this.frame.lon.setText("");

 // Simulate button click event
 ActionEvent actionEvent = new ActionEvent(this.frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 this.frame.actionPerformed(actionEvent);

 // Check if no point was added and error dialog was displayed
 assertEquals(1, this.frame.points.size());
 assertTrue(this.frame.isErrorDialogDisplayed());
 }

 @Test
 public void testAddPointNullInputs() {
 // Set up input fields with null values
 this.frame.theta = null;
 this.frame.lat = null;
 this.frame.lon = null;

 // Simulate button click event
 ActionEvent actionEvent = new ActionEvent(this.frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 this.frame.actionPerformed(actionEvent);

 // Check if no point was added and error dialog was displayed
 assertEquals(1, this.frame.points.size());
 assertTrue(this.frame.isErrorDialogDisplayed());
 }

 @Test
 public void testCalcFoxValidInput() {
 // Add two points to simulate initializing fox vector
 this.frame.addPoint();
 this.frame.addPoint();

 // Check if fox point is calculated and displayed correctly
 assertEquals(2, this.frame.fox.size());
 TrigPoint expectedFox = new TrigPoint(40.7128, -74.006, 45);
 TrigPoint actualFox = (TrigPoint) this.frame.fox.lastElement();
 assertEquals(expectedFox.getX(), actualFox.getX(), 0.001);
 assertEquals(expectedFox.getY(), actualFox.getY(), 0.001);
 }

 @Test
 public void testCalcFoxInvalidInput() {
 // Add only one point to simulate invalid input for calcFox method
 this.frame.addPoint();

 // Check if no fox point is calculated and displayed
 assertEquals(1, this.frame.fox.size());
 assertNull(this.frame.fox.lastElement());
 }
}