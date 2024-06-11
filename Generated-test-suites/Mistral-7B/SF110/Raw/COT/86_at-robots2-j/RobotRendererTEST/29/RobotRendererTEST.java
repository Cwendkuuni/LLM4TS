import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {

 private RobotRenderer renderer = new RobotRenderer();
 private Graphics2D g2d;
 private RobotSnapshot robotSnapshot;

 @Test
 public void testRender() {
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertFalse(robotSnapshot.isDead() && !renderer.isRenderDead());
 }

 @Test
 public void testRenderDead() {
 RobotSnapshot robotSnapshot = new RobotSnapshot();
 robotSnapshot.setDead(true);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertTrue(robotSnapshot.isDead() || renderer.isRenderDead());
 }

 @Test
 public void testPaintBody() {
 robotSnapshot = new RobotSnapshot();
 renderer.paintBody(g2d, robotSnapshot);
 Shape expectedShape = new GeneralPath(); // the shape of the robot body
 assertEquals(expectedShape, g2d.getShape());
 }

 @Test
 public void testPaintBodyDead() {
 robotSnapshot.setDead(true);
 renderer.paintBody(g2d, robotSnapshot);
 Shape expectedShape = new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f)
 .createLine(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 assertEquals(expectedShape, g2d.getShape());
 }

 @Test
 public void testPaintShield() {
 robotSnapshot = new RobotSnapshot();
 renderer.paintShield(g2d, robotSnapshot);
 RobotRenderer.RadialGradientPaint gradient = (RobotRenderer.RadialGradientPaint) g2d.getPaint();
 float[] expectedColors = { 0.0f, 0.75f, 1.0f };
 Color[] expectedGradientColors = { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white };
 assertArrayEquals(expectedColors, gradient.getFractionLocations());
 assertArrayEquals(expectedGradientColors, gradient.getColors());
 }

 @Test
 public void testPaintShieldNoFill() {
 robotSnapshot.setActiveShield(false);
 renderer.paintShield(g2d, robotSnapshot);
 Shape expectedShape = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);
 assertEquals(expectedShape, g2d.getShape());
 }

 @Test
 public void testPaintName() {
 robotSnapshot = new RobotSnapshot();
 renderer.paintName(g2d, robotSnapshot);
 String expectedName = "RobotName";
 assertEquals(expectedName, g2d.getFontMetrics().getStringBounds(robotSnapshot.getName(), g2d).getWidth(), g2d.getFontMetrics().getStringBounds(expectedName, g2d).getWidth());
 }

 @Test(expected = NullPointerException.class)
 public void testPaintNameNullName() {
 robotSnapshot.setName(null);
 renderer.paintName(g2d, robotSnapshot);
 }

 @Test
 public void testPaintHeat() {
 robotSnapshot = new RobotSnapshot();
 renderer.paintHeat(g2d, robotSnapshot);
 Shape expectedShape = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, robotSnapshot.getTemperature().getLogScale() * 0.2, 10.0);
 assertEquals(expectedShape, g2d.getShape());
 }

 @Test
 public void testPaintHeatDead() {
 robotSnapshot.setDead(true);
 renderer.paintHeat(g2d, robotSnapshot);
 Shape expectedShape = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);
 assertEquals(expectedShape, g2d.getShape());
 }

 @Test
 public void testPaintArmor() {
 robotSnapshot = new RobotSnapshot();
 renderer.paintArmor(g2d, robotSnapshot);
 Shape expectedShape = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, robotSnapshot.getArmor(), 10.0);
 assertEquals(expectedShape, g2d.getShape());
 }

 @Test
 public void testPaintArmorNegativeArmor() {
 robotSnapshot.setArmor(-10);
 renderer.paintArmor(g2d, robotSnapshot);
 Shape expectedShape = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, 0, 10.0);
 assertEquals(expectedShape, g2d.getShape());
 }

 @Test
 public void testPaintTurret() {
 robotSnapshot = new RobotSnapshot();
 renderer.paintTurret(g2d, robotSnapshot);
 Line2D.Double expectedLine = new Line2D.Double(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0);
 assertEquals(expectedLine, g2d.getShape());
 }

 @Test
 public void testPaintSelection() {
 robotSnapshot = new RobotSnapshot();
 renderer.paintSelection(g2d, robotSnapshot);
 Shape expectedShape = new Ellipse2D.Double();
 expectedShape.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);
 assertEquals(expectedShape, g2d.getShape());
 }

 @Test
 public void testPaintSelectionOutsideRobot() {
 robotSnapshot.setX(-100);
 renderer.paintSelection(g2d, robotSnapshot);
 Shape expectedShape = new Ellipse2D.Double();
 expectedShape.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), 0, 0);
 assertEquals(expectedShape, g2d.getShape());
 }

}