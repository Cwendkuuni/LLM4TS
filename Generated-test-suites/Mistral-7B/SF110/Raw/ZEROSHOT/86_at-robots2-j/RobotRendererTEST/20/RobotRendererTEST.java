import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {

 private Graphics2D g2d;
 private RobotRenderer renderer;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setup() {
 g2d = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).createGraphics();
 renderer = new RobotRenderer();
 robotSnapshot = new RobotSnapshot(1, 1.0, 2.0, new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), 10.0, 50.0, 10.0, true, true, false, false, false, null);
 }

 @Test
 public void testRender() {
 RobotSnapshot renderedRobotSnapshot = new RobotSnapshot(1, 1.0, 2.0, new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), 10.0, 50.0, 10.0, true, true, false, false, false, null);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertFalse(renderer.isRenderDead());
 assertEquals(robotSnapshot.getX(), renderedRobotSnapshot.getX(), 0.01);
 assertEquals(robotSnapshot.getY(), renderedRobotSnapshot.getY(), 0.01);

 // Test painting body
 assertEquals(Color.RED, g2d.getColor());
 Path2D bodyPath = (Path2D) g2d.getPath();
 assertEquals(3, bodyPath.getWindingRule());
 Path2D expectedBodyPath = new Path2D.Double();
 expectedBodyPath.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 expectedBodyPath.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedBodyPath.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 expectedBodyPath.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedBodyPath.closePath();
 assertEquals(expectedBodyPath, bodyPath);

 // Test painting shield
 if (renderer.isFillShield()) {
 Color[] colors = { Color.decode("#8060B4"), Color.decode("#000059"), Color.WHITE };
 GradientPaint gradient = (GradientPaint) g2d.getPaint();
 assertEquals(colors[0], gradient.getColorAt(0.0f));
 assertEquals(colors[1], gradient.getColorAt(0.75f));
 assertEquals(colors[2], gradient.getColorAt(1.0f));
 }

 // Test painting name
 assertEquals(Color.YELLOW, g2d.getColor());
 String name = "Test name";
 g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
 FontMetrics metrics = g2d.getFontMetrics();
 float x = (float)(robotSnapshot.getX() - metrics.getStringBounds(name, g2d).getWidth() / 2.0);
 float y = (float)(robotSnapshot.getY() - 10.0);
 assertEquals(name, g2d.getFont().getFontName());
 assertEquals(x, g2d.getFontMetrics().stringWidth(name), 0.01);
 assertEquals(y, g2d.getFontMetrics().getAscent(), 0.01);
 }

 @Test
 public void testRenderDeadRobot() {
 renderer.setRenderDead(true);
 RobotSnapshot renderedRobotSnapshot = new RobotSnapshot(1, 1.0, 2.0, new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), 10.0, 50.0, 10.0, true, true, false, false, false, null);
 renderer.render(g2d, robotSnapshot, Collections.emptySet());
 assertTrue(renderer.isRenderDead());
 assertNull(g2d.getColor());
 }

 @Test
 public void testRenderRobotWithSelectedId() {
 Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(robotSnapshot.getId());
 renderer.render(g2d, robotSnapshot, selectedRobotIds);

 Path2D selectionPath = (Path2D) g2d.getPath();
 assertEquals(Path2D.Float.WIND_EVEN_ODD, selectionPath.getWindingRule());
 Ellipse2D selectionEllipse = new Ellipse2D.Double();
 selectionEllipse.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);
 assertEquals(selectionEllipse, selectionPath);
 }
}