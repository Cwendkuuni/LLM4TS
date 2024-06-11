package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

@RunWith(JUnit4.class)
public class HuntDisplayTest {

 private HuntDisplay display;

 @Before
 public void setUp() {
 this.display = new HuntDisplay();
 }

 @Test
 public void testInitialState() {
 assertEquals(new Dimension(200, 200), this.display.getPreferredSize());
 assertEquals(new Dimension(50, 50), this.display.getMinimumSize());
 assertTrue(this.display.points.isEmpty());
 assertTrue(this.display.landmarks.isEmpty());
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 this.display.addPoint(point);
 assertEquals(1, this.display.points.size());
 assertEquals(point, this.display.points.elementAt(0));
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));
 this.display.addLandmark(landmark);
 assertEquals(1, this.display.landmarks.size());
 assertEquals(landmark, this.display.landmarks.elementAt(0));
 }

 @Test
 public void testPaintComponent_NoPointsOrLandmarks() {
 Graphics2D g = this.createMockGraphics2D();
 this.display.paintComponent(g);
 assertNull(g.getColor());
 }

 @Test
 public void testPaintComponent_WithPoints() {
 Graphics2D g = this.createMockGraphics2D();
 TrigPoint p1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint p2 = new TrigPoint(3.0, 4.0, 0.0, false);
 this.display.addPoint(p1);
 this.display.addPoint(p2);
 this.display.paintComponent(g);
 assertEquals(Color.BLACK, g.getColor());
 for (int i = 0; i < this.display.points.size(); i++) {
 TrigPoint point = this.display.points.elementAt(i);
 int x = (int)((point.getX() + 0.5 - this.display.getTrigPointAvgX()) * this.display.getWidth());
 int y = (int)((point.getY() + 0.5 - this.display.getTrigPointAvgY()) * this.display.getHeight());
 point.isFox() ? assertEquals(Color.GREEN, g.getColor()) : assertEquals(Color.BLACK, g.getColor());
 g.fillOval(x, y, 5, 5);
 }
 }

 @Test
 public void testPaintComponent_WithLandmarks() {
 Graphics2D g = this.createMockGraphics2D();
 Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));
 this.display.addLandmark(landmark);
 this.display.paintComponent(g);
 assertEquals(Color.getBackgroundColors()[0], g.getColor());
 int x = (int)((landmark.getLon() + 0.5 - this.display.getTrigPointAvgX()) * this.display.getWidth());
 int y = (int)((landmark.getLat() + 0.5 - this.display.getTrigPointAvgY()) * this.display.getHeight());
 g.drawImage(landmark.getIcon().getImage(), x, y, 10, 10, this.display.getBackground(), this);
 }

 @Test
 public void testPaintComponent_WithPointsAndLandmarks() {
 Graphics2D g = this.createMockGraphics2D();
 TrigPoint p1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint p2 = new TrigPoint(3.0, 4.0, 0.0, false);
 Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));
 this.display.addPoint(p1);
 this.display.addPoint(p2);
 this.display.addLandmark(landmark);
 this.display.paintComponent(g);
 assertEquals(Color.getBackgroundColors()[0], g.getColor());
 for (int i = 0; i < this.display.points.size(); i++) {
 TrigPoint point = this.display.points.elementAt(i);
 int x = (int)((point.getX() + 0.5 - this.display.getTrigPointAvgX()) * this.display.getWidth());
 int y = (int)((point.getY() + 0.5 - this.display.getTrigPointAvgY()) * this.display.getHeight());
 point.isFox() ? assertEquals(Color.GREEN, g.getColor()) : assertEquals(Color.BLACK, g.getColor());
 g.fillOval(x, y, 5, 5);
 }
 int x = (int)((landmark.getLon() + 0.5 - this.display.getTrigPointAvgX()) * this.display.getWidth());
 int y = (int)((landmark.getLat() + 0.5 - this.display.getTrigPointAvgY()) * this.display.getHeight());
 g.drawImage(landmark.getIcon().getImage(), x, y, 10, 10, this.display.getBackground(), this);
 }

 private Graphics2D createMockGraphics2D() {
 GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
 Graphics2D g = gc.createGraphics2D();
 return g;
 }

}