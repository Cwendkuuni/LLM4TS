package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.Test;

public class RobotRendererTest {

 RobotSnapshot mockRobotSnapshot() {
 return mock(RobotSnapshot.class);
 }

 @Test
 public void testNewInstance() {
 RobotRenderer robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isRenderDead());
 assertTrue(robotRenderer.isShowStatusBars());
 assertTrue(robotRenderer.isShowName());
 assertTrue(robotRenderer.isFillShield());
 }

 @Test
 public void testIsRenderDead() {
 RobotRenderer robotRenderer = new RobotRenderer();
 RobotSnapshot robotSnapshot = mockRobotSnapshot();
 when(robotSnapshot.isDead()).thenReturn(false);
 robotRenderer.setRenderDead(false);
 assertFalse(robotRenderer.isRenderDead());

 robotSnapshot.isDead = true;
 robotRenderer.render(mock(Graphics2D.class), robotSnapshot, Collections.emptySet());
 assertTrue(robotRenderer.isRenderDead());
 }

 @Test
 public void testRenderRobotWithSelection() {
 final Graphics2D g2d = mock(Graphics2D.class);
 final RobotSnapshot robotSnapshot = mockRobotSnapshot();
 final Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(robotSnapshot.getId());

 new RobotRenderer().render(g2d, robotSnapshot, selectedRobotIds);

 final Ellipse2D.Double s = new Ellipse2D.Double();
 s.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);
 assertTrue(g2d.getClipBounds().contains(s));
 }

 @Test
 public void testPaintSelectionWithNullGraphics2D() {
 final RobotSnapshot robotSnapshot = mockRobotSnapshot();
 final Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(robotSnapshot.getId());

 new RobotRenderer().paintSelection(null, robotSnapshot);
 }

 @Test
 public void testPaintBody() {
 final Graphics2D g2d = mock(Graphics2D.class);
 final RobotSnapshot robotSnapshot = mockRobotSnapshot();

 new RobotRenderer().paintBody(g2d, robotSnapshot);

 final Stroke stroke = g2d.getStroke();
 final BasicStroke basicStroke = new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f);
 assertEquals(basicStroke, g2d.getStroke());
 final Path2D path = new Path2D.Double();
 path.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 path.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 path.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 path.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 path.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 path.closePath();
 assertEquals(path, g2d.getPath());
 }

 @Test
 public void testPaintBodyWithDeadRobot() {
 final Graphics2D g2d = mock(Graphics2D.class);
 final RobotSnapshot robotSnapshot = mockRobotSnapshot();
 when(robotSnapshot.isDead()).thenReturn(true);
 new RobotRenderer().paintBody(g2d, robotSnapshot);

 final Line2D line = new Line2D.Double();
 assertEquals(line, g2d.getPath());
 }

 @Test
 public void testPaintShield() {
 final Graphics2D g2d = mock(Graphics2D.class);
 final RobotSnapshot robotSnapshot = mockRobotSnapshot();
 when(robotSnapshot.isActiveShield()).thenReturn(true);
 new RobotRenderer().paintShield(g2d, robotSnapshot);

 final Ellipse2D ellipse = new Ellipse2D.Double();
 final RadialGradientPaint radialGradientPaint = new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white });
 assertEquals(radialGradientPaint, g2d.getPaint());
 }

 @Test
 public void testPaintShieldWithInactiveRobot() {
 final Graphics2D g2d = mock(Graphics2D.class);
 final RobotSnapshot robotSnapshot = mockRobotSnapshot();
 when(robotSnapshot.isActiveShield()).thenReturn(false);
 new RobotRenderer().paintShield(g2d, robotSnapshot);

 final Color color = new Color(0.8f, 0.6f, 1.0f);
 assertEquals(color, g2d.getPaint());
 }

 @Test
 public void testPaintName() {
 final Graphics2D g2d = mock(Graphics2D.class);
 final RobotSnapshot robotSnapshot = mockRobotSnapshot();
 when(robotSnapshot.getName()).thenReturn("Test");
 new RobotRenderer().paintName(g2d, robotSnapshot);

 final String test = "Test";
 assertEquals(test, g2d.getFont().getFamily());
 }

 @Test
 public void testPaintHeat() {
 final Graphics2D g2d = mock(Graphics2D.class);
 final RobotSnapshot robotSnapshot = mockRobotSnapshot();
 final Rectangle2D.Double rect = new Rectangle2D.Double();
 rect.setFrame(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);

 new RobotRenderer().paintHeat(g2d, robotSnapshot);

 final GradientPaint gradientPaint = new GradientPaint((float)rect.getMinX(), (float)rect.getMinY(), RobotRenderer.HEAT_COLOR1, (float)rect.getMaxX(), (float)rect.getMinY(), RobotRenderer.HEAT_COLOR2);
 assertEquals(gradientPaint, g2d.getPaint());
 }

 @Test
 public void testPaintArmor() {
 final Graphics2D g2d = mock(Graphics2D.class);
 final RobotSnapshot robotSnapshot = mockRobotSnapshot();
 final float armor = 20.0f;
 when(robotSnapshot.getArmor()).thenReturn(armor);

 new RobotRenderer().paintArmor(g2d, robotSnapshot);

 final Path2D path = new Path2D.Double();
 path.moveTo(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0);
 path.lineTo(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0 + armor);
 path.lineTo(robotSnapshot.getX() + 100.0, robotSnapshot.getY() + 35.0 + armor);
 path.lineTo(robotSnapshot.getX() + 100.0, robotSnapshot.getY() + 20.0);
 path.lineTo(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0);
 path.closePath();
 assertEquals(path, g2d.getPath());
 }

 @Test
 public void testPaintTurret() {
 final Graphics2D g2D = mock(Graphics2D.class);
 final RobotSnapshot robotSnapshot = mockRobotSnapshot();
 final double turretHeading = Math.PI / 4.0;
 when(robotSnapshot.getTurretHeading()).thenReturn(new AbsoluteAngle(turretHeading));

 new RobotRenderer().paintTurret(g2D, robotSnapshot);

 final Line2D line = new Line2D.Double();
 line.setLine(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + Math.cos(turretHeading) * 5.0, robotSnapshot.getY() + Math.sin(turretHeading) * 5.0);
 assertEquals(line, g2D.getPath());
 }
}