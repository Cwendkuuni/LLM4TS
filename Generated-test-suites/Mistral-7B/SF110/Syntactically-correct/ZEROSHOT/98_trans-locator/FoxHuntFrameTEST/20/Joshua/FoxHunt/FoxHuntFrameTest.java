package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

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
 public void testFoxHuntFrameConstructor() {
 // Check frame title
 assertEquals("Triangulation", frame.getTitle());
 // Check default close operation
 assertEquals(JFrame.DISPOSE_ON_CLOSE, frame.getDefaultCloseOperation());
 // Check if frame is visible
 assertEquals(true, frame.isVisible());
 }

 @Test
 public void testAddPointMethod() throws InterruptedException {
 // Set text fields values
 frame.theta.setText("10");
 frame.lat.setText("51.50722222");
 frame.lon.setText("-0.12755555");
 SwingUtilities.invokeAndWait(() -> frame.add.doClick());
 // Check points size
 assertEquals(2, frame.points.size());
 // Check fox size
 assertEquals(1, frame.fox.size());
 // Check table size
 assertEquals(1, ((JTable) frame.pos).getRowCount());
 // Check display points
 assertNotNull(frame.display);
 assertEquals(2, frame.display.getLandmarks().size());
 // Check fox position
 TrigPoint point = (TrigPoint) frame.fox.lastElement();
 assertEquals(51.50722222, point.getY(), 0.0001);
 assertEquals(-0.12755555, point.getX(), 0.0001);
 }

 @Test
 public void testAddPointMethodWithTwoPoints() throws InterruptedException {
 // Set text fields values for the first point
 frame.theta.setText("10");
 frame.lat.setText("51.50722222");
 frame.lon.setText("-0.12755555");
 SwingUtilities.invokeAndWait(() -> frame.add.doClick());
 // Set text fields values for the second point
 frame.theta.setText("45");
 frame.lat.setText("51.45677777");
 frame.lon.setText("-0.11233333");
 SwingUtilities.invokeAndWait(() -> frame.add.doClick());
 // Check points size
 assertEquals(3, frame.points.size());
 // Check fox size
 assertEquals(2, frame.fox.size());
 // Check table size
 assertEquals(2, ((JTable) frame.pos).getRowCount());
 // Check fox positions
 TrigPoint firstFox = (TrigPoint) frame.fox.lastElement();
 TrigPoint secondFox = (TrigPoint) frame.fox.elementAt(frame.fox.size() - 2);
 assertEquals(51.45677777, secondFox.getY(), 0.0001);
 assertEquals(-0.11233333, secondFox.getX(), 0.0001);
 assertEquals(51.50722222, firstFox.getY(), 0.0001);
 assertEquals(-0.12755555, firstFox.getX(), 0.0001);
 }

 @Test
 public void testAddPointMethodWithThreePoints() throws InterruptedException {
 // Set text fields values for the first point
 frame.theta.setText("10");
 frame.lat.setText("51.50722222");
 frame.lon.setText("-0.12755555");
 SwingUtilities.invokeAndWait(() -> frame.add.doClick());
 // Set text fields values for the second point
 frame.theta.setText("45");
 frame.lat.setText("51.45677777");
 frame.lon.setText("-0.11233333");
 SwingUtilities.invokeAndWait(() -> frame.add.doClick());
 // Set text fields values for the third point
 frame.theta.setText("90");
 frame.lat.setText("51.44444444");
 frame.lon.setText("-0.10666666");
 SwingUtilities.invokeAndWait(() -> frame.add.doClick());
 // Check points size
 assertEquals(4, frame.points.size());
 // Check fox size
 assertEquals(3, frame.fox.size());
 // Check table size
 assertEquals(3, ((JTable) frame.pos).getRowCount());
 // Check fox positions
 TrigPoint firstFox = (TrigPoint) frame.fox.lastElement();
 TrigPoint secondFox = (TrigPoint) frame.fox.elementAt(frame.fox.size() - 2);
 TrigPoint thirdFox = (TrigPoint) frame.fox.elementAt(frame.fox.size() - 3);
 assertEquals(51.44444444, thirdFox.getY(), 0.0001);
 assertEquals(-0.10666666, thirdFox.getX(), 0.0001);
 assertEquals(51.50722222, firstFox.getY(), 0.0001);
 assertEquals(-0.12755555, firstFox.getX(), 0.0001);
 assertEquals(51.45677777, secondFox.getY(), 0.0001);
 assertEquals(-0.11233333, secondFox.getX(), 0.0001);
 }

 @Test
 public void testAddLandmarksMethod() {
 // Check initial landmarks size
 assertEquals(0, frame.display.getLandmarks().size());
 // Test loadLandmarks method
 frame.loadLandmarks();
 // Check landmarks size
 assertEquals(10, frame.display.getLandmarks().size());
 }

 @Test
 public void testTableModel() {
 FoxTableModel model = (FoxTableModel) frame.posModel;
 // Check column names
 assertEquals(2, model.getColumnCount());
 assertEquals("Lattitude", model.getColumnName(0));
 assertEquals("Longitude", model.getColumnName(1));
 // Check rows count
 assertEquals(2, model.getRowCount());
 // Check cells values
 assertEquals("51.50722222", model.getValueAt(0, 0).toString());
 assertEquals("-0.12755555", model.getValueAt(0, 1).toString());
 assertEquals("51.45677777", model.getValueAt(1, 0).toString());
 assertEquals("-0.11233333", model.getValueAt(1, 1).toString());
 }

 @Test
 public void testAddLandmarksWithThreePoints() {
 // Test loadLandmarks method
 frame.loadLandmarks();
 TrigPoint firstPoint = (TrigPoint) frame.points.lastElement();
 TrigPoint secondPoint = (TrigPoint) frame.points.elementAt(frame.points.size() - 2);
 TrigPoint thirdPoint = (TrigPoint) frame.points.elementAt(frame.points.size() - 3);
 Landmark firstLandmark = frame.display.getLandmarks().get(0);
 Landmark secondLandmark = frame.display.getLandmarks().get(1);
 Landmark thirdLandmark = frame.display.getLandmarks().get(2);
 // Check landmarks positions
 assertEquals(firstPoint.getX(), thirdLandmark.getX(), 0.0001);
 assertEquals(firstPoint.getY(), thirdLandmark.getY(), 0.0001);
 assertEquals(secondPoint.getX(), secondLandmark.getX(), 0.0001);
 assertEquals(secondPoint.getY(), secondLandmark.getY(), 0.0001);
 }

 @Test
 public void testErrorMethod() {
 String errorMessage = "Error message";
 frame.error(errorMessage);
 // Check if an error dialog is displayed
 assertEquals(1, JOptionPane.showOptionDialog(frame, errorMessage, "Error", 0, 0, null, new Object[] { "Abort", "Continue" }, 0) == 0);
 }
}