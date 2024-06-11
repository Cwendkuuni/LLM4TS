package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

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
 // Initialize graphics context and renderer
 g2d = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
 renderer = new RobotRenderer();

 // Initialize robot snapshot
 robotSnapshot = new RobotSnapshot();
 robotSnapshot.setId(1);
 robotSnapshot.setX(50.0);
 robotSnapshot.setY(50.0);
 robotSnapshot.setHeading(new Angle(Math.PI / 4.0));
 robotSnapshot.setTurretHeading(new Angle(Math.PI / 4.0));
 robotSnapshot.setArmor(100.0);
 robotSnapshot.setTemperature(new Temperature(50.0));
 robotSnapshot.setDead(false);
 }

 @Test
 public void testRender() {
 // Test default rendering
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertFalse(robotSnapshot.isDead());

 // Test rendering of dead robot
 renderer.setRenderDead(true);
 robotSnapshot.setDead(true);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertTrue(robotSnapshot.isDead());

 // Test rendering with showStatusBars = false
 renderer.setShowStatusBars(false);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertFalse(renderer.isShowStatusBars());

 // Test rendering with showName = false
 renderer.setShowName(false);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertFalse(renderer.isShowName());

 // Test rendering with fillShield = false
 renderer.setFillShield(false);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertFalse(renderer.isFillShield());
 }

 @Test
 public void testPaintSelection() {
 // Test painting selection
 renderer.paintSelection(g2d, robotSnapshot);
 Ellipse2D selection = new Ellipse2D.Double();
 selection.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);
 assertEquals(0.25f, g2d.getPaint2D().getAlpha(), 0.01f);
 assertEquals(1.0f, g2d.getPaint2D().getColor().getRed(), 0.01f);
 assertEquals(1.0f, g2d.getPaint2D().getColor().getGreen(), 0.01f);
 assertEquals(0.0f, g2d.getPaint2D().getColor().getBlue(), 0.01f);
 assertEquals(selection.getX(), g2d.getTransform().getTransformedPoint(selection.getCenterX(), selection.getCenterY()).getX(), 0.01f);
 assertEquals(selection.getY(), g2d.getTransform().getTransformedPoint(selection.getCenterX(), selection.getCenterY()).getY(), 0.01f);
 }

 @Test
 public void testPaintShield() {
 // Test painting shield with fill = true
 renderer.setFillShield(true);
 renderer.paintShield(g2d, robotSnapshot);
 Ellipse2D shield = new Ellipse2D.Double();
 shield.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 15.0, robotSnapshot.getY() + 15.0);
 assertEquals(0.8f, g2d.getPaint2D().getColor().getRed(), 0.01f);
 assertEquals(0.6f, g2d.getPaint2D().getColor().getGreen(), 0.01f);
 assertEquals(1.0f, g2d.getPaint2D().getColor().getBlue(), 0.01f);
 assertEquals(0.8f, g2d.getPaint2D().getColor().getAlpha(), 0.01f);
 assertEquals(shield.getX(), g2d.getTransform().getTransformedPoint(shield.getCenterX(), shield.getCenterY()).getX(), 0.01f);
 assertEquals(shield.getY(), g2d.getTransform().getTransformedPoint(shield.getCenterX(), shield.getCenterY()).getY(), 0.01f);

 // Test painting shield with fill = false
 renderer.setFillShield(false);
 renderer.paintShield(g2d, robotSnapshot);
 assertEquals(0.8f, g2d.getPaint2D().getColor().getRed(), 0.01f);
 assertEquals(0.6f, g2d.getPaint2D().getColor().getGreen(), 0.01f);
 assertEquals(1.0f, g2d.getPaint2D().getColor().getBlue(), 0.01f);
 assertEquals(0.6f, g2d.getPaint2D().getColor().getAlpha(), 0.01f);
 }

 @Test
 public void testPaintName() {
 // Test painting name
 renderer.paintName(g2d, robotSnapshot);
 String expectedName = "Robot";
 assertEquals(expectedName, g2d.getFontMetrics().getStringBounds(expectedName, g2d).getWidth(), 0.01f);
 }

 @Test
 public void testPaintHeat() {
 // Test painting heat
 renderer.paintHeat(g2d, robotSnapshot);
 Rectangle2D heatRect = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);
 GradientPaint gradient = (GradientPaint) g2d.getPaint();
 assertEquals(RobotRenderer.HEAT_COLOR1, gradient.getColor(0));
 assertEquals(RobotRenderer.HEAT_COLOR2, gradient.getColor(1));
 }

 @Test
 public void testPaintArmor() {
 // Test painting armor
 renderer.paintArmor(g2d, robotSnapshot);
 Rectangle2D armorRect = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, robotSnapshot.getArmor(), 10.0);
 assertEquals(0.0f, g2d.getPaint2D().getColor().getRed(), 0.01f);
 assertEquals(0.0f, g2d.getPaint2D().getColor().getGreen(), 0.01f);
 assertEquals(1.0f, g2d.getPaint2D().getColor().getBlue(), 0.01f);
 assertEquals(0.6f, g2d.getPaint2D().getColor().getAlpha(), 0.01f);
 }

 @Test
 public void testPaintTurret() {
 // Test painting turret
 renderer.paintTurret(g2d, robotSnapshot);
 Line2D turretLine = new Line2D.Double(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0);
 assertEquals(Color.white.getRGB(), g2d.getPaint().getColor().getRGB(), 0);
 assertEquals(turretLine.getX1(), g2d.getTransform().getTransformedPoint(turretLine.getX1(), turretLine.getY1()).getX(), 0.01f);
 assertEquals(turretLine.getY1(), g2d.getTransform().getTransformedPoint(turretLine.getX1(), turretLine.getY1()).getY(), 0.01f);
 }

 @Test
 public void testPaintBody() {
 // Test painting body
 renderer.paintBody(g2d, robotSnapshot);
 GeneralPath bodyPath = new GeneralPath();
 bodyPath.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 bodyPath.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 bodyPath.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 bodyPath.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 bodyPath.closePath();
 if (robotSnapshot.isDead()) {
 assertEquals(Color.orange.darker().darker().getRGB(), g2d.getPaint().getColor().getRGB(), 0);
 } else {
 assertEquals(Color.red.getRGB(), g2d.getPaint().getColor().getRGB(), 0);
 }
 }
}