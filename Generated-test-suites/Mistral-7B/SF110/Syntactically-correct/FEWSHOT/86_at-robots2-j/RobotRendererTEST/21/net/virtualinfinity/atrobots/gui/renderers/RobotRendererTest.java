package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class RobotRendererTest {

    private RobotSnapshot robotSnapshot;
    private Graphics2D graphics2DMock;
    private Set<Integer> selectedRobotIds;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        robotSnapshot = mock(RobotSnapshot.class);
        graphics2DMock = mock(Graphics2D.class);
        selectedRobotIds = new HashSet<>();
    }

    @Test
    public void testRenderWhenRobotIsNotDead() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getTemperature()).thenReturn(new Temperature(25.0f));
        when(robotSnapshot.getArmor()).thenReturn(100.0);
        when(robotSnapshot.getHeading()).thenReturn(new RelativeAngle(Math.PI / 2.0));
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        when(robotSnapshot.getName()).thenReturn("TestRobot");

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        verify(graphics2DMock, times(1)).setPaint(any(Color.class));
        verify(graphics2DMock, times(1)).setStroke(any(Stroke.class));
        verify(graphics2DMock, times(1)).fill(any(Shape.class));
        verify(graphics2DMock, times(1)).draw(any(Shape.class));
        verifyZeroInteractions(graphics2DMock, robotSnapshot);

        // Test rendering body
        verify(graphics2DMock, times(1)).draw(any(Line2D.Double.class));
        verify(graphics2DMock, times(1)).fill(any(GeneralPath.class));

        // Test rendering shield
        verify(graphics2DMock, times(1)).setPaint(any(RadialGradientPaint.class));
        verify(graphics2DMock, times(1)).fill(any(Ellipse2D.Double.class));
        verify(graphics2DMock, times(1)).draw(any(Ellipse2D.Double.class));

        // Test rendering name
        verify(graphics2DMock, times(1)).setPaint(eq(Color.yellow));
        verify(graphics2DMock, times(1)).drawString(eq("TestRobot"), anyFloat(), anyFloat());

        // Test rendering heat
        verify(graphics2DMock, times(1)).setPaint(new GradientPaint(anyFloat(), anyFloat(), RobotRenderer.HEAT_COLOR1, anyFloat(), anyFloat(), RobotRenderer.HEAT_COLOR2));
        verify(graphics2DMock, times(1)).fill(any(Rectangle2D.Double.class));
        verify(graphics2DMock, times(1)).draw(any(Rectangle2D.Double.class));

        // Test rendering armor
        verify(graphics2DMock, times(1)).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
        verify(graphics2DMock, times(1)).fill(any(Rectangle2D.Double.class));
        verify(graphics2DMock, times(1)).setPaint(new Color(0.0f, 1.0f, 0.0f, 0.6f));
        verify(graphics2DMock, times(1)).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testRenderWhenRobotIsDead() {
        when(robotSnapshot.isDead()).thenReturn(true);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        verify(graphics2DMock, times(1)).setPaint(any(Color.class));
        verify(graphics2DMock, times(1)).setStroke(any(BasicStroke.class));
        verify(graphics2DMock, times(1)).setPaint(Color.orange.darker().darker());
        verify(graphics2DMock, times(1)).draw(any(GeneralPath.class));
        verifyZeroInteractions(graphics2DMock, robotSnapshot);
    }

    @Test
    public void testRenderWithSelectedRobot() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getTemperature()).thenReturn(new Temperature(25.0f));
        when(robotSnapshot.getArmor()).thenReturn(100.0);
        when(robotSnapshot.getHeading()).thenReturn(new RelativeAngle(Math.PI / 2.0));
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        when(robotSnapshot.getName()).thenReturn("TestRobot");

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setRenderDead(false);
        robotRenderer.setShowStatusBars(true);
        robotRenderer.setShowName(true);
        robotRenderer.setFillShield(true);

        selectedRobotIds.add(robotSnapshot.getId());

        RobotRenderer spyRobotRenderer = spy(robotRenderer);
        doCallRealMethod().when(spyRobotRenderer).render(graphics2DMock, robotSnapshot, selectedRobotIds);

        spyRobotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        verify(spyRobotRenderer, times(1)).paintSelection(graphics2DMock, robotSnapshot);
    }

    @Test
    public void testSetRenderDead() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setRenderDead(true);
        assertFalse(robotRenderer.isRenderDead());
        robotRenderer.setRenderDead(false);
        assertFalse(robotRenderer.isRenderDead());
        robotRenderer.setRenderDead(true);
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testSetShowStatusBars() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowStatusBars(true);
        assertTrue(robotRenderer.isShowStatusBars());
        robotRenderer.setShowStatusBars(false);
        assertFalse(robotRenderer.isShowStatusBars());
        robotRenderer.setShowStatusBars(true);
        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowName() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowName(true);
        assertTrue(robotRenderer.isShowName());
        robotRenderer.setShowName(false);
        assertFalse(robotRenderer.isShowName());
        robotRenderer.setShowName(true);
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testSetFillShield() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setFillShield(true);
        assertTrue(robotRenderer.isFillShield());
        robotRenderer.setFillShield(false);
        assertFalse(robotRenderer.isFillShield());
        robotRenderer.setFillShield(true);
        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testIsRenderDead() {
        RobotRenderer robotRenderer = new RobotRenderer();
        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars() {
        RobotRenderer robotRenderer = new RobotRenderer();
        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName() {
        RobotRenderer robotRenderer = new RobotRenderer();
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testIsFillShield() {
        RobotRenderer robotRenderer = new RobotRenderer();
        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testPaintSelection() {
        Graphics2D graphics2D = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();

        robotRenderer.paintSelection(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
        verify(graphics2D, times(1)).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShield() {
        Graphics2D graphics2D = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();

        when(robotSnapshot.isActiveShield()).thenReturn(true);

        robotRenderer.paintShield(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(new RadialGradientPaint(any(Point2D.Double.class), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white }));
        verify(graphics2D, times(1)).fill(any(Ellipse2D.Double.class));
        verify(graphics2D, times(1)).fill(any(Ellipse2D.Double.class));
        verifyZeroInteractions(graphics2D, robotSnapshot);

        when(robotSnapshot.isActiveShield()).thenReturn(false);

        robotRenderer.paintShield(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(new Color(0.8f, 0.6f, 1.0f));
        verify(graphics2D, times(1)).draw(any(Ellipse2D.Double.class));
        verifyZeroInteractions(graphics2D, robotSnapshot);
    }

    @Test
    public void testPaintName() {
        Graphics2D graphics2D = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();

        robotRenderer.paintName(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(Color.yellow);
        verify(graphics2D, times(1)).drawString(anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testPaintHeat() {
        Graphics2D graphics2D = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();

        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getTemperature()).thenReturn(new Temperature(50.0f));

        robotRenderer.paintHeat(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(new GradientPaint((float)anyDouble(), (float)anyDouble(), RobotRenderer.HEAT_COLOR1, (float)anyDouble(), (float)anyDouble(), RobotRenderer.HEAT_COLOR2));
        verify(graphics2D, times(1)).fill(any(Rectangle2D.Double.class));
        verify(graphics2D, times(1)).draw(any(Rectangle2D.Double.class));

        when(robotSnapshot.isDead()).thenReturn(true);

        robotRenderer.paintHeat(graphics2D, robotSnapshot);

        verifyZeroInteractions(graphics2D, robotSnapshot);
    }

    @Test
    public void testPaintArmor() {
        Graphics2D graphics2D = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();

        robotRenderer.paintArmor(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
        verify(graphics2D, times(1)).fill(any(Rectangle2D.Double.class));
        verify(graphics2D, times(1)).setPaint(new Color(0.0f, 1.0f, 0.0f, 0.6f));
        verify(graphics2D, times(1)).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintTurret() {
        Graphics2D graphics2D = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();

        robotRenderer.paintTurret(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(Color.white);
        verify(graphics2D, times(1)).draw(any(Line2D.Double.class));
    }

    @Test
    public void testPaintBody() {
        Graphics2D graphics2D = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();

        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getHeading()).thenReturn(new RelativeAngle(Math.PI / 2.0));

        robotRenderer.paintBody(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(Color.red);
        verify(graphics2D, times(1)).fill(any(GeneralPath.class));
        verifyZeroInteractions(graphics2D, robotSnapshot);

        when(robotSnapshot.isDead()).thenReturn(true);

        robotRenderer.paintBody(graphics2D, robotSnapshot);

        verify(graphics2D, times(1)).setPaint(new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f));
        verify(graphics2D, times(1)).setPaint(Color.orange.darker().darker());
        verify(graphics2D, times(1)).draw(any(GeneralPath.class));
        verifyZeroInteractions(graphics2D, robotSnapshot);
    }

    @Test
    public void testRenderWithEmptySelectedRobotIds() {
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setRenderDead(false);
        robotRenderer.setShowStatusBars(true);
        robotRenderer.setShowName(true);
        robotRenderer.setFillShield(true);

        Set<Integer> selectedRobotIds = Collections.emptySet();

        RobotRenderer spyRobotRenderer = spy(robotRenderer);
        doCallRealMethod().when(spyRobotRenderer).render(graphics2DMock, robotSnapshot, selectedRobotIds);

        spyRobotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        verifyZeroInteractions(graphics2DMock, robotSnapshot);
    }
}