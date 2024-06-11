package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.*;
import org.mockito.*;

public class RobotRendererTest {

 private RobotRenderer robotRenderer;
 private Graphics2D graphics2dMock;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 robotRenderer = new RobotRenderer();
 graphics2dMock = mock(Graphics2D.class);
 robotSnapshot = new RobotSnapshot(1L, 0.0, new PositionVector(10.0, 10.0), 50.0, 100.0, false, 100.0, false, new AbsoluteAngle(0.0), new AbsoluteAngle(0.0), 5.0, 3.0, false, new Temperature(25.0), new AbsoluteAngle(0.0), new AbsoluteAngle(0.0), new AbsoluteAngle(0.0), new AbsoluteAngle(0.0), new PositionVector(10.0, 10.0), new AbsoluteAngle(0.0));
 }

 @Test
 public void testRenderWithNonDeadRobot() {
 // Given
 when(graphics2dMock.getColor()).thenReturn(Color.WHITE);
 when(graphics2dMock.getFont()).thenReturn(new Font("Serif", Font.BOLD, 12));
 when(graphics2dMock.getFontMetrics()).thenReturn(Mockito.mock(FontMetrics.class));

 // When
 robotRenderer.render(graphics2dMock, robotSnapshot, new HashSet<>());

 // Then
 // Test if body is rendered
 Path expectedBodyPath = new GeneralPath();
 expectedBodyPath.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
 expectedBodyPath.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedBodyPath.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
 expectedBodyPath.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
 expectedBodyPath.closePath();
 Path actualBodyPath = (Path) capture(graphics2dMock).getPath();
 assertEquals("Body path is not as expected", expectedBodyPath, actualBodyPath.getValue(0));

 // Test if shield is rendered
 if (robotSnapshot.isActiveShield()) {
 Ellipse2D expectedShieldEllipse = new Ellipse2D.Double(robotSnapshot.getX() - 5.0, robotSnapshot.getY() + 10.0, 15.0, 15.0);
 Ellipse2D actualShieldEllipse = (Ellipse2D) capture(graphics2dMock).getShape();
 assertEquals("Shield ellipse is not as expected", expectedShieldEllipse, actualShieldEllipse);
 }

 // Test if name is rendered
 String expectedName = robotSnapshot.getName();
 String actualName = (String) capture(graphics2dMock).getFont().getStringBounds(expectedName, graphics2dMock).getSubstring(0, (int) capture(graphics2dMock).getFontMetrics().getStringBounds(expectedName, graphics2dMock).getWidth());
 assertEquals("Name is not as expected", expectedName, actualName);
 }

 @Test
 public void testRenderWithDeadRobot() {
 // Given
 when(graphics2dMock.getColor()).thenReturn(Color.ORANGE.darker().darker());
 when(graphics2dMock.getFont()).thenReturn(new Font("Serif", Font.BOLD, 12));
 when(graphics2dMock.getFontMetrics()).thenReturn(Mockito.mock(FontMetrics.class));

 // When
 robotRenderer.render(graphics2dMock, robotSnapshot, new HashSet<>());

 // Then
 Path expectedBodyPath = new GeneralPath();
 // The dead robot's body path doesn't change
 // Test if selection is not rendered when robot is dead
 assertNull("Selection should not be rendered", capture(graphics2dMock).getPaint());
 }

 @Test
 public void testIsRenderDead() {
 // Given

 // When
 boolean actualRenderDead = robotRenderer.isRenderDead();
 boolean expectedRenderDead = robotRenderer.renderDead;

 // Then
 assertEquals("isRenderDead method returns unexpected value", expectedRenderDead, actualRenderDead);
 }

 @Test
 public void testSetRenderDead() {
 // Given

 // When
 robotRenderer.setRenderDead(false);

 // Then
 assertFalse("setRenderDead method doesn't set renderDead to false", robotRenderer.isRenderDead());
 robotRenderer.setRenderDead(true);
 assertTrue("setRenderDead method doesn't set renderDead to true", robotRenderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars() {
 // Given

 // When
 boolean actualShowStatusBars = robotRenderer.isShowStatusBars();
 boolean expectedShowStatusBars = robotRenderer.showStatusBars;

 // Then
 assertEquals("isShowStatusBars method returns unexpected value", expectedShowStatusBars, actualShowStatusBars);
 }

 @Test
 public void testSetShowStatusBars() {
 // Given

 // When
 robotRenderer.setShowStatusBars(false);

 // Then
 assertFalse("setShowStatusBars method doesn't set showStatusBars to false", robotRenderer.isShowStatusBars());
 robotRenderer.setShowStatusBars(true);
 assertTrue("setShowStatusBars method doesn't set showStatusBars to true", robotRenderer.isShowStatusBars());
 }

 @Test
 public void testIsShowName() {
 // Given

 // When
 boolean actualShowName = robotRenderer.isShowName();
 boolean expectedShowName = robotRenderer.showName;

 // Then
 assertEquals("isShowName method returns unexpected value", expectedShowName, actualShowName);
 }

 @Test
 public void testSetShowName() {
 // Given

 // When
 robotRenderer.setShowName(false);

 // Then
 assertFalse("setShowName method doesn't set showName to false", robotRenderer.isShowName());
 robotRenderer.setShowName(true);
 assertTrue("setShowName method doesn't set showName to true", robotRenderer.isShowName());
 }

 @Test
 public void testIsFillShield() {
 // Given

 // When
 boolean actualFillShield = robotRenderer.isFillShield();
 boolean expectedFillShield = robotRenderer.fillShield;

 // Then
 assertEquals("isFillShield method returns unexpected value", expectedFillShield, actualFillShield);
 }

 @Test
 public void testSetFillShield() {
 // Given

 // When
 robotRenderer.setFillShield(false);

 // Then
 assertFalse("setFillShield method doesn't set fillShield to false", robotRenderer.isFillShield());
 robotRenderer.setFillShield(true);
 assertTrue("setFillShield method doesn't set fillShield to true", robotRenderer.isFillShield());
 }
}