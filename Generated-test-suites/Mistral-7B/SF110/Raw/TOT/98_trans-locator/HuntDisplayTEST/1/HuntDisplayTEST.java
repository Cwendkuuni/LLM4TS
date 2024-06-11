package Joshua.FoxHunt.Tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Before
 public void setUp() {
 this.display = new HuntDisplay();
 }

 @Test
 public void testGetPreferredSize() {
 Dimension expected = new Dimension(200, 200);
 Dimension actual = this.display.getPreferredSize();
 assertEquals(expected.getWidth(), actual.getWidth());
 assertEquals(expected.getHeight(), actual.getHeight());
 }

 @Test
 public void testGetMinimumSize() {
 Dimension expected = new Dimension(50, 50);
 Dimension actual = this.display.getMinimumSize();
 assertEquals(expected.getWidth(), actual.getWidth());
 assertEquals(expected.getHeight(), actual.getHeight());
 }

 @Test
 public void testAddPoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 this.display.addPoint(point);
 assertEquals(1, this.display.points.size());
 }

 @Test
 public void testAddMultiplePoints() {
 TrigPoint[] points = new TrigPoint[5];
 for (int i = 0; i < points.length; i++) {
 points[i] = new TrigPoint(i + 1.0, i + 2.0, 0.0, false);
 this.display.addPoint(points[i]);
 }
 assertEquals(5, this.display.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullPoint() {
 this.display.addPoint(null);
 }

 @Test
 public void testPaintComponent_NoPoints() {
 Graphics g = this.createMockGraphics();
 this.display.paintComponent(g);
 assertTrue(g.getColor().equals(Color.WHITE));
 }

 @Test
 public void testPaintComponent_SinglePoint() {
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 this.display.addPoint(point);
 Graphics g = this.createMockGraphics();
 this.display.paintComponent(g);
 int expectedX = (int)((point.getX() * this.display.getWidth()) + this.display.getWidth() / 2.0 - point.getX() * this.display.getWidth() / 2.0);
 int expectedY = (int)((point.getY() * this.display.getHeight()) + this.display.getHeight() / 2.0 - point.getY() * this.display.getHeight() / 2.0);
 int actualX = 0;
 int actualY = 0;
 g.getGraphicsConfig().getTranslatedTransform().getTransformComponents(null, null, null, null, null, null);
 g.getGraphicsConfig().getDeviceConfiguration().getBounds().getLocation(actualX, actualY);
 assertEquals(expectedX, actualX);
 assertEquals(expectedY, actualY);
 }

 @Test
 public void testPaintComponent_MultiplePoints() {
 TrigPoint[] points = new TrigPoint[5];
 for (int i = 0; i < points.length; i++) {
 points[i] = new TrigPoint(i + 1.0, i + 2.0, 0.0, false);
 this.display.addPoint(points[i]);
 }
 Graphics g = this.createMockGraphics();
 this.display.paintComponent(g);
 for (TrigPoint point : points) {
 int expectedX = (int)((point.getX() * this.display.getWidth()) + this.display.getWidth() / 2.0 - point.getX() * this.display.getWidth() / 2.0);
 int expectedY = (int)((point.getY() * this.display.getHeight()) + this.display.getHeight() / 2.0 - point.getY() * this.display.getHeight() / 2.0);
 int actualX = 0;
 int actualY = 0;
 g.getGraphicsConfig().getTranslatedTransform().getTransformComponents(null, null, null, null, null, null);
 g.getGraphicsConfig().getDeviceConfiguration().getBounds().getLocation(actualX, actualY);
 assertEquals(expectedX, actualX);
 assertEquals(expectedY, actualY);
 }
 }

 @Test
 public void testPaintComponent_FoxPoint() {
 TrigPoint foxPoint = new TrigPoint(1.0, 2.0, 0.0, true);
 this.display.addPoint(foxPoint);
 Graphics g = this.createMockGraphics();
 this.display.paintComponent(g);
 int expectedX = (int)((foxPoint.getX() * this.display.getWidth()) + this.display.getWidth() / 2.0 - foxPoint.getX() * this.display.getWidth() / 2.0);
 int expectedY = (int)((foxPoint.getY() * this.display.getHeight()) + this.display.getHeight() / 2.0 - foxPoint.getY() * this.display.getHeight() / 2.0);
 int actualX = 0;
 int actualY = 0;
 g.getGraphicsConfig().getTranslatedTransform().getTransformComponents(null, null, null, null, null, null);
 g.getGraphicsConfig().getDeviceConfiguration().getBounds().getLocation(actualX, actualY);
 assertEquals(expectedX, actualX);
 assertEquals(expectedY, actualY);
 assertTrue(g.getColor().equals(Color.GREEN));
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(3.0, 4.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
 this.display.addLandmark(landmark);
 assertEquals(1, this.display.landmarks.size());
 }

 @Test
 public void testAddMultipleLandmarks() {
 Landmark[] landmarks = new Landmark[5];
 for (int i = 0; i < landmarks.length; i++) {
 landmarks[i] = new Landmark(i + 3.0, i + 4.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
 this.display.addLandmark(landmarks[i]);
 }
 assertEquals(5, this.display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullLandmark() {
 this.display.addLandmark(null);
 }

 @Test
 public void testPaintComponent_NoLandmarks() {
 Graphics g = this.createMockGraphics();
 this.display.paintComponent(g);
 }

 @Test
 public void testPaintComponent_SingleLandmark() {
 Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
 this.display.addLandmark(landmark);
 Graphics g = this.createMockGraphics();
 this.display.paintComponent(g);
 int expectedX = (int)((landmark.getLon() * this.display.getWidth()) + this.display.getWidth() / 2.0 - landmark.getLon() * this.display.getWidth() / 2.0);
 int expectedY = (int)((landmark.getLat() * this.display.getHeight()) + this.display.getHeight() / 2.0 - landmark.getLat() * this.display.getHeight() / 2.0);
 int actualX = 0;
 int actualY = 0;
 g.getGraphicsConfig().getTranslatedTransform().getTransformComponents(null, null, null, null, null, null);
 g.getGraphicsConfig().getDeviceConfiguration().getBounds().getLocation(actualX, actualY);
 assertEquals(expectedX, actualX);
 assertEquals(expectedY, actualY);
 }

 @Test
 public void testPaintComponent_MultipleLandmarks() {
 Landmark[] landmarks = new Landmark[5];
 for (int i = 0; i < landmarks.length; i++) {
 landmarks[i] = new Landmark(i + 3.0, i + 4.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
 this.display.addLandmark(landmarks[i]);
 }
 Graphics g = this.createMockGraphics();
 this.display.paintComponent(g);
 for (Landmark landmark : landmarks) {
 int expectedX = (int)((landmark.getLon() * this.display.getWidth()) + this.display.getWidth() / 2.0 - landmark.getLon() * this.display.getWidth() / 2.0);
 int expectedY = (int)((landmark.getLat() * this.display.getHeight()) + this.display.getHeight() / 2.0 - landmark.getLat() * this.display.getHeight() / 2.0);
 int actualX = 0;
 int actualY = 0;
 g.getGraphicsConfig().getTranslatedTransform().getTransformComponents(null, null, null, null, null, null);
 g.getGraphicsConfig().getDeviceConfiguration().getBounds().getLocation(actualX, actualY);
 assertEquals(expectedX, actualX);
 assertEquals(expectedY, actualY);
 }
 }

 private Graphics createMockGraphics() {
 Graphics2D mockGraphics = mock(Graphics2D.class);
 Graphics graphics = new Graphics() {
 @Override
 public Graphics2D getGraphics2D() {
 return mockGraphics;
 }
 };
 return graphics;
 }
}