package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import Joshua.FoxHunt.*;

@RunWith(JUnit4.class)
public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 }

 @Test
 public void testConstructor() {
 HuntDisplay display = new HuntDisplay();
 assertNotNull(display);
 }

 @Test
 public void testGetPreferredSize() {
 Dimension preferredSize = huntDisplay.getPreferredSize();
 assertEquals(200, preferredSize.getWidth());
 assertEquals(200, preferredSize.getHeight());
 }

 @Test
 public void testGetMinimumSize() {
 Dimension minimumSize = huntDisplay.getMinimumSize();
 assertEquals(50, minimumSize.getWidth());
 assertEquals(50, minimumSize.getHeight());
 }

 @Test
 public void testAddPoint() {
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 3.0, true);
 huntDisplay.addPoint(trigPoint);
 assertEquals(1, huntDisplay.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullTrigPoint() {
 huntDisplay.addPoint(null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(3.0, 4.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
 huntDisplay.addLandmark(landmark);
 assertEquals(1, huntDisplay.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullLandmark() {
 huntDisplay.addLandmark(null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testPaintComponent() {
 Graphics2D g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 assertNotNull(g);
 huntDisplay.repaint();
 }

 @Test
 public void testPaintComponentWithEmptyLists() {
 Graphics2D g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 assertFalse(g.getColor().equals(Color.GREEN));
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 3.0, true);
 Landmark landmark = new Landmark(3.0, 4.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
 huntDisplay.addPoint(trigPoint);
 huntDisplay.addLandmark(landmark);
 huntDisplay.paintComponent(huntDisplay.createGraphics());
 assertTrue(huntDisplay.getBackground().equals(Color.WHITE));
 }
}