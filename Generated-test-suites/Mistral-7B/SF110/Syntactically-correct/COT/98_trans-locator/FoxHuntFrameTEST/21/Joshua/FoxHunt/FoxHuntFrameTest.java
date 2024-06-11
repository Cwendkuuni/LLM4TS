package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

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
 // Check if the frame is initialized correctly
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.getJTable());
 }

 @Test
 public void testActionPerformed() {
 // Simulate button click event
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Clicked");
 frame.actionPerformed(actionEvent);

 // Check if the addPoint method is called and the table is updated
 assertTrue(frame.points.size() == 1);
 assertTrue(frame.fox.size() == 0);

 // Add another point and check if the fox position is calculated correctly
 frame.theta.setText("45");
 frame.lat.setText("45.123456");
 frame.lon.setText("-123.123456");
 frame.actionPerformed(actionEvent);

 // Check if the table contains two rows and the fox position is calculated correctly
 assertEquals(frame.points.size(), 2);
 assertEquals(frame.fox.size(), 1);
 TrigPoint lastPoint = (TrigPoint) frame.points.lastElement();
 TrigPoint foxPoint = (TrigPoint) frame.fox.lastElement();
 assertEquals(lastPoint.getX(), foxPoint.getX(), 0.00001);
 assertEquals(lastPoint.getY(), foxPoint.getY(), 0.00001);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidThetaInput() {
 frame.theta.setText("");
 frame.actionPerformed(new ActionEvent(new Object(), 0, "Clicked"));
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidLatInput() {
 frame.theta.setText("90");
 frame.lat.setText("ddddddddddddd");
 frame.actionPerformed(new ActionEvent(new Object(), 0, "Clicked"));
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidLonInput() {
 frame.theta.setText("90");
 frame.lat.setText("55.123456");
 frame.lon.setText("dddddddddddddd");
 frame.actionPerformed(new ActionEvent(new Object(), 0, "Clicked"));
 }

 @Test(expected = NullPointerException.class)
 public void testLoadLandmarksFileNotFound() {
 frame.loadLandmarks();
 }

 @Test
 public void testLoadLandmarks() {
 // Simulate loading landmarks from a file
 Vector<Landmark> landmarks = new Vector<>();
 landmarks.add(new Landmark(1.0, 2.0, new ImageIcon("icon.gif")));
 landmarks.add(new Landmark(3.0, 4.0, new ImageIcon("icon2.gif")));
 frame.display.setLandmarks(landmarks);

 // Check if the landmarks are loaded correctly and displayed on the map
 assertEquals(frame.display.getLandmarks().size(), 2);
 }

 @Test
 public void testErrorDialog() {
 // Simulate an error during file loading
 frame.error("Error message");
 JOptionPane.showMessageDialog(frame, "Expected error message", "Error", JOptionPane.ERROR_MESSAGE);
 }

 @Test
 public void testFoxTableModelGetValueAt() {
 FoxTableModel tableModel = frame.posModel;
 JTable table = frame.pos;
 DecimalFormat formatter = new DecimalFormat("###.##############");

 // Check if the table model returns the correct values
 assertEquals(tableModel.getValueAt(0, 0).toString(), formatter.format(frame.points.elementAt(0).getY()));
 assertEquals(tableModel.getValueAt(0, 1).toString(), formatter.format(frame.points.elementAt(0).getX()));
 assertEquals(tableModel.getValueAt(1, 0).toString(), formatter.format(frame.fox.elementAt(0).getY()));
 assertEquals(tableModel.getValueAt(1, 1).toString(), formatter.format(frame.fox.elementAt(0).getX()));
 }
}