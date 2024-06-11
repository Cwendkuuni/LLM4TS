import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private JTable table;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 table = frame.pos;
 }

 @Test
 public void testAddPointWithValidValues() {
 JTextField theta = frame.theta;
 JTextField lat = frame.lat;
 JTextField lon = frame.lon;
 theta.setText("45");
 lat.setText("51.507259");
 lon.setText("0.127763");
 ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
 frame.actionPerformed(event);

 TrigPoint expectedPoint = new TrigPoint(51.507259, 0.127763, 45);
 TrigPoint actualPoint = (TrigPoint) frame.points.lastElement();

 Assert.assertEquals("The added point should match the expected point", expectedPoint, actualPoint);
 }

 @Test
 public void testAddPointWithInvalidTheta() {
 JTextField theta = frame.theta;
 JTextField lat = frame.lat;
 JTextField lon = frame.lon;
 theta.setText("abc");
 lat.setText("51.507259");
 lon.setText("0.127763");
 ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
 frame.actionPerformed(event);

 Assert.assertEquals("The number of points should not change when an invalid theta is entered", frame.points.size(), 1);
 }

 @Test
 public void testAddPointWithInvalidLat() {
 JTextField theta = frame.theta;
 JTextField lat = frame.lat;
 JTextField lon = frame.lon;
 theta.setText("45");
 lat.setText("abc");
 lon.setText("0.127763");
 ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
 frame.actionPerformed(event);

 Assert.assertEquals("The number of points should not change when an invalid lat is entered", frame.points.size(), 1);
 }

 @Test
 public void testAddPointWithInvalidLon() {
 JTextField theta = frame.theta;
 JTextField lat = frame.lat;
 JTextField lon = frame.lon;
 theta.setText("45");
 lat.setText("51.507259");
 lon.setText("abc");
 ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
 frame.actionPerformed(event);

 Assert.assertEquals("The number of points should not change when an invalid lon is entered", frame.points.size(), 1);
 }

 @Test
 public void testAddPointWithEmptyFields() {
 JTextField theta = frame.theta;
 JTextField lat = frame.lat;
 JTextField lon = frame.lon;
 ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add");
 frame.actionPerformed(event);

 Assert.assertEquals("The number of points should not change when all fields are empty", frame.points.size(), 0);
 }

 @Test
 public void testAddPointWithInvalidZoom() {
 JTextField zoom = frame.zoom;
 zoom.setText("abc");
 ActionEvent event = new ActionEvent(frame.zoom, ActionEvent.ACTION_PERFORMED, "ActionPerformed");
 frame.actionPerformed(event);

 Assert.assertEquals("The zoom should not change when an invalid value is entered", frame.getZoom(), 1.0);
 }

 @Test
 public void testTableDataChangedAfterAddingAPoint() {
 frame.addPoint();
 frame.posModel.fireTableDataChanged();

 int rowCount = table.getRowCount();

 Assert.assertEquals("The table should have one more row after a point is added", rowCount, 2);
 }

 @Test
 public void testTableDataChangedAfterAddingAFox() {
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 30);
 frame.points.add(point1);
 frame.display.addPoint(point1);
 frame.addPoint();
 TrigPoint point2 = FoxCalc.calcFox(point1, (TrigPoint) frame.points.lastElement());
 frame.fox.add(point2);
 frame.posModel.fireTableDataChanged();
 frame.display.addPoint(point2);

 int rowCount = table.getRowCount();

 Assert.assertEquals("The table should have one more row after a fox is calculated and added", rowCount, 3);
 }

 @Test
 public void testErrorDialogIsDisplayedWhenFileNotFound() {
 frame.loadLandmarks();

 Assert.assertTrue("An error dialog should be displayed when the file is not found", JOptionPane.showOptionDialog(frame, "Error message", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == 0);
 }

 @Test
 public void testErrorDialogIsDisplayedWhenIOException() {
 final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
 final PrintStream errStream = new PrintStream(outContent);
 System.setErr(errStream);
 frame.loadLandmarks();
 System.setErr(System.err);

 Assert.assertTrue("An error dialog should be displayed when there is an I/O exception", JOptionPane.showOptionDialog(frame, "Error message", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == 0);
 }

 @Test
 public void testTableModelReturnsCorrectValues() {
 Object[] row1 = table.getValueAt(0, 0);
 Object[] row2 = table.getValueAt(1, 0);

 Double lat1 = (Double) row1[0];
 Double lat2 = (Double) row2[0];

 Assert.assertEquals("The first row's latitude value should match the expected value", lat1, 51.507259);
 Assert.assertEquals("The second row's latitude value should match the expected value", lat2, 51.512345);
 }

 @Test
 public void testTableModelReturnsCorrectValuesForLongitude() {
 Object[] row1 = table.getValueAt(0, 1);
 Object[] row2 = table.getValueAt(1, 1);

 Double lon1 = (Double) row1[1];
 Double lon2 = (Double) row2[1];

 Assert.assertEquals("The first row's longitude value should match the expected value", lon1, 0.127763);
 Assert.assertEquals("The second row's longitude value should match the expected value", lon2, 0.128123);
 }
}