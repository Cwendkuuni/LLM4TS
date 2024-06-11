import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.measures.Length;
import net.virtualinfinity.atrobots.measures.Vector2D;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;

public class RobotRendererTest {

 private RobotRenderer renderer;
 private Graphics2D graphics2D;
 private RobotSnapshot robotSnapshot;
 private Set<Integer> selectedRobotIds;

 @Before
 public void setUp() {
 robotSnapshot = new RobotSnapshot(new Vector2D(10, 10), new Length(30), new Length(30), new Vector2D(0, 0), new Vector2D(1, 0), 10, 50, 1.0f, true, true, 5.0, new Length(50), new Length(10), new Length(100), false, 0.0f, true, new Length(10), Color.red);
 graphics2D = mock(Graphics2D.class);
 selectedRobotIds = new HashSet<Integer>(Arrays.asList(1));
 renderer = new RobotRenderer();
 }

 @Test
 public void testRender_RobotNotDead_ShowAllFeatures() {
 when(graphics2D.getColor()).thenReturn(Color.BLACK);
 renderer.render(graphics2D, robotSnapshot, selectedRobotIds);

 // Test body painting
 Path2D bodyPath = new Path2D.Double();
 bodyPath.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 bodyPath.lineTo(robotSnapshot.getX() + Math.cos(Math.toRadians(robotSnapshot.getHeading().getDegrees())) * 9.0, robotSnapshot.getY() + Math.sin(Math.toRadians(robotSnapshot.getHeading().getDegrees())) * 9.0);
 bodyPath.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 bodyPath.lineTo(robotSnapshot.getX() + Math.cos(Math.toRadians(robotSnapshot.getHeading().getDegrees() + 180.0)) * 9.0, robotSnapshot.getY() + Math.sin(Math.toRadians(robotSnapshot.getHeading().getDegrees() + 180.0)) * 9.0);
 bodyPath.closePath();

 Mockito.verify(graphics2D, Mockito.times(1)).setPaint(Mockito.eq(Color.red));
 Mockito.verify(graphics2D, Mockito.times(1)).fill(bodyPath);
 Mockito.verify(graphics2D, Mockito.times(1)).setPaint(Mockito.eq(Color.white));
 Mockito.verify(graphics2D, Mockito.times(1)).draw(new Line2D.Double(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + Math.cos(Math.toRadians(robotSnapshot.getHeading().getDegrees())) * 5.0, robotSnapshot.getY() + Math.sin(Math.toRadians(robotSnapshot.getHeading().getDegrees())) * 5.0));

 // Test shield painting
 when(graphics2D.getColor()).thenReturn(Color.BLACK);
 Path2D shieldPath = new Ellipse2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, robotSnapshot.getArmor().getValue() + 100.0, 10.0);
 Mockito.verify(graphics2D, Mockito.times(1)).setPaint(new GradientPaint(robotSnapshot.getX(), robotSnapshot.getY() + 35.0, RobotRenderer.HEAT_COLOR1, robotSnapshot.getX(), robotSnapshot.getY() + 35.0, RobotRenderer.HEAT_COLOR2));
 Mockito.verify(graphics2D, Mockito.times(1)).fill(shieldPath);
 Mockito.verify(graphics2D, Mockito.times(1)).fill(shieldPath);

 // Test name painting
 when(graphics2D.getColor()).thenReturn(Color.YELLOW);
 Mockito.verify(graphics2D, Mockito.times(1)).drawString(robotSnapshot.getName(), (float)(robotSnapshot.getX() - graphics2D.getFontMetrics().getStringBounds(robotSnapshot.getName(), graphics2D).getWidth() / 2.0), (float)(robotSnapshot.getY() - 10.0));
 }

 @Test
 public void testRender_RobotIsDead() {
 when(graphics2D.getColor()).thenReturn(Color.BLACK);
 renderer.render(graphics2D, robotSnapshot, selectedRobotIds);

 // Test body painting
 Mockito.verify(graphics2D, Mockito.times(1)).setStroke(Mockito.eq(new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f)));
 Mockito.verify(graphics2D, Mockito.times(1)).setPaint(Color.ORANGE.darker().darker());
 Mockito.verify(graphics2D, Mockito.times(1)).draw(new Path2D.Double());
 }

 @Test
 public void testRender_SelectedRobot() {
 when(graphics2D.getColor()).thenReturn(Color.BLACK);
 renderer.render(graphics2D, robotSnapshot, selectedRobotIds);

 // Test selection painting
 Ellipse2D selectionEllipse = new Ellipse2D.Double();
 selectionEllipse.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);
 Mockito.verify(graphics2D, Mockito.times(1)).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
 Mockito.verify(graphics2D, Mockito.times(1)).fill(selectionEllipse);
 }
}