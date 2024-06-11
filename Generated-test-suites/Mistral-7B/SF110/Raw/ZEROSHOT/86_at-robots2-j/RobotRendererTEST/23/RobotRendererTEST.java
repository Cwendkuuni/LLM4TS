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

 private RobotRenderer robotRenderer;
 private Graphics2D graphics2dMock;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 robotRenderer = new RobotRenderer();
 graphics2dMock = mock(Graphics2D.class);
 robotSnapshot = new RobotSnapshot(1, 50.0, 50.0, new PositionVector(0, 0), 10.0, 20.0, 50.0, 50.0, 50.0, false, false, false, new RelativeAngle(0), new RelativeAngle(0), new RelativeAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0), false, 0.5f);
 }

 @Test
 public void testRenderWithDeadRobot() {
 robotRenderer.render(graphics2dMock, robotSnapshot, Collections.emptySet());
 verify(graphics2dMock, never()).fill(any(Shape.class));
 verify(graphics2dMock).setPaint(any(Color.class));
 verify(graphics2dMock, times(2)).draw(any(Shape.class));
 }

 @Test
 public void testRenderWithRobotWithoutDeadStatus() {
 robotSnapshot.setDead(false);
 reset(graphics2dMock);
 robotRenderer.render(graphics2dMock, robotSnapshot, Collections.emptySet());

 verify(graphics2dMock).fill(any(Shape.class));
 verify(graphics2dMock, times(6)).draw(any(Shape.class));
 }

 @Test
 public void testPaintBody() {
 final Shape expectedBodyShape = new GeneralPath();
 expectedBodyShape.moveTo(20.0, 20.0);
 expectedBodyShape.lineTo(25.0, 15.0);
 expectedBodyShape.lineTo(20.0, 10.0);
 expectedBodyShape.lineTo(15.0, 15.0);
 expectedBodyShape.closePath();

 when(graphics2dMock.getStroke()).thenReturn(new BasicStroke(1.0f, 1, 0));
 robotRenderer.paintBody(graphics2dMock, robotSnapshot);

 final Shape actualBodyShape = (Shape) capture(graphics2dMock).getPaint();
 assertEquals(expectedBodyShape, actualBodyShape);
 }

 @Test
 public void testPaintShield() {
 when(graphics2dMock.isDrawingGray()).thenReturn(false);
 robotRenderer.paintShield(graphics2dMock, robotSnapshot);

 final Color expectedFillColor = new Color(0.5f, 0.5f, 0.0f, 0.8f);
 final RadialGradientPaint expectedPaint = new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white });

 verify(graphics2dMock).setPaint(expectedPaint);
 verify(graphics2dMock).fill(any(Shape.class));
 verify(graphics2dMock).fill(any(Shape.class));
 verify(graphics2dMock, never()).setPaint(expectedFillColor);

 when(graphics2dMock.isDrawingGray()).thenReturn(true);
 robotRenderer.paintShield(graphics2dMock, robotSnapshot);

 final Color expectedBorderColor = new Color(0.8f, 0.6f, 1.0f);
 final BasicStroke expectedBorderStroke = new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f);

 verify(graphics2dMock).setPaint(expectedFillColor);
 verify(graphics2dMock, never()).fill(any(Shape.class));
 verify(graphics2dMock).setStroke(expectedBorderStroke);
 verify(graphics2dMock).setPaint(expectedBorderColor);
 verify(graphics2dMock).draw(any(Shape.class));
 }
}