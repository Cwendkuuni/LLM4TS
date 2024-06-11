import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import static org.junit.Assert.*;

public class RobotRendererTest {

 private RobotRenderer robotRenderer;
 private Graphics2D graphics2DMock;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 robotRenderer = new RobotRenderer();
 graphics2DMock = Mockito.mock(Graphics2D.class);
 robotSnapshot = Mockito.spy(new RobotSnapshot());
 }

 @Test
 public void testRenderWithDeadRobotSnapshot() {
 robotRenderer.render(graphics2DMock, robotSnapshot, new HashSet<>());
 assertFalse(graphics2DMock.isDrawing());
 }

 @Test
 public void testRenderWithLiveRobotSnapshot() {
 Mockito.when(graphics2DMock.isDrawing()).thenReturn(true);
 robotRenderer.render(graphics2DMock, robotSnapshot, new HashSet<>());
 robotRenderer.paintBody(graphics2DMock, robotSnapshot);
 robotRenderer.paintShield(graphics2DMock, robotSnapshot);
 robotRenderer.paintTurret(graphics2DMock, robotSnapshot);
 robotRenderer.paintArmor(graphics2DMock, robotSnapshot);
 robotRenderer.paintHeat(graphics2DMock, robotSnapshot);
 robotRenderer.paintName(graphics2DMock, robotSnapshot);
 Mockito.verify(graphics2DMock, Mockito.times(1)).drawString(robotSnapshot.getName(), (float)(robotSnapshot.getX() - graphics2DMock.getFontMetrics().getStringBounds(robotSnapshot.getName(), graphics2DMock).getWidth() / 2.0), (float)(robotSnapshot.getY() - 10.0));
 assertTrue(graphics2DMock.isDrawing());
 }

 @Test
 public void testIsRenderDead() {
 robotRenderer.setRenderDead(false);
 assertFalse(robotRenderer.isRenderDead());
 robotRenderer.setRenderDead(true);
 assertTrue(robotRenderer.isRenderDead());
 }

 @Test
 public void testSetShowStatusBars() {
 robotRenderer.setShowStatusBars(false);
 assertFalse(robotRenderer.isShowStatusBars());
 robotRenderer.setShowStatusBars(true);
 assertTrue(robotRenderer.isShowStatusBars());
 }

 @Test
 public void testSetShowName() {
 robotRenderer.setShowName(false);
 assertFalse(robotRenderer.isShowName());
 robotRenderer.setShowName(true);
 assertTrue(robotRenderer.isShowName());
 }

 @Test
 public void testSetFillShield() {
 robotRenderer.setFillShield(false);
 assertFalse(robotRenderer.isFillShield());
 robotRenderer.setFillShield(true);
 assertTrue(robotRenderer.isFillShield());
 }

 @Test
 public void testPaintSelection() {
 Rectangle selection = new Rectangle();
 robotRenderer.paintSelection(graphics2DMock, robotSnapshot);
 assertEquals(graphics2DMock.getPaint(), new Color(1.0f, 1.0f, 0.0f, 0.25f));
 }

 @Test
 public void testPaintShield() {
 Ellipse2D shield = new Ellipse2D.Double();
 robotRenderer.paintShield(graphics2DMock, robotSnapshot);
 assertEquals(graphics2DMock.getPaint(), new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white }));
 }

 @Test
 public void testPaintName() {
 String robotName = "TestRobot";
 robotSnapshot.setName(robotName);
 robotRenderer.paintName(graphics2DMock, robotSnapshot);
 assertEquals(graphics2DMock.getPaint(), Color.yellow);
 assertEquals(graphics2DMock.getFontMetrics().getStringBounds(robotName, graphics2DMock).getWidth(), 2 * robotName.length() * 12, 0);
 }

 @Test
 public void testPaintHeat() {
 Rectangle2D heatRect = new Rectangle2D.Double();
 robotRenderer.paintHeat(graphics2DMock, robotSnapshot);
 assertEquals(graphics2DMock.getPaint(), new GradientPaint((float)heatRect.getMinX(), (float)heatRect.getMinY(), RobotRenderer.HEAT_COLOR1, (float)heatRect.getMaxX(), (float)heatRect.getMinY(), RobotRenderer.HEAT_COLOR2));
 }

 @Test
 public void testPaintArmor() {
 Rectangle2D armorRect = new Rectangle2D.Double();
 robotRenderer.paintArmor(graphics2DMock, robotSnapshot);
 assertEquals(graphics2DMock.getPaint(), new Color(0.0f, 0.0f, 1.0f, 0.6f));
 }

 @Test
 public void testPaintTurret() {
 Line2D turretLine = new Line2D.Double();
 robotRenderer.paintTurret(graphics2DMock, robotSnapshot);
 assertEquals(graphics2DMock.getPaint(), Color.white);
 }

 @Test
 public void testPaintBody() {
 Path2D bodyPath = new Path2D.Double();
 robotRenderer.paintBody(graphics2DMock, robotSnapshot);
 assertEquals(graphics2DMock.getPaint(), robotSnapshot.isDead() ? Color.orange.darker().darker() : Color.red);
 }
}