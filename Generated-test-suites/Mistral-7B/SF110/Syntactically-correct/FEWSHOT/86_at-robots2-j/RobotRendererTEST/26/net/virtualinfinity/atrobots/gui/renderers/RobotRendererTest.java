package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.*;
import org.mockito.MockitoAnnotations;

public class RobotRendererTest {

    private RobotRenderer robotRenderer;
    private Graphics2D graphics2DMock;
    private RobotSnapshot robotSnapshot;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        robotRenderer = new RobotRenderer();
        graphics2DMock = mock(Graphics2D.class);
        robotSnapshot = new RobotSnapshot();
    }

    @Test
    public void testRenderRobotWithSelectedId() {
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(1);

        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        verify(graphics2DMock, times(1)).fill(any(Shape.class));
        verify(graphics2DMock, times(1)).setPaint(any(Color.class));
        verify(graphics2DMock, times(1)).drawString(robotSnapshot.getName(), anyFloat(), anyFloat());
        verify(graphics2DMock, times(1)).setPaint(eq(Color.YELLOW));
    }

    @Test
    public void testRenderRobotNotSelected() {
        Set<Integer> selectedRobotIds = new HashSet<>();

        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        verify(graphics2DMock, times(0)).fill(any(Shape.class));
        verify(graphics2DMock, times(0)).setPaint(any(Color.class));
        verify(graphics2DMock, times(0)).drawString(robotSnapshot.getName(), anyFloat(), anyFloat());
    }

    @Test
    public void testRenderDeadRobot() {
        robotSnapshot.setDead(true);

        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.render(graphics2DMock, robotSnapshot, Collections.emptySet());

        verify(graphics2DMock, times(1)).setStroke(any(Stroke.class));
        verify(graphics2DMock, times(1)).setPaint(any(Color.class));
        verify(graphics2DMock, times(1)).draw(any(Shape.class));
    }

    @Test
    public void testRenderRobotWithActiveShield() {
        robotSnapshot.setActiveShield(true);

        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.render(graphics2DMock, robotSnapshot, Collections.emptySet());

        verify(graphics2DMock, times(1)).setPaint(any(RadialGradientPaint.class));
        verify(graphics2DMock, times(2)).fill(any(Shape.class));
    }

    @Test
    public void testRenderRobotWithoutActiveShield() {
        robotSnapshot.setActiveShield(false);

        when(graphics2DMock.getFontMetrics()).thenReturn(createFontMetricsMock());

        robotRenderer.render(graphics2DMock, robotSnapshot, Collections.emptySet());

        verify(graphics2DMock, times(1)).setPaint(any(Color.class));
        verify(graphics2DMock, times(1)).draw(any(Shape.class));
    }

    private FontMetrics createFontMetricsMock() {
        FontMetrics fontMetricsMock = mock(FontMetrics.class);
        when(fontMetricsMock.getStringBounds(anyString(), any(Graphics2D.class))).thenReturn(new Rectangle2D.Double(0, 0, 10, 10));
        return fontMetricsMock;
    }
}