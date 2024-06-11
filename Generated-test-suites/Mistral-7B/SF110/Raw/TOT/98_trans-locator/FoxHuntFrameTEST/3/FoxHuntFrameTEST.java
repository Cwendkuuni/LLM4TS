package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private JTable table;
 private FoxTableModel model;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 model = new FoxTableModel();
 frame.posModel = model;
 table = new JTable(model);
 }

 @Test
 public void testAddPoint() {
 // Given: Frame initialized and text fields with valid values
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("0.1278");
 frame.addActionPerformed(mock(ActionEvent.class));

 // When: Add button is clicked
 frame.addPoint();

 // Then: Point is added to the Vector and displayed in the table
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 verify(frame.display, times(1)).addPoint(any(TrigPoint.class));
 verify(model, times(1)).fireTableDataChanged();

 // Test adding another point
 frame.theta.setText("30");
 frame.lat.setText("51.5137");
 frame.lon.setText("0.1282");
 frame.addActionPerformed(mock(ActionEvent.class));
 frame.addPoint();

 // Then: Another point is added and fox position is calculated
 assertEquals(2, frame.points.size());
 assertEquals(1, frame.fox.size());
 verify(frame.display, times(2)).addPoint(any(TrigPoint.class));
 verify(model, times(1)).fireTableDataChanged();
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidValues() {
 // Given: Frame initialized and text fields with invalid values
 frame.theta.setText("abc");
 frame.lat.setText("ddd.ddd");
 frame.lon.setText("eee.eee");
 frame.addActionPerformed(mock(ActionEvent.class));

 // When: Add button is clicked
 frame.addPoint();

 // Then: An error message is displayed and no points are added
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());
 }
}