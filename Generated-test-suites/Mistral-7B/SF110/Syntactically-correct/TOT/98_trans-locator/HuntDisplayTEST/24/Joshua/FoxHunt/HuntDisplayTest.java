package Joshua.FoxHunt.Tests;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.*;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 }

 @Test
 public void testConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertNotNull(hd);
 assertEquals(50, hd.getMinimumSize().width);
 assertEquals(50, hd.getMinimumSize().height);
 assertEquals(200, hd.getPreferredSize().width);
 assertEquals(200, hd.getPreferredSize().height);
 }

 @Test
 public void testAddPoint() {
 TrigPoint trigPoint = new TrigPoint(1.0, 180.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 assertEquals(1, huntDisplay.points.size());
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(1.0, 180.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 huntDisplay.addLandMark(landmark);
 assertEquals(1, huntDisplay.landmarks.size());
 }

 @Test
 public void testAddPointAndLandMark() {
 TrigPoint trigPoint = new TrigPoint(1.0, 180.0, 0.0, false);
 Landmark landmark = new Landmark(1.0, 180.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 huntDisplay.addPoint(trigPoint);
 huntDisplay.addLandMark(landmark);
 assertEquals(2, huntDisplay.points.size());
 assertEquals(1, huntDisplay.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTrigPointX() {
 TrigPoint trigPoint = new TrigPoint(-1.0, 180.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 fail("Exception not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTrigPointY() {
 TrigPoint trigPoint = new TrigPoint(1.0, -180.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 fail("Exception not thrown");
 }

 @Test
 public void testAddMultiplePoints() {
 for (int i = 0; i < 10; i++) {
 TrigPoint trigPoint = new TrigPoint(i / 2.0 + 1.0, i % 2 == 0 ? 180.0 : 179.999, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 }
 assertEquals(10, huntDisplay.points.size());
 }

 @Test
 public void testPaintComponent() {
 Graphics2D g = huntDisplay.createGraphic();
 huntDisplay.paintComponent(g);
 huntDisplay.addPoint(new TrigPoint(0.5, 0.5, 0.0, false));
 huntDisplay.addLandMark(new Landmark(0.5, 0.5, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB))));
 huntDisplay.paintComponent(g);
 g.dispose();
 }

 @After
 public void tearDown() {
 huntDisplay = null;
 }
}