import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.measures.Distance;
import net.virtualinfinity.atrobots.measures.Direction;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import java.awt.*;
import java.awt.geom.*;

public class RobotRendererTest {

 private RobotRenderer renderer;
 private Graphics2D g2d;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 renderer = new RobotRenderer();
 g2d = new StubGraphics2D();
 robotSnapshot = new RobotSnapshot(1, new Distance(100.0), new Distance(100.0), 10.0, new Distance(50.0), new Distance(50.0), new Direction(Math.PI / 2), 1.0, 0.5, 100, false, false, false, false, new double[] { 0.5, 0.5 }, 100, 50, false, new Distance(50.0), 1.0, 1.0);
 }

 @Test
 public void testRender() {
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 // Add assertions for the rendered image here
 }

 @Test
 public void testIsRenderDead() {
 renderer.setRenderDead(false);
 assertFalse(renderer.isRenderDead());
 renderer.setRenderDead(true);
 assertTrue(renderer.isRenderDead());
 }

 @Test
 public void testSetShowStatusBars() {
 renderer.setShowStatusBars(false);
 assertFalse(renderer.isShowStatusBars());
 renderer.setShowStatusBars(true);
 assertTrue(renderer.isShowStatusBars());
 }

 @Test
 public void testSetShowName() {
 renderer.setShowName(false);
 assertFalse(renderer.isShowName());
 renderer.setShowName(true);
 assertTrue(renderer.isShowName());
 }

 @Test
 public void testSetFillShield() {
 renderer.setFillShield(false);
 assertFalse(renderer.isFillShield());
 renderer.setFillShield(true);
 assertTrue(renderer.isFillShield());
 }

 @Test
 public void testPaintSelection() {
 final Graphics2D g2dSpy = createSpy(Graphics2D.class, new Graphics2D() {
 @Override
 public void fill(Shape shape) {
 renderer.paintSelection(this, robotSnapshot);
 verify(this).setPaint(argThat(isA(Color.class)).andReturn(argThat(isA(Color.class))));
 verify(this).fill(argThat(isA(Ellipse2D.Double.class)));
 }
 });
 renderer.render(g2dSpy, robotSnapshot, Collections.singleton(robotSnapshot.getId()));
 }

 @Test
 public void testPaintShield() {
 final Graphics2D g2dSpy = createSpy(Graphics2D.class, new Graphics2D() {
 @Override
 public void setPaint(Paint paint) {
 renderer.paintShield(this, robotSnapshot);
 verify(this).setPaint(argThat(isA(RadialGradientPaint.class)));
 verify(this).fill(argThat(isA(Ellipse2D.Double.class)));
 verify(this).fill(argThat(isA(Ellipse2D.Double.class)));
 }
 });
 renderer.setRenderDead(false);
 renderer.render(g2dSpy, robotSnapshot, Collections.singleton(robotSnapshot.getId()));
 }

 @Test
 public void testPaintName() {
 final Graphics2D g2dSpy = createSpy(Graphics2D.class, new Graphics2D() {
 @Override
 public void drawString(String string, float x, float y) {
 renderer.paintName(this, robotSnapshot);
 verify(this).setPaint(argThat(isA(Color.class)));
 verify(this).drawString(eq(robotSnapshot.getName()), argThat(isA(Float.class)), argThat(isA(Float.class)));
 }
 });
 renderer.render(g2dSpy, robotSnapshot, Collections.emptySet());
 }

 @Test
 public void testPaintHeat() {
 final Graphics2D g2dSpy = createSpy(Graphics2D.class, new Graphics2D() {
 @Override
 public void fill(Shape shape) {
 renderer.paintHeat(this, robotSnapshot);
 verify(this).setPaint(argThat(isA(GradientPaint.class)));
 verify(this).fill(argThat(isA(Rectangle2D.Double.class)));
 verify(this).draw(argThat(isA(Rectangle2D.Double.class)));
 }
 });
 renderer.render(g2dSpy, robotSnapshot, Collections.emptySet());
 }

 @Test
 public void testPaintArmor() {
 final Graphics2D g2dSpy = createSpy(Graphics2D.class, new Graphics2D() {
 @Override
 public void fill(Shape shape) {
 renderer.paintArmor(this, robotSnapshot);
 verify(this).setPaint(argThat(isA(Color.class).or(isA(Color.class))));
 verify(this).fill(argThat(isA(Rectangle2D.Double.class)));
 }
 });
 renderer.render(g2dSpy, robotSnapshot, Collections.emptySet());
 }

 @Test
 public void testPaintTurret() {
 final Graphics2D g2dSpy = createSpy(Graphics2D.class, new Graphics2D() {
 @Override
 public void draw(Shape shape) {
 renderer.paintTurret(this, robotSnapshot);
 verify(this).setPaint(argThat(isA(Color.class)));
 verify(this).draw(argThat(isA(Line2D.Double.class)));
 }
 });
 renderer.render(g2dSpy, robotSnapshot, Collections.emptySet());
 }

 @Test
 public void testPaintBody() {
 final Graphics2D g2dSpy = createSpy(Graphics2D.class, new Graphics2D() {
 @Override
 public void fill(Shape shape) {
 renderer.paintBody(this, robotSnapshot);
 verify(this).setPaint(argThat(isA(Color.class).or(isA(Color.class))));
 verify(this).fill(argThat(isA(GeneralPath.class)));
 }
 });
 renderer.render(g2dSpy, robotSnapshot, Collections.emptySet());
 }
}