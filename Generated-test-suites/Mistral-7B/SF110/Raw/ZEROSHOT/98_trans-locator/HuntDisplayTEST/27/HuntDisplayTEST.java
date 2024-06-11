package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;
 private BufferedImage image;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
 }

 @Test
 public void testHuntDisplayConstructor() {
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
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 assertEquals(1, huntDisplay.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointNullPoint() {
 huntDisplay.addPoint(null);
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
 huntDisplay.addLandmark(landmark);
 assertEquals(1, huntDisplay.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkNullLandmark() {
 huntDisplay.addLandmark(null);
 }

 @Test
 public void testPaintComponent() {
 Graphics2D g = image.createGraphics();
 huntDisplay.paintComponent(g);
 g.dispose();
 assertNotNull(huntDisplay);
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
 huntDisplay.addPoint(trigPoint);
 huntDisplay.addLandmark(landmark);
 Graphics2D g = image.createGraphics();
 huntDisplay.paintComponent(g);
 g.dispose();
 assertNotNull(huntDisplay);
 }

 @Test
 public void testPaintComponentWithOnlyPoints() {
 TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
 huntDisplay.addPoint(trigPoint1);
 huntDisplay.addPoint(trigPoint2);
 Graphics2D g = image.createGraphics();
 huntDisplay.paintComponent(g);
 g.dispose();
 assertNotNull(huntDisplay);
 }

 @Test
 public void testPaintComponentWithOnlyLandmarks() {
 Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
 Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon("icon.png"));
 huntDisplay.addLandmark(landmark1);
 huntDisplay.addLandmark(landmark2);
 Graphics2D g = image.createGraphics();
 huntDisplay.paintComponent(g);
 g.dispose();
 assertNotNull(huntDisplay);
 }

 @Test
 public void testDisplayPointsAndLandmarks() {
 TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
 Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
 Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon("icon.png"));
 huntDisplay.addPoint(trigPoint1);
 huntDisplay.addPoint(trigPoint2);
 huntDisplay.addLandmark(landmark1);
 huntDisplay.addLandmark(landmark2);
 Graphics2D g = image.createGraphics();
 huntDisplay.paintComponent(g);
 g.dispose();
 JFrame frame = new JFrame();
 JLabel label = new JLabel(new ImageIcon(image));
 frame.getContentPane().add(label);
 frame.pack();
 frame.setVisible(true);
 }

}