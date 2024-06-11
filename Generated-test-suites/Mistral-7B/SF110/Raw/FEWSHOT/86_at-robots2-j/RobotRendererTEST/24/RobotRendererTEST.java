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
    private Snapshot<RobotSnapshot> robotSnapshot;
    private Set<Integer> selectedRobotIds;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        graphics2dMock = mock(Graphics2D.class);
        robotRenderer = new RobotRenderer();
        robotSnapshot = new RobotSnapshot(1, 1.0, 1.0, 1.0, new Vector3D(0.0, 0.0, 0.0), new Vector3D(0.0, 0.0, 0.0), 100.0, 50.0, 5.0, 30.0, 5.0, 3.0, 5.0, true, false, "Robot1", 10.0f, 100.0f, 20.0f, new Temperature(10.0), new Armor(100.0), new Position2D(1.0, 1.0), new Angle(0.0), new Angle(0.0), new Angle(0.0), new Angle(0.0));
        selectedRobotIds = new HashSet<>();
    }

    @Test
    public void testRender_deadRobotSnapshot() {
        robotRenderer.render(graphics2dMock, robotSnapshot, selectedRobotIds);
        verify(graphics2dMock, times(0)).fill(any(Shape.class));
        verify(graphics2dMock, times(0)).setPaint(any(Paint.class));
        verify(graphics2dMock, times(0)).draw(any(Shape.class));
    }

    @Test
    public void testRender_aliveRobotSnapshot() {
        robotRenderer.render(graphics2dMock, robotSnapshot, selectedRobotIds);

        // Test painting body
        verify(graphics2dMock, times(1)).setPaint(any(Paint.class));
        verify(graphics2dMock, times(1)).fill(any(Shape.class));
        verify(graphics2dMock, times(1)).draw(any(Shape.class));

        // Test painting shield
        verify(graphics2dMock, times(robotSnapshot.isActiveShield() ? 1 : 0)).setPaint(any(Paint.class));
        verify(graphics2dMock, times(robotSnapshot.isActiveShield() ? 1 : 0)).fill(any(Shape.class));
        verify(graphics2dMock, times(robotSnapshot.isActiveShield() ? 1 : 0)).draw(any(Shape.class));

        // Test painting turret
        verify(graphics2dMock, times(1)).draw(any(Line2D.class));

        // Test painting name
        verify(graphics2dMock, times(1)).setPaint(any(Paint.class));
        verify(graphics2dMock, times(1)).drawString(robotSnapshot.getName(), anyFloat(), anyFloat());

        // Test painting armor
        verify(graphics2dMock, times(1)).setPaint(any(Paint.class));
        verify(graphics2dMock, times(1)).fill(any(Shape.class));
        verify(graphics2dMock, times(1)).setPaint(any(Paint.class));
        verify(graphics2dMock, times(1)).draw(any(Shape.class));

        // Test painting heat
        verify(graphics2dMock, times(1)).setPaint(any(GradientPaint.class));
        verify(graphics2dMock, times(1)).fill(any(Shape.class));
        verify(graphics2dMock, times(1)).draw(any(Shape.class));

        // Test painting selection
        verify(graphics2dMock, times(selectedRobotIds.contains(robotSnapshot.getId()) ? 1 : 0)).setPaint(any(Paint.class));
        verify(graphics2dMock, times(selectedRobotIds.contains(robotSnapshot.getId()) ? 1 : 0)).fill(any(Shape.class));
    }

    @Test
    public void testIsRenderDead() {
        assertTrue(robotRenderer.isRenderDead());
        robotRenderer.setRenderDead(false);
        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testSetRenderDead() {
        robotRenderer.setRenderDead(true);
        assertTrue(robotRenderer.isRenderDead());
        robotRenderer.setRenderDead(false);
        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars() {
        assertTrue(robotRenderer.isShowStatusBars());
        robotRenderer.setShowStatusBars(false);
        assertFalse(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowStatusBars() {
        robotRenderer.setShowStatusBars(true);
        assertTrue(robotRenderer.isShowStatusBars());
        robotRenderer.setShowStatusBars(false);
        assertFalse(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName() {
        assertTrue(robotRenderer.isShowName());
        robotRenderer.setShowName(false);
        assertFalse(robotRenderer.isShowName());
    }

    @Test
    public void testSetShowName() {
        robotRenderer.setShowName(true);
        assertTrue(robotRenderer.isShowName());
        robotRenderer.setShowName(false);
        assertFalse(robotRenderer.isShowName());
    }

    @Test
    public void testIsFillShield() {
        assertTrue(robotRenderer.isFillShield());
        robotRenderer.setFillShield(false);
        assertFalse(robotRenderer.isFillShield());
    }

    @Test
    public void testSetFillShield() {
        robotRenderer.setFillShield(true);
        assertTrue(robotRenderer.isFillShield());
        robotRenderer.setFillShield(false);
        assertFalse(robotRenderer.isFillShield());
    }
}