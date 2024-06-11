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
 public void setup() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 // Check if the frame is created successfully
 assertNotNull(frame);
 }

 @Test
 public void testActionPerformedMethod() {
 // Given
 final String angle = "45";
 final double lat = 37.7749;
 final double lon = -122.4194;
 frame.theta.setText(angle);
 frame.lat.setText(String.format("%.6f", lat));
 frame.lon.setText(String.format("%.6f", lon));
 final ActionEvent event = new ActionEvent(new Object(), 0, "Add It!!!");

 // When
 frame.actionPerformed(event);

 // Then
 assertEquals(1, frame.points.size());
 assertEquals(lat, frame.points.elementAt(0).getY(), 0.0001);
 assertEquals(lon, frame.points.elementAt(0).getX(), 0.0001);
 }

 @Test
 public void testAddPointMethodWithTwoPoints() {
 // Given
 frame.theta.setText("45");
 frame.lat.setText("37.7749");
 frame.lon.setText("-122.4194");
 frame.theta.setText("30");
 frame.lat.setText("37.7748");
 frame.lon.setText("-122.4193");
 frame.actionPerformed(new ActionEvent(new Object(), 0, "Add It!!!"));
 frame.actionPerformed(new ActionEvent(new Object(), 0, "Add It!!!"));

 // When
 final TrigPoint fox = frame.fox.elementAt(0);

 // Then
 assertEquals(2, frame.points.size());
 assertEquals(fox.getX(), frame.points.elementAt(1).getX(), 0.0001);
 assertEquals(fox.getY(), frame.points.elementAt(1).getY(), 0.0001);
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidTheta() {
 // Given
 frame.theta.setText("abcd");
 frame.lat.setText("37.7749");
 frame.lon.setText("-122.4194");
 frame.actionPerformed(new ActionEvent(new Object(), 0, "Add It!!!"));

 // Then
 fail("An exception of type NumberFormatException was expected");
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidLatitude() {
 // Given
 frame.theta.setText("45");
 frame.lat.setText("abcd");
 frame.lon.setText("-122.4194");
 frame.actionPerformed(new ActionEvent(new Object(), 0, "Add It!!!"));

 // Then
 fail("An exception of type NumberFormatException was expected");
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidLongitude() {
 // Given
 frame.theta.setText("45");
 frame.lat.setText("37.7749");
 frame.lon.setText("abcd");
 frame.actionPerformed(new ActionEvent(new Object(), 0, "Add It!!!"));

 // Then
 fail("An exception of type NumberFormatException was expected");
 }

 @Test
 public void testLoadLandmarksMethod() {
 // Given
 frame = new FoxHuntFrame();
 final String expectedLandmarksFileName = "./landmarks.cfg";

 // When
 frame.loadLandmarks();

 // Then
 assertTrue(frame.display.getLandmarks().size() > 0);
 assertEquals(expectedLandmarksFileName, frame.display.getLandmarks().get(0).getIconFile());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksMethodWithFileNotFoundException() {
 // Given
 frame = new FoxHuntFrame();
 final String invalidLandmarksFileName = "invalidFile.cfg";

 // When
 // Then
 try {
 frame.loadLandmarks();
 fail("A FileNotFoundException was expected");
 } catch (FileNotFoundException e) {
 // Pass
 }
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksMethodWithIOException() {
 // Given
 frame = new FoxHuntFrame();
 final String expectedLandmarksFileName = "./landmarks.cfg";
 final String invalidLandmarksFileName = "invalidFile.cfg";
 final String expectedErrorMessage = "Error communicating with the file";

 // When & Then
 try {
 frame.loadLandmarks();
 fail("An IOException was expected");
 } catch (IOException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 @Test
 public void testFoxTableModelGetValueAtMethod() {
 // Given
 final double foxLat = 37.7749;
 final double foxLon = -122.4194;
 final TrigPoint fox = new TrigPoint(foxLat, foxLon, 0);
 frame.fox.add(fox);
 final FoxTableModel tableModel = frame.posModel;

 // When & Then
 assertEquals("" + foxLat, tableModel.getValueAt(0, 0));
 assertEquals("" + foxLon, tableModel.getValueAt(0, 1));
 }
}