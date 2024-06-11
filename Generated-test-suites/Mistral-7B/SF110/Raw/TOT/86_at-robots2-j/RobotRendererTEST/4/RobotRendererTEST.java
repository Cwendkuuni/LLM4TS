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
 private Graphics2D graphics2DMock;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 this.robotRenderer = new RobotRenderer();
 this.graphics2DMock = mock(Graphics2D.class);
 this.robotSnapshot = new RobotSnapshot(1L, 10.0, 10.0, new Vector3D(0.0, 0.0, 0.0), 1.0, 1.0, new AbsoluteAngle(0.0, AngleUnit.RADIAN), new AbsoluteAngle(0.0, AngleUnit.RADIAN), 1.0, Color.RED, true, false, false, false, null, null, null, null);
 }

 @Test
 public void testRenderWithValidSnapshot() {
 // given
 when(graphics2DMock.getColor()).thenReturn(Color.RED);
 when(graphics2DMock.setRenderDead(anyBoolean())).thenAnswer(invocation -> {
 this.robotRenderer.setRenderDead(invocation.getArgument(0));
 return null;
 });
 when(graphics2DMock.setShowStatusBars(anyBoolean())).thenAnswer(invocation -> {
 this.robotRenderer.setShowStatusBars(invocation.getArgument(0));
 return null;
 });
 when(graphics2DMock.setShowName(anyBoolean())).thenAnswer(invocation -> {
 this.robotRenderer.setShowName(invocation.getArgument(0));
 return null;
 });
 when(graphics2DMock.setFillShield(anyBoolean())).thenAnswer(invocation -> {
 this.robotRenderer.setFillShield(invocation.getArgument(0));
 return null;
 });

 // when
 this.robotRenderer.render(this.graphics2DMock, this.robotSnapshot, new HashSet<>());

 // then
 verify(graphics2DMock, times(1)).draw(any(Graphics2D.class));
 verify(graphics2DMock, times(1)).fill(any(Shape.class));
 verify(graphics2DMock, times(1)).setPaint(Color.RED);
 verify(graphics2DMock, times(1)).setPaint(Color.YELLOW);
 verify(graphics2DMock, times(1)).drawString(eq(this.robotSnapshot.getName()), anyFloat(), anyFloat());
 }

 @Test
 public void testRenderWithSnapshotThatIsDeadAndRenderDeadIsFalse() {
 // given
 when(graphics2DMock.getColor()).thenReturn(Color.RED);
 when(graphics2DMock.setRenderDead(anyBoolean())).thenAnswer(invocation -> {
 this.robotRenderer.setRenderDead(invocation.getArgument(0));
 return null;
 });
 when(graphics2DMock.setShowStatusBars(anyBoolean())).thenAnswer(invocation -> {
 this.robotRenderer.setShowStatusBars(invocation.getArgument(0));
 return null;
 });
 when(graphics2DMock.setShowName(anyBoolean())).thenAnswer(invocation -> {
 this.robotRenderer.setShowName(invocation.getArgument(0));
 return null;
 });
 when(graphics2DMock.setFillShield(anyBoolean())).thenAnswer(invocation -> {
 this.robotRenderer.setFillShield(invocation.getArgument(0));
 return null;
 });

 // when
 this.robotSnapshot.setDead(true);
 this.robotRenderer.render(this.graphics2DMock, this.robotSnapshot, new HashSet<>());

 // then
 verify(graphics2DMock, never()).fill(any(Shape.class));
 }

 @Test
 public void testRenderWithSelectedSnapshot() {
 // given
 when(graphics2DMock.getColor()).thenReturn(Color.RED);
 when(graphics2DMock.setRenderDead(anyBoolean())).thenAnswer(invocation -> {
 this.robotRenderer.setRenderDead(invocation.getArgument(0));
 return null;
 });
 when(graphics2DMock.setShowStatusBars(anyBoolean())).thenAnswer(invocation -> {
 this.robotRenderer.setShowStatusBars(invocation.getArgument(0));
 return null;
 });
 when(graphics2DMock.setShowName(anyBoolean())).thenAnswer(invocation -> {
 this.robotRenderer.setShowName(invocation.getArgument(0));
 return null;
 });
 when(graphics2DMock.setFillShield(anyBoolean())).thenAnswer(invocation -> {
 this.robotRenderer.setFillShield(invocation.getArgument(0));
 return null;
 });

 // given and when
 final Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(this.robotSnapshot.getId());
 this.robotRenderer.render(this.graphics2DMock, this.robotSnapshot, selectedRobotIds);

 // then
 verify(graphics2DMock, times(1)).fill(any(Shape.class));
 }
}