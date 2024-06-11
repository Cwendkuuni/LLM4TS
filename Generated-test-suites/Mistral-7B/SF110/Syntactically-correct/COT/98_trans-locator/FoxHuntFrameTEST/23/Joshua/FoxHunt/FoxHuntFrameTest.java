package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

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
 public void testFoxHuntFrameConstruction() {
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.getTextFields());
 assertNotNull(frame.getPosTable());
 assertNotNull(frame.getDisplay());
 }

 @Test
 public void testAddPointMethod() {
 final TrigPoint testPoint = new TrigPoint(0.0, 0.0, 0);
 frame.addPoint();
 frame.getPoints().add(testPoint);
 final int expectedSize = 2;
 assertEquals(expectedSize, frame.getPoints().size());
 final TrigPoint foxPoint = FoxCalc.calcFox(testPoint, frame.getPoints().lastElement());
 frame.getFox().add(foxPoint);
 final int tableSize = frame.getPosModel().getRowCount();
 assertEquals(tableSize, 2);
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidInput() {
 frame.theta.setText("abc");
 frame.lat.setText("123");
 frame.lon.setText("123.123");
 frame.addActionListener(frame);
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLatInput() {
 frame.theta.setText("45");
 frame.lat.setText("123.123");
 frame.lon.setText("123.123");
 frame.addActionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLonInput() {
 frame.theta.setText("45");
 frame.lat.setText("123.123");
 frame.lon.setText("123");
 frame.addActionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddPointWithOnlyOnePoint() {
 frame.theta.setText("45");
 frame.lat.setText("123.123");
 frame.lon.setText("123.123");
 frame.addActionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 frame.addPoint();
 final TrigPoint expectedFoxPoint = new TrigPoint(Double.NaN, Double.NaN, 0);
 assertEquals(expectedFoxPoint, frame.getFox().lastElement());
 }

 @Test
 public void testLoadLandmarksMethod() {
 final int expectedNumLandmarks = 5;
 frame.loadLandmarks();
 final int actualNumLandmarks = frame.getDisplay().getLandmarks().size();
 assertEquals(expectedNumLandmarks, actualNumLandmarks);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksWithFileNotFound() {
 frame.loadLandmarks();
}

 @Test(expected = IOException.class)
 public void testLoadLandmarksWithIOException() {
 frame.loadLandmarks();
}

 @Test
 public void testErrorMethod() {
 final String errorMessage = "Test Error Message";
 frame.error(errorMessage);
}
}