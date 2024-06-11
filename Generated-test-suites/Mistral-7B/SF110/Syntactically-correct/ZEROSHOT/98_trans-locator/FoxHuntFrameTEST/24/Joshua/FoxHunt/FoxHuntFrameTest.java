package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testConstructor() {
 assertNotNull(frame);
 assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testAddPoint() {
 final String theta = "30";
 final String lat = "37.423210";
 final String lon = "-122.083575";
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 final Vector<TrigPoint> points = frame.points;
 final TrigPoint expectedPoint = new TrigPoint(Double.parseDouble(lat), Double.parseDouble(lon), Integer.parseInt(theta));
 assertEquals(1, points.size());
 assertEquals(expectedPoint, points.get(0));
 }

 @Test
 public void testAddMultiplePoints() {
 final String theta1 = "30";
 final String lat1 = "37.423210";
 final String lon1 = "-122.083575";
 final String theta2 = "60";
 final String lat2 = "37.412310";
 final String lon2 = "-122.084575";
 frame.theta.setText(theta1);
 frame.lat.setText(lat1);
 frame.lon.setText(lon1);
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 frame.theta.setText(theta2);
 frame.lat.setText(lat2);
 frame.lon.setText(lon2);
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 final Vector<TrigPoint> points = frame.points;
 final TrigPoint expectedPoint1 = new TrigPoint(Double.parseDouble(lat1), Double.parseDouble(lon1), Integer.parseInt(theta1));
 final TrigPoint expectedPoint2 = new TrigPoint(Double.parseDouble(lat2), Double.parseDouble(lon2), Integer.parseInt(theta2));
 assertEquals(2, points.size());
 assertEquals(expectedPoint1, points.get(0));
 assertEquals(expectedPoint2, points.get(1));
 }

 @Test
 public void testDisplayAddPoint() {
 final String theta = "30";
 final String lat = "37.423210";
 final String lon = "-122.083575";
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 final HuntDisplay display = frame.display;
 final Vector<TrigPoint> points = frame.points;
 final TrigPoint expectedPoint = new TrigPoint(Double.parseDouble(lat), Double.parseDouble(lon), Integer.parseInt(theta));
 assertEquals(1, points.size());
 assertEquals(expectedPoint, points.get(0));
 assertEquals(1, display.getPoints().size());
 assertEquals(expectedPoint, display.getPoints().get(0));
 }

 @Test
 public void testDisplayAddMultiplePoints() {
 final String theta1 = "30";
 final String lat1 = "37.423210";
 final String lon1 = "-122.083575";
 final String theta2 = "60";
 final String lat2 = "37.412310";
 final String lon2 = "-122.084575";
 frame.theta.setText(theta1);
 frame.lat.setText(lat1);
 frame.lon.setText(lon1);
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 frame.theta.setText(theta2);
 frame.lat.setText(lat2);
 frame.lon.setText(lon2);
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 final HuntDisplay display = frame.display;
 final Vector<TrigPoint> points = frame.points;
 final TrigPoint expectedPoint1 = new TrigPoint(Double.parseDouble(lat1), Double.parseDouble(lon1), Integer.parseInt(theta1));
 final TrigPoint expectedPoint2 = new TrigPoint(Double.parseDouble(lat2), Double.parseDouble(lon2), Integer.parseInt(theta2));
 assertEquals(2, points.size());
 assertEquals(expectedPoint1, points.get(0));
 assertEquals(expectedPoint2, points.get(1));
 assertEquals(2, display.getPoints().size());
 assertEquals(expectedPoint1, display.getPoints().get(0));
 assertEquals(expectedPoint2, display.getPoints().get(1));
 }

 @Test
 public void testTableModel() {
 final FoxTableModel model = frame.posModel;
 assertEquals(2, model.getColumnCount());
 assertEquals(0, model.getRowCount());
 }

 @Test
 public void testTableModelAddPoint() {
 final String theta = "30";
 final String lat = "37.423210";
 final String lon = "-122.083575";
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 final FoxTableModel model = frame.posModel;
 assertEquals(2, model.getColumnCount());
 assertEquals(1, model.getRowCount());
 }

 @Test
 public void testTableModelAddMultiplePoints() {
 final String theta1 = "30";
 final String lat1 = "37.423210";
 final String lon1 = "-122.083575";
 final String theta2 = "60";
 final String lat2 = "37.412310";
 final String lon2 = "-122.084575";
 frame.theta.setText(theta1);
 frame.lat.setText(lat1);
 frame.lon.setText(lon1);
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 frame.theta.setText(theta2);
 frame.lat.setText(lat2);
 frame.lon.setText(lon2);
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 final FoxTableModel model = frame.posModel;
 assertEquals(2, model.getColumnCount());
 assertEquals(2, model.getRowCount());
 }

 @Test
 public void testTableModelGetValueAt() {
 final String theta = "30";
 final String lat = "37.423210";
 final String lon = "-122.083575";
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 final FoxTableModel model = frame.posModel;
 assertEquals("37.423210", model.getValueAt(0, 0).toString());
 assertEquals("-122.083575", model.getValueAt(0, 1).toString());
 }

 @Test
 public void testDisplay() {
 final HuntDisplay display = frame.display;
 assertNotNull(display);
 }

 @Test
 public void testLoadLandmarks() {
 final HuntDisplay display = frame.display;
 frame.loadLandmarks();
 final Vector<Landmark> landmarks = display.getLandmarks();
 assertNotNull(landmarks);
 assertEquals(2, landmarks.size());
 }
}