import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Vector;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Test
 public void testConstructor() {
 HuntDisplay testDisplay = new HuntDisplay();
 assertNotNull(testDisplay);
 }

 @Test
 public void testGetPreferredSize() {
 HuntDisplay testDisplay = new HuntDisplay();
 Dimension size = testDisplay.getPreferredSize();
 assertEquals(new Dimension(200, 200), size);
 }

 @Test
 public void testGetMinimumSize() {
 HuntDisplay testDisplay = new HuntDisplay();
 Dimension size = testDisplay.getMinimumSize();
 assertEquals(new Dimension(50, 50), size);
 }

 @Test
 public void testAddPoint() {
 HuntDisplay testDisplay = new HuntDisplay();
 TrigPoint testPoint = new TrigPoint(5.0 / 90.0, 10.0 / 180.0, 0.0, false);
 testDisplay.addPoint(testPoint);
 assertNotNull(testDisplay.points);
 assertEquals(1, testDisplay.points.size());
 }

 @Test
 public void testAddLandmark() {
 HuntDisplay testDisplay = new HuntDisplay();
 Landmark testLandmark = new Landmark(2.5 / 90.0, 15.0 / 180.0, createImageIcon("/icon/landmark.png"));
 testDisplay.addLandmark(testLandmark);
 assertNotNull(testDisplay.landmarks);
 assertEquals(1, testDisplay.landmarks.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullLandmark() {
 HuntDisplay testDisplay = new HuntDisplay();
 Landmark nullLandmark = null;
 testDisplay.addLandmark(nullLandmark);
 }

 @Test
 public void testPaintComponentWithPoints() {
 HuntDisplay testDisplay = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(1.0 / 90.0, 2.0 / 180.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0 / 90.0, 4.0 / 180.0, 0.0, false);
 TrigPoint point3 = new TrigPoint(5.0 / 90.0, 6.0 / 180.0, 0.0, false);
 testDisplay.addPoint(point1);
 testDisplay.addPoint(point2);
 testDisplay.addPoint(point3);
 Graphics2D graphics = (Graphics2D) testDisplay.createGraphics();
 testDisplay.paintComponent(graphics);
 int[] xPoints = { (int)((point1.getX() + 0.5) * testDisplay.getWidth()),
                  (int)((point2.getX() + 0.5) * testDisplay.getWidth()),
                  (int)((point3.getX() + 0.5) * testDisplay.getWidth()) };
 int[] yPoints = { (int)((point1.getY() + 0.5) * testDisplay.getHeight()),
                  (int)((point2.getY() + 0.5) * testDisplay.getHeight()),
                  (int)((point3.getY() + 0.5) * testDisplay.getHeight()) };
 for (int i = 0; i < 3; i++) {
     assertEquals(Color.BLACK, testDisplay.getGraphics().getColor());
     graphics.setColor(Color.BLACK);
     graphics.fillOval(xPoints[i], yPoints[i], 5, 5);
 }
 }

 @Test
 public void testPaintComponentWithFox() {
 HuntDisplay testDisplay = new HuntDisplay();
 TrigPoint foxPoint = new TrigPoint(1.0 / 90.0, 2.0 / 180.0, 0.0, true);
 testDisplay.addPoint(foxPoint);
 Graphics2D graphics = (Graphics2D) testDisplay.createGraphics();
 testDisplay.paintComponent(graphics);
 int x = (int)((foxPoint.getX() + 0.5) * testDisplay.getWidth());
 int y = (int)((foxPoint.getY() + 0.5) * testDisplay.getHeight());
 assertEquals(Color.GREEN, testDisplay.getGraphics().getColor());
 graphics.setColor(Color.GREEN);
 graphics.fillOval(x, y, 5, 5);
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 HuntDisplay testDisplay = new HuntDisplay();
 Landmark landmark1 = new Landmark(1.0 / 90.0, 2.0 / 180.0, createImageIcon("/icon/landmark1.png"));
 Landmark landmark2 = new Landmark(3.0 / 90.0, 4.0 / 180.0, createImageIcon("/icon/landmark2.png"));
 testDisplay.addLandmark(landmark1);
 testDisplay.addLandmark(landmark2);
 Graphics2D graphics = (Graphics2D) testDisplay.createGraphics();
 testDisplay.paintComponent(graphics);
 int[] xPoints = { (int)((landmark1.getLon() + 0.5) * testDisplay.getWidth()),
                  (int)((landmark2.getLon() + 0.5) * testDisplay.getWidth()) };
 int[] yPoints = { (int)((landmark1.getLat() + 0.5) * testDisplay.getHeight()),
                  (int)((landmark2.getLat() + 0.5) * testDisplay.getHeight()) };
 BufferedImage image1 = ImageIO.read(new ByteArrayInputStream(landmark1.getIcon().getImage().getBytes("byte")));
 BufferedImage image2 = ImageIO.read(new ByteArrayInputStream(landmark2.getIcon().getImage().getBytes("byte")));
 for (int i = 0; i < 2; i++) {
     assertEquals(Color.BLACK, testDisplay.getGraphics().getColor());
     graphics.setColor(Color.BLACK);
     graphics.drawImage(image1, xPoints[i], yPoints[i], 10, 10, testDisplay);
     assertEquals(image1, testDisplay.getGraphics().getImage());
     graphics.setColor(Color.BLACK);
     graphics.drawImage(image2, xPoints[i], yPoints[i], 10, 10, testDisplay);
     assertEquals(image2, testDisplay.getGraphics().getImage());
 }
 }

 private BufferedImage createImageIcon(String path) {
 BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 image.getGraphics().drawImage(new ImageIcon(getClass().getResource(path)).getImage(), 0, 0, null);
 return image;
 }

}