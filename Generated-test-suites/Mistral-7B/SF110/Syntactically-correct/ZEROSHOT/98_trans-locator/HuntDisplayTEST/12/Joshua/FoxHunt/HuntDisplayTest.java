package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Test
 public void testCreateInstance() {
 display = new HuntDisplay();
 assertNotNull(display);
 }

 @Test
 public void testGetPreferredSize() {
 HuntDisplay hd = new HuntDisplay();
 assertEquals(new Dimension(200, 200), hd.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 HuntDisplay hd = new HuntDisplay();
 assertEquals(new Dimension(50, 50), hd.getMinimumSize());
 }

 @Test
 public void testAddPoint() {
 HuntDisplay hd = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 hd.addPoint(trigPoint);
 assertEquals(1, hd.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullPoint() {
 HuntDisplay hd = new HuntDisplay();
 TrigPoint trigPoint = null;
 hd.addPoint(trigPoint);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testAddLandMark() {
 HuntDisplay hd = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/image.png")));
 hd.addLandMark(landmark);
 assertEquals(1, hd.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandMarkWithNullLandmark() {
 HuntDisplay hd = new HuntDisplay();
 Landmark landmark = null;
 hd.addLandMark(landmark);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testPaintComponent() {
 HuntDisplay hd = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/image.png")));
 hd.addPoint(trigPoint);
 hd.addLandMark(landmark);
 BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 hd.paintComponent(g);
 g.dispose();
 assertNotNull(hd.getGraphics());
 }

 @Test
 public void testPaintComponentWithNullGraphics() {
 HuntDisplay hd = new HuntDisplay();
 assertNull(hd.getGraphics());
 }

 @Test
 public void testCalculateCentroid() {
 HuntDisplay hd = new HuntDisplay();
 TrigPoint[] trigpoints = {new TrigPoint(1.0, 2.0, 0.0, false), new TrigPoint(3.0, 4.0, 0.0, false), new TrigPoint(5.0, 6.0, 0.0, false)};
 hd.points.addAll(Arrays.asList(trigpoints));
 double trigPointAvgX = hd.calculateCentroidX(trigpoints);
 double trigPointAvgY = hd.calculateCentroidY(trigpoints);
 double expectedTrigPointAvgX = (1.0 + 3.0 + 5.0) / 3.0;
 double expectedTrigPointAvgY = (2.0 + 4.0 + 6.0) / 3.0;
 assertEquals(expectedTrigPointAvgX, trigPointAvgX, 0.001);
 assertEquals(expectedTrigPointAvgY, trigPointAvgY, 0.001);
 }
}