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

    private static final double DELTA = 0.001;
    private static final int ID = 1;
    private static final double X = 10.0;
    private static final double Y = 10.0;
    private static final double TEMPERATURE = 50.0;
    private static final double ARMOR = 100.0;
    private static final double HEADING = Math.PI / 4.0;
    private static final double TURRET_HEADING = Math.PI / 4.0;
    private static final boolean IS_ACTIVE_SHIELD = true;
    private static final boolean IS_DEAD = false;
    private static final boolean IS_FILLED_SHIELD = true;

    private RobotRenderer robotRenderer;
    private Graphics2D graphics2DMock;
    private RobotSnapshot robotSnapshot;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        robotRenderer = new RobotRenderer();
        graphics2DMock = mock(Graphics2D.class);
        robotSnapshot = new RobotSnapshot(ID, X, Y, HEADING, TEMPERATURE, ARMOR, TURRET_HEADING, IS_ACTIVE_SHIELD, IS_DEAD);
    }

    @Test
    public void testRender_whenRobotIsNotDead_thenRenderAllComponents() {
        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.render(graphics2DMock, robotSnapshot, Collections.emptySet());

        verify(graphics2DMock, times(1)).setPaint(Color.red);
        verify(graphics2DMock, times(1)).fill(any(GeneralPath.class));
        verify(graphics2DMock, times(1)).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
        verify(graphics2DMock, times(1)).fill(any(Rectangle2D.Double.class));
        verify(graphics2DMock, times(1)).setPaint(new Color(0.0f, 1.0f, 0.0f, 0.6f));
        verify(graphics2DMock, times(1)).draw(any(Rectangle2D.Double.class));
        verify(graphics2DMock, times(1)).setPaint(Color.yellow);
        verify(graphics2DMock, times(1)).drawString(robotSnapshot.getName(), (float)(X - graphics2DMock.getFontMetrics().getStringBounds(robotSnapshot.getName(), graphics2DMock).getWidth() / 2.0), (float)(Y - 10.0));
        verify(graphics2DMock, times(1)).setPaint(new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white }));
        if (IS_ACTIVE_SHIELD) {
            verify(graphics2DMock, times(1)).fill(any(Ellipse2D.Double.class));
            verify(graphics2DMock, times(1)).fill(any(Ellipse2D.Double.class));
        } else {
            verify(graphics2DMock, times(1)).setPaint(new Color(0.8f, 0.6f, 1.0f));
            verify(graphics2DMock, times(1)).draw(any(Ellipse2D.Double.class));
        }
        verify(graphics2DMock, times(1)).setPaint(new Color(1.0f, 0.0f, 0.0f, 0.25f));
        verify(graphics2DMock, times(1)).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testRender_whenRobotIsDead_thenNoRenderingIsPerformed() {
        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.render(graphics2DMock, new RobotSnapshot(ID, X, Y, HEADING, TEMPERATURE, ARMOR, TURRET_HEADING, IS_ACTIVE_SHIELD, true), Collections.emptySet());

        verify(graphics2DMock, never()).setPaint(Color.red);
        verify(graphics2DMock, never()).fill(any(GeneralPath.class));
        verify(graphics2DMock, never()).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
        verify(graphics2DMock, never()).fill(any(Rectangle2D.Double.class));
        verify(graphics2DMock, never()).setPaint(new Color(0.0f, 1.0f, 0.0f, 0.6f));
        verify(graphics2DMock, never()).draw(any(Rectangle2D.Double.class));
        verify(graphics2DMock, never()).setPaint(Color.yellow);
        verify(graphics2DMock, never()).drawString(robotSnapshot.getName(), (float)(X - graphics2DMock.getFontMetrics().getStringBounds(robotSnapshot.getName(), graphics2DMock).getWidth() / 2.0), (float)(Y - 10.0));
        verify(graphics2DMock, never()).setPaint(new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white }));
        verify(graphics2DMock, never()).fill(any(Ellipse2D.Double.class));
        verify(graphics2DMock, never()).fill(any(Ellipse2D.Double.class));
        verify(graphics2DMock, never()).setPaint(new Color(1.0f, 0.0f, 0.0f, 0.25f));
        verify(graphics2DMock, never()).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintSelection_whenRobotIsSelected_thenPaintSelectionEllipse() {
        when(graphics2DMock.getPaint()).thenReturn(Color.BLACK);
        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.paintSelection(graphics2DMock, robotSnapshot);

        verify(graphics2DMock, times(1)).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
        verify(graphics2DMock, times(1)).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShield_whenActiveShield_thenFillShieldWithGradient() {
        when(graphics2DMock.getPaint()).thenReturn(Color.BLACK);
        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.paintShield(graphics2DMock, robotSnapshot);

        verify(graphics2DMock, times(1)).setPaint(new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white }));
        verify(graphics2DMock, times(1)).fill(any(Ellipse2D.Double.class));
        verify(graphics2DMock, times(1)).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShield_whenNoActiveShield_thenDrawShieldWithColor() {
        when(graphics2DMock.getPaint()).thenReturn(Color.BLACK);
        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.paintShield(graphics2DMock, new RobotSnapshot(ID, X, Y, HEADING, TEMPERATURE, ARMOR, TURRET_HEADING, IS_ACTIVE_SHIELD, false));

        verify(graphics2DMock, times(1)).setPaint(new Color(0.8f, 0.6f, 1.0f));
        verify(graphics2DMock, times(1)).draw(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintName_whenShowName_thenPaintNameString() {
        when(graphics2DMock.getPaint()).thenReturn(Color.BLACK);
        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.paintName(graphics2DMock, robotSnapshot);

        verify(graphics2DMock, times(1)).setPaint(Color.yellow);
        verify(graphics2DMock, times(1)).drawString(robotSnapshot.getName(), (float)(X - graphics2DMock.getFontMetrics().getStringBounds(robotSnapshot.getName(), graphics2DMock).getWidth() / 2.0), (float)(Y - 10.0));
    }

    @Test
    public void testPaintHeat_whenRobotIsNotDead_thenPaintHeatBar() {
        when(graphics2DMock.getPaint()).thenReturn(Color.BLACK);
        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.paintHeat(graphics2DMock, robotSnapshot);

        verify(graphics2DMock, times(1)).setPaint(new GradientPaint((float)robotSnapshot.getX() - 50.0, (float)robotSnapshot.getY() + 35.0, RobotRenderer.HEAT_COLOR1, (float)robotSnapshot.getX() - 50.0, (float)robotSnapshot.getY() + 35.0, RobotRenderer.HEAT_COLOR2));
        if (IS_ACTIVE_SHIELD) {
            verify(graphics2DMock, times(1)).fill(new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, robotSnapshot.getTemperature().getLogScale() * 0.2, 10.0));
        } else {
            verify(graphics2DMock, times(1)).draw(new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, robotSnapshot.getTemperature().getLogScale() * 0.2, 10.0));
        }
        verify(graphics2DMock, times(1)).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintArmor_whenShowArmor_thenPaintArmorBar() {
        when(graphics2DMock.getPaint()).thenReturn(Color.BLACK);
        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.paintArmor(graphics2DMock, robotSnapshot);

        verify(graphics2DMock, times(1)).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
        verify(graphics2DMock, times(1)).fill(any(Rectangle2D.Double.class));
        verify(graphics2DMock, times(1)).setPaint(new Color(0.0f, 1.0f, 0.0f, 0.6f));
        verify(graphics2DMock, times(1)).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintTurret_whenRenderTurret_thenPaintTurretLine() {
        when(graphics2DMock.getPaint()).thenReturn(Color.BLACK);
        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.paintTurret(graphics2DMock, robotSnapshot);

        verify(graphics2DMock, times(1)).setPaint(Color.white);
        verify(graphics2DMock, times(1)).draw(new Line2D.Double(X, Y, X + Math.cos(HEADING) * 5.0, Y + Math.sin(HEADING) * 5.0));
    }

    @Test
    public void testPaintBody_whenRenderBody_thenPaintBodyShape() {
        when(graphics2DMock.getPaint()).thenReturn(Color.BLACK);
        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.paintBody(graphics2DMock, robotSnapshot);

        verify(graphics2DMock, times(1)).setPaint(Color.red);
        verify(graphics2DMock, times(1)).fill(any(GeneralPath.class));

        if (!IS_DEAD) {
            verify(graphics2DMock, times(1)).setPaint(Color.red);
            verify(graphics2DMock, times(1)).fill(any(GeneralPath.class));
        }
    }

    private FontMetrics createFontMetricsMock() {
        return mock(FontMetrics.class);
    }
}