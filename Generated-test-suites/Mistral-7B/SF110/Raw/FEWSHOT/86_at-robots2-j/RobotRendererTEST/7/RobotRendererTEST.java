package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {

    @Test
    public void testRender() {
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Graphics2D g2d = mock(Graphics2D.class);
        Set<Integer> selectedRobotIds = mock(HashSet.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        verify(robotSnapshot, never()).isDead(); // Test that render method does not call isDead() on robotSnapshot when it is not dead

        robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isDead()).thenReturn(true);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        verify(robotSnapshot).isDead(); // Test that render method calls isDead() on robotSnapshot when it is dead
    }

    @Test
    public void testPaintSelection() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setId(1);
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintSelection(g2d, robotSnapshot);

        Path2D selectionPath = (Path2D) g2d.getPath();
        assertEquals("ellipse", selectionPath.getType());
        Ellipse2D.Double s = (Ellipse2D.Double) selectionPath.getBounds2D();
        assertEquals(10.0, s.getCenterX(), 0.1);
        assertEquals(10.0, s.getCenterY(), 0.1);
        assertEquals(30.0, s.getWidth(), 0.1);
        assertEquals(30.0, s.getHeight(), 0.1);
    }

    @Test
    public void testPaintShield() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setActiveShield(true);
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);
        robotSnapshot.setPositionVector(new Vector2D(10.0, 10.0));

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintShield(g2d, robotSnapshot);

        Path2D shieldPath = (Path2D) g2d.getPath();
        assertEquals("ellipse", shieldPath.getType());
        Ellipse2D.Double s = (Ellipse2D.Double) shieldPath.getBounds2D();
        assertEquals(10.0, s.getCenterX(), 0.1);
        assertEquals(10.0, s.getCenterY(), 0.1);
        assertEquals(15.0, s.getWidth(), 0.1);
        assertEquals(15.0, s.getHeight(), 0.1);

        verify(g2d, times(2)).fill(any());
        verify(g2d).setPaint(isA(RadialGradientPaint.class));
        verify(g2d).setPaint(isA(Color.class));
        verify(g2d).draw(any());
    }

    @Test
    public void testPaintName() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setName("TestRobot");
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintName(g2d, robotSnapshot);

        String name = g2d.getFontMetrics().getStringBounds("TestRobot", g2d).toString();
        assertEquals("TestRobot", name);

        Point2D namePosition = g2d.getFontMetrics().getBounds().getLocation();
        assertEquals(10.0, namePosition.getX(), 0.1);
        assertEquals(10.0, namePosition.getY(), 0.1);
    }

    @Test
    public void testPaintHeat() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);
        robotSnapshot.setTemperature(new Temperature(50.0));

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintHeat(g2d, robotSnapshot);

        Path2D heatPath = (Path2D) g2d.getPath();
        assertEquals("rectangle", heatPath.getType());
        Rectangle2D.Double rect = (Rectangle2D.Double) heatPath.getBounds2D();
        assertEquals(10.0, rect.getMinX(), 0.1);
        assertEquals(10.0, rect.getMinY(), 0.1);
        assertEquals(50.0, rect.getWidth(), 0.1);
        assertEquals(10.0, rect.getHeight(), 0.1);

        GradientPaint gradientPaint = (GradientPaint) g2d.getPaint();
        float[] gradientFractions = gradientPaint.getFractions();
        assertEquals(0.0f, gradientFractions[0], 0.01f);
        assertEquals(0.75f, gradientFractions[1], 0.01f);
        assertEquals(1.0f, gradientFractions[2], 0.01f);

        Color color1 = gradientPaint.getColor(0.0f);
        assertEquals(1.0f, color1.getRed(), 0.01f);
        assertEquals(0.0f, color1.getGreen(), 0.01f);
        assertEquals(0.0f, color1.getBlue(), 0.01f);
        assertEquals(0.1f, color1.getAlpha(), 0.01f);

        Color color2 = gradientPaint.getColor(1.0f);
        assertEquals(1.0f, color2.getRed(), 0.01f);
        assertEquals(1.0f, color2.getGreen(), 0.01f);
        assertEquals(0.0f, color2.getBlue(), 0.01f);
        assertEquals(1.0f, color2.getAlpha(), 0.01f);
    }

    @Test
    public void testPaintArmor() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);
        robotSnapshot.setArmor(100.0);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintArmor(g2d, robotSnapshot);

        Path2D armorPath = (Path2D) g2d.getPath();
        assertEquals("rectangle", armorPath.getType());
        Rectangle2D.Double rect = (Rectangle2D.Double) armorPath.getBounds2D();
        assertEquals(10.0, rect.getMinX(), 0.1);
        assertEquals(10.0, rect.getMinY(), 0.1);
        assertEquals(100.0, rect.getWidth(), 0.1);
        assertEquals(10.0, rect.getHeight(), 0.1);

        Color color1 = (Color) g2d.getColor();
        assertEquals(0.0f, color1.getRed(), 0.01f);
        assertEquals(0.0f, color1.getGreen(), 0.01f);
        assertEquals(1.0f, color1.getBlue(), 0.01f);
        assertEquals(0.6f, color1.getAlpha(), 0.01f);

        Color color2 = Color.BLUE;
        verify(g2d).setPaint(isA(Color.class));
        verify(g2d).fill(any());
        verify(g2d).setPaint(color2);
        verify(g2d).draw(any());
    }

    @Test
    public void testPaintTurret() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);
        robotSnapshot.setHeading(new Angle(Math.PI / 2));

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintTurret(g2d, robotSnapshot);

        Line2D turretLine = (Line2D) g2d.getPath();
        assertEquals(2, turretLine.getPoints().length);
        assertEquals(10.0, turretLine.getPoints()[0].getX(), 0.1);
        assertEquals(10.0, turretLine.getPoints()[0].getY(), 0.1);
        assertEquals(10.0 + Math.cos(Math.PI / 2) * 5.0, turretLine.getPoints()[1].getX(), 0.1);
        assertEquals(10.0 + Math.sin(Math.PI / 2) * 5.0, turretLine.getPoints()[1].getY(), 0.1);
    }

    @Test
    public void testPaintBody() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);
        robotSnapshot.setHeading(new Angle(Math.PI / 2));

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintBody(g2d, robotSnapshot);

        GeneralPath bodyPath = (GeneralPath) g2d.getPath();
        assertEquals(4, bodyPath.getPathIterator(null).getWindingRule());
        MoveToElement moveToElement = (MoveToElement) bodyPath.getPathIterator(null);
        assertEquals(10.0 + Math.cos(Math.PI / 2) * 15.0, moveToElement.getX(), 0.1);
        assertEquals(10.0 + Math.sin(Math.PI / 2) * 15.0, moveToElement.getY(), 0.1);

        LineElement lineElement1 = (LineElement) bodyPath.getPathIterator(null).next();
        assertEquals(10.0, lineElement1.getX(), 0.1);
        assertEquals(10.0, lineElement1.getY(), 0.1);
        assertEquals(10.0 + Math.cos(Math.PI / 4) * 9.0, lineElement1.getX2(), 0.1);
        assertEquals(10.0 + Math.sin(Math.PI / 4) * 9.0, lineElement1.getY2(), 0.1);

        LineElement lineElement2 = (LineElement) bodyPath.getPathIterator(null).next();
        assertEquals(10.0, lineElement2.getX(), 0.1);
        assertEquals(10.0, lineElement2.getY(), 0.1);
        assertEquals(10.0, lineElement2.getX2(), 0.1);
        assertEquals(10.0, lineElement2.getY2(), 0.1);

        CubicCurveElement cubicCurveElement = (CubicCurveElement) bodyPath.getPathIterator(null).next();
        assertEquals(10.0 + Math.cos(Math.PI / 4) * 9.0, cubicCurveElement.getX1(), 0.1);
        assertEquals(10.0 + Math.sin(Math.PI / 4) * 9.0, cubicCurveElement.getY1(), 0.1);
        assertEquals(10.0, cubicCurveElement.getX2(), 0.1);
        assertEquals(10.0, cubicCurveElement.getY2(), 0.1);
        assertEquals(10.0 + Math.cos(Math.PI / 2) * 9.0, cubicCurveElement.getX3(), 0.1);
        assertEquals(10.0 + Math.sin(Math.PI / 2) * 9.0, cubicCurveElement.getY3(), 0.1);
        assertEquals(10.0 + Math.cos(Math.PI / 2) * 15.0, cubicCurveElement.getX4(), 0.1);
        assertEquals(10.0 + Math.sin(Math.PI / 2) * 15.0, cubicCurveElement.getY4(), 0.1);
    }
}