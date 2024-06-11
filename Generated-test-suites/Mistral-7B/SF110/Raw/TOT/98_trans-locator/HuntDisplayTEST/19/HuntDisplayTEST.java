import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;
 private JFrame testFrame;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 testFrame = new JFrame();
 testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 testFrame.add(huntDisplay);
 testFrame.pack();
 testFrame.setSize(400, 400);
 testFrame.setVisible(true);
 }

 @Test
 public void testAddPoint() {
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 HuntDisplay expectedDisplay = new HuntDisplay();
 expectedDisplay.addPoint(trigPoint);
 BufferedImage expectedImage = new BufferedImage(huntDisplay.getWidth(), huntDisplay.getHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics2D g = expectedImage.createGraphics();
 expectedDisplay.paintComponent(g);
 g.dispose();

 BufferedImage actualImage = huntDisplay.createImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));
 huntDisplay.addLandmark(landmark);
 HuntDisplay expectedDisplay = new HuntDisplay();
 expectedDisplay.addLandmark(landmark);

 BufferedImage expectedImage = new BufferedImage(huntDisplay.getWidth(), huntDisplay.getHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics2D g = expectedImage.createGraphics();
 expectedDisplay.paintComponent(g);
 g.dispose();

 BufferedImage actualImage = huntDisplay.createImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithInvalidPoint() {
 TrigPoint trigPoint = null;
 huntDisplay.addPoint(trigPoint);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullLandmark() {
 Landmark landmark = null;
 huntDisplay.addLandmark(landmark);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullIcon() {
 Landmark landmark = new Landmark(1.0, 2.0, null);
 huntDisplay.addLandmark(landmark);
 }

 @Test
 public void testGetSize() {
 Dimension expectedSize = new Dimension(200, 200);
 Dimension actualSize = huntDisplay.getSize();
 assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testGetBackground() {
 Color expectedBackground = Color.WHITE;
 Color actualBackground = huntDisplay.getBackground();
 assertEquals(expectedBackground, actualBackground);
 }

 @Test
 public void testPaintComponentWithEmptyLists() {
 BufferedImage expectedImage = new BufferedImage(huntDisplay.getWidth(), huntDisplay.getHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics2D g = expectedImage.createGraphics();
 huntDisplay.paintComponent(g);
 g.dispose();

 BufferedImage actualImage = huntDisplay.createImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
 Landmark landmark1 = new Landmark(1.5, 2.5, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));
 Landmark landmark2 = new Landmark(3.5, 4.5, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));

 huntDisplay.addPoint(trigPoint1);
 huntDisplay.addPoint(trigPoint2);
 huntDisplay.addLandmark(landmark1);
 huntDisplay.addLandmark(landmark2);

 HuntDisplay expectedDisplay = new HuntDisplay();
 expectedDisplay.addPoint(trigPoint1);
 expectedDisplay.addPoint(trigPoint2);
 expectedDisplay.addLandmark(landmark1);
 expectedDisplay.addLandmark(landmark2);

 BufferedImage expectedImage = new BufferedImage(huntDisplay.getWidth(), huntDisplay.getHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics2D g = expectedImage.createGraphics();
 expectedDisplay.paintComponent(g);
 g.dispose();

 BufferedImage actualImage = huntDisplay.createImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testPaintComponentWithFoxPoint() {
 TrigPoint foxPoint = new TrigPoint(1.0, 2.0, 0.0, true);
 huntDisplay.addPoint(foxPoint);

 BufferedImage expectedImage = new BufferedImage(huntDisplay.getWidth(), huntDisplay.getHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics2D g = expectedImage.createGraphics();
 g.setColor(Color.GREEN);
 g.fillOval((int)(foxPoint.getX() * huntDisplay.getWidth() + 0.5), (int)(foxPoint.getY() * huntDisplay.getHeight() + 0.5), 5, 5);
 g.setColor(huntDisplay.getForeground());
 huntDisplay.paintComponent(g);
 g.dispose();

 BufferedImage actualImage = huntDisplay.createImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 Landmark landmark1 = new Landmark(1.5, 2.5, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));
 Landmark landmark2 = new Landmark(3.5, 4.5, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));

 huntDisplay.addLandmark(landmark1);
 huntDisplay.addLandmark(landmark2);

 BufferedImage expectedImage = new BufferedImage(huntDisplay.getWidth(), huntDisplay.getHeight(), BufferedImage.TYPE_INT_RGB);
 Graphics2D g = expectedImage.createGraphics();
 g.setColor(Color.BLACK);
 for (Object obj : huntDisplay.getLandmarks()) {
 Landmark landmark = (Landmark) obj;
 int x = (int)(landmark.getLon() * huntDisplay.getWidth() + 0.5);
 int y = (int)(landmark.getLat() * huntDisplay.getHeight() + 0.5);
 g.drawImage(landmark.getIcon().getImage(), x, y, 10, 10, huntDisplay);
 }
 g.dispose();

 BufferedImage actualImage = huntDisplay.createImage();
 assertEquals(expectedImage, actualImage);
 }
}