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
 // Check if the frame is created with the correct title
 assertEquals("Triangulation", frame.getTitle());

 // Check if the frame is closed when the default close operation is set to EXIT_ON_CLOSE
 frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
 assertFalse(frame.isVisible());
 }

 @Test
 public void testAddPoint() {
 // Given a valid angle, latitude and longitude
 frame.theta.setText("45");
 frame.lat.setText("51.503902");
 frame.lon.setText("-0.127767");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));

 // Check if the point is added to the points vector
 assertEquals(1, frame.points.size());
 TrigPoint point = (TrigPoint) frame.points.elementAt(0);
 assertEquals(45, point.getAngle(), 0.1);
 assertEquals(51.503902, point.getY(), 0.0001);
 assertEquals(-0.127767, point.getX(), 0.0001);

 // Given a second valid point
 frame.theta.setText("35");
 frame.lat.setText("51.542898");
 frame.lon.setText("-0.082368");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));

 // Check if the fox point is calculated and added to the fox vector
 assertEquals(2, frame.points.size());
 assertEquals(2, frame.fox.size());
 TrigPoint foxPoint = (TrigPoint) frame.fox.elementAt(0);
 assertEquals(45, foxPoint.getAngle(), 0.1);
 assertEquals(51.503902, foxPoint.getY(), 0.0001);
 assertEquals(-0.127767, foxPoint.getX(), 0.0001);
 TrigPoint secondFoxPoint = (TrigPoint) frame.fox.elementAt(1);
 assertEquals(35, secondFoxPoint.getAngle(), 0.1);
 assertEquals(51.542898, secondFoxPoint.getY(), 0.0001);
 assertEquals(-0.082368, secondFoxPoint.getX(), 0.0001);

 // Check if the table model is updated
 FoxTableModel model = (FoxTableModel) frame.posModel;
 assertEquals(2, model.getRowCount());
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLatitude() {
 // Given an invalid latitude
 frame.theta.setText("45");
 frame.lat.setText("A1");
 frame.lon.setText("-0.127767");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 fail("An exception of type NumberFormatException was expected.");
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLongitude() {
 // Given an invalid longitude
 frame.theta.setText("45");
 frame.lat.setText("51.503902");
 frame.lon.setText("A123.123");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 fail("An exception of type NumberFormatException was expected.");
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidAngle() {
 // Given an invalid angle
 frame.theta.setText("ABC");
 frame.lat.setText("51.503902");
 frame.lon.setText("-0.127767");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 fail("An exception of type NumberFormatException was expected.");
 }

 @Test
 public void testError() {
 // Given an error message
 frame.error("Can't Find the file");
 // Check if an error dialog is displayed
 assertTrue(JOptionPane.showConfirmDialog(frame, "Error", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.CLOSED_OPTION || JOptionPane.showConfirmDialog(frame, "Error", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.CANCEL_OPTION);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksWithMissingFile() {
 // Given a missing file
 frame.loadLandmarks();
 fail("An exception of type FileNotFoundException was expected.");
 }

 @Test
 public void testLoadLandmarks() {
 // Given a valid landmarks.cfg file
 // Assuming the file content is:
 // FILE icon1.png
 // LAT 51.503902
 // LON -0.127767
 // FILE icon2.png
 // LAT 51.503915
 // LON -0.127769
 // ...

 frame.loadLandmarks();

 // Check if the number of landmarks is correct
 assertEquals(2, frame.display.getLandmarks().size());

 // Check if the landmarks are added to the display
 Landmark landmark1 = frame.display.getLandmarks().elementAt(0);
 assertEquals("icon1.png", landmark1.getIcon().getDescription());
 assertEquals(51.503902, landmark1.getLatitude(), 0.0001);
 assertEquals(-0.127767, landmark1.getLongitude(), 0.0001);

 Landmark landmark2 = frame.display.getLandmarks().elementAt(1);
 assertEquals("icon2.png", landmark2.getIcon().getDescription());
 assertEquals(51.503915, landmark2.getLatitude(), 0.0001);
 assertEquals(-0.127769, landmark2.getLongitude(), 0.0001);
 }
}