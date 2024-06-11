package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.Test;

public class RobotRendererTest {

    @Test
    public void testPaintSelection() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);

        RobotRenderer renderer = new RobotRenderer();
        renderer.paintSelection(g2d, robotSnapshot);

        verify(g2d).setPaint(argThat(color -> color.getAlpha() == 0.25f && color.getRed() == 1.0f && color.getGreen() == 1.0f && color.getBlue() == 0.0f));
        verify(g2d).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShieldWhenActive() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);
        robotSnapshot.setActiveShield(true);

        RobotRenderer renderer = new RobotRenderer();
        renderer.paintShield(g2d, robotSnapshot);

        verify(g2d).setPaint(argThat(p -> p instanceof RadialGradientPaint));
        verify(g2d).fill(any(Ellipse2D.Double.class));
        verify(g2d).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShieldWhenInactive() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);
        robotSnapshot.setActiveShield(false);

        RobotRenderer renderer = new RobotRenderer();
        renderer.paintShield(g2d, robotSnapshot);

        verify(g2d).setPaint(argThat(p -> p instanceof Color && p.getRed() == 0.8f && p.getGreen() == 0.6f && p.getBlue() == 1.0f));
        verify(g2d).draw(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintHeat() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);
        robotSnapshot.setTemperature(new Temperature(100));

        RobotRenderer renderer = new RobotRenderer();
        renderer.paintHeat(g2d, robotSnapshot);

        verify(g2d).setPaint(argThat(p -> p instanceof GradientPaint));
        verify(g2d, times(2)).draw(any(Shape.class));
        verify(g2d).fill(any(Shape.class));
    }

    @Test
    public void testPaintArmor() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);
        robotSnapshot.setArmor(50.0);

        RobotRenderer renderer = new RobotRenderer();
        renderer.paintArmor(g2d, robotSnapshot);

        verify(g2d).setPaint(argThat(p -> p.getRed() == 0.0f && p.getGreen() == 0.0f && p.getBlue() == 1.0f && p.getAlpha() == 0.6f));
        verify(g2d).fill(any(Rectangle2D.Double.class));
        verify(g2d).setPaint(argThat(p -> p.getRed() == 0.0f && p.getGreen() == 1.0f && p.getBlue() == 0.0f && p.getAlpha() == 0.6f));
        verify(g2d).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintTurret() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);
        robotSnapshot.setHeading(new Angle(Math.PI / 4));

        RobotRenderer renderer = new RobotRenderer();
        renderer.paintTurret(g2d, robotSnapshot);

        verify(g2d).setPaint(Color.white);
        verify(g2d).draw(any(Line2D.Double.class));
    }

    @Test
    public void testPaintBody() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);
        robotSnapshot.setHeading(new Angle(Math.PI / 4));

        RobotRenderer renderer = new RobotRenderer();
        renderer.paintBody(g2d, robotSnapshot);

        Path2D path = new Path2D.Double();
        path.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
        path.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
        path.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
        path.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
        path.closePath();

        verify(g2d).setPaint(argThat(p -> p instanceof Color && (robotSnapshot.isDead() ? p.getRed() == Color.orange.darker().darker().getRed() : p.getRed() == Color.red.getRed())));
        verify(g2d).fill(path);
    }
}