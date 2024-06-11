import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.util.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {

 private Graphics2D g2d;
 private RobotRenderer renderer;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 g2d = new StubGraphics2D();
 renderer = new RobotRenderer();
 robotSnapshot = new RobotSnapshot(1, new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), new Temperature(100), new Armor(100), new Angle(0), new Angle(0), new Angle(0), false, false, false, false);
 }

 @Test
 public void testRender() {
 renderer.render(g2d, robotSnapshot, Collections.emptySet());

 // Test if body is painted correctly
 Shape body = g2d.getShape();
 GeneralPath expectedBody = createExpectedBodyShape();
 assertEquals(expectedBody, body);

 // Test if shield is painted correctly when fillShield is true
 renderer.setFillShield(true);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 Rectangle2D shield = g2d.getClipBounds();
 assertEquals(new Rectangle2D.Double(robotSnapshot.getX() - 7.5, robotSnapshot.getY() + 7.5, 30.0, 30.0), shield);

 // Test if shield is not painted when fillShield is false
 renderer.setFillShield(false);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertNull(g2d.getClipBounds());

 // Test if name is painted correctly
 renderer.setShowName(true);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 String name = g2d.getFontMetrics().getStringBounds(robotSnapshot.getName(), g2d).getSubstring(0, g2d.getFontMetrics().getStringBounds(robotSnapshot.getName(), g2d).length() / 2);
 assertEquals("R", name);

 // Test if heat is painted correctly
 renderer.setShowStatusBars(true);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 Rectangle2D heatBoundingBox = g2d.getClipBounds();
 assertEquals(new Rectangle2D.Double(robotSnapshot.getX() - 45.0, robotSnapshot.getY() + 30.0, 90.0, 10.0), heatBoundingBox);

 // Test if armor is painted correctly
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 Shape armor = g2d.getClip();
 assertEquals(new Rectangle2D.Double(robotSnapshot.getX() - 45.0, robotSnapshot.getY() + 15.0, robotSnapshot.getArmor(), 10.0), armor);
 }

 @Test
 public void testIsRenderDead() {
 renderer.setRenderDead(false);
 assertFalse(renderer.isRenderDead());

 renderer.setRenderDead(true);
 assertTrue(renderer.isRenderDead());
 }

 @Test
 public void testSetRenderDead() {
 renderer.setRenderDead(false);
 renderer.isRenderDead(); // should return false

 renderer.setRenderDead(true);
 renderer.isRenderDead(); // should return true
 }

 @Test
 public void testIsShowStatusBars() {
 renderer.setShowStatusBars(false);
 assertFalse(renderer.isShowStatusBars());

 renderer.setShowStatusBars(true);
 assertTrue(renderer.isShowStatusBars());
 }

 @Test
 public void testSetShowStatusBars() {
 renderer.setShowStatusBars(false);
 renderer.isShowStatusBars(); // should return false

 renderer.setShowStatusBars(true);
 renderer.isShowStatusBars(); // should return true
 }

 @Test
 public void testIsShowName() {
 renderer.setShowName(false);
 assertFalse(renderer.isShowName());

 renderer.setShowName(true);
 assertTrue(renderer.isShowName());
 }

 @Test
 public void testSetShowName() {
 renderer.setShowName(false);
 renderer.isShowName(); // should return false

 renderer.setShowName(true);
 renderer.isShowName(); // should return true
 }

 @Test
 public void testIsFillShield() {
 renderer.setFillShield(false);
 assertFalse(renderer.isFillShield());

 renderer.setFillShield(true);
 assertTrue(renderer.isFillShield());
 }

 @Test
 public void testSetFillShield() {
 renderer.setFillShield(false);
 renderer.isFillShield(); // should return false

 renderer.setFillShield(true);
 renderer.isFillShield(); // should return true
 }

 private GeneralPath createExpectedBodyShape() {
 GeneralPath path = new GeneralPath();
 path.moveTo(15.0, 15.0);
 path.lineTo(Math.cos(Math.toRadians(robotSnapshot.getHeading().getDegrees())) * 9.0 + robotSnapshot.getX(), Math.sin(Math.toRadians(robotSnapshot.getHeading().getDegrees())) * 9.0 + robotSnapshot.getY());
 path.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 path.lineTo(Math.cos(Math.toRadians(robotSnapshot.getHeading().getCounterClockwise(RelativeAngle.fromBygrees(160)).getDegrees())) * 9.0 + robotSnapshot.getX(), Math.sin(Math.toRadians(robotSnapshot.getHeading().getCounterClockwise(RelativeAngle.fromBygrees(160)).getDegrees())) * 9.0 + robotSnapshot.getY());
 path.closePath();
 return path;
 }
}