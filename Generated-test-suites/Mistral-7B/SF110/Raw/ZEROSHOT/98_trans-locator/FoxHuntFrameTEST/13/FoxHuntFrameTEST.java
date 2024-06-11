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
 public void testFoxHuntFrameConstruction() {
 assertEquals("Triangulation", frame.getTitle());
 assertTrue(frame.isVisible());
 }

 @Test
 public void testActionPerformedAddPoint() {
 final String angle = "45";
 final double lat = 1.23456;
 final double lon = 123.45678;
 frame.theta.setText(angle);
 frame.lat.setText(String.format("%.6f", lat));
 frame.lon.setText(String.format("%.6f", lon));
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));

 final Vector<TrigPoint> points = frame.points;
 final Vector<TrigPoint> fox = frame.fox;

 assertEquals(1, points.size());
 assertEquals(0, fox.size());

 final TrigPoint point = (TrigPoint) points.lastElement();
 assertEquals(angle, String.valueOf(point.getAngle()));
 assertEquals(lat, point.getY(), 0.00001);
 assertEquals(lon, point.getX(), 0.00001);

 assertEquals(1, fox.size());
 final TrigPoint foxPoint = (TrigPoint) fox.lastElement();
 assertNotEquals(point.getX(), foxPoint.getX());
 assertNotEquals(point.getY(), foxPoint.getY());
 }

 @Test
 public void testAddMultiplePoints() {
 final String angle1 = "45";
 final double lat1 = 1.23456;
 final double lon1 = 123.45678;
 frame.theta.setText(angle1);
 frame.lat.setText(String.format("%.6f", lat1));
 frame.lon.setText(String.format("%.6f", lon1));
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));

 final String angle2 = "90";
 final double lat2 = 2.34567;
 final double lon2 = 234.56789;
 frame.theta.setText(angle2);
 frame.lat.setText(String.format("%.6f", lat2));
 frame.lon.setText(String.format("%.6f", lon2));
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));

 final Vector<TrigPoint> points = frame.points;
 final Vector<TrigPoint> fox = frame.fox;

 assertEquals(2, points.size());
 assertEquals(1, fox.size());

 final TrigPoint point1 = (TrigPoint) points.lastElement();
 assertEquals(angle1, String.valueOf(point1.getAngle()));
 assertEquals(lat1, point1.getY(), 0.00001);
 assertEquals(lon1, point1.getX(), 0.00001);

 final TrigPoint point2 = (TrigPoint) points.elementAt(points.size() - 2);
 assertEquals(angle2, String.valueOf(point2.getAngle()));
 assertEquals(lat2, point2.getY(), 0.00001);
 assertEquals(lon2, point2.getX(), 0.00001);

 final TrigPoint foxPoint = (TrigPoint) fox.lastElement();
 assertNotEquals(point1.getX(), foxPoint.getX());
 assertNotEquals(point1.getY(), foxPoint.getY());
 }

 @Test
 public void testTableModel() {
 final int numRows = frame.fox.size();
 final FoxTableModel model = frame.posModel;
 assertEquals(2, model.getColumnCount());
 assertEquals(numRows, model.getRowCount());
 assertArrayEquals(new String[]{"Lattitude", "Longitude"}, model.getColumnNames());
 }

 @Test(expected = AssertionError.class)
 public void testEmptyTable() {
 final FoxTableModel model = new FoxTableModel();
 final JTable table = new JTable(model);
 table.getCellEditor(0, 0).stopCellEditing();
}

}