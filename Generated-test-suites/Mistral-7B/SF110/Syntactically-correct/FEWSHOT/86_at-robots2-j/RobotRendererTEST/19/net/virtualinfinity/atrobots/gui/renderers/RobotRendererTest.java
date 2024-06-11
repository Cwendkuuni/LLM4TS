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

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        robotSnapshot = mock(RobotSnapshot.class);
        graphics2D = mock(Graphics2D.class);
        robotRenderer = new RobotRenderer();
    }

    @Test
    public void testRender_deadRobot_doesNotRender() {
        when(robotSnapshot.isDead()).thenReturn(true);
        robotRenderer.render(graphics2D, robotSnapshot, new HashSet<>());
        verify(graphics2D, never()).fill(any(Shape.class));
        verify(graphics2D, never()).draw(any(Shape.class));
    }

    @Test
    public void testRender_activeRobot_rendersBody() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getHeading()).thenReturn(new AngleByGrees(0));
        when(robotSnapshot.getX()).thenReturn(10.0);
        when(robotSnapshot.getY()).thenReturn(10.0);

        robotRenderer.render(graphics2D, robotSnapshot, new HashSet<>());

        Path2D path = (Path2D) robotRenderer.path;
        assertEquals(Path2D.Float.WIND_EVEN_ODD, path.getWindingRule());
        assertEquals(12, path.getElements().length);

        Point2D startPoint = new Point2D.Double();
        path.getCurrentPoint(startPoint);
        assertEquals(10.0 + Math.cos(Math.toRadians(0)) * 15.0, startPoint.getX(), 0.01);
        assertEquals(10.0 + Math.sin(Math.toRadians(0)) * 15.0, startPoint.getY(), 0.01);

        Path2D expectedPath = new GeneralPath();
        expectedPath.moveTo(10.0 + Math.cos(Math.toRadians(150)) * 15.0, 10.0 + Math.sin(Math.toRadians(150)) * 15.0);
        expectedPath.lineTo(10.0 + Math.cos(Math.toRadians(-150)) * 9.0, 10.0 + Math.sin(Math.toRadians(-150)) * 9.0);
        expectedPath.lineTo(10.0, 10.0 + 9.0);
        expectedPath.lineTo(10.0 + Math.cos(Math.toRadians(150)) * 9.0, 10.0 + Math.sin(Math.toRadians(150)) * 9.0);
        expectedPath.closePath();

        verify(graphics2D).fill(expectedPath);
    }

    @Test
    public void testRender_activeRobot_rendersName() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getX()).thenReturn(10.0);
        when(robotSnapshot.getY()).thenReturn(10.0);
        when(robotSnapshot.getName()).thenReturn("TestBot");

        robotRenderer.render(graphics2D, robotSnapshot, new HashSet<>());

        verify(graphics2D).setPaint(eq(Color.yellow));
        verify(graphics2D).drawString("TestBot", anyDouble(), anyDouble());
    }

    @Test
    public void testRender_activeRobot_rendersSelection() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getId()).thenReturn(1);
        when(robotSnapshot.getX()).thenReturn(10.0);
        when(robotSnapshot.getY()).thenReturn(10.0);

        robotRenderer.render(graphics2D, robotSnapshot, new HashSet<>(Collections.singleton(1)));

        Ellipse2D shape = (Ellipse2D) robotRenderer.selection;
        assertEquals(10.0, shape.getCenterX(), 0.01);
        assertEquals(10.0, shape.getCenterY(), 0.01);
        assertEquals(30.0, shape.getWidth(), 0.01);
        assertEquals(30.0, shape.getHeight(), 0.01);

        verify(graphics2D).setPaint(eq(new Color(1.0f, 1.0f, 0.0f, 0.25f)));
        verify(graphics2D).fill(shape);
    }

    @Test
    public void testRender_activeRobot_rendersShield() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getX()).thenReturn(10.0);
        when(robotSnapshot.getY()).thenReturn(10.0);
        when(robotSnapshot.getArmor()).thenReturn(100.0);
        when(robotSnapshot.isActiveShield()).thenReturn(true);

        robotRenderer.render(graphics2D, robotSnapshot, new HashSet<>());

        Ellipse2D shield = (Ellipse2D) robotRenderer.shield;
        assertEquals(10.0, shield.getCenterX(), 0.01);
        assertEquals(10.0, shield.getCenterY(), 0.01);
        assertEquals(15.0, shield.getWidth(), 0.01);
        assertEquals(15.0, shield.getHeight(), 0.01);

        verify(graphics2D).setPaint(eq(new RadialGradientPaint(anyDouble(), 15.0, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white })));
        verify(graphics2D).fill(shield);
        verify(graphics2D).fill(shield);
    }

    @Test
    public void testRender_activeRobot_rendersHeat() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getX()).thenReturn(10.0);
        when(robotSnapshot.getY()).thenReturn(10.0);
        when(robotSnapshot.getTemperature()).thenReturn(new Temperature(100));

        robotRenderer.render(graphics2D, robotSnapshot, new HashSet<>());

        Rectangle2D heatRect = (Rectangle2D) robotRenderer.heat;
        assertEquals(10.0 - 50.0, heatRect.getX(), 0.01);
        assertEquals(10.0 + 35.0, heatRect.getY(), 0.01);
        assertEquals(100.0 * 0.2, heatRect.getWidth(), 0.01);
        assertEquals(10.0, heatRect.getHeight(), 0.01);

        verify(graphics2D).setPaint(eq(new GradientPaint(anyDouble(), anyDouble(), RobotRenderer.HEAT_COLOR1, anyDouble(), anyDouble(), RobotRenderer.HEAT_COLOR2)));
        verify(graphics2D).fill(any(Rectangle2D.class));
        verify(graphics2D).draw(any(Rectangle2D.class));
    }

    @Test
    public void testRender_activeRobot_rendersArmor() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getX()).thenReturn(10.0);
        when(robotSnapshot.getY()).thenReturn(10.0);
        when(robotSnapshot.getArmor()).thenReturn(100.0);

        robotRenderer.render(graphics2D, robotSnapshot, new HashSet<>());

        Shape armor = robotRenderer.armor;
        assertEquals(10.0 - 50.0, armor.getBounds2D().getMinX(), 0.01);
        assertEquals(10.0 + 20.0, armor.getBounds2D().getMinY(), 0.01);
        assertEquals(100.0, armor.getBounds2D().getWidth(), 0.01);
        assertEquals(10.0, armor.getBounds2D().getHeight(), 0.01);

        verify(graphics2D).setPaint(eq(new Color(0.0f, 0.0f, 1.0f, 0.6f)));
        verify(graphics2D).fill(any(Rectangle2D.class));
        verify(graphics2D).setPaint(eq(new Color(0.0f, 1.0f, 0.0f, 0.6f)));
        verify(graphics2D).draw(any(Rectangle2D.class));
    }

    @Test
    public void testRender_activeRobot_rendersTurret() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getX()).thenReturn(10.0);
        when(robotSnapshot.getY()).thenReturn(10.0);
        when(robotSnapshot.getHeading()).thenReturn(new AngleByGrees(90));

        robotRenderer.render(graphics2D, robotSnapshot, new HashSet<>());

        Line2D line = (Line2D) robotRenderer.turret;
        assertEquals(10.0, line.getX1(), 0.01);
        assertEquals(10.0, line.getY1(), 0.01);
        assertEquals(10.0 + Math.cos(Math.toRadians(90)) * 5.0, line.getX2(), 0.01);
        assertEquals(10.0 + Math.sin(Math.toRadians(90)) * 5.0, line.getY2(), 0.01);

        verify(graphics2D).setPaint(eq(Color.white));
        verify(graphics2D).draw(line);
    }

    @Test
    public void testRender_activeRobot_rendersBodyWithDeadColor() {
        when(robotSnapshot.isDead()).thenReturn(true);
        when(robotSnapshot.getX()).thenReturn(10.0);
        when(robotSnapshot.getY()).thenReturn(10.0);

        robotRenderer.render(graphics2D, robotSnapshot, new HashSet<>());

        Path2D path = (Path2D) robotRenderer.path;
        assertEquals(Path2D.Float.WIND_EVEN_ODD, path.getWindingRule());
        assertEquals(12, path.getElements().length);

        Point2D startPoint = new Point2D.Double();
        path.getCurrentPoint(startPoint);
        assertEquals(10.0 + Math.cos(Math.toRadians(0)) * 15.0, startPoint.getX(), 0.01);
        assertEquals(10.0 + Math.sin(Math.toRadians(0)) * 15.0, startPoint.getY(), 0.01);

        Path2D expectedPath = new GeneralPath();
        expectedPath.moveTo(10.0 + Math.cos(Math.toRadians(150)) * 15.0, 10.0 + Math.sin(Math.toRadians(150)) * 15.0);
        expectedPath.lineTo(10.0 + Math.cos(Math.toRadians(-150)) * 9.0, 10.0 + Math.sin(Math.toRadians(-150)) * 9.0);
        expectedPath.lineTo(10.0, 10.0 + 9.0);
        expectedPath.lineTo(10.0 + Math.cos(Math.toRadians(150)) * 9.0, 10.0 + Math.sin(Math.toRadians(150)) * 9.0);
        expectedPath.closePath();

        verify(graphics2D).setPaint(eq(Color.orange.darker().darker()));
        verify(graphics2D).setStroke(any(BasicStroke.class));
        verify(graphics2D).fill(expectedPath);
    }
}