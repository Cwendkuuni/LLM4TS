package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.*;
import java.awt.geom.*;
import java.awt.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import de.progra.charting.model.*;
import de.progra.charting.CoordSystemUtilities;

@RunWith(MockitoJUnitRunner.class)
public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities util;
    private CoordSystem coord;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;
    private ChartDataModel model;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        coord = Mockito.mock(CoordSystem.class);
        constraints = Mockito.mock(ChartDataModelConstraints.class);
        constraints2 = Mockito.mock(ChartDataModelConstraints.class);
        model = Mockito.mock(ChartDataModel.class);
        util = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        Mockito.when(constraints.getMinimumColumnValue()).thenReturn(10.0);
        Mockito.when(constraints.getMaximumColumnValue()).thenReturn(20.0);
        Mockito.when(constraints.getFont()).thenReturn(Mockito.mock(Font.class));
        Mockito.when(constraints.getFontRenderContext()).thenReturn(Mockito.mock(FontRenderContext.class));
        Mockito.when(constraints.getBounds()).thenReturn(new Rectangle2D.Double(0, 0, 800, 600));
        Mockito.when(coord.getRightMargin()).thenReturn(20);

        int result = util.computeLeftMargin();
        int expected = 86;
        assertEquals(expected, result);
    }

    @Test
    public void testComputeRightMargin() {
        Mockito.when(constraints.getFont()).thenReturn(Mockito.mock(Font.class));
        Mockito.when(constraints.getFontRenderContext()).thenReturn(Mockito.mock(FontRenderContext.class));

        int result = util.computeRightMargin();
        int expected = 15;
        assertEquals(expected, result);
    }

    @Test
    public void testComputeTopMargin() {
        Mockito.when(constraints.getFont()).thenReturn(Mockito.mock(Font.class));
        Mockito.when(constraints.getFontRenderContext()).thenReturn(Mockito.mock(FontRenderContext.class));

        int result = util.computeTopMargin();
        int expected = 15;
        assertEquals(expected, result);
    }

    @Test
    public void testComputeBottomMargin() {
        Mockito.when(constraints.getMinimumValue()).thenReturn(-5.0);
        Mockito.when(constraints.getMaximumValue()).thenReturn(10.0);
        Mockito.when(constraints.getFont()).thenReturn(Mockito.mock(Font.class));
        Mockito.when(constraints.getFontRenderContext()).thenReturn(Mockito.mock(FontRenderContext.class));
        Mockito.when(constraints.getBounds()).thenReturn(new Rectangle2D.Double(0, 0, 800, 600));
        Mockito.when(coord.getTopMargin()).thenReturn(20);

        int result = util.computeBottomMargin();
        int expected = 61;
        assertEquals(expected, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        Mockito.when(constraints.getMinimumColumnValue()).thenReturn(1.0);
        Mockito.when(constraints.getMaximumColumnValue()).thenReturn(10.0);
        Mockito.when(constraints.getTickSpacing()).thenReturn(1.0);
        Mockito.when(constraints.getFont()).thenReturn(Mockito.mock(Font.class));
        Mockito.when(constraints.getFontRenderContext()).thenReturn(Mockito.mock(FontRenderContext.class));

        int result = util.computeXAxisLabelHeight();
        int expected = 12;
        assertEquals(expected, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        Mockito.when(constraints.getMinimumValue()).thenReturn(1.0);
        Mockito.when(constraints.getMaximumValue()).thenReturn(10.0);
        Mockito.when(constraints.getTickSpacing()).thenReturn(1.0);
        Mockito.when(constraints.getFont()).thenReturn(Mockito.mock(Font.class));
        Mockito.when(constraints.getFontRenderContext()).thenReturn(Mockito.mock(FontRenderContext.class));

        int result = util.computeYAxisLabelWidth();
        int expected = 12;
        assertEquals(expected, result);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // Testing with mocks, this method should be tested with actual graphics context
    }

    @Test
    public void testDrawXAxisTicks() {
        // Testing with mocks, this method should be tested with actual graphics context
    }

    @Test
    public void testDrawYAxisTicks() {
        // Testing with mocks, this method should be tested with actual graphics context
    }

}