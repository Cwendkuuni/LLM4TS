package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import org.junit.Test;

public class HuntDisplayTest {

 final int WIDTH = 200;
 final int HEIGHT = 200;

 @Test
 public void testHuntDisplayConstruction() {
 HuntDisplay huntDisplay = new HuntDisplay();
 assertNotNull(huntDisplay);
 assertEquals(Color.WHITE, huntDisplay.getBackground());
 assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
 assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
 }

 @Test
 public void testAddPoint() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = mock(TrigPoint.class);
 huntDisplay.addPoint(trigPoint);
 assertEquals(1, huntDisplay.points.size());
 verify(trigPoint, times(1)).getY();
 verify(trigPoint, times(1)).getX();
 verify(trigPoint, times(1)).getTheta();
 verify(trigPoint, times(1)).isFox();
 huntDisplay.repaint();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullPoint() {
 HuntDisplay huntDisplay = new HuntDisplay();
 huntDisplay.addPoint(null);
 }

 @Test
 public void testAddLandMark() {
 HuntDisplay huntDisplay = new HuntDisplay();
 Landmark landmark = mock(Landmark.class);
 huntDisplay.addLandMark(landmark);
 assertEquals(1, huntDisplay.landmarks.size());
 verify(landmark, times(1)).getLat();
 verify(landmark, times(1)).getLon();
 verify(landmark, times(1)).getIcon();
 huntDisplay.repaint();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullLandMark() {
 HuntDisplay huntDisplay = new HuntDisplay();
 huntDisplay.addLandMark(null);
 }

 @Test
 public void testPaintComponent() {
 HuntDisplay huntDisplay = new HuntDisplay();
 TrigPoint trigPoint = mock(TrigPoint.class);
 Landmark landmark = mock(Landmark.class);
 when(trigPoint.isFox()).thenReturn(true);
 when(landmark.getIcon()).thenReturn(new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
 huntDisplay.points.add(trigPoint);
 huntDisplay.landmarks.add(landmark);
 huntDisplay.setSize(new Dimension(WIDTH, HEIGHT));
 Graphics2D g = mock(Graphics2D.class);
 huntDisplay.paintComponent(g);

 verify(g, times(1)).setColor(any(Color.class));
 verify(g, times(trigPoints.length)).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g, times(landMarks.length)).drawImage(any(Image.class), anyInt(), anyInt(), anyInt(), anyInt(), any(Component.class), any(Graphics2D.class));
 }

 @Test
 public void testGetPreferredSize() {
 HuntDisplay huntDisplay = new HuntDisplay();
 assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 HuntDisplay huntDisplay = new HuntDisplay();
 assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
 }
}