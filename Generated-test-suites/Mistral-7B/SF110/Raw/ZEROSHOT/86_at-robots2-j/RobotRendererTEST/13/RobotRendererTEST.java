package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.Test;

public class RobotRendererTest {

 private RobotSnapshot getTestSnapshot() {
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 robotSnapshot.setId(1);
 robotSnapshot.setX(100.0);
 robotSnapshot.setY(100.0);
 robotSnapshot.setHeading(new PolarAngle(Math.PI / 2.0));
 robotSnapshot.setTurretHeading(new PolarAngle(Math.PI / 4.0));
 robotSnapshot.setArmor(50.0);
 robotSnapshot.setTemperature(new Temperature(100.0));
 robotSnapshot.setIsDead(false);
 robotSnapshot.setIsActiveShield(true);
 robotSnapshot.setRenderDead(false);
 robotSnapshot.setShowStatusBars(true);
 robotSnapshot.setShowName(true);
 robotSnapshot.setFillShield(true);
 return robotSnapshot;
 }

 @Test
 public void testRender() {
 RobotRenderer renderer = new RobotRenderer();
 Graphics2D g2d = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
 Set<Integer> selectedRobotIds = new HashSet<Integer>();
 RobotSnapshot robotSnapshot = getTestSnapshot();

 renderer.render(g2d, robotSnapshot, selectedRobotIds);

 // Test painting body
 Shape body = new GeneralPath();
 body.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 body.lineTo(robotSnapshot.getX() + body.getCurrentPoint().getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + body.getCurrentPoint().getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 body.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 body.lineTo(robotSnapshot.getX() + body.getCurrentPoint().getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + body.getCurrentPoint().getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 body.closePath();
 Path2D bodyPath = new Path2D.Double();
 bodyPath.append(body, false);

 if (!robotSnapshot.isDead()) {
 Color expectedBodyFillColor = Color.red;
 } else {
 Color expectedBodyFillColor = Color.orange.darker().darker();
 }

 assertEquals("Incorrect body fill color", expectedBodyFillColor, g2d.getColor());
 assertTrue("Body was not filled", g2d.getFont().getFontMetrics().containsPoint(bodyPath.getBounds2D().getCenterX(), bodyPath.getBounds2D().getCenterY()));

 // Test painting shield
 Ellipse2D shield = new Ellipse2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, robotSnapshot.getArmor(), 10.0);
 Path2D shieldPath = new Path2D.Double();
 shieldPath.append(shield, false);

 if (robotSnapshot.isActiveShield()) {
 Color expectedShieldColor1 = RobotRenderer.HEAT_COLOR1;
 Color expectedShieldColor2 = Color.yellow;
 } else {
 Color expectedShieldColor1 = Color.white;
 Color expectedShieldColor2 = Color.white;
 }

 assertEquals("Incorrect shield colors", expectedShieldColor1, g2d.getColor());
 g2d.setColor(expectedShieldColor2);
 g2d.fill(shieldPath);
 g2d.setColor(expectedShieldColor1);
 g2d.draw(shieldPath);

 // Test painting name
 String robotName = "TestBot";

 assertEquals("Incorrect name text", robotName, g2d.getFont().getStringMetrics().getStringBounds(robotName, g2d).getCenterX(), g2d.getFont().getStringMetrics().getStringBounds(robotName, g2d).getCenterY());

 // Test painting heat
 Rectangle2D heatRect = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);
 Path2D heatPath = new Path2D.Double();
 heatPath.append(new Rectangle2D.Double(heatRect.getX(), heatRect.getY(), heatRect.getWidth(), heatRect.getHeight()), false);

 if (!robotSnapshot.isDead()) {
 double temperatureScale = robotSnapshot.getTemperature().getLogScale();
 double heatLength = temperatureScale * 0.2;
 } else {
 heatLength = 0.0;
 }

 Path2D filledHeatPath = new Path2D.Double();
 filledHeatPath.append(heatPath, false);
 filledHeatPath.append(new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, heatLength, 10.0), false);

 assertEquals("Incorrect heat colors", RobotRenderer.HEAT_COLOR1, g2d.getColor());
 g2d.setColor(RobotRenderer.HEAT_COLOR2);
 g2d.fill(filledHeatPath);
 g2d.setColor(RobotRenderer.HEAT_COLOR1);
 g2d.draw(heatPath);

 // Test painting armor
 Path2D armorPath = new Path2D.Double();
 armorPath.append(new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, robotSnapshot.getArmor(), 10.0), false);
 Path2D filledArmorPath = new Path2D.Double();
 filledArmorPath.append(new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, 100.0, 10.0), false);
 filledArmorPath.append(new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, robotSnapshot.getArmor(), 10.0), false);

 assertEquals("Incorrect armor colors", new Color(0.0f, 0.0f, 1.0f, 0.6f), g2d.getColor());
 g2d.setColor(new Color(0.0f, 1.0f, 0.0f, 0.6f));
 g2d.fill(filledArmorPath);
 g2d.setColor(new Color(0.0f, 0.0f, 1.0f, 0.6f));
 g2d.draw(armorPath);

 // Test painting turret
 Line2D turretLine = new Line2D.Double(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0);

 assertEquals("Incorrect turret line color", Color.white, g2d.getColor());
 g2d.draw(turretLine);
 }

 @Test
 public void testSetRenderDead() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setRenderDead(false);
 assertFalse("Render dead should be false by default", renderer.isRenderDead());

 renderer.setRenderDead(true);
 assertTrue("Render dead should be true when set", renderer.isRenderDead());
 }

 @Test
 public void testSetShowStatusBars() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setShowStatusBars(false);
 assertFalse("Status bars should be false by default", renderer.isShowStatusBars());

 renderer.setShowStatusBars(true);
 assertTrue("Status bars should be true when set", renderer.isShowStatusBars());
 }

 @Test
 public void testSetShowName() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setShowName(false);
 assertFalse("Name should be false by default", renderer.isShowName());

 renderer.setShowName(true);
 assertTrue("Name should be true when set", renderer.isShowName());
 }

 @Test
 public void testSetFillShield() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setFillShield(false);
 assertFalse("Fill shield should be false by default", renderer.isFillShield());

 renderer.setFillShield(true);
 assertTrue("Fill shield should be true when set", renderer.isFillShield());
 }
}