package net.virtualinfinity.atrobots.gui.renderers.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.Before;
import org.junit.Test;

public class RobotRendererTest {

 private Graphics2D g2d;
 private RobotSnapshot robotSnapshot;
 private RobotRenderer robotRenderer;

 @Before
 public void setUp() {
 g2d = new Graphics2D(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
 robotSnapshot = new RobotSnapshot(1, new PositionVector(10.0, 10.0), new DirectionVector(0.0), new Temperature(50.0), 100.0, 50.0, true, false, new ArrayList<>());
 robotRenderer = new RobotRenderer();
 }

 @Test
 public void testRender() {
 RobotSnapshot robotSnapshot1 = new RobotSnapshot(1, new PositionVector(10.0, 10.0), new DirectionVector(0.0), new Temperature(50.0), 100.0, 50.0, true, false, new ArrayList<>());
 Set<Integer> selectedRobotIds = new HashSet<>();
 robotRenderer.render(g2d, robotSnapshot1, selectedRobotIds);
 assertFalse(robotSnapshot1.isDead());

 RobotSnapshot robotSnapshot2 = new RobotSnapshot(2, new PositionVector(11.0, 11.0), new DirectionVector(0.0), new Temperature(50.0), 100.0, 50.0, true, false, new ArrayList<>());
 Set<Integer> selectedRobotIds1 = new HashSet<>();
 robotRenderer.render(g2d, robotSnapshot2, selectedRobotIds1);
 assertFalse(robotSnapshot2.isDead());

 // Test when the robot is dead
 RobotSnapshot robotSnapshot3 = new RobotSnapshot(3, new PositionVector(12.0, 12.0), new DirectionVector(0.0), new Temperature(50.0), 100.0, 50.0, false, false, new ArrayList<>());
 Set<Integer> selectedRobotIds2 = new HashSet<>();
 robotRenderer.render(g2d, robotSnapshot3, selectedRobotIds2);
 assertTrue(robotSnapshot3.isDead());

 robotSnapshot = robotSnapshot1;
 // Test rendering with selected robot
 selectedRobotIds.add(robotSnapshot.getId());
 robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
 // Test painting selection
 Shape selection = robotRenderer.paintSelection(g2d, robotSnapshot);
 assertNotNull(selection);
 }

 @Test
 public void testIsRenderDead() {
 RobotRenderer robotRenderer = new RobotRenderer();
 assertFalse(robotRenderer.isRenderDead());

 RobotRenderer robotRenderer1 = new RobotRenderer();
 robotRenderer1.setRenderDead(true);
 assertTrue(robotRenderer1.isRenderDead());
 }

 @Test
 public void testSetRenderDead() {
 RobotRenderer robotRenderer = new RobotRenderer();
 assertFalse(robotRenderer.isRenderDead());

 robotRenderer.setRenderDead(true);
 assertTrue(robotRenderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars() {
 RobotRenderer robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isShowStatusBars());

 RobotRenderer robotRenderer1 = new RobotRenderer();
 robotRenderer1.setShowStatusBars(false);
 assertFalse(robotRenderer1.isShowStatusBars());
 }

 @Test
 public void testSetShowStatusBars() {
 RobotRenderer robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isShowStatusBars());

 robotRenderer.setShowStatusBars(false);
 assertFalse(robotRenderer.isShowStatusBars());
 }

 @Test
 public void testIsShowName() {
 RobotRenderer robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isShowName());

 RobotRenderer robotRenderer1 = new RobotRenderer();
 robotRenderer1.setShowName(false);
 assertFalse(robotRenderer1.isShowName());
 }

 @Test
 public void testSetShowName() {
 RobotRenderer robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isShowName());

 robotRenderer.setShowName(false);
 assertFalse(robotRenderer.isShowName());
 }

 @Test
 public void testIsFillShield() {
 RobotRenderer robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isFillShield());

 RobotRenderer robotRenderer1 = new RobotRenderer();
 robotRenderer1.setFillShield(false);
 assertFalse(robotRenderer1.isFillShield());
 }

 @Test
 public void testSetFillShield() {
 RobotRenderer robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isFillShield());

 robotRenderer.setFillShield(false);
 assertFalse(robotRenderer.isFillShield());
 }

 @Test(expected = NullPointerException.class)
 public void testPaintSelectionWithNullGraphics2D() {
 robotRenderer.paintSelection(null, robotSnapshot);
 }

 @Test(expected = NullPointerException.class)
 public void testPaintSelectionWithNullRobotSnapshot() {
 robotRenderer.paintSelection(g2d, null);
 }

 @Test
 public void testPaintShieldWithActiveShield() {
 RobotSnapshot robotSnapshot = new RobotSnapshot(1, new PositionVector(10.0, 10.0), new DirectionVector(0.0), new Temperature(50.0), 100.0, 50.0, true, false, new ArrayList<>());
 robotRenderer.setFillShield(true);
 Shape shield = robotRenderer.paintShield(g2d, robotSnapshot);
 assertNotNull(shield);
 }

 @Test
 public void testPaintShieldWithoutActiveShield() {
 RobotSnapshot robotSnapshot = new RobotSnapshot(1, new PositionVector(10.0, 10.0), new DirectionVector(0.0), new Temperature(50.0), 100.0, 50.0, false, false, new ArrayList<>());
 Shape shield = robotRenderer.paintShield(g2d, robotSnapshot);
 assertNull(shield);
 }

 @Test(expected = NullPointerException.class)
 public void testPaintNameWithNullGraphics2D() {
 robotRenderer.paintName(null, robotSnapshot);
 }

 @Test
 public void testPaintName() {
 RobotSnapshot robotSnapshot = new RobotSnapshot(1, new PositionVector(10.0, 10.0), new DirectionVector(0.0), new Temperature(50.0), 100.0, 50.0, true, false, new ArrayList<>());
 String robotName = "TestRobot";
 robotSnapshot.setName(robotName);
 robotRenderer.paintName(g2d, robotSnapshot);
 String expectedName = robotName;
 String actualName = g2d.getFontMetrics().getStringBounds(expectedName, g2d).toString();
 assertTrue(actualName.contains(expectedName));
 }
}