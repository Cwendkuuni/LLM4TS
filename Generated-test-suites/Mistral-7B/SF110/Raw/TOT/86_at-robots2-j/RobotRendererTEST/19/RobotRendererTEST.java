package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.Before;
import org.junit.Test;

public class RobotRendererTest {

 private RobotRenderer robotRenderer;
 private Graphics2D graphics2DMock;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setup() {
 robotRenderer = new RobotRenderer();
 graphics2DMock = mock(Graphics2D.class);
 robotSnapshot = new RobotSnapshot(1, new PositionVector(10.0, 10.0), false, new VelocityVector(0.0, 0.0), 50.0, 100.0, new Armor(100.0), new Temperature(50.0), "Robot1", new Heading(0.0), new AngularVelocity(0.0), new TurretHeading(0.0), false, false, new PositionVector(11.0, 11.0), false);
 }

 @Test
 public void testRender_ActiveRobot_ShouldRenderBodyShieldAndName() {
 robotRenderer.render(graphics2DMock, robotSnapshot, new HashSet<Integer>());

 verify(graphics2DMock, times(1)).fill(any(Shape.class));
 verify(graphics2DMock, times(1)).draw(any(Shape.class));
 verify(graphics2DMock, times(1)).setPaint(eq(Color.yellow));
 verify(graphics2DMock, times(1)).drawString(eq("Robot1"), anyFloat(), anyFloat());
 }

 @Test
 public void testRender_DeadRobot_ShouldNotRenderBodyOrName() {
 robotSnapshot.setDead(true);
 robotRenderer.render(graphics2DMock, robotSnapshot, new HashSet<Integer>());

 verify(graphics2DMock, never()).fill(any(Shape.class));
 verify(graphics2DMock, never()).drawString(anyString(), anyFloat(), anyFloat());
 }

 @Test
 public void testRender_SelectedRobot_ShouldRenderSelection() {
 robotRenderer.render(graphics2DMock, robotSnapshot, new HashSet<Integer>(Arrays.asList(robotSnapshot.getId())));

 verify(graphics2DMock, times(1)).setPaint(eq(new Color(1.0f, 1.0f, 0.0f, 0.25f)));
 verify(graphics2DMock, times(1)).fill(any(Ellipse2D.Double.class));
 }

 @Test
 public void testRender_InactiveShield_ShouldNotRenderShield() {
 robotSnapshot.setActiveShield(false);
 robotRenderer.render(graphics2DMock, robotSnapshot, new HashSet<Integer>());

 verify(graphics2DMock, never()).setPaint(any(RadialGradientPaint.class));
 verify(graphics2DMock, never()).fill(any(Ellipse2D.Double.class));
 }

 @Test
 public void testRender_ActiveShield_ShouldRenderFilledShield() {
 robotSnapshot.setActiveShield(true);
 robotRenderer.setFillShield(true);
 robotRenderer.render(graphics2DMock, robotSnapshot, new HashSet<Integer>());

 verify(graphics2DMock, times(1)).setPaint(any(RadialGradientPaint.class));
 verify(graphics2DMock, times(2)).fill(any(Ellipse2D.Double.class));
 }

 @Test
 public void testRender_ActiveShield_ShouldRenderOutlinedShield() {
 robotSnapshot.setActiveShield(true);
 robotRenderer.setFillShield(false);
 robotRenderer.render(graphics2DMock, robotSnapshot, new HashSet<Integer>());

 verify(graphics2DMock, times(1)).setPaint(eq(new Color(0.8f, 0.6f, 1.0f)));
 verify(graphics2DMock, times(1)).draw(any(Ellipse2D.Double.class));
 }

 @Test
 public void testRender_DeadAndSelectedRobot_ShouldNotRenderRobot() {
 robotSnapshot.setDead(true);
 robotRenderer.render(graphics2DMock, robotSnapshot, new HashSet<Integer>(Arrays.asList(robotSnapshot.getId())));

 verify(graphics2DMock, never()).fill(any(Shape.class));
 verify(graphics2DMock, never()).draw(any(Shape.class));
 }
}