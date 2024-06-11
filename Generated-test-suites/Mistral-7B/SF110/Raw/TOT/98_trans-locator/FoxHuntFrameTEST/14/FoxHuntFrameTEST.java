import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.*;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Test
 public void testFoxHuntFrameConstruction() {
 frame = new FoxHuntFrame();
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.posModel);
 assertNotNull(frame.pos);
 assertNotNull(frame.display);
 }

 @Test
 public void testAddPointValidValues() {
 frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "1"));
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 }

 @Test
 public void testAddPointInvalidValues() {
 frame = new FoxHuntFrame();
 frame.theta.setText("abc");
 frame.lat.setText("XYZ");
 frame.lon.setText("PQR");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "1"));
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());
 }

 @Test
 public void testAddPointWhenNoPreviousPoint() {
 frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "1"));
 frame.theta.setText("30");
 frame.lat.setText("37.7749");
 frame.lon.setText("-122.4194");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "1"));
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 }

 @Test
 public void testAddPointWhenPreviousPointExists() {
 frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "1"));
 frame.theta.setText("30");
 frame.lat.setText("37.7749");
 frame.lon.setText("-122.4194");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "1"));
 frame.theta.setText("60");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.1278");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "1"));
 assertEquals(3, frame.points.size());
 assertEquals(3, frame.fox.size());
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLatitude() {
 frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("XYZ");
 frame.lon.setText("-74.0060");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "1"));
 fail("Should not be able to add point with invalid latitude");
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLongitude() {
 frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("40.7128");
 frame.lon.setText("PQR");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "1"));
 fail("Should not be able to add point with invalid longitude");
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidTheta() {
 frame = new FoxHuntFrame();
 frame.theta.setText("abc");
 frame.lat.setText("40.7128");
 frame.lon.setText("-74.0060");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "1"));
 fail("Should not be able to add point with invalid theta");
 }
}