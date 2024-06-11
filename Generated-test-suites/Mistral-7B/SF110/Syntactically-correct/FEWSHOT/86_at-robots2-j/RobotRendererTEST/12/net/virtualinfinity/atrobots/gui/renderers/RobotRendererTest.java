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

public class RobotRendererTest {

    private RobotRenderer robotRenderer;
    private Graphics2D graphics2DMock;
    private RobotSnapshot robotSnapshot;
    private Set<Integer> selectedRobotIds;

    @Before
    public void setUp() {
        graphics2DMock = mock(Graphics2D.class);
        robotSnapshot = new RobotSnapshot(1L, 50.0, 50.0, new Vector2D(1.0, 1.0), new Vector2D(1.0, 1.0), 100.0, 50.0, 50.0, new Temperature(10.0), "Robot1", false, false, false);
        selectedRobotIds = new HashSet<>();
        robotRenderer = new RobotRenderer();
    }

    @Test
    public void testRenderRobotWithSelection() {
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);
        verify(graphics2DMock, times(1)).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
        verify(graphics2DMock, times(1)).fill(any(Ellipse2D.class));
    }

    @Test
    public void testRenderRobotWithoutSelection() {
        selectedRobotIds.clear();
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);
        verify(graphics2DMock, never()).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
    }

    @Test
    public void testRenderDeadRobot() {
        robotSnapshot.setDead(true);
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);
        verify(graphics2DMock, times(1)).setPaint(new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f));
        verify(graphics2DMock, times(1)).setPaint(Color.orange.darker().darker());
    }

    @Test
    public void testRenderAliveRobot() {
        robotSnapshot.setDead(false);
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);
        verify(graphics2DMock, times(1)).setPaint(Color.red);
    }

    @Test
    public void testPaintShieldFilled() {
        robotSnapshot.setActiveShield(true);
        robotRenderer.paintShield(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(1)).setPaint(new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white }));
        verify(graphics2DMock, times(1)).fill(any(Shape.class));
        verify(graphics2DMock, times(1)).fill(any(Shape.class));
    }

    @Test
    public void testPaintShieldNotFilled() {
        robotSnapshot.setActiveShield(false);
        robotRenderer.paintShield(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(1)).setPaint(new Color(0.8f, 0.6f, 1.0f));
        verify(graphics2DMock, times(1)).draw(any(Shape.class));
    }

    @Test
    public void testPaintName() {
        robotRenderer.paintName(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(1)).setPaint(Color.yellow);
        verify(graphics2DMock, times(1)).drawString(robotSnapshot.getName(), anyFloat(), anyFloat());
    }
}