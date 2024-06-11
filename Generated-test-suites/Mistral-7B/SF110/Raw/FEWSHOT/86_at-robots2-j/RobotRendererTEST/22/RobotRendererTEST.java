package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.renderers.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.Test;

public class RobotRendererTest {

    @Test
    public void testRenderWithNullSnapshot() {
        Graphics2D g2dMock = mock(Graphics2D.class);
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.render(g2dMock, null, new HashSet<>());
        verify(g2dMock, never()).paintBody(any(RobotSnapshot.class));
        verify(g2dMock, never()).paintShield(any(RobotSnapshot.class));
        verify(g2dMock, never()).paintTurret(any(RobotSnapshot.class));
        verify(g2dMock, never()).paintArmor(any(RobotSnapshot.class));
        verify(g2dMock, never()).paintHeat(any(RobotSnapshot.class));
        verify(g2dMock, never()).paintName(any(RobotSnapshot.class));
        verify(g2dMock, never()).paintSelection(any(RobotSnapshot.class));
    }

    @Test
    public void testRenderWithDeadRobotSnapshot() {
        Graphics2D g2dMock = mock(Graphics2D.class);
        RobotSnapshot deadRobotSnapshot = new RobotSnapshot();
        deadRobotSnapshot.setDead(true);
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.render(g2dMock, deadRobotSnapshot, new HashSet<>());
        verify(g2dMock, never()).paintShield(any(RobotSnapshot.class));
        verify(g2dMock, never()).paintTurret(any(RobotSnapshot.class));
        verify(g2dMock, never()).paintArmor(any(RobotSnapshot.class));
        verify(g2dMock, never()).paintHeat(any(RobotSnapshot.class));
        verify(g2dMock, never()).paintName(any(RobotSnapshot.class));
        verify(g2dMock, never()).paintSelection(any(RobotSnapshot.class));
        verify(g2dMock).setPaint(argThat(p -> p.equals(Color.orange.darker().darker())));
        verify(g2dMock).setStroke(argThat(s -> s.equals(new BasicStroke(1.0f, 1, 0, 10.0f, new float[]{5.0f, 5.0f}, 0.0f))));
        verify(g2dMock).fill(any(Shape.class));
    }

    @Test
    public void testRenderWithNonDeadRobotSnapshot() {
        Graphics2D g2dMock = mock(Graphics2D.class);
        RobotSnapshot nonDeadRobotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.render(g2dMock, nonDeadRobotSnapshot, new HashSet<>());
        verify(g2dMock, times(1)).paintBody(nonDeadRobotSnapshot);
        verify(g2dMock, times(1)).paintShield(nonDeadRobotSnapshot);
        verify(g2dMock, times(1)).paintTurret(nonDeadRobotSnapshot);
        verify(g2dMock, times(1)).paintArmor(nonDeadRobotSnapshot);
        verify(g2dMock, times(1)).paintHeat(nonDeadRobotSnapshot);
        verify(g2dMock, times(1)).paintName(nonDeadRobotSnapshot);
        verify(g2dMock, times(1)).paintSelection(nonDeadRobotSnapshot);
    }

    @Test
    public void testPaintSelectionWithNullGraphics2D() {
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintSelection(null, robotSnapshot);
        fail("Expected NullPointerException");
    }

    @Test
    public void testPaintSelection() {
        Graphics2D g2dMock = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintSelection(g2dMock, robotSnapshot);
        verify(g2dMock).setPaint(argThat(p -> p.equals(new Color(1.0f, 1.0f, 0.0f, 0.25f))));
        verify(g2dMock).fill(argThat(s -> s instanceof Ellipse2D.Double));
    }

    @Test
    public void testPaintShieldWithNullGraphics2D() {
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintShield(null, robotSnapshot);
        fail("Expected NullPointerException");
    }

    @Test
    public void testPaintShieldWithRobotSnapshotWithoutShield() {
        Graphics2D g2dMock = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintShield(g2dMock, robotSnapshot);
        verify(g2dMock, never()).setPaint(any(Paint.class));
        verify(g2dMock, never()).fill(any(Shape.class));
        verify(g2dMock, never()).draw(any(Shape.class));
    }

