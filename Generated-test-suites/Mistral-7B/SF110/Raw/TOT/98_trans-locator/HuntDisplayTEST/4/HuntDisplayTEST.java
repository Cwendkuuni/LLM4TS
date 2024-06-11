package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import javax.swing.*;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;
 private List<TrigPoint> trigPoints;
 private List<Landmark> landmarks;

 @Before
 public void setUp() {
 display = new HuntDisplay();
 trigPoints = new ArrayList<>();
 landmarks = new ArrayList<>();
 }

 @Test
 public void testGetPreferredSize() {
 assertEquals(new Dimension(200, 200), display.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 assertEquals(new Dimension(50, 50), display.getMinimumSize());
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 assertTrue(display.points.contains(point));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullPoint() {
 display.addPoint(null);
 fail();
 }

 @Test
 public void testAddLandmarks() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)));
 display.addLandmark(landmark);
 assertTrue(display.landmarks.contains(landmark));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullLandmark() {
 display.addLandmark(null);
 fail();
 }

 @Test
 public void testPaintComponent() {
 Graphics2D g = mock(Graphics2D.class);
 display.paintComponent(g);
 verify(g, times(display.landmarks.size())).drawImage(any(Image.class), anyInt(), anyInt(), anyInt(), anyInt(), eq(display.getBackground()), any(Component.class));
 }

 @Test
 public void testAddPointAndPaintComponent() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 display.paintComponent(mock(Graphics.class));
 assertTrue(display.points.contains(point));
 }

 @Test
 public void testAddLandmarkAndPaintComponent() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)));
 display.addLandmark(landmark);
 display.paintComponent(mock(Graphics.class));
 assertTrue(display.landmarks.contains(landmark));
 }

 @Test
 public void testAddPointAndAddLandmarkAndPaintComponent() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)));
 display.addPoint(point);
 display.addLandmark(landmark);
 display.paintComponent(mock(Graphics.class));
 assertTrue(display.points.contains(point));
 assertTrue(display.landmarks.contains(landmark));
 }
}