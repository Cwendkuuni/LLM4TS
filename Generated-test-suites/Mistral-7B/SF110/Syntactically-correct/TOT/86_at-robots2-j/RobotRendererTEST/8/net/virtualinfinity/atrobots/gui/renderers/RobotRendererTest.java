package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RobotRendererTest {

 private RobotRenderer renderer;
 private Graphics2D mockGraphics2D;
 private RobotSnapshot mockRobotSnapshot;

 @Before
 public void setup() {
 renderer = new RobotRenderer();
 mockGraphics2D = mock(Graphics2D.class);
 mockRobotSnapshot = new RobotSnapshot(1, new PositionVector(10.0, 10.0), false, 100.0, 50.0, 50.0, new TurretHeading(0.0), new AbsoluteAngle(0.0), new AbsoluteAngle(0.0), new AbsoluteAngle(0.0), "TestBot", true, false, false, true, false, false, true, true, false, false, false, new SnapshotStats());
 }

 @Test
 public void testRender_RenderBot_ShouldCallPaintMethodsInOrder() {
 when(mockGraphics2D.getColor()).thenReturn(renderer.getForeground());
 renderer.render(mockGraphics2D, mockRobotSnapshot, new HashSet<>());

 ArgumentCaptor<RobotSnapshot> snapshotArgumentCaptor = ArgumentCaptor.forClass(RobotSnapshot.class);
 verify(mockGraphics2D, times(1)).setRenderHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 verify(mockGraphics2D, times(1)).translate(mockRobotSnapshot.getX(), mockRobotSnapshot.getY());
 verify(mockGraphics2d, times(1)).setPaint(renderer.getBackground());
 verify(mockGraphics2D, times(1)).fill(any(Shape.class)); // paintBody
 verify(mockGraphics2D, times(1)).setPaint(Color.yellow);
 verify(mockGraphics2D, times(1)).drawString(mockRobotSnapshot.getName(), (float)(mockRobotSnapshot.getX() - mockGraphics2D.getFontMetrics().getStringBounds(mockRobotSnapshot.getName(), mockGraphics2D).getWidth() / 2.0), (float)(mockRobotSnapshot.getY() - 10.0)); // paintName
 verify(mockGraphics2D, times(1)).setPaint(new GradientPaint((float)mockRobotSnapshot.getX(), (float)mockRobotSnapshot.getY(), RobotRenderer.HEAT_COLOR1, (float)mockRobotSnapshot.getX(), (float)mockRobotSnapshot.getY(), RobotRenderer.HEAT_COLOR2));
 verify(mockGraphics2D, times(1)).fill(new Rectangle2D.Double(mockRobotSnapshot.getX() - 50.0, mockRobotSnapshot.getY() + 35.0, mockRobotSnapshot.getTemperature().getLogScale() * 0.2, 10.0)); // paintHeat
 verify(mockGraphics2D, times(1)).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
 verify(mockGraphics2D, times(1)).fill(new Rectangle2D.Double(mockRobotSnapshot.getX() - 50.0, mockRobotSnapshot.getY() + 20.0, mockRobotSnapshot.getArmor(), 10.0)); // paintArmor
 verify(mockGraphics2D, times(1)).setPaint(new Color(0.0f, 1.0f, 0.0f, 0.6f));
 verify(mockGraphics2D, times(1)).draw(new Rectangle2D.Double(mockRobotSnapshot.getX() - 50.0, mockRobotSnapshot.getY() + 20.0, 100.0, 10.0)); // paintSelection

 // paintShield and paintTurret are not called as robotSnapshot is not dead
 verify(mockGraphics2D, never()).setPaint(new RadialGradientPaint(anyDouble(), anyDouble(), anyFloatArray(), anyColorArray()));
 verify(mockGraphics2D, never()).fill(any(Ellipse2D.class));
 }

 @Test
 public void testRender_RenderDeadBot_ShouldNotCallRenderMethods() {
 when(mockRobotSnapshot.isDead()).thenReturn(true);
 renderer.render(mockGraphics2D, mockRobotSnapshot, new HashSet<>());

 // Body, shield, turret, armor, name, selection should not be called
 verify(mockGraphics2D, never()).setPaint(renderer.getBackground());
 verify(mockGraphics2D, never()).fill(any(Shape.class));
 verify(mockGraphics2D, never()).setPaint(Color.yellow);
 verify(mockGraphics2D, never()).drawString(mockRobotSnapshot.getName(), (float)(mockRobotSnapshot.getX() - mockGraphics2D.getFontMetrics().getStringBounds(mockRobotSnapshot.getName(), mockGraphics2D).getWidth() / 2.0), (float)(mockRobotSnapshot.getY() - 10.0));
 verify(mockGraphics2D, never()).setPaint(new GradientPaint((float)mockRobotSnapshot.getX(), (float)mockRobotSnapshot.getY(), RobotRenderer.HEAT_COLOR1, (float)mockRobotSnapshot.getX(), (float)mockRobotSnapshot.getY(), RobotRenderer.HEAT_COLOR2));
 verify(mockGraphics2D, never()).fill(new Rectangle2D.Double(mockRobotSnapshot.getX() - 50.0, mockRobotSnapshot.getY() + 35.0, mockRobotSnapshot.getTemperature().getLogScale() * 0.2, 10.0));
 verify(mockGraphics2D, never()).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
 verify(mockGraphics2D, never()).fill(new Rectangle2D.Double(mockRobotSnapshot.getX() - 50.0, mockRobotSnapshot.getY() + 20.0, mockRobotSnapshot.getArmor(), 10.0));
 verify(mockGraphics2D, never()).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
 verify(mockGraphics2D, never()).fill(any(Ellipse2D.class));
 }

 @Test
 public void testSetShowStatusBars() {
 renderer = new RobotRenderer();
 renderer.setShowStatusBars(false);
 assertFalse(renderer.isShowStatusBars());
 }

 @Test
 public void testSetShowName() {
 renderer = new RobotRenderer();
 renderer.setShowName(false);
 assertFalse(renderer.isShowName());
 }

 @Test
 public void testSetFillShield() {
 renderer = new RobotRenderer();
 renderer.setFillShield(false);
 assertFalse(renderer.isFillShield());
 }
}