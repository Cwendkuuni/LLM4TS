package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import net.virtualinfinity.atrobots.measures.Length;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RobotRendererTest {

 private RobotSnapshot robotSnapshot;
 private Graphics2D g2d;
 private RobotRenderer robotRenderer;

 @Before
 public void setUp() {
 robotSnapshot = new RobotSnapshot(1, new AbsolutePosition(10.0, 10.0), new AbsoluteAngle(0), new Velocity(0, 0), new Temperature(20.0), 100.0, true, true, new AbsolutePositionVector(new Length(5.0)), new AbsoluteHeading(RelativeAngle.fromDegrees(0)), new AbsoluteHeading(RelativeAngle.fromDegrees(180)));
 robotRenderer = new RobotRenderer();
 }

 @Test
 public void testRender() {
 Graphics2D g2d = createGraphics2D();
 robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

 // Test painting body
 Shape expectedBody = new GeneralPath();
 expectedBody.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 expectedBody.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedBody.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 expectedBody.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedBody.closePath();

 Shape actualBody = g2d.getClip();
 assertEquals(expectedBody, actualBody);

 // Test painting shield
 if (robotSnapshot.isActiveShield()) {
 Ellipse2D expectedShield = new Ellipse2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);
 Shape actualShield = g2d.getClip();
 assertEquals(expectedShield, actualShield);
 }

 // Test painting name
 String expectedName = "RobotName";
 FontMetrics metrics = g2d.getFontMetrics();
 FontRenderContext context = g2d.getFontRenderContext();
 TextLayout layout = new TextLayout(expectedName, g2d.getFont(), context);
 Shape expectedNameShape = layout.getOutline();
 Shape actualNameShape = g2d.getClip();
 assertEquals(expectedNameShape, actualNameShape);

 g2d.dispose();
 }

 @Test
 public void testRenderRobotThatIsDead() {
 Graphics2D g2d = createGraphics2D();
 robotSnapshot.setIsDead(true);
 robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());
 g2d.dispose();

 // Test not painting body
 Shape expectedBody = new GeneralPath();
 expectedBody.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 expectedBody.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedBody.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 expectedBody.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedBody.closePath();

 Shape actualBody = g2d.getClip();
 assertNotNull(actualBody);
 assertNotEquals(expectedBody, actualBody);
 }

 @Test
 public void testPaintSelection() {
 Graphics2D g2d = createGraphics2D();
 Set<Integer> selectedRobotIds = new HashSet<>(Arrays.asList(robotSnapshot.getId()));
 robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

 Ellipse2D expectedSelection = new Ellipse2D.Double();
 expectedSelection.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);

 Shape actualSelection = g2d.getClip();
 assertEquals(expectedSelection, actualSelection);

 g2d.dispose();
 }

 @Test
 public void testPaintShield() {
 Graphics2D g2d = createGraphics2D();
 robotSnapshot.setIsActiveShield(true);
 robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

 Ellipse2D expectedShield = new Ellipse2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);
 Shape actualShield = g2d.getClip();
 assertEquals(expectedShield, actualShield);

 g2d.dispose();
 }

 @Test
 public void testPaintHeat() {
 Graphics2D g2d = createGraphics2D();
 robotSnapshot.setTemperature(new Temperature(50.0));
 robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

 Rectangle2D expectedHeatArea = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, robotSnapshot.getTemperature().getLogScale() * 0.2, 10.0);
 Shape actualHeatArea = g2d.getClip();
 assertEquals(expectedHeatArea, actualHeatArea);

 g2d.dispose();
 }

 @Test
 public void testPaintArmor() {
 Graphics2D g2d = createGraphics2D();
 robotSnapshot.setArmor(50.0);
 robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

 Rectangle2D expectedArmorArea = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, robotSnapshot.getArmor(), 10.0);
 Shape actualArmorArea = g2d.getClip();
 assertEquals(expectedArmorArea, actualArmorArea);

 g2d.dispose();
 }

 @Test
 public void testPaintTurret() {
 Graphics2D g2d = createGraphics2D();
 robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

 Line2D expectedTurret = new Line2D.Double(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0);
 Shape actualTurret = g2d.getClip();
 assertEquals(expectedTurret, actualTurret);

 g2d.dispose();
 }

 @Test
 public void testPaintNameWhenShowNameIsFalse() {
 Graphics2D g2d = createGraphics2D();
 robotRenderer.setShowName(false);
 robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

 Shape expectedNameShape = null;
 Shape actualNameShape = g2d.getClip();
 assertNull(actualNameShape);

 g2d.dispose();
 }

 @Test
 public void testPaintStatusBarsWhenShowStatusBarsIsFalse() {
 Graphics2D g2d = createGraphics2D();
 robotRenderer.setShowStatusBars(false);
 robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

 Shape expectedShield = null;
 Shape expectedHeatArea = null;
 Shape expectedArmorArea = null;

 Shape actualShield = g2d.getClip();
 assertNull(actualShield);

 Shape actualHeatArea = g2d.getClip();
 assertNull(actualHeatArea);

 Shape actualArmorArea = g2d.getClip();
 assertNull(actualArmorArea);

 g2d.dispose();
 }

 @Test
 public void testFillShield() {
 Graphics2D g2d = createGraphics2D();
 robotSnapshot.setIsActiveShield(true);
 robotRenderer.setFillShield(true);
 robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

 Ellipse2D expectedShield = new Ellipse2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);
 Shape actualShield = g2d.getClip();
 assertEquals(expectedShield, actualShield);

 Rectangle2D expectedFilledArea = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);
 Shape actualFilledArea = g2d.getClip();
 assertTrue(expectedFilledArea.intersects(actualFilledArea));

 g2d.dispose();
 }

 @Test
 public void testNotFillShield() {
 Graphics2D g2d = createGraphics2D();
 robotSnapshot.setIsActiveShield(true);
 robotRenderer.setFillShield(false);
 robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

 Ellipse2D expectedShield = new Ellipse2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);
 Shape actualShield = g2d.getClip();
 assertEquals(expectedShield, actualShield);

 Rectangle2D expectedFilledArea = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);
 Shape actualFilledArea = g2d.getClip();
 assertFalse(expectedFilledArea.intersects(actualFilledArea));

 g2d.dispose();
 }

 private Graphics2D createGraphics2D() {
 GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
 return gc.createGraphics2D();
 }
}