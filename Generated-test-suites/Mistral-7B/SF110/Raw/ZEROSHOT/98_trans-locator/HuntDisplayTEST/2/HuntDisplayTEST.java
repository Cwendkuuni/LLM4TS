package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

public class HuntDisplayTest {

 private HuntDisplay display;
 private Vector<TrigPoint> points;
 private Vector<Landmark> landmarks;

 @Before
 public void setUp() {
 display = new HuntDisplay();
 points = new Vector<TrigPoint>();
 landmarks = new Vector<Landmark>();
 }

 @Test
 public void testConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertEquals(new Dimension(50, 50), hd.getMinimumSize());
 assertEquals(new Dimension(200, 200), hd.getPreferredSize());
 }

 @Test
 public void testAddPoint() {
 points.add(new TrigPoint(1.0, 2.0, 0.0, false));
 display.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
 assertEquals(1, display.points.size());
 }

 @Test
 public void testAddLandmark() {
 BufferedImage icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 landmarks.add(new Landmark(1.0, 2.0, new ImageIcon(icon)));
 display.addLandmark(new Landmark(1.0, 2.0, new ImageIcon(icon)));
 assertEquals(1, display.landmarks.size());
 }

 @Test
 public void testPaintComponent() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.add(display);
 frame.pack();
 frame.setVisible(true);
 Graphics g = display.getGraphics();
 assertNotNull(g);
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));
 display.addPoint(point);
 display.addLandmark(landmark);
 display.paintComponent(g);
 int width = frame.getWidth();
 int height = frame.getHeight();
 int x = (int)((point.getX() + 0.5) * width);
 int y = (int)((point.getY() + 0.5) * height);
 Color expectedColor = point.isFox() ? Color.GREEN : Color.BLACK;
 Color actualColor = new Color(g.getRGB(x, y));
 assertEquals(expectedColor, actualColor);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointNullPoint() {
 display.addPoint(null);
 }
}