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
 assertTrue(frame instanceof FoxHuntFrame);
 assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testActionPerformedWithValidInput() {
 final ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Add It!!!");
 frame.actionPerformed(actionEvent);
 final int angle = 45;
 final double lat = 40.7128;
 final double lon = -74.0060;
 final TrigPoint expectedPoint = new TrigPoint(lat, lon, angle);
 assertEquals(1, frame.points.size());
 assertEquals(expectedPoint, frame.points.lastElement());
 }

 @Test(expected = NumberFormatException.class)
 public void testActionPerformedWithInvalidAngleInput() {
 final ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Add It!!!");
 frame.theta.setText("abc");
 frame.theta.setCaretPosition(0);
 frame.actionPerformed(actionEvent);
 final Vector<TrigPoint> points = frame.points;
 assertEquals(0, points.size());
 }

 @Test(expected = NumberFormatException.class)
 public void testActionPerformedWithInvalidLatitudeInput() {
 final ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Add It!!!");
 frame.lat.setText("ddd*mm.mmm");
 frame.lat.setCaretPosition(0);
 frame.actionPerformed(actionEvent);
 final Vector<TrigPoint> points = frame.points;
 assertEquals(0, points.size());
 }

 @Test(expected = NumberFormatException.class)
 public void testActionPerformedWithInvalidLongitudeInput() {
 final ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Add It!!!");
 frame.lon.setText("dddd*mm.mmm");
 frame.lon.setCaretPosition(0);
 frame.actionPerformed(actionEvent);
 final Vector<TrigPoint> points = frame.points;
 assertEquals(0, points.size());
 }

 @Test
 public void testAddPointWithTwoPoints() {
 final ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Add It!!!");
 frame.theta.setText("45");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");
 frame.theta.setCaretPosition(0);
 frame.lat.setCaretPosition(0);
 frame.lon.setCaretPosition(0);
 frame.actionPerformed(actionEvent);
 final ActionEvent actionEvent1 = new ActionEvent(new Object(), 0, "Add It!!!");
 frame.theta.setText("30");
 frame.lat.setText("37.7749");
 frame.lon.setText("-122.4194");
 frame.theta.setCaretPosition(0);
 frame.lat.setCaretPosition(0);
 frame.lon.setCaretPosition(0);
 frame.actionPerformed(actionEvent1);
 final Vector<TrigPoint> expectedFox = new Vector<>();
 final TrigPoint firstPoint = new TrigPoint(40.7128, -74.0060, 45);
 final TrigPoint secondPoint = new TrigPoint(37.7749, -122.4194, 30);
 final TrigPoint expectedFoxPoint = FoxCalc.calcFox(secondPoint, firstPoint);
 assertEquals(3, frame.points.size());
 assertEquals(expectedFoxPoint, frame.fox.lastElement());
 }

 @Test
 public void testLoadLandmarks() {
 final int numLandmarks = 3;
 final String[] iconFile = new String[numLandmarks];
 final double[] lat = new double[numLandmarks];
 final double[] lon = new double[numLandmarks];
 final Landmark[] landmarks = new Landmark[numLandmarks];
 for (int i = 0; i < numLandmarks; i++) {
 iconFile[i] = "icon" + (i + 1) + ".gif";
 lat[i] = 40.7128 + i;
 lon[i] = -74.0060 + i;
 landmarks[i] = new Landmark(lat[i], lon[i], new ImageIcon(iconFile[i]));
 }
 frame.display.addLandMark(landmarks[0]);
 frame.display.addLandMark(landmarks[1]);
 frame.display.addLandMark(landmarks[2]);
 final Object[] landmarksArray = frame.display.getLandmarks().toArray();
 for (int i = 0; i < numLandmarks; i++) {
 assertEquals(landmarks[i], landmarksArray[i]);
 }
 }
}