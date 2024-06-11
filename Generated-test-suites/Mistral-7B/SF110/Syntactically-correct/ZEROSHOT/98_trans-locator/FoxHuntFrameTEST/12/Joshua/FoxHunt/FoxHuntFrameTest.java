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
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.getIconImage());
 }

 @Test
 public void testAddPoint() {
 // Set text fields with valid values
 frame.theta.setText("45");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");

 // Simulate button click
 ActionEvent e = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(e);

 // Check if point was added to points vector
 assertEquals(1, frame.points.size());

 // Check if point was added to fox vector
 assertEquals(1, frame.fox.size());

 // Check if table was updated
 FoxTableModel tableModel = (FoxTableModel) frame.pos.getModel();
 assertEquals(1, tableModel.getRowCount());

 // Check if display was updated
 Landmark landmark = frame.display.getComponentAt(0, 0);
 assertNotNull(landmark);
 TrigPoint expectedPoint = new TrigPoint(40.7128, -74.006, 45);
 assertEquals(expectedPoint, landmark.getPoint());
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidTheta() {
 // Set text fields with invalid values
 frame.theta.setText("abc");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");

 // Simulate button click
 ActionEvent e = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(e);

 // Verify NumberFormatException is thrown
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLat() {
 // Set text fields with invalid values
 frame.theta.setText("45");
 frame.lat.setText("abc");
 frame.lon.setText("-74.0060");

 // Simulate button click
 ActionEvent e = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(e);

 // Verify NumberFormatException is thrown
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLon() {
 // Set text fields with invalid values
 frame.theta.setText("45");
 frame.lat.setText("40.7128");
 frame.lon.setText("abc");

 // Simulate button click
 ActionEvent e = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(e);

 // Verify NumberFormatException is thrown
 }

 @Test
 public void testAddPointWithExistingPoints() {
 // Set text fields with valid values
 frame.theta.setText("45");
 frame.lat.setText("41.8781");
 frame.lon.setText("-87.6298");

 // Add first point
 ActionEvent e1 = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(e1);

 // Check if first point was added
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());

 // Set text fields with valid values
 frame.theta.setText("35");
 frame.lat.setText("37.7749");
 frame.lon.setText("-122.4194");

 // Add second point
 ActionEvent e2 = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(e2);

 // Check if second point was added
 assertEquals(2, frame.points.size());
 assertEquals(2, frame.fox.size());

 // Check if fox point was calculated correctly
 TrigPoint foxPoint1 = (TrigPoint) frame.fox.elementAt(0);
 TrigPoint foxPoint2 = (TrigPoint) frame.fox.elementAt(1);
 assertEquals(-13.2033, foxPoint1.getY(), 0.001);
 assertEquals(-122.4146, foxPoint1.getX(), 0.001);
 assertEquals(33.4526, foxPoint2.getY(), 0.001);
 assertEquals(-119.8653, foxPoint2.getX(), 0.001);
 }
}