package jahuwaldt.plot;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import java.text.NumberFormat;
import java.text.DecimalFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testDefaultConstructor() {
        PlotAxis axis = new PlotAxis();
        assertEquals(1.0, axis.getxA(), 0.0);
        assertEquals(1.0, axis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
        assertNull(axis.getLabel());
        assertNull(axis.getScale());
        assertNull(axis.getNumberFormat());
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testConstructorWithLabelScaleNumberFormatAndGridStyle() {
        PlotAxis axis = new PlotAxis("TestAxis", new LinearAxisScale(), new DecimalFormat("#.##"), 2);
        assertEquals("TestAxis", axis.getLabel());
        assertEquals(new LinearAxisScale(), axis.getScale());
        assertEquals(new DecimalFormat("#.##"), axis.getNumberFormat());
        assertEquals(2, axis.getGridStyle());
        assertEquals(1.0, axis.getxA(), 0.0);
        assertEquals(1.0, axis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
    }

    @Test
    public void testSetAndGetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("TestLabel");
        assertEquals("TestLabel", axis.getLabel());
    }

    @Test
    public void testSetAndGetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LogAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetAndGetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = new DecimalFormat("#.###");
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
    }

    @Test
    public void testSetAndGetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(3);
        assertEquals(3, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        assertTrue(axis.isManualLB());
        axis.manualLowerBounds(false);
        assertFalse(axis.isManualLB());
    }

    @Test
    public void testSetAndGetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(0.5);
        assertEquals(0.5, axis.getLowerBounds(), 0.001);
        axis.setLowerBounds(2.0);
        assertEquals(2.0, axis.getLowerBounds(), 0.001);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = new DecimalFormat("#.##");
        axis.setLabel("TestLabel");
        axis.setNumberFormat(format);
        axis.setLowerBounds(1.5);
        assertEquals("1.5", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        assertTrue(axis.isManualUB());
        axis.manualUpperBounds(false);
        assertFalse(axis.isManualUB());
    }

    @Test
    public void testSetAndGetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(1.5);
        assertEquals(1.5, axis.getUpperBounds(), 0.001);
        axis.setUpperBounds(2.0);
        assertEquals(2.0, axis.getUpperBounds(), 0.001);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = new DecimalFormat("#.##");
        axis.setLabel("TestLabel");
        axis.setNumberFormat(format);
        axis.setUpperBounds(1.5);
        assertEquals("1.5", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        assertTrue(axis.isManualQuant());
        axis.manualTickStepSize(false);
        assertFalse(axis.isManualQuant());
    }

    @Test
    public void testSetAndGetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.001);
        axis.setTickStepSize(2.0);
        assertEquals(2.0, axis.getTickStepSize(), 0.001);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        assertEquals(0, axis.scaleCoord(0.0));
        assertEquals(1, axis.scaleCoord(1.0));
        assertEquals(2, axis.scaleCoord(2.0));
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneThrowsCloneNotSupportedException() throws CloneNotSupportedException {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis)axis.clone();
    }
}