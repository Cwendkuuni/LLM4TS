package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

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
 assertNotNull(frame);
 assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testAddPoint() {
 frame.theta.setText("123");
 frame.lat.setText("45.123456");
 frame.lon.setText("78.654321");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, null));
 final int expectedSize = 1;
 assertEquals(expectedSize, frame.points.size());
 final int actualSize = frame.display.getPoints().size();
 assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testAddTwoPoints() {
 final int angle = 123;
 frame.theta.setText(Integer.toString(angle));
 frame.lat.setText("45.123456");
 frame.lon.setText("78.654321");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, null));
 final int expectedSize1 = 1;
 assertEquals(expectedSize1, frame.points.size());
 final int actualSize1 = frame.display.getPoints().size();
 assertEquals(expectedSize1, actualSize1);
 frame.theta.setText("246");
 frame.lat.setText("51.234567");
 frame.lon.setText("80.765432");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, null));
 final int expectedSize2 = 2;
 assertEquals(expectedSize2, frame.points.size());
 final int actualSize2 = frame.display.getPoints().size();
 assertEquals(expectedSize2, actualSize2);
 }

 @Test
 public void testTableDataChanged() {
 final int angle = 123;
 frame.theta.setText(Integer.toString(angle));
 frame.lat.setText("45.123456");
 frame.lon.setText("78.654321");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, null));
 final JTable table = frame.pos;
 final FoxTableModel tableModel = (FoxTableModel) table.getModel();
 final int column = 0;
 final int row = 0;
 table.setValueAt("45.123456", row, column);
 tableModel.fireTableDataChanged(row, column);
 final String expectedValue = "45.123456";
 final String actualValue = (String) table.getValueAt(row, column);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testLoadLandmarks() {
 final int expectedNumLandmarks = 5;
 frame.loadLandmarks();
 final int actualNumLandmarks = frame.display.getLandMarks().size();
 assertEquals(expectedNumLandmarks, actualNumLandmarks);
 }
}