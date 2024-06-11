package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;

import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelDataSeries;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {

    @Test
    public void testDefaultChartConstruction() {
        ChartDataModel model = new ChartDataModel();
        model.addSeries(new ModelDataSeries("Series1", new double[][]{new double[]{0, 1, 2, 3}, new double[]{1, 2, 3, 4}}));
        DefaultChart chart = new DefaultChart(model, "My Chart");
        assertNotNull(chart.getTitle());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getLegend());
        assertNull(chart.getRowColorModel());
    }

    @Test
    public void testChartWithCoordinateSystem() {
        ChartDataModel model = new ChartDataModel();
        model.addSeries(new ModelDataSeries("Series1", new double[][]{new double[]{0, 1, 2, 3}, new double[]{1, 2, 3, 4}}));
        DefaultChart chart = new DefaultChart(model, "My Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        CoordSystem cs = chart.getCoordSystem();
        assertNotNull(cs);
    }

    @Test
    public void testChartWithNoCoordinateSystem() {
        ChartDataModel model = new ChartDataModel();
        model.addSeries(new ModelDataSeries("Series1", new double[][]{new double[]{0, 1, 2, 3}, new double[]{1, 2, 3, 4}}));
        DefaultChart chart = new DefaultChart(model, "My Chart", DefaultChart.NO_COORDINATE_SYSTEM);
        assertNull(chart.getCoordSystem());
    }

    @Test
    public void testChartRendering() {
        ChartDataModel model = new ChartDataModel();
        model.addSeries(new ModelDataSeries("Series1", new double[][]{new double[]{0, 1, 2, 3}, new double[]{1, 2, 3, 4}}));
        model.addSeries(new ModelDataSeries("Series2", new double[][]{new double[]{0, 1, 2, 3}, new double[]{4, 5, 6, 7}}));
        DefaultChart chart = new DefaultChart(model, "My Chart", DefaultChart.LINEAR_X_LINEAR_Y);

        Graphics2D g = null; // Replace with a mock or real graphics object
        chart.render(g);

        // Add asserts here to check the rendering of title, legend, coordinate system, and chart renderers
    }

    @Test
    public void testRowColorModel() {
        ChartDataModel model = new ChartDataModel();
        model.addSeries(new ModelDataSeries("Series1", new double[][]{new double[]{0, 1, 2, 3}, new double[]{1, 2, 3, 4}}));
        model.addSeries(new ModelDataSeries("Series2", new double[][]{new double[]{0, 1, 2, 3}, new double[]{4, 5, 6, 7}}));
        DefaultChart chart = new DefaultChart(model, "My Chart", DefaultChart.LINEAR_X_LINEAR_Y);

        RowColorModel rcm = chart.getRowColorModel();
        assertNotNull(rcm);
        assertEquals(2, rcm.getNumberOfSeries());

        // Add asserts here to check the colors of each series
    }

    @Test
    public void testLegend() {
        ChartDataModel model = new ChartDataModel();
        model.addSeries(new ModelDataSeries("Series1", new double[][]{new double[]{0, 1, 2, 3}, new double[]{1, 2, 3, 4}}));
        model.addSeries(new ModelDataSeries("Series2", new double[][]{new double[]{0, 1, 2, 3}, new double[]{4, 5, 6, 7}}));
        DefaultChart chart = new DefaultChart(model, "My Chart", DefaultChart.LINEAR_X_LINEAR_Y);

        Legend legend = chart.getLegend();
        assertNotNull(legend);
        assertEquals(2, legend.getNumberOfSeries());

        // Add asserts here to check the labels and positions of each series in the legend
    }

    @Test
    public void testTitle() {
        ChartDataModel model = new ChartDataModel();
        model.addSeries(new ModelDataSeries("Series1", new double[][]{new double[]{0, 1, 2, 3}, new double[]{1, 2, 3, 4}}));
        model.addSeries(new ModelDataSeries("Series2", new double[][]{new double[]{0, 1, 2, 3}, new double[]{4, 5, 6, 7}}));
        DefaultChart chart = new DefaultChart(model, "My Chart", DefaultChart.LINEAR_X_LINEAR_Y);

        Title title = chart.getTitle();
        assertNotNull(title);
        assertEquals("My Chart", title.getText());

        // Add asserts here to check the position and size of the title
    }

    @Test
    public void testChartDataModel() {
        ChartDataModel model = new ChartDataModel();
        model.addSeries(new ModelDataSeries("Series1", new double[][]{new double[]{0, 1, 2, 3}, new double[]{1, 2, 3, 4}}));
        model.addSeries(new ModelDataSeries("Series2", new double[][]{new double[]{0, 1, 2, 3}, new double[]{4, 5, 6, 7}}));
        DefaultChart chart = new DefaultChart(model, "My Chart", DefaultChart.LINEAR_X_LINEAR_Y);

        ChartDataModel chartModel = chart.getChartDataModel();
        assertSame(model, chartModel);
    }

    @Test
    public void testChartRenderer() {
        ChartDataModel model = new ChartDataModel();
        model.addSeries(new ModelDataSeries("Series1", new double[][]{new double[]{0, 1, 2, 3}, new double[]{1, 2, 3, 4}}));
        model.addSeries(new ModelDataSeries("Series2", new double[][]{new double[]{0, 1, 2, 3}, new double[]{4, 5, 6, 7}}));
        DefaultChart chart = new DefaultChart(model, "My Chart", DefaultChart.LINEAR_X_LINEAR_Y);

        Map<String, AbstractChartRenderer> chartRenderers = chart.getChartRenderer();
        assertNotNull(chartRenderers);
        assertEquals(2, chartRenderers.size());

        // Add asserts here to check the instances of each chart renderer
    }

    @Test
    public void testGetPreferredSize() {
        ChartDataModel model = new ChartDataModel();
        model.addSeries(new ModelDataSeries("Series1", new double[][]{new double[]{0, 1, 2, 3}, new double[]{1, 2, 3, 4}}));
        model.addSeries(new ModelDataSeries("Series2", new double[][]{new double[]{0, 1, 2, 3}, new double[]{4, 5, 6, 7}}));
        DefaultChart chart = new DefaultChart(model, "My Chart", DefaultChart.LINEAR_X_LINEAR_Y);

        Dimension preferredSize = chart.getPreferredSize();
        assertNotNull(preferredSize);

        // Add asserts here to check the width and height of the preferred size
    }
}