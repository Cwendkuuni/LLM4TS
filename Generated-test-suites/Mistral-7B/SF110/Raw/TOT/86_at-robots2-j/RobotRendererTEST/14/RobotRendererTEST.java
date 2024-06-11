package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.util.*;

import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.*;
import org.mockito.*;

public class RobotRendererTest {

 private RobotRenderer renderer;
 private Graphics2D mockGraphics2D;
 private RobotSnapshot mockRobotSnapshot;

 @Before
 public void setUp() {
 mockGraphics2D = mock(Graphics2D.class);
 mockRobotSnapshot = mock(RobotSnapshot.class);
 renderer = new RobotRenderer();
 }

 @Test
 public void testRender_DeadRobot_DoNotRender() {
 when(mockRobotSnapshot.isDead()).thenReturn(true);
 renderer.render(mockGraphics2D, mockRobotSnapshot, Collections.emptySet());
 assertFalse(mockGraphics2D.isDrawing());
 }

 @Test
 public void testRender_LiveRobot_RenderBody() {
 when(mockRobotSnapshot.isDead()).thenReturn(false);
 renderer.render(mockGraphics2D, mockRobotSnapshot, Collections.emptySet());
 verify(mockGraphics2D, times(1)).fill(any(Path2D.class));
 }

 @Test
 public void testRender_LiveRobot_RenderShield() {
 when(mockRobotSnapshot.isDead()).thenReturn(false);
 when(mockRobotSnapshot.isActiveShield()).thenReturn(true);
 renderer.render(mockGraphics2D, mockRobotSnapshot, Collections.emptySet());
 verify(mockGraphics2D, times(1)).fill(any(Ellipse2D.class));
 }

 @Test
 public void testRender_LiveRobot_RenderName() {
 when(mockRobotSnapshot.isDead()).thenReturn(false);
 renderer.render(mockGraphics2D, mockRobotSnapshot, Collections.emptySet());
 verify(mockGraphics2D, times(1)).drawString(eq(mockRobotSnapshot.getName()), anyFloat(), anyFloat());
 }

 @Test
 public void testRender_LiveRobot_RenderHeat() {
 when(mockRobotSnapshot.isDead()).thenReturn(false);
 when(mockRobotSnapshot.getTemperature()).thenReturn(new Temperature(100));
 renderer.render(mockGraphics2D, mockRobotSnapshot, Collections.emptySet());
 verify(mockGraphics2D, times(1)).fill(any(Rectangle2D.class));
 }

 @Test
 public void testRender_LiveRobot_RenderArmor() {
 when(mockRobotSnapshot.isDead()).thenReturn(false);
 when(mockRobotSnapshot.getArmor()).thenReturn(50.0);
 renderer.render(mockGraphics2D, mockRobotSnapshot, Collections.emptySet());
 verify(mockGraphics2D, times(1)).fill(any(Rectangle2D.class));
 }

 @Test
 public void testRender_SelectedRobot_RenderSelection() {
 when(mockRobotSnapshot.isDead()).thenReturn(false);
 when(mockRobotSnapshot.getId()).thenReturn(1);
 renderer.render(mockGraphics2D, mockRobotSnapshot, Collections.singleton(1));
 verify(mockGraphics2D, times(1)).fill(any(Ellipse2D.class));
 }

 @Test
 public void testIsRenderDead_DefaultFalse() {
 RobotRenderer renderer = new RobotRenderer();
 assertFalse(renderer.isRenderDead());
 }

 @Test
 public void testIsRenderDead_SetToTrue() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setRenderDead(true);
 assertTrue(renderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars_DefaultTrue() {
 RobotRenderer renderer = new RobotRenderer();
 assertTrue(renderer.isShowStatusBars());
 }

 @Test
 public void testIsShowStatusBars_SetToFalse() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setShowStatusBars(false);
 assertFalse(renderer.isShowStatusBars());
 }

 @Test
 public void testIsShowName_DefaultTrue() {
 RobotRenderer renderer = new RobotRenderer();
 assertTrue(renderer.isShowName());
 }

 @Test
 public void testIsShowName_SetToFalse() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setShowName(false);
 assertFalse(renderer.isShowName());
 }

 @Test
 public void testIsFillShield_DefaultTrue() {
 RobotRenderer renderer = new RobotRenderer();
 assertTrue(renderer.isFillShield());
 }

 @Test
 public void testIsFillShield_SetToFalse() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setFillShield(false);
 assertFalse(renderer.isFillShield());
 }
}