package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.junit.After;
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

 @After
 public void tearDown() {
 SwingUtilities.invokeLater(frame::dispose);
 }

 @Test
 public void constructorTest() {
 assertEquals("Triangulation", frame.getTitle());
 assertTrue(frame.isVisible());
 assertNotNull(frame.getContentPane());
 }

 @Test
 public void actionPerformedTest() {
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Add It!!!");
 frame.actionPerformed(actionEvent);

 // Check if the number of points in the frame has increased by 1
 assertEquals(1, frame.points.size());

 // Check if the table model has a row added
 assertEquals(1, frame.posModel.getRowCount());

 // Check if the HuntDisplay has a new point added
 HuntDisplay display = frame.display;
 assertEquals(1, display.getPoints().size());
 }

 @Test
 public void addPointTest() {
 // Set the values of the text fields
 frame.theta.setText("45");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");
 SwingUtilities.invokeLater(() -> frame.add.doClick());

 // Check if the number of points in the frame has increased by 1
 assertEquals(2, frame.points.size());

 // Check if the table model has a row added
 assertEquals(2, frame.posModel.getRowCount());

 // Check if the Fox vector has a new point added
 assertEquals(1, frame.fox.size());

 // Check if the HuntDisplay has a new point added
 HuntDisplay display = frame.display;
 assertEquals(2, display.getPoints().size());
 }

 @Test
 public void finalPrepTest() {
 assertNotNull(frame.zoom);
 assertNotNull(frame.panel3);
 assertNotNull(frame.panel);
 }

 @Test
 public void textPrepTest() {
 assertNotNull(frame.theta);
 assertNotNull(frame.lat);
 assertNotNull(frame.lon);
 assertNotNull(frame.add);
 }

 @Test
 public void tablePrepTest() {
 assertNotNull(frame.posModel);
 assertNotNull(frame.pos);
 }

 @Test
 public void displayPrepTest() {
 HuntDisplay display = frame.display;
 assertNotNull(display);
 }

 @Test
 public void loadLandmarksTest() {
 HuntDisplay display = frame.display;
 assertEquals(3, display.getLandmarks().size());
 }
}