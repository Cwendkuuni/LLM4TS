import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;
import static org.junit.Assert.*;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Before
 public void setUp() {
 display = new HuntDisplay();
 }

 @After
 public void tearDown() {
 display = null;
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 int width = display.getWidth();
 int height = display.getHeight();
 assertEquals(2, display.points.size());
 assertTrue(display.points.contains(new TrigPoint(1.0 / 90.0, 2.0 / 180.0, 0.0, false)));
 assertNotNull(display.getPreferredSize());
 assertNotNull(display.getMinimumSize());
 assertNotEquals(width, 0);
 assertNotEquals(height, 0);
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullPoint() {
 display.addPoint(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithInvalidTrigPoint() {
 TrigPoint point = new TrigPoint(Double.NaN, 2.0, 0.0, false);
 display.addPoint(point);
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(3.0, 4.0, new ImageIcon("landmark.png"));
 display.addLandmark(landmark);
 int width = display.getWidth();
 int height = display.getHeight();
 assertEquals(2, display.points.size());
 assertEquals(1, display.landmarks.size());
 assertNotNull(display.getPreferredSize());
 assertNotNull(display.getMinimumSize());
 assertNotEquals(width, 0);
 assertNotEquals(height, 0);
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullLandmark() {
 display.addLandmark(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithInvalidLandmark() {
 Landmark landmark = new Landmark(3.0, Double.NaN, new ImageIcon("landmark.png"));
 display.addLandmark(landmark);
 }

 @Test
 public void testPaintComponent() {
 Graphics2D g = (Graphics2D) display.createGraphic();
 display.paintComponent(g);
 g.dispose();
 }

 @Test(expected = NullPointerException.class)
 public void testPaintComponentWithNullGraphics() {
 display.paintComponent(null);
 }
}