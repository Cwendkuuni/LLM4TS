package Joshua.FoxHunt.tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

public class HuntDisplayTest {

 private HuntDisplay display;
 private JFrame frame;

 @Before
 public void setUp() {
 display = new HuntDisplay();
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.add(display);
 frame.pack();
 frame.setSize(display.getPreferredSize().width + 10, display.getPreferredSize().height + 20);
 frame.setVisible(true);
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(1.0 / 90.0, 2.0 / 180.0, 0.0, false);
 display.addPoint(point);
 assertEquals(1, display.points.size());
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(3.0 / 90.0, 4.0 / 180.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 display.addLandmark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 TrigPoint[] trigpoints = new TrigPoint[5];
 Landmark[] landmarks = new Landmark[5];
 for (int i = 0; i < trigpoints.length; i++) {
 trigpoints[i] = new TrigPoint(i / 90.0, i / 180.0, 0.0, false);
 }
 for (int i = 0; i < landmarks.length; i++) {
 landmarks[i] = new Landmark(i / 90.0, i / 180.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 }
 display.points = new Vector<>(Arrays.asList(trigpoints));
 display.landmarks = new Vector<>(Arrays.asList(landmarks));
 display.repaint();
 //Use assertions to check the position and color of points and landmarks
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullPoint() {
 display.addPoint(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullLandmark() {
 display.addLandmark(null);
 }

 @Test
 public void testGetPreferredSize() {
 assertEquals(new Dimension(200, 200), display.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 assertEquals(new Dimension(50, 50), display.getMinimumSize());
 }
}