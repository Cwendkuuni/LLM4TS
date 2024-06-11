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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        robotSnapshot = new RobotSnapshot(1L, new PositionVector(1.0, 2.0), new VelocityVector(0.0, 0.0), 10.0, 50.0, 10.0, 5.0, new AbsoluteAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0), true, true, false, new String("Name"), null);
        graphics2DMock = mock(Graphics2D.class);
        robotRenderer = new RobotRenderer();
    }

    @Test
    public void testRender_RobotIsNotDead_AllComponentsAreDrawn() {
        RobotSnapshot robotSnapshot = new RobotSnapshot(1L, new PositionVector(1.0, 2.0), new VelocityVector(0.0, 0.0), 10.0, 50.0, 10.0, 5.0, new AbsoluteAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0), false, true, false, new String("Name"), null);
        when(graphics2DMock.getColor()).thenReturn(Color.RED);
        when(graphics2DMock.getStroke()).thenReturn(new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f));

        robotRenderer.render(graphics2DMock, robotSnapshot, new HashSet<>());

        verify(graphics2DMock).draw(any(Shape.class)); // body
        verify(graphics2DMock, times(2)).fill(any(Shape.class)); // shield and name
        verify(graphics2DMock).draw(any(Shape.class)); // turret
        verifyNoMoreInteractions(graphics2DMock);
    }

    @Test
    public void testRender_RobotIsDead_OnlySelectionIsDrawn() {
        RobotSnapshot robotSnapshot = new RobotSnapshot(1L, new PositionVector(1.0, 2.0), new VelocityVector(0.0, 0.0), 10.0, 50.0, 10.0, 5.0, new AbsoluteAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0), true, false, false, new String("Name"), null);
        when(graphics2DMock.getColor()).thenReturn(Color.YELLOW.withAlpha(0.25f));
        when(graphics2DMock.getStroke()).thenReturn(new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f));

        robotRenderer.render(graphics2DMock, robotSnapshot, new HashSet<>());

        verify(graphics2DMock).fill(eq(new Ellipse2D.Double())); // selection
        verifyNoMoreInteractions(graphics2DMock);
    }

    @Test
    public void testRender_RobotIsDeadAndSelected_BothSelectionAndComponentsAreDrawn() {
        RobotSnapshot robotSnapshot = new RobotSnapshot(1L, new PositionVector(1.0, 2.0), new VelocityVector(0.0, 0.0), 10.0, 50.0, 10.0, 5.0, new AbsoluteAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0), true, false, false, new String("Name"), null);
        when(graphics2DMock.getColor()).thenReturn(Color.YELLOW.withAlpha(0.25f));
        when(graphics2DMock.getStroke()).thenReturn(new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f));

        robotRenderer.render(graphics2DMock, robotSnapshot, new HashSet<>(Arrays.asList(1L)));

        verify(graphics2DMock).fill(eq(new Ellipse2D.Double())); // selection
        verify(graphics2DMock).draw(any(Shape.class)); // body
        verify(graphics2DMock, times(2)).fill(any(Shape.class)); // shield and name
        verify(graphics2DMock).draw(any(Shape.class)); // turret
        verifyNoMoreInteractions(graphics2DMock);
    }

    @Test
    public void testIsRenderDead_DefaultValueIsTrue() {
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testIsRenderDead_ValueCanBeChanged() {
        robotRenderer.setRenderDead(false);
        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars_DefaultValueIsTrue() {
        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowStatusBars_ValueCanBeChanged() {
        robotRenderer.setShowStatusBars(false);
        assertFalse(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName_DefaultValueIsTrue() {
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testIsShowName_ValueCanBeChanged() {
        robotRenderer.setShowName(false);
        assertFalse(robotRenderer.isShowName());
    }

    @Test
    public void testIsFillShield_DefaultValueIsTrue() {
        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testIsFillShield_ValueCanBeChanged() {
        robotRenderer.setFillShield(false);
        assertFalse(robotRenderer.isFillShield());
    }
}