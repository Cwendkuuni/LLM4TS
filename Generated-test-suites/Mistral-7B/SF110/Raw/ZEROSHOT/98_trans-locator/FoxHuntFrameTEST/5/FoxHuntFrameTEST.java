package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private FoxTableModel tableModel;
 private JTable table;

 @Test
 public void testFoxHuntFrameConstructor() {
 frame = new FoxHuntFrame();
 assertNotNull(frame.getContentPane());
 assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testAddPointMethod() {
 frame = new FoxHuntFrame();
 frame.theta.setText("30");
 frame.lat.setText("40.123456");
 frame.lon.setText("-71.123456");
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.add.doClick();
 }
 });

 TrigPoint expectedPoint = new TrigPoint(40.123456, -71.123456, 30);
 TrigPoint actualPoint = (TrigPoint) frame.points.lastElement();
 assertEquals(expectedPoint.getY(), actualPoint.getY(), 0.0001);
 assertEquals(expectedPoint.getX(), actualPoint.getX(), 0.0001);
 assertEquals(expectedPoint.getAngle(), actualPoint.getAngle(), 0.01);

 TrigPoint expectedFox = new TrigPoint(0, 0, 0);
 TrigPoint actualFox = (TrigPoint) frame.fox.lastElement();
 assertNull(actualFox);

 tableModel = (FoxTableModel) frame.posModel;
 JTable table = frame.pos;
 assertEquals(0, tableModel.getRowCount());

 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 TrigPoint previousPoint = (TrigPoint) frame.points.elementAt(frame.points.size() - 2);
 TrigPoint expectedFox = FoxCalc.calcFox(expectedPoint, previousPoint);
 frame.addPoint();
 }
 });

 TrigPoint actualFox = (TrigPoint) frame.fox.lastElement();
 assertEquals(expectedFox.getX(), actualFox.getX(), 0.01);
 assertEquals(expectedFox.getY(), actualFox.getY(), 0.01);

 assertEquals(1, tableModel.getRowCount());
}

 @Test
 public void testLoadLandmarksMethod() {
 frame = new FoxHuntFrame();
 frame.loadLandmarks();
 HuntDisplay display = frame.display;
 assertEquals(10, display.getLandMarks().size());
}

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksFileNotFoundException() {
 frame = new FoxHuntFrame();
 frame.loadLandmarks(); // First successful call

 try {
 frame.loadLandmarks(); // Second call to raise the exception
 } catch (IOException e) {
 fail("Unexpected IOException: " + e.getMessage());
 }

 fail("Expected FileNotFoundException not thrown");
}

 @Test(expected = IOException.class)
 public void testLoadLandmarksIOException() {
 frame = new FoxHuntFrame();

 try {
 frame.loadLandmarks(); // First successful call
 } catch (FileNotFoundException e) {
 fail("Unexpected FileNotFoundException: " + e.getMessage());
 }

 try {
 frame.loadLandmarks(); // Second call to raise the exception
 } catch (FileNotFoundException e) {
 fail("Unexpected FileNotFoundException: " + e.getMessage());
 }

 try {
 frame.loadLandmarks(); // Third call to raise the exception
 } catch (IOException e) {
 assertEquals("Error communicating with the file", e.getMessage());
}
}

 @Test
 public void testErrorDialog() {
 frame = new FoxHuntFrame();
 frame.error("Test error");
 assertEquals(JOptionPane.ERROR_MESSAGE, frame.getJOptionPaneMessageType());
}
}