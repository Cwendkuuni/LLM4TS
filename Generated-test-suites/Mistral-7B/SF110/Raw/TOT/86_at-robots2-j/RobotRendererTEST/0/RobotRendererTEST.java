package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RobotRendererTest {

 private Graphics2D g2d;
 private RobotSnapshot robotSnapshot;
 private Set<Integer> selectedRobotIds = new HashSet<>();
 private RobotRenderer robotRenderer;

 @Before
 public void setUp() {
 g2d = mock(Graphics2D.class);
 robotSnapshot = new RobotSnapshot(1, 10.0, 10.0, 10.0, 10.0, new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0), 10, 20, 30, true, true, true, true, true, new Color(1, 1, 1));
 robotRenderer = new RobotRenderer();
 }

 @Test
 public void testRender_ActiveRobot_WithSelection_ShouldRenderBodyShieldTurretAndName() {
 robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

 // Verify body was painted
 ArgumentCaptor<Path2D> bodyPathCaptor = ArgumentCaptor.forClass(Path2D.class);
 verify(g2d).fill(bodyPathCaptor.capture());
 Path2D bodyPath = bodyPathCaptor.getValue();
 assertEquals(Path2D.Float.TYPE, bodyPath.getType());
 assertEquals(11, bodyPath.getWindingRule());
 // Verify shield was painted
 verify(g2d).setPaint(any(RadialGradientPaint.class));
 // Verify turret was painted
 verify(g2d).draw(any(Line2D.Double.class));
 // Verify name was painted
 verify(g2d).drawString(robotSnapshot.getName(), argThat(isCloseTo((float)(robotSnapshot.getX() - g2d.getFontMetrics().getStringBounds(robotSnapshot.getName(), g2d).getWidth() / 2.0))), argThat(isCloseTo((float)(robotSnapshot.getY() - 10.0))));
 }

 @Test
 public void testRender_DeadRobot_ShouldNotRender() {
 robotSnapshot.setDead(true);
 robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

 verifyNoMoreInteractions(g2d);
 }

 @Test
 public void testRender_SelectedRobot_ShouldRenderSelection() {
 selectedRobotIds.add(robotSnapshot.getId());
 robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

 verify(g2d).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
 Ellipse2D selection = new Ellipse2D.Double();
 selection.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);
 verify(g2d).fill(eq(selection));
 }

 @Test
 public void testIsRenderDead_DefaultIsTrue_ShouldReturnTrue() {
 robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isRenderDead());
 }

 @Test
 public void testIsRenderDead_SetFalse_ShouldReturnFalse() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setRenderDead(false);
 assertFalse(robotRenderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars_DefaultIsTrue_ShouldReturnTrue() {
 robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isShowStatusBars());
 }

 @Test
 public void testIsShowStatusBars_SetFalse_ShouldReturnFalse() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setShowStatusBars(false);
 assertFalse(robotRenderer.isShowStatusBars());
 }

 @Test
 public void testIsShowName_DefaultIsTrue_ShouldReturnTrue() {
 robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isShowName());
 }

 @Test
 public void testIsShowName_SetFalse_ShouldReturnFalse() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setShowName(false);
 assertFalse(robotRenderer.isShowName());
 }

 @Test
 public void testIsFillShield_DefaultIsTrue_ShouldReturnTrue() {
 robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isFillShield());
 }

 @Test
 public void testIsFillShield_SetFalse_ShouldReturnFalse() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setFillShield(false);
 assertFalse(robotRenderer.isFillShield());
 }

 @Test
 public void testPaintSelection_ShouldPaintSelectionEllipse() {
 ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
 robotRenderer.paintSelection(g2d, robotSnapshot);
 verify(g2d).setPaint(colorCaptor.capture());
 assertEquals(new Color(1.0f, 1.0f, 0.0f, 0.25f), colorCaptor.getValue());
 Ellipse2D selection = new Ellipse2D.Double();
 selection.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);
 verify(g2d).fill(eq(selection));
 }
}