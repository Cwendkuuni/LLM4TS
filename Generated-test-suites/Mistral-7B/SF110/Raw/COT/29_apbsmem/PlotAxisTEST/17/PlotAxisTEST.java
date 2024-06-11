package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {

    private PlotAxis xAxis;
    private PlotAxis yAxis;

    @Before
    public void setup() {
        xAxis = new LinearAxis();
        yAxis = new LinearAxis();
    }

    @Test
    public void testConstructor() {
        PlotAxis plotAxis = new PlotAxis();
        assertEquals(1.0, plotAxis.getxA(), 0.0);
        assertEquals(1.0, plotAxis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, plotAxis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, plotAxis.getAU(), 0.0);
        assertNull(plotAxis.getLabel());
        assertSame(LinearAxisScale.class, plotAxis.getScale().getClass());
        assertEquals(NumberFormat.getInstance().getClass(), plotAxis.getNumberFormat().getClass());
        assertEquals(1, plotAxis.getGridStyle());
    }

    @Test
    public void testConstructorWithLabelScaleNumberFormatAndGridStyle() {
        String label = "x-axis";
        PlotAxisScale scale = new LinearAxisScale();
        NumberFormat nf = NumberFormat.getNumberInstance();
        int gridStyle = 2;
        PlotAxis plotAxis = new PlotAxis(label, scale, nf, gridStyle);
        assertEquals(label, plotAxis.getLabel());
        assertSame(scale.getClass(), plotAxis.getScale().getClass());
        assertSame(nf.getClass(), plotAxis.getNumberFormat().getClass());
        assertEquals(gridStyle, plotAxis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        xAxis.setLabel("x-axis");
        assertEquals("x-axis", xAxis.getLabel());
    }

    @Test
    public void testGetLabel() {
        xAxis.setLabel("x-axis");
        assertEquals("x-axis", xAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxisScale scale = new LogAxisScale();
        xAxis.setScale(scale);
        assertSame(scale.getClass(), xAxis.getScale().getClass());
    }

    @Test
    public void testGetScale() {
        PlotAxisScale scale = new LogAxisScale();
        xAxis.setScale(scale);
        assertSame(scale.getClass(), xAxis.getScale().getClass());
    }

    @Test
    public void testSetNumberFormat() {
        NumberFormat nf = NumberFormat.getPercentInstance();
        xAxis.setNumberFormat(nf);
        assertSame(nf.getClass(), xAxis.getNumberFormat().getClass());
    }

    @Test
    public void testGetNumberFormat() {
        NumberFormat nf = NumberFormat.getPercentInstance();
        xAxis.setNumberFormat(nf);
        assertSame(nf.getClass(), xAxis.getNumberFormat().getClass());
    }

    @Test
    public void testSetGridStyle() {
        xAxis.setGridStyle(kMajorGrid);
        assertEquals(kMajorGrid, xAxis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        xAxis.setGridStyle(kMajorGrid);
        assertEquals(kMajorGrid, xAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        xAxis.manualLowerBounds(true);
        assertTrue(xAxis.isManualLB());
    }

    @Test
    public void testSetLowerBounds() {
        xAxis.setLowerBounds(0.5);
        assertEquals(0.5, xAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        xAxis.setLowerBounds(0.5);
        assertEquals("0.5", xAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        xAxis.manualUpperBounds(true);
        assertTrue(xAxis.isManualUB());
    }

    @Test
    public void testSetUpperBounds() {
        xAxis.setUpperBounds(1.5);
        assertEquals(1.5, xAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        xAxis.setUpperBounds(1.5);
        assertEquals("1.5", xAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        xAxis.manualTickStepSize(true);
        assertTrue(xAxis.isManualQuant());
    }

    @Test
    public void testSetTickStepSize() {
        xAxis.setTickStepSize(0.1);
        assertEquals(0.1, xAxis.getTickStepSize(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTickStepSizeNegativeValue() {
        xAxis.setTickStepSize(-0.1);
    }

    @Test
    public void testSetAxisBounds() {
        // Abstract method, cannot be tested directly
    }

    @Test
    public void testSetSize() {
        // Abstract method, cannot be tested directly
    }

    @Test
    public void testSetAxisTickLimits() {
        xAxis.setAxisTickLimits();
        // Cannot test results directly, as they depend on scale and bounds
    }

    @Test
    public void testResizeAxis() {
        // Abstract method, cannot be tested directly
    }

    @Test
    public void testDraw() {
        // Cannot test directly, as it relies on Graphics and Component objects
    }

    @Test
    public void testScaleCoord() {
        xAxis.setAxisBounds(null);
        xAxis.setSize(new Rectangle(0, 0, 100, 100));
        int coordinate = 50;
        int result = xAxis.scaleCoord(0.5);
        assertEquals(result, coordinate);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        PlotAxis clonedAxis = (PlotAxis)xAxis.clone();
        assertNotSame(xAxis, clonedAxis);
        assertEquals(xAxis.getClass(), clonedAxis.getClass());
    }

    @Test
    public void testSerializability() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(xAxis);
        byte[] bytes = byteStream.toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(input);
        PlotAxis deserializedAxis = (PlotAxis)in.readObject();
        assertNotSame(xAxis, deserializedAxis);
        assertEquals(xAxis.getClass(), deserializedAxis.getClass());
    }
}