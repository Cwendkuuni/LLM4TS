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

 private RobotSnapshot robotSnapshot;
 private Graphics2D graphics2dMock;

 @Before
 public void setup() {
 robotSnapshot = new RobotSnapshot();
 robotSnapshot.setX(10.0);
 robotSnapshot.setY(10.0);
 robotSnapshot.setHeading(new RelativeAngle(Math.PI / 4));
 robotSnapshot.setTurretHeading(new RelativeAngle(Math.PI / 4));
 robotSnapshot.setArmor(50.0);
 robotSnapshot.setTemperature(new Temperature(50.0));
 robotSnapshot.setId(1);
 robotSnapshot.setName("TestBot");
 robotSnapshot.setDead(false);
 robotSnapshot.setActiveShield(true);
 robotSnapshot.setPositionVector(new PositionVector(10.0, 10.0));

 graphics2dMock = mock(Graphics2D.class);
 }

 @Test
 public void testRenderWithDeadRobot() {
 final SnapshotRenderer<RobotSnapshot> robotRenderer = new RobotRenderer();
 robotRenderer.render(graphics2dMock, robotSnapshot, Collections.singletonList(robotSnapshot.getId()));

 verify(graphics2dMock, never()).drawString(anyString(), anyFloat(), anyFloat());
 verify(graphics2dMock, never()).fill(any(Shape.class));
 verify(graphics2dMock, times(1)).setStroke(any(Stroke.class));
 verify(graphics2dMock, times(1)).setPaint(any(Color.class));
 }

 @Test
 public void testRenderWithAliveRobot() {
 final SnapshotRenderer<RobotSnapshot> robotRenderer = new RobotRenderer();
 robotRenderer.render(graphics2dMock, robotSnapshot, Collections.emptySet());

 verify(graphics2dMock, times(1)).setPaint(eq(Color.red));
 verify(graphics2dMock, times(1)).fill(any(Shape.class));
 verify(graphics2dMock, times(1)).drawString(eq("TestBot"), anyFloat(), anyFloat());
 verify(graphics2dMock, times(1)).setPaint(eq(Color.orange.darker().darker()));
 verify(graphics2dMock, times(1)).setStroke(any(BasicStroke.class));

 ArgumentCaptor<Shape> shapeCaptor = ArgumentCaptor.forClass(Shape.class);
 verify(graphics2dMock, times(1)).fill(shapeCaptor.capture());

 Path path = (Path) shapeCaptor.getValue();
 Point2D expectedPosition = new Point2D.Double(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 assertEquals(expectedPosition, path.getPathPoints()[0]);
 assertEquals(15.0, path.getPathPoints()[1].getX() - expectedPosition.getX(), 0.01);
 assertEquals(15.0, path.getPathPoints()[1].getY() - expectedPosition.getY(), 0.01);

 verify(graphics2dMock, times(1)).setPaint(eq(Color.white));
 verify(graphics2dMock, times(1)).draw(any(Line2D.class));
 }

 @Test
 public void testRenderWithSelectedRobot() {
 final SnapshotRenderer<RobotSnapshot> robotRenderer = new RobotRenderer();
 robotRenderer.render(graphics2dMock, robotSnapshot, Collections.singletonList(robotSnapshot.getId()));

 verify(graphics2dMock, times(1)).setPaint(eq(new Color(1.0f, 1.0f, 0.0f, 0.25f)));
 }

 @Test
 public void testRenderWithDeadAndActiveShieldRobot() {
 final SnapshotRenderer<RobotSnapshot> robotRenderer = new RobotRenderer();
 robotSnapshot.setDead(true);
 robotSnapshot.setActiveShield(true);
 robotRenderer.render(graphics2dMock, robotSnapshot, Collections.emptySet());

 verify(graphics2dMock, never()).drawString(anyString(), anyFloat(), anyFloat());
 verify(graphics2dMock, times(1)).setPaint(any(Color.class));
 verify(graphics2dMock, times(1)).fill(any(Shape.class));
 }

 @Test
 public void testRenderWithDeadAndInactiveShieldRobot() {
 final SnapshotRenderer<RobotRenderer> robotRenderer = new RobotRenderer();
 robotSnapshot.setDead(true);
 robotSnapshot.setActiveShield(false);
 robotRenderer.render(graphics2dMock, robotSnapshot, Collections.emptySet());

 verify(graphics2dMock, never()).drawString(anyString(), anyFloat(), anyFloat());
 verify(graphics2dMock, times(1)).setPaint(any(Color.class));
 verify(graphics2dMock, times(1)).fill(any(Shape.class));
 }

 @Test
 public void testPaintSelection() {
 final Graphics2D graphics2d = mock(Graphics2D.class);
 final SnapshotRenderer<RobotSnapshot> robotRenderer = new RobotRenderer();
 robotRenderer.paintSelection(graphics2d, robotSnapshot);

 verify(graphics2d, times(1)).setPaint(eq(new Color(1.0f, 1.0f, 0.0f, 0.25f)));
 final Ellipse2D.Double s = new Ellipse2D.Double();
 verify(graphics2d, times(1)).fill(eq(s));

 final Point2D center = new Point2D.Double(robotSnapshot.getX(), robotSnapshot.getY());
 s.setFrameFromCenter(center.getX(), center.getY(), 30.0, 30.0);
 assertEquals(center.getX() + 15.0, s.getX(), 0.01);
 assertEquals(center.getY() + 15.0, s.getY(), 0.01);
 }
}