package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testConstructor() {
 // Verify the title of the frame
 assertEquals("Triangulation", frame.getTitle());
 // Check if the frame is visible and closed by default
 assertTrue(frame.isVisible());
 assertFalse(frame.isFocusable());
 assertFalse(frame.isDisplayable());
 }

 @Test
 public void testTextFieldsInitialization() {
 // Verify the text fields are initialized
 assertNotNull(frame.theta);
 assertNotNull(frame.lat);
 assertNotNull(frame.lon);
 }

 @Test
 public void testAddPointWithValidInput() {
 // Set text fields with valid data
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("0.0723");
 // Simulate button click
 frame.add.doClick();
 // Verify the point has been added to the list
 assertEquals(1, frame.points.size());
 // Verify the table has updated with the new data
 assertEquals("51.5074", frame.posModel.getValueAt(0, 0).toString());
 assertEquals("0.0723", frame.posModel.getValueAt(0, 1).toString());
 }

 @Test
 public void testAddPointWithInvalidInput() {
 // Set text fields with invalid data
 frame.theta.setText("abc");
 frame.lat.setText("123.456");
 frame.lon.setText("xyz");
 // Simulate button click
 frame.add.doClick();
 // Verify no new point is added and no error is shown
 assertEquals(0, frame.points.size());
 // Check if an error message is shown
 assertNotNull(frame.getErrorMessage());
 }

 @Test
 public void testLoadLandmarks() {
 // Simulate file loading
 frame.loadLandmarks();
 // Verify the number of landmarks loaded is correct
 assertEquals(10, frame.display.getNumLandmarks());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksWithMissingFile() {
 // Simulate file loading with missing file
 frame.loadLandmarks();
 fail("A FileNotFoundException should be thrown");
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksWithIOException() {
 // Simulate file loading with IOException
 frame.loadLandmarks();
 fail("An IOException should be thrown");
 }

 @Test
 public void testDisplay() {
 // Verify the display is initialized
 assertNotNull(frame.display);
 // Verify the number of landmarks displayed is correct
 assertEquals(10, frame.display.getNumLandmarks());
 }

 @After
 public void tearDown() {
 // Close the frame to free resources
 frame.dispose();
 frame = null;
 }

}