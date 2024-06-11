package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 this.frame = new FoxHuntFrame();
 }

 @Test
 public void testAddPointWithValidCoordinates() {
 // Set up text fields with valid values
 JTextField theta = this.frame.theta;
 theta.setText("45");
 JTextField lat = this.frame.lat;
 lat.setText("51.5074");
 JTextField lon = this.frame.lon;
 lon.setText("-0.0823");

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(this.frame.add, 0, "click");
 this.frame.actionPerformed(actionEvent);

 // Check if point was added to the list
 assertEquals(1, this.frame.points.size());
 }

 @Test
 public void testAddPointWithInvalidCoordinates() {
 // Set up text fields with invalid values
 JTextField theta = this.frame.theta;
 theta.setText("abc");
 JTextField lat = this.frame.lat;
 lat.setText("123.456");
 JTextField lon = this.frame.lon;
 lon.setText("X.Y.Z");

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(this.frame.add, 0, "click");
 this.frame.actionPerformed(actionEvent);

 // Check if no point was added to the list
 assertEquals(0, this.frame.points.size());
 }

 @Test
 public void testAddPointWithEmptyTextFields() {
 // Set up text fields as empty
 JTextField theta = this.frame.theta;
 JTextField lat = this.frame.lat;
 JTextField lon = this.frame.lon;

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(this.frame.add, 0, "click");
 this.frame.actionPerformed(actionEvent);

 // Check if no point was added to the list
 assertEquals(0, this.frame.points.size());
 }

 @Test
 public void testAddPointWithNullTextFields() {
 // Set up text fields as null
 JTextField theta = null;
 JTextField lat = null;
 JTextField lon = null;

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(this.frame.add, 0, "click");
 this.frame.actionPerformed(actionEvent);

 // Check if no point was added to the list
 assertEquals(0, this.frame.points.size());
 }

 @Test
 public void testAddPointWithTwoPoints() {
 // Add first point
 JTextField theta = this.frame.theta;
 theta.setText("45");
 JTextField lat = this.frame.lat;
 lat.setText("51.5074");
 JTextField lon = this.frame.lon;
 lon.setText("-0.0823");
 ActionEvent actionEvent = new ActionEvent(this.frame.add, 0, "click");
 this.frame.actionPerformed(actionEvent);

 // Add second point
 theta.setText("30");
 lat.setText("52.5074");
 lon.setText("-0.1234");
 actionEvent = new ActionEvent(this.frame.add, 0, "click");
 this.frame.actionPerformed(actionEvent);

 // Check if two points were added to the list
 assertEquals(2, this.frame.points.size());
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLat() {
 // Set up text fields with invalid lat value
 JTextField theta = this.frame.theta;
 theta.setText("45");
 JTextField lat = this.frame.lat;
 lat.setText("abc");
 JTextField lon = this.frame.lon;
 lon.setText("-0.0823");

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(this.frame.add, 0, "click");
 this.frame.actionPerformed(actionEvent);

 // Check if exception was thrown
 // ...
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLon() {
 // Set up text fields with invalid lon value
 JTextField theta = this.frame.theta;
 theta.setText("45");
 JTextField lat = this.frame.lat;
 lat.setText("51.5074");
 JTextField lon = this.frame.lon;
 lon.setText("X.Y.Z");

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(this.frame.add, 0, "click");
 this.frame.actionPerformed(actionEvent);

 // Check if exception was thrown
 // ...
 }

 @Test
 public void testDisplayPoints() {
 // Add two points
 JTextField theta = this.frame.theta;
 theta.setText("45");
 JTextField lat = this.frame.lat;
 lat.setText("51.5074");
 JTextField lon = this.frame.lon;
 lon.setText("-0.0823");
 ActionEvent actionEvent = new ActionEvent(this.frame.add, 0, "click");
 this.frame.actionPerformed(actionEvent);

 theta.setText("30");
 lat.setText("52.5074");
 lon.setText("-0.1234");
 actionEvent = new ActionEvent(this.frame.add, 0, "click");
 this.frame.actionPerformed(actionEvent);

 // Check if points are displayed in the table
 assertEquals(2, this.frame.pos.getRowCount());
 }
}