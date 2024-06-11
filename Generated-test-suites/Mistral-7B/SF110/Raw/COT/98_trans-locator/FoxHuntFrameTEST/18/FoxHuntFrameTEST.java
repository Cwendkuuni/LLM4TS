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
 // Test constructor
 assertNotNull(frame);
 }

 @Test
 public void testActionPerformed() {
 // Test addPoint method when there are already two points
 String theta = "30";
 String lat = "40.123456";
 String lon = "71.123456";
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);
 ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 frame.actionPerformed(event);

 // Check if the points vector size is 2
 assertEquals(2, frame.points.size());

 // Check if the fox vector size is 1
 assertEquals(1, frame.fox.size());
 }

 @Test(expected = NumberFormatException.class)
 public void testActionPerformedWithInvalidTheta() {
 // Test addPoint method with invalid theta input
 frame.theta.setText("abc");
 ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 frame.actionPerformed(event);
 fail("Expected NumberFormatException not thrown");
 }

 @Test(expected = NumberFormatException.class)
 public void testActionPerformedWithInvalidLat() {
 // Test addPoint method with invalid lat input
 frame.theta.setText("45");
 frame.lat.setText("ddd.mm.mmm");
 ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 frame.actionPerformed(event);
 fail("Expected NumberFormatException not thrown");
 }

 @Test(expected = NumberFormatException.class)
 public void testActionPerformedWithInvalidLon() {
 // Test addPoint method with invalid lon input
 frame.theta.setText("45");
 frame.lat.setText("40.123456");
 frame.lon.setText("ddd.ddd.ddd");
 ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add Point");
 frame.actionPerformed(event);
 fail("Expected NumberFormatException not thrown");
 }

 @Test
 public void testLoadLandmarks() {
 // Test loadLandmarks method
 frame.loadLandmarks();
 // Check if the display has the correct number of landmarks
 assertEquals(4, frame.display.getLandMarkCount());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksWithMissingFile() {
 // Test loadLandmarks method with missing file
 frame.loadLandmarks();
 fail("Expected FileNotFoundException not thrown");
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksWithIOException() {
 // Test loadLandmarks method with IOException
 frame.loadLandmarks();
 fail("Expected IOException not thrown");
 }

 @Test
 public void testGettingDataFromFoxTableModel() {
 // Test getting data from FoxTableModel
 FoxTableModel model = frame.posModel;
 assertEquals(2, model.getRowCount());
 assertEquals("40.123456", model.getValueAt(0, 0).toString());
 assertEquals("71.123456", model.getValueAt(0, 1).toString());
 assertEquals("45.0", model.getValueAt(1, 0).toString());
 assertEquals("71.123456", model.getValueAt(1, 1).toString());
 }

}