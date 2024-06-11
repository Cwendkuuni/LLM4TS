package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.*;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class RobotRendererTest {

 private Graphics2D g2d;
 private RobotSnapshot robotSnapshot;
 private RobotRenderer renderer;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 g2d = mock(Graphics2D.class);
 robotSnapshot = new RobotSnapshot(1, new Vector2D(10.0, 10.0), 50.0, new Vector2D(Math.PI / 2.0), 25.0, false, 10.0, 5.0, 100.0, 1.0, false, "Robot1");
 renderer = new RobotRenderer();
 }

 @Test
 public void testRenderWithActiveShieldAndFillShieldEnabled() {
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 verify(g2d, times(1)).setPaint(new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white }));
 verify(g2d, times(2)).fill(argThat(s -> s instanceof Ellipse2D.Double));
 }

 @Test
 public void testRenderWithActiveShieldAndFillShieldDisabled() {
 renderer.setFillShield(false);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 verify(g2d, times(1)).setPaint(new Color(0.8f, 0.6f, 1.0f));
 verify(g2d, times(1)).draw(argThat(s -> s instanceof Ellipse2D.Double));
 }

 @Test
 public void testRenderWithoutActiveShield() {
 robotSnapshot.setActiveShield(false);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 verify(g2d, never()).setPaint(new RadialGradientPaint(anyDouble(), anyDouble(), anyFloatArray(), any(Color[].class)));
 verify(g2d, never()).fill(argThat(s -> s instanceof Ellipse2D.Double));
 }

 @Test
 public void testRenderWithSelectedRobot() {
 renderer.render(g2d, robotSnapshot, new HashSet<>(Collections.singleton(robotSnapshot.getId())));
 verify(g2d, times(1)).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
 verify(g2d, times(1)).fill(argThat(s -> s instanceof Ellipse2D.Double));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRenderWithNullGraphics2D() {
 renderer.render(null, robotSnapshot, Collections.emptySet());
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullRobotSnapshot() {
 renderer.render(g2d, null, Collections.emptySet());
 }

 @Test
 public void testIsRenderDeadWithDefaultValue() {
 assertEquals(true, renderer.isRenderDead());
 }

 @Test
 public void testIsRenderDeadWithDisabledFlag() {
 renderer.setRenderDead(false);
 assertEquals(false, renderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBarsWithDefaultValue() {
 assertEquals(true, renderer.isShowStatusBars());
 }

 @Test
 public void testIsShowStatusBarsWithDisabledFlag() {
 renderer.setShowStatusBars(false);
 assertEquals(false, renderer.isShowStatusBars());
 }

 @Test
 public void testIsShowNameWithDefaultValue() {
 assertEquals(true, renderer.isShowName());
 }

 @Test
 public void testIsShowNameWithDisabledFlag() {
 renderer.setShowName(false);
 assertEquals(false, renderer.isShowName());
 }

 @Test
 public void testIsFillShieldWithDefaultValue() {
 assertEquals(true, renderer.isFillShield());
 }

 @Test
 public void testIsFillShieldWithDisabledFlag() {
 renderer.setFillShield(false);
 assertEquals(false, renderer.isFillShield());
 }
}