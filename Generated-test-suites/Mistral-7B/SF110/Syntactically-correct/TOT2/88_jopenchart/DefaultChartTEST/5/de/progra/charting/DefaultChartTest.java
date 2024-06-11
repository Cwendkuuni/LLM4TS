package de.progra.charting.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import java.awt.*;
import java.util.*;

public class DefaultChartTest {

    DefaultChart chart;
    ChartDataModel model;

    @Before
    public void setUp() {
        model = new ChartDataModel();
    }

    

    // Test Case 1: Default Chart Constructor
    @Test
    public void testDefaultChartConstructor() {
        chart = new DefaultChart();
        assertNull(chart.getTitle());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
    }

    // Test Case 2: Chart With Title Constructor
    @Test
    public void testDefaultChartWithTitleConstructor() {
        chart = new DefaultChart(model, "Test Chart");
        assertNotNull(chart.getTitle());
        assertEquals("Test Chart", chart.getTitle().getCaption());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
    }

    // Test Case 3: Chart With Coordinate System Constructor
    @Test
    public void testDefaultChartWithCoordinateSystemConstructor() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getTitle());
        assertEquals("Test Chart", chart.getTitle().getCaption());
        assertNotNull(chart.getCoordSystem());
        // ... Other assertions for CoordSystem
    }

    // Test Case 4: Chart With Title, Coordinate System, and Units Constructor
    @Test
    public void testDefaultChartWithTitleCoordinateSystemAndUnitsConstructor() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "XAxisUnit", "YAxisUnit");
        assertNotNull(chart.getTitle());
        assertEquals("Test Chart", chart.getTitle().getCaption());
        assertNotNull(chart.getCoordSystem());
        // ... Other assertions for CoordSystem, XAxisUnit, and YAxisUnit
    }

    // Test Case 5: Get Preferred Size
    @Test
    public void testGetPreferredSize() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNull(chart.getPreferredSize());
    }

    // Test Case 6: Render Method
    @Test
    public void testRenderMethod() {
        ChartDataModel model = new ChartDataModel();
        model.addDataSeries("Test Series", new Double[][]{
            {1.0, 2.0}, {2.0, 4.0}, {3.0, 6.0}, {4.0, 8.0}, {5.0, 10.0}
        });
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        Graphics2D g = mock(Graphics2D.class);
        chart.render(g);
        // ... Other assertions for rendering
    }

    // Test Case 7: Null ChartDataModel
    @Test(expected = IllegalArgumentException.class)
    public void testDefaultChartNullChartDataModel() {
        chart = new DefaultChart(null);
    }

    // Test Case 8: Empty Title
    @Test
    public void testDefaultChartEmptyTitle() {
        chart = new DefaultChart(model, "");
        assertNotNull(chart.getTitle());
        assertEquals("", chart.getTitle().getCaption());
    }

    // Test Case 9: Null Title
    @Test(expected = IllegalArgumentException.class)
    public void testDefaultChartNullTitle() {
        chart = new DefaultChart(model, null);
    }

    // Test Case 10: Null Legend
    @Test
    public void testDefaultChartNullLegend() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, null, null);
        assertNotNull(chart.getLegend());
        assertNull(chart.getLegend().getItems());
    }

    // Test Case 11: Null CoordSystem
    @Test
    public void testDefaultChartNullCoordSystem() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "XAxisUnit", "YAxisUnit");
        assertNotNull(chart.getCoordSystem());
        assertNull(chart.getCoordSystem().getXAxis());
        assertNull(chart.getCoordSystem().getYAxis());
    }

    // Test Case 12: Invalid Coordinate System
    @Test(expected = IllegalArgumentException.class)
    public void testDefaultChartInvalidCoordinateSystem() {
        chart = new DefaultChart(model, "Test Chart", -1);
    }

    // Test Case 13: Set XAxisUnit Null
    @Test(expected = IllegalArgumentException.class)
    public void testDefaultChartSetXAxisUnitNull() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, null, "YAxisUnit");
    }

    // Test Case 14: Set YAxisUnit Null
    @Test(expected = IllegalArgumentException.class)
    public void testDefaultChartSetYAxisUnitNull() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "XAxisUnit", null);
    }

    // Test Case 15: Render Method with Null Graphics2D
    @Test(expected = IllegalArgumentException.class)
    public void testDefaultChartRenderNullGraphics2D() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.render(null);
    }

    
}