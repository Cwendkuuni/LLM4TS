package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import Joshua.FoxHunt.*;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.points);
 assertNotNull(frame.fox);
 }

 @Test
 public void testActionPerformedAddPoint() {
 final int angle = 180;
 final double lat = 40.7128;
 final double lon = -74.0060;
 frame.theta.setText(String.valueOf(angle));
 frame.lat.setText(String.format("%.6f", lat));
 frame.lon.setText(String.format("%.6f", lon));
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 final int sizeBefore = frame.points.size();
 final int sizeAfter = frame.points.size();
 assertEquals(sizeBefore + 1, sizeAfter);
 final TrigPoint expectedPoint = new TrigPoint(lat, lon, angle);
 assertTrue(frame.points.contains(expectedPoint));
 }

 @Test(expected = NumberFormatException.class)
 public void testActionPerformedAddPointInvalidData() {
 frame.theta.setText("123");
 frame.lat.setText("asd");
 frame.lon.setText("sda");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 }

 @Test
 public void testAddPointToDisplay() {
 final int angle = 180;
 final double lat = 40.7128;
 final double lon = -74.0060;
 frame.theta.setText(String.valueOf(angle));
 frame.lat.setText(String.format("%.6f", lat));
 frame.lon.setText(String.format("%.6f", lon));
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 final int sizeBefore = frame.fox.size();
 final int sizeAfter = frame.fox.size();
 assertEquals(sizeBefore + 1, sizeAfter);
 final TrigPoint expectedFox = FoxCalc.calcFox(new TrigPoint(lat, lon, angle), frame.points.lastElement());
 assertTrue(frame.fox.contains(expectedFox));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsCellEditable() {
 final JTable table = frame.pos;
 final int row = 0;
 final int column = 0;
 table.setValueAt("", table, row, column);
 table.getModel().fireTableDataChanged(row, column);
 table.setCellEditor(null);
 table.editCellAt(row, column);
 }

 @Test
 public void testTableDisplayData() {
 final int angle1 = 180;
 final double lat1 = 40.7128;
 final double lon1 = -74.0060;
 final int angle2 = 270;
 final double lat2 = 40.7218;
 final double lon2 = -74.0070;
 frame.theta.setText(String.valueOf(angle1));
 frame.lat.setText(String.format("%.6f", lat1));
 frame.lon.setText(String.format("%.6f", lon1));
 frame.theta.setText(String.valueOf(angle2));
 frame.lat.setText(String.format("%.6f", lat2));
 frame.lon.setText(String.format("%.6f", lon2));
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 final Object valueAt00 = frame.pos.getValueAt(0, 0);
 final Object valueAt01 = frame.pos.getValueAt(0, 1);
 final Object valueAt10 = frame.pos.getValueAt(1, 0);
 final Object valueAt11 = frame.pos.getValueAt(1, 1);
 final DecimalFormat formatter = new DecimalFormat("###.##############");
 final String expectedValueAt00 = formatter.format(lat1);
 final String expectedValueAt01 = formatter.format(lon1);
 final String expectedValueAt10 = formatter.format(lat2);
 final String expectedValueAt11 = formatter.format(lon2);
 assertEquals(expectedValueAt00, valueAt00);
 assertEquals(expectedValueAt01, valueAt01);
 assertEquals(expectedValueAt10, valueAt10);
 assertEquals(expectedValueAt11, valueAt11);
 }

 @Test
 public void testLoadLandmarks() {
 final int expectedNumLandmarks = 3;
 final String[] expectedIconFiles = new String[] { "icon1.gif", "icon2.gif", "icon3.gif" };
 final double[] expectedLat = new double[] { 40.7218, 40.7128, 40.7118 };
 final double[] expectedLon = new double[] { -74.0070, -74.0060, -74.0050 };
 frame.loadLandmarks();
 final int numLandmarks = frame.display.getLandmarks().size();
 assertEquals(expectedNumLandmarks, numLandmarks);
 for (int i = 0; i < numLandmarks; ++i) {
 final Landmark landmark = frame.display.getLandmarks().get(i);
 final String iconFile = landmark.getIcon().getDescription();
 final double lat = landmark.getY();
 final double lon = landmark.getX();
 assertEquals(expectedIconFiles[i], iconFile);
 assertEquals(expectedLat[i], lat, 0.001);
 assertEquals(expectedLon[i], lon, 0.001);
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksFileNotFound() {
 frame = new FoxHuntFrame();
 frame.loadLandmarks();
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksIOException() {
 frame = new FoxHuntFrame() {
 @Override
 public void error(final String text) {
 fail();
 }
 };
 frame.loadLandmarks();
 }

}