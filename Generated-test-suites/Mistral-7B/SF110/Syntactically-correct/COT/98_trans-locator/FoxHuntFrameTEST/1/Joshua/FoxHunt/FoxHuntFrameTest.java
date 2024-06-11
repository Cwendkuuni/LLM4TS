package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Test
 public void testFoxHuntFrameConstructor() {
  frame = new FoxHuntFrame();
  assertNotNull(frame.getContentPane());
 }

 @Test
 public void testActionPerformed() {
  frame = new FoxHuntFrame();
  frame.theta.setText("45");
  frame.lat.setText("40.7128");
  frame.lon.setText("-74.0060");
  ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
  frame.actionPerformed(event);
  assertEquals(2, frame.points.size());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTheta() {
  frame = new FoxHuntFrame();
  frame.theta.setText("abc");
  ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
  frame.actionPerformed(event);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidLat() {
  frame = new FoxHuntFrame();
  frame.theta.setText("45");
  frame.lat.setText("ddd.mm.mmm");
  ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
  frame.actionPerformed(event);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidLon() {
  frame = new FoxHuntFrame();
  frame.theta.setText("45");
  frame.lat.setText("40.7128");
  frame.lon.setText("ddd.ddd.ddd");
  ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
  frame.actionPerformed(event);
 }

 @Test
 public void testAddPointWithTwoPoints() {
  frame = new FoxHuntFrame();
  frame.theta.setText("45");
  frame.lat.setText("40.7128");
  frame.lon.setText("-74.0060");
  ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
  frame.actionPerformed(event);
  frame.theta.setText("30");
  frame.lat.setText("40.7145");
  frame.lon.setText("-74.0062");
  ActionEvent event2 = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
  frame.actionPerformed(event2);
  assertEquals(3, frame.points.size());
 }

 @Test
 public void testLoadLandmarks() {
  frame = new FoxHuntFrame();
  frame.loadLandmarks();
  assertTrue(frame.display.getLandmarks().size() > 0);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksFileNotFound() {
  frame = new FoxHuntFrame();
  frame.loadLandmarks();
  assertEquals(0, frame.display.getLandmarks().size());
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksIOException() {
  frame = new FoxHuntFrame();
  String fileName = "nonexistentfile.cfg";
  frame.loadLandmarks(fileName);
  assertEquals(0, frame.display.getLandmarks().size());
 }

 @Test
 public void testFoxTableModelGetValueAt() {
  frame = new FoxHuntFrame();
  frame.loadLandmarks();
  FoxTableModel model = (FoxTableModel) frame.posModel;
  assertEquals("40.7128", model.getValueAt(0, 0).toString());
  assertEquals("-74.0060", model.getValueAt(0, 1).toString());
 }
}