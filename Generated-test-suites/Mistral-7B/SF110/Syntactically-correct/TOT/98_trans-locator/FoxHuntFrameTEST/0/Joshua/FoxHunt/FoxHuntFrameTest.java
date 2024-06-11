package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testAddPointWithValidInput() {
 // Set up input fields with valid values
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.1278");

 // Simulate button click event
 ActionEvent addEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 frame.actionPerformed(addEvent);

 // Check if point was added to the list and displayed correctly
 assertEquals(1, frame.points.size());
 TrigPoint point = (TrigPoint) frame.points.lastElement();
 assertEquals(45, point.getAngle(), 0.01);
 assertEquals(51.5074, point.getY(), 0.0001);
 assertEquals(-0.1278, point.getX(), 0.0001);

 // Check if table and display were updated correctly
 assertEquals("51.5074", frame.posModel.getValueAt(frame.pos.getRowCount() - 1, 0).toString());
 assertEquals("-0.1278", frame.posModel.getValueAt(frame.pos.getRowCount() - 1, 1).toString());
 }

 @Test
 public void testAddPointWithInvalidAngleInput() {
 // Set up input fields with invalid angle value
 frame.theta.setText("abc");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.1278");

 // Simulate button click event
 ActionEvent addEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 frame.actionPerformed(addEvent);

 // Check if no new point was added and no error dialog was shown
 assertEquals(0, frame.points.size());
 // Add assertions here to check for error dialog or other expected behavior
 }

 @Test
 public void testAddPointWithInvalidLatInput() {
 // Set up input fields with invalid latitude value
 frame.theta.setText("45");
 frame.lat.setText("abc");
 frame.lon.setText("-0.1278");

 // Simulate button click event
 ActionEvent addEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 frame.actionPerformed(addEvent);

 // Check if no new point was added and no error dialog was shown
 assertEquals(0, frame.points.size());
 // Add assertions here to check for error dialog or other expected behavior
 }

 @Test
 public void testAddPointWithInvalidLonInput() {
 // Set up input fields with invalid longitude value
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("abc");

 // Simulate button click event
 ActionEvent addEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 frame.actionPerformed(addEvent);

 // Check if no new point was added and no error dialog was shown
 assertEquals(0, frame.points.size());
 // Add assertions here to check for error dialog or other expected behavior
 }

 @Test
 public void testAddPointWithMissingInputFields() {
 // Set up input fields with missing values
 frame.theta.setText("");
 frame.lat.setText("");
 frame.lon.setText("");

 // Simulate button click event
 ActionEvent addEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 frame.actionPerformed(addEvent);

 // Check if no new point was added and no error dialog was shown
 assertEquals(0, frame.points.size());
 // Add assertions here to check for error dialog or other expected behavior
 }
}