package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class RobotRendererTest {

    private RobotRenderer robotRenderer;
    private Graphics2D graphics2DMock;
    private RobotSnapshot robotSnapshot;
    private Set<Integer> selectedRobotIds;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        robotRenderer = new RobotRenderer();
        graphics2DMock = mock(Graphics2D.class);
        robotSnapshot = new RobotSnapshot();
        selectedRobotIds = new HashSet<>();
    }

    @Test
    public void testRenderWithDeadRobot() {
        when(graphics2DMock.isEnabled()).thenReturn(true);
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);
        verify(graphics2DMock, never()).fill(any(Shape.class));
        verify(graphics2DMock, never()).draw(any(Shape.class));
    }

    @Test
    public void testRenderWithLiveRobot() {
        when(graphics2DMock.isEnabled()).thenReturn(true);
        robotSnapshot.setId(1);
        robotSnapshot.setX(100.0);
        robotSnapshot.setY(100.0);
        robotSnapshot.setTemperature(new Temperature(50));
        robotSnapshot.setArmor(100.0);
        robotSnapshot.setDead(false);
        robotSnapshot.setName("TestRobot");
        robotSnapshot.setHeading(new AbsoluteAngle(0));
        robotSnapshot.setTurretHeading(new AbsoluteAngle(0));
        robotSnapshot.setPositionVector(new Vector2D(100, 100));

        when(graphics2DMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));
        when(graphics2DMock.getStroke()).thenReturn(new BasicStroke());

        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        verify(graphics2DMock, times(1)).fill(any(Shape.class));
        verify(graphics2DMock, times(1)).draw(any(Shape.class));
        verify(graphics2DMock, times(1)).setPaint(any(Color.class));
        verify(graphics2DMock, times(1)).setPaint(eq(Color.YELLOW));
        verify(graphics2DMock, times(1)).drawString(eq("TestRobot"), anyFloat(), anyFloat());
        verify(graphics2DMock, times(1)).setPaint(eq(Color.RED));
        verify(graphics2DMock, times(1)).fill(any(Shape.class));
        verify(graphics2DMock, times(1)).setPaint(mock(Color.ORANGE.darker().darker()));
        verify(graphics2DMock, times(1)).setStroke(any(Stroke.class));
        verifyNoMoreInteractions(graphics2DMock);
    }

    @Test
    public void testRenderWithSelectedRobot() {
        robotSnapshot.setId(1);
        selectedRobotIds.add(1);

        when(graphics2DMock.isEnabled()).thenReturn(true);

        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        verify(graphics2DMock, times(1)).setPaint(any(Color.class));
        verify(graphics2DMock, times(1)).setPaint(eq(new Color(1.0f, 1.0f, 0.0f, 0.25f)));
        verify(graphics2DMock, times(1)).fill(any(Shape.class));
    }

    @Test
    public void testIsRenderDead() {
        assertTrue(robotRenderer.isRenderDead());
        robotRenderer.setRenderDead(false);
        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testSetRenderDead() {
        robotRenderer.setRenderDead(false);
    }

    @Test
    public void testIsShowStatusBars() {
        assertTrue(robotRenderer.isShowStatusBars());
        robotRenderer.setShowStatusBars(false);
        assertFalse(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowStatusBars() {
        robotRenderer.setShowStatusBars(false);
    }

    @Test
    public void testIsShowName() {
        assertTrue(robotRenderer.isShowName());
        robotRenderer.setShowName(false);
        assertFalse(robotRenderer.isShowName());
    }

    @Test
    public void testSetShowName() {
        robotRenderer.setShowName(false);
    }

    @Test
    public void testIsFillShield() {
        assertTrue(robotRenderer.isFillShield());
        robotRenderer.setFillShield(false);
        assertFalse(robotRenderer.isFillShield());
    }

    @Test
    public void testSetFillShield() {
        robotRenderer.setFillShield(false);
    }
}