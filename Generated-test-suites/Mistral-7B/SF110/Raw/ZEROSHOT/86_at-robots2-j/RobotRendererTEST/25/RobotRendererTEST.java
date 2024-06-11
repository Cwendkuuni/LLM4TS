import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.virtualinfinity.atrobots.gui.renderers.RobotSnapshot;
import net.virtualinfinity.atrobots.measures.Length;
import net.virtualinfinity.atrobots.measures.Position;
import java.awt.*;
import java.util.*;

public class RobotRendererTest {

 private Graphics2D g2d;
 private RobotRenderer robotRenderer;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 g2d = Mockito.mock(Graphics2D.class);
 robotRenderer = new RobotRenderer();
 robotSnapshot = new RobotSnapshot(new Position(10.0, 10.0), new Length(50.0), "TestRobot", 1, 5.0, 10.0, 20.0, 15.0, 25.0, true, true, false, false, false, new Length(50.0), null, null);
 }

 @Test
 public void testRenderWithDeadRobot() {
 robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());
 verify(g2d, never()).fill(any(Shape.class));
 }

 @Test
 public void testRenderWithActiveRobot() {
 robotRenderer.setShowStatusBars(true);
 robotRenderer.setRenderDead(false);
 robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

 verify(g2d, times(1)).fill(any(Shape.class));

 // Test painting body
 Shape bodyShape = Mockito.capture(g2d).getLastDrawnShape();
 Path2D expectedBodyPath = new Path2D.Double();
 expectedBodyPath.moveTo(10.0 + Math.cos(Math.toRadians(robotSnapshot.getHeading().getBygreesDegrees())) * 15.0, 10.0 + Math.sin(Math.toRadians(robotSnapshot.getHeading().getBygreesDegrees())) * 15.0);
 expectedBodyPath.lineTo(robotSnapshot.getX() - Math.cos(Math.toRadians(robotSnapshot.getHeading().getCounterClockwise(RelativeAngle.fromBygrees(160)).getBygreesDegrees())) * 9.0, robotSnapshot.getY() + Math.sin(Math.toRadians(robotSnapshot.getHeading().getCounterClockwise(RelativeAngle.fromBygrees(160)).getBygreesDegrees())) * 9.0);
 expectedBodyPath.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 expectedBodyPath.lineTo(robotSnapshot.getX() + Math.cos(Math.toRadians(robotSnapshot.getHeading().getClockwise(RelativeAngle.fromBygrees(160)).getBygreesDegrees())) * 9.0, robotSnapshot.getY() + Math.sin(Math.toRadians(robotSnapshot.getHeading().getClockwise(RelativeAngle.fromBygrees(160)).getBygreesDegrees())) * 9.0);
 expectedBodyPath.closePath();
 Path2D actualBodyPath = (Path2D) bodyShape;
 assertEquals(expectedBodyPath, actualBodyPath);

 // Test painting shield
 if (robotSnapshot.isActiveShield()) {
 Ellipse2D shieldEllipse = Mockito.capture(g2d).getLastDrawnShape();
 Ellipse2D expectedShieldEllipse = new Ellipse2D.Double(robotSnapshot.getX() - 7.5, robotSnapshot.getY() + 7.5, 15.0, 15.0);
 Ellipse2D actualShieldEllipse = (Ellipse2D) shieldEllipse;
 assertEquals(expectedShieldEllipse, actualShieldEllipse);
 }

 // Test painting name
 TextComponent nameTextComponent = Mockito.capture(g2d).getLastComponent(TextComponent.class);
 String expectedNameText = robotSnapshot.getName();
 String actualNameText = ((JTextComponent) nameTextComponent).getText();
 assertEquals(expectedNameText, actualNameText);

 // Test painting armor and heat bars
 Rectangle2D armorRectangle = Mockito.capture(g2d).getLastDrawnShape();
 Path2D expectedArmorPath = new Path2D.Double();
 expectedArmorPath.moveTo(robotSnapshot.getX() - 65.0, robotSnapshot.getY() + 15.0);
 expectedArmorPath.lineTo(robotSnapshot.getX() - 65.0, robotSnapshot.getY() + 25.0);
 expectedArmorPath.lineTo(robotSnapshot.getX() - 55.0, robotSnapshot.getY() + 25.0);
 expectedArmorPath.lineTo(robotSnapshot.getX() - 55.0, robotSnapshot.getY() + 15.0);
 expectedArmorPath.closePath();
 Path2D actualArmorPath = (Path2D) armorRectangle;
 assertEquals(expectedArmorPath, actualArmorPath);

 // Test painting heat bars
 Rectangle2D heatRectangle = Mockito.capture(g2d).getLastDrawnShape();
 Rectangle2D expectedHeatRectangle = new Rectangle2D.Double(robotSnapshot.getX() - 60.0, robotSnapshot.getY() + 30.0, 120.0, 10.0);
 Rectangle2D actualHeatRectangle = (Rectangle2D) heatRectangle;
 assertEquals(expectedHeatRectangle, actualHeatRectangle);
 }

 @Test
 public void testRenderWithSelectedRobot() {
 robotRenderer.setShowStatusBars(true);
 robotRenderer.setRenderDead(false);
 Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(robotSnapshot.getId());
 robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

 Ellipse2D selectionEllipse = Mockito.capture(g2d).getLastDrawnShape();
 Ellipse2D expectedSelectionEllipse = new Ellipse2D.Double(robotSnapshot.getX() - 15.0, robotSnapshot.getY() - 15.0, 30.0, 30.0);
 Ellipse2D actualSelectionEllipse = (Ellipse2D) selectionEllipse;
 assertEquals(expectedSelectionEllipse, actualSelectionEllipse);
 }

 // Other test methods for individual methods in the class, such as testPaintSelection(), testPaintShield(), etc.
}