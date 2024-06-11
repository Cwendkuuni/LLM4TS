package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.virtualinfinity.atrobots.measures.Length;
import net.virtualinfinity.atrobots.measures.Measure;
import net.virtualinfinity.atrobots.measures.Velocity;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {

    private RobotSnapshot createRobotSnapshot(int id, double x, double y, Velocity velocity, AbsoluteAngle heading, AbsoluteAngle turretHeading, Length armor, Length temperature, boolean isDead, boolean isActiveShield) {
        return new RobotSnapshot(id, x, y, velocity, heading, turretHeading, armor, temperature, isDead, isActiveShield);
    }

    private Graphics2D createMockGraphics2D() {
        Graphics2D mockGraphics2D = mock(Graphics2D.class);
        when(mockGraphics2D.getFontMetrics()).thenReturn(mock(FontMetrics.class));
        return mockGraphics2D;
    }

    @Test
    public void testRenderWhenRobotIsDead() {
        RobotRenderer renderer = new RobotRenderer();
        Graphics2D graphics2D = createMockGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(-1, 1.0, 1.0, null, null, null, null, null, true, false);

        renderer.render(graphics2D, robotSnapshot, new HashSet<>());

        verify(graphics2D, times(1)).setPaint(any());
        verify(graphics2D, times(1)).setStroke(any());
        verify(graphics2D, times(1)).draw(any(Line2D.class));
        verify(graphics2D, never()).fill(any(Shape.class));
        verify(graphics2D, never()).drawString(anyString(), anyFloat(), anyFloat());
        verify(graphics2D, never()).setPaint(any(RadialGradientPaint.class));
        verify(graphics2D, never()).fill(any(Shape.class));
        verify(graphics2D, never()).draw(any(Shape.class));
    }

    @Test
    public void testRenderWhenRobotIsAlive() {
        RobotRenderer renderer = new RobotRenderer();
        Graphics2D graphics2D = createMockGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(1, 1.0, 1.0, null, new AbsoluteAngle(Math.PI / 2), new AbsoluteAngle(Math.PI), new Length(10.0), new Temperature(50.0), false, false);

        renderer.render(graphics2D, robotSnapshot, new HashSet<>());

        verify(graphics2D, times(1)).setPaint(any());
        verify(graphics2D, times(1)).setStroke(any());
        verify(graphics2D, times(1)).fill(any(Shape.class));
        verify(graphics2D, times(1)).drawString(eq(robotSnapshot.getName()), anyFloat(), anyFloat());
        verify(graphics2D, times(1)).setPaint(any(GradientPaint.class));
        verify(graphics2D, times(1)).fill(any(Shape.class));
        verify(graphics2D, times(1)).draw(any(Shape.class));
    }

    @Test
    public void testPaintSelection() {
        RobotRenderer renderer = new RobotRenderer();
        Graphics2D graphics2D = createMockGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(1, 1.0, 1.0, null, null, null, null, null, false, false);

        renderer.paintSelection(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(eq(new Color(1.0f, 1.0f, 0.0f, 0.25f)));
        verify(graphics2D, times(1)).fill(any(Ellipse2D.class));
    }

    @Test
    public void testPaintShield() {
        RobotRenderer renderer = new RobotRenderer();
        Graphics2D graphics2D = createMockGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(1, 1.0, 1.0, null, null, null, new Length(50.0), null, false, true);

        renderer.paintShield(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(any(RadialGradientPaint.class));
        verify(graphics2D, times(1)).fill(any(Ellipse2D.class));
        verify(graphics2D, times(1)).fill(any(Ellipse2D.class));
    }

    @Test
    public void testPaintName() {
        RobotRenderer renderer = new RobotRenderer();
        Graphics2D graphics2D = createMockGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(1, 1.0, 1.0, null, null, null, null, null, false, false);

        renderer.paintName(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(eq(Color.yellow));
        verify(graphics2D, times(1)).drawString(eq(robotSnapshot.getName()), anyFloat(), anyFloat());
    }

    @Test
    public void testPaintHeat() {
        RobotRenderer renderer = new RobotRenderer();
        Graphics2D graphics2D = createMockGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(1, 1.0, 1.0, null, null, null, null, new Temperature(50.0), false, false);

        renderer.paintHeat(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(any(GradientPaint.class));
        verify(graphics2D, times(1)).fill(any(Rectangle2D.class));
        verify(graphics2D, times(1)).draw(any(Rectangle2D.class));
    }

    @Test
    public void testPaintArmor() {
        RobotRenderer renderer = new RobotRenderer();
        Graphics2D graphics2D = createMockGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(1, 1.0, 1.0, null, null, null, new Length(50.0), null, false, false);

        renderer.paintArmor(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(eq(new Color(0.0f, 0.0f, 1.0f, 0.6f)));
        verify(graphics2D, times(1)).fill(any(Rectangle2D.class));
        verify(graphics2D, times(1)).setPaint(eq(new Color(0.0f, 1.0f, 0.0f, 0.6f)));
        verify(graphics2D, times(1)).draw(any(Rectangle2D.class));
    }

    @Test
    public void testPaintTurret() {
        RobotRenderer renderer = new RobotRenderer();
        Graphics2D graphics2D = createMockGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(1, 1.0, 1.0, null, null, null, null, null, false, false);

        renderer.paintTurret(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(eq(Color.white));
        verify(graphics2D, times(1)).draw(any(Line2D.class));
    }

    @Test
    public void testPaintBody() {
        RobotRenderer renderer = new RobotRenderer();
        Graphics2D graphics2D = createMockGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(1, 1.0, 1.0, null, null, null, null, null, false, false);

        renderer.paintBody(graphics2D, robotSnapshot);

        verify(graphics2D, times(2)).setPaint(any());
        verify(graphics2D, times(1)).fill(any(GeneralPath.class));
        verify(graphics2D, times(1)).draw(any(GeneralPath.class));
        verify(graphics2D, times(1)).setStroke(any());
    }

    @Test
    public void testIsRenderDead() {
        RobotRenderer renderer = new RobotRenderer();

        assertTrue(renderer.isRenderDead());

        renderer.setRenderDead(false);

        assertFalse(renderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars() {
        RobotRenderer renderer = new RobotRenderer();

        assertTrue(renderer.isShowStatusBars());

        renderer.setShowStatusBars(false);

        assertFalse(renderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName() {
        RobotRenderer renderer = new RobotRenderer();

        assertTrue(renderer.isShowName());

        renderer.setShowName(false);

        assertFalse(renderer.isShowName());
    }

    @Test
    public void testIsFillShield() {
        RobotRenderer renderer = new RobotRenderer();

        assertTrue(renderer.isFillShield());

        renderer.setFillShield(false);

        assertFalse(renderer.isFillShield());
    }
}