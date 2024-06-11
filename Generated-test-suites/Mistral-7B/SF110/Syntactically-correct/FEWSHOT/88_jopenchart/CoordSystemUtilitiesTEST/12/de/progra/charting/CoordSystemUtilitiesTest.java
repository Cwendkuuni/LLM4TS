package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
import java.text.*;

import org.apache.commons.decimal.DecimalFormat;
import org.apache.commons.decimal.DecimalFormatSymbols;
import org.apache.commons.decimal.MathContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.DataColumn;

@RunWith(MockitoJUnitRunner.class)
public class CoordSystemUtilitiesTest {

    @InjectMocks
    CoordSystemUtilities utilities;

    @Mock
    CoordSystem coordSystem;

    @Mock
    ChartDataModel chartDataModel;

    @Mock
    ChartDataModelConstraints constraints;

    @Mock
    ChartDataModelConstraints constraints2;

    @Mock
    DecimalFormat xDecimalFormat;

    @Mock
    DecimalFormat yDecimalFormat;

    @Test
    public void testComputeLeftMargin() {
        when(coordSystem.getXAxisUnit()).thenReturn("Unit X");
        when(coordSystem.getFont()).thenReturn(Mockito.mock(Font.class));
        when(coordSystem.getFontRenderContext()).thenReturn(Mockito.mock(FontRenderContext.class));
        when(coordSystem.getBounds()).thenReturn(new Rectangle(0, 0, 1000, 1000));
        when(coordSystem.getRightMargin()).thenReturn(10);

        when(constraints.getMinimumColumnValue()).thenReturn(-100.0);
        when(constraints.getMaximumColumnValue()).thenReturn(100.0);

        when(xDecimalFormat.format(Mockito.anyDouble())).then((Answer<String>) invocation -> "Formatted value");

        assertEquals(145, utilities.computeLeftMargin(coordSystem, constraints, constraints2, chartDataModel));
    }

    // You can add more test cases for other methods in the same way.
    // Make sure to mock all necessary dependencies and verify their behavior.

}