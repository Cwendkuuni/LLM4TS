package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private FoxTableModel tableModel;
 private Vector<TrigPoint> points;
 private Vector<TrigPoint> fox;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 tableModel = frame.posModel;
 points = frame.points;
 fox = frame.fox;
 }

 @Test
 public void testFoxHuntFrameConstruction() {
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.getContentPane());
 }

 @Test
 public void testAddPoint() {
 // Testing with valid input
 frame.theta.setText("120");
 frame.lat.setText("42.213612");
 frame.lon.setText("-87.628851");
 ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(event);

 TrigPoint expectedPoint = new TrigPoint(42.213612, -87.628851, 120);
 assertTrue(points.contains(expectedPoint));
 assertTrue(fox.size() == 1);
 TrigPoint expectedFox = FoxCalc.calcFox(expectedPoint, FoxHuntFrame.this.points.elementAt(points.size() - 2));
 assertTrue(fox.contains(expectedFox));
 tableModel.fireTableDataChanged();

 // Testing with invalid input
 frame.theta.setText("abc");
 frame.lat.setText("123.456");
 frame.lon.setText("123.456abc");
 event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(event);

 assertFalse(points.contains(expectedPoint));
 assertFalse(fox.size() > 1);
 }

 @Test
 public void testFinalPrep() {
 assertNotNull(frame.zoom);
 assertNotNull(frame.panel3);
 assertNotNull(frame.panel);
 assertNotNull(frame.posModel);
 assertNotNull(frame.pos);
 assertNotNull(frame.display);
 }

 @Test
 public void testTextPrep() {
 assertNotNull(frame.theta);
 assertNotNull(frame.lat);
 assertNotNull(frame.lon);
 assertNotNull(frame.add);
 assertNotNull(frame.panel1);
 }

 @Test
 public void testTablePrep() {
 assertNotNull(tableModel);
 assertNotNull(frame.pos);
 }

 @Test
 public void testDisplayPrep() {
 assertNotNull(frame.display);
 }

 @Test
 public void testLoadLandmarks() {
 try {
 frame.loadLandmarks();
 HuntDisplay display = frame.display;
 assertEquals(5, display.landMarks.size());
 } catch (Exception e) {
 fail("Test failed: " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadLandmarksWithInvalidFile() {
 frame.loadLandmarks(null);
 }

}