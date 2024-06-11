import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.measures.Vector2D;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import org.junit.Test;

public class RobotRendererTest {

    @Test
    public void testRender() {
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        Graphics2D g2d = mock(Graphics2D.class);
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());
        verify(g2d, times(1)).render(any(RobotSnapshot.class), anySet(), anyBoolean());
    }

    @Test
    public void testIsRenderDead() {
        RobotRenderer robotRenderer = new RobotRenderer();
        assertTrue(robotRenderer.isRenderDead());
        robotRenderer.setRenderDead(false);
        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testPaintSelection() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintSelection(g2d, robotSnapshot);
        verify(g2d, times(1)).setPaint(any(Color.class));
        verify(g2d, times(1)).fill(any(Shape.class));
    }

    @Test
    public void testPaintShield() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintShield(g2d, robotSnapshot);
        verify(g2d, times(1)).setPaint(any(Color.class));
        verify(g2d, times(1)).fill(any(Shape.class));
        verify(g2d, times(1)).draw(any(Shape.class));
    }

    @Test
    public void testPaintName() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintName(g2d, robotSnapshot);
        verify(g2d, times(1)).setPaint(any(Color.class));
        verify(g2d, times(1)).drawString(eq(robotSnapshot.getName()), anyFloat(), anyFloat());
    }

    @Test
    public void testPaintHeat() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintHeat(g2d, robotSnapshot);
        verify(g2d, times(1)).setPaint(any(GradientPaint.class));
        verify(g2d, times(1)).fill(any(Shape.class));
        verify(g2d, times(1)).draw(any(Shape.class));
    }

    @Test
    public void testPaintArmor() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintArmor(g2d, robotSnapshot);
        verify(g2d, times(1)).setPaint(any(Color.class));
        verify(g2d, times(1)).fill(any(Shape.class));
        verify(g2d, times(1)).setPaint(any(Color.class));
        verify(g2d, times(1)).draw(any(Shape.class));
    }

    @Test
    public void testPaintTurret() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintTurret(g2d, robotSnapshot);
        verify(g2d, times(1)).setPaint(any(Color.class));
        verify(g2d, times(1)).draw(any(Shape.class));
    }

    @Test
    public void testPaintBody() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintBody(g2d, robotSnapshot);
        verify(g2d, times(1)).setPaint(any(Color.class));
        verify(g2d, times(1)).fill(any(Shape.class));
        verify(g2d, times(1)).setStroke(any(Stroke.class));
    }

}