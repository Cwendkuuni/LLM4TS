package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.TrigPoint;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testAddPointWithValidInput() {
 // Given: valid input in text fields
 frame.theta.setText("45");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");

 // When: add button is clicked
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));

 // Then: new point is added to points and fox vectors, and displayed
 assertEquals(2, frame.points.size());
 assertEquals(2, frame.fox.size());
 assertTrue(frame.posModel.getRowCount() == 2);
 }

 @Test
 public void testAddPointWithInvalidInput() {
 // Given: invalid input in text fields
 frame.theta.setText("abcd");
 frame.lat.setText("xxx.yyy.zzz");
 frame.lon.setText("123.456.789");

 // When: add button is clicked
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));

 // Then: no new point is added to points or fox vectors, and no point is displayed
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 assertTrue(frame.posModel.getRowCount() == 1);
 }

 @Test
 public void testAddPointWithEmptyTextFields() {
 // Given: empty text fields
 frame.theta.setText("");
 frame.lat.setText("");
 frame.lon.setText("");

 // When: add button is clicked
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));

 // Then: no new point is added to points or fox vectors, and no point is displayed
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 assertTrue(frame.posModel.getRowCount() == 1);
 }

 @Test
 public void testAddPointWithNullTextFields() {
 // Given: null text fields
 frame.theta = null;
 frame.lat = null;
 frame.lon = null;

 // When: add button is clicked
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));

 // Then: an error message is displayed and no new point is added to points or fox vectors
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 assertFalse(frame.isVisible());
 }

 @Test
 public void testCalcFoxWithTwoPoints() {
 // Given: two points in points vector
 frame.addPoint();
 frame.addPoint();

 // When: calculate fox with last two points
 TrigPoint foxPoint = FoxHuntFrame.FoxCalc.calcFox(
 frame.points.elementAt(frame.points.size() - 1),
 frame.points.elementAt(frame.points.size() - 2)
 );

 // Then: new fox point is added to fox vector and displayed
 assertEquals(2, frame.fox.size());
 assertEquals(2, frame.posModel.getRowCount());
 assertTrue(frame.pos.getValueAt(1, 0).equals("" + foxPoint.getY()));
 assertTrue(frame.pos.getValueAt(1, 1).equals("" + foxPoint.getX()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCalcFoxWithLessThanTwoPoints() {
 // Given: less than two points in points vector
 frame.addPoint(); // one point

 // When: calculate fox with less than two points
 TrigPoint foxPoint = FoxHuntFrame.FoxCalc.calcFox(
 frame.points.elementAt(frame.points.size() - 1),
 null // null second point
 );

 // Then: an IllegalArgumentException is thrown
 }
}