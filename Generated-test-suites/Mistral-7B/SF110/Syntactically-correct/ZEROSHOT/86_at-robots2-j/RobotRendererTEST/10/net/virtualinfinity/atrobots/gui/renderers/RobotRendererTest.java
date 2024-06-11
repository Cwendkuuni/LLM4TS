package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.*;
import org.mockito.*;

public class RobotRendererTest {

 private RobotRenderer robotRenderer;
 private Graphics2D graphics2DMock;
 private RobotSnapshot robotSnapshot;

 @Before
 public void initialize() {
 robotRenderer = new RobotRenderer();
 graphics2DMock = Mockito.mock(Graphics2D.class);
 robotSnapshot = new RobotSnapshot(1L, new Position2D(10.0, 10.0), new Vector2D(50.0, 50.0), new Angle(0), new Angle(160), new Energy(100), new Temperature(50), true, false, false, false, "Robot1", new Armor(10));
 }

 @Test
 public void testRenderRobot_whenRobotIsNotDead_thenRenderBodyShieldTurretArmorNameHeatAndSelection() {
 robotRenderer.render(graphics2DMock, robotSnapshot, Collections.singleton(robotSnapshot.getId()));

 verify(graphics2DMock, times(1)).draw(Mockito.any(Line2D.class));
 verify(graphics2DMock, times(1)).fill(Mockito.any(GeneralPath.class));
 verify(graphics2DMock, times(1)).setPaint(Color.RED);
 verify(graphics2DMock, times(1)).setPaint(Color.YELLOW);
 verify(graphics2DMock, times(1)).drawString(robotSnapshot.getName(), (float) (robotSnapshot.getX() - graphics2DMock.getFontMetrics().getStringBounds(robotSnapshot.getName(), graphics2DMock).getWidth() / 2.0f), (float) (robotSnapshot.getY() - 10.0f));
 verify(graphics2DMock, times(1)).setPaint(new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.WHITE }));
 verify(graphics2DMock, times(1)).fill(Mockito.any(Ellipse2D.Double.class));
 verify(graphics2DMock, times(1)).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
 verify(graphics2DMock, times(1)).fill(Mockito.any(Rectangle2D.Double.class));
 verify(graphics2DMock, times(1)).draw(Mockito.any(Rectangle2D.Double.class));
 verify(graphics2DMock, times(1)).setPaint(new GradientPaint((float) robotSnapshot.getX() - 50.0f, (float) robotSnapshot.getY() + 35.0f, RobotRenderer.HEAT_COLOR1, (float) robotSnapshot.getX() - 50.0f, (float) robotSnapshot.getY() + 35.0f, RobotRenderer.HEAT_COLOR2));
 verify(graphics2DMock, times(1)).fill(Mockito.any(Rectangle2D.Double.class));
 verify(graphics2DMock, times(1)).draw(Mockito.any(Rectangle2D.Double.class));
 }

 @Test
 public void testRenderRobot_whenRobotIsDead_thenDoNotRenderAnything() {
 robotSnapshot.setDead(true);
 robotRenderer.render(graphics2DMock, robotSnapshot, Collections.singleton(robotSnapshot.getId()));

 verify(graphics2DMock, never()).draw(Mockito.any(Line2D.class));
 verify(graphics2DMock, never()).fill(Mockito.any(GeneralPath.class));
 verify(graphics2DMock, never()).setPaint(Color.RED);
 verify(graphics2DMock, never()).setPaint(Color.YELLOW);
 verify(graphics2DMock, never()).drawString(robotSnapshot.getName(), (float) (robotSnapshot.getX() - graphics2DMock.getFontMetrics().getStringBounds(robotSnapshot.getName(), graphics2DMock).getWidth() / 2.0f), (float) (robotSnapshot.getY() - 10.0f));
 verify(graphics2DMock, never()).setPaint(new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.WHITE }));
 verify(graphics2DMock, never()).fill(Mockito.any(Ellipse2D.Double.class));
 verify(graphics2DMock, never()).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
 verify(graphics2DMock, never()).fill(Mockito.any(Rectangle2D.Double.class));
 verify(graphics2DMock, never()).draw(Mockito.any(Rectangle2D.Double.class));
 }

 @Test
 public void testRenderRobot_whenRobotIsSelected_thenPaintSelection() {
 robotRenderer.render(graphics2DMock, robotSnapshot, Collections.singleton(robotSnapshot.getId()));

 verify(graphics2DMock, times(1)).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
 verify(graphics2DMock, times(1)).fill(Mockito.any(Ellipse2D.Double.class));
 }

 @Test
 public void testIsRenderDead_whenRenderDeadIsTrue_thenReturnTrue() {
 robotRenderer.setRenderDead(true);
 assertTrue(robotRenderer.isRenderDead());
 }

 @Test
 public void testIsRenderDead_whenRenderDeadIsFalse_thenReturnFalse() {
 robotRenderer.setRenderDead(false);
 assertFalse(robotRenderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars_whenShowStatusBarsIsTrue_thenReturnTrue() {
 robotRenderer.setShowStatusBars(true);
 assertTrue(robotRenderer.isShowStatusBars());
 }

 @Test
 public void testIsShowStatusBars_whenShowStatusBarsIsFalse_thenReturnFalse() {
 robotRenderer.setShowStatusBars(false);
 assertFalse(robotRenderer.isShowStatusBars());
 }

 @Test
 public void testIsShowName_whenShowNameIsTrue_thenReturnTrue() {
 robotRenderer.setShowName(true);
 assertTrue(robotRenderer.isShowName());
 }

 @Test
 public void testIsShowName_whenShowNameIsFalse_thenReturnFalse() {
 robotRenderer.setShowName(false);
 assertFalse(robotRenderer.isShowName());
 }

 @Test
 public void testIsFillShield_whenFillShieldIsTrue_thenReturnTrue() {
 robotRenderer.setFillShield(true);
 assertTrue(robotRenderer.isFillShield());
 }

 @Test
 public void testIsFillShield_whenFillShieldIsFalse_thenReturnFalse() {
 robotRenderer.setFillShield(false);
 assertFalse(robotRenderer.isFillShield());
 }
}