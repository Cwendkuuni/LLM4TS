package net.virtualinfinity.atrobots.gui.renderers.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.*;

public class RobotRendererTest {

 private RobotRenderer robotRenderer;
 private Graphics2D graphics2d;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 robotRenderer = new RobotRenderer();
 graphics2d = Mockito.mock(Graphics2D.class);
 robotSnapshot = new RobotSnapshot();
 robotSnapshot.setId(1);
 robotSnapshot.setX(10.0);
 robotSnapshot.setY(10.0);
 robotSnapshot.setHeading(new Angle(0));
 robotSnapshot.setTurretHeading(new Angle(0));
 robotSnapshot.setArmor(100.0);
 robotSnapshot.setTemperature(new Temperature(50.0));
 robotSnapshot.setDead(false);
 robotSnapshot.setName("TestRobot");
 }

 @Test
 public void testRender() {
 robotRenderer.render(graphics2d, robotSnapshot, new HashSet<>());
 // Add assertions for rendering functionality here
 }

 @Test
 public void testIsRenderDead() {
 // Test with default constructor
 RobotRenderer renderer1 = new RobotRenderer();
 assertTrue(renderer1.isRenderDead());

 // Test with setRenderDead method
 RobotRenderer renderer2 = new RobotRenderer();
 renderer2.setRenderDead(false);
 assertFalse(renderer2.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars() {
 // Test with default constructor
 RobotRenderer renderer1 = new RobotRenderer();
 assertTrue(renderer1.isShowStatusBars());

 // Test with setShowStatusBars method
 RobotRenderer renderer2 = new RobotRenderer();
 renderer2.setShowStatusBars(false);
 assertFalse(renderer2.isShowStatusBars());
 }

 @Test
 public void testIsShowName() {
 // Test with default constructor
 RobotRenderer renderer1 = new RobotRenderer();
 assertTrue(renderer1.isShowName());

 // Test with setShowName method
 RobotRenderer renderer2 = new RobotRenderer();
 renderer2.setShowName(false);
 assertFalse(renderer2.isShowName());
 }

 @Test
 public void testIsFillShield() {
 // Test with default constructor
 RobotRenderer renderer1 = new RobotRenderer();
 assertTrue(renderer1.isFillShield());

 // Test with setFillShield method
 RobotRenderer renderer2 = new RobotRenderer();
 renderer2.setFillShield(false);
 assertFalse(renderer2.isFillShield());
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullGraphics2D() {
 robotRenderer.render(null, robotSnapshot, new HashSet<>());
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullRobotSnapshot() {
 robotRenderer.render(graphics2d, null, new HashSet<>());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRenderWithEmptySelectedRobotIds() {
 robotRenderer.render(graphics2d, robotSnapshot, new HashSet<>());
 }

 @Test
 public void testPaintSelection() {
 final Ellipse2D.Double selection = new Ellipse2D.Double();
 robotRenderer.paintSelection(graphics2d, robotSnapshot);
 selection.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);
 assertEquals(selection, robotRenderer.getShape(graphics2d, robotSnapshot));
 }

 @Test
 public void testPaintShield() {
 final Ellipse2D.Double shield = new Ellipse2D.Double();
 robotRenderer.paintShield(graphics2d, robotSnapshot);
 shield.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 15.0, robotSnapshot.getY() + 15.0);
 if (robotRenderer.isFillShield()) {
 final Shape filledShape = robotRenderer.getShape(graphics2d, robotSnapshot);
 assertEquals(shield, filledShape);
 } else {
 final Stroke stroke = graphics2d.getStroke();
 assertEquals(stroke, robotRenderer.getStroke(graphics2d));
 final Shape outlineShape = robotRenderer.getShape(graphics2d, robotSnapshot);
 assertEquals(shield, outlineShape);
 }
 }

 @Test
 public void testPaintName() {
 final String robotName = "TestRobot";
 robotSnapshot.setName(robotName);
 robotRenderer.paintName(graphics2d, robotSnapshot);
 final FontMetrics fontMetrics = graphics2d.getFontMetrics();
 final float x = (float)(robotSnapshot.getX() - fontMetrics.getStringBounds(robotName, graphics2d).getWidth() / 2.0);
 final float y = (float)(robotSnapshot.getY() - 10.0);
 assertEquals(robotName, graphics2d.getFont().getFontName());
 assertEquals(x, graphics2d.getTransform().getTranslateX(), 0.01F);
 assertEquals(y, graphics2d.getTransform().getTranslateY(), 0.01F);
 }

 @Test
 public void testPaintHeat() {
 final Rectangle2D.Double heatRect = new Rectangle2D.Double();
 robotRenderer.paintHeat(graphics2d, robotSnapshot);
 heatRect.setFrameFromRect(new Rectangle(0, 0, 50, 10));
 heatRect.setLocation(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0);
 assertEquals(heatRect, robotRenderer.getShape(graphics2d, robotSnapshot));
 }

 @Test
 public void testPaintArmor() {
 final Rectangle2D.Double armorRect = new Rectangle2D.Double();
 robotRenderer.paintArmor(graphics2d, robotSnapshot);
 armorRect.setFrameFromRect(new Rectangle(0, 0, 100, 10));
 armorRect.setLocation(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0);
 assertEquals(armorRect.getWidth(), robotRenderer.getArmorWidth(robotSnapshot), 0.01);
 assertEquals(armorRect, robotRenderer.getShape(graphics2d, robotSnapshot));
 }

 @Test
 public void testPaintTurret() {
 final Line2D.Double turretLine = new Line2D.Double();
 robotRenderer.paintTurret(graphics2d, robotSnapshot);
 turretLine.setLine(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0);
 assertEquals(turretLine, robotRenderer.getShape(graphics2d, robotSnapshot));
 }

 @Test
 public void testPaintBody() {
 final GeneralPath bodyPath = new GeneralPath();
 robotRenderer.paintBody(graphics2d, robotSnapshot);
 bodyPath.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 bodyPath.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 bodyPath.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 bodyPath.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 bodyPath.closePath();
 if (robotSnapshot.isDead()) {
 final Stroke stroke = graphics2d.getStroke();
 assertEquals(stroke, robotRenderer.getStroke(graphics2d));
 assertEquals(bodyPath, robotRenderer.getShape(graphics2d, robotSnapshot));
 } else {
 final Shape filledBody = robotRenderer.getShape(graphics2d, robotSnapshot);
 assertEquals(bodyPath, filledBody);
 }
 }

}