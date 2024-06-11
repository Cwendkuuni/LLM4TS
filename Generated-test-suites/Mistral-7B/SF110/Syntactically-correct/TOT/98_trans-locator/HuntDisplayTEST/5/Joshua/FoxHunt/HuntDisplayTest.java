import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Test
 public void testConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertNotNull(hd);
 assertEquals(200, hd.getPrefSize().width);
 assertEquals(200, hd.getPrefSize().height);
 assertEquals(50, hd.getMinSize().width);
 assertEquals(50, hd.getMinSize().height);
 }

 @Test
 public void testAddPoint() {
 display = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test
 public void testAddPointWithDuplicate() {
 display = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test
 public void testAddPointWithFox() {
 display = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, true);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullPoint() {
 display = new HuntDisplay();
 display.addPoint(null);
 }

 @Test
 public void testAddLandMark() {
 display = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("testIcon.png"));
 display.addLandMark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test
 public void testAddLandMarkWithDuplicate() {
 display = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("testIcon.png"));
 display.addLandMark(landmark);
 display.addLandMark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test
 public void testPaintComponent_NoPoints() {
 display = new HuntDisplay();
 Graphics2D g = createMockGraphics2D();
 display.paintComponent(g);
 assertTrue(g.getColor().equals(Color.WHITE));
 }

 @Test
 public void testPaintComponent_SinglePoint() {
 display = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 Graphics2D g = createMockGraphics2D();
 display.paintComponent(g);
 int x = (int)((point.getX() + 0.5) * display.getWidth());
 int y = (int)((point.getY() + 0.5) * display.getHeight());
 assertTrue(g.getColor().equals(Color.BLACK));
 g.fillOval(x, y, 5, 5);
 }

 @Test
 public void testPaintComponent_SingleFox() {
 display = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, true);
 display.addPoint(point);
 Graphics2D g = createMockGraphics2D();
 display.paintComponent(g);
 int x = (int)((point.getX() + 0.5) * display.getWidth());
 int y = (int)((point.getY() + 0.5) * display.getHeight());
 assertTrue(g.getColor().equals(Color.GREEN));
 g.fillOval(x, y, 5, 5);
 }

 @Test
 public void testPaintComponent_MultiplePoints() {
 display = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
 display.addPoint(point1);
 display.addPoint(point2);
 Graphics2D g = createMockGraphics2D();
 display.paintComponent(g);
 int x1 = (int)((point1.getX() + 0.5) * display.getWidth());
 int y1 = (int)((point1.getY() + 0.5) * display.getHeight());
 int x2 = (int)((point2.getX() + 0.5) * display.getWidth());
 int y2 = (int)((point2.getY() + 0.5) * display.getHeight());
 assertTrue(g.getColor().equals(Color.BLACK));
 g.fillOval(x1, y1, 5, 5);
 g.fillOval(x2, y2, 5, 5);
 }

 @Test
 public void testPaintComponent_LandMarks() {
 display = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("testIcon.png"));
 display.addLandMark(landmark);
 Graphics2D g = createMockGraphics2D();
 display.paintComponent(g);
 int x = (int)((landmark.getLon() + 0.5) * display.getWidth());
 int y = (int)((landmark.getLat() + 0.5) * display.getHeight());
 assertTrue(g.getColor().equals(getForeground()));
 g.drawImage(landmark.getIcon().getImage(), x, y, 10, 10, display.getBackground(), display);
 }

 @Test
 public void testPaintComponent_MultipleLandMarks() {
 display = new HuntDisplay();
 Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon("testIcon1.png"));
 Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon("testIcon2.png"));
 display.addLandMark(landmark1);
 display.addLandMark(landmark2);
 Graphics2D g = createMockGraphics2D();
 display.paintComponent(g);
 int x1 = (int)((landmark1.getLon() + 0.5) * display.getWidth());
 int y1 = (int)((landmark1.getLat() + 0.5) * display.getHeight());
 int x2 = (int)((landmark2.getLon() + 0.5) * display.getWidth());
 int y2 = (int)((landmark2.getLat() + 0.5) * display.getHeight());
 assertTrue(g.getColor().equals(getForeground()));
 g.drawImage(landmark1.getIcon().getImage(), x1, y1, 10, 10, display.getBackground(), display);
 g.drawImage(landmark2.getIcon().getImage(), x2, y2, 10, 10, display.getBackground(), display);
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 return g;
 }

 private Color getForeground() {
 return display.getForeground();
 }

}