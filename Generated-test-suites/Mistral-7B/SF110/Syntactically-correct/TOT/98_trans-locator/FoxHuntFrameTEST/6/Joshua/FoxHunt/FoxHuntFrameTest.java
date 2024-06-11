package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.TrigPoint;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testAddPoint_ValidValues() {
 // Set up text fields with valid values
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("0.1278");

 // Simulate button click
 ActionEvent e = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(e);

 // Verify that a new point has been added to the vector and display
 assertEquals(2, frame.points.size());
 assertEquals(1, frame.fox.size());
 }

 @Test
 public void testAddPoint_InvalidValues_MissingTheta() {
 // Set up text fields with invalid values (missing theta)
 frame.lat.setText("51.5074");
 frame.lon.setText("0.1278");
 frame.theta.setText("");

 // Simulate button click
 ActionEvent e1 = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(e1);

 // Verify that nothing has been added to the vectors and display is unchanged
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());
 }

 @Test
 public void testAddPoint_InvalidValues_InvalidLat() {
 // Set up text fields with invalid values (invalid lat)
 frame.theta.setText("45");
 frame.lat.setText("abc");
 frame.lon.setText("0.1278");

 // Simulate button click
 ActionEvent e2 = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(e2);

 // Verify that nothing has been added to the vectors and display is unchanged
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());
 }

 @Test
 public void testAddPoint_InvalidValues_InvalidLon() {
 // Set up text fields with invalid values (invalid lon)
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("abc");

 // Simulate button click
 ActionEvent e3 = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(e3);

 // Verify that nothing has been added to the vectors and display is unchanged
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());
 }

 @Test
 public void testAddPoint_InvalidValues_EmptyPointsVector() {
 // Set up text fields with valid values
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("0.1278");

 // Remove the last point from the points vector to simulate an empty vector before adding a new point
 frame.points.remove(frame.points.size() - 1);

 // Simulate button click
 ActionEvent e4 = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(e4);

 // Verify that a new point has been added to the vector and display
 assertEquals(1, frame.points.size());
 assertEquals(0, frame.fox.size());
 }
}