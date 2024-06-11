package net.virtualinfinity.atrobots.gui.renderers.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.measures.Length;
import net.virtualinfinity.atrobots.measures.Position;
import net.virtualinfinity.atrobots.measures.Speed;
import net.virtualinfinity.atrobots.measures.Vector;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import org.junit.Test;

public class RobotRendererTest {

 private static final RobotSnapshot ROBOT_SNAPSHOT = new RobotSnapshot() {{
 setId(1);
 setName("TestRobot");
 setArmor(new Length(50.0));
 setDead(false);
 setX(0.0);
 setY(0.0);
 setHeading(new Vector(Math.PI / 2.0, 0.0));
 setTemperature(new Speed(50.0));
 }};

 @Test
 public void testConstructor() {
 RobotRenderer renderer = new RobotRenderer();
 assertTrue(renderer.isRenderDead());
 assertTrue(renderer.isShowStatusBars());
 assertTrue(renderer.isRenderDead());
 assertTrue(renderer.isShowName());
 assertTrue(renderer.isFillShield());
 }

 @Test
 public void testRenderWithDeadRobot() {
 RobotRenderer renderer = new RobotRenderer();
 Graphics2D g2d = createMockGraphics2D();
 renderer.render(g2d, ROBOT_SNAPSHOT, Collections.emptySet());
 assertFalse(g2d.getPaint().equals(Color.RED));
 }

 @Test
 public void testRenderWithAliveRobot() {
 RobotRenderer renderer = new RobotRenderer();
 Graphics2D g2d = createMockGraphics2D();
 renderer.render(g2d, ROBOT_SNAPSHOT, Collections.emptySet());
 assertTrue(g2d.getPaint().equals(Color.RED));
 }

