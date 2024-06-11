package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class HuntDisplayTest {

 private HuntDisplay display;
 private Vector<TrigPoint> points;
 private Vector<Landmark> landmarks;

 @Before
 public void setUp() {
 display = new HuntDisplay();
 points = new Vector<>();
 landmarks = new Vector<>();
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
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(trigPoint);
 points.add(trigPoint);
 verify(display).repaint();

 int width = 10;
 int height = 10;
 assertEquals(new Dimension(width, height), display.getPreferredSize());

 graphicsTest(display, trigPoint.getX(), trigPoint.getY(), Color.BLACK);
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(3.0, 4.0, createImageIcon("path/to/icon.png"));
 display.addLandMark(landmark);
 landmarks.add(landmark);
 verify(display).repaint();

 graphicsTest(display, landmark.getLon(), landmark.getLat(), Color.LIGHT_GRAY);
 }

 @Test
 public void testAddPointAndLandMark() {
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 Landmark landmark = new Landmark(3.0, 4.0, createImageIcon("path/to/icon.png"));
 display.addPoint(trigPoint);
 display.addLandMark(landmark);
 points.add(trigPoint);
 landmarks.add(landmark);
 verify(display).repaint();

 int width = 10 + landmark.getIcon().getIconWidth() + 5;
 int height = 10 + landmark.getIcon().getIconHeight() + 5;
 assertEquals(new Dimension(width, height), display.getPreferredSize());

 graphicsTest(display, trigPoint.getX(), trigPoint.getY(), Color.BLACK);
 graphicsTest(display, landmark.getLon(), landmark.getLat(), Color.LIGHT_GRAY);
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullPoint() {
 display.addPoint(null);
 verify(display, never()).repaint();
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullLandMark() {
 Landmark landmark = null;
 display.addLandMark(landmark);
 verify(display, never()).repaint();
 }

 private void graphicsTest(HuntDisplay display, double coordinate, int colorCode, Color expectedColor) {
 Graphics2D g = mock(Graphics2D.class);
 when(display.getGraphics()).thenReturn(g);

 display.paintComponent(g);
 int x = (int)((coordinate + 0.5) * display.getWidth());
 int y = (int)((0.5 - display.getHeight()) * display.getHeight() + display.getHeight());

 g.setColor(argThat(isA(Color.class)));
 verify(g).setColor(argThat(isA(expectedColor.getClass())));
 g.fillOval(x, y, 5, 5);

 verifyNoMoreInteractions(g);
 }

 private BufferedImage createImageIcon(String path) {
 BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
 Graphics2D g = image.createGraphics();
 g.setPaint(Color.RED);
 g.fillRect(0, 0, 10, 10);
 g.dispose();
 return image;
}

}