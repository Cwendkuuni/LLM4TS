import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {

 private RobotRenderer robotRenderer;
 private Graphics2D graphics2DMock;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 robotRenderer = new RobotRenderer();
 graphics2DMock = Mockito.mock(Graphics2D.class);
 robotSnapshot = Mockito.mock(RobotSnapshot.class);
 }

 @Test
 public void testRenderWithDeadRobot() {
 robotRenderer.render(graphics2DMock, robotSnapshot, null);
 Mockito.verify(graphics2DMock, Mockito.never()).fill(Mockito.any(Shape.class));
 }

 @Test
 public void testRenderWithoutDeadRobot() {
 Mockito.when(robotSnapshot.isDead()).thenReturn(false);
 robotRenderer.render(graphics2DMock, robotSnapshot, null);

 verifyBodyIsRendered(graphics2DMock);
 verifyShieldIsRendered(graphics2DMock);
 verifyTurretIsRendered(graphics2DMock);
 verifyArmorIsRendered(graphics2DMock);
 verifyHeatIsRendered(graphics2DMock);
 verifyNameIsRendered(graphics2DMock);
 }

 @Test
 public void testRenderWithSelectedRobot() {
 Mockito.when(robotSnapshot.isDead()).thenReturn(false);
 Mockito.when(robotSnapshot.getId()).thenReturn(1);
 Set<Integer> selectedRobotIds = new HashSet<>();
 selectedRobotIds.add(1);
 robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);
 verifySelectionIsRendered(graphics2DMock);
 }

 @Test
 public void testSetRenderDead() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setRenderDead(false);
 assertFalse(robotRenderer.isRenderDead());

 robotRenderer.setRenderDead(true);
 assertTrue(robotRenderer.isRenderDead());
 }

 @Test
 public void testSetShowStatusBars() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setShowStatusBars(false);
 assertFalse(robotRenderer.isShowStatusBars());

 robotRenderer.setShowStatusBars(true);
 assertTrue(robotRenderer.isShowStatusBars());
 }

 @Test
 public void testSetShowName() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setShowName(false);
 assertFalse(robotRenderer.isShowName());

 robotRenderer.setShowName(true);
 assertTrue(robotRenderer.isShowName());
 }

 @Test
 public void testSetFillShield() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setFillShield(false);
 assertFalse(robotRenderer.isFillShield());

 robotRenderer.setFillShield(true);
 assertTrue(robotRenderer.isFillShield());
 }

 private void verifyBodyIsRendered(Graphics2D graphics2DMock) {
 Path2D path = new Path2D.Double();
 path.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 path.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 path.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 path.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 path.closePath();
 Mockito.verify(graphics2DMock).fill(path);
 }

 private void verifyShieldIsRendered(Graphics2D graphics2DMock) {
 Ellipse2D.Double s = new Ellipse2D.Double();
 s.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 15.0, robotSnapshot.getY() + 15.0);
 if (robotRenderer.isFillShield()) {
 Mockito.verify(graphics2DMock).setPaint(Mockito.any(RadialGradientPaint.class));
 Mockito.verify(graphics2DMock).fill(s);
 } else {
 Mockito.verify(graphics2DMock).setPaint(Mockito.any(Color.class));
 Mockito.verify(graphics2DMock).draw(s);
 }
 }

 private void verifyTurretIsRendered(Graphics2D graphics2DMock) {
 Line2D line = new Line2D.Double();
 line.setLine(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0);
 Mockito.verify(graphics2DMock).draw(line);
 }

 private void verifyArmorIsRendered(Graphics2D graphics2DMock) {
 Rectangle2D.Double rect = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, robotSnapshot.getArmor(), 10.0);
 Mockito.verify(graphics2DMock).setPaint(Mockito.any(Color.class));
 Mockito.verify(graphics2DMock).fill(rect);
 }

 private void verifyHeatIsRendered(Graphics2D graphics2DMock) {
 Rectangle2D.Double rect = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);
 Mockito.verify(graphics2DMock).setPaint(Mockito.any(GradientPaint.class));
 Mockito.verify(graphics2DMock).fill(new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, robotSnapshot.getTemperature().getLogScale() * 0.2, 10.0));
 }

 private void verifyNameIsRendered(Graphics2D graphics2DMock) {
 Mockito.verify(graphics2DMock).setPaint(Mockito.any(Color.class));
 Mockito.verify(graphics2DMock).drawString(robotSnapshot.getName(), (float)(robotSnapshot.getX() - graphics2DMock.getFontMetrics().getStringBounds(robotSnapshot.getName(), graphics2DMock).getWidth() / 2.0), (float)(robotSnapshot.getY() - 10.0));
 }

 private void verifySelectionIsRendered(Graphics2D graphics2DMock) {
 Ellipse2D.Double s = new Ellipse2D.Double();
 s.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);
 Mockito.verify(graphics2DMock).setPaint(Mockito.any(Color.class));
 Mockito.verify(graphics2DMock).fill(s);
 }
}