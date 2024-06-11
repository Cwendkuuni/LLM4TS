package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 }

 // Test constructor
 @Test
 public void testHuntDisplayConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertNotNull(hd);
 }

 // Test getPreferredSize method
 @Test
 public void testGetPreferredSize() {
 assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
 }

 // Test getMinimumSize method
 @Test
 public void testGetMinimumSize() {
 assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
 }

 // Test addPoint method with valid arguments
 @Test
 public void testAddPointValidArguments() {
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 assertEquals(1, huntDisplay.points.size());
 }

 // Test addPoint method with null argument
 @Test(expected = NullPointerException.class)
 public void testAddPointNullArgument() {
 huntDisplay.addPoint(null);
 }

 // Test addLandMark method with valid arguments
 @Test
 public void testAddLandMarkValidArguments() {
 Landmark landmark = new Landmark(3.0, 4.0, new ImageIcon(getClass().getResource("/icon.png")));
 huntDisplay.addLandMark(landmark);
 assertEquals(1, huntDisplay.landmarks.size());
 }

 // Test addLandMark method with null argument
 @Test(expected = NullPointerException.class)
 public void testAddLandMarkNullArgument() {
 huntDisplay.addLandMark(null);
 }

 // Test paintComponent method with empty list
 @Test
 public void testPaintComponentEmptyList() {
 Graphics2D g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 assertFalse(g.getColor().equals(Color.GREEN));
 }

 // Test paintComponent method with points list
 @Test
 public void testPaintComponentWithPointsList() {
 TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
 huntDisplay.addPoint(trigPoint1);
 huntDisplay.addPoint(trigPoint2);
 Graphics2D g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 int trigPoint1X = (int)(trigPoint1.getX() * huntDisplay.getWidth());
 int trigPoint1Y = (int)(trigPoint1.getY() * huntDisplay.getHeight());
 int trigPoint2X = (int)(trigPoint2.getX() * huntDisplay.getWidth());
 int trigPoint2Y = (int)(trigPoint2.getY() * huntDisplay.getHeight());
 assertTrue(g.getColor().equals(Color.BLACK));
 g.setColor(Color.GREEN);
 g.fillOval(trigPoint1X, trigPoint1Y, 5, 5);
 g.setColor(Color.BLACK);
 g.fillOval(trigPoint2X, trigPoint2Y, 5, 5);
 }

 // Test paintComponent method with landmarks list
 @Test
 public void testPaintComponentWithLandmarksList() {
 Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/icon.png")));
 Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(getClass().getResource("/icon.png")));
 huntDisplay.addLandMark(landmark1);
 huntDisplay.addLandMark(landmark2);
 Graphics2D g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 int landmark1X = (int)(landmark1.getLon() * huntDisplay.getWidth());
 int landmark1Y = (int)(landmark1.getLat() * huntDisplay.getHeight());
 int landmark2X = (int)(landmark2.getLon() * huntDisplay.getWidth());
 int landmark2Y = (int)(landmark2.getLat() * huntDisplay.getHeight());
 Image image1 = landmark1.getIcon().getImage();
 Image image2 = landmark2.getIcon().getImage();
 BufferedImage bufferedImage1 = (BufferedImage)image1;
 BufferedImage bufferedImage2 = (BufferedImage)image2;
 for (int i = 0; i < bufferedImage1.getWidth(); i++) {
 for (int j = 0; j < bufferedImage1.getHeight(); j++) {
 int pixel1 = bufferedImage1.getRGB(i, j);
 int pixel2 = g.getRGB(landmark1X + i, landmark1Y + j);
 assertEquals(pixel1, pixel2);
 }
 }
 for (int i = 0; i < bufferedImage2.getWidth(); i++) {
 for (int j = 0; j < bufferedImage2.getHeight(); j++) {
 int pixel1 = bufferedImage2.getRGB(i, j);
 int pixel2 = g.getRGB(landmark2X + i, landmark2Y + j);
 assertEquals(pixel1, pixel2);
 }
 }
 }

}