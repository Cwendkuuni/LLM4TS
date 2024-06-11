package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setup() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.getContentPane());
 }

 @Test
 public void testAddPointMethod() {
 frame.theta.setText("10");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");
 frame.add.doClick();
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidTheta() {
 frame.theta.setText("abc");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");
 frame.add.doClick();
 fail("Expected NumberFormatException but no exception was thrown.");
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidLat() {
 frame.theta.setText("10");
 frame.lat.setText("40.7128xyz");
 frame.lon.setText("-74.0060");
 frame.add.doClick();
 fail("Expected NumberFormatException but no exception was thrown.");
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidLon() {
 frame.theta.setText("10");
 frame.lat.setText("40.7128");
 frame.lon.setText("123.456xyz");
 frame.add.doClick();
 fail("Expected NumberFormatException but no exception was thrown.");
 }

 @Test
 public void testAddTwoPoints() {
 frame.theta.setText("10");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");
 frame.add.doClick();
 frame.theta.setText("20");
 frame.lat.setText("35.7588");
 frame.lon.setText("-100.4567");
 frame.add.doClick();
 assertEquals(2, frame.points.size());
 assertEquals(2, frame.fox.size());
 }

 @Test
 public void testLoadLandmarksMethod() {
 frame.loadLandmarks();
 assertTrue(frame.display.getLandmarkCount() > 0);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksMethodWithMissingFile() {
 frame.loadLandmarks();
 fail("Expected FileNotFoundException but no exception was thrown.");
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksMethodWithIOException() {
 frame.loadLandmarks();
 fail("Expected IOException but no exception was thrown.");
 }

 @Test
 public void testTableDataChanged() {
 frame.addPoint();
 frame.posModel.fireTableDataChanged();
 assertTrue(frame.pos.getModel() instanceof FoxTableModel);
 }

 @Test
 public void testFoxTableModelGetValueAt() {
 TrigPoint firstPoint = (TrigPoint) frame.points.elementAt(0);
 assertEquals(firstPoint.getY(), frame.posModel.getValueAt(0, 0));
 assertEquals(firstPoint.getX(), frame.posModel.getValueAt(0, 1));
 }
}