package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;

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

public class RobotRendererTest {

 private RobotRenderer renderer;
 private Graphics2D g2d;

 @Before
 public void setup() {
 renderer = new RobotRenderer();
 g2d = Mockito.mock(Graphics2D.class);
 }

 @Test
 public void testRender_RobotAlive_ShouldRenderBodyShieldTurretNameArmorHeatAndSelection() {
 // Arrange
 RobotSnapshot robotSnapshot = createRobotSnapshot();
 // Act
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 // Assert
 verify(g2d).setPaint(Color.red);
 verify(g2d).fill(ArgumentMatchers.any(GeneralPath.class));
 verify(g2d).setPaint(Color.white);
 verify(g2d).draw(ArgumentMatchers.any(Line2D.class));
 verify(g2d).setPaint(Color.yellow);
 verify(g2d).drawString(robotSnapshot.getName(), ArgumentMatchers.anyFloat(), ArgumentMatchers.anyFloat());
 verify(g2d).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
 verify(g2d).fill(ArgumentMatchers.any(Rectangle2D.Double.class));
 verify(g2d).setPaint(new Color(0.0f, 1.0f, 0.0f, 0.6f));
 verify(g2d).draw(ArgumentMatchers.any(Rectangle2D.Double.class));
 verify(g2d).setPaint(new GradientPaint(ArgumentMatchers.anyFloat(), ArgumentMatchers.anyFloat(), RobotRenderer.HEAT_COLOR1, ArgumentMatchers.anyFloat(), ArgumentMatchers.anyFloat(), RobotRenderer.HEAT_COLOR2));
 verify(g2d).fill(ArgumentMatchers.any(Rectangle2D.Double.class));
 verify(g2d).draw(ArgumentMatchers.any(Rectangle2D.Double.class));
 }

 @Test
 public void testRender_RobotDead_ShouldNotRenderAnything() {
 // Arrange
 RobotSnapshot robotSnapshot = createRobotSnapshot();
 robotSnapshot.setDead(true);
 // Act
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 // Assert
 verifyNoMoreInteractions(g2d);
 }

 @Test
 public void testRender_RobotSelected_ShouldRenderSelection() {
 // Arrange
 RobotSnapshot robotSnapshot = createRobotSnapshot();
 Set<Integer> selectedRobotIds = new HashSet<>(Arrays.asList(1));
 // Act
 renderer.render(g2d, robotSnapshot, selectedRobotIds);
 // Assert
 verify(g2d).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
 verify(g2d).fill(ArgumentMatchers.any(Ellipse2D.Double.class));
 }

 @Test
 public void testIsRenderDead_DefaultIsTrue_ShouldReturnTrue() {
 // Act
 assertTrue(renderer.isRenderDead());
 // Act
 // Assert
 }

 @Test
 public void testIsRenderDead_DefaultIsFalse_ShouldReturnFalse() {
 // Act
 renderer = new RobotRenderer();
 renderer.setRenderDead(false);
 // Act
 assertFalse(renderer.isRenderDead());
 // Act
 // Assert
 }

 @Test
 public void testIsShowStatusBars_DefaultIsTrue_ShouldReturnTrue() {
 // Act
 assertTrue(renderer.isShowStatusBars());
 // Act
 // Assert
 }

 @Test
 public void testIsShowStatusBars_DefaultIsFalse_ShouldReturnFalse() {
 // Act
 renderer = new RobotRenderer();
 renderer.setShowStatusBars(false);
 // Act
 assertFalse(renderer.isShowStatusBars());
 // Act
 // Assert
 }

 @Test
 public void testIsShowName_DefaultIsTrue_ShouldReturnTrue() {
 // Act
 assertTrue(renderer.isShowName());
 // Act
 // Assert
 }

 @Test
 public void testIsShowName_DefaultIsFalse_ShouldReturnFalse() {
 // Act
 renderer = new RobotRenderer();
 renderer.setShowName(false);
 // Act
 assertFalse(renderer.isShowName());
 // Act
 // Assert
 }

 @Test
 public void testIsFillShield_DefaultIsTrue_ShouldReturnTrue() {
 // Act
 assertTrue(renderer.isFillShield());
 // Act
 // Assert
 }

 @Test
 public void testIsFillShield_DefaultIsFalse_ShouldReturnFalse() {
 // Act
 renderer = new RobotRenderer();
 renderer.setFillShield(false);
 // Act
 assertFalse(renderer.isFillShield());
 // Act
 // Assert
 }

 private RobotSnapshot createRobotSnapshot() {
 return new RobotSnapshot(1, new Point2D.Double(10, 10), 10, 10, 10, 10, new Angle(0), new Angle(0), false, new Temperature(100), 50, 50, false, new Vector2D(0, 0), "RobotName");
 }
}