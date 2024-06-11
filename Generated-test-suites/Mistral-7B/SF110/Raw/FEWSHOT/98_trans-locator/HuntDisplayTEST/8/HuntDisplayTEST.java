package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import javax.swing.*;
import org.junit.Test;

public class HuntDisplayTest {

 private HuntDisplay display;
 private Vector<TrigPoint> points;
 private Vector<Landmark> landmarks;

 @Test
 public void testHuntDisplayConstruction() {
 display = new HuntDisplay();
 assertEquals(new Dimension(200, 200), display.getPrefSize());
 assertEquals(new Dimension(50, 50), display.getMinSize());
 }

 @Test
 public void testAddPoint() {
 display = new HuntDisplay();
 points = new Vector<>();
 display.points = points;
 TrigPoint testPoint = new TrigPoint(1.0, 2.0, 1.0, false);
 display.addPoint(testPoint);
 assertEquals(1, points.size());
 verify(display, times(1)).repaint();
 }

 @Test
 public void testAddLandMark() {
 display = new HuntDisplay();
 landmarks = new Vector<>();
 display.landmarks = landmarks;
 Landmark testLandmark = new Landmark(1.0, 2.0, new ImageIcon(new BufferedImage(1, 1, BufferStrategy.TYPE_INT_ARGB)));
 display.addLandMark(testLandmark);
 assertEquals(1, landmarks.size());
 verify(display, times(1)).repaint();
 }

 @Test
 public void testPaintComponent() {
 display = new HuntDisplay();
 points = new Vector<>();
 landmarks = new Vector<>();
 display.points = points;
 display.landmarks = landmarks;
 when(display.getWidth()).thenReturn(500);
 when(display.getHeight()).thenReturn(500);

 TrigPoint point = new TrigPoint(1.0, 2.0, 1.0, false);
 points.add(point);
 Landmark landmark = new Landmark(2.0, 3.0, new ImageIcon(new BufferedImage(1, 1, BufferStrategy.TYPE_INT_ARGB)));
 landmarks.add(landmark);

 MockGraphics graphicsMock = mock(Graphics.class);
 when(graphicsMock.getColor()).thenAnswer(invocation -> Color.BLACK);
 doAnswer(invocation -> {}).when(graphicsMock).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
 doAnswer(invocation -> {}).when(graphicsMock).setColor(any(Color.class));
 doAnswer(invocation -> {
 final BufferedImage image = new BufferedImage(1, 1, BufferStrategy.TYPE_INT_ARGB);
 image.getGraphics().drawImage(any(Image.class), anyInt(), anyInt(), null);
 return image;
 }).when(graphicsMock).getImage();
 doAnswer(invocation -> {}).when(graphicsMock).drawImage(any(Image.class), anyInt(), anyInt(), anyInt(), anyInt(), any(Component.class), any(ImageObserver.class));

 display.paintComponent(graphicsMock);

 verify(graphicsMock, times(2)).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
 }
}