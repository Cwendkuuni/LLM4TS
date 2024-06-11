package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;
 private ImageIcon landmarkIcon;

 @Before
 public void setup() {
 huntDisplay = new HuntDisplay();
 landmarkIcon = new ImageIcon(getClass().getResource("/landmarkIcon.png"));
 }

 @Test
 public void testHuntDisplayConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertNotNull(hd);
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
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(point);
 huntDisplay.repaint();

 Graphics2D g = huntDisplay.createGraphics();
 Point2D pointLocation = huntDisplay.getLocationOfComponent(huntDisplay, point.getX(), point.getY());
 assertEquals(point.getX(), pointLocation.getX(), 0.01);
 assertEquals(point.getY(), pointLocation.getY(), 0.01);
 g.dispose();
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(3.0, 4.0, landmarkIcon);
 huntDisplay.addLandMark(landmark);
 huntDisplay.repaint();

 Graphics2D g = huntDisplay.createGraphics();
 Point2D landmarkLocation = huntDisplay.getLocationOfComponent(huntDisplay, landmark.getLon(), landmark.getLat());
 assertEquals(landmark.getLon(), landmarkLocation.getX(), 0.01);
 assertEquals(landmark.getLat(), landmarkLocation.getY(), 0.01);
 g.dispose();
 }

 @Test
 public void testPaintComponent() {
 HuntDisplay hd = new HuntDisplay();
 hd.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
 hd.addPoint(new TrigPoint(3.0, 4.0, 0.0, false));
 hd.addLandMark(new Landmark(5.0, 6.0, landmarkIcon));
 BufferedImage image = new BufferedImage(hd.getWidth(), hd.getHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 hd.paint(g);
 g.dispose();

 // Perform visual assertions using libraries like Hamcrest or Custom assertions
 // for example, using Hamcrest:
 // assertThat(image, Matchers.hasColor(Color.GREEN, anyOf(hasWidth(5), hasHeight(5))));
 }
}