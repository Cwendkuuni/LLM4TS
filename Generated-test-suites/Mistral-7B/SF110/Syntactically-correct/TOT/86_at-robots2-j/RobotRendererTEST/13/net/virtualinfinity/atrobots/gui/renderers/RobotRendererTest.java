import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.measures.Measure2D;
import net.virtualinfinity.atrobots.measures.PositionVector;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshotImpl;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RobotRendererTest {

 private RobotRenderer renderer;
 private Graphics2D g2d;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 renderer = new RobotRenderer();
 g2d = createGraphics2D();
 robotSnapshot = new RobotSnapshotImpl(1, new PositionVector(10.0, 10.0), new Measure2D(50.0), new Measure2D(15.0), 50.0, 15.0, true, false, false, true, true, true, Color.RED, 0.0);
 }

 @Test
 public void testRenderMethod_RobotIsDead_RenderMethodIsNotCalled() {
 renderer.setRenderDead(true);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertEquals(0, g2d.getPaintCount());
 }

 @Test
 public void testRenderMethod_RobotIsAlive_MethodsAreCalledInCorrectOrder() {
 renderer.setRenderDead(false);
 robotSnapshot.setDead(false);
 Set<Integer> selectedRobotIds = new HashSet<>();
 renderer.render(g2d, robotSnapshot, selectedRobotIds);

 verify(g2d, times(1)).setPaint(Color.RED);
 verify(g2d, times(1)).fillPath(any(Path2D.class));
 verify(g2d, times(1)).drawPath(any(Path2D.class));
 verify(g2d, times(1)).setPaint(Color.YELLOW);
 verify(g2d, times(1)).drawString(eq(robotSnapshot.getName()), argThat(isA(Float.class)), argThat(isA(Float.class)));
 verify(g2d, times(1)).setPaint(new Color(1.0f, 0.0f, 0.0f, 0.1f));
 verify(g2d, times(1)).fill(any(Path2D.class));
 verify(g2d, times(1)).fill(any(Path2D.class));
 verify(g2d, times(1)).draw(any(Path2D.class));
 }

 @Test
 public void testRenderMethod_SelectedRobot_SelectionIsDrawn() {
 renderer.setRenderDead(false);
 robotSnapshot.setDead(false);
 Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(robotSnapshot.getId());
 renderer.render(g2d, robotSnapshot, selectedRobotIds);

 verify(g2d, times(1)).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
 verify(g2d, times(1)).fill(any(Ellipse2D.Double.class));
 }

 @Test
 public void testIsRenderDead_DefaultIsTrue() {
 assertTrue(renderer.isRenderDead());
 }

 @Test
 public void testIsRenderDead_CanBeSetToFalse() {
 renderer.setRenderDead(false);
 assertFalse(renderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars_DefaultIsTrue() {
 assertTrue(renderer.isShowStatusBars());
 }

 @Test
 public void testIsShowStatusBars_CanBeSetToFalse() {
 renderer.setShowStatusBars(false);
 assertFalse(renderer.isShowStatusBars());
 }

 @Test
 public void testIsShowName_DefaultIsTrue() {
 assertTrue(renderer.isShowName());
 }

 @Test
 public void testIsShowName_CanBeSetToFalse() {
 renderer.setShowName(false);
 assertFalse(renderer.isShowName());
 }

 @Test
 public void testIsFillShield_DefaultIsTrue() {
 assertTrue(renderer.isFillShield());
 }

 @Test
 public void testIsFillShield_CanBeSetToFalse() {
 renderer.setFillShield(false);
 assertFalse(renderer.isFillShield());
 }
}