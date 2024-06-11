package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {

    private RobotSnapshot robotSnapshot;
    private Graphics2D graphics2D;
    private SnapshotRenderer<RobotSnapshot> robotRenderer;

    @Before
    public void setup() {
        robotSnapshot = Mockito.spy(new RobotSnapshot(1, 1.0, new Point2D.Double(10.0, 10.0), new Vector2D(Math.PI / 2.0), 50.0, 100.0, "Test robot", new Point2D.Double(50.0, 35.0), 0.75f, 0.6f, true, new AbsoluteAngle(Math.PI / 4.0), new AbsoluteAngle(Math.PI / 4.0), 15.0f, 30.0f));
        graphics2D = mock(Graphics2D.class);
        robotRenderer = new RobotRenderer();
    }

    @Test
    public void testRenderWithRenderDeadFalseAndAliveRobot() {
        when(graphics2D.getFontMetrics()).thenReturn(mock(FontMetrics.class));

        robotRenderer.render(graphics2D, robotSnapshot, Collections.emptySet());

        verify(graphics2D).setPaint(argThat((Color c) -> c.equals(Color.red)));
        verify(graphics2D).fill(argThat(path -> path instanceof GeneralPath && path.getBounds2D().getCenterX() == 10.0 && path.getBounds2D().getCenterY() == 10.0));
        verify(graphics2D).setPaint(Color.yellow);
        verify(graphics2D).drawString(eq("Test robot"), argThat(d -> d.getX() == 5.0 && d.getY() == -5.5), mock(FontRenderContext.class));
        verify(graphics2D).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
        verify(graphics2D).fill(argThat(path -> path instanceof Rectangle2D.Double && path.getBounds2D().getMinX() == 5.0 && path.getBounds2D().getMinY() == 20.0 && path.getBounds2D().getWidth() == 100.0 && path.getBounds2D().getHeight() == 10.0));
        verify(graphics2D).setPaint(new Color(0.0f, 1.0f, 0.0f, 0.6f));
        verify(graphics2D).draw(argThat(path -> path instanceof Rectangle2D.Double && path.getBounds2D().getMinX() == 5.0 && path.getBounds2D().getMinY() == 20.0 && path.getBounds2D().getWidth() == 100.0 && path.getBounds2D().getHeight() == 10.0));
        verify(graphics2D).setPaint(Color.white);
        verify(graphics2D).draw(argThat(line -> line instanceof Line2D.Double && line.getX1() == 10.0 && line.getY1() == 10.0 && line.getX2() == 10.0 + Math.cos(Math.PI / 2.0) * 5.0 && line.getY2() == 10.0 + Math.sin(Math.PI / 2.0) * 5.0));
    }

    @Test
    public void testRenderWithRenderDeadTrueAndDeadRobot() {
        robotSnapshot.setDead(true);

        when(graphics2D.getFontMetrics()).thenReturn(mock(FontMetrics.class));

        robotRenderer.render(graphics2D, robotSnapshot, Collections.emptySet());

        verify(graphics2D).setPaint(argThat((Color c) -> c.equals(Color.orange.darker().darker())));
        verify(graphics2D).setStroke(argThat(stroke -> stroke instanceof BasicStroke && stroke.getLineWidth() == 1.0f));
        verify(graphics2D).draw(argThat(path -> path instanceof GeneralPath && path.getBounds2D().getCenterX() == 10.0 && path.getBounds2D().getCenterY() == 10.0));
    }

    @Test
    public void testIsRenderDead() {
        assertEquals(true, robotRenderer.isRenderDead());
        robotRenderer.setRenderDead(false);
        assertEquals(false, robotRenderer.isRenderDead());
    }

    @Test
    public void testSetRenderDead() {
        robotRenderer.setRenderDead(true);
        assertEquals(true, robotRenderer.isRenderDead());
        robotRenderer.setRenderDead(false);
        assertEquals(false, robotRenderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars() {
        assertEquals(true, robotRenderer.isShowStatusBars());
        robotRenderer.setShowStatusBars(false);
        assertEquals(false, robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowStatusBars() {
        robotRenderer.setShowStatusBars(true);
        assertEquals(true, robotRenderer.isShowStatusBars());
        robotRenderer.setShowStatusBars(false);
        assertEquals(false, robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName() {
        assertEquals(true, robotRenderer.isShowName());
        robotRenderer.setShowName(false);
        assertEquals(false, robotRenderer.isShowName());
    }

    @Test
    public void testSetShowName() {
        robotRenderer.setShowName(true);
        assertEquals(true, robotRenderer.isShowName());
        robotRenderer.setShowName(false);
        assertEquals(false, robotRenderer.isShowName());
    }

    @Test
    public void testIsFillShield() {
        assertEquals(true, robotRenderer.isFillShield());
        robotRenderer.setFillShield(false);
        assertEquals(false, robotRenderer.isFillShield());
    }

    @Test
    public void testSetFillShield() {
        robotRenderer.setFillShield(true);
        assertEquals(true, robotRenderer.isFillShield());
        robotRenderer.setFillShield(false);
        assertEquals(false, robotRenderer.isFillShield());
    }
}