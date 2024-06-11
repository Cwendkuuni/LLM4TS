package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

class RobotRendererTest {

 @BeforeEach
 void setUp() {
 RobotSnapshot robotSnapshot = new RobotSnapshot(1L, new PositionVector(10, 10), "TestRobot", new Temperature(100), new HeadVector(0), new BodyArmor(10), true, false, false);
 MockitoAnnotations.initMocks(this);
 this.renderer = new RobotRenderer();
 this.graphics2DMock = mock(Graphics2D.class);
 }

 @AfterEach
 void tearDown() {
 this.renderer = null;
 this.graphics2DMock = null;
 }

 @Test
 void testRender_shouldRenderDeadRobot_whenRenderDeadIsTrue() {
 // Given
 RobotSnapshot deadRobotSnapshot = new RobotSnapshot(1L, new PositionVector(10, 10), "DeadRobot", null, null, null, true, false, false);
 Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(1);

 // When
 this.renderer.render(this.graphics2DMock, deadRobotSnapshot, selectedRobotIds);

 // Then
 verify(this.graphics2DMock, never()).setPaint(Color.red);
 verify(this.graphics2DMock).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
 }

 @Test
 void testRender_shouldRenderAliveRobot_whenRenderDeadIsFalse() {
 // Given
 when(this.graphics2DMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));
 RobotSnapshot aliveRobotSnapshot = new RobotSnapshot(1L, new PositionVector(10, 10), "AliveRobot", new Temperature(50), new HeadVector(0), new BodyArmor(10), false, false, false);
 Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(1);

 // When
 this.renderer.render(this.graphics2DMock, aliveRobotSnapshot, selectedRobotIds);

