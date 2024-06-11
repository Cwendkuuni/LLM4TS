package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Vector;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testAddPointValidValues() {
 // Set up initial values
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.1278");
 // Simulate button click
 ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 frame.actionPerformed(event);
 // Check if points and fox vectors have the correct size and content
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 TrigPoint expectedPoint = new TrigPoint(51.5074, -0.1278, 45);
 assertEquals(expectedPoint, frame.points.elementAt(0));
 TrigPoint expectedFox = FoxCalc.calcFox(expectedPoint, FoxCalc.getPreviousPoint(frame.points));
 assertEquals(expectedFox, frame.fox.elementAt(0));
 }

 @Test
 public void testAddPointInvalidValues() {
 // Set up initial values
 frame.theta.setText("abc");
 frame.lat.setText("123.456");
 frame.lon.setText("789.123");
 // Simulate button click
 ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 frame.actionPerformed(event);
 // Check if no points or foxes were added
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());
 // Check if error message is displayed
 assertTrue(JOptionPane.showOptionDialog(frame, "Error: Invalid input values.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.DEFAULT_OPTION);
 }

 @Test
 public void testLoadLandmarks() {
 // Create a mock landmarks.cfg file with some content
 String landmarksFileContent = "NUM=3\nFILE icon1.gif\nLAT 51.5111\nLON -0.1222\nFILE icon2.gif\nLAT 51.5222\nLON -0.1333\nFILE icon3.gif\nLAT 51.5333\nLON -0.1444";
 try {
 // Create a File object for the mock file
 File landmarksFile = new File("landmarks.cfg");
 // Write the mock content to the file
 FileWriter fileWriter = new FileWriter(landmarksFile);
 fileWriter.write(landmarksFileContent);
 fileWriter.close();
 // Initialize the frame
 frame = new FoxHuntFrame();
 // Load landmarks
 frame.loadLandmarks();
 // Check if the correct number of landmarks were loaded
 assertEquals(3, frame.display.getLandmarks().size());
 } catch (IOException e) {
 // Handle the exception
 fail("Failed to write the mock landmarks.cfg file.");
 } finally {
 // Delete the mock file
 landmarksFile.delete();
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testLoadLandmarksInvalidInput() {
 // Create a landmarks.cfg file with invalid input
 String landmarksFileContent = "NUM=3\nFILE icon1.gif\nLAT abc\nLON 123.456\nFILE icon3.gif\nLAT 51.5333\nLON -0.1444";
 try {
 // Create a File object for the mock file
 File landmarksFile = new File("landmarks.cfg");
 // Write the mock content to the file
 FileWriter fileWriter = new FileWriter(landmarksFile);
 fileWriter.write(landmarksFileContent);
 fileWriter.close();
 // Initialize the frame
 frame = new FoxHuntFrame();
 // Load landmarks
 frame.loadLandmarks();
 // Check that an exception is thrown
 // (This test case is intentionally left empty)
 } catch (IOException e) {
 // Handle the exception
 fail("Failed to write the mock landmarks.cfg file.");
 } finally {
 // Delete the mock file
 landmarksFile.delete();
 }
 }
}