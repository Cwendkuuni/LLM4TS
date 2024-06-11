package Joshua.FoxHunt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import java.awt. Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class HuntDisplayTest {

 HuntDisplay huntDisplay;
 Vector<TrigPoint> points;
 Vector<Landmark> landmarks;

 @Test
 public void testHuntDisplayNewInstance() {
 HuntDisplay hd = new HuntDisplay();
 assertEquals(new Dimension(200, 200), hd.getPrefSize());
 assertEquals(new Dimension(50, 50), hd.getMinSize());
 }

 @Test
 public void testAddPoint() {
 huntDisplay = new HuntDisplay();
 points = new Vector<>();
 huntDisplay.points = points;

 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, true);
 huntDisplay.addPoint(point);

 assertEquals(1, huntDisplay.points.size());
 assertNotSame(point, huntDisplay.points.get(0));
 }

 @Test
 public void testAddLandMark() {
 huntDisplay = new HuntDisplay();
 landmarks = new Vector<>();
 huntDisplay.landmarks = landmarks;

 Landmark landmark = new Landmark(1.0, 2.0, Mockito.mock(BufferedImage.class));
 huntDisplay.addLandMark(landmark);

 assertEquals(1, huntDisplay.landmarks.size());
 assertNotSame(landmark, huntDisplay.landmarks.get(0));
 }

 @Test
 public void testPaintComponent_NoLandmarks() {
 huntDisplay = PowerMockito.spy(new HuntDisplay());
 points = new Vector<>();
 huntDisplay.points = points;
 Graphics g = Mockito.mock(Graphics.class);
 PowerMockito.doNothing().when(huntDisplay).repaint();

 huntDisplay.paintComponent(g);

 PowerMockito.verifyNoMoreInteractions(huntDisplay);
 }

 @Test
 public void testPaintComponent_WithLandmarks() {
 huntDisplay = PowerMockito.spy(new HuntDisplay());
 points = new Vector<>();
 landmarks = new Vector<>();
 huntDisplay.points = points;
 huntDisplay.landmarks = landmarks;

 Landmark landmark = new Landmark(1.0, 2.0, Mockito.mock(BufferedImage.class));
 landmarks.add(landmark);

 TrigPoint[] trigpoints = new TrigPoint[1];
 TrigPoint point = new TrigPoint(1.1, 2.1, 0.0, false);
 trigpoints[0] = point;

 Graphics g = Mockito.mock(Graphics.class);
 PowerMockito.doNothing().when(huntDisplay).repaint();

 huntDisplay.paintComponent(g);

 PowerMockito.verify(huntDisplay, times(2)).getWidth();
 PowerMockito.verify(huntDisplay, times(2)).getHeight();
 PowerMockito.verify(huntDisplay).repaint();

 for (TrigPoint trigpoint : trigpoints) {
 PowerMockito.verify(g).fillOval(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Color.BLACK));
 PowerMockito.verifyZeroInteractions(g).setColor(Color.GREEN);
 }

 PowerMockito.verify(g, times(landmarks.size())).drawImage(Mockito.any(BufferedImage.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Color.WHITE), Mockito.eq(huntDisplay));
 }

}