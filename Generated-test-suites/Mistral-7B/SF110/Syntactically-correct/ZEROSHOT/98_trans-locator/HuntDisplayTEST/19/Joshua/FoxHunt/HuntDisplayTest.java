package Joshua.FoxHunt.Tests;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

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
 }

 @Test
 public void testGetPreferredSize() {
 assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
 }

 @Test
 public void testAddPoint() {
 Vector<TrigPoint> points = huntDisplay.getPoints();
 huntDisplay.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
 assertEquals(1, points.size());
 huntDisplay.addPoint(new TrigPoint(3.0, 4.0, 0.0, false));
 assertEquals(2, points.size());
 huntDisplay.addPoint(new TrigPoint(5.0, 6.0, 0.0, true));
 assertEquals(3, points.size());
 }

 @Test
 public void testAddLandMark() {
 Vector<Landmark> landmarks = huntDisplay.getLandmarks();
 BufferedImage icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
 huntDisplay.addLandMark(new Landmark(1.0, 2.0, icon));
 assertEquals(1, landmarks.size());
 huntDisplay.addLandMark(new Landmark(3.0, 4.0, icon));
 assertEquals(2, landmarks.size());
 }

 @Test
 public void testPaintComponent() {
 Graphics2D g = (Graphics2D) huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 // Add assertions for painting points and landmarks here
 g.dispose();
 }
}