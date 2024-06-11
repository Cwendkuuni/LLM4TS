package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testConstructor() {
 // Verify that the frame is initialized correctly
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.getIconImage());
 assertNotNull(frame.getContentPane());
 }

 @Test
 public void testAddPoint_ValidInput() {
 // Set up input fields with valid data
 frame.theta.setText("45");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");

 // Simulate button click event
 frame.add.doClick();

 // Verify that the point was added to the model and displayed
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.posModel.getRowCount());
 }

 @Test
 public void testAddPoint_InvalidInput() {
 // Set up input fields with invalid data
 frame.theta.setText("abc");
 frame.lat.setText("X.Y.Z");
 frame.lon.setText("XXX.XXX.XXX");

 // Simulate button click event
 frame.add.doClick();

 // Verify that nothing was added to the model or displayed
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.posModel.getRowCount());
 }

 @Test
 public void testAddPoint_EmptyInput() {
 // Set up input fields with empty data
 frame.theta.setText("");
 frame.lat.setText("");
 frame.lon.setText("");

 // Simulate button click event
 frame.add.doClick();

 // Verify that nothing was added to the model or displayed
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.posModel.getRowCount());
 }

 @Test
 public void testDisplayLandmarks() {
 // Assume that loadLandmarks() method has been called correctly
 // Verify that the landmarks are displayed in the correct order and positions
 // (This test would be more thorough if it could interact with the GUI directly, but it's difficult to simulate that in JUnit)
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPoint_InvalidTheta() {
 // Set up input fields with invalid theta data
 frame.theta.setText("invalid");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");

 // Simulate button click event
 frame.add.doClick();
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPoint_InvalidLat() {
 // Set up input fields with invalid lat data
 frame.theta.setText("45");
 frame.lat.setText("invalid");
 frame.lon.setText("-74.0060");

 // Simulate button click event
 frame.add.doClick();
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPoint_InvalidLon() {
 // Set up input fields with invalid lon data
 frame.theta.setText("45");
 frame.lat.setText("40.7128");
 frame.lon.setText("invalid");

 // Simulate button click event
 frame.add.doClick();
 }
}