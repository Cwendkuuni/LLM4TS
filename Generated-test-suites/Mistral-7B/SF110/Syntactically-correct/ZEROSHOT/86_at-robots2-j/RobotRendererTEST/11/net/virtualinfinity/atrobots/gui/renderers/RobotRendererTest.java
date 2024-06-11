package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.Test;

public class RobotRendererTest {

    @Test
    public void testRender() {
        Graphics2D g2d = createGraphics2D();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer renderer = new RobotRenderer();

        renderer.render(g2d, robotSnapshot, new HashSet<>());

        // Test painting body
        Shape expectedBody = createBodyShape(15.0, 15.0, 5.0);
        assertShapeEqual(expectedBody, g2d.getShape());

        // Test painting shield
        Shape expectedShield = createShieldShape(15.0, 15.0);
        if (renderer.isFillShield()) {
            assertShapeEqual(expectedShield, g2d.getClip());
        } else {
            assertShapeEqual(expectedShield.getBounds(), g2d.getTransform().createTransformedShape(expectedShield).getBounds());
        }

        g2d.dispose();

        // Test painting name
        g2d = createGraphics2D();
        robotSnapshot.setName("TestName");
        renderer.render(g2d, robotSnapshot, new HashSet<>());
        assertEquals("TestName", g2d.getFontMetrics().getStringBounds("TestName", g2d).getCenterX(), g2d.getFontMetrics().getStringBounds("TestName", g2d).getCenterY());

        g2d.dispose();

        // Test painting heat
        g2d = createGraphics2D();
        robotSnapshot.setTemperature(new Temperature(10.0));
        renderer.render(g2d, robotSnapshot, new HashSet<>());
        Shape expectedHeat = createHeatShape(15.0, 45.0, 10.0);
        assertShapeEqual(expectedHeat, g2d.getClip());

        g2d.dispose();

        // Test painting armor
        g2d = createGraphics2D();
        robotSnapshot.setArmor(50.0);
        renderer.render(g2d, robotSnapshot, new HashSet<>());
        Shape expectedArmor = createArmorShape(15.0, 20.0, 50.0);
        assertShapeEqual(expectedArmor, g2d.getClip());

        g2d.dispose();

        // Test painting turret
        g2d = createGraphics2D();
        robotSnapshot.setTurretHeading(new Angle(Math.PI / 2.0));
        renderer.render(g2d, robotSnapshot, new HashSet<>());
        Line2D expectedTurret = new Line2D.Double(15.0, 15.0, 15.0 + Math.cos(Math.PI / 2.0) * 5.0, 15.0 + Math.sin(Math.PI / 2.0) * 5.0);
        assertShapeEqual(expectedTurret, g2d.getShape());

        g2d.dispose();

        // Test painting selection
        g2d = createGraphics2D();
        Set<Integer> selectedRobots = new HashSet<>();
        selectedRobots.add(robotSnapshot.getId());
        renderer.render(g2d, robotSnapshot, selectedRobots);
        Ellipse2D expectedSelection = new Ellipse2D.Double(robotSnapshot.getX() - 15.0, robotSnapshot.getY() - 15.0, 30.0, 30.0);
        assertShapeEqual(expectedSelection, g2d.getClip());

        g2d.dispose();
    }

    @Test
    public void testIsRenderDead() {
        RobotRenderer renderer = new RobotRenderer();
        assertFalse(renderer.isRenderDead());

        renderer.setRenderDead(true);
        assertTrue(renderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars() {
        RobotRenderer renderer = new RobotRenderer();
        assertTrue(renderer.isShowStatusBars());

        renderer.setShowStatusBars(false);
        assertFalse(renderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName() {
        RobotRenderer renderer = new RobotRenderer();
        assertTrue(renderer.isShowName());

        renderer.setShowName(false);
        assertFalse(renderer.isShowName());
    }

    @Test
    public void testIsFillShield() {
        RobotRenderer renderer = new RobotRenderer();
        assertTrue(renderer.isFillShield());

        renderer.setFillShield(false);
        assertFalse(renderer.isFillShield());
    }

    private static Graphics2D createGraphics2D() {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().get(0).getDefaultConfiguration();
        return gc.createGraphics2D();
    }

    private static void assertShapeEqual(Shape expected, Shape actual) {
        assertEquals(expected.getBounds(), actual.getBounds());
        assertArrayEquals(expected.getPathIterator(null).getWindingRule(), actual.getPathIterator(null).getWindingRule());
    }

    private static Shape createBodyShape(double width, double height, double size) {
        AbsoluteAngle cc = Angle.fromBygrees(160).counterClockwise();
        AbsoluteAngle c = Angle.fromBygrees(160);

        GeneralPath path = new GeneralPath();
        path.moveTo(width / 2.0 + size * cc.cosine(), height / 2.0 + size * cc.sine());
        path.lineTo(width / 2.0 + size * c.cosine(), height / 2.0 + size * c.sine());
        path.lineTo(width / 2.0, height / 2.0);
        path.lineTo(width / 2.0 + size * c.cosine(), height / 2.0 + size * c.sine());
        path.closePath();

        return path;
    }

    private static Shape createShieldShape(double width) {
        return new Ellipse2D.Double(width / 2.0 - 5.0, height / 2.0, 100.0, 10.0);
    }

    private static Shape createHeatShape(double x, double y, double width) {
        Rectangle2D rect = new Rectangle2D.Double(x - 50.0, y + 35.0, width, 10.0);
        return rect;
    }

    private static Shape createArmorShape(double x, double y, double width) {
        return new Rectangle2D.Double(x - 50.0, y + 20.0, width, 10.0);
    }
}