package net.virtualinfinity.atrobots.gui.renderers.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import net.virtualinfinity.atrobots.gui.renderers.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.*;

public class RobotRendererTest {

 private RobotRenderer robotRenderer;
 private Graphics2D graphics2D;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 robotRenderer = new RobotRenderer();
 graphics2D = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 robotSnapshot = new RobotSnapshot();
 robotSnapshot.setX(0);
 robotSnapshot.setY(0);
 robotSnapshot.setName("Test Robot");
 robotSnapshot.setArmor(100);
 robotSnapshot.setTemperature(new Temperature(50));
 robotSnapshot.setDead(false);
 robotSnapshot.setHeading(new AbsoluteAngle(0));
 robotSnapshot.setTurretHeading(new AbsoluteAngle(0));
 robotSnapshot.setPositionVector(new PositionVector(0, 0));
 robotSnapshot.setId(1);
 }

 @Test
 public void testRender() {
 RobotRenderer spy = spy(robotRenderer);
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 robotSnapshot.setX(0);
 robotSnapshot.setY(0);
 robotSnapshot.setName("Test Robot");
 robotSnapshot.setArmor(100);
 robotSnapshot.setTemperature(new Temperature(50));
 robotSnapshot.setDead(false);
 robotSnapshot.setHeading(new AbsoluteAngle(0));
 robotSnapshot.setTurretHeading(new AbsoluteAngle(0));
 robotSnapshot.setPositionVector(new PositionVector(0, 0));
 robotSnapshot.setId(1);
 doReturn(robotSnapshot).when(robotRenderer).getSnapshot();
 doCallRealMethod().when(robotRenderer).render(graphics2D, robotSnapshot, Collections.emptySet());
 verify(robotRenderer).paintBody(graphics2D, robotSnapshot);
 verify(robotRenderer).paintShield(graphics2D, robotSnapshot);
 verify(robotRenderer).paintTurret(graphics2D, robotSnapshot);
 verify(robotRenderer, never()).paintArmor(graphics2D, robotSnapshot);
 verify(robotRenderer, never()).paintHeat(graphics2D, robotSnapshot);
 verify(robotRenderer, never()).paintName(graphics2D, robotSnapshot);
 verify(robotRenderer, never()).paintSelection(graphics2D, robotSnapshot);
 }

 @Test
 public void testRenderWhenRobotIsDead() {
 RobotRenderer spy = spy(robotRenderer);
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 robotSnapshot.setX(0);
 robotSnapshot.setY(0);
 robotSnapshot.setName("Test Robot");
 robotSnapshot.setArmor(100);
 robotSnapshot.setTemperature(new Temperature(50));
 robotSnapshot.setDead(true);
 robotSnapshot.setHeading(new AbsoluteAngle(0));
 robotSnapshot.setTurretHeading(new AbsoluteAngle(0));
 robotSnapshot.setPositionVector(new PositionVector(0, 0));
 robotSnapshot.setId(1);
 doReturn(robotSnapshot).when(robotRenderer).getSnapshot();
 doCallRealMethod().when(robotRenderer).render(graphics2D, robotSnapshot, Collections.emptySet());
 verify(robotRenderer, never()).paintBody(graphics2D, robotSnapshot);
 verify(robotRenderer, never()).paintShield(graphics2D, robotSnapshot);
 verify(robotRenderer, never()).paintTurret(graphics2D, robotSnapshot);
 verify(robotRenderer, never()).paintArmor(graphics2D, robotSnapshot);
 verify(robotRenderer, never()).paintHeat(graphics2D, robotSnapshot);
 verify(robotRenderer, never()).paintName(graphics2D, robotSnapshot);
 verify(robotRenderer, never()).paintSelection(graphics2D, robotSnapshot);
 }

 @Test
 public void testIsRenderDead() {
 RobotRenderer robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isRenderDead());
 RobotRenderer robotRenderer2 = new RobotRenderer();
 assertFalse(robotRenderer2.isRenderDead());
 }

 @Test
 public void testSetRenderDead() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.setRenderDead(true);
 assertTrue(robotRenderer.isRenderDead());
 RobotRenderer robotRenderer2 = new RobotRenderer();
 robotRenderer2.setRenderDead(false);
 assertFalse(robotRenderer2.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars() {
 RobotRenderer robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isShowStatusBars());
 RobotRenderer robotRenderer2 = new RobotRenderer();
 assertFalse(robotRenderer2.isShowStatusBars());
 }

 @Test
 public void testSetShowStatusBars() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.setShowStatusBars(true);
 assertTrue(robotRenderer.isShowStatusBars());
 RobotRenderer robotRenderer2 = new RobotRenderer();robotRenderer2.setShowStatusBars(false);
 assertFalse(robotRenderer2.isShowStatusBars());
 }

 @Test
 public void testIsShowName() {
 RobotRenderer robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isShowName());
 RobotRenderer robotRenderer2 = new RobotRenderer();
 assertFalse(robotRenderer2.isShowName());
 }

 @Test
 public void testSetShowName() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.setShowName(true);
 assertTrue(robotRenderer.isShowName());
 RobotRenderer robotRenderer2 = new RobotRenderer();
 robotRenderer2.setShowName(false);
 assertFalse(robotRenderer2.isShowName());
 }

 @Test
 public void testIsFillShield() {
 RobotRenderer robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isFillShield());
 RobotRenderer robotRenderer2 = new RobotRenderer();
 assertFalse(robotRenderer2.isFillShield());
 }

 @Test
 public void testSetFillShield() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.setFillShield(true);
 assertTrue(robotRenderer.isFillShield());
 RobotRenderer robotRenderer2 = new RobotRenderer();
 robotRenderer2.setFillShield(false);
 assertFalse(robotRenderer2.isFillShield());
 }

 @Test
 public void testPaintSelectionWithRobotNotSelected() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.paintSelection(graphics2D, robotSnapshot);
 assertTrue(graphics2D.getPaint() instanceof Color);
 assertEquals(graphics2D.getPaint(), Color.white);
 assertTrue(graphics2D.getClip() == null);
 }

 @Test
 public void testPaintSelectionWithRobotSelected() {
 RobotRenderer robotRenderer = new RobotRenderer();
 Set<Integer> selectedRobotIds = new HashSet<Integer>();
 selectedRobotIds.add(1);
 robotRenderer.paintSelection(graphics2D, robotSnapshot);
 assertTrue(graphics2D.getPaint() instanceof Color);
 assertFalse(graphics2D.getPaint().equals(Color.white));
 assertTrue(graphics2D.getClip() == null);
 }

 @Test
 public void testPaintShieldWithRobotHasActiveShield() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotSnapshot.setActiveShield(true);
 robotRenderer.paintShield(graphics2D, robotSnapshot);
 assertNotNull(graphics2D.getPaint());
 assertTrue(graphics2D.getPaint() instanceof RadialGradientPaint);
 }

 @Test
 public void testPaintShieldWithRobotDoesNotHaveActiveShield() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotSnapshot.setActiveShield(false);
 robotRenderer.paintShield(graphics2D, robotSnapshot);
 assertNotNull(graphics2D.getPaint());
 assertTrue(graphics2D.getPaint() instanceof Color);
 }

 @Test
 public void testPaintName() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.paintName(graphics2D, robotSnapshot);
 assertNotNull(graphics2D.getFont());
 }

 @Test(expected = NullPointerException.class)
 public void testPaintNameWithNullGraphics2D() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.paintName(null, robotSnapshot);
 }

 @Test
 public void testPaintHeatWithRobotNotDead() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.paintHeat(graphics2D, robotSnapshot);
 assertNotNull(graphics2D.getPaint());
 assertTrue(graphics2D.getPaint() instanceof GradientPaint);
 }

 @Test(expected = IllegalStateException.class)
 public void testPaintHeatWithRobotDead() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotSnapshot.setDead(true);
 robotRenderer.paintHeat(graphics2D, robotSnapshot);
 }

 @Test
 public void testPaintArmor() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.paintArmor(graphics2D, robotSnapshot);
 assertNotNull(graphics2D.getPaint());
 assertTrue(graphics2D.getPaint() instanceof Color);
 }

 @Test(expected = NullPointerException.class)
 public void testPaintArmorWithNullGraphics2D() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.paintArmor(null, robotSnapshot);
 }

 @Test
 public void testPaintTurret() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.paintTurret(graphics2D, robotSnapshot);
 assertNotNull(graphics2D.getPaint());
 assertTrue(graphics2D.getPaint() instanceof Color);
 }

 @Test(expected = NullPointerException.class)
 public void testPaintTurretWithNullGraphics2D() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.paintTurret(null, robotSnapshot);
 }

 @Test
 public void testPaintBody() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.paintBody(graphics2D, robotSnapshot);
 assertNotNull(graphics2D.getPaint());
 assertTrue(graphics2D.getPaint() instanceof Color);
 }

 @Test(expected = NullPointerException.class)
 public void testPaintBodyWithNullGraphics2D() {
 RobotRenderer robotRenderer = new RobotRenderer();
 robotRenderer.paintBody(null, robotSnapshot);
 }
}