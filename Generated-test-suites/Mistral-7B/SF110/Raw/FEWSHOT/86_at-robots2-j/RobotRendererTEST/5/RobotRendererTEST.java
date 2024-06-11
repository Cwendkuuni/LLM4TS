package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.awt.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {

    @Test
    public void testPaintSelection() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot(1, 50.0, 50.0, 30.0, 50.0, 15.0, new PositionVector(1.0, 1.0), new Heading(1.0), 100.0, 100.0, "robot1", true, true, false, false, false, false, false, false);
        RobotRenderer robotRenderer = new RobotRenderer();

        robotRenderer.paintSelection(g2d, robotSnapshot);

        Ellipse2D.Double expectedSelection = new Ellipse2D.Double();
        expectedSelection.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);

        verify(g2d).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
        verify(g2d).fill(expectedSelection);
    }

    @Test
    public void testPaintShield() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot(1, 50.0, 50.0, 30.0, 50.0, 15.0, new PositionVector(1.0, 1.0), new Heading(1.0), 100.0, 100.0, "robot1", true, true, true, false, false, false, false, false);
        RobotRenderer robotRenderer = new RobotRenderer();

        robotRenderer.paintShield(g2d, robotSnapshot);

        Ellipse2D.Double expectedShield = new Ellipse2D.Double();
        expectedShield.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 15.0, robotSnapshot.getY() + 15.0);

        if (robotRenderer.isFillShield()) {
            verify(g2d).setPaint(new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white }));
            verify(g2d).fill(expectedShield);
            verify(g2d).fill(expectedShield);
        } else {
            verify(g2d).setPaint(new Color(0.8f, 0.6f, 1.0f));
            verify(g2d).draw(expectedShield);
        }
    }

    @Test
    public void testPaintName() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot(1, 50.0, 50.0, 30.0, 50.0, 15.0, new PositionVector(1.0, 1.0), new Heading(1.0), 100.0, 100.0, "robot1", true, true, false, false, false, false, false, false);
        RobotRenderer robotRenderer = new RobotRenderer();

        robotRenderer.paintName(g2d, robotSnapshot);

        String robotName = "robot1";
        Point2D expectedNamePosition = new Point2D.Double();
        expectedNamePosition.setLocation(robotSnapshot.getX() - g2d.getFontMetrics().getStringBounds(robotName, g2d).getWidth() / 2.0, robotSnapshot.getY() - 10.0);

        verify(g2d).setPaint(Color.yellow);
        verify(g2d).drawString(robotName, (float)expectedNamePosition.getX(), (float)expectedNamePosition.getY());
    }

    @Test
    public void testPaintHeat() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot(1, 50.0, 50.0, 30.0, 50.0, 15.0, new PositionVector(1.0, 1.0), new Heading(1.0), 100.0, 100.0, "robot1", true, true, false, false, false, false, false, false, 10.0);
        RobotRenderer robotRenderer = new RobotRenderer();

        robotRenderer.paintHeat(g2d, robotSnapshot);

        Rectangle2D.Double expectedHeatRect = new Rectangle2D.Double();
        expectedHeatRect.setFrame(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);
        GradientPaint expectedGradient = new GradientPaint((float)expectedHeatRect.getMinX(), (float)expectedHeatRect.getMinY(), RobotRenderer.HEAT_COLOR1, (float)expectedHeatRect.getMaxX(), (float)expectedHeatRect.getMinY(), RobotRenderer.HEAT_COLOR2);

        if (!robotSnapshot.isDead()) {
            verify(g2d).setPaint(expectedGradient);
            verify(g2d).fill(new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, robotSnapshot.getTemperature().getLogScale() * 0.2, 10.0));
        }

        verify(g2d).draw(expectedHeatRect);
    }

    @Test
    public void testPaintArmor() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot(1, 50.0, 50.0, 30.0, 50.0, 15.0, new PositionVector(1.0, 1.0), new Heading(1.0), 100.0, 100.0, "robot1", true, true, false, false, false, false, false, false, 100.0);
        RobotRenderer robotRenderer = new RobotRenderer();

        robotRenderer.paintArmor(g2d, robotSnapshot);

        Rectangle2D.Double expectedArmorRect = new Rectangle2D.Double();
        expectedArmorRect.setFrame(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, robotSnapshot.getArmor(), 10.0);

        verify(g2d).setPaint(new Color(0.0f, 0.0f, 1.0f, 0.6f));
        verify(g2d).fill(expectedArmorRect);
        verify(g2d).setPaint(new Color(0.0f, 1.0f, 0.0f, 0.6f));
        verify(g2d).draw(new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, 100.0, 10.0));
    }

    @Test
    public void testPaintTurret() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot(1, 50.0, 50.0, 30.0, 50.0, 15.0, new PositionVector(1.0, 1.0), new Heading(1.0), 100.0, 100.0, "robot1", true, true, false, false, false, false, false, false, 10.0, 5.0);
        RobotRenderer robotRenderer = new RobotRenderer();

        robotRenderer.paintTurret(g2d, robotSnapshot);

        Line2D.Double expectedTurretLine = new Line2D.Double();
        expectedTurretLine.setLine(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0);

        verify(g2d).setPaint(Color.white);
        verify(g2d).draw(expectedTurretLine);
    }

    @Test
    public void testPaintBody() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot(1, 50.0, 50.0, 30.0, 50.0, 15.0, new PositionVector(1.0, 1.0), new Heading(1.0), 100.0, 100.0, "robot1", true, true, false, false, false, false, false, false, 10.0, 5.0, null);
        RobotRenderer robotRenderer = new RobotRenderer();

        if (robotSnapshot.isDead()) {
            verify(g2d).setPaint(new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f));
            verify(g2d).setPaint(Color.orange.darker().darker());
        } else {
            verify(g2d).setPaint(Color.red);
        }

        verify(g2d).setStroke(g2d.getStroke());

        GeneralPath expectedBodyPath = new GeneralPath();
        expectedBodyPath.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
        AbsoluteAngle cc = robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160));
        AbsoluteAngle c = robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160));
        expectedBodyPath.lineTo(robotSnapshot.getX() + cc.cosine() * 9.0, robotSnapshot.getY() + cc.sine() * 9.0);
        expectedBodyPath.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
        expectedBodyPath.lineTo(robotSnapshot.getX() + c.cosine() * 9.0, robotSnapshot.getY() + c.sine() * 9.0);
        expectedBodyPath.closePath();

        if (robotSnapshot.isDead()) {
            verify(g2d).draw(expectedBodyPath);
        } else {
            verify(g2d).fill(expectedBodyPath);
        }
    }

    @Test
    public void testRenderNullSnapshot() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotRenderer robotRenderer = new RobotRenderer();
        RobotSnapshot robotSnapshot = null;

        robotRenderer.render(g2d, robotSnapshot, new HashSet<>());

        verify(g2d, never()).render(any(RobotSnapshot.class));
    }

    @Test
    public void testRenderDeadRobotSnapshot() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotRenderer robotRenderer = new RobotRenderer();
        RobotSnapshot robotSnapshot = new RobotSnapshot(1, 50.0, 50.0, 30.0, 50.0, 15.0, new PositionVector(1.0, 1.0), new Heading(1.0), 100.0, 100.0, "robot1", true, true, false, false, false, false, false, false, 10.0, 5.0, null, true);

        robotRenderer.render(g2d, robotSnapshot, new HashSet<>());

        verify(g2d, never()).paintSelection(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(g2d, never()).paintBody(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(g2d, never()).paintShield(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(g2d, never()).paintTurret(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(g2d, never()).paintArmor(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(g2d, never()).paintHeat(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(g2d, never()).paintName(any(Graphics2D.class), any(RobotSnapshot.class));
    }
}