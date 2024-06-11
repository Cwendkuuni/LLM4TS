package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.After;
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

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 assertEquals("Triangulation", frame.getTitle());
 assertTrue(frame.isVisible());
 }

 @Test
 public void testAddPointMethod() {
 // Test valid input
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.theta.setText("10");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.0898");
 frame.add.doClick();
 }
 });

 // Test exception when invalid input is given
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.theta.setText("abc");
 frame.lat.setText("51.5074");
 frame.lon.setText("ddd");
 frame.add.doClick();
 }
 });

 // Test adding second point and checking if fox is calculated correctly
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.theta.setText("20");
 frame.lat.setText("52.0083");
 frame.lon.setText("-1.8536");
 frame.add.doClick();

 // Checking if new point is added to the vector points
 assertEquals(2, frame.points.size());

 // Checking if new fox position is calculated correctly
 final TrigPoint lastPoint = (TrigPoint) frame.points.lastElement();
 final TrigPoint expectedFox = new TrigPoint(51.76049798324076, -0.835733184871875, 154.8887152185391);
 final TrigPoint fox = FoxCalc.calcFox(lastPoint, (TrigPoint) frame.points.elementAt(0));
 assertTrue(fox.equals(expectedFox));
 }
 });
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidInput() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.theta.setText("abc");
 frame.lat.setText("51.5074");
 frame.lon.setText("ddd");
 frame.add.doClick();
 }
 });
 }

 @Test
 public void testLoadLandmarksMethod() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.loadLandmarks();
 }
 });

 // Checking if landmarks are added to the HuntDisplay
 final int numLandmarks = frame.display.getLandMarks().size();
 assertEquals(3, numLandmarks);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksMethodWithFileNotFoundException() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.loadLandmarks();
 }
 });
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksMethodWithIOException() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.loadLandmarks();
 }
 });
 }

 @Test
 public void testFoxTableModelGetValueAt() {
 final FoxTableModel model = frame.posModel;
 final Object[] expectedData = new Object[] { "51.5074", "-0.0898" };
 final Object[] actualData = (Object[]) model.getValueAt(0, 0);
 assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testFoxTableModelGetColumnClass() {
 final FoxTableModel model = frame.posModel;
 final Class expectedClass = Double.class;
 assertEquals(expectedClass, model.getColumnClass(0));
 }
}