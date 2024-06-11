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

 private static final double DELTA = 0.001;
 private static final double EPSILON = 1e-9;

 @Mock
 private Graphics2D graphics2D;
 @Mock
 private RobotSnapshot robotSnapshot;
 @Mock
 private Set<Integer> selectedRobotIds;

 private RobotRenderer robotRenderer;

 @Before
 public void setUp() {
 robotRenderer = new RobotRenderer();
 }

 @Test
 public void testRender_deadRobot_notRendered() {
 given(robotSnapshot.isDead()).willReturn(true);
 robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
 verify(graphics2D, never()).fill(any(Shape.class));
 }

 @Test
 public void testRender_aliveRobot_bodyPainted() {
 given(robotSnapshot.isDead()).willReturn(false);
 when(robotSnapshot.getHeading()).willReturn(new AbsoluteAngle(Math.PI / 2.0));
 when(robotSnapshot.getX()).willReturn(10.0);
 when(robotSnapshot.getY()).willReturn(20.0);
 when(robotSnapshot.getArmor()).willReturn(50.0);
 when(robotSnapshot.getTemperature()).willReturn(new Temperature(100.0));
 when(robotSnapshot.getPositionVector()).willReturn(new Point2D.Double(10.0, 20.0));

 Path bodyPath = mock(Path.class);
 when(graphics2D.getStroke()).thenReturn(new BasicStroke(1.0f));
 when(graphics2D.getPaint()).thenReturn(Color.red);

 doAnswer(invocation -> {
 Path path = invocation.getArgument(0);
 robotRenderer.paintBody(graphics2D, robotSnapshot);
 return null;
 }).when(graphics2D).fill(eq(bodyPath));

 doAnswer(invocation -> {
 Path path = invocation.getArgument(0);
 robotRenderer.paintBody(graphics2D, robotSnapshot);
 return null;
 }).when(graphics2D).draw(eq(bodyPath));

 robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);

 assertEquals(15.0, robotSnapshot.getHeading().cosine(), EPSILON);
 assertEquals(15.0, robotSnapshot.getHeading().sine(), EPSILON);
 assertEquals(10.0, robotSnapshot.getX(), DELTA);
 assertEquals(20.0, robotSnapshot.getY(), DELTA);
 assertEquals(50.0, robotSnapshot.getArmor(), DELTA);

 verify(graphics2D).fill(eq(bodyPath));
 }

 @Test
 public void testRender_aliveRobot_shieldPainted() {
 given(robotSnapshot.isDead()).willReturn(false);
 when(robotSnapshot.isActiveShield()).willReturn(true);
 given(robotSnapshot.getX()).willReturn(10.0);
 given(robotSnapshot.getY()).willReturn(20.0);
 given(robotSnapshot.getArmor()).willReturn(50.0);

 Ellipse2D shield = mock(Ellipse2D.class);
 when(graphics2D.fill(eq(shield))).thenReturn(true);

 robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);

 verify(graphics2D).fill(eq(shield));
 }

 @Test
 public void testRender_aliveRobot_namePainted() {
 given(robotSnapshot.isDead()).willReturn(false);
 given(robotSnapshot.getName()).willReturn("TestBot");
 given(robotSnapshot.getX()).willReturn(10.0);
 given(robotSnapshot.getY()).willReturn(20.0);

 String expectedName = "TestBot";

 when(graphics2D.getFontMetrics()).thenReturn(mock(FontMetrics.class));
 when(graphics2D.getFont()).thenReturn(mock(Font.class));

 doCallRealMethod().when(graphics2d).drawString(eq(expectedName), anyDouble(), anyDouble());

 robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);

 verify(graphics2D).drawString(eq(expectedName), eq(10.0 - (graphics2D.getFontMetrics().getStringBounds(expectedName, graphics2D).getWidth() / 2.0)), eq(20.0 - 10.0));
 }

 @Test
 public void testRender_aliveRobot_heatPainted() {
 given(robotSnapshot.isDead()).willReturn(false);
 given(robotSnapshot.getX()).willReturn(10.0);
 given(robotSnapshot.getY()).willReturn(20.0);
 given(robotSnapshot.getTemperature()).willReturn(new Temperature(50.0));
 given(graphics2D.getPaint()).willReturn(new GradientPaint(10.0, 20.0, RobotRenderer.HEAT_COLOR1, 10.0, 20.0, RobotRenderer.HEAT_COLOR2));
 given(graphics2D.fill(any(Shape.class))).willReturn(true);

 Rectangle2D heatRect = mock(Rectangle2D.class);
 when(graphics2D.fill(eq(heatRect))).thenReturn(true);

 robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);

 verify(graphics2D).fill(eq(heatRect));
 }

 @Test
 public void testRender_aliveRobot_armorPainted() {
 given(robotSnapshot.isDead()).willReturn(false);
 given(robotSnapshot.getX()).willReturn(10.0);
 given(robotSnapshot.getY()).willReturn(20.0);
 given(robotSnapshot.getArmor()).willReturn(50.0);

 Path armorPath = mock(Path.class);
 when(graphics2D.fill(eq(armorPath))).thenReturn(true);

 robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);

 verify(graphics2D).fill(eq(armorPath));
 }

 @Test
 public void testRender_aliveRobot_turretPainted() {
 given(robotSnapshot.isDead()).willReturn(false);
 given(robotSnapshot.getX()).willReturn(10.0);
 given(robotSnapshot.getY()).willReturn(20.0);
 given(robotSnapshot.getHeading()).willReturn(new AbsoluteAngle(Math.PI / 2.0));
 given(graphics2D.getPaint()).willReturn(Color.white);

 Line2D turretLine = mock(Line2D.class);
 when(graphics2D.draw(eq(turretLine))).thenReturn(true);

 robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);

 verify(graphics2D).draw(eq(turretLine));
 }

 @Test
 public void testRender_selectedRobot() {
 given(robotSnapshot.isDead()).willReturn(false);
 given(selectedRobotIds.contains(robotSnapshot.getId())).willReturn(true);
 given(robotSnapshot.getX()).willReturn(10.0);
 given(robotSnapshot.getY()).willReturn(20.0);

 Ellipse2D selectionEllipse = mock(Ellipse2D.class);
 when(graphics2D.fill(eq(selectionEllipse))).thenReturn(true);

 robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);

 verify(graphics2D).fill(eq(selectionEllipse));
 }
}