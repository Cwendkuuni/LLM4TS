package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.model.Datapoint;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {

    private ChartDataModel model;
    private DefaultChart chart;

    @Before
    public void setUp() {
        model = new ChartDataModel();
        model.addSeries(new Series("Series1", Arrays.asList(new Datapoint(1, 1), new Datapoint(2, 2), new Datapoint(3, 3))));
        chart = new DefaultChart(model, "TestChart");
    }

    @Test
    public void testDefaultChartConstruction() {
        assertNotNull(chart);
    }

    @Test
    public void testChartConstructionWithTitle() {
        DefaultChart chart = new DefaultChart(model, "TestChart");
        assertNotNull(chart.getTitle());
        assertEquals("TestChart", chart.getTitle().getText());
    }

    @Test
    public void testChartConstructionWithTitleAndCoordinateSystem() {
        DefaultChart chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testChartConstructionWithTitleAndCoordinateSystemAndAxisLabels() {
        DefaultChart chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y, "XaxisLabel", "YaxisLabel");
        assertNotNull(chart.getCoordSystem());
        assertEquals("XaxisLabel", chart.getCoordSystem().getXAxisUnit());
        assertEquals("YaxisLabel", chart.getCoordSystem().getYAxisUnit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChartConstructionWithInvalidCoordinateSystem() {
        new DefaultChart(model, "TestChart", 3);
    }

    @Test
    public void testGetPreferredSize() {
        assertEquals(0, chart.getPreferredSize().getWidth());
        assertEquals(0, chart.getPreferredSize().getHeight());
    }

    @Test
    public void testRender() {
        Graphics2D g = mock(Graphics2D.class);
        chart.render(g);
        // Add assertions for rendering components (title, legend, coordinate system, chart renderers)
    }

    @Test(expected = NullPointerException.class)
    public void testRenderWithNullGraphics() {
        chart.render(null);
    }

    @Test
    public void testSetChartDataModel() {
        DefaultChart chart = new DefaultChart();
        ChartDataModel model = new ChartDataModel();
        chart.setChartDataModel(model);
        assertEquals(model, chart.getChartDataModel());
    }

    @Test
    public void testSetRowColorModel() {
        DefaultChart chart = new DefaultChart();
        RowColorModel rowColorModel = new RowColorModel(new ChartDataModel());
        chart.setRowColorModel(rowColorModel);
        assertEquals(rowColorModel, chart.getRowColorModel());
    }

    @Test
    public void testSetLegend() {
        DefaultChart chart = new DefaultChart();
        Legend legend = new Legend(new RowColorModel(new ChartDataModel()));
        chart.setLegend(legend);
        assertEquals(legend, chart.getLegend());
    }

    @Test
    public void testSetTitle() {
        DefaultChart chart = new DefaultChart();
        Title title = new Title("TestTitle");
        chart.setTitle(title);
        assertEquals(title, chart.getTitle());
    }

    @Test
    public void testSetCoordSystem() {
        DefaultChart chart = new DefaultChart();
        CoordSystem coordSystem = new CoordSystem(new ChartDataModel());
        chart.setCoordSystem(coordSystem);
        assertEquals(coordSystem, chart.getCoordSystem());
    }

    @Test
    public void testGetChartRenderer() {
        DefaultChart chart = new DefaultChart();
        HashMap<String, AbstractChartRenderer> chartRenderer = new HashMap<>();
        chart.setChartRenderer(chartRenderer);
        assertEquals(chartRenderer, chart.getChartRenderer());
    }
}