package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {

    private RobotRenderer robotRenderer;
    private Graphics2D graphics2dMock;
    private RobotSnapshot robotSnapshot;
    private Set<Integer> selectedRobotIds;

    @org.junit.Before
    public void setup() {
        this.robotRenderer = new RobotRenderer();
        this.graphics2dMock = mock(Graphics2D.class);
        this.robotSnapshot = mock(RobotSnapshot.class);
        this.selectedRobotIds = new HashSet<>();
    }

    @org.junit.Test
    public void testRender_RobotIsNotDead_RenderBodyShieldTurretNameArmorHeatSelection() {
        when(this.robotSnapshot.isDead()).thenReturn(false);

        this.robotRenderer.render(this.graphics2dMock, this.robotSnapshot, this.selectedRobotIds);

        verify(this.graphics2dMock, times(1)).drawString(eq(this.robotSnapshot.getName()), anyFloat(), anyFloat());
        verify(this.graphics2dMock, times(1)).paint(eq(Color.yellow));
        verify(this.graphics2dMock, times(1)).fill(any(Ellipse2D.class));
        verify(this.graphics2dMock, times(1)).setPaint(eq(new RadialGradientPaint(anyDouble(), anyDouble(), anyFloatArray(), anyColorArray())));
        verify(this.graphics2dMock, times(1)).fill(any(Rectangle2D.class));
        verify(this.graphics2dMock, times(1)).fill(any(Ellipse2D.class));
        verify(this.graphics2dMock, times(1)).fill(any(Rectangle2D.class));
        verify(this.graphics2dMock, times(1)).draw(any(Line2D.class));
        verify(this.graphics2dMock, times(1)).setPaint(eq(Color.white));
        verify(this.graphics2dMock, times(1)).fill(any(GeneralPath.class));
        verify(this.graphics2dMock, times(1)).setStroke(eq(new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f)));
        verify(this.graphics2dMock, times(1)).setPaint(eq(Color.orange.darker().darker()));
        verify(this.graphics2dMock, times(1)).draw(any(GeneralPath.class));
        verify(this.graphics2dMock, times(1)).setStroke(eq(mock(Stroke.class)));
    }

    @org.junit.Test
    public void testRender_RobotIsDead_DoNotRenderBodyShieldTurretNameArmorHeatSelection() {
        when(this.robotSnapshot.isDead()).thenReturn(true);

        this.robotRenderer.render(this.graphics2dMock, this.robotSnapshot, this.selectedRobotIds);

        verify(this.graphics2dMock, never()).drawString(eq(this.robotSnapshot.getName()), anyFloat(), anyFloat());
        verify(this.graphics2dMock, never()).fill(any(Ellipse2D.class));
        verify(this.graphics2dMock, never()).setPaint(eq(new RadialGradientPaint(anyDouble(), anyDouble(), anyFloatArray(), anyColorArray())));
        verify(this.graphics2dMock, never()).fill(any(Rectangle2D.class));
        verify(this.graphics2dMock, never()).fill(any(Ellipse2D.class));
        verify(this.graphics2dMock, never()).fill(any(Rectangle2D.class));
        verify(this.graphics2dMock, never()).draw(any(Line2D.class));
        verify(this.graphics2dMock, never()).setPaint(eq(Color.white));
        verify(this.graphics2dMock, never()).fill(any(GeneralPath.class));
    }

    @org.junit.Test
    public void testPaintSelection() {
        this.robotRenderer.paintSelection(this.graphics2dMock, this.robotSnapshot);

        verify(this.graphics2dMock, times(1)).
                setPaint(eq(new Color(1.0f, 1.0f, 0.0f, 0.25f)));
        verify(this.graphics2dMock, times(1)).fill(any(Ellipse2D.class));
    }

    @org.junit.Test
    public void testPaintShield() {
        this.robotRenderer.paintShield(this.graphics2dMock, this.robotSnapshot);

        verify(this.graphics2dMock, never()).setPaint(eq(new RadialGradientPaint(anyDouble(), anyDouble(), anyFloatArray(), anyColorArray())));
        verify(this.graphics2dMock, times(1)).setPaint(eq(new Color(0.8f, 0.6f, 1.0f)));
        verify(this.graphics2dMock, times(1)).draw(any(Ellipse2D.class));

        when(this.robotSnapshot.isActiveShield()).thenReturn(true);
        this.robotRenderer.paintShield(this.graphics2dMock, this.robotSnapshot);

        verify(this.graphics2dMock, times(1)).setPaint(eq(new RadialGradientPaint(anyDouble(), anyDouble(), anyFloatArray(), anyColorArray())));
        verify(this.graphics2dMock, times(2)).fill(any(Ellipse2D.class));
    }

    @org.junit.Test
    public void testPaintName() {
        this.robotRenderer.paintName(this.graphics2dMock, this.robotSnapshot);

        verify(this.graphics2dMock, times(1)).setPaint(eq(Color.yellow));
        verify(this.graphics2dMock, times(1)).drawString(eq(this.robotSnapshot.getName()), anyFloat(), anyFloat());
    }

    @org.junit.Test
    public void testPaintHeat() {
        this.robotRenderer.paintHeat(this.graphics2dMock, this.robotSnapshot);

        verify(this.graphics2dMock, times(1)).setPaint(eq(new GradientPaint((float) anyDouble(), (float) anyDouble(), RobotRenderer.HEAT_COLOR1, (float) anyDouble(), (float) anyDouble(), RobotRenderer.HEAT_COLOR2)));
        verify(this.graphics2dMock, times(1)).fill(any(Rectangle2D.class));
        verify(this.graphics2dMock, times(1)).draw(any(Rectangle2D.class));
    }

    @org.junit.Test
    public void testPaintArmor() {
        this.robotRenderer.paintArmor(this.graphics2dMock, this.robotSnapshot);

        verify(this.graphics2dMock, times(1)).setPaint(eq(new Color(0.0f, 0.0f, 1.0f, 0.6f)));
        verify(this.graphics2dMock, times(1)).fill(any(Rectangle2D.class));
        verify(this.graphics2dMock, times(1)).setPaint(eq(new Color(0.0f, 1.0f, 0.0f, 0.6f)));
        verify(this.graphics2dMock, times(1)).draw(any(Rectangle2D.class));
    }

    @org.junit.Test
    public void testPaintTurret() {
        this.robotRenderer.paintTurret(this.graphics2dMock, this.robotSnapshot);

        verify(this.graphics2dMock, times(1)).setPaint(eq(Color.white));
        verify(this.graphics2dMock, times(1)).draw(any(Line2D.class));
    }

    @org.junit.Test
    public void testPaintBody() {
        this.robotRenderer.paintBody(this.graphics2dMock, this.robotSnapshot);

        if (this.robotSnapshot.isDead()) {
            verify(this.graphics2dMock, times(1)).setPaint(eq(Color.orange.darker().darker()));
            verify(this.graphics2dMock, times(1)).setStroke(eq(new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f)));
            verify(this.graphics2dMock, times(1)).draw(any(GeneralPath.class));
            verify(this.graphics2dMock, times(1)).setStroke(eq(mock(Stroke.class)));
        } else {
            verify(this.graphics2dMock, times(1)).setPaint(eq(Color.red));
            verify(this.graphics2dMock, times(1)).fill(any(GeneralPath.class));
        }
    }
}