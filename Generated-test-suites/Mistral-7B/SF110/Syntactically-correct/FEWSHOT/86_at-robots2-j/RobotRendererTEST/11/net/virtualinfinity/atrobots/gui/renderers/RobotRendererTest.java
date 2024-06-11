package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {

    @Test
    public void testRender() {
        Graphics2D g2d = createMockGraphics2D();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer renderer = new RobotRenderer();

        renderer.render(g2d, robotSnapshot, new HashSet<>());

        // Test painting body
        Path2D bodyPath = (Path2D) g2d.getClip();
        assertEquals(3, bodyPath.getWindingRule());
        Point2D center = bodyPath.getBounds2D().getCenterX();
        assertEquals(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, center.getX(), 0.01);
        assertEquals(robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0, center.getY(), 0.01);

        // Test painting shield
        if (renderer.isFillShield()) {
            Area shieldArea = (Area) g2d.getClip();
            Rectangle2D.Double shieldBoundingBox = shieldArea.getBounds();
            assertEquals(robotSnapshot.getX(), shieldBoundingBox.getMinX(), 0.01);
            assertEquals(robotSnapshot.getY(), shieldBoundingBox.getMinY(), 0.01);
            assertEquals(robotSnapshot.getX() + 15.0, shieldBoundingBox.getMaxX(), 0.01);
            assertEquals(robotSnapshot.getY() + 15.0, shieldBoundingBox.getMaxY(), 0.01);
        }

        // Test painting turret
        Line2D turretLine = (Line2D) g2d.getClip();
        assertEquals(robotSnapshot.getX(), turretLine.getX1(), 0.01);
        assertEquals(robotSnapshot.getY(), turretLine.getY1(), 0.01);
        assertEquals(robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, turretLine.getX2(), 0.01);
        assertEquals(robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0, turretLine.getY2(), 0.01);

        // Test painting name
        FontMetrics fontMetrics = g2d.getFontMetrics();
        float nameWidth = fontMetrics.getStringBounds(robotSnapshot.getName(), g2d).getWidth();
        float nameHeight = fontMetrics.getHeight();
        Point2D nameCenter = new Point2D.Double(robotSnapshot.getX() - nameWidth / 2.0, robotSnapshot.getY() - 10.0 - nameHeight);
        assertEquals(nameCenter.getX(), g2d.getCurrentTranslateX(), 0.01);
        assertEquals(nameCenter.getY(), g2d.getCurrentTranslateY(), 0.01);

        g2d.dispose();
    }

    @Test
    public void testPaintSelection() {
        Graphics2D g2d = createMockGraphics2D();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer renderer = new RobotRenderer();

        renderer.paintSelection(g2d, robotSnapshot);

        Shape selection = g2d.getClip();
        Ellipse2D.Double selectionBoundingBox = (Ellipse2D.Double) selection;
        assertEquals(robotSnapshot.getX(), selectionBoundingBox.getCenterX(), 0.01);
        assertEquals(robotSnapshot.getY(), selectionBoundingBox.getCenterY(), 0.01);
        assertEquals(30.0, selectionBoundingBox.getWidth(), 0.01);
        assertEquals(30.0, selectionBoundingBox.getHeight(), 0.01);

        Color selectionColor = g2d.getPaint();
        assertEquals(1.0f, selectionColor.getRed(), 0.01);
        assertEquals(1.0f, selectionColor.getGreen(), 0.01);
        assertEquals(0.25f, selectionColor.getAlpha(), 0.01);

        g2d.dispose();
    }

    @Test
    public void testPaintShield() {
        Graphics2D g2d = createMockGraphics2D();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer renderer = new RobotRenderer();

        renderer.paintShield(g2d, robotSnapshot);

        Area shieldArea = (Area) g2d.getClip();
        Rectangle2D.Double shieldBoundingBox = shieldArea.getBounds();
        if (renderer.isFillShield()) {
            assertEquals(robotSnapshot.getX(), shieldBoundingBox.getMinX(), 0.01);
            assertEquals(robotSnapshot.getY(), shieldBoundingBox.getMinY(), 0.01);
            assertEquals(robotSnapshot.getX() + 15.0, shieldBoundingBox.getMaxX(), 0.01);
            assertEquals(robotSnapshot.getY() + 15.0, shieldBoundingBox.getMaxY(), 0.01);
        } else {
            assertEquals(robotSnapshot.getX(), shieldBoundingBox.getMinX(), 0.01);
            assertEquals(robotSnapshot.getY(), shieldBoundingBox.getMinY(), 0.01);
            assertEquals(100.0, shieldBoundingBox.getWidth(), 0.01);
            assertEquals(10.0, shieldBoundingBox.getHeight(), 0.01);
        }

        Color color1 = g2d.getPaint();
        assertEquals(0.5f, color1.getRed(), 0.01);
        assertEquals(0.5f, color1.getGreen(), 0.01);
        assertEquals(0.8f, color1.getBlue(), 0.01);
        assertEquals(0.8f, color1.getAlpha(), 0.01);

        Color color2 = g2d.getPaint();
        assertEquals(0.0f, color2.getRed(), 0.01);
        assertEquals(0.0f, color2.getGreen(), 0.01);
        assertEquals(0.5f, color2.getBlue(), 0.01);
        assertEquals(0.1f, color2.getAlpha(), 0.01);

        g2d.dispose();
    }

    @Test
    public void testPaintName() {
        Graphics2D g2d = createMockGraphics2D();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer renderer = new RobotRenderer();

        renderer.paintName(g2d, robotSnapshot);

        String name = g2d.getFontMetrics().getStringBounds(robotSnapshot.getName(), g2d).toString();
        assertEquals(robotSnapshot.getName(), name);

        g2d.dispose();
    }

    @Test
    public void testPaintHeat() {
        Graphics2D g2d = createMockGraphics2D();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer renderer = new RobotRenderer();

        renderer.paintHeat(g2d, robotSnapshot);

        Shape heatShape = g2d.getClip();
        Rectangle2D.Double heatRectangle = (Rectangle2D.Double) heatShape;
        assertEquals(robotSnapshot.getX() - 50.0, heatRectangle.getMinX(), 0.01);
        assertEquals(robotSnapshot.getY() + 35.0, heatRectangle.getMinY(), 0.01);
        assertEquals(100.0, heatRectangle.getWidth(), 0.01);
        assertEquals(10.0, heatRectangle.getHeight(), 0.01);

        GradientPaint gradientPaint = (GradientPaint) g2d.getPaint();
        assertEquals(RobotRenderer.HEAT_COLOR1, gradientPaint.getColor(0));
        assertEquals(RobotRenderer.HEAT_COLOR2, gradientPaint.getColor(1));

        g2d.dispose();
    }

    @Test
    public void testPaintArmor() {
        Graphics2D g2d = createMockGraphics2D();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer renderer = new RobotRenderer();

        renderer.paintArmor(g2d, robotSnapshot);

        Shape armorShape = g2d.getClip();
        Area armorArea = (Area) armorShape;
        Rectangle2D.Double armorRectangle = armorArea.getBounds();
        assertEquals(robotSnapshot.getX() - 50.0, armorRectangle.getMinX(), 0.01);
        assertEquals(robotSnapshot.getY() + 20.0, armorRectangle.getMinY(), 0.01);
        assertEquals(robotSnapshot.getArmor(), armorRectangle.getWidth(), 0.01);
        assertEquals(10.0, armorRectangle.getHeight(), 0.01);

        Color color1 = g2d.getPaint();
        assertEquals(0.0f, color1.getRed(), 0.01);
        assertEquals(0.0f, color1.getGreen(), 0.01);
        assertEquals(1.0f, color1.getBlue(), 0.01);
        assertEquals(0.6f, color1.getAlpha(), 0.01);

        Color color2 = g2d.getPaint();
        assertEquals(0.0f, color2.getRed(), 0.01);
        assertEquals(1.0f, color2.getGreen(), 0.01);
        assertEquals(0.0f, color2.getBlue(), 0.01);
        assertEquals(0.6f, color2.getAlpha(), 0.01);

        g2d.dispose();
    }

    @Test
    public void testPaintTurret() {
        Graphics2D g2d = createMockGraphics2D();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer renderer = new RobotRenderer();

        renderer.paintTurret(g2d, robotSnapshot);

        Line2D turretLine = (Line2D) g2d.getClip();
        assertEquals(robotSnapshot.getX(), turretLine.getX1(), 0.01);
        assertEquals(robotSnapshot.getY(), turretLine.getY1(), 0.01);
        assertEquals(robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, turretLine.getX2(), 0.01);
        assertEquals(robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0, turretLine.getY2(), 0.01);

        g2d.dispose();
    }

    @Test
    public void testPaintBody() {
        Graphics2D g2d = createMockGraphics2D();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        RobotRenderer renderer = new RobotRenderer();

        renderer.paintBody(g2d, robotSnapshot);

        Path2D bodyPath = (Path2D) g2d.getClip();
        assertEquals(3, bodyPath.getWindingRule());
        Point2D center = bodyPath.getBounds2D().getCenterX();
        assertEquals(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, center.getX(), 0.01);
        assertEquals(robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0, center.getY(), 0.01);

        if (robotSnapshot.isDead()) {
            Stroke stroke = g2d.getStroke();
            BasicStroke basicStroke = (BasicStroke) stroke;
            assertEquals(1.0f, basicStroke.getLineWidth(), 0.01);
            assertEquals(1.0f, basicStroke.getMiterLimit(), 0.01);
            assertEquals(1, basicStroke.getDashPattern().length);
            assertEquals(5.0f, basicStroke.getDashPattern()[0], 0.01);
            assertEquals(Color.orange.darker().darker(), basicStroke.getLineColor());
        } else {
            Stroke stroke = g2d.getStroke();
            assertEquals(null, stroke);
        }

        g2d.dispose();
    }

    @Test
    public void testSetRenderDead() {
        RobotRenderer renderer = new RobotRenderer();
        assertTrue(renderer.isRenderDead());
        renderer.setRenderDead(false);
        assertFalse(renderer.isRenderDead());

        renderer.setRenderDead(true);
        assertTrue(renderer.isRenderDead());
    }

    @Test
    public void testSetShowStatusBars() {
        RobotRenderer renderer = new RobotRenderer();
        assertTrue(renderer.isShowStatusBars());
        renderer.setShowStatusBars(false);
        assertFalse(renderer.isShowStatusBars());

        renderer.setShowStatusBars(true);
        assertTrue(renderer.isShowStatusBars());
    }

    @Test
    public void testSetShowName() {
        RobotRenderer renderer = new RobotRenderer();
        assertTrue(renderer.isShowName());
        renderer.setShowName(false);
        assertFalse(renderer.isShowName());

        renderer.setShowName(true);
        assertTrue(renderer.isShowName());
    }

    @Test
    public void testSetFillShield() {
        RobotRenderer renderer = new RobotRenderer();
        assertTrue(renderer.isFillShield());
        renderer.setFillShield(false);
        assertFalse(renderer.isFillShield());

        renderer.setFillShield(true);
        assertTrue(renderer.isFillShield());
    }

    private Graphics2D createMockGraphics2D() {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Graphics2D g2d = gc.createCompatibleGraphicContext(null);
        return g2d;
    }
}