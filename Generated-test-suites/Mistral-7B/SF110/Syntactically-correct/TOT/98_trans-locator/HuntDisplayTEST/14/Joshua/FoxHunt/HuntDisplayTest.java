package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private static final int WIDTH = 400;
 private static final int HEIGHT = 400;

 @Before
 public void setUp() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(WIDTH, HEIGHT);

 HuntDisplay huntDisplay = new HuntDisplay();
 huntDisplay.setSize(WIDTH, HEIGHT);
 huntDisplay.setBackground(Color.WHITE);

 frame.add(huntDisplay);
 frame.setVisible(true);
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testAddPoint() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);

 assertEquals(1, huntDisplay.points.size());
 }

 @Test
 public void testAddLandMark() {
 HuntDisplay huntDisplay = new HuntDisplay();
 Landmark landmark = new Landmark(3.0, 4.0, new ImageIcon(BufferedImage.TYPE_INT_RGB, 5, 5, Color.RED.getRGB()));
 huntDisplay.addLandMark(landmark);

 assertEquals(1, huntDisplay.landmarks.size());
 }

 @Test
 public void testAddPointAndLandMark() {
 HuntDisplay huntDisplay = new HuntDisplay();

 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 Landmark landmark = new Landmark(3.0, 4.0, new ImageIcon(BufferedImage.TYPE_INT_RGB, 5, 5, Color.RED.getRGB()));
 huntDisplay.addLandMark(landmark);

 assertEquals(2, huntDisplay.points.size());
 assertEquals(1, huntDisplay.landmarks.size());
 }

 @Test
 public void testPaintComponentWithNoPoints() {
 HuntDisplay huntDisplay = new HuntDisplay();
 BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 huntDisplay.paintComponent(g);
 g.dispose();

 assertArrayEquals(new byte[]{}, image.getRGB(0, 0, WIDTH, HEIGHT, null, 0));
 }

 @Test
 public void testPaintComponentWithPoints() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
 TrigPoint trigPoint3 = new TrigPoint(5.0, 6.0, 0.0, false);

 huntDisplay.addPoint(trigPoint1);
 huntDisplay.addPoint(trigPoint2);
 huntDisplay.addPoint(trigPoint3);

 HuntDisplay huntDisplay2 = new HuntDisplay();
 BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 huntDisplay2.paintComponent(g);
 g.dispose();

 BufferedImage expectedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
 Graphics2D expectedG = expectedImage.createGraphics();
 huntDisplay.paintComponent(expectedG);
 g.dispose();

 assertArrayEquals(expectedImage.getRGB(0, 0, WIDTH, HEIGHT, null, 0), image.getRGB(0, 0, WIDTH, HEIGHT, null, 0));
 }

 @Test
 public void testPaintComponentWithFoxPoint() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, true);
 TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
 TrigPoint trigPoint3 = new TrigPoint(5.0, 6.0, 0.0, false);

 huntDisplay.addPoint(trigPoint1);
 huntDisplay.addPoint(trigPoint2);
 huntDisplay.addPoint(trigPoint3);

 HuntDisplay huntDisplay2 = new HuntDisplay();
 BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 huntDisplay2.paintComponent(g);
 g.dispose();

 BufferedImage expectedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
 Graphics2D expectedG = expectedImage.createGraphics();
 huntDisplay.paintComponent(expectedG);
 g.dispose();

 expectedG.setColor(Color.GREEN);
 expectedG.fillOval((int)Math.round(trigPoint1.getX() * WIDTH), (int)Math.round(trigPoint1.getY() * HEIGHT), 5, 5);
 expectedG.dispose();

 assertArrayEquals(expectedImage.getRGB(0, 0, WIDTH, HEIGHT, null, 0), image.getRGB(0, 0, WIDTH, HEIGHT, null, 0));
 }

 @Test
 public void testPaintComponentWithLandMark() {
 HuntDisplay huntDisplay = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(BufferedImage.TYPE_INT_RGB, 5, 5, Color.RED.getRGB()));
 TrigPoint trigPoint = new TrigPoint(3.0, 4.0, 0.0, false);
 huntDisplay.addLandMark(landmark);
 huntDisplay.addPoint(trigPoint);

 HuntDisplay huntDisplay2 = new HuntDisplay();
 BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 huntDisplay2.paintComponent(g);
 g.dispose();

 BufferedImage expectedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
 Graphics2D expectedG = expectedImage.createGraphics();
 huntDisplay.paintComponent(expectedG);
 g.dispose();

 expectedG.drawImage(landmark.getIcon().getImage(), (int)Math.round(landmark.getLon() * WIDTH), (int)Math.round(landmark.getLat() * HEIGHT), 10, 10, null);
 expectedG.dispose();

 assertArrayEquals(expectedImage.getRGB(0, 0, WIDTH, HEIGHT, null, 0), image.getRGB(0, 0, WIDTH, HEIGHT, null, 0));
 }

 @Test
 public void testPaintComponentWithPointsAndLandMark() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
 TrigPoint trigPoint3 = new TrigPoint(5.0, 6.0, 0.0, false);
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(BufferedImage.TYPE_INT_RGB, 5, 5, Color.RED.getRGB()));

 huntDisplay.addPoint(trigPoint1);
 huntDisplay.addPoint(trigPoint2);
 huntDisplay.addPoint(trigPoint3);
 huntDisplay.addLandMark(landmark);

 HuntDisplay huntDisplay2 = new HuntDisplay();
 BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 huntDisplay2.paintComponent(g);
 g.dispose();

 BufferedImage expectedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
 Graphics2D expectedG = expectedImage.createGraphics();
 huntDisplay.paintComponent(expectedG);
 g.dispose();

 expectedG.setColor(Color.GREEN);
 expectedG.fillOval((int)Math.round(trigPoint1.getX() * WIDTH), (int)Math.round(trigPoint1.getY() * HEIGHT), 5, 5);
 expectedG.fillOval((int)Math.round(trigPoint2.getX() * WIDTH), (int)Math.round(trigPoint2.getY() * HEIGHT), 5, 5);
 expectedG.fillOval((int)Math.round(trigPoint3.getX() * WIDTH), (int)Math.round(trigPoint3.getY() * HEIGHT), 5, 5);
 expectedG.drawImage(landmark.getIcon().getImage(), (int)Math.round(landmark.getLon() * WIDTH), (int)Math.round(landmark.getLat() * HEIGHT), 10, 10, null);
 expectedG.dispose();

 assertArrayEquals(expectedImage.getRGB(0, 0, WIDTH, HEIGHT, null, 0), image.getRGB(0, 0, WIDTH, HEIGHT, null, 0));
 }
}