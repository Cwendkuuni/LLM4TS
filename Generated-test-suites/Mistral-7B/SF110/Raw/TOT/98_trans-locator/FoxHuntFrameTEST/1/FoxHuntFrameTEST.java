import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.TrigPoint;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @After
 public void tearDown() {
 frame.dispose();
 System.out.println("Test Completed!");
 }

 @Test
 public void testAddPointWithValidValues() {
 frame.theta.setText("180");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.1278");
 frame.add.doClick();

 TrigPoint expectedPoint = new TrigPoint(51.5074, -0.1278, 180);
 assertEquals(expectedPoint, frame.points.elementAt(frame.points.size() - 1));
 }

 @Test
 public void testAddPointWithInvalidAngle() {
 frame.theta.setText("361");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.1278");
 frame.add.doClick();

 assertTrue(frame.points.isEmpty());
 }

 @Test
 public void testAddPointWithInvalidLatitude() {
 frame.theta.setText("180");
 frame.lat.setText("DDDDDDDDDDDDDDDDD");
 frame.lon.setText("-0.1278");
 frame.add.doClick();

 assertTrue(frame.points.isEmpty());
 }

 @Test
 public void testAddPointWithInvalidLongitude() {
 frame.theta.setText("180");
 frame.lat.setText("51.5074");
 frame.lon.setText("DDDDDDDDDDDDDDDDD");
 frame.add.doClick();

 assertTrue(frame.points.isEmpty());
 }

 @Test
 public void testAddPointWithEmptyFields() {
 frame.theta.setText("");
 frame.lat.setText("");
 frame.lon.setText("");
 frame.add.doClick();

 assertTrue(frame.points.isEmpty());
 }

 @Test
 public void testAddPointWithNullValues() {
 frame.theta.setText(null);
 frame.lat.setText(null);
 frame.lon.setText(null);
 frame.add.doClick();

 assertTrue(frame.points.isEmpty());
 }

 @Test
 public void testAddPointWithNoPreviousPoints() {
 frame.theta.setText("180");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.1278");
 frame.add.doClick();

 assertEquals(1, frame.points.size());
 }

 @Test
 public void testAddPointWithPreviousPoints() {
 TrigPoint point1 = new TrigPoint(51.5074, -0.1278, 180);
 TrigPoint point2 = new TrigPoint(51.5356, -0.1262, 185);
 frame.points.add(point1);
 frame.fox.add(FoxCalc.calcFox(point1, point2));
 frame.theta.setText("180");
 frame.lat.setText("51.5555");
 frame.lon.setText("-0.1278");
 frame.add.doClick();

 TrigPoint expectedFox = FoxCalc.calcFox(point1, new TrigPoint(51.5555, -0.1278, 180));
 assertEquals(expectedFox, frame.fox.elementAt(frame.fox.size() - 1));
 }
}