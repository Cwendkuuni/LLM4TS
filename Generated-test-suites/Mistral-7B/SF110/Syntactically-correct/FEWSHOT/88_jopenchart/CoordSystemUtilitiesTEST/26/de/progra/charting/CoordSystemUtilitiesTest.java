package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.Locale;

import org.junit.Test;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.model.ChartDataModelConstraints;
import de.progra.charting.model.CoordSystem;
import de.progra.charting.utils.ChartUtilities;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(5.0);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(32, util.computeLeftMargin());

        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);

        assertEquals(36, util.computeLeftMargin());

        constraints.setMinimumColumnValue(-1.0);
        constraints.setMaximumColumnValue(11.0);

        assertEquals(42, util.computeLeftMargin());

        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(1.0);

        assertEquals(52, util.computeLeftMargin());

        constraints.setMinimumColumnValue(-100.0);
        constraints.setMaximumColumnValue(1.0);

        assertEquals(200, util.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(21, util.computeRightMargin());

        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(5.0);

        assertEquals(27, util.computeRightMargin());

        constraints.setMinimumColumnValue(-1.0);
        constraints.setMaximumColumnValue(11.0);

        assertEquals(33, util.computeRightMargin());

        constraints.setMinimumColumnValue(-1.0);
        constraints.setMaximumColumnValue(1.0);

        assertEquals(39, util.computeRightMargin());

        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(1.0);

        assertEquals(95, util.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(Double.MIN_VALUE);
        constraints.setMaximumValue(Double.MAX_VALUE);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(31, util.computeTopMargin());

        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(10.0);

        assertEquals(35, util.computeTopMargin());

        constraints.setMinimumValue(-1.0);
        constraints.setMaximumValue(11.0);

        assertEquals(41, util.computeTopMargin());

        constraints.setMinimumValue(-1.0);
        constraints.setMaximumValue(1.0);

        assertEquals(47, util.computeTopMargin());

        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(1.0);

        assertEquals(151, util.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(1.0);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(31, util.computeBottomMargin());

        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);

        assertEquals(37, util.computeBottomMargin());

        constraints.setMinimumValue(-1.0);
        constraints.setMaximumValue(11.0);

        assertEquals(43, util.computeBottomMargin());

        constraints.setMinimumValue(-1.0);
        constraints.setMaximumValue(1.0);

        assertEquals(49, util.computeBottomMargin());

        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(1.0);

        assertEquals(151, util.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelWidth() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(1.0);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setLocale(Locale.ENGLISH);

        assertEquals(168, util.computeXAxisLabelWidth());

        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(5.0);

        assertEquals(122, util.computeXAxisLabelWidth());

        constraints.setMinimumColumnValue(-1.0);
        constraints.setMaximumColumnValue(11.0);

        assertEquals(226, util.computeXAxisLabelWidth());

        constraints.setMinimumColumnValue(-1.0);
        constraints.setMaximumColumnValue(1.0);

        assertEquals(128, util.computeXAxisLabelWidth());

        constraints.setMinimumColumnValue(-100.0);
        constraints.setMaximumColumnValue(1.0);

        assertEquals(604, util.computeXAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(1.0);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        when(g.getTransform()).thenReturn(Mockito.mock(AffineTransform.class));

        util.drawNumericalXAxisTicks(g);

        verify(g, times(1)).draw(Mockito.any(Line2D.class));
        verify(g, times(11)).drawString(Mockito.anyString(), Mockito.anyFloat(), Mockito.anyFloat());
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10);
        constraints.setMaximumColumnValue(11);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        when(g.getTransform()).thenReturn(Mockito.mock(AffineTransform.class));

        util.drawXAxisTicks(g);

        verify(g, times(11)).draw(Mockito.any(Line2D.class));
        verify(g, times(10)).drawString(Mockito.anyString(), Mockito.anyFloat(), Mockito.anyFloat());
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(1.0);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        when(g.getTransform()).thenReturn(Mockito.mock(AffineTransform.class));

        util.drawYAxisTicks(g);

        verify(g, times(1)).draw(Mockito.any(Line2D.class));
        verify(g, times(11)).drawString(Mockito.anyString(), Mockito.anyFloat(), Mockito.anyFloat());
    }
}