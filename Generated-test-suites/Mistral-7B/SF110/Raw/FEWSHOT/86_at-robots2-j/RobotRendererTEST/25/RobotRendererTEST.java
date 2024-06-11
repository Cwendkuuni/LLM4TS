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
        robotSnapshot = mock(RobotSnapshot.class);
        selectedRobotIds = new HashSet<>();
    }

    @Test
    public void testRenderWithDeadRobotSnapshot() {
        when(robotSnapshot.isDead()).thenReturn(true);
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);
        verify(graphics2DMock, times(1)).setPaint(argThat(isA(Color.class).and(color -> color.equals(Color.orange.darker().darker()))));
        verify(graphics2DMock, times(1)).setStroke(argThat(isA(BasicStroke.class)));
        verify(graphics2DMock, times(1)).draw(any(GeneralPath.class));
    }

    @Test
    public void testRenderWithAliveRobotSnapshot() {
        when(robotSnapshot.isDead()).thenReturn(false);
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);
        verify(graphics2DMock, times(1)).setPaint(argThat(isA(Color.class).and(color -> color.equals(Color.red))));
        verify(graphics2DMock, times(1)).fill(any(GeneralPath.class));
        verify(graphics2DMock, times(1)).setStroke(eq(graphics2DMock.getStroke()));
    }

    @Test
    public void testPaintSelection() {
        robotRenderer.paintSelection(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(1)).setPaint(argThat(isA(Color.class).and(color -> color.equals(new Color(1.0f, 1.0f, 0.0f, 0.25f)))));
        verify(graphics2DMock, times(1)).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShield() {
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        robotRenderer.paintShield(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(1)).setPaint(argThat(isA(RadialGradientPaint.class)));
        verify(graphics2DMock, times(1)).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShieldWithoutFill() {
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        robotRenderer.setFillShield(false);
        robotRenderer.paintShield(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(1)).setPaint(argThat(isA(Color.class).and(color -> color.equals(new Color(0.8f, 0.6f, 1.0f)))));
        verify(graphics2DMock, times(1)).draw(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintName() {
        when(robotSnapshot.getName()).thenReturn("TestBot");
        robotRenderer.paintName(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(1)).setPaint(argThat(isA(Color.class).and(color -> color.equals(Color.yellow))));
        verify(graphics2DMock, times(1)).drawString("TestBot", anyFloat(), anyFloat());
    }

    @Test
    public void testPaintHeat() {
        when(robotSnapshot.isDead()).thenReturn(false);
        double temperature = 50.0;
        when(robotSnapshot.getTemperature()).thenReturn(new Temperature(temperature));
        robotRenderer.paintHeat(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(1)).setPaint(argThat(isA(GradientPaint.class)));
        verify(graphics2DMock, times(1)).fill(any(Rectangle2D.Double.class));
        verify(graphics2DMock, times(1)).draw(any(Rectangle2D.Double.class));
        assertEquals(5.0, graphics2DMock.getFontMetrics().getStringBounds("", graphics2DMock).getHeight(), 0.0);
    }

    @Test
    public void testPaintArmor() {
        when(robotSnapshot.getArmor()).thenReturn(50.0);
        robotRenderer.paintArmor(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(1)).setPaint(argThat(isA(Color.class).and(color -> color.equals(new Color(0.0f, 0.0f, 1.0f, 0.6f)))));
        verify(graphics2DMock, times(1)).fill(any(Rectangle2D.Double.class));
        verify(graphics2DMock, times(1)).setPaint(argThat(isA(Color.class).and(color -> color.equals(new Color(0.0f, 1.0f, 0.0f, 0.6f)))));
        verify(graphics2DMock, times(1)).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintTurret() {
        when(robotSnapshot.getTurretHeading()).thenReturn(new AbsoluteAngle(30.0));
        robotRenderer.paintTurret(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(1)).setPaint(argThat(isA(Color.class).and(color -> color.equals(Color.white))));
        verify(graphics2DMock, times(1)).draw(any(Line2D.Double.class));
    }

    @Test
    public void testPaintBody() {
        robotRenderer.paintBody(graphics2DMock, robotSnapshot);
        verify(graphics2DMock, times(1)).setPaint(argThat(isA(Color.class).and(color -> color.equals(Color.red))));
        verify(graphics2DMock, times(1)).fill(any(GeneralPath.class));
    }
}