 @Test
 public void testIsRenderDead() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setRenderDead(false);
 assertFalse(renderer.isRenderDead());
 renderer.setRenderDead(true);
 assertTrue(renderer.isRenderDead());
 }

 @Test
 public void testSetShowStatusBars() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setShowStatusBars(false);
 assertFalse(renderer.isShowStatusBars());
 renderer.setShowStatusBars(true);
 assertTrue(renderer.isShowStatusBars());
 }

 @Test
 public void testSetShowName() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setShowName(false);
 assertFalse(renderer.isShowName());
 renderer.setShowName(true);
 assertTrue(renderer.isShowName());
 }

 @Test
 public void testSetFillShield() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setFillShield(false);
 assertFalse(renderer.isFillShield());
 renderer.setFillShield(true);
 assertTrue(renderer.isFillShield());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRenderWithNullGraphics2D() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.render(null, ROBOT_SNAPSHOT, Collections.emptySet());
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullRobotSnapshot() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.render(createMockGraphics2D(), null, Collections.emptySet());
 }

 @Test
 public void testRenderWithSelectedRobot() {
 RobotRenderer renderer = new RobotRenderer();
 Graphics2D g2d = createMockGraphics2D();
 Set<Integer> selectedRobotIds = new HashSet<>(Arrays.asList(ROBOT_SNAPSHOT.getId()));
 renderer.render(g2d, ROBOT_SNAPSHOT, selectedRobotIds);
 Shape selection = g2d.getClip();
 assertEquals(new Ellipse2D.Double(ROBOT_SNAPSHOT.getX() - 15.0, ROBOT_SNAPSHOT.getY() - 15.0, 30.0, 30.0), selection);
 }

 @Test
 public void testPaintSelection() {
 Graphics2D g2d = createMockGraphics2D();
 RobotRenderer renderer = new RobotRenderer();
 renderer.paintSelection(g2d, ROBOT_SNAPSHOT);
 Shape selection = g2d.getClip();
 assertEquals(new Ellipse2D.Double(ROBOT_SNAPSHOT.getX() - 15.0, ROBOT_SNAPSHOT.getY() - 15.0, 30.0, 30.0), selection);
 }

 @Test
 public void testPaintShieldWithActiveShield() {
 Graphics2D g2d = createMockGraphics2D();
 RobotRenderer renderer = new RobotRenderer();
 renderer.setFillShield(true);
 renderer.paintShield(g2d, ROBOT_SNAPSHOT);
 Shape shield = g2d.getClip();
 assertEquals(new Ellipse2D.Double(ROBOT_SNAPSHOT.getX() - 7.5, ROBOT_SNAPSHOT.getY() - 7.5, 15.0, 15.0), shield);
 }

 @Test
 public void testPaintShieldWithoutActiveShield() {
 Graphics2D g2d = createMockGraphics2D();
 RobotRenderer renderer = new RobotRenderer();
 renderer.setFillShield(false);
 renderer.paintShield(g2d, ROBOT_SNAPSHOT);
 Shape shield = g2d.getClip();
 assertEquals(new Rectangle2D.Double(ROBOT_SNAPSHOT.getX() - 50.0, ROBOT_SNAPSHOT.getY() + 20.0, ROBOT_SNAPSHOT.getArmor().getValue(), 10.0), shield);
 }

 @Test
 public void testPaintName() {
 Graphics2D g2D = createMockGraphics2D();
 RobotRenderer renderer = new RobotRenderer();
 renderer.paintName(g2D, ROBOT_SNAPSHOT);
 String name = g2D.getFontMetrics().getStringBounds(ROBOT_SNAPSHOT.getName(), g2D).toString();
 assertEquals(ROBOT_SNAPSHOT.getName(), name);
 }

 @Test
 public void testPaintHeat() {
 Graphics2D g2d = createMockGraphics2D();
 RobotRenderer renderer = new RobotRenderer();
 renderer.paintHeat(g2d, ROBOT_SNAPSHOT);
 Shape heatBar = g2d.getClip();
 double expectedWidth = ROBOT_SNAPSHOT.getTemperature().getValue() * 0.2;
 assertEquals(new Rectangle2D.Double(ROBOT_SNAPSHOT.getX() - 50.0, ROBOT_SNAPSHOT.getY() + 35.0, expectedWidth, 10.0), heatBar);
 }

 @Test
 public void testPaintArmor() {
 Graphics2D g2d = createMockGraphics2D();
 RobotRenderer renderer = new RobotRenderer();
 renderer.paintArmor(g2d, ROBOT_SNAPSHOT);
 Shape armor = g2d.getClip();
 double expectedWidth = ROBOT_SNAPSHOT.getArmor().getValue();
 assertEquals(new Rectangle2D.Double(ROBOT_SNAPSHOT.getX() - 50.0, ROBOT_SNAPSHOT.getY() + 20.0, expectedWidth, 10.0), armor);
 }

 @Test
 public void testPaintTurret() {
 Graphics2D g2d = createMockGraphics2D();
 RobotRenderer renderer = new RobotRenderer();
 renderer.paintTurret(g2d, ROBOT_SNAPSHOT);
 Line2D.Double turretLine = (Line2D.Double) g2d.getClip();
 assertEquals(ROBOT_SNAPSHOT.getX(), turretLine.getX1());
 assertEquals(ROBOT_SNAPSHOT.getY(), turretLine.getY1());
 assertEquals(ROBOT_SNAPSHOT.getX() + ROBOT_SNAPSHOT.getTurretHeading().cosine() * 5.0, turretLine.getX2());
 assertEquals(ROBOT_SNAPSHOT.getY() + ROBOT_SNAPSHOT.getTurretHeading().sine() * 5.0, turretLine.getY2());
 }

 @Test
 public void testPaintBody() {
 Graphics2D g2d = createMockGraphics2D();
 RobotRenderer renderer = new RobotRenderer();
 renderer.paintBody(g2d, ROBOT_SNAPSHOT);
 Path2D bodyPath = (Path2D) g2d.getClip();
 assertEquals(3, bodyPath.getElements().length);
 }
}