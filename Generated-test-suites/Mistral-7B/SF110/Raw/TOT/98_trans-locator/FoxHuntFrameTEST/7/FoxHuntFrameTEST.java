import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;
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
 }

 @Test
 public void testAddPoint() {
 // Given: Initial frame state
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());

 // When: Add a valid point
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.1278");
 frame.add.doClick();

 // Then: One point is added to the points vector and one to the fox vector
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());

 // When: Add another valid point
 frame.theta.setText("135");
 frame.lat.setText("51.5439");
 frame.lon.setText("-0.1536");
 frame.add.doClick();

 // Then: Two points are added to the points vector and two to the fox vector
 assertEquals(2, frame.points.size());
 assertEquals(2, frame.fox.size());
 }

 @Test
 public void testInvalidPointAddition() {
 // Given: Initial frame state
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());

 // When: Add an invalid point with empty latitude textfield
 frame.theta.setText("45");
 frame.lat.setText("");
 frame.lon.setText("-0.1278");
 frame.add.doClick();

 // Then: No points are added to the points or fox vectors
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());

 // When: Add an invalid point with empty longitude textfield
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("");
 frame.add.doClick();

 // Then: No points are added to the points or fox vectors
 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());
 }

 @Test
 public void testCalculateFox() {
 // Given: Two valid points added
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.1278");
 frame.add.doClick();
 frame.theta.setText("135");
 frame.lat.setText("51.5439");
 frame.lon.setText("-0.1536");
 frame.add.doClick();

 // When: Calculate fox position
 frame.add.doClick();

 // Then: The fox vector should contain the calculated fox position
 TrigPoint expectedFox = new TrigPoint(51.5231, -0.1367);
 assertEquals(expectedFox.getX(), frame.fox.lastElement().getX(), 0.001);
 assertEquals(expectedFox.getY(), frame.fox.lastElement().getY(), 0.001);
 }

 @Test
 public void testLoadLandmarks() {
 // Given: Frame initialized
 assertEquals(0, frame.display.getLandmarks().size());

 // When: Landmarks are loaded from the file
 frame.loadLandmarks();

 // Then: The display should contain the loaded landmarks
 assertEquals(3, frame.display.getLandmarks().size());
 }

 @Test
 public void testErrorHandling() {
 // Given: Frame initialized
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.1278");
 frame.zoom.setText("0");
 frame.add.doClick();

 // When: An error occurs during landmarks loading
 frame.loadLandmarks();
 frame.error("Error loading landmarks");

 // Then: The application should display an error message and exit
 assertEquals("Error", frame.getText());
 System.exit(0);
 }
}