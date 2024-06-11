package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testDefaultConstructor() {
        PlotAxis axis = new PlotAxis();
        assertNotNull(axis);
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
        assertEquals(1, axis.gridStyle);
        assertNull(axis.label);
        assertNull(axis.scale);
        assertNotNull(axis.nf);
    }

    @Test
    public void testCustomConstructor() {
        String label = "Test Label";
        PlotAxisScale scale = new LinearAxisScale();
        NumberFormat nf = NumberFormat.getInstance();
        int gridStyle = 2;

        PlotAxis axis = new PlotAxis(label, scale, nf, gridStyle);

        assertEquals("Test Label", axis.getLabel());
        assertEquals(scale, axis.getScale());
        assertEquals(nf, axis.getNumberFormat());
        assertEquals(gridStyle, axis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testGetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        assertEquals(nf, axis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        assertEquals(nf, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(3);
        assertEquals(3, axis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(3);
        assertEquals(3, axis.getGridStyle());
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
        double lowerBounds = 0.5;
        axis.setLowerBounds(lowerBounds);
        assertEquals(lowerBounds, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setLowerBounds(0.5);
        assertEquals("0.5", axis.lowerBoundsAsString());
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
        double upperBounds = 1.5;
        axis.setUpperBounds(upperBounds);
        assertEquals(upperBounds, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setUpperBounds(1.5);
        assertEquals("1.5", axis.upperBoundsAsString());
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
        double tickStepSize = 0.1;
        axis.setTickStepSize(tickStepSize);
        assertEquals(tickStepSize, axis.getTickStepSize(), 0.0);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        PlotRunList runList = new PlotRunList();
        axis.setAxisBounds(runList);
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(0, 0, 100, 100);
        axis.setSize(plotFrame);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        double x = 0.5;
        int expected = (int)(axis.xA * 0.5 + axis.xB);
        int actual = axis.scaleCoord(x);
        assertEquals(expected, actual);
    }

    @Test
    public void testDraw() {
        PlotAxis axis = new PlotAxis();
        Component component = new Component() {
            @Override
            public Graphics getGraphics() {
                return new Graphics() {
                    @Override
                    public void drawLine(int x1, int y1, int x2, int y2) {
                    }

                    @Override
                    public void setColor(Color color) {
                    }
                };
            }
        };
        Rectangle rectangle = new Rectangle(0, 0, 100, 100);
        axis.draw(new Graphics() {
        }, component, rectangle);
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testClone() throws CloneNotSupportedException {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis)axis.clone();
    }
}