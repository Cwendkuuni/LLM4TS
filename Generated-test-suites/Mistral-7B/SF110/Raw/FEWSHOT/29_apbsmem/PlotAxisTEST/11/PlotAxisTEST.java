package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testPlotAxisInstance() {
        PlotAxis axis = new PlotAxis();
        assertNotNull(axis);
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
        assertEquals(1, axis.gridStyle);
        assertNull(axis.label);
        assertNull(axis.scale);
        assertNull(axis.nf);
    }

    @Test
    public void testPlotAxisWithLabelScaleFormatAndGridStyle() {
        PlotAxis axis = new PlotAxis("Test Axis", new LinearAxisScale(), new DecimalFormat("#.####"), 2);
        assertNotNull(axis);
        assertEquals("Test Axis", axis.getLabel());
        assertNotNull(axis.scale);
        assertEquals(2, axis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("Test Axis");
        assertEquals("Test Axis", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        axis.setScale(new LogAxisScale());
        assertEquals(new LogAxisScale().getClass(), axis.getScale().getClass());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        axis.setNumberFormat(NumberFormat.getNumberInstance());
        assertNotNull(axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(1);
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        assertTrue(axis.manualLB);
        axis.manualLowerBounds(false);
        assertFalse(axis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(1.5);
        assertEquals(1.5, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(1.5);
        assertEquals("1.5", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        assertTrue(axis.manualUB);
        axis.manualUpperBounds(false);
        assertFalse(axis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(2.5);
        assertEquals(2.5, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(2.5);
        assertEquals("2.5", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        assertTrue(axis.manualQuant);
        axis.manualTickStepSize(false);
        assertFalse(axis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTickStepSizeNegativeValue() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(-0.5);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        // Stubbing the setAxisTickLimits and resizeAxis methods
        axis.setAxisTickLimits = (p) -> {};
        axis.resizeAxis = (p) -> {};
        axis.setAxisBounds(new PlotRunList());
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(10, 10, 50, 50);
        axis.setSize(plotFrame);
        // Checking the size of the axis after setting the size
        assertEquals(50, axis.getWidth(), 0.0);
        assertEquals(50, axis.getHeight(), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testSetSizeNullComponent() {
        PlotAxis axis = new PlotAxis();
        axis.setSize(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetSizeNullPlotFrame() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = null;
        axis.setSize(plotFrame);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        double x = 0.5;
        int expected = (int)(axis.xA * axis.scale.func(x) + axis.xB);
        int actual = axis.scaleCoord(x);
        assertEquals(expected, actual);
    }

    @Test
    public void testClone() {
        PlotAxis originalAxis = new PlotAxis();
        PlotAxis clonedAxis = originalAxis.clone();
        assertNotNull(clonedAxis);
        assertNotSame(originalAxis, clonedAxis);
        assertEquals(originalAxis.getClass(), clonedAxis.getClass());
    }

    @Test
    public void testDraw() {
        PlotAxis axis = new PlotAxis();
        Graphics g = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR));
        Component c = new JFrame();
        Rectangle rect = new Rectangle(0, 0, 10, 10);

        // Drawing the axis with no tick marks and no grid
        axis.draw(g, c, rect);
        assertTrue(g.getColor().equals(Color.BLACK));
        assertFalse(g.getStroke().equals(new Line2D.Double(0.0, 0.0, 2.0).createStrokedShape(1.0)));

        // Drawing the axis with tick marks only
        axis.gridStyle = PlotAxis.kTickMarksOnly;
        axis.draw(g, c, rect);
        assertTrue(g.getColor().equals(Color.BLACK));
        assertTrue(g.getStroke().equals(new Line2D.Double(0.0, 0.0, 2.0).createStrokedShape(1.0)));

        // Drawing the axis with major grid
        axis.gridStyle = PlotAxis.kMajorGrid;
        axis.draw(g, c, rect);
        assertTrue(g.getColor().equals(Color.BLACK));
        assertTrue(g.getStroke().equals(new Line2D.Double(0.0, 0.0, 2.0).createStrokedShape(1.0)));

        // Drawing the axis with tick marks and major grid
        axis.gridStyle = PlotAxis.kTickAndMajorsGrid;
        axis.draw(g, c, rect);
        assertTrue(g.getColor().equals(Color.BLACK));
        assertTrue(g.getStroke().equals(new Line2D.Double(0.0, 0.0, 2.0).createStrokedShape(1.0)));
    }
}