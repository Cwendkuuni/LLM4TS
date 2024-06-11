package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
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
 // Verify frame title
 assertEquals("Triangulation", frame.getTitle());
 // Verify default close operation
 assertEquals(JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
 // Verify initial size
 assertEquals(800, frame.getWidth());
 assertEquals(600, frame.getHeight());
 }

 @Test
 public void testActionPerformed() {
 // Simulate action event
 ActionEvent event = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(event);
 // Verify points vector size increased
 assertEquals(1, frame.points.size());
 // Verify display table size increased
 assertEquals(1, frame.posModel.getRowCount());
 }

 @Test
 public void testAddPoint() {
 // Test valid input
 frame.theta.setText("45");
 frame.lat.setText("40.123456");
 frame.lon.setText("-74.567890");
 frame.add.doClick();
 // Verify points vector size increased
 assertEquals(2, frame.points.size());
 // Verify display table size increased
 assertEquals(2, frame.posModel.getRowCount());
 // Test invalid input
 frame.theta.setText("abc");
 frame.lat.setText("abc");
 frame.lon.setText("abc");
 frame.add.doClick();
 // Verify points vector size did not change
 assertEquals(2, frame.points.size());
 // Verify display table size did not change
 assertEquals(2, frame.posModel.getRowCount());
 }

 @Test
 public void testFinalPrep() {
 // Verify zoom component added
 assertTrue(frame.panel3 instanceof JPanel);
 assertTrue(frame.panel3.getComponent(0) instanceof JTextField);
 // Verify display added to panel3
 assertTrue(frame.panel3.getComponent(1) instanceof HuntDisplay);
 }

 @Test
 public void testTextPrep() {
 // Verify text fields added to panel1
 assertEquals(4, frame.panel1.getComponentCount());
 // Verify add button added to panel1
 assertEquals(5, frame.panel1.getComponentCount());
 }

 @Test
 public void testTablePrep() {
 // Verify table model initialized
 assertEquals(2, frame.posModel.getColumnCount());
 // Verify table initialized
 assertTrue(frame.pos instanceof JTable);
 // Verify scroll pane initialized
 assertTrue(frame.pos.getComponent(0) instanceof JScrollPane);
 }

 @Test
 public void testDisplayPrep() {
 // Verify landmarks loaded
 assertEquals(10, frame.display.getLandMarkCount());
 }

 @Test
 public void testFoxTableModel() {
 FoxTableModel model = frame.posModel;
 // Verify column names
 assertEquals(2, model.getColumnCount());
 assertEquals("Lattitude", model.getColumnName(0));
 assertEquals("Longitude", model.getColumnName(1));
 // Verify row count
 assertEquals(2, model.getRowCount());
 // Verify data returned
 TrigPoint point1 = (TrigPoint) frame.points.elementAt(0);
 TrigPoint point2 = (TrigPoint) frame.fox.elementAt(0);
 assertEquals("40.123456", model.getValueAt(0, 0).toString());
 assertEquals("-74.567890", model.getValueAt(0, 1).toString());
 assertEquals("40.123456", model.getValueAt(1, 0).toString());
 assertEquals("-73.567890", model.getValueAt(1, 1).toString());
 }
}