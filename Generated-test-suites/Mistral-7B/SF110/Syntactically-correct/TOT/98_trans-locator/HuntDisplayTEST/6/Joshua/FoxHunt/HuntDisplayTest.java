package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Before
 public void setup() {
 this.display = new HuntDisplay();
 }

 @Test
 public void testGetPreferredSize() {
 assertEquals(new Dimension(200, 200), this.display.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 assertEquals(new Dimension(50, 50), this.display.getMinimumSize());
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 this.display.addPoint(point);
 Vector points = this.display.getPoints();
 assertEquals(1, points.size());
 TrigPoint addedPoint = (TrigPoint) points.get(0);
 assertEquals(1.0, addedPoint.getY(), 0.01);
 assertEquals(2.0, addedPoint.getX(), 0.01);
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 this.display.addLandmark(landmark);
 Vector landmarks = this.display.getLandmarks();
 assertEquals(1, landmarks.size());
 Landmark addedLandmark = (Landmark) landmarks.get(0);
 assertEquals(1.0, addedLandmark.getLat(), 0.01);
 assertEquals(2.0, addedLandmark.getLon(), 0.01);
 }

 @Test
 public void testAddPointAndLandmark() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 this.display.addPoint(point);
 this.display.addLandmark(landmark);
 Vector points = this.display.getPoints();
 assertEquals(1, points.size());
 TrigPoint addedPoint = (TrigPoint) points.get(0);
 assertEquals(1.0, addedPoint.getY(), 0.01);
 assertEquals(2.0, addedPoint.getX(), 0.01);
 Vector landmarks = this.display.getLandmarks();
 assertEquals(1, landmarks.size());
 Landmark addedLandmark = (Landmark) landmarks.get(0);
 assertEquals(1.0, addedLandmark.getLat(), 0.01);
 assertEquals(2.0, addedLandmark.getLon(), 0.01);
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(300, 300);
 frame.add(this.display);
 frame.setVisible(true);
 Graphics2D g = (Graphics2D) this.display.createGraphics();
 this.display.paintComponent(g);
 frame.dispose();
 assertTrue(this.display.getBackground().equals(Color.WHITE));
 }

 @Test
 public void testPaintComponentWithPoints() {
 TrigPoint[] points = { new TrigPoint(1.0, 2.0, 0.0, false), new TrigPoint(3.0, 4.0, 0.0, false), new TrigPoint(5.0, 6.0, 0.0, false) };
 this.display.points = new Vector();
 for (TrigPoint point : points) {
 this.display.addPoint(point);
 }
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(300, 300);
 frame.add(this.display);
 frame.setVisible(true);
 Graphics2D g = (Graphics2D) this.display.createGraphics();
 this.display.paintComponent(g);
 frame.dispose();
 for (int i = 0; i < points.length; i++) {
 TrigPoint point = points[i];
 assertTrue(g.getColor().equals(point.isFox() ? Color.GREEN : Color.BLACK));
 int x = (int)((point.getX() + 0.5 - this.display.getWidth() / 2.0) * this.display.getWidth());
 int y = (int)((point.getY() + 0.5 - this.display.getHeight() / 2.0) * this.display.getHeight());
 assertEquals(5, g.fillOval(x, y, 5, 5).getWidth());
 }
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 Landmark[] landmarks = { new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB))),
 new Landmark(3.0, 4.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB))),
 new Landmark(5.0, 6.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB))) };
 this.display.landmarks = new Vector();
 for (Landmark landmark : landmarks) {
 this.display.addLandmark(landmark);
 }
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(300, 300);
 frame.add(this.display);
 frame.setVisible(true);
 Graphics2D g = (Graphics2D) this.display.createGraphics();
 this.display.paintComponent(g);
 frame.dispose();
 for (int i = 0; i < landmarks.length; i++) {
 Landmark landmark = landmarks[i];
 int x = (int)((landmark.getLon() + 0.5 - this.display.getWidth() / 2.0) * this.display.getWidth());
 int y = (int)((landmark.getLat() + 0.5 - this.display.getHeight() / 2.0) * this.display.getHeight());
 BufferedImage image = landmark.getIcon().getImage();
 assertEquals(image.getHeight(), 10);
 assertEquals(image.getWidth(), 10);
 assertEquals(image, g.getImage());
 }
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 TrigPoint[] points = { new TrigPoint(1.0, 2.0, 0.0, false), new TrigPoint(3.0, 4.0, 0.0, false), new TrigPoint(5.0, 6.0, 0.0, false) };
 Landmark[] landmarks = { new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB))),
 new Landmark(3.0, 4.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB))),
 new Landmark(5.0, 6.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB))) };
 this.display.points = new Vector();
 this.display.landmarks = new Vector();
 for (TrigPoint point : points) {
 this.display.addPoint(point);
 }
 for (Landmark landmark : landmarks) {
 this.display.addLandmark(landmark);
 }
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(300, 300);
 frame.add(this.display);
 frame.setVisible(true);
 Graphics2D g = (Graphics2D) this.display.createGraphics();
 this.display.paintComponent(g);
 frame.dispose();
 for (int i = 0; i < points.length; i++) {
 TrigPoint point = points[i];
 assertTrue(g.getColor().equals(point.isFox() ? Color.GREEN : Color.BLACK));
 int x = (int)((point.getX() + 0.5 - this.display.getWidth() / 2.0) * this.display.getWidth());
 int y = (int)((point.getY() + 0.5 - this.display.getHeight() / 2.0) * this.display.getHeight());
 assertEquals(5, g.fillOval(x, y, 5, 5).getWidth());
 }
 for (int i = 0; i < landmarks.length; i++) {
 Landmark landmark = landmarks[i];
 int x = (int)((landmark.getLon() + 0.5 - this.display.getWidth() / 2.0) * this.display.getWidth());
 int y = (int)((landmark.getLat() + 0.5 - this.display.getHeight() / 2.0) * this.display.getHeight());
 BufferedImage image = landmark.getIcon().getImage();
 assertEquals(image.getHeight(), 10);
 assertEquals(image.getWidth(), 10);
 assertEquals(image, g.getImage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullPoint() {
 this.display.addPoint(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullLandmark() {
 this.display.addLandmark(null);
 }
}