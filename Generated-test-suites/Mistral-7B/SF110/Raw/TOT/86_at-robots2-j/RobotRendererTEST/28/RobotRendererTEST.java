package net.virtualinfinity.atrobots.gui.renderers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {

 private Graphics2D g2d;
 private RobotRenderer renderer;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 g2d = Mockito.mock(Graphics2D.class);
 renderer = new RobotRenderer();
 robotSnapshot = new RobotSnapshot(1, new PositionVector(10.0, 10.0), new VelocityVector(0.0, 0.0), new Angle(0.0), new Temperature(100.0), 100.0, false, false, false, false, false, new Color(1, 1, 1), new Color(1, 1, 1), new Color(1, 1, 1));
 }

 @Test
 public void testRenderDeadRobot() {
 renderer.render(g2d, robotSnapshot, new HashSet<>());
 assertFalse(renderer.isRenderDead());
 assertFalse(g2d.getColor().equals(Color.RED));
 }

 @Test
 public void testRenderAliveRobot() {
 renderer.setRenderDead(false);
 robotSnapshot.setDead(false);
 renderer.render(g2d, robotSnapshot, new HashSet<>());
 assertTrue(renderer.isRenderDead());
 assertTrue(g2d.getColor().equals(Color.RED));
 }

 @Test
 public void testPaintSelectionWithSelectedRobot() {
 renderer.render(g2d, robotSnapshot, new HashSet<>(robotSnapshot.getId()));
 Path2D selectionPath = (Path2D) g2d.getPath();
 assertEquals(Ellipse2D.Double.class, selectionPath.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullGraphics2D() {
 renderer.render(null, robotSnapshot, new HashSet<>());
 }

 @Test(expected = NullPointerException.class)
 public void testNullRobotSnapshot() {
 renderer.render(g2d, null, new HashSet<>());
 }

 @Test
 public void testPaintShieldWithFillShield() {
 renderer.setFillShield(true);
 renderer.render(g2d, robotSnapshot, new HashSet<>());
 Path2D shieldPath = (Path2D) g2d.getPath();
 assertNotNull(shieldPath);
 }

 @Test
 public void testPaintShieldWithoutFillShield() {
 renderer.setFillShield(false);
 renderer.render(g2d, robotSnapshot, new HashSet<>());
 Path2D shieldPath = (Path2D) g2d.getPath();
 assertNull(shieldPath);
 }

 @Test
 public void testPaintName() {
 renderer.render(g2d, robotSnapshot, new HashSet<>());
 String name = g2d.getFontMetrics().getStringBounds(robotSnapshot.getName(), g2d).getSubstring(0, (int) g2d.getFontMetrics().getStringBounds(robotSnapshot.getName(), g2d).getWidth() / 2);
 assertEquals(" Robot", name);
 }

 @Test
 public void testPaintHeatWithActiveShield() {
 robotSnapshot.setActiveShield(true);
 renderer.setFillShield(true);
 renderer.render(g2d, robotSnapshot, new HashSet<>());
 Path2D heatPath = (Path2D) g2d.getPath();
 assertNotNull(heatPath);
 }

 @Test
 public void testPaintHeatWithoutActiveShield() {
 robotSnapshot.setActiveShield(false);
 renderer.setFillShield(false);
 renderer.render(g2d, robotSnapshot, new HashSet<>());
 Path2D heatPath = (Path2D) g2d.getPath();
 assertNull(heatPath);
 }

 @Test
 public void testPaintArmor() {
 renderer.render(g2d, robotSnapshot, new HashSet<>());
 Path2D armorPath = (Path2D) g2d.getPath();
 assertNotNull(armorPath);
 }

 @Test
 public void testPaintTurret() {
 renderer.render(g2d, robotSnapshot, new HashSet<>());
 Path2D turretPath = (Path2D) g2d.getPath();
 assertNotNull(turretPath);
 }

 @Test
 public void testPaintBody() {
 renderer.render(g2d, robotSnapshot, new HashSet<>());
 Path2D bodyPath = (Path2D) g2d.getPath();
 assertNotNull(bodyPath);
 }
}