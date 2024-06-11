package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {

    private RobotRenderer robotRenderer;
    private Graphics2D graphics2DMock;
    private RobotSnapshot robotSnapshot;
    private Set<Integer> selectedRobotIds;

    @org.junit.Before
    public void setUp() {
        robotRenderer = new RobotRenderer();
        graphics2DMock = mock(Graphics2D.class);
        robotSnapshot = new RobotSnapshot(1L, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, "RobotName", new AbsoluteAngle(1.0));
        selectedRobotIds = new HashSet<>();
    }

    @org.junit.Test
    public void testRender() {
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);
        verify(graphics2DMock, atLeastOnce()).setPaint(any(Color.class));
        verify(graphics2DMock, atLeastOnce()).draw(any(Shape.class));
        verify(graphics2DMock, atLeastOnce()).fill(any(Shape.class));
        verify(graphics2DMock, atLeastOnce()).setStroke(any(Stroke.class));
    }

    @org.junit.Test
    public void testIsRenderDead() {
        assertTrue(robotRenderer.isRenderDead());
        robotRenderer.setRenderDead(false);
        assertFalse(robotRenderer.isRenderDead());
    }

    @org.junit.Test
    public void testPaintSelection() {
        robotRenderer.paintSelection(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, atLeastOnce()).setPaint(any(Color.class));
        verify(graphics2DMock, atLeastOnce()).fill(any(Shape.class));
    }

    @org.junit.Test
    public void testPaintShield() {
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        robotRenderer.paintShield(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(2)).setPaint(any(Paint.class));
        verify(graphics2DMock, times(1)).fill(any(Shape.class));
        verify(graphics2DMock, times(1)).draw(any(Shape.class));

        when(robotSnapshot.isActiveShield()).thenReturn(false);
        robotRenderer.paintShield(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(1)).setPaint(any(Color.class));
        verify(graphics2DMock, times(1)).draw(any(Shape.class));

        when(robotSnapshot.isActiveShield()).thenReturn(true);
        robotRenderer.setFillShield(false);
        robotRenderer.paintShield(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(1)).setPaint(any(Color.class));
        verify(graphics2DMock, times(1)).draw(any(Shape.class));
    }

    @org.junit.Test
    public void testPaintName() {
        robotRenderer.paintName(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, atLeastOnce()).setPaint(any(Color.class));
        verify(graphics2DMock, atLeastOnce()).drawString(eq("RobotName"), anyFloat(), anyFloat());
    }

    @org.junit.Test
    public void testPaintHeat() {
        robotRenderer.paintHeat(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, atLeastOnce()).setPaint(any(GradientPaint.class));
        verify(graphics2DMock, atLeastOnce()).fill(any(Shape.class));
        verify(graphics2DMock, atLeastOnce()).draw(any(Shape.class));
    }

    @org.junit.Test
    public void testPaintArmor() {
        robotRenderer.paintArmor(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, atLeastOnce()).setPaint(any(Color.class));
        verify(graphics2DMock, atLeastOnce()).fill(any(Shape.class));
        verify(graphics2DMock, atLeastOnce()).setPaint(any(Color.class));
        verify(graphics2DMock, atLeastOnce()).draw(any(Shape.class));
    }

    @org.junit.Test
    public void testPaintTurret() {
        robotRenderer.paintTurret(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, atLeastOnce()).setPaint(any(Color.class));
        verify(graphics2DMock, atLeastOnce()).draw(any(Shape.class));
    }

    @org.junit.Test
    public void testPaintBody() {
        robotRenderer.paintBody(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, atLeastOnce()).setPaint(any(Color.class));
        verify(graphics2DMock, atLeastOnce()).fill(any(Shape.class));
        verify(graphics2DMock, atLeastOnce()).setStroke(any(Stroke.class));
        verify(graphics2DMock, atLeastOnce()).draw(any(Shape.class));
    }
}