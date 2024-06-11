package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.junit.After;
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

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 // Check if the frame is initialized with the correct title
 assertEquals("Triangulation", frame.getTitle());

 // Check if the frame is visible
 assertTrue(frame.isVisible());
 }

 @Test
 public void testActionPerformedAddPoint() {
 // Set up test data for addPoint method
 final JTextField theta = new JTextField("45");
 final JTextField lat = new JTextField("40.123456");
 final JTextField lon = new JTextField("-74.567890");
 final JButton add = new JButton("Add It!!!");
 add.addActionListener(frame);
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;
 frame.getContentPane().add(frame.panel, "Center");

 // Simulate a button click event
 ActionEvent event = new ActionEvent(add, 0, "ActionEvent");
 frame.actionPerformed(event);

 // Check if the point has been added to the points vector
 assertEquals(1, frame.points.size());

 // Check if the fox vector size has increased
 assertEquals(0, frame.fox.size());

 // Check if the table model's row count has increased
 TableModel model = frame.posModel;
 assertEquals(0, model.getRowCount());

 // Add a second point and check if the fox vector has the expected value
 theta.setText("90");
 lat.setText("41.234567");
 lon.setText("-75.678901");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;
 frame.getContentPane().add(frame.panel, "Center");
 frame.actionPerformed(event);
 assertEquals(2, frame.points.size());
 TrigPoint point1 = (TrigPoint) frame.points.elementAt(0);
 TrigPoint point2 = (TrigPoint) frame.points.elementAt(1);
 Landmark foxPoint = FoxCalc.calcFox(point2, point1);
 TrigPoint expectedFoxPoint = new TrigPoint(40.616667, -74.951523, 135.0);
 assertEquals(expectedFoxPoint.getX(), foxPoint.getX(), 0.00001);
 assertEquals(expectedFoxPoint.getY(), foxPoint.getY(), 0.00001);
 assertEquals(1, frame.fox.size());
 assertEquals(3, model.getRowCount());
 }

 @Test
 public void testAddLandMarks() {
 // Check if landmarks are loaded from the file
 assertTrue(frame.display.getLandMarkCount() > 0);
 }
}