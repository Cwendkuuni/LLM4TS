import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;
import java.awt.Image;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Test
 public void testConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertNotNull(hd);
 }

 @Test
 public void testGetPreferredSize() {
 HuntDisplay hd = new HuntDisplay();
 Dimension prefSize = hd.getPreferredSize();
 assertEquals(200, prefSize.getHeight());
 assertEquals(200, prefSize.getWidth());
 }

 @Test
 public void testGetMinimumSize() {
 HuntDisplay hd = new HuntDisplay();
 Dimension minSize = hd.getMinimumSize();
 assertEquals(50, minSize.getHeight());
 assertEquals(50, minSize.getWidth());
 }

 @Test
 public void testAddPoint() {
 HuntDisplay hd = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 hd.addPoint(point);
 assertEquals(1, hd.points.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullPoint() {
 HuntDisplay hd = new HuntDisplay();
 TrigPoint point = null;
 hd.addPoint(point);
 }

 @Test
 public void testAddLandmark() {
 HuntDisplay hd = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/image.png")));
 hd.addLandmark(landmark);
 assertEquals(1, hd.landmarks.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullLandmark() {
 HuntDisplay hd = new HuntDisplay();
 Landmark landmark = null;
 hd.addLandmark(landmark);
 }

 @Test
 public void testPaintComponent() {
 HuntDisplay hd = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 Landmark landmark = new Landmark(1.5, 3.5, new ImageIcon(getClass().getResource("/image.png")));
 hd.addPoint(point1);
 hd.addPoint(point2);
 hd.addLandmark(landmark);
 Graphics2D g = (Graphics2D) hd.getGraphics();
 hd.paintComponent(g);
 int width = hd.getWidth();
 int height = hd.getHeight();
 int point1X = (int)((point1.getX() + 0.5 - (0.5 - width / 2.0)) * width);
 int point1Y = (int)((point1.getY() + 0.5 - (0.5 - height / 2.0)) * height);
 int point2X = (int)((point2.getX() + 0.5 - (0.5 - width / 2.0)) * width);
 int point2Y = (int)((point2.getY() + 0.5 - (0.5 - height / 2.0)) * height);
 int landmarkX = (int)((landmark.getLon() + 0.5 - (0.5 - width / 2.0)) * width);
 int landmarkY = (int)((landmark.getLat() + 0.5 - (0.5 - height / 2.0)) * height);
 // Checking that points have been drawn correctly
 assertTrue(g.getColor().equals(Color.BLACK));
 g.setColor(Color.GREEN);
 g.fillOval(point1X, point1Y, 5, 5);
 g.setColor(Color.BLACK);
 g.fillOval(point2X, point2Y, 5, 5);
 // Checking that landmark has been drawn correctly
 g.setColor(hd.getForeground());
 g.drawImage(landmark.getIcon().getImage(), landmarkX, landmarkY, 10, 10, hd);
 }
}