package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;

import java.awt.*;
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
 private RobotSnapshot robotSnapshot;

 @Before
 public void setup() {
 renderer = new RobotRenderer();
 g2d = new Graphics2D(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
 robotSnapshot = new RobotSnapshot(1, 10.0, 10.0, 50.0, new Point2D.Double(0, 0), new Vector2D.Double(0, 0), 5.0, "RoboTest", 100, 15.0, 50.0, false, false, 1.0, 0.5, false);
 }

 @Test
 public void testRenderWithValidSnapshot() {
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 // Add assertions for the different parts of the rendered robot here
 assertNotNull(g2d.getFontMetrics().getFont());
 assertTrue(g2d.getFontMetrics().getStringBounds(robotSnapshot.getName(), g2d).getWidth() > 0);
 }

 @Test
 public void testRenderWithDeadSnapshot() {
 renderer.setRenderDead(false);
 robotSnapshot.setDead(true);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertFalse(g2d.getStroke() instanceof BasicStroke);
 }

 @Test
 public void testRenderWithShowStatusBarsSetToFalse() {
 renderer.setShowStatusBars(false);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertFalse(g2d.getColor().equals(new Color(0.0f, 0.0f, 1.0f, 0.6f)));
 }

 @Test
 public void testRenderWithShowNameSetToFalse() {
 renderer.setShowName(false);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertFalse(g2d.getPaint().equals(Color.yellow));
 }

 @Test
 public void testRenderWithFillShieldSetToFalse() {
 renderer.setFillShield(false);
 robotSnapshot.setActiveShield(true);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertFalse(g2d.getPaint() instanceof RadialGradientPaint);
 }

 @Test
 public void testSetAndGetRenderDead() {
 renderer.setRenderDead(true);
 assertTrue(renderer.isRenderDead());
 renderer.setRenderDead(false);
 assertFalse(renderer.isRenderDead());
 }

 @Test
 public void testSetAndGetShowStatusBars() {
 renderer.setShowStatusBars(true);
 assertTrue(renderer.isShowStatusBars());
 renderer.setShowStatusBars(false);
 assertFalse(renderer.isShowStatusBars());
 }

 @Test
 public void testSetAndGetShowName() {
 renderer.setShowName(true);
 assertTrue(renderer.isShowName());
 renderer.setShowName(false);
 assertFalse(renderer.isShowName());
 }

 @Test
 public void testSetAndGetFillShield() {
 renderer.setFillShield(true);
 assertTrue(renderer.isFillShield());
 renderer.setFillShield(false);
 assertFalse(renderer.isFillShield());
 }

 @Test
 public void testPaintSelection() {
 renderer.paintSelection(g2d, robotSnapshot);
 Shape selectionShape = g2d.getShape();
 assertTrue(selectionShape instanceof Ellipse2D.Double);
 Ellipse2D.Double selectionEllipse = (Ellipse2D.Double) selectionShape;
 assertEquals(robotSnapshot.getX(), selectionEllipse.getCenterX(), 0.01);
 assertEquals(robotSnapshot.getY(), selectionEllipse.getCenterY(), 0.01);
 assertEquals(30.0, selectionEllipse.getWidth(), 0.01);
 assertEquals(30.0, selectionEllipse.getHeight(), 0.01);
 }

 @Test
 public void testPaintShield() {
 renderer.paintShield(g2d, robotSnapshot);
 Shape shieldShape = g2d.getShape();
 assertTrue(shieldShape instanceof Ellipse2D.Double);
 Ellipse2D.Double shieldEllipse = (Ellipse2D.Double) shieldShape;
 assertEquals(robotSnapshot.getX(), shieldEllipse.getCenterX(), 0.01);
 assertEquals(robotSnapshot.getY(), shieldEllipse.getCenterY(), 0.01);
 assertEquals(15.0, shieldEllipse.getWidth(), 0.01);
 assertEquals(15.0, shieldEllipse.getHeight(), 0.01);
 }

 @Test
 public void testPaintName() {
 renderer.paintName(g2d, robotSnapshot);
 String nameString = g2d.getFontMetrics().getStringBounds(robotSnapshot.getName(), g2d).getBounds().toString();
 assertEquals("-10.0, -10.0, 10.0, 20.0", nameString);
 }

 @Test
 public void testPaintHeat() {
 renderer.paintHeat(g2d, robotSnapshot);
 Shape heatShape = g2d.getShape();
 assertTrue(heatShape instanceof Rectangle2D.Double);
 Rectangle2D.Double heatRectangle = (Rectangle2D.Double) heatShape;
 assertEquals(robotSnapshot.getX() - 50.0, heatRectangle.getMinX(), 0.01);
 assertEquals(robotSnapshot.getY() + 35.0, heatRectangle.getMinY(), 0.01);
 assertEquals(100.0, heatRectangle.getWidth(), 0.01);
 assertEquals(10.0, heatRectangle.getHeight(), 0.01);
 }

 @Test
 public void testPaintArmor() {
 renderer.paintArmor(g2d, robotSnapshot);
 Shape armorShape = g2d.getShape();
 assertTrue(armorShape instanceof Rectangle2D.Double);
 Rectangle2D.Double armorRectangle = (Rectangle2D.Double) armorShape;
 assertEquals(robotSnapshot.getX() - 50.0, armorRectangle.getMinX(), 0.01);
 assertEquals(robotSnapshot.getY() + 20.0, armorRectangle.getMinY(), 0.01);
 assertEquals(robotSnapshot.getArmor(), armorRectangle.getWidth(), 0.01);
 assertEquals(10.0, armorRectangle.getHeight(), 0.01);
 }

 @Test
 public void testPaintTurret() {
 renderer.paintTurret(g2d, robotSnapshot);
 Shape turretShape = g2d.getShape();
 assertTrue(turretShape instanceof Line2D.Double);
 Line2D.Double turretLine = (Line2D.Double) turretShape;
 assertEquals(robotSnapshot.getX(), turretLine.getStartX(), 0.01);
 assertEquals(robotSnapshot.getY(), turretLine.getStartY(), 0.01);
 assertEquals(robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, turretLine.getEndX(), 0.01);
 assertEquals(robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0, turretLine.getEndY(), 0.01);
 }

 @Test
 public void testPaintBody() {
 renderer.paintBody(g2d, robotSnapshot);
 Shape bodyShape = g2d.getShape();
 assertTrue(bodyShape instanceof GeneralPath);
 GeneralPath bodyPath = (GeneralPath) bodyShape;
 assertTrue(bodyPath.getWindingRule() == WindingRule.NON_ZERO);
 Point2D.Double bodyStart = bodyPath.getCurrentPoint();
 Point2D.Double bodyEnd = bodyPath.getPreviousPoint();
 assertEquals(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, bodyStart.getX(), 0.01);
 assertEquals(robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0, bodyStart.getY(), 0.01);
 assertEquals(robotSnapshot.getX() + bodyEnd.getX(), bodyEnd.getX(), 0.01);
 assertEquals(robotSnapshot.getY() + bodyEnd.getY(), bodyEnd.getY(), 0.01);
 }
}