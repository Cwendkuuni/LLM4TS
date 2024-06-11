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
 private Graphics2D graphics2dMock;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 this.renderer = new RobotRenderer();
 this.graphics2dMock = mock(Graphics2D.class);
 this.robotSnapshot = new RobotSnapshot(1L, 10.0, 10.0, 10.0, new Vector3D(0, 0, 0), 30.0, 50.0, 100.0, new Vector3D(0, 0, 1.0), 5.0, new Vector3D(0, 0, 0), false, false, false, false, false, false, false, false, false, false, false, false, false, new AbsoluteAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0));
 }

 @Test
 public void testRenderWithActiveShieldAndFillShieldTrue() {
 // Given
 when(this.graphics2dMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));

 // When
 this.renderer.render(this.graphics2dMock, this.robotSnapshot, new HashSet<Integer>());

 // Then
 verify(this.graphics2dMock, times(1)).setPaint(new RadialGradientPaint(this.robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white }));
 verify(this.graphics2dMock, times(2)).fill(argThat(r -> r instanceof Ellipse2D.Double));
 }

 @Test
 public void testRenderWithActiveShieldAndFillShieldFalse() {
 // Given
 when(this.graphics2dMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));

 // When
 this.renderer.setFillShield(false);
 this.renderer.render(this.graphics2dMock, this.robotSnapshot, new HashSet<Integer>());

 // Then
 verify(this.graphics2dMock, never()).setPaint(new RadialGradientPaint(this.robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white }));
 verify(this.graphics2dMock, times(1)).setPaint(new Color(0.8f, 0.6f, 1.0f));
 }

 @Test
 public void testRenderWithDeadRobot() {
 // Given
 this.robotSnapshot.setDead(true);

 // When
 this.renderer.render(this.graphics2dMock, this.robotSnapshot, new HashSet<Integer>());

 // Then
 verify(this.graphics2dMock, never()).paint(new Ellipse2D.Double());
 }

 @Test
 public void testRenderWithRobotNameVisibilityTrue() {
 // Given
 when(this.graphics2dMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));

 // When
 this.renderer.setShowName(true);
 this.renderer.render(this.graphics2dMock, this.robotSnapshot, new HashSet<Integer>());

 // Then
 verify(this.graphics2dMock, times(1)).setPaint(Color.yellow);
 }

 @Test
 public void testRenderWithRobotNameVisibilityFalse() {
 // Given
 when(this.graphics2dMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));

 // When
 this.renderer.setShowName(false);
 this.renderer.render(this.graphics2dMock, this.robotSnapshot, new HashSet<Integer>());

 // Then
 verify(this.graphics2dMock, never()).setPaint(Color.yellow);
 }

 @Test
 public void testRenderWithRobotSelection() {
 // Given
 this.graphics2dMock.setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
 final Set<Integer> selectedRobotIds = new HashSet<Integer>();
 selectedRobotIds.add(this.robotSnapshot.getId());

 // When
 this.renderer.render(this.graphics2dMock, this.robotSnapshot, selectedRobotIds);

 // Then
 verify(this.graphics2dMock, times(1)).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
 final Ellipse2D.Double s = new Ellipse2D.Double();
 s.setFrameFromCenter(this.robotSnapshot.getX(), this.robotSnapshot.getY(), this.robotSnapshot.getX() + 30.0, this.robotSnapshot.getY() + 30.0);
 verify(this.graphics2dMock, times(1)).fill(s);
 }
}