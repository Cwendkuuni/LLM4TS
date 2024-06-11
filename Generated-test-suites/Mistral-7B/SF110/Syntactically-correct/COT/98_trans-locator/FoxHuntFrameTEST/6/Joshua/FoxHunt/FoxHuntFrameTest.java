package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JTextField;

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
 // Testing if the constructor works correctly
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.getTextFields());
 assertNotNull(frame.getTable());
 assertNotNull(frame.getDisplay());
 }

 @Test
 public void testAddPointMethod() {
 // Testing if addPoint method add points to the Vector correctly
 final JTextField theta = new JTextField("45");
 final JTextField lat = new JTextField("51.506944");
 final JTextField lon = new JTextField("0.123456");
 frame.getTextFields()[0] = theta;
 frame.getTextFields()[1] = lat;
 frame.getTextFields()[2] = lon;
 frame.actionPerformed(new ActionEvent(frame.getAddButton(), 0, "ActionPerformed"));
 final Vector expectedPoints = new Vector<>();
 expectedPoints.add(new TrigPoint(51.506944, 0.123456, 45));
 assertEquals(expectedPoints, frame.getPoints());
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidInput() {
 // Testing if addPoint method throws NumberFormatException with invalid input
 final JTextField theta = new JTextField("abc");
 final JTextField lat = new JTextField("51.506944");
 final JTextField lon = new JTextField("0.123456");
 frame.getTextFields()[0] = theta;
 frame.getTextFields()[1] = lat;
 frame.getTextFields()[2] = lon;
 frame.actionPerformed(new ActionEvent(frame.getAddButton(), 0, "ActionPerformed"));
 }

 @Test
 public void testLoadLandmarksMethod() {
 // Testing if loadLandmarks method correctly loads landmarks from the file
 final Vector expectedLandmarks = new Vector<>();
 final Landmark landmark1 = new Landmark(51.521234, 0.042345, new ImageIcon("icon1.gif"));
 expectedLandmarks.add(landmark1);
 frame.loadLandmarks();
 assertEquals(expectedLandmarks, frame.getDisplay().getLandmarks());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksMethodWithFileNotFound() {
 // Testing if loadLandmarks method throws FileNotFoundException when the file is not found
 final FoxHuntFrame spyFrame = spy(frame);
 doThrow(new FileNotFoundException("File not found"))
 .when(spyFrame)
 .error("Can't Find the file");
 frame.loadLandmarks();
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksMethodWithIOException() {
 // Testing if loadLandmarks method throws IOException when there is an error communicating with the file
 final FoxHuntFrame spyFrame = spy(frame);
 doThrow(new IOException("Error communicating with the file"))
 .when(spyFrame)
 .error("Error communicating with the file");
 frame.loadLandmarks();
 }

 @Test
 public void testFoxTableModel() {
 // Testing if FoxTableModel correctly displays the data in the table
 final FoxTableModel model = frame.getPosModel();
 final Object[][] data = new Object[1][model.getColumnCount()];
 data[0][0] = "51.506944";
 data[0][1] = "0.123456";
 final JTable table = new JTable(model);
 table.setModel(new DefaultTableModel(data, model.getColumnNames()));
 assertEquals(model.getValueAt(0, 0), table.getValueAt(0, 0));
 assertEquals(model.getValueAt(0, 1), table.getValueAt(0, 1));
 }

 @Test
 public void testIsCellEditableMethod() {
 // Testing if isCellEditable method returns false
 assertFalse(frame.getPosModel().isCellEditable(0, 0));
 }

}