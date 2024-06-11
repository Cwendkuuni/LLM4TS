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
 public void testFoxHuntFrameConstruction() {
 assertEquals("Triangulation", frame.getTitle());
 assertTrue(frame.isVisible());
 }

 @Test
 public void testAddPointActionPerformed() {
 // Set up test values
 frame.theta.setText("45");
 frame.lat.setText("51.506958");
 frame.lon.setText("-0.125714");

 // Simulate button click
 ActionEvent actionEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(actionEvent);

 // Check if point was added to list
 assertEquals(1, frame.points.size());

 // Check if new point was added to display
 assertEquals(1, frame.display.getPoints().size());

 // Check if fox was calculated and added to list
 assertEquals(1, frame.fox.size());
 }

 @Test
 public void testAddMultiplePoints() {
 // Set up test values for first point
 frame.theta.setText("45");
 frame.lat.setText("51.506958");
 frame.lon.setText("-0.125714");

 // Simulate button click
 ActionEvent actionEventFirstPoint = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(actionEventFirstPoint);

 // Set up test values for second point
 frame.theta.setText("60");
 frame.lat.setText("51.532345");
 frame.lon.setText("-0.132432");

 // Simulate button click
 ActionEvent actionEventSecondPoint = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(actionEventSecondPoint);

 // Check if two points were added to list
 assertEquals(2, frame.points.size());

 // Check if two points were added to display
 assertEquals(2, frame.display.getPoints().size());

 // Check if fox was calculated and added to list
 assertEquals(1, frame.fox.size());
 }

 @Test
 public void testLoadLandmarks() {
 // Stub out display's addLandMark method
 frame.display = new HuntDisplay() {
 @Override
 public void addLandMark(Landmark landmark) {
 // Do nothing
 }
 };

 // Set up test values for landmarks.cfg file
 String landmarksCfg = "NUM 2\nFILE icon1.gif\nLAT 51.506958\nLON -0.125714\nFILE icon2.gif\nLAT 51.532345\nLON -0.132432";

 // Replace file input stream with test input stream
 System.setIn(new ByteArrayInputStream(landmarksCfg.getBytes()));

 // Call loadLandmarks method
 frame.loadLandmarks();

 // Check if two landmarks were added to display
 assertEquals(2, frame.display.getLandmarks().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadLandmarksInvalidFile() {
 // Replace file input stream with test input stream containing invalid file format
 System.setIn(new ByteArrayInputStream("INVALID_FORMAT".getBytes()));

 // Call loadLandmarks method
 frame.loadLandmarks();
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksFileNotFound() {
 // Call loadLandmarks method with file not found exception
 frame.loadLandmarks();
 }

 @Test
 public void testFoxTableModelData() {
 // Set up test data for table model
 TrigPoint point1 = new TrigPoint(51.506958, -0.125714, 45);
 TrigPoint point2 = new TrigPoint(51.532345, -0.132432, 60);
 frame.points = new Vector<TrigPoint>() {
 {
 add(point1);
 add(point2);
 }
 };
 frame.posModel = new FoxTableModel();
 frame.pos = new JTable(frame.posModel);

 // Get data from table
 Object[][] data = (Object[][]) frame.posModel.getData();

 // Check if data contains correct values
 assertEquals("Lattitude", data[0][0]);
 assertEquals("Longitude", data[0][1]);
 assertEquals(51.506958, data[0][0], 0.00001);
 assertEquals(-0.125714, data[0][1], 0.00001);

 // Check if data contains correct values for second row
 assertEquals("Lattitude", data[1][0]);
 assertEquals("Longitude", data[1][1]);
 assertEquals(51.532345, data[1][0], 0.00001);
 assertEquals(-0.132432, data[1][1], 0.00001);
 }
}