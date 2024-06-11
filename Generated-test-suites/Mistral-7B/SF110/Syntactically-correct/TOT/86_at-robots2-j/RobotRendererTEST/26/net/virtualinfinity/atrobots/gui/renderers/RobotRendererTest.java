package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.renderers.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.*;
import org.mockito.*;

public class RobotRendererTest {

 private RobotRenderer robotRenderer;
 private Graphics2D graphics2DMock;
 private RobotSnapshot robotSnapshot;
 private Set<Integer> selectedRobotIds;

 @Before
 public void setUp() {
 robotRenderer = new RobotRenderer();
 graphics2DMock = mock(Graphics2D.class);
 robotSnapshot = new RobotSnapshot(1L, 50.0, 50.0, new Point2D.Double(10.0, 10.0), new Vector2D(1.0, 1.0), 50.0, 50.0, true, 10.0, 15.0, "Robot1", true, 0.0);
 selectedRobotIds = new HashSet<>();
 }

 @Test
 public void testRender_DeadRobot_ShouldNotRender() {
 robotSnapshot.setDead(true);
 robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);
 verify(graphics2DMock, never()).fill(any(Shape.class));
 }

 @Test
 public void testRender_ShouldRenderBody() {
 robotSnapshot.setDead(false);
 robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);
 verify(graphics2DMock, times(1)).fill(any(Shape.class));
 }

 @Test
 public void testPaintSelection_ShouldPaintSelection() {
 robotSnapshot.setId(1L);
 selectedRobotIds.add(1L);
 robotRenderer.paintSelection(graphics2DMock, robotSnapshot);
 verify(graphics2DMock, times(1)).setPaint(any(Color.class));
 verify(graphics2DMock, times(1)).fill(any(Shape.class));
 }

 @Test
 public void testPaintShield_ShouldPaintShield() {
 robotSnapshot.setActiveShield(true);
 robotRenderer.paintShield(graphics2DMock, robotSnapshot);
 verify(graphics2DMock, times(1)).setPaint(any(RadialGradientPaint.class));
 verify(graphics2DMock, times(1)).fill(any(Shape.class));
 }

 @Test
 public void testPaintName_ShouldPaintName() {
 robotRenderer.paintName(graphics2DMock, robotSnapshot);
 verify(graphics2DMock, times(1)).setPaint(any(Color.class));
 verify(graphics2DMock, times(1)).drawString(robotSnapshot.getName(), anyFloat(), anyFloat());
 }

 @Test
 public void testPaintHeat_ShouldPaintHeat() {
 robotSnapshot.setTemperature(new Temperature(50.0));
 robotRenderer.paintHeat(graphics2DMock, robotSnapshot);
 verify(graphics2DMock, times(1)).setPaint(any(GradientPaint.class));
 verify(graphics2DMock, times(1)).fill(any(Shape.class));
 }

 @Test
 public void testPaintArmor_ShouldPaintArmor() {
 robotSnapshot.setArmor(10.0);
 robotRenderer.paintArmor(graphics2DMock, robotSnapshot);
 verify(graphics2DMock, times(1)).setPaint(any(Color.class));
 verify(graphics2DMock, times(1)).fill(any(Shape.class));
 }

 @Test
 public void testPaintTurret_ShouldPaintTurret() {
 robotSnapshot.setTurretHeading(new Angle(Math.PI / 2.0));
 robotRenderer.paintTurret(graphics2DMock, robotSnapshot);
 verify(graphics2DMock, times(1)).setPaint(any(Color.class));
 verify(graphics2DMock, times(1)).draw(any(Shape.class));
 }

 @Test
 public void testPaintBody_ShouldPaintBody() {
 robotRenderer.paintBody(graphics2DMock, robotSnapshot);
 verify(graphics2DMock, times(1)).setPaint(any(Color.class));
 verify(graphics2DMock, times(1)).fill(any(Shape.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPaintBody_NullGraphics2D_ShouldThrowException() {
 robotRenderer.paintBody(null, robotSnapshot);
 }

 @Test(expected = NullPointerException.class)
 public void testPaintBody_NullRobotSnapshot_ShouldThrowException() {
 robotRenderer.paintBody(graphics2DMock, null);
 }

 @After
 public void tearDown() {
 reset(graphics2DMock);
 }
}