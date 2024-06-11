package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart");
        assertNotNull(chart.getTitle());
        assertEquals("Test Chart", chart.getTitle().getText());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertEquals(0, chart.getRowColorModel().size());
        assertFalse(chart.hasCoordSystem());
    }

    @Test
    public void testDefaultChartWithTitle() {
        ChartDataModel model = new ChartDataModel();
        ModelDataSeries series1 = new ModelDataSeries("Series 1", Arrays.asList(1.0, 2.0, 3.0));
        ModelDataSeries series2 = new ModelDataSeries("Series 2", Arrays.asList(4.0, 5.0, 6.0));
        model.addSeries(series1);
        model.addSeries(series2);

        DefaultChart chart = new DefaultChart(model, "Test Chart");

        assertNotNull(chart.getTitle());
        assertEquals("Test Chart", chart.getTitle().getText());
        assertNotNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertEquals(2, chart.getRowColorModel().size());
        assertTrue(chart.hasSeries());
        assertFalse(chart.hasCoordSystem());
    }

    @Test
    public void testDefaultChartWithTitleAndCoordinateSystem() {
        ChartDataModel model = new ChartDataModel();
        ModelDataSeries series1 = new ModelDataSeries("Series 1", Arrays.asList(1.0, 2.0, 3.0));
        ModelDataSeries series2 = new ModelDataSeries("Series 2", Arrays.asList(4.0, 5.0, 6.0));
        model.addSeries(series1);
        model.addSeries(series2);

        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);

        assertNotNull(chart.getTitle());
        assertEquals("Test Chart", chart.getTitle().getText());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getCoordSystem());
        assertEquals(2, chart.getRowColorModel().size());
        assertTrue(chart.hasSeries());
        assertTrue(chart.hasCoordSystem());
    }

    @Test
    public void testDefaultChartWithTitleAndCoordinateSystemAndAxes() {
        ChartDataModel model = new ChartDataModel();
        ModelDataSeries series1 = new ModelDataSeries("Series 1", Arrays.asList(1.0, 2.0, 3.0));
        ModelDataSeries series2 = new ModelDataSeries("Series 2", Arrays.asList(4.0, 5.0, 6.0));
        model.addSeries(series1);
        model.addSeries(series2);

        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");

        assertNotNull(chart.getTitle());
        assertEquals("Test Chart", chart.getTitle().getText());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getCoordSystem());
        assertEquals("X-Axis", chart.getCoordSystem().getXAxisUnit());
        assertEquals("Y-Axis", chart.getCoordSystem().getYAxisUnit());
        assertEquals(2, chart.getRowColorModel().size());
        assertTrue(chart.hasSeries());
        assertTrue(chart.hasCoordSystem());
    }

    @Test
    public void testGetPreferredSize() {
        ChartDataModel model = new ChartDataModel();
        ModelDataSeries series1 = new ModelDataSeries("Series 1", Arrays.asList(1.0, 2.0, 3.0));
        ModelDataSeries series2 = new ModelDataSeries("Series 2", Arrays.asList(4.0, 5.0, 6.0));
        model.addSeries(series1);
        model.addSeries(series2);

        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");

        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRender() {
        ChartDataModel model = new ChartDataModel();
        ModelDataSeries series1 = new ModelDataSeries("Series 1", Arrays.asList(1.0, 2.0, 3.0));
        ModelDataSeries series2 = new ModelDataSeries("Series 2", Arrays.asList(4.0, 5.0, 6.0));
        model.addSeries(series1);
        model.addSeries(series2);

        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");

        Graphics2D g = createMockGraphics2D();
        chart.render(g);

        // TODO: Add assertions to verify rendering of title, legend, coordinate system, and chart renderers
    }

    private Graphics2D createMockGraphics2D() {
        Graphics2D g = mock(Graphics2D.class);
        when(g.getColor()).thenReturn(Color.WHITE);
        when(g.getFont()).thenReturn(mock(Font.class));
        return g;
    }

    @Test
    public void testSetCoordSystem() {
        ChartDataModel model = new ChartDataModel();
        ModelDataSeries series1 = new ModelDataSeries("Series 1", Arrays.asList(1.0, 2.0, 3.0));
        ModelDataSeries series2 = new ModelDataSeries("Series 2", Arrays.asList(4.0, 5.0, 6.0));
        model.addSeries(series1);
        model.addSeries(series2);

        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");

        CoordSystem coordSystem = mock(CoordSystem.class);
        chart.setCoordSystem(coordSystem);

        verify(chart).getCoordSystem();
        verify(coordSystem).setBounds(any(Rectangle2D.class));
    }

    @Test
    public void testSetLegend() {
        ChartDataModel model = new ChartDataModel();
        ModelDataSeries series1 = new ModelDataSeries("Series 1", Arrays.asList(1.0, 2.0, 3.0));
        ModelDataSeries series2 = new ModelDataSeries("Series 2", Arrays.asList(4.0, 5.0, 6.0));
        model.addSeries(series1);
        model.addSeries(series2);

        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");

        Legend legend = mock(Legend.class);
        chart.setLegend(legend);

        verify(chart).getLegend();
        verify(legend).setBounds(any(Rectangle2D.class));
    }

    @Test
    public void testSetRowColorModel() {
        ChartDataModel model = new ChartDataModel();
        ModelDataSeries series1 = new ModelDataSeries("Series 1", Arrays.asList(1.0, 2.0, 3.0));
        ModelDataSeries series2 = new ModelDataSeries("Series 2", Arrays.asList(4.0, 5.0, 6.0));
        model.addSeries(series1);
        model.addSeries(series2);

        RowColorModel rowColorModel = new RowColorModel(model);
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");

        chart.setRowColorModel(rowColorModel);

        verify(chart).getRowColorModel();
    }

    @Test
    public void testSetTitle() {
        ChartDataModel model = new ChartDataModel();
        ModelDataSeries series1 = new ModelDataSeries("Series 1", Arrays.asList(1.0, 2.0, 3.0));
        ModelDataSeries series2 = new ModelDataSeries("Series 2", Arrays.asList(4.0, 5.0, 6.0));
        model.addSeries(series1);
        model.addSeries(series2);

        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");

        Title title = mock(Title.class);
        chart.setTitle(title);

        verify(chart).getTitle();
        verify(title).setBounds(any(Rectangle2D.class));
    }

    @Test
    public void testHasCoordSystem() {
        ChartDataModel model = new ChartDataModel();
        ModelDataSeries series1 = new ModelDataSeries("Series 1", Arrays.asList(1.0, 2.0, 3.0));
        ModelDataSeries series2 = new ModelDataSeries("Series 2", Arrays.asList(4.0, 5.0, 6.0));
        model.addSeries(series1);
        model.addSeries(series2);

        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");

        assertTrue(chart.hasCoordSystem());

        DefaultChart chartWithoutCoordSystem = new DefaultChart(model, "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM);

        assertFalse(chartWithoutCoordSystem.hasCoordSystem());
    }

    @Test
    public void testHasSeries() {
        ChartDataModel model = new ChartDataModel();
        ModelDataSeries series1 = new ModelDataSeries("Series 1", Arrays.asList(1.0, 2.0, 3.0));
        ModelDataSeries series2 = new ModelDataSeries("Series 2", Arrays.asList(4.0, 5.0, 6.0));
        model.addSeries(series1);
        model.addSeries(series2);

        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");

        assertTrue(chart.hasSeries());

        DefaultChart chartWithoutSeries = new DefaultChart(new ChartDataModel());

        assertFalse(chartWithoutSeries.hasSeries());
    }

    @Test
    public void testGetChartRenderer() {
        ChartDataModel model = new ChartDataModel();
        ModelDataSeries series1 = new ModelDataSeries("Series 1", Arrays.asList(1.0, 2.0, 3.0));
        ModelDataSeries series2 = new ModelDataSeries("Series 2", Arrays.asList(4.0, 5.0, 6.0));
        model.addSeries(series1);
        model.addSeries(series2);

        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");

        Set<AbstractChartRenderer> chartRenderers = new HashSet<>();
        chartRenderers.add(mock(AbstractChartRenderer.class));
        chartRenderers.add(mock(AbstractChartRenderer.class));
        chart.setChartRenderer(chartRenderers);

        assertEquals(chartRenderers, chart.getChartRenderer());
    }
}