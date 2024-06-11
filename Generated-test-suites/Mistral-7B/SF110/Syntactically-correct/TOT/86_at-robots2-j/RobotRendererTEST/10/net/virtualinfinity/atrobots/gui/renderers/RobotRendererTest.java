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

 private Graphics2D mockGraphics2D;
 private RobotSnapshot mockRobotSnapshot;
 private Set<Integer> mockSelectedRobotIds;
 private RobotRenderer robotRenderer;

 @Before
 public void setUp() {
 mockGraphics2D = mock(Graphics2D.class);
 mockRobotSnapshot = mock(RobotSnapshot.class);
 mockSelectedRobotIds = new HashSet<>();
 robotRenderer = new RobotRenderer();
 }

 @Test
 public void testRender_RobotIsNotDead_RenderBodyShieldTurretAndStatusBars() {
 when(mockRobotSnapshot.isDead()).thenReturn(false);
 robotRenderer.render(mockGraphics2D, mockRobotSnapshot, mockSelectedRobotIds);
 verify(mockGraphics2D).draw(any(Line2D.class));
 verify(mockGraphics2D).fill(any(Path2D.class));
 verify(mockGraphics2D).draw(any(Ellipse2D.class));
 verify(mockGraphics2D, times(2)).fill(any(Rectangle2D.class));
 verifyZeroInteractions(mockGraphics2D, mockRobotSnapshot, mockSelectedRobotIds);
 }

 @Test
 public void testRender_RobotIsDead_DoNotRenderAnything() {
 when(mockRobotSnapshot.isDead()).thenReturn(true);
 robotRenderer.render(mockGraphics2D, mockRobotSnapshot, mockSelectedRobotIds);
 verifyZeroInteractions(mockGraphics2D, mockRobotSnapshot, mockSelectedRobotIds);
 }

 @Test
 public void testRender_SelectedRobotId_PaintSelection() {
 mockRobotSnapshot.setId(1);
 mockSelectedRobotIds.add(1);
 robotRenderer.render(mockGraphics2D, mockRobotSnapshot, mockSelectedRobotIds);
 verify(mockGraphics2D).fill(any(Ellipse2D.class));
 }

 @Test
 public void testIsRenderDead_DefaultValueIsTrue() {
 robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isRenderDead());
 }

 @Test
 public void testIsRenderDead_ValueCanBeChanged() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setRenderDead(false);
 assertFalse(robotRenderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars_DefaultValueIsTrue() {
 robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isShowStatusBars());
 }

 @Test
 public void testIsShowStatusBars_ValueCanBeChanged() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setShowStatusBars(false);
 assertFalse(robotRenderer.isShowStatusBars());
 }

 @Test
 public void testIsShowName_DefaultValueIsTrue() {
 robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isShowName());
 }

 @Test
 public void testIsShowName_ValueCanBeChanged() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setShowName(false);
 assertFalse(robotRenderer.isShowName());
 }

 @Test
 public void testIsFillShield_DefaultValueIsTrue() {
 robotRenderer = new RobotRenderer();
 assertTrue(robotRenderer.isFillShield());
 }

 @Test
 public void testIsFillShield_ValueCanBeChanged() {
 robotRenderer = new RobotRenderer();
 robotRenderer.setFillShield(false);
 assertFalse(robotRenderer.isFillShield());
 }

 @After
 public void tearDown() {
 reset(mockGraphics2D, mockRobotSnapshot, mockSelectedRobotIds);
 }

}