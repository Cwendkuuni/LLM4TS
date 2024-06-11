package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Test
 public void testHuntDisplayConstructor() {
 huntDisplay = new HuntDisplay();
 assertEquals(200, huntDisplay.getPreferredSize().getWidth());
 assertEquals(200, huntDisplay.getPreferredSize().getHeight());
 }

 @Test
 public void testAddPoint() {
 huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = new TrigPoint(45.0, 75.0, 90.0, true);
 huntDisplay.addPoint(trigPoint);
 assertFalse(huntDisplay.points.isEmpty());
 HuntDisplay spyDisplay = spy(huntDisplay);
 verify(spyDisplay).repaint();
 }

 @Test
 public void testAddLandmark() {
 huntDisplay = new HuntDisplay();
 BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 image.setRGB(0, 0, 0xFF0000); // red color
 Landmark landmark = new Landmark(50.0, 100.0, image);
 huntDisplay.addLandmark(landmark);
 assertFalse(huntDisplay.landmarks.isEmpty());
 HuntDisplay spyDisplay = spy(huntDisplay);
 verify(spyDisplay).repaint();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullTrigPoint() {
 huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = null;
 huntDisplay.addPoint(trigPoint);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullLandmark() {
 huntDisplay = new HuntDisplay();
 Landmark landmark = null;
 huntDisplay.addLandmark(landmark);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testPaintComponent_NoPointsOrLandmarks() {
 HuntDisplay spyDisplay = spy(huntDisplay);
 Graphics graphicsMock = mock(Graphics.class);
 when(graphicsMock.getWidth()).thenReturn(200);
 when(graphicsMock.getHeight()).thenReturn(200);
 when(graphicsMock.getColor()).thenReturn(Color.BLACK);
 huntDisplay.paintComponent(graphicsMock);
 verifyNoMoreInteractions(graphicsMock);
 }

 @Test
 public void testPaintComponent_WithPoints() {
 HuntDisplay spyDisplay = spy(huntDisplay);
 Graphics graphicsMock = mock(Graphics.class);
 when(graphicsMock.getWidth()).thenReturn(200);
 when(graphicsMock.getHeight()).thenReturn(200);
 when(graphicsMock.getColor()).thenReturn(Color.BLACK);

 TrigPoint trigPoint1 = new TrigPoint(45.0, 75.0, 90.0, true);
 TrigPoint trigPoint2 = new TrigPoint(50.0, 80.0, 180.0, false);
 Vector<TrigPoint> points = new Vector<>();
 points.add(trigPoint1);
 points.add(trigPoint2);
 huntDisplay.points = points;

 huntDisplay.paintComponent(graphicsMock);

 verify(graphicsMock).fillOval(argThat(new PointMatcher(trigPoint1.getX(), trigPoint1.getY())), eq(Color.GREEN));
 verify(graphicsMock).fillOval(argThat(new PointMatcher(trigPoint2.getX(), trigPoint2.getY())), eq(Color.BLACK));
 }

 @Test
 public void testPaintComponent_WithLandmarks() {
 HuntDisplay spyDisplay = spy(huntDisplay);
 Graphics graphicsMock = mock(Graphics.class);
 when(graphicsMock.getWidth()).thenReturn(200);
 when(graphicsMock.getHeight()).thenReturn(200);
 when(graphicsMock.getColor()).thenReturn(Color.BLACK);

 BufferedImage image1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 image1.setRGB(0, 0, 0xFF0000); // red color
 Landmark landmark1 = new Landmark(50.0, 100.0, image1);
 BufferedImage image2 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 image2.setRGB(0, 0, 0xFF00FF); // pink color
 Landmark landmark2 = new Landmark(60.0, 120.0, image2);
 Vector<Landmark> landmarks = new Vector<>();
 landmarks.add(landmark1);
 landmarks.add(landmark2);
 huntDisplay.landmarks = landmarks;

 huntDisplay.paintComponent(graphicsMock);

 verify(graphicsMock).drawImage(argThat(new ImageMatcher(image1)), argThat(new PointMatcher(landmark1.getLon(), landmark1.getLat())), eq(10), eq(10), eq(huntDisplay), any());
 verify(graphicsMock).drawImage(argThat(new ImageMatcher(image2)), argThat(new PointMatcher(landmark2.getLon(), landmark2.getLat())), eq(10), eq(10), eq(huntDisplay), any());
 }

 private static Matcher<Point> pointMatcher(double x, double y) {
 return new PointMatcher(x, y);
 }

 private static Matcher<BufferedImage> imageMatcher(BufferedImage image) {
 return new ImageMatcher(image);
 }

 private static class PointMatcher implements ArgumentMatcher<Point> {
 private final double x, y;

 PointMatcher(double x, double y) {
 this.x = x;
 this.y = y;
 }

 @Override
 public boolean matches(Point argument) {
 return argument.getX() == x && argument.getY() == y;
 }
 }

 private static class ImageMatcher implements ArgumentMatcher<BufferedImage> {
 private final BufferedImage image;

 ImageMatcher(BufferedImage image) {
 this.image = image;
 }

 @Override
 public boolean matches(BufferedImage argument) {
 return image.equals(argument);
 }
 }
}