    @Test
    public void testPaintShieldWithRobotSnapshotWithShield() {
        Graphics2D g2dMock = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintShield(g2dMock, robotSnapshot);
        verify(g2dMock).setPaint(argThat(p -> p instanceof RadialGradientPaint || p instanceof Color));
        verify(g2dMock, times(2)).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintNameWithNullGraphics2D() {
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintName(null, robotSnapshot);
        fail("Expected NullPointerException");
    }

    @Test
    public void testPaintName() {
        Graphics2D g2dMock = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintName(g2dMock, robotSnapshot);
        verify(g2dMock).setPaint(argThat(p -> p.equals(Color.yellow)));
        verify(g2dMock).drawString(robotSnapshot.getName(), argThat(f -> f > -1000 && f < 1000), argThat(f -> f > -10 && f < 110));
    }

    @Test
    public void testPaintHeatWithNullGraphics2D() {
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintHeat(null, robotSnapshot);
        fail("Expected NullPointerException");
    }

    @Test
    public void testPaintHeatWithRobotSnapshotWithoutTemperature() {
        Graphics2D g2dMock = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintHeat(g2dMock, robotSnapshot);
        verify(g2dMock, never()).setPaint(any(Paint.class));
        verify(g2dMock, never()).fill(any(Rectangle2D.Double.class));
        verify(g2dMock, never()).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintHeatWithRobotSnapshotWithTemperature() {
        Graphics2D g2dMock = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintHeat(g2dMock, robotSnapshot);
        verify(g2dMock).setPaint(argThat(p -> p instanceof GradientPaint || p instanceof Color));
        verify(g2dMock, times(1)).fill(any(Rectangle2D.Double.class));
        verify(g2dMock, times(1)).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintArmorWithNullGraphics2D() {
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintArmor(null, robotSnapshot);
        fail("Expected NullPointerException");
    }

    @Test
    public void testPaintArmor() {
        Graphics2D g2dMock = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintArmor(g2dMock, robotSnapshot);
        verify(g2dMock).setPaint(argThat(p -> p.equals(new Color(0.0f, 0.0f, 1.0f, 0.6f))));
        verify(g2dMock).fill(any(Rectangle2D.Double.class));
        verify(g2dMock).setPaint(argThat(p -> p.equals(new Color(0.0f, 1.0f, 0.0f, 0.6f))));
        verify(g2dMock).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintTurretWithNullGraphics2D() {
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintTurret(null, robotSnapshot);
        fail("Expected NullPointerException");
    }

    @Test
    public void testPaintTurret() {
        Graphics2D g2dMock = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintTurret(g2dMock, robotSnapshot);
        verify(g2dMock).setPaint(argThat(p -> p.equals(Color.white)));
        verify(g2dMock).draw(argThat(l -> l instanceof Line2D.Double));
    }

    @Test
    public void testPaintBodyWithNullGraphics2D() {
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintBody(null, robotSnapshot);
        fail("Expected NullPointerException");
    }

    @Test
    public void testPaintBodyWithDeadRobotSnapshot() {
        Graphics2D g2dMock = mock(Graphics2D.class);
        RobotSnapshot deadRobotSnapshot = new RobotSnapshot();
        deadRobotSnapshot.setDead(true);
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintBody(g2dMock, deadRobotSnapshot);
        verify(g2dMock).setPaint(argThat(p -> p.equals(Color.orange.darker().darker())));
        verify(g2dMock).setStroke(argThat(s -> s.equals(new BasicStroke(1.0f, 1, 0, 10.0f, new float[]{5.0f, 5.0f}, 0.0f))));
        verify(g2dMock).fill(argThat(p -> p instanceof GeneralPath));
    }

    @Test
    public void testPaintBodyWithNonDeadRobotSnapshot() {
        Graphics2D g2dMock = mock(Graphics2D.class);
        RobotSnapshot nonDeadRobotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintBody(g2dMock, nonDeadRobotSnapshot);
        verify(g2dMock).setPaint(argThat(p -> p.equals(Color.red)));
        verify(g2dMock).setStroke(argThat(s -> s.equals(new BasicStroke(1.0f, 0, 0, 0, new float[]{5.0f, 5.0f}, 0.0f))));
        verify(g2dMock).fill(argThat(p -> p instanceof GeneralPath));
    }
}