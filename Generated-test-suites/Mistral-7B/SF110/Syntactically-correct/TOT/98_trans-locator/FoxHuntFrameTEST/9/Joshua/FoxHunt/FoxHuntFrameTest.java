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
 public void testAddPointWithValidInput() {
 // Set up input fields with valid data
 this.frame.theta.setText("270");
 this.frame.lat.setText("40.7128");
 this.frame.lon.setText("-74.0060");

 // Simulate button click
 this.frame.add.doClick();

 // Check if point was added to points vector
 assertEquals(1, this.frame.points.size());

 // Check if point was added to display
 // (This would require access to private members or a getter method)

 // Check if fox vector has one element
 assertEquals(1, this.frame.fox.size());
 }

 @Test
 public void testAddPointWithInvalidInput() {
 // Set up input fields with invalid data
 this.frame.theta.setText("abc");
 this.frame.lat.setText("ddd.dddd");
 this.frame.lon.setText("xxx.xxxxx");

 // Simulate button click
 this.frame.add.doClick();

 // Check if no point was added to points vector
 assertEquals(0, this.frame.points.size());

 // Check if no point was added to display
 // (This would require access to private members or a getter method)
 }

 @Test
 public void testAddPointWithEmptyInput() {
 // Set up input fields with empty data
 this.frame.theta.setText("");
 this.frame.lat.setText("");
 this.frame.lon.setText("");

 // Simulate button click
 this.frame.add.doClick();

 // Check if no point was added to points vector
 assertEquals(0, this.frame.points.size());

 // Check if no point was added to display
 // (This would require access to private members or a getter method)
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidThetaInput() {
 // Set up input fields with invalid theta data
 this.frame.theta.setText("asd");
 this.frame.lat.setText("40.7128");
 this.frame.lon.setText("-74.0060");

 // Simulate button click
 this.frame.add.doClick();

 // Check if no point was added to points vector
 assertEquals(0, this.frame.points.size());

 // Check if no point was added to display
 // (This would require access to private members or a getter method)

 // Verify that NumberFormatException is thrown
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLatInput() {
 // Set up input fields with invalid lat data
 this.frame.theta.setText("270");
 this.frame.lat.setText("dddd.dddddd");
 this.frame.lon.setText("-74.0060");

 // Simulate button click
 this.frame.add.doClick();

 // Check if no point was added to points vector
 assertEquals(0, this.frame.points.size());

 // Check if no point was added to display
 // (This would require access to private members or a getter method)

 // Verify that NumberFormatException is thrown
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLonInput() {
 // Set up input fields with invalid lon data
 this.frame.theta.setText("270");
 this.frame.lat.setText("40.7128");
 this.frame.lon.setText("xxxx.xxxxx");

 // Simulate button click
 this.frame.add.doClick();

 // Check if no point was added to points vector
 assertEquals(0, this.frame.points.size());

 // Check if no point was added to display
 // (This would require access to private members or a getter method)

 // Verify that NumberFormatException is thrown
 }

 @Test
 public void testAddPointWithDuplicateInput() {
 // Set up input fields with duplicate data
 this.frame.theta.setText("270");
 this.frame.lat.setText("40.7128");
 this.frame.lon.setText("-74.0060");
 this.frame.add.doClick(); // Add first point
 this.frame.theta.setText("270");
 this.frame.lat.setText("40.7128");
 this.frame.lon.setText("-74.0060");
 this.frame.add.doClick(); // Add second point

 // Check if only one point was added to points vector
 assertEquals(1, this.frame.points.size());

 // Check if only one point was added to display
 // (This would require access to private members or a getter method)
 }
}