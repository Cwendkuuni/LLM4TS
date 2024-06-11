import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import java.awt.*;
import java.text.*;
import org.junit.Before;
import org.junit.Test;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;
    private ChartDataModel model;
    private CoordSystem coord;

    @Before
    public void setUp() {
        constraints = new ChartDataModelConstraints();
        constraints2 = new ChartDataModelConstraints();
        model = new ChartDataModel();
        coord = new CoordSystem();
        utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        constraints.setMinimumColumnValue(-10.5);
        constraints.setMaximumColumnValue(15.0);
        assertEquals(37, utilities.computeLeftMargin());

        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(15.0);
        assertEquals(20, utilities.computeLeftMargin());

        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(0.0);
        assertEquals(20, utilities.computeLeftMargin());

        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        assertThrows(IllegalArgumentException.class, () -> utilities.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        assertEquals(15, utilities.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        assertEquals(15, utilities.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        constraints.setMinimumValue(Double.MIN_VALUE);
        constraints.setMaximumValue(Double.MIN_VALUE);
        assertEquals(37, utilities.computeBottomMargin());

        constraints.setMinimumValue(Double.MIN_VALUE);
        constraints.setMaximumValue(0.0);
        assertEquals(37, utilities.computeBottomMargin());

        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(Double.MAX_VALUE);
        assertEquals(37, utilities.computeBottomMargin());

        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(1.0);
        assertEquals(20, utilities.computeBottomMargin());

        constraints.setMinimumValue(Double.MAX_VALUE);
        constraints.setMaximumValue(Double.MAX_VALUE);
        assertEquals(20, utilities.computeBottomMargin());

        constraints.setMinimumValue(-1.0);
        constraints.setMaximumValue(0.0);
        assertThrows(IllegalArgumentException.class, () -> utilities.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        constraints.setMinimumColumnValue(-1.0);
        constraints.setMaximumColumnValue(1.0);
        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(1.0);
        assertEquals(20, utilities.computeXAxisLabelHeight());

        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(10.0);
        assertEquals(100, utilities.computeXAxisLabelHeight());

        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(10.0);
        assertEquals(50, utilities.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(1.0);
        constraints.setMinimumColumnValue(-1.0);
        constraints.setMaximumColumnValue(1.0);
        assertEquals(15, utilities.computeYAxisLabelWidth());

        constraints.setMinimumValue(-1.0);
        constraints.setMaximumValue(1.0);
        constraints.setMinimumColumnValue(-1.0);
        constraints.setMaximumColumnValue(1.0);
        assertEquals(45, utilities.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // Test with empty Graphics2D
        Graphics2D g = new Graphics2D() {};
        utilities.drawNumericalXAxisTicks(g);

        // Test with valid arguments
        g = new MockGraphics2D();
        constraints.setMinimumColumnValue(-1.0);
        constraints.setMaximumColumnValue(1.0);
        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(1.0);
        utilities.drawNumericalXAxisTicks(g);
    }

    @Test
    public void testDrawXAxisTicks() {
        // Test with empty Graphics2D
        Graphics2D g = new Graphics2D() {};
        utilities.drawXAxisTicks(g);

        // Test with valid arguments
        g = new MockGraphics2D();
        constraints.setMinimumColumnValue(-1);
        constraints.setMaximumColumnValue(5);
        utilities.drawXAxisTicks(g);
    }

    @Test
    public void testDrawYAxisTicks() {
        // Test with empty Graphics2D
        Graphics2D g = new Graphics2D() {};
        utilities.drawYAxisTicks(g);

        // Test with valid arguments
        g = new MockGraphics2D();
        constraints.setMinimumValue(-1.0);
        constraints.setMaximumValue(1.0);
        utilities.drawYAxisTicks(g);
    }

    @Test
    public void testGetXAxisLine2D() {
        // Test with valid arguments
        Line2D line = utilities.getXAxisLine2D();
        assertNotNull(line);
    }

    @Test
    public void testGetYAxisLine2D() {
        // Test with valid arguments
        Line2D line = utilities.getYAxisLine2D();
        assertNotNull(line);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        // Test with valid arguments
        Line2D line = utilities.getSecondYAxisLine2D();
        assertNotNull(line);
    }
}