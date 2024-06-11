package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HuntDisplayTest {

 private HuntDisplay huntDisplay;
 private Vector<TrigPoint> points;
 private Vector<Landmark> landmarks;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 points = new Vector<>();
 landmarks = new Vector<>();
 }

 @Test
 public void testConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertEquals(50, hd.getMinimumSize().width);
 assertEquals(50, hd.getMinimumSize().height);
 assertEquals(200, hd.getPreferredSize().width);
 assertEquals(200, hd.getPreferredSize().height);
 }

 @Test
 public void testAddPoint() {
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, true);
 huntDisplay.addPoint(trigPoint);
 assertEquals(1, huntDisplay.getPoints().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullPoint() {
 huntDisplay.addPoint(null);
 }

 @Test
 public void testPaintComponentWithNoPoints() {
 Graphics graphicsMock = mock(Graphics.class);
 huntDisplay.paintComponent(graphicsMock);
 verify(graphicsMock, never()).fillOval(anyInt(), anyInt(), anyInt(), anyInt(), any(Color.class));
 verify(graphicsMock, never()).drawImage(any(Image.class), anyInt(), anyInt(), anyInt(), anyInt(), any(Component.class), any(Graphics2D.class));
 }

 @Test
 public void testPaintComponentWithPoints() {
 TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, true);
 TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
 points.add(trigPoint1);
 points.add(trigPoint2);
 huntDisplay.points = points;

 Graphics graphicsMock = mock(Graphics.class);
 huntDisplay.paintComponent(graphicsMock);

 verify(graphicsMock, times(2)).fillOval(anyInt(), anyInt(), anyInt(), anyInt(), eq(Color.GREEN));
 verify(graphicsMock, times(2)).fillOval(anyInt(), anyInt(), anyInt(), anyInt(), eq(Color.BLACK));
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(1.0, 2.0, Mockito.mock(BufferedImage.class));
 huntDisplay.addLandmark(landmark);
 assertEquals(1, huntDisplay.getLandmarks().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullLandmark() {
 huntDisplay.addLandmark(null);
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 Landmark landmark = new Landmark(1.0, 2.0, Mockito.mock(BufferedImage.class));
 landmarks.add(landmark);
 huntDisplay.landmarks = landmarks;

 Graphics graphicsMock = mock(Graphics.class);
 huntDisplay.paintComponent(graphicsMock);

 verify(graphicsMock, times(1)).drawImage(any(Image.class), anyInt(), anyInt(), anyInt(), anyInt(), any(Component.class), any(Graphics2D.class));
 }

 @Test
 public void testCalculateCentroid() {
 TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
 points.add(trigPoint1);
 points.add(trigPoint2);

 double expectedX = (1.0 + 3.0) / 2.0;
 double expectedY = (2.0 + 4.0) / 2.0;

 huntDisplay.calculateCentroid();
 assertEquals(expectedX, huntDisplay.getXAdd(), 0.01);
 assertEquals(expectedY, huntDisplay.getYAdd(), 0.01);
 }
}