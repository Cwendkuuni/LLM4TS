import org.junit.Before;
import org.junit.Test;
import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.measures.Vector2D;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import static org.junit.Assert.*;

public class RobotRendererTest {

 private RobotRenderer renderer;
 private Graphics2D g2d;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 renderer = new RobotRenderer();
 g2d = mock(Graphics2D.class);
 robotSnapshot = new RobotSnapshot(new Vector2D(10.0, 10.0), "TestRobot", 50.0, 100.0, 50.0, false, false, false, false);
 }

 @Test
 public void testRender_RobotIsNotDead_ShouldRenderRobot() {
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 verify(g2d, times(7)).draw(any(Shape.class));
 verify(g2d, times(1)).fill(any(Shape.class));
 }

 @Test
 public void testRender_RobotIsDead_ShouldNotRenderRobot() {
 renderer.setRenderDead(false);
 robotSnapshot.setDead(true);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 verify(g2d, never()).draw(any(Shape.class));
 verify(g2d, never()).fill(any(Shape.class));
 }

 @Test
 public void testPaintSelection_SelectedRobot_ShouldPaintSelection() {
 renderer.paintSelection(g2d, robotSnapshot);
 verify(g2d, times(1)).setPaint(anyColor());
 verify(g2d, times(1)).fill(any(Ellipse2D.class));
 }

 @Test
 public void testPaintShield_ActiveShield_ShouldPaintShield() {
 robotSnapshot.setActiveShield(true);
 renderer.paintShield(g2d, robotSnapshot);
 verify(g2d, times(1)).setPaint(anyGradientPaint());
 verify(g2d, times(1)).fill(any(Ellipse2D.class));
 }

 @Test
 public void testPaintShield_InactiveShield_ShouldNotPaintShield() {
 robotSnapshot.setActiveShield(false);
 renderer.paintShield(g2d, robotSnapshot);
 verify(g2d, never()).setPaint(anyGradientPaint());
 verify(g2d, never()).fill(any(Ellipse2D.class));
 }

 @Test
 public void testPaintName_ShouldPaintName() {
 renderer.paintName(g2d, robotSnapshot);
 verify(g2d, times(1)).setPaint(Color.yellow);
 verify(g2d, times(1)).drawString(robotSnapshot.getName(), anyDouble(), anyDouble());
 }

 @Test
 public void testPaintHeat_RobotIsNotDead_ShouldPaintHeatBar() {
 robotSnapshot.setTemperature(new Temperature(50.0));
 renderer.paintHeat(g2d, robotSnapshot);
 verify(g2d, times(1)).setPaint(new GradientPaint(anyDouble(), anyDouble(), RobotRenderer.HEAT_COLOR1, anyDouble(), anyDouble(), RobotRenderer.HEAT_COLOR2));
 verify(g2d, times(1)).fill(any(Rectangle2D.class));
 }

 @Test
 public void testPaintArmor_ShouldPaintArmorBar() {
 renderer.paintArmor(g2d, robotSnapshot);
 verify(g2d, times(1)).setPaint(Color.color(0.0f, 0.0f, 1.0f, 0.6f));
 verify(g2d, times(1)).fill(any(Rectangle2D.class));
 }

 @Test
 public void testPaintTurret_ShouldPaintTurretLine() {
 renderer.paintTurret(g2d, robotSnapshot);
 verify(g2d, times(1)).setPaint(Color.white);
 verify(g2d, times(1)).draw(any(Line2D.class));
 }

 @Test
 public void testPaintBody_RobotIsNotDead_ShouldPaintRobotBody() {
 renderer.paintBody(g2d, robotSnapshot);
 verify(g2d, times(1)).setPaint(Color.red);
 verify(g2d, times(1)).fill(any(GeneralPath.class));
 }

 @Test
 public void testPaintBody_RobotIsDead_ShouldPaintRobotBodyWithDifferentColorAndStroke() {
 renderer.setRenderDead(false);
 robotSnapshot.setDead(true);
 renderer.paintBody(g2d, robotSnapshot);
 verify(g2d, times(1)).setPaint(Color.orange.darker().darker());
 verify(g2d, times(1)).setStroke(new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f));
 }
}