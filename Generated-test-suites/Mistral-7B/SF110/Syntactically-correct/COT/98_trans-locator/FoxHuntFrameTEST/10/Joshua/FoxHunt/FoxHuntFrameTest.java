package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 // Testing if the constructor is called correctly
 assertNotNull(frame);
 }

 @Test
 public void testActionPerformed() {
 // Create test data
 final String theta = "10";
 final String lat = "1.234567890";
 final String lon = "123.456789012";

 // Set up test data in text fields
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
 frame.actionPerformed(actionEvent);

 // Check if point was added to the points vector
 assertEquals(1, frame.points.size());

 // Check if point was added to the display
 assertTrue(frame.display.getPoints().contains(new TrigPoint(Double.parseDouble(lat), Double.parseDouble(lon), Integer.parseInt(theta))));
 }

 @Test
 public void testAddPointWithInvalidTheta() {
 // Set up test data
 final String theta = "";
 final String lat = "1.234567890";
 final String lon = "123.456789012";

 // Set up test data in text fields
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
 frame.actionPerformed(actionEvent);

 // Check if no point was added to the points vector or display
 assertEquals(0, frame.points.size());
 assertFalse(frame.display.getPoints().size() > 0);
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLat() {
 // Set up test data
 final String theta = "10";
 final String lat = "";
 final String lon = "123.456789012";

 // Set up test data in text fields
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
 frame.actionPerformed(actionEvent);

 // Check that an exception is thrown
 // NumberFormatException should be thrown due to empty lat text field
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLon() {
 // Set up test data
 final String theta = "10";
 final String lat = "1.234567890";
 final String lon = "";

 // Set up test data in text fields
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
 frame.actionPerformed(actionEvent);

 // Check that an exception is thrown
 // NumberFormatException should be thrown due to empty lon text field
 }

 @Test
 public void testFinalPrep() {
 // Test if finalPrep method is called correctly
 frame.finalPrep();
 // Check if components are added to the frame
 assertTrue(frame.getContentPane().getComponentCount() == 1);
 assertTrue(frame.getContentPane().getComponentAt(0) instanceof JSplitPane);
 }

 @Test
 public void testTextPrep() {
 // Test if textPrep method is called correctly
 frame.textPrep();
 // Check if text fields and labels are added to panel1
 assertTrue(frame.panel1.getComponentCount() == 5);
 }

 @Test
 public void testTablePrep() {
 // Test if tablePrep method is called correctly
 frame.tablePrep();
 // Check if table and scroll pane are added to panel2
 assertTrue(frame.panel2.getComponentCount() == 1);
 assertTrue(frame.panel2.getComponentAt(0) instanceof JScrollPane);
 assertTrue(frame.panel2.getComponentAt(0).getComponent() instanceof JTable);
 }

 @Test
 public void testDisplayPrep() {
 // Test if displayPrep method is called correctly
 frame.displayPrep();
 // Check if HuntDisplay is added to panel3
 assertTrue(frame.panel3.getComponentCount() == 1);
 assertTrue(frame.panel3.getComponentAt(0) instanceof HuntDisplay);
 }

 @Test
 public void testLoadLandmarks() {
 // Test if loadLandmarks method is called correctly
 frame.loadLandmarks();
 // Add checkpoint for exception handling
 try {
 frame.loadLandmarks();
 } catch (FileNotFoundException e) {
 fail("FileNotFoundException should not be thrown as file is in the same directory");
 }
 // Check if landmarks are added to the display
 assertEquals(3, frame.display.getLandmarks().size());
 }
}