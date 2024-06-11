package net.virtualinfinity.atrobots.gui.renderers.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.Test;

public class RobotRendererTest {

 private RobotRenderer renderer;
 private Graphics2D g2d;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 this.renderer = new RobotRenderer();
 this.g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 }

 @Test
 public void testRender() {
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 this.renderer.render(this.g2d, robotSnapshot, null);
 assertNotNull(robotSnapshot);
 }

 @Test
 public void testIsRenderDead() {
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 robotSnapshot.setDead(true);
 assertTrue(this.renderer.isRenderDead());
 robotSnapshot.setDead(false);
 assertFalse(this.renderer.isRenderDead());
 }

 @Test
 public void testSetRenderDead() {
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 this.renderer.setRenderDead(false);
 assertFalse(this.renderer.isRenderDead());
 this.renderer.setRenderDead(true);
 assertTrue(this.renderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars() {
 assertTrue(this.renderer.isShowStatusBars());
 this.renderer.setShowStatusBars(false);
 assertFalse(this.renderer.isShowStatusBars());
 }

 @Test
 public void testSetShowStatusBars() {
 this.renderer.setShowStatusBars(false);
 assertFalse(this.renderer.isShowStatusBars());
 this.renderer.setShowStatusBars(true);
 assertTrue(this.renderer.isShowStatusBars());
 }

 @Test
 public void testIsShowName() {
 assertTrue(this.renderer.isShowName());
 this.renderer.setShowName(false);
 assertFalse(this.renderer.isShowName());
 }

 @Test
 public void testSetShowName() {
 this.renderer.setShowName(false);
 assertFalse(this.renderer.isShowName());
 this.renderer.setShowName(true);
 assertTrue(this.renderer.isShowName());
 }

 @Test
 public void testIsFillShield() {
 assertTrue(this.renderer.isFillShield());
 this.renderer.setFillShield(false);
 assertFalse(this.renderer.isFillShield());
 }

 @Test
 public void testSetFillShield() {
 this.renderer.setFillShield(false);
 assertFalse(this.renderer.isFillShield());
 this.renderer.setFillShield(true);
 assertTrue(this.renderer.isFillShield());
 }

 @Test(expected = NullPointerException.class)
 public void testRenderNullGraphics2D() {
 this.renderer.render(null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testRenderNullRobotSnapshot() {
 this.renderer.render(this.g2d, null, null);
 }

 @Test
 public void testRenderWithSelectedRobot() {
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(1);
 this.renderer.render(this.g2d, robotSnapshot, selectedRobotIds);
 assertNotNull(robotSnapshot);
 Shape selection = this.g2d.getClip();
 assertEquals(Shape.Type.ELLIPSE, selection.getType());
 }

 @Test
 public void testPaintSelection() {
 Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 this.renderer.paintSelection(g2d, robotSnapshot);
 Shape selection = g2d.getClip();
 assertEquals(Shape.Type.ELLIPSE, selection.getType());
 }

 @Test
 public void testPaintShield() {
 Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 this.renderer.paintShield(g2d, robotSnapshot);
 Shape shield = g2d.getClip();
 assertTrue(shield.getType() == Shape.Type.ELLIPSE);
 }

 @Test
 public void testPaintName() {
 Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 robotSnapshot.setName("testName");
 this.renderer.paintName(g2d, robotSnapshot);
 String name = g2d.getFontMetrics().getStringBounds("testName", g2d).getSubstring(0, g2d.getFontMetrics().getStringBounds("testName", g2d).length() / 2);
 assertEquals("testName", name);
 }

 @Test
 public void testPaintHeat() {
 Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 robotSnapshot.setTemperature(new Temperature(100));
 this.renderer.paintHeat(g2d, robotSnapshot);
 Shape heat = g2d.getClip();
 assertTrue(heat.getType() == Shape.Type.RECTANGLE);
 }

 @Test
 public void testPaintArmor() {
 Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 robotSnapshot.setArmor(50);
 this.renderer.paintArmor(g2d, robotSnapshot);
 Shape armor = g2d.getClip();
 assertEquals(Shape.Type.RECTANGLE, armor.getType());
 }

 @Test
 public void testPaintTurret() {
 Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 this.renderer.paintTurret(g2d, robotSnapshot);
 Shape turret = g2d.getClip();
 assertEquals(Shape.Type.LINE, turret.getType());
 }

 @Test
 public void testPaintBody() {
 Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 this.renderer.paintBody(g2d, robotSnapshot);
 Shape body = g2d.getClip();
 assertEquals(Shape.Type.POLYGON, body.getType());
 }

}