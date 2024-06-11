import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.measures.Distance;
import net.virtualinfinity.atrobots.measures.AngularMeasure;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

public class RobotRendererTest {

 private RobotRenderer renderer;
 private Graphics2D graphics;
 private RobotSnapshot snapshot;

 @Before
 public void setUp() {
 renderer = new RobotRenderer();
 graphics = new MockGraphics2D();
 snapshot = new RobotSnapshot(1, new Distance(0, 0), new AngularMeasure(0), 100, 50, 50, AngularMeasure.fromDegrees(0), 1.0f, new boolean[3], new boolean[3], new boolean[3], false);
 }

 @Test
 public void testRenderWithDeadRobot() {
 renderer.render(graphics, snapshot, new HashSet<Integer>());
 assertTrue(graphics.getColor().equals(Color.orange.darker().darker()));
 }

 @Test
 public void testRenderWithLiveRobot() {
 renderer.render(graphics, snapshot, new HashSet<Integer>());
 assertTrue(graphics.getColor().equals(Color.red));
 }

 @Test
 public void testPaintSelection() {
 final Set<Integer> selectedRobotIds = new HashSet<Integer>();
 selectedRobotIds.add(snapshot.getId());
 renderer.paintSelection(graphics, snapshot);
 Rectangle2D rectangle = graphics.getClipBounds();
 assertTrue(rectangle.getWidth() > 0 && rectangle.getHeight() > 0);
 }

 @Test
 public void testPaintShield() {
 renderer.paintShield(graphics, snapshot);
 final Shape shape = graphics.getClip();
 assertTrue(shape instanceof Ellipse2D.Double);
 }

 @Test
 public void testPaintName() {
 renderer.paintName(graphics, snapshot);
 String name = graphics.getFontMetrics().getStringBounds(snapshot.getName(), graphics).toString();
 assertEquals(snapshot.getName(), name);
 }

 @Test
 public void testPaintHeat() {
 renderer.paintHeat(graphics, snapshot);
 final Shape shape = graphics.getClip();
 assertTrue(shape instanceof Rectangle2D.Double);
 }

 @Test
 public void testPaintArmor() {
 renderer.paintArmor(graphics, snapshot);
 final Shape shape = graphics.getClip();
 assertTrue(shape instanceof Rectangle2D.Double);
 }

 @Test
 public void testPaintTurret() {
 renderer.paintTurret(graphics, snapshot);
 final Shape shape = graphics.getClip();
 assertTrue(shape instanceof Line2D.Double);
 }

 @Test
 public void testPaintBody() {
 renderer.paintBody(graphics, snapshot);
 final Shape shape = graphics.getClip();
 assertTrue(shape instanceof GeneralPath);
 }
}