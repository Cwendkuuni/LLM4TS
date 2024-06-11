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
    public void testRenderWithNullGraphics2D() {
        robotRenderer.render(null, robotSnapshot, selectedRobotIds);
        verify(robotSnapshot, never()).isDead();
    }

    @Test
    public void testRenderWithDeadRobotSnapshot() {
        when(robotSnapshot.isDead()).thenReturn(true);
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);
        verify(robotSnapshot).isDead();
        verify(graphics2DMock, never()).fill(any(Shape.class));
        verify(graphics2DMock, never()).draw(any(Shape.class));
    }

    @Test
    public void testRenderWithAliveRobotSnapshot() {
        when(robotSnapshot.isDead()).thenReturn(false);

        // Test painting body
        when(robotSnapshot.getX()).thenReturn(25.0);
        when(robotSnapshot.getY()).thenReturn(25.0);
        when(robotSnapshot.getHeading()).thenReturn(new RelativeAngle(0.0f, 0.0f));
        when(robotSnapshot.getTurretHeading()).thenReturn(new RelativeAngle(0.0f, 0.0f));
        when(robotSnapshot.getArmor()).thenReturn(100.0);
        when(robotSnapshot.getTemperature()).thenReturn(new Temperature(50.0));

        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        verify(robotSnapshot).isDead();
        verify(graphics2DMock).setPaint(Color.orange.darker().darker());
        verify(graphics2DMock).setStroke(any(Stroke.class));
        verify(graphics2DMock).fill(any(Shape.class));
        verify(graphics2DMock).setPaint(Color.red);
        verify(graphics2DMock).setStroke(eq(stroke));
        verify(graphics2DMock).fill(any(GeneralPath.class));
    }

    @Test
    public void testRenderWithSelectedRobotSnapshot() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getId()).thenReturn(1);
        selectedRobotIds.add(1);

        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        verify(graphics2DMock).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
        verify(graphics2DMock).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testIsRenderDead() {
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testSetRenderDead() {
        robotRenderer.setRenderDead(false);
        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars() {
        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowStatusBars() {
        robotRenderer.setShowStatusBars(false);
        assertFalse(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName() {
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testSetShowName() {
        robotRenderer.setShowName(false);
        assertFalse(robotRenderer.isShowName());
    }

    @Test
    public void testIsFillShield() {
        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testSetFillShield() {
        robotRenderer.setFillShield(false);
        assertFalse(robotRenderer.isFillShield());
    }
}