package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

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
 frame.dispose();
 }

 @Test
 public void testFoxHuntFrameConstruction() {
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.theta);
 assertNotNull(frame.lat);
 assertNotNull(frame.lon);
 assertNotNull(frame.zoom);
 assertNotNull(frame.add);
 assertNotNull(frame.pos);
 assertNotNull(frame.posModel);
 assertNotNull(frame.display);
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());
 }

 @Test
 public void testAddPoint() {
 frame.theta.setText("45");
 frame.lat.setText("37.424242");
 frame.lon.setText("122.343434");
 frame.actionPerformed(new ActionEvent(frame.add, 0, ""));
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 assertEquals("37.424242", ((TrigPoint) frame.points.lastElement()).getY());
 assertEquals("122.343434", ((TrigPoint) frame.points.lastElement()).getX());
 assertNotNull(frame.display.getLandmarkAt(0));
 assertFalse(frame.display.isFoxVisible());
 }

 @Test
 public void testAddMultiplePoints() {
 frame.theta.setText("45");
 frame.lat.setText("37.424242");
 frame.lon.setText("122.343434");
 frame.actionPerformed(new ActionEvent(frame.add, 0, ""));
 frame.theta.setText("120");
 frame.lat.setText("32.323232");
 frame.lon.setText("118.121212");
 frame.actionPerformed(new ActionEvent(frame.add, 0, ""));
 assertEquals(2, frame.points.size());
 assertEquals(2, frame.fox.size());
 assertEquals("37.424242", ((TrigPoint) frame.points.lastElement()).getY());
 assertEquals("122.343434", ((TrigPoint) frame.points.lastElement()).getX());
 assertEquals("32.323232", ((TrigPoint) frame.points.elementAt(0)).getY());
 assertEquals("118.121212", ((TrigPoint) frame.points.elementAt(0)).getX());
 assertTrue(frame.display.isFoxVisible());
 }

 @Test
 public void testLoadLandmarks() {
 frame.loadLandmarks();
 assertFalse(frame.display.isEmpty());
 }

 @Test
 public void testError() {
 frame.error("Test Error");
 fail("Error dialog should be shown");
 }

 @Test
 public void testFoxTableModel() {
 FoxTableModel model = frame.posModel;
 assertEquals(0, model.getRowCount());
 assertEquals("Lattitude", model.getColumnName(0));
 assertEquals("Longitude", model.getColumnName(1));
 assertEquals("37.424242", model.getValueAt(0, 0).toString());
 assertEquals("122.343434", model.getValueAt(0, 1).toString());
 }
}