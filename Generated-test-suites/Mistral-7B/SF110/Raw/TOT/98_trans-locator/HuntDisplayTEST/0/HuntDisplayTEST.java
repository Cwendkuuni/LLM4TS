import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;
import java.awt.Image;
import java.awt.Toolkit;

public class HuntDisplayTest {

 private HuntDisplay display;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 display = new HuntDisplay();
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullPoint() {
 display.addPoint(null);
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(Toolkit.getDefaultToolkit().getImage("path/to/image.jpg")));
 display.addLandMark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandMarkWithNullLandmark() {
 display.addLandMark(null);
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 assertTrue(g.getColor().equals(Color.WHITE));
 }

 @Test
 public void testPaintComponentWithPointsButNoLandmarks() {
 Graphics g = createMockGraphics();
 display.addPoint(new TrigPoint(1.0, 2.0, 0, false));
 display.addPoint(new TrigPoint(3.0, 4.0, 0, false));
 display.paintComponent(g);
 for (int i = 0; i < 2; i++) {
 TrigPoint point = (TrigPoint) display.points.elementAt(i);
 int x = (int) ((point.getX() + 0.5) * display.getWidth());
 int y = (int) ((point.getY() + 0.5) * display.getHeight());
 assertEquals(point.isFox(), g.getColor().equals(point.isFox() ? Color.GREEN : Color.BLACK));
 g.setColor(Color.BLACK);
 g.fillOval(x, y, 5, 5);
 }
 }

 @Test
 public void testPaintComponentWithLandmarksAndPoints() {
 Graphics g = createMockGraphics();
 display.addPoint(new TrigPoint(1.0, 2.0, 0, false));
 display.addPoint(new TrigPoint(3.0, 4.0, 0, false));
 display.addLandMark(new Landmark(1.5, 1.5, new ImageIcon(Toolkit.getDefaultToolkit().getImage("path/to/image.jpg"))));
 display.addLandMark(new Landmark(3.5, 3.5, new ImageIcon(Toolkit.getDefaultToolkit().getImage("path/to/image2.jpg"))));
 display.paintComponent(g);
 for (int i = 0; i < 2; i++) {
 TrigPoint point = (TrigPoint) display.points.elementAt(i);
 int x = (int) ((point.getX() + 0.5) * display.getWidth());
 int y = (int) ((point.getY() + 0.5) * display.getHeight());
 assertEquals(point.isFox(), g.getColor().equals(point.isFox() ? Color.GREEN : Color.BLACK));
 g.setColor(Color.BLACK);
 g.fillOval(x, y, 5, 5);
 }
 Landmark landmark = (Landmark) display.landmarks.elementAt(0);
 int x1 = (int) ((landmark.getLon() + 0.5) * display.getWidth());
 int y1 = (int) ((landmark.getLat() + 0.5) * display.getHeight());
 Image image = landmark.getIcon().getImage();
 g.drawImage(image, x1, y1, 10, 10, display.getBackground(), display);
 }

 @Test
 public void testGetMinimumSize() {
 Dimension expected = new Dimension(50, 50);
 assertEquals(expected, display.getMinimumSize());
 }

 @Test
 public void testGetPreferredSize() {
 Dimension expected = new Dimension(200, 200);
 assertEquals(expected, display.getPreferredSize());
 }

}