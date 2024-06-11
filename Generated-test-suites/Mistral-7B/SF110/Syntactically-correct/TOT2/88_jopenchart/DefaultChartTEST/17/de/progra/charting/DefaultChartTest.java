package de.progra.charting.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.Model;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.render.ChartRendererMap;
import de.progra.charting.render.CoordSystemRenderer;

public class DefaultChartTest {
    // ... test cases from Experts 1 and 2

    @Test
    public void testStaticInitialization() {
        assertEquals(0, DefaultChart.LINEAR_X_LINEAR_Y);
        assertEquals(1, DefaultChart.NO_COORDINATE_SYSTEM);
    }

    // ...
}