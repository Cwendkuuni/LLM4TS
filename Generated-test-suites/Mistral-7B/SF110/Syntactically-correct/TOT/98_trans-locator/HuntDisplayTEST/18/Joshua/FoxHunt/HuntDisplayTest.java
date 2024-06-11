package Joshua.FoxHunt.test;

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
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;
 private Vector<TrigPoint> points;
 private Vector<Landmark> landmarks;

 @Before
 public void setUp() {
 this.display = new HuntDisplay();
 this.points = new Vector<TrigPoint>();
 this.landmarks = new Vector<Landmark>();
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
 this.points.add(point);
 assertEquals(1, this.points.size());
 assertEquals(1, this.display.points.size());
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(3.0, 4.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 this.display.addLandMark(landmark);
 this.landmarks.add(landmark);
 assertEquals(1, this.landmarks.size());
 assertEquals(1, this.display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullPoint() {
 TrigPoint point = null;
 this.display.addPoint(point);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullLandMark() {
 Landmark landmark = null;
 this.display.addLandMark(landmark);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testPaintComponentWithPoints() {
 Graphics g = this.createMockGraphics();
 this.display.paintComponent(g);
 this.points.add(new TrigPoint(1.0, 2.0, 0.0, false));
 this.points.add(new TrigPoint(3.0, 4.0, 0.0, false));
 this.points.add(new TrigPoint(5.0, 6.0, 0.0, false));
 this.display.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
 this.display.addPoint(new TrigPoint(3.0, 4.0, 0.0, false));
 this.display.addPoint(new TrigPoint(5.0, 6.0, 0.0, false));
 this.display.repaint();

 int expectedPointCount = 3;
 int actualPointCount = 0;
 for (int y = 0; y < this.display.getHeight(); ++y) {
 for (int x = 0; x < this.display.getWidth(); ++x) {
 if (g.getColor().equals(Color.GREEN)) {
 if (this.display.isFoxAt(x, y)) {
 actualPointCount++;
 }
 }
 else if (g.getColor().equals(Color.BLACK)) {
 actualPointCount++;
 }
 }
 }
 assertEquals(expectedPointCount, actualPointCount);
 }

 @Test
 public void testPaintComponentWithLandMarks() {
 Graphics g = this.createMockGraphics();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 this.display.addLandMark(landmark);
 this.landmarks.add(landmark);
 this.display.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
 this.display.repaint();

 int expectedLandMarkCount = 1;
 int actualLandMarkCount = 0;
 for (int y = 0; y < this.display.getHeight(); ++y) {
 for (int x = 0; x < this.display.getWidth(); ++x) {
 if (g.getColor().equals(this.landmark.getIcon().getColor())) {
 actualLandMarkCount++;
 }
 }
 }
 assertEquals(expectedLandMarkCount, actualLandMarkCount);
 }

 @Test
 public void testIsFoxAt() {
 this.display.addPoint(new TrigPoint(1.0, 2.0, 0.0, true));
 assertTrue(this.display.isFoxAt(1, 2));
 }

 @Test
 public void testIsFoxAtEdge() {
 this.display.addPoint(new TrigPoint(0.0, 0.0, 0.0, true));
 assertFalse(this.display.isFoxAt(0, 0));
 }

 @Test
 public void testIsFoxAtOutside() {
 this.display.addPoint(new TrigPoint(-1.0, -1.0, 0.0, true));
 assertFalse(this.display.isFoxAt(-1, -1));
 }

 @Test
 public void testIsLandMarkAt() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 this.display.addLandMark(landmark);
 this.display.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
 this.display.repaint();

 int expectedLandMarkX = 0;
 int expectedLandMarkY = 0;
 int actualLandMarkX = 0;
 int actualLandMarkY = 0;
 for (int y = 0; y < this.display.getHeight(); ++y) {
 for (int x = 0; x < this.display.getWidth(); ++x) {
 if (g.getColor().equals(this.landmark.getIcon().getColor())) {
 actualLandMarkX = x;
 actualLandMarkY = y;
 }
 }
 }
 assertEquals(expectedLandMarkX, actualLandMarkX);
 assertEquals(expectedLandMarkY, actualLandMarkY);
 }

 @Test
 public void testIsLandMarkAtEdge() {
 Landmark landmark = new Landmark(0.0, 0.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 this.display.addLandMark(landmark);
 this.display.addPoint(new TrigPoint(0.0, 0.0, 0.0, false));
 this.display.repaint();

 int expectedLandMarkX = 0;
 int expectedLandMarkY = 0;
 int actualLandMarkX = 0;
 int actualLandMarkY = 0;
 for (int y = 0; y < this.display.getHeight(); ++y) {
 for (int x = 0; x < this.display.getWidth(); ++x) {
 if (g.getColor().equals(this.landmark.getIcon().getColor())) {
 actualLandMarkX = x;
 actualLandMarkY = y;
 }
 }
 }
 assertEquals(expectedLandMarkX, actualLandMarkX);
 assertEquals(expectedLandMarkY, actualLandMarkY);
 }

 @Test
 public void testIsLandMarkAtOutside() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 this.display.addLandMark(landmark);
 this.display.addPoint(new TrigPoint(-1.0, -1.0, 0.0, false));
 this.display.repaint();

 int expectedLandMarkX = 0;
 int expectedLandMarkY = 0;
 int actualLandMarkX = 0;
 int actualLandMarkY = 0;
 for (int y = 0; y < this.display.getHeight(); ++y) {
 for (int x = 0; x < this.display.getWidth(); ++x) {
 if (g.getColor().equals(this.landmark.getIcon().getColor())) {
 actualLandMarkX = x;
 actualLandMarkY = y;
 }
 }
 }
 assertFalse(actualLandMarkX == 0 && actualLandMarkY == 0);
 }

 @Test
 public void testDisplaySize() {
 JFrame frame = new JFrame();
 frame.add(this.display);
 frame.pack();
 assertEquals(200, frame.getSize().width);
 assertEquals(200, frame.getSize().height);
 }

 private Graphics g;
 private void createMockGraphics() {
 this.g = this.createMock(Graphics.class);
 }

 private Graphics createMockGraphics() {
 return this.g;
 }
}