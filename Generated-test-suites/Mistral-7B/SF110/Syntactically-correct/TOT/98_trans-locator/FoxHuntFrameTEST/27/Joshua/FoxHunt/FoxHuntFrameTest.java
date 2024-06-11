package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private FoxTableModel model;
 private JTable table;

 @Before
 public void setup() {
 frame = new FoxHuntFrame();
 model = mock(FoxTableModel.class);
 table = Mockito.spy(new JTable(model));
 frame.pos = table;
 }

 @Test
 public void testAddPointValidInput() {
 // Given: User inputs valid data
 frame.theta.setText("45");
 frame.lat.setText("51.5072");
 frame.lon.setText("0.0828");

 // When: User clicks the "Add It!!!" button
 ActionEvent actionEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(actionEvent);

 // Then: A new point is added to the points and fox vectors, and the JTable is updated
 verify(model, times(1)).fireTableDataChanged();
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointInvalidLatInput() {
 // Given: User inputs invalid latitude data
 frame.theta.setText("45");
 frame.lat.setText("abc");
 frame.lon.setText("0.0828");

 // When: User clicks the "Add It!!!" button
 ActionEvent actionEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(actionEvent);

 // Then: An exception is thrown
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointInvalidLonInput() {
 // Given: User inputs invalid longitude data
 frame.theta.setText("45");
 frame.lat.setText("51.5072");
 frame.lon.setText("abc");

 // When: User clicks the "Add It!!!" button
 ActionEvent actionEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(actionEvent);

 // Then: An exception is thrown
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointInvalidThetaInput() {
 // Given: User inputs invalid theta data
 frame.theta.setText("abc");
 frame.lat.setText("51.5072");
 frame.lon.setText("0.0828");

 // When: User clicks the "Add It!!!" button
 ActionEvent actionEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(actionEvent);

 // Then: An exception is thrown
 }

 @Test
 public void testAddPointExistingPoint() {
 // Given: User inputs data for a point that already exists
 frame.theta.setText("45");
 frame.lat.setText("51.5072");
 frame.lon.setText("0.0828");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!"));

 // When: User clicks the "Add It!!!" button again
 ActionEvent actionEvent = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
 frame.actionPerformed(actionEvent);

 // Then: No new point is added to the vectors, and the JTable is not updated
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 }
}