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
 // Test the constructor
 assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testActionPerformed() {
 // Given
 String angle = "30";
 String lat = "40.7128";
 String lon = "-74.0060";
 frame.theta.setText(angle);
 frame.lat.setText(lat);
 frame.lon.setText(lon);

 // When
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Test"));

 // Then
 // Check the size of the points and fox vectors
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());

 // Check the last element of the points vector
 TrigPoint point = (TrigPoint) frame.points.lastElement();
 assertEquals(Double.parseDouble(lat), point.getY(), 0.0001);
 assertEquals(Double.parseDouble(lon), point.getX(), 0.0001);
 assertEquals(Integer.parseInt(angle), point.getAngle());

 // Check the last element of the fox vector
 TrigPoint fox = (TrigPoint) frame.fox.lastElement();
 // The fox position is not calculable without a second point
 assertNotNull(fox);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTheta() {
 // Given
 frame.theta.setText("abc");

 // When
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Test"));

 // Then
 // A NumberFormatException should be thrown
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidLat() {
 // Given
 frame.lat.setText("ddd.mm.mmm");

 // When
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Test"));

 // Then
 // A NumberFormatException should be thrown
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidLon() {
 // Given
 frame.lon.setText("ddd*mm.mmm");

 // When
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Test"));

 // Then
 // A NumberFormatException should be thrown
 }

 @Test
 public void testLoadLandmarks() {
 // Given
 // Assuming that the landmarks.cfg file exists and contains data

 // When
 frame.loadLandmarks();

 // Then
 // Check the size of the display's landmarks
 assertEquals(10, frame.display.getLandMarks().size());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksFileNotFoundException() {
 // Given
 // Assuming that the landmarks.cfg file does not exist

 // When
 // Call the loadLandmarks method

 // Then
 // A FileNotFoundException should be thrown
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksIOException() {
 // Given
 // Assuming that there is an error while communicating with the file

 // When
 // Call the loadLandmarks method with an IOException

 // Then
 // An IOException should be thrown
 }

 @Test
 public void testFoxTableModelGetValueAt() {
 // Given
 // Assuming that the frame has at least one fox point
 Vector<TrigPoint> fox = frame.fox;
 FoxTableModel model = (FoxTableModel) frame.posModel;

 // When
 Object valueLat = model.getValueAt(0, 0);
 Object valueLon = model.getValueAt(0, 1);

 // Then
 // Check the values of the first cell in the JTable
 assertEquals(valueLat.getClass(), Double.class);
 assertEquals(valueLat, Double.valueOf(fox.get(0).getY()));
 assertEquals(valueLon.getClass(), Double.class);
 assertEquals(valueLon, Double.valueOf(fox.get(0).getX()));
 }
}