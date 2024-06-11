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

 private RobotRenderer robotRenderer;
 private Graphics2D graphics2DMock;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 robotRenderer = new RobotRenderer();
 graphics2DMock = mock(Graphics2D.class);
 robotSnapshot = new RobotSnapshot(1, new PositionVector(10, 10), 10.0, 50.0, 100.0, "TestRobot", new Angle(0), new Angle(0), true, true, false, 50.0f, 10.0f, false);
 }

 @Test
 public void testRenderWithDeadRobot() {
 robotRenderer.render(graphics2DMock, robotSnapshot, Collections.emptySet());
 verify(graphics2DMock, never()).drawString(anyString(), anyFloat(), anyFloat());
 verify(graphics2DMock, never()).fill(any(Shape.class));
 }

 @Test
 public void testRenderBody() {
 when(graphics2DMock.getStroke()).thenReturn(new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f));
 robotRenderer.render(graphics2DMock, robotSnapshot, Collections.emptySet());
 Path expectedPath = new GeneralPath();
 expectedPath.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 expectedPath.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedPath.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 expectedPath.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedPath.closePath();
 verify(graphics2DMock).draw(eq(expectedPath));
 }

 @Test
 public void testRenderWhenRobotIsNotDead() {
 when(graphics2DMock.getStroke()).thenReturn(new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f));
 robotRenderer.setRenderDead(false);
 robotRenderer.render(graphics2DMock, robotSnapshot, Collections.emptySet());
 Path expectedPath = new GeneralPath();
 expectedPath.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 expectedPath.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedPath.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 expectedPath.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedPath.closePath();
 verify(graphics2DMock).fill(eq(expectedPath));

 verify(graphics2DMock).setPaint(eq(Color.red));
 verify(graphics2DMock).drawString(robotSnapshot.getName(), (float)(robotSnapshot.getX() - graphics2DMock.getFontMetrics().getStringBounds(robotSnapshot.getName(), graphics2DMock).getWidth() / 2.0), (float)(robotSnapshot.getY() - 10.0));
 }

 @Test
 public void testPaintSelection() {
 when(graphics2DMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));
 robotRenderer.render(graphics2DMock, robotSnapshot, Collections.singleton(1));
 Ellipse2D expectedEllipse = new Ellipse2D.Double();
 expectedEllipse.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);
 verify(graphics2DMock).setPaint(eq(new Color(1.0f, 1.0f, 0.0f, 0.25f)));
 verify(graphics2DMock).fill(eq(expectedEllipse));
 }
}