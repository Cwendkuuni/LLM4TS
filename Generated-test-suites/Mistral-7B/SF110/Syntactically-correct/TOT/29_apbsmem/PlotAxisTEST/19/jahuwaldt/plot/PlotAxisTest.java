package jahuwaldt.plot.test;

import org.junit.Test;
import static org.junit.Assert.*;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;
import java.text.NumberFormat;

public class PlotAxisTest {

    @Test
    public void testDefaultConstructor() {
        PlotAxis axis = new PlotAxis();
        // Test default values
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
        assertEquals(1, axis.gridStyle);
        assertNotNull(axis.scale);
        assertInstanceof(LinearAxisScale.class, axis.scale);
        assertNotNull(axis.nf);
        assertInstanceof(NumberFormat.class, axis.nf);
        assertNull(axis.label);
    }

    @Test
    public void testConstructorWithArguments() {
        PlotAxis axis = new PlotAxis("TestAxis", new LogAxisScale(), NumberFormat.getNumberInstance(), 2);
        // Test provided values
        assertEquals("TestAxis", axis.getLabel());
        assertInstanceof(LogAxisScale.class, axis.getScale());
        assertInstanceof(NumberFormat.class, axis.getNumberFormat());
        assertEquals(2, axis.getGridStyle());
        // Test default values
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertNotSame(Double.MAX_VALUE, axis.aLB);
        assertNotSame(-1.7976931348623157E308, axis.aUB);
    }

    @Test
    public void testSettersAndGetters() {
        PlotAxis axis = new PlotAxis();
        // Test initial values
        assertNull(axis.getLabel());
        assertInstanceof(LinearAxisScale.class, axis.getScale());
        assertInstanceof(NumberFormat.class, axis.getNumberFormat());
        assertEquals(1, axis.getGridStyle());

        // Test setting values
        axis.setLabel("TestLabel");
        axis.setScale(new TimeAxisScale());
        axis.setNumberFormat(NumberFormat.getPercentInstance());
        axis.setGridStyle(0);

        // Test getting values
        assertEquals("TestLabel", axis.getLabel());
        assertInstanceof(TimeAxisScale.class, axis.getScale());
        assertInstanceof(NumberFormat.class, axis.getNumberFormat());
        assertEquals(0, axis.getGridStyle());
    }
}