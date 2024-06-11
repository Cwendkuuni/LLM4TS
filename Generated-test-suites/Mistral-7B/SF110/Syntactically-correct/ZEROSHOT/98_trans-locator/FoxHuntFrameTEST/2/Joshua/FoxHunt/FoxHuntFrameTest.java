package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstruction() {
 assertNotNull(frame);
 assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testAddPoint() {
 // Set up initial text fields values
 frame.theta.setText("120");
 frame.lat.setText("51.505414");
 frame.lon.setText("-0.126977");

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
 frame.actionPerformed(actionEvent);

 // Check if point was added to the points vector
 assertEquals(1, frame.points.size());
 TrigPoint point = (TrigPoint) frame.points.lastElement();
 assertEquals(120, point.getAngle(), 0.1);
 assertEquals(51.505414, point.getY(), 0.0001);
 assertEquals(-0.126977, point.getX(), 0.0001);

 // Check if fox was added to the fox vector
 assertEquals(1, frame.fox.size());
 TrigPoint fox = (TrigPoint) frame.fox.lastElement();
 // Fox calculation is not implemented in the constructor test, so we can't assert its coordinates

 // Check if table model data was updated
 FoxTableModel tableModel = (FoxTableModel) frame.posModel;
 assertEquals(1, tableModel.getRowCount());
 Object latValue = tableModel.getValueAt(0, 0);
 assertEquals("51.505414", latValue.toString());
 Object lonValue = tableModel.getValueAt(0, 1);
 assertEquals("-0.126977", lonValue.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithInvalidTheta() {
 // Set up text fields with invalid value
 frame.theta.setText("abc");
 frame.lat.setText("51.505414");
 frame.lon.setText("-0.126977");

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
 frame.actionPerformed(actionEvent);

 // Check if exception was thrown
 fail("IllegalArgumentException expected");
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLatOrLon() {
 // Set up text fields with invalid values
 frame.theta.setText("120");
 frame.lat.setText("abc");
 frame.lon.setText("-0.126977");

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
 frame.actionPerformed(actionEvent);

 // Check if exception was thrown
 fail("NumberFormatException expected");
 }

 @Test
 public void testLoadLandmarks() {
 // Stub landmarks loading
 HuntDisplay display = new HuntDisplay();
 display.addLandMark(new Landmark(0.0, 0.0, null));
 //... other landmarks stubbing

 // Set up expectations for landmarks loading
 //...

 // Simulate landmarks loading
 frame.loadLandmarks();

 // Check if landmarks were added to the display
 assertEquals(expectedNumberOfLandmarks, display.getLandmarks().size());
 //... assert landmarks properties
 }

}