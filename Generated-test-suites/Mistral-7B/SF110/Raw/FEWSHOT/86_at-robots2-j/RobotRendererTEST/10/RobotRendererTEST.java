package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.Test;
import org.mockito.Mockito;

public class RobotRendererTest {

    @Test
    public void testRender() {
        Graphics2D g2dMock = Mockito.mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        Set<Integer> selectedRobotIds = new HashSet<>();

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.render(g2dMock, robotSnapshot, selectedRobotIds);

        verify(g2dMock, times(1)).drawString(robotSnapshot.getName(), argThat(isCloseTo(robotSnapshot.getX() - 5.0, 0.1)), argThat(isCloseTo(robotSnapshot.getY() - 10.0, 0.1)));
        verify(g2dMock, times(1)).setPaint(Color.yellow);
    }

    @Test
    public void testRenderWithDeadRobot() {
        Graphics2D g2dMock = Mockito.mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        when(robotSnapshot.isDead()).thenReturn(true);
        Set<Integer> selectedRobotIds = new HashSet<>();

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.render(g2dMock, robotSnapshot, selectedRobotIds);

        verify(g2dMock, never()).fill(any(Shape.class));
        verify(g2dMock, times(1)).setPaint(Color.orange.darker().darker());
        verify(g2dMock, times(1)).setStroke(any(Stroke.class));
        verify(g2dMock, times(1)).draw(any(Shape.class));
    }

    @Test
    public void testRenderWithSelectedRobot() {
        Graphics2D g2dMock = Mockito.mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(robotSnapshot.getId());

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.render(g2dMock, robotSnapshot, selectedRobotIds);

        verify(g2dMock, times(1)).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
        verify(g2dMock, times(1)).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testIsRenderDead() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setRenderDead(false);
        assertFalse(robotRenderer.isRenderDead());

        robotRenderer.setRenderDead(true);
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testSetRenderDead() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setRenderDead(false);
        robotRenderer.setRenderDead(true);
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowStatusBars(false);
        assertFalse(robotRenderer.isShowStatusBars());

        robotRenderer.setShowStatusBars(true);
        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowStatusBars() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowStatusBars(false);
        robotRenderer.setShowStatusBars(true);
        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowName(false);
        assertFalse(robotRenderer.isShowName());

        robotRenderer.setShowName(true);
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testSetShowName() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowName(false);
        robotRenderer.setShowName(true);
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testIsFillShield() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setFillShield(false);
        assertFalse(robotRenderer.isFillShield());

        robotRenderer.setFillShield(true);
        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testSetFillShield() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setFillShield(false);
        robotRenderer.setFillShield(true);
        assertTrue(robotRenderer.isFillShield());
    }
}