 // Then
 verify(this.graphics2DMock).setPaint(Color.red);
 verify(this.graphics2DMock).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
 verify(this.graphics2DMock).drawString(equalTo("AliveRobot"), anyFloat(), anyFloat());
 }

 @Test
 void testRender_shouldDrawArmorBar_whenShowStatusBarsIsTrue() {
 // Given
 when(this.graphics2DMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));
 RobotSnapshot robotSnapshot = new RobotSnapshot(1L, new PositionVector(10, 10), "Robot", new Temperature(50), new HeadVector(0), new BodyArmor(10), false, false, false);
 Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(1);

 // When
 this.renderer.render(this.graphics2DMock, robotSnapshot, selectedRobotIds);

 // Then
 Path2D expectedPath = new Path2D.Double();
 expectedPath.moveTo(25.0, 15.0);
 expectedPath.lineTo(125.0, 15.0);
 expectedPath.lineTo(125.0, 25.0);
 expectedPath.lineTo(25.0, 25.0);
 expectedPath.closePath();
 verify(this.graphics2DMock).fill(isA(Path2D.class));
 verify(this.graphics2DMock).setPaint(new Color(0.0f, 1.0f, 0.0f, 0.6f));
 verify(this.graphics2DMock).draw(eq(expectedPath));
 }

 @Test
 void testRender_shouldNotDrawArmorBar_whenShowStatusBarsIsFalse() {
 // Given
 when(this.graphics2DMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));
 RobotSnapshot robotSnapshot = new RobotSnapshot(1L, new PositionVector(10, 10), "Robot", new Temperature(50), new HeadVector(0), new BodyArmor(10), false, false, false);
 Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(1);

 // When
 this.renderer.setShowStatusBars(false);
 this.renderer.render(this.graphics2DMock, robotSnapshot, selectedRobotIds);

 // Then
 verify(this.graphics2DMock, never()).setPaint(new Color(0.0f, 1.0f, 0.0f, 0.6f));
 }

 @Test
 void testRender_shouldDrawHeatBar_whenShowStatusBarsIsTrue() {
 // Given
 when(this.graphics2DMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));
 RobotSnapshot robotSnapshot = new RobotSnapshot(1L, new PositionVector(10, 10), "Robot", new Temperature(50), null, null, false, false, false);
 Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(1);

 // When
 this.renderer.render(this.graphics2DMock, robotSnapshot, selectedRobotIds);

 // Then
 Path2D expectedPath = new Path2D.Double();
 expectedPath.moveTo(25.0, 55.0);
 expectedPath.lineTo(125.0, 55.0);
 expectedPath.lineTo(125.0, 65.0);
 expectedPath.lineTo(25.0, 65.0);
 expectedPath.lineTo(25.0, 55.0);
 expectedPath.closePath();
 verify(this.graphics2DMock).fill(isA(Path2D.class));
 verify(this.graphics2DMock).setPaint(new GradientPaint(25.0, 55.0, RobotRenderer.HEAT_COLOR1, 110.0, 55.0, RobotRenderer.HEAT_COLOR2));
 }

 @Test
 void testRender_shouldDrawName_whenShowNameIsTrue() {
 // Given
 when(this.graphics2DMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));
 RobotSnapshot robotSnapshot = new RobotSnapshot(1L, new PositionVector(10, 10), "Robot", new Temperature(50), new HeadVector(0), new BodyArmor(10), false, false, false);
 Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(1);

 // When
 this.renderer.render(this.graphics2DMock, robotSnapshot, selectedRobotIds);

 // Then
 verify(this.graphics2DMock).setPaint(Color.yellow);
 verify(this.graphics2DMock).drawString("Robot", anyFloat(), anyFloat());
 }

 @Test
 void testRender_shouldDrawSelectionEllipse_whenRobotIsSelected() {
 // Given
 when(this.graphics2DMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));
 RobotSnapshot robotSnapshot = new RobotSnapshot(1L, new PositionVector(10, 10), "Robot", new Temperature(50), new HeadVector(0), new BodyArmor(10), false, false, false);
 Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(1);

 // When
 this.renderer.render(this.graphics2DMock, robotSnapshot, selectedRobotIds);

 // Then
 Ellipse2D expectedEllipse = new Ellipse2D.Double();
 expectedEllipse.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), 30.0, 30.0);
 verify(this.graphics2DMock).fill(isA(Ellipse2D.class));
 }

 @Test
 void testRender_shouldNotDrawSelectionEllipse_whenRobotIsNotSelected() {
 // Given
 when(this.graphics2DMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));
 RobotSnapshot robotSnapshot = new RobotSnapshot(1L, new PositionVector(10, 10), "Robot", new Temperature(50), new HeadVector(0), new BodyArmor(10), false, false, false);
 Set<Integer> selectedRobotIds = new HashSet<>();

 // When
 this.renderer.render(this.graphics2DMock, robotSnapshot, selectedRobotIds);

 // Then
 verify(this.graphics2DMock, never()).fill(isA(Ellipse2D.class));
 }

 @Test
 void testIsRenderDead_shouldReturnFalseByDefault() {
 // Given

 // When
 boolean result = this.renderer.isRenderDead();

 // Then
 assertFalse(result);
 }

 @Test
 void testIsRenderDead_shouldReturnTrueWhenSetToTrue() {
 // Given

 // When
 this.renderer.setRenderDead(true);
 boolean result = this.renderer.isRenderDead();

 // Then
 assertTrue(result);
 }

 @Test
 void testIsShowStatusBars_shouldReturnTrueByDefault() {
 // Given

 // When
 boolean result = this.renderer.isShowStatusBars();

 // Then
 assertTrue(result);
 }

 @Test
 void testIsShowStatusBars_shouldReturnFalseWhenSetToFalse() {
 // Given

 // When
 this.renderer.setShowStatusBars(false);
 boolean result = this.renderer.isShowStatusBars();

 // Then
 assertFalse(result);
 }

 @Test
 void testIsShowName_shouldReturnTrueByDefault() {
 // Given

 // When
 boolean result = this.renderer.isShowName();

 // Then
 assertTrue(result);
 }

 @Test
 void testIsShowName_shouldReturnFalseWhenSetToFalse() {
 // Given

 // When
 this.renderer.setShowName(false);
 boolean result = this.renderer.isShowName();

 // Then
 assertFalse(result);
 }

 @Test
 void testIsFillShield_shouldReturnTrueByDefault() {
 // Given

 // When
 boolean result = this.renderer.isFillShield();

 // Then
 assertTrue(result);
 }

 @Test
 void testIsFillShield_shouldReturnFalseWhenSetToFalse() {
 // Given

 // When
 this.renderer.setFillShield(false);
 boolean result = this.renderer.isFillShield();

 // Then
 assertFalse(result);
 }

 private RobotRenderer renderer;
 private Graphics2D graphics2DMock;
}