package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Test
 public void testHuntDisplayConstruction() {
 display = new HuntDisplay();
 assertNotNull(display);
 }

 @Test
 public void testGetPreferredSize() {
 display = new HuntDisplay();
 assertEquals(new Dimension(200, 200), display.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 display = new HuntDisplay();
 assertEquals(new Dimension(50, 50), display.getMinimumSize());
 }

 @Test
 public void testAddPoint() {
 display = new HuntDisplay();
 TrigPoint point = new TrigPoint(45.21, 123.45, 12.34, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test
 public void testAddLandmark() {
 display = new HuntDisplay();
 Landmark landmark = new Landmark(55.21, 133.45, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
 display.addLandmark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 display = new HuntDisplay();
 Graphics2D g = (Graphics2D) display.createGraphics();
 display.paintComponent(g);
 assertEmpty(g.getColor());
 g.dispose();
 }

 @Test
 public void testPaintComponentWithPoints() {
 display = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(45.21, 123.45, 12.34, false);
 TrigPoint point2 = new TrigPoint(46.21, 123.46, 12.35, false);
 display.addPoint(point1);
 display.addPoint(point2);
 Graphics2D g = (Graphics2D) display.createGraphics();
 display.paintComponent(g);

 int x1 = (int) (point1.getX() * display.getWidth());
 int y1 = (int) (point1.getY() * display.getHeight());
 int x2 = (int) (point2.getX() * display.getWidth());
 int y2 = (int) (point2.getY() * display.getHeight());

 assertEquals(Color.BLACK, g.getColor());
 g.fillOval(x1, y1, 5, 5);
 g.fillOval(x2, y2, 5, 5);
 g.dispose();
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 display = new HuntDisplay();
 Landmark landmark1 = new Landmark(45.21, 123.45, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
 Landmark landmark2 = new Landmark(46.21, 123.46, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
 display.addLandmark(landmark1);
 display.addLandmark(landmark2);
 Graphics2D g = (Graphics2D) display.createGraphics();
 display.paintComponent(g);

 int x1 = (int) (landmark1.getLon() * display.getWidth());
 int y1 = (int) (landmark1.getLat() * display.getHeight());
 int x2 = (int) (landmark2.getLon() * display.getWidth());
 int y2 = (int) (landmark2.getLat() * display.getHeight());

 assertEquals(Color.WHITE, g.getColor());
 g.drawImage(landmark1.getIcon().getImage(), x1, y1, 10, 10, display.getBackground(), null);
 g.drawImage(landmark2.getIcon().getImage(), x2, y2, 10, 10, display.getBackground(), null);
 g.dispose();
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 display = new HuntDisplay();
 TrigPoint point1 = new TrigPoint(45.21, 123.45, 12.34, false);
 TrigPoint point2 = new TrigPoint(46.21, 123.46, 12.35, false);
 Landmark landmark1 = new Landmark(45.21, 123.45, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
 Landmark landmark2 = new Landmark(46.21, 123.46, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
 display.addPoint(point1);
 display.addPoint(point2);
 display.addLandmark(landmark1);
 display.addLandmark(landmark2);
 Graphics2D g = (Graphics2D) display.createGraphics();
 display.paintComponent(g);

 int x1 = (int) (point1.getX() * display.getWidth());
 int y1 = (int) (point1.getY() * display.getHeight());
 int x2 = (int) (point2.getX() * display.getWidth());
 int y2 = (int) (point2.getY() * display.getHeight());
 int x3 = (int) (landmark1.getLon() * display.getWidth());
 int y3 = (int) (landmark1.getLat() * display.getHeight());
 int x4 = (int) (landmark2.getLon() * display.getWidth());
 int y4 = (int) (landmark2.getLat() * display.getHeight());

 assertEquals(Color.BLACK, g.getColor());
 g.fillOval(x1, y1, 5, 5);
 g.fillOval(x2, y2, 5, 5);
 assertEquals(Color.WHITE, g.getColor());
 g.drawImage(landmark1.getIcon().getImage(), x3, y3, 10, 10, display.getBackground(), null);
 g.drawImage(landmark2.getIcon().getImage(), x4, y4, 10, 10, display.getBackground(), null);
 g.dispose();
 }
}