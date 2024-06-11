import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;
import java.awt.Image;
import java.awt.Toolkit;

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
 public void testGetPreferredSize() {
 Dimension expected = new Dimension(200, 200);
 Dimension actual = display.getPreferredSize();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMinimumSize() {
 Dimension expected = new Dimension(50, 50);
 Dimension actual = display.getMinimumSize();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(0.0, 0.0, 0.0, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullPoint() {
 display.addPoint(null);
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(0.0, 0.0, null);
 Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/landmarkIcon.png"));
 landmark.setIcon(new ImageIcon(icon));
 display.addLandMark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullLandmark() {
 display.addLandMark(null);
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 Graphics2D g = (Graphics2D)display.createGraphics();
 display.paintComponent(g);
 assertTrue(display.getWidth() * display.getHeight() > 0);
 g.dispose();
 }

 @Test
 public void testPaintComponentWithPoints() {
 TrigPoint p1 = new TrigPoint(0.0, 0.0, 0.0, false);
 TrigPoint p2 = new TrigPoint(1.0, 1.0, 0.0, false);
 display.addPoint(p1);
 display.addPoint(p2);
 Graphics2D g = (Graphics2D)display.createGraphics();
 display.paintComponent(g);
 int width = display.getWidth();
 int height = display.getHeight();
 for (int x = 0; x < width; x++) {
 for (int y = 0; y < height; y++) {
 if (g.getColor().equals(Color.GREEN) && x > width / 2 && x < width / 2 + 5 && y > height / 2 && y < height / 2 + 5) {
 // Fox point
 continue;
 }
 if (g.getColor().equals(Color.BLACK) && (Math.abs(x - (int)(p1.getX() * width)) < 10 && Math.abs(y - (int)(p1.getY() * height)) < 10) ||
 (Math.abs(x - (int)(p2.getX() * width)) < 10 && Math.abs(y - (int)(p2.getY() * height)) < 10)) {
 // Points
 continue;
 }
 fail("Unexpected pixel color: " + g.getColor());
 }
 }
 g.dispose();
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 Landmark l1 = new Landmark(0.0, 0.0, null);
 Image icon1 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/landmarkIcon1.png"));
 l1.setIcon(new ImageIcon(icon1));
 Landmark l2 = new Landmark(1.0, 1.0, null);
 Image icon2 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/landmarkIcon2.png"));
 l2.setIcon(new ImageIcon(icon2));
 display.addLandMark(l1);
 display.addLandMark(l2);
 Graphics2D g = (Graphics2D)display.createGraphics();
 display.paintComponent(g);
 int width = display.getWidth();
 int height = display.getHeight();
 for (int x = 0; x < width; x++) {
 for (int y = 0; y < height; y++) {
 if (g.getColor().equals(Color.GREEN) && x > width / 2 && x < width / 2 + 5 && y > height / 2 && y < height / 2 + 5) {
 // Fox point
 continue;
 }
 if ((x > width / 2 - 10 && x < width / 2 + 10) && (y > height / 2 - 10 && y < height / 2 + 10)) {
 // Landmark 1
 assertTrue(g.getImage(x, y) != null);
 }
 if ((x > width / 2 + 5 && x < width / 2 + 15) && (y > height / 2 - 10 && y < height / 2 + 10)) {
 // Landmark 2
 assertTrue(g.getImage(x, y) != null);
 }
 if (!((x > width / 2 - 10 && x < width / 2 + 10) || (x > width / 2 + 5 && x < width / 2 + 15) ||
 (Math.abs(x - (int)(p1.getX() * width)) < 10 && Math.abs(y - (int)(p1.getY() * height)) < 10) ||
 (Math.abs(x - (int)(p2.getX() * width)) < 10 && Math.abs(y - (int)(p2.getY() * height)) < 10))) {
 fail("Unexpected pixel color: " + g.getColor());
 }
 }
 }
 g.dispose();
 }
}