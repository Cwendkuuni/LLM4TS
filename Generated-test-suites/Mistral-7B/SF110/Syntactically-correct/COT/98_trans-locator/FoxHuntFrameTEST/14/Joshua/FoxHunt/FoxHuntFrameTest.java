package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import Joshua.FoxHunt.*;
import org.junit.*;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 // Test the constructor
 assertNotNull(frame);
 assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testActionPerformed() {
 // Add a test point and verify that the frame's state is updated accordingly
 frame.theta.setText("45");
 frame.lat.setText("40.712776");
 frame.lon.setText("-74.005974");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, null));
 assertEquals(2, frame.points.size());
 assertEquals(1, frame.fox.size());

 // Test adding another point and verify that the fox position is updated
 frame.theta.setText("135");
 frame.lat.setText("40.748442");
 frame.lon.setText("-73.993273");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, null));
 assertEquals(3, frame.points.size());
 assertEquals(2, frame.fox.size());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidThetaInput() {
 // Test invalid theta input and verify that an exception is thrown
 frame.theta.setText("");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, null));
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidLatInput() {
 // Test invalid latitude input and verify that an exception is thrown
 frame.theta.setText("90");
 frame.lat.setText("dddddddd");
 frame.lon.setText("-74.005974");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, null));
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidLonInput() {
 // Test invalid longitude input and verify that an exception is thrown
 frame.theta.setText("90");
 frame.lat.setText("40.712776");
 frame.lon.setText("ddddddddd");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, null));
 }

 @Test
 public void testLoadLandmarks() {
 // Test the loadLandmarks method with a valid configuration file
 frame.loadLandmarks();
 // Add some assertions here based on the expected behavior of the method
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksFileNotFoundException() {
 // Test the loadLandmarks method with a missing configuration file
 frame.loadLandmarks();
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksIOException() {
 // Test the loadLandmarks method with an I/O error
 frame = new FoxHuntFrame();
 // Add some mocking or stubbing to simulate an I/O error here
 frame.loadLandmarks();
 }

 @After
 public void tearDown() {
 frame.dispose();
 }
}