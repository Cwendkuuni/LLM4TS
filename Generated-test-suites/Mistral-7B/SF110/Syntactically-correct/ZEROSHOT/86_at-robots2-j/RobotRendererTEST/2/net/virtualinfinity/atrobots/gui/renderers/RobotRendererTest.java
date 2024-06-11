import org.junit.Test;
import org.junit.Assert.*;
import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.measures.Length;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import java.awt.*;

public class RobotRendererTest {

 private RobotSnapshot getTestRobotSnapshot() {
 return new RobotSnapshot(1, new Position(0, 0), new Velocity(0, 0), new AngularVelocity(0), new Angle(0), new Temperature(25), new Armor(100), new Energy(100), new Vector3D(1, 1, 0), new Vector2D(0, 0), new Angle(0), new Angle(0), new Angle(0), true, false, false, false, new PositionVector(new Vector3D(0, 0, 0)));
 }

 @Test
 public void testRender() {
 RobotRenderer renderer = new RobotRenderer();
 Graphics2D g2d = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
 RobotSnapshot robotSnapshot = getTestRobotSnapshot();
 Set<Integer> selectedRobotIds = new HashSet<>();
 renderer.render(g2d, robotSnapshot, selectedRobotIds);

 // Test painting body
 Shape expectedBody = new GeneralPath();
 expectedBody.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 expectedBody.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedBody.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 expectedBody.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedBody.closePath();

 Shape actualBody = g2d.getClip();
 assertTrue(expectedBody.equals(actualBody));

 // Test painting shield
 if (renderer.isFillShield()) {
 Color expectedColor1 = new Color(0.5f, 0.5f, 0.0f, 0.8f);
 Color expectedColor2 = new Color(0.0f, 0.0f, 0.5f, 0.1f);
 Color actualColor1 = new Color(expectedBody.getBounds2D().getCenterX(), expectedBody.getBounds2D().getCenterY(), expectedColor1.getRGB());
 Color actualColor2 = new Color(expectedBody.getBounds2D().getMinX() + 15.0, expectedBody.getBounds2D().getMinY(), expectedColor2.getRGB());
 assertSame(expectedColor1, actualColor1);
 assertSame(expectedColor2, actualColor2);
 } else {
 Color expectedColor = new Color(0.8f, 0.6f, 1.0f);
 Color actualColor = g2d.getColor();
 assertSame(expectedColor, actualColor);
 }

 // Test painting name
 String expectedName = "TestRobot";
 String actualName = g2d.getFontMetrics().getStringBounds(expectedName, g2d).getCenterX() > 0 ? g2d.getFontMetrics().getStringBounds(expectedName, g2d).getString() : null;
 assertEquals(expectedName, actualName);
 }

 @Test
 public void testRenderWithDeadRobot() {
 RobotRenderer renderer = new RobotRenderer();
 Graphics2D g2d = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
 RobotSnapshot robotSnapshot = getTestRobotSnapshot();
 robotSnapshot.setDead(true);
 Set<Integer> selectedRobotIds = new HashSet<>();
 renderer.render(g2d, robotSnapshot, selectedRobotIds);

 // Test not painting body
 Shape expectedBody = new GeneralPath();
 expectedBody.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 expectedBody.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedBody.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 expectedBody.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedBody.closePath();

 Shape actualBody = null;
 try {
 actualBody = g2d.getClip();
 } catch (IllegalArgumentException e) {
 // Clip is null when not painting anything
 }

 assertNull(actualBody);
 }

 @Test
 public void testRenderWithSelectedRobot() {
 RobotRenderer renderer = new RobotRenderer();
 Graphics2D g2d = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
 RobotSnapshot robotSnapshot = getTestRobotSnapshot();
 Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(robotSnapshot.getId());
 renderer.render(g2d, robotSnapshot, selectedRobotIds);

 // Test painting selection
 Shape expectedSelection = new Ellipse2D.Double(robotSnapshot.getX() - 15.0, robotSnapshot.getY() - 15.0, 30.0, 30.0);
 Shape actualSelection = g2d.getClip();
 assertTrue(expectedSelection.equals(actualSelection));
 }

 @Test
 public void testSetRenderDead() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setRenderDead(false);
 RobotSnapshot robotSnapshot = getTestRobotSnapshot();
 Graphics2D g2d = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
 renderer.render(g2d, robotSnapshot, new HashSet<>());
 assertTrue(renderer.isRenderDead());

 renderer.setRenderDead(true);
 robotSnapshot.setDead(true);
 renderer.render(g2d, robotSnapshot, new HashSet<>());
 assertFalse(renderer.isRenderDead());
 }

 @Test
 public void testSetShowStatusBars() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setShowStatusBars(false);
 Graphics2D g2d = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
 RobotSnapshot robotSnapshot = getTestRobotSnapshot();
 Set<Integer> selectedRobotIds = new HashSet<>();
 renderer.render(g2d, robotSnapshot, selectedRobotIds);
 assertFalse(renderer.isShowStatusBars());

 renderer.setShowStatusBars(true);
 renderer.render(g2d, robotSnapshot, selectedRobotIds);
 assertTrue(renderer.isShowStatusBars());
 }

 @Test
 public void testSetShowName() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setShowName(false);
 Graphics2D g2d = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
 RobotSnapshot robotSnapshot = getTestRobotSnapshot();
 Set<Integer> selectedRobotIds = new HashSet<>();
 renderer.render(g2d, robotSnapshot, selectedRobotIds);
 assertFalse(g2d.getFontMetrics().getStringBounds("", g2d).getWidth() > 0);

 renderer.setShowName(true);
 renderer.render(g2d, robotSnapshot, selectedRobotIds);
 assertTrue(g2d.getFontMetrics().getStringBounds("", g2d).getWidth() > 0);
 }

 @Test
 public void testSetFillShield() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setFillShield(false);
 Graphics2D g2d = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
 RobotSnapshot robotSnapshot = getTestRobotSnapshot();
 Set<Integer> selectedRobotIds = new HashSet<>();
 renderer.render(g2d, robotSnapshot, selectedRobotIds);
 assertFalse(renderer.isFillShield());

 renderer.setFillShield(true);
 renderer.render(g2d, robotSnapshot, selectedRobotIds);
 assertTrue(renderer.isFillShield());
 }
}