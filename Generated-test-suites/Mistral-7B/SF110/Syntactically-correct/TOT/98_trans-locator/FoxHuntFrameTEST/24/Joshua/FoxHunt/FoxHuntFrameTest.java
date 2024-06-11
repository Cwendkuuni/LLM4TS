package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private FoxTableModel tableModel;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 frame = new FoxHuntFrame();
 tableModel = new FoxTableModel();
 frame.posModel = tableModel;
 }

 @Test
 public void testFrameInitialization() {
 // Verify that the frame is initialized correctly
 assertEquals("Triangulation", frame.getTitle());
 // Add expected mock objects to the frame
 verify(frame.display, never()).addLandMark(any(Landmark.class));
 verify(frame.display, never()).addPoint(any(TrigPoint.class));
 verifyZeroInteractions(frame.pos);
 // Verify that the frame is visible
 assertTrue(frame.isVisible());
 }

 @Test
 public void testAddPoint_ValidInput() {
 // Given a valid input
 final String theta = "45";
 final String lat = "40.712776";
 final String lon = "-74.006001";
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);
 frame.add_actionPerformed(mock(ActionEvent.class));

 // When the add button is clicked
 // Then a new TrigPoint is added to the points vector
 verify(frame.points, times(1)).add(any(TrigPoint.class));
 // And a new TrigPoint is added to the fox vector
 verify(frame.fox, times(1)).add(any(TrigPoint.class));
 // And the table is updated
 verify(tableModel, times(1)).fireTableDataChanged();
 }

 @Test
 public void testAddPoint_InvalidInput() {
 // Given invalid input
 frame.theta.setText("");
 frame.lat.setText("");
 frame.lon.setText("");
 frame.add_actionPerformed(mock(ActionEvent.class));

 // Then no new TrigPoint is added to the points vector
 verify(frame.points, never()).add(any(TrigPoint.class));
 // And no new TrigPoint is added to the fox vector
 verify(frame.fox, never()).add(any(TrigPoint.class));
 // And an error message is displayed
 assertEquals("Abort", JOptionPane.showOptionDialog(frame, "Can't Add That!", "Error", 0, JOptionPane.PLAIN_MESSAGE));
 }

 @Test
 public void testLoadLandmarks_ValidFile() {
 // Given a valid file
 final File landmarksFile = new File("./landmarks.cfg");
 when(frame.loadLandmarks(landmarksFile)).thenReturn(true);

 // When the loadLandmarks method is called
 frame.loadLandmarks(landmarksFile);

 // Then the display is populated with landmarks
 verify(frame.display, times(1)).addLandMark(any(Landmark.class));
 }

 @Test
 public void testLoadLandmarks_InvalidFile() {
 // Given an invalid file
 final File landmarksFile = new File("./nonExistentFile.cfg");
 when(frame.loadLandmarks(landmarksFile)).thenReturn(false);

 // When the loadLandmarks method is called
 frame.loadLandmarks(landmarksFile);

 // Then no landmarks are added to the display
 verify(frame.display, never()).addLandMark(any(Landmark.class));
 // And an error message is displayed
 assertEquals("Abort", JOptionPane.showOptionDialog(frame, "Can't Find the file", "Error", 0, JOptionPane.PLAIN_MESSAGE));
 }

 @Test
 public void testTableModel_GetValueAt() {
 // Given a valid TrigPoint
 final TrigPoint point = new TrigPoint(40.712776, -74.006001, 45);
 frame.points.add(point);
 frame.posModel.fireTableDataChanged();

 // When the value at the table is retrieved
 final Object value = frame.pos.getValueAt(0, 0);

 // Then the value is the latitude of the TrigPoint
 assertEquals("40.712776", value.toString());
 }
}