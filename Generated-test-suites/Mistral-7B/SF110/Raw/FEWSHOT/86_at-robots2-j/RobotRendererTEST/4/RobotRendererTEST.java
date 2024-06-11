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
        robotSnapshot = new RobotSnapshot(1, new Position2D(10.0, 10.0), new Velocity2D(1.0, 1.0), new Angle(0), true, 50.0, 100.0, "Test Robot", new Temperature(50.0f), new Armor(100.0), new Angle(180.0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0), new Angle(0));
        selectedRobotIds = new HashSet<>();
    }

    @Test
    public void testRender_DeadRobot_DoesNotRender() {
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);
        verify(graphics2DMock, times(0)).fill(any(Shape.class));
        verify(graphics2DMock, times(0)).draw(any(Shape.class));
    }

    @Test
    public void testRender_Body() {
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        final GeneralPath path = new GeneralPath();
        path.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
        final AbsoluteAngle cc = robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160));
        final AbsoluteAngle c = robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160));
        path.lineTo(robotSnapshot.getX() + cc.cosine() * 9.0, robotSnapshot.getY() + cc.sine() * 9.0);
        path.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
        path.lineTo(robotSnapshot.getX() + c.cosine() * 9.0, robotSnapshot.getY() + c.sine() * 9.0);
        path.closePath();

        verify(graphics2DMock).setPaint(any(Paint.class));
        verify(graphics2DMock).fill(path);
        verify(graphics2DMock).setStroke(eq(robotRenderer.getStroke()));
    }

    @Test
    public void testRender_Shield() {
        robotRenderer.setFillShield(true);
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        final Ellipse2D.Double s = new Ellipse2D.Double();
        s.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 15.0, robotSnapshot.getY() + 15.0);

        verify(graphics2DMock).setPaint(new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white }));
        verify(graphics2DMock).fill(s);
        verify(graphics2DMock).fill(s);
    }

    @Test
    public void testRender_Shield_NotFilled() {
        robotRenderer.setFillShield(false);
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        final Ellipse2D.Double s = new Ellipse2D.Double();
        s.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 15.0, robotSnapshot.getY() + 15.0);

        verify(graphics2DMock).setPaint(new Color(0.8f, 0.6f, 1.0f));
        verify(graphics2DMock).draw(s);
    }

    @Test
    public void testRender_Name() {
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        verify(graphics2DMock).setPaint(Color.yellow);
        verify(graphics2DMock).drawString(robotSnapshot.getName(), (float)(robotSnapshot.getX() - graphics2DMock.getFontMetrics().getStringBounds(robotSnapshot.getName(), graphics2DMock).getWidth() / 2.0), (float)(robotSnapshot.getY() - 10.0));
    }

    @Test
    public void testRender_Selection() {
        selectedRobotIds.add(robotSnapshot.getId());
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        final Ellipse2D.Double s = new Ellipse2D.Double();
        s.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);

        verify(graphics2DMock).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
        verify(graphics2DMock).fill(s);
    }

    @Test
    public void testRender_Heat() {
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        final Rectangle2D.Double rect = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);

        verify(graphics2DMock).setPaint(new GradientPaint((float)rect.getMinX(), (float)rect.getMinY(), RobotRenderer.HEAT_COLOR1, (float)rect.getMaxX(), (float)rect.getMinY(), RobotRenderer.HEAT_COLOR2));
        if (robotSnapshot.isDead()) {
            verify(graphics2DMock, times(0)).fill(any(Shape.class));
        } else {
            final Rectangle2D.Double heatRect = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, robotSnapshot.getTemperature().getLogScale() * 0.2, 10.0);
            verify(graphics2DMock).fill(heatRect);
        }
        verify(graphics2DMock).draw(rect);
    }

    @Test
    public void testRender_Armor() {
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        final Rectangle2D.Double rect = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, robotSnapshot.getArmor(), 10.0);

        verify(graphics2DMock).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
        verify(graphics2DMock).fill(rect);

        final Rectangle2D.Double fillRect = new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, 100.0, 10.0);
        verify(graphics2DMock).setPaint(new Color(0.0f, 1.0f, 0.0f, 0.6f));
        verify(graphics2DMock).draw(fillRect);
    }

    @Test
    public void testRender_Turret() {
        robotRenderer.render(graphics2DMock, robotSnapshot, selectedRobotIds);

        verify(graphics2DMock).setPaint(Color.white);
        verify(graphics2DMock).draw(new Line2D.Double(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0));
    }
}