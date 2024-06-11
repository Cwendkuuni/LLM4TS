package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.*;
import org.mockito.*;

public class RobotRendererTest {

 private RobotSnapshot mockRobotSnapshot;
 private Graphics2D mockGraphics2D;
 private RobotRenderer robotRenderer;

 @Before
 public void setup() {
 mockRobotSnapshot = mock(RobotSnapshot.class);
 mockGraphics2D = mock(Graphics2D.class);
 robotRenderer = new RobotRenderer();
 }

 @Test
 public void testRender_DeadRobot_DoesNotRender() {
 when(mockRobotSnapshot.isDead()).thenReturn(true);
 robotRenderer.render(mockGraphics2D, mockRobotSnapshot, new HashSet<>());
 verify(mockGraphics2D, never()).drawString(anyString(), anyFloat(), anyFloat());
 verify(mockGraphics2D, never()).fill(any(Shape.class));
 verify(mockGraphics2D, never()).draw(any(Shape.class));
 }

 @Test
 public void testRender_RobotWithStatusBars_RendersBodyShieldTurretAndStatusBars() {
 when(mockRobotSnapshot.isDead()).thenReturn(false);
 when(mockRobotSnapshot.getName()).thenReturn("TestRobot");
 when(mockRobotSnapshot.getX()).thenReturn(100.0);
 when(mockRobotSnapshot.getY()).thenReturn(200.0);
 when(mockRobotSnapshot.getHeading()).thenReturn(new AbsoluteAngle(0.0));
 when(mockRobotSnapshot.getTemperature()).thenReturn(new Temperature(50.0));
 when(mockRobotSnapshot.getArmor()).thenReturn(20.0);
 when(mockRobotSnapshot.getTurretHeading()).thenReturn(new AbsoluteAngle(180.0));

 robotRenderer.render(mockGraphics2D, mockRobotSnapshot, new HashSet<>());

 verify(mockGraphics2D).drawString("TestRobot", closeTo((float)100.0, 0.1), closeTo(199.0, 0.1));
 verify(mockGraphics2D, times(2)).fill(any(Shape.class));
 verify(mockGraphics2D, times(2)).draw(any(Shape.class));
 }

 @Test
 public void testRender_RobotWithSelection_RendersSelection() {
 when(mockRobotSnapshot.isDead()).thenReturn(false);
 when(mockRobotSnapshot.getId()).thenReturn(1);
 when(mockRobotSnapshot.getX()).thenReturn(100.0);
 when(mockRobotSnapshot.getY()).thenReturn(200.0);

 robotRenderer.render(mockGraphics2D, mockRobotSnapshot, new HashSet<>(Collections.singleton(1)));

 verify(mockGraphics2D).setPaint(argThat(color -> color.getRed() == 1.0 && color.getGreen() == 1.0 && color.getBlue() == 0.0 && color.getAlpha() == 0.25));
 verify(mockGraphics2D).fill(argThat(ellipse -> ellipse.getCenterX() == 100.0 && ellipse.getCenterY() == 200.0 && ellipse.getWidth() > 0 && ellipse.getHeight() > 0));
 }

 @Test
 public void testIsRenderDead_DefaultValueIsTrue() {
 robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isRenderDead());
 }

 @Test
 public void testIsRenderDead_CanBeSetToFalse() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setRenderDead(false);
 assertFalse(robotRenderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars_DefaultValueIsTrue() {
 robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isShowStatusBars());
 }

 @Test
 public void testIsShowStatusBars_CanBeSetToFalse() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setShowStatusBars(false);
 assertFalse(robotRenderer.isShowStatusBars());
 }

 @Test
 public void testIsShowName_DefaultValueIsTrue() {
 robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isShowName());
 }

 @Test
 public void testIsShowName_CanBeSetToFalse() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setShowName(false);
 assertFalse(robotRenderer.isShowName());
 }

 @Test
 public void testIsFillShield_DefaultValueIsTrue() {
 robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isFillShield());
 }

 @Test
 public void testIsFillShield_CanBeSetToFalse() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setFillShield(false);
 assertFalse(robotRenderer.isFillShield());
 }
}