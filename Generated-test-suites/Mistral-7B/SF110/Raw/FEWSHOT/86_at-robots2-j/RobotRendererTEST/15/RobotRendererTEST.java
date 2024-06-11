package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {

    private RobotRenderer robotRenderer;
    private Graphics2D mockGraphics2D;
    private RobotSnapshot mockRobotSnapshot;
    private Set<Integer> mockSelectedRobotIds;

    @Before
    public void setup() {
        mockGraphics2D = mock(Graphics2D.class);
        mockRobotSnapshot = mock(RobotSnapshot.class);
        mockSelectedRobotIds = new HashSet<>();
        robotRenderer = new RobotRenderer();
    }

    @Test
    public void testRender_DeadRobot_DoNotRender() {
        when(mockRobotSnapshot.isDead()).thenReturn(true);
        robotRenderer.render(mockGraphics2D, mockRobotSnapshot, mockSelectedRobotIds);
        verify(mockGraphics2D, never()).fill(any(Shape.class));
        verify(mockGraphics2D, never()).draw(any(Shape.class));
        verify(mockGraphics2D, never()).setPaint(any(Color.class));
    }

    @Test
    public void testRender_NotDeadRobot_RenderBody() {
        when(mockRobotSnapshot.isDead()).thenReturn(false);
        robotRenderer.render(mockGraphics2D, mockRobotSnapshot, mockSelectedRobotIds);
        verify(mockGraphics2D, times(1)).fill(any(Shape.class));
        verify(mockGraphics2D, times(1)).draw(any(Line2D.class));
        verify(mockGraphics2D, times(2)).setPaint(any(Color.class));
    }

    @Test
    public void testRender_SelectedRobot_PaintSelection() {
        when(mockRobotSnapshot.isDead()).thenReturn(false);
        mockSelectedRobotIds.add(mockRobotSnapshot.getId());
        robotRenderer.render(mockGraphics2D, mockRobotSnapshot, mockSelectedRobotIds);
        verify(mockGraphics2D, times(1)).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
        verify(mockGraphics2D, times(1)).fill(any(Shape.class));
    }

    @Test
    public void testIsRenderDead_DefaultFalse() {
        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testIsRenderDead_SetToTrue() {
        robotRenderer.setRenderDead(true);
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars_DefaultTrue() {
        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowStatusBars_SetToFalse() {
        robotRenderer.setShowStatusBars(false);
        assertFalse(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName_DefaultTrue() {
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testIsShowName_SetToFalse() {
        robotRenderer.setShowName(false);
        assertFalse(robotRenderer.isShowName());
    }

    @Test
    public void testIsFillShield_DefaultTrue() {
        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testIsFillShield_SetToFalse() {
        robotRenderer.setFillShield(false);
        assertFalse(robotRenderer.isFillShield());
    }
}