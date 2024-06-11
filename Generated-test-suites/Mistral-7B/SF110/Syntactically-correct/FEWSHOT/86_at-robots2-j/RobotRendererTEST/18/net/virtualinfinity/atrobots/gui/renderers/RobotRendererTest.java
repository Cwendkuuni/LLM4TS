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
    private RobotRenderer renderer;
    private Graphics2D g2dMock;
    private RobotSnapshot robotSnapshot;
    private Set<Integer> selectedRobotIds;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        renderer = new RobotRenderer();
        g2dMock = mock(Graphics2D.class);
        robotSnapshot = new RobotSnapshot();
        selectedRobotIds = new HashSet<>();
    }

    @Test
    public void testRenderWithDeadRobot() {
        // Given
        when(g2dMock.isDisposed()).thenReturn(false);

        // When
        renderer.render(g2dMock, robotSnapshot, selectedRobotIds);

        // Then
        verify(g2dMock, never()).fill(any(Shape.class));
        verify(g2dMock, never()).setPaint(anyColor());
    }

    @Test
    public void testRenderWithRobot() {
        // Given
        robotSnapshot.setName("Robot1");
        robotSnapshot.setX(10);
        robotSnapshot.setY(20);
        robotSnapshot.setArmor(100);
        robotSnapshot.setTemperature(new Temperature(50));
        robotSnapshot.setDead(false);
        when(g2dMock.isDisposed()).thenReturn(false);

        // When
        renderer.render(g2dMock, robotSnapshot, selectedRobotIds);

        // Then
        verify(g2dMock, times(1)).fill(any(Shape.class));
        verify(g2dMock, times(1)).setPaint(color(Color.RED));
        verify(g2dMock, times(1)).draw(any(Shape.class));
        verify(g2dMock, times(1)).setPaint(color(Color.YELLOW));
        verify(g2dMock, times(1)).drawString(eq("Robot1"), anyFloat(), anyFloat());

        // Test shield painting
        robotSnapshot.setActiveShield(true);
        renderer.render(g2dMock, robotSnapshot, selectedRobotIds);
        verify(g2dMock, times(1)).setPaint(new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white }));
        verify(g2dMock, times(1)).fill(any(Shape.class));
        verify(g2dMock, times(1)).fill(any(Shape.class));

        // Test name painting with overlapping names
        robotSnapshot.setName("Robot2");
        renderer.render(g2dMock, robotSnapshot, selectedRobotIds);
        verify(g2dMock, times(1)).setPaint(color(Color.YELLOW));
        verify(g2dMock, times(1)).drawString(eq("Robot2"), anyFloat(), anyFloat());

        // Test selection painting
        selectedRobotIds.add(robotSnapshot.getId());
        renderer.render(g2dMock, robotSnapshot, selectedRobotIds);
        verify(g2dMock, times(1)).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
        verify(g2dMock, times(1)).fill(any(Shape.class));
    }

    @Test
    public void testIsRenderDead() {
        // Given
        renderer.setRenderDead(false);

        // When
        boolean result = renderer.isRenderDead();

        // Then
        assertTrue(result);
    }

    @Test
    public void testSetRenderDead() {
        // Given
        renderer.setRenderDead(true);

        // When
        boolean result = renderer.isRenderDead();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsShowStatusBars() {
        // Given
        renderer.setShowStatusBars(true);

        // When
        boolean result = renderer.isShowStatusBars();

        // Then
        assertTrue(result);
    }

    @Test
    public void testSetShowStatusBarsFalse() {
        // Given
        renderer.setShowStatusBars(false);

        // When
        boolean result = renderer.isShowStatusBars();

        // Then
        assertFalse(result);
    }

    @Test
    public void testIsShowName() {
        // Given
        renderer.setShowName(true);

        // When
        boolean result = renderer.isShowName();

        // Then
        assertTrue(result);
    }

    @Test
    public void testSetShowNameFalse() {
        // Given
        renderer.setShowName(false);

        // When
        boolean result = renderer.isShowName();

        // Then
        assertFalse(result);
    }

    @Test
    public void testIsFillShield() {
        // Given
        renderer.setFillShield(true);

        // When
        boolean result = renderer.isFillShield();

        // Then
        assertTrue(result);
    }

    @Test
    public void testSetFillShieldFalse() {
        // Given
        renderer.setFillShield(false);

        // When
        boolean result = renderer.isFillShield();

        // Then
        assertFalse(result);
    }

    private Color color(Color color) {
        return color;
    }
}