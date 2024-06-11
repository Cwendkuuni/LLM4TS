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

 private Graphics2D g2d;
 private RobotSnapshot robotSnapshot;
 private RobotRenderer robotRenderer;

 @Before
 public void setUp() {
 g2d = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).createGraphics();
 robotSnapshot = new RobotSnapshot(1L, 50.0, new Point2D.Double(10.0, 10.0), new Vector2D.Double(1.0, 1.0), new Temperature(100.0), new Armor(100.0), new Heading(Math.PI / 4.0), false, true, false);
 robotRenderer = new RobotRenderer();
 }

 @Test
 public void testRender() {
 RobotSnapshot robotSnapshotWithoutHeat = new RobotSnapshot(1L, 50.0, new Point2D.Double(10.0, 10.0), new Vector2D.Double(1.0, 1.0), new Armor(100.0), new Heading(Math.PI / 4.0), false, true, false);
 g2d.setColor(Color.WHITE);
 g2d.clearRect(0, 0, g2d.getWidth(), g2d.getHeight());

 robotRenderer.render(g2d, robotSnapshot, Collections.singletonList(robotSnapshot.getId()));

 Point2D.Double expectedSelectionCenter = new Point2D.Double(robotSnapshot.getX() + 15.0, robotSnapshot.getY() + 15.0);
 Ellipse2D.Double expectedSelection = new Ellipse2D.Double(expectedSelectionCenter.getX() - 15.0, expectedSelectionCenter.getY() - 15.0, 30.0, 30.0);
 Shape actualSelection = g2d.getShape();
 assertEquals("Incorrect robot selection shape", expectedSelection, actualSelection);

 Point2D.Double expectedBodyCenter = new Point2D.Double(robotSnapshot.getX() + 15.0 * Math.cos(robotSnapshot.getHeading().getRadians()), robotSnapshot.getY() + 15.0 * Math.sin(robotSnapshot.getHeading().getRadians()));
 GeneralPath expectedBody = new GeneralPath();
 expectedBody.moveTo(expectedBodyCenter.getX(), expectedBodyCenter.getY());
 expectedBody.lineTo(expectedBodyCenter.getX() + Math.cos(robotSnapshot.getHeading().getRadians() - Math.PI / 2.0) * 9.0, expectedBodyCenter.getY() + Math.sin(robotSnapshot.getHeading().getRadians() - Math.PI / 2.0) * 9.0);
 expectedBody.lineTo(expectedBodyCenter.getX(), expectedBodyCenter.getY());
 expectedBody.lineTo(expectedBodyCenter.getX() + Math.cos(robotSnapshot.getHeading().getRadians() + Math.PI / 2.0) * 9.0, expectedBodyCenter.getY() + Math.sin(robotSnapshot.getHeading().getRadians() + Math.PI / 2.0) * 9.0);
 expectedBody.closePath();
 Shape actualBody = g2d.getShape();
 assertEquals("Incorrect robot body shape", expectedBody, actualBody);

 robotSnapshot.setDead(true);
 robotRenderer.render(g2d, robotSnapshotWithoutHeat, Collections.singletonList(robotSnapshotWithoutHeat.getId()));

 Shape actualSelectionDead = g2d.getShape();
 assertNull("Robot selection should not be drawn when dead", actualSelectionDead);
 }

 @Test
 public void testRenderWithHeat() {
 RobotSnapshot robotSnapshotWithHeat = new RobotSnapshot(1L, 50.0, new Point2D.Double(10.0, 10.0), new Vector2D.Double(1.0, 1.0), new Temperature(50.0), new Armor(100.0), new Heading(Math.PI / 4.0), false, true, false);
 g2d.setColor(Color.WHITE);
 g2d.clearRect(0, 0, g2d.getWidth(), g2d.getHeight());

 robotRenderer.render(g2d, robotSnapshotWithHeat, Collections.singletonList(robotSnapshotWithHeat.getId()));

 Point2D.Double expectedSelectionCenter = new Point2D.Double(robotSnapshotWithHeat.getX() + 15.0, robotSnapshotWithHeat.getY() + 15.0);
 Ellipse2D.Double expectedSelection = new Ellipse2D.Double(expectedSelectionCenter.getX() - 15.0, expectedSelectionCenter.getY() - 15.0, 30.0, 30.0);
 Shape actualSelection = g2d.getShape();
 assertEquals("Incorrect robot selection shape", expectedSelection, actualSelection);

 Point2D.Double expectedBodyCenter = new Point2D.Double(robotSnapshotWithHeat.getX() + 15.0 * Math.cos(robotSnapshotWithHeat.getHeading().getRadians()), robotSnapshotWithHeat.getY() + 15.0 * Math.sin(robotSnapshotWithHeat.getHeading().getRadians()));
 GeneralPath expectedBody = new GeneralPath();
 expectedBody.moveTo(expectedBodyCenter.getX(), expectedBodyCenter.getY());
 expectedBody.lineTo(expectedBodyCenter.getX() + Math.cos(robotSnapshotWithHeat.getHeading().getRadians() - Math.PI / 2.0) * 9.0, expectedBodyCenter.getY() + Math.sin(robotSnapshotWithHeat.getHeading().getRadians() - Math.PI / 2.0) * 9.0);
 expectedBody.lineTo(expectedBodyCenter.getX(), expectedBodyCenter.getY());
 expectedBody.lineTo(expectedBodyCenter.getX() + Math.cos(robotSnapshotWithHeat.getHeading().getRadians() + Math.PI / 2.0) * 9.0, expectedBodyCenter.getY() + Math.sin(robotSnapshotWithHeat.getHeading().getRadians() + Math.PI / 2.0) * 9.0);
 expectedBody.closePath();
 Shape actualBody = g2d.getShape();
 assertEquals("Incorrect robot body shape", expectedBody, actualBody);

 Rectangle2D.Double expectedHeatRect = new Rectangle2D.Double(robotSnapshotWithHeat.getX() - 50.0, robotSnapshotWithHeat.getY() + 35.0, 100.0, 10.0);
 Shape actualHeat = g2d.getShape();
 Rectangle2D actualHeatBounds = actualHeat.getBounds2D();
 assertEquals("Incorrect heat rectangle bounds", expectedHeatRect, actualHeatBounds);
 }

 @Test
 public void testPaintSelection() {
 Graphics2D graphics = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).createGraphics();
 Ellipse2D.Double selection = new Ellipse2D.Double(10.0, 10.0, 30.0, 30.0);
 robotRenderer.paintSelection(graphics, robotSnapshot, Collections.singletonList(robotSnapshot.getId()));

 Shape actualSelection = graphics.getShape();
 assertEquals("Incorrect robot selection shape", selection, actualSelection);
 }

 @Test
 public void testPaintShield() {
 Graphics2D graphics = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).createGraphics();
 Ellipse2D.Double shield = new Ellipse2D.Double(10.0, 10.0, 15.0, 15.0);
 robotRenderer.paintShield(graphics, robotSnapshot);

 Graphics2D graphicsWithFill = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).createGraphics();
 Ellipse2D.Double shieldWithFill = new Ellipse2D.Double(10.0, 10.0, 15.0, 15.0);
 robotRenderer.setFillShield(true);
 robotRenderer.paintShield(graphicsWithFill, robotSnapshot);

 Shape actualSelection = graphics.getShape();
 assertEquals("Incorrect robot shield shape without fill", shield, actualSelection);

 Shape actualSelectionWithFill = graphicsWithFill.getShape();
 assertEquals("Incorrect robot shield shape with fill", shieldWithFill, actualSelectionWithFill);
 }

 @Test
 public void testPaintName() {
 Graphics2D graphics = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).createGraphics();
 String expectedName = "TestRobot";
 fontSize = 12; // Set an appropriate font size for the test
 g2d.setFont(new Font("SansSerif", Font.BOLD, fontSize));
 robotRenderer.paintName(graphics, robotSnapshot);

 String actualName = graphics.getFontMetrics().getStringBounds(expectedName, g2d).toString();
 assertEquals("Incorrect robot name text", expectedName, actualName.substring(1, actualName.length() - 1));
 }

 @Test
 public void testPaintHeat() {
 Graphics2D graphics = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).createGraphics();
 Rectangle2D.Double expectedHeatRect = new Rectangle2D.Double(5.0, 35.0, 100.0, 10.0);
 robotRenderer.paintHeat(graphics, robotSnapshot);

 Shape actualHeat = graphics.getShape();
 Rectangle2D actualHeatBounds = actualHeat.getBounds2D();
 assertEquals("Incorrect heat rectangle bounds", expectedHeatRect, actualHeatBounds);
 }

 @Test
 public void testPaintArmor() {
 Graphics2D graphics = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).createGraphics();
 Rectangle2D.Double expectedArmorRect = new Rectangle2D.Double(5.0, 20.0, 100.0, 10.0);
 robotRenderer.paintArmor(graphics, robotSnapshot);

 Shape actualArmor = graphics.getShape();
 Rectangle2D actualArmorBounds = actualArmor.getBounds2D();
 assertEquals("Incorrect armor rectangle bounds", expectedArmorRect, actualArmorBounds);
 }

 @Test
 public void testPaintTurret() {
 Graphics2D graphics = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).createGraphics();
 Line2D.Double expectedTurretLine = new Line2D.Double(10.0, 10.0, 10.0 + Math.cos(Math.PI / 4.0) * 5.0, 10.0 + Math.sin(Math.PI / 4.0) * 5.0);
 robotRenderer.paintTurret(graphics, robotSnapshot);

 Shape actualTurret = graphics.getShape();
 assertEquals("Incorrect robot turret shape", expectedTurretLine, actualTurret);
 }

 @Test
 public void testPaintBody() {
 Graphics2D graphics = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).createGraphics();
 Point2D.Double expectedBodyCenter = new Point2D.Double(10.0 + Math.cos(Math.PI / 4.0) * 15.0, 10.0 + Math.sin(Math.PI / 4.0) * 15.0);
 GeneralPath expectedBody = new GeneralPath();
 expectedBody.moveTo(expectedBodyCenter.getX(), expectedBodyCenter.getY());
 expectedBody.lineTo(expectedBodyCenter.getX() + Math.cos(Math.PI / 4.0 - Math.PI / 2.0) * 9.0, expectedBodyCenter.getY() + Math.sin(Math.PI / 4.0 - Math.PI / 2.0) * 9.0);
 expectedBody.lineTo(expectedBodyCenter.getX(), expectedBodyCenter.getY());
 expectedBody.lineTo(expectedBodyCenter.getX() + Math.cos(Math.PI / 4.0 + Math.PI / 2.0) * 9.0, expectedBodyCenter.getY() + Math.sin(Math.PI / 4.0 + Math.PI / 2.0) * 9.0);
 expectedBody.closePath();
 robotRenderer.paintBody(graphics, robotSnapshot);

 Shape actualBody = graphics.getShape();
 assertEquals("Incorrect robot body shape", expectedBody, actualBody);
 }
}