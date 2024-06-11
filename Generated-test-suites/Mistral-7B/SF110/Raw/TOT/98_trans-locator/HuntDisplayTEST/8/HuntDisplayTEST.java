import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Test
 public void testConstructor() {
 display = new HuntDisplay();
 assertNotNull(display);
 assertEquals(new Dimension(200, 200), display.getPrefSize());
 assertEquals(new Dimension(50, 50), display.getMinSize());
 }

 @Test
 public void testAddPoint() {
 display = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test
 public void testAddPointWithNullTrigPoint() {
 display = new HuntDisplay();
 TrigPoint point = null;
 display.addPoint(point);
 assertFalse(display.points.isEmpty());
 }

 @Test
 public void testAddLandmark() {
 display = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
 display.addLandmark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test
 public void testAddLandmarkWithNullLandmark() {
 display = new HuntDisplay();
 Landmark landmark = null;
 display.addLandmark(landmark);
 assertFalse(display.landmarks.isEmpty());
 }

 @Test
 public void testPaintComponentWithEmptyLists() {
 Graphics g = new GraphicsStub(); // Replace with actual Graphics or Mockito mock
 display = new HuntDisplay();
 display.paintComponent(g);
 assertTrue(g.isDrawingCalled());
 assertTrue(display.points.isEmpty());
 assertTrue(display.landmarks.isEmpty());
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 display = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 Landmark landmark1 = new Landmark(1.5, 2.5, new ImageIcon("icon1.png"));
 Landmark landmark2 = new Landmark(3.5, 4.5, new ImageIcon("icon2.png"));
 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandmark(landmark1);
 display.addLandmark(landmark2);
 Graphics g = new GraphicsStub(); // Replace with actual Graphics or Mockito mock
 display.paintComponent(g);
 assertTrue(g.isDrawingCalled());
 assertEquals(4, display.points.size()); // 2 points + 2 foxes (assuming foxes are represented as points)
 assertEquals(2, display.landmarks.size());
 }

 // Add more test cases as needed for edge cases and error scenarios.

}