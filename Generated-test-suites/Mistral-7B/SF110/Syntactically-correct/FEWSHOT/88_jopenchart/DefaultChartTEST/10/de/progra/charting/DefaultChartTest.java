package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartRenderer;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelDataSeries;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;

public class DefaultChartTest {

    private DefaultChart chart;
    private ChartDataModel model;

    @Before
    public void setUp() {
        model = new ChartDataModel();
        model.addSeries(new ModelDataSeries("Series1", Arrays.asList(1d, 2d, 3d)));
        chart = new DefaultChart(model, "Test Chart");
    }

    @Test
    public void testDefaultChartConstructor() {
        assertNull(chart.getTitle());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertTrue(chart.getChartRenderer().isEmpty());
    }

    @Test
    public void testChartConstructorWithTitle() {
        DefaultChart chart = new DefaultChart(model, "Test Chart");
        Title title = chart.getTitle();
        assertEquals("Test Chart", title.getText());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertTrue(chart.getChartRenderer().isEmpty());
    }

    @Test
    public void testChartConstructorWithTitleAndCoordinateSystem() {
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        Title title = chart.getTitle();
        assertEquals("Test Chart", title.getText());
        assertNotNull(chart.getCoordSystem());
        assertTrue(chart.getChartRenderer().isEmpty());
    }

    @Test
    public void testChartConstructorWithTitleAndCoordinateSystemAndAxisLabels() {
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis Label", "Y Axis Label");
        Title title = chart.getTitle();
        assertEquals("Test Chart", title.getText());
        assertNotNull(chart.getCoordSystem());
        CoordSystem coordSystem = chart.getCoordSystem();
        assertEquals("X Axis Label", coordSystem.getXAxisUnit());
        assertEquals("Y Axis Label", coordSystem.getYAxisUnit());
        assertTrue(chart.getChartRenderer().isEmpty());
    }

    @Test
    public void testGetPreferredSize() {
        assertEquals(0, chart.getPreferredSize().getWidth());
        assertEquals(0, chart.getPreferredSize().getHeight());
    }

    @Test(expected = NullPointerException.class)
    public void testRenderNullGraphics() {
        chart.render(null);
    }

    @Test
    public void testRender() {
        Map<String, ChartRenderer> renderers = new HashMap<String, ChartRenderer>();
        renderers.put("Renderer1", new TestChartRenderer());
        chart.setChartRenderer(renderers);

        Graphics2D g = createMockGraphics2D();
        chart.render(g);

        // Perform assertions on graphics context here
    }

    private Graphics2D createMockGraphics2D() {
        // Create and return a mock Graphics2D object
    }

    private static class TestChartRenderer implements AbstractChartRenderer {

        @Override
        public void setBounds(Rectangle bounds) {
            // Implementation
        }

        @Override
        public void render(Graphics2D g) {
            // Implementation
        }
    }

    @Test
    public void testSetTitle() {
        Title title = new Title("Test Title");
        chart.setTitle(title);
        assertEquals("Test Title", chart.getTitle().getText());
    }

    @Test
    public void testSetLegend() {
        Legend legend = new Legend(new RowColorModel(model));
        chart.setLegend(legend);
        assertEquals(legend, chart.getLegend());
    }

    @Test
    public void testSetCoordinateSystem() {
        CoordSystem coordSystem = new CoordSystem(model);
        chart.setCoordSystem(coordSystem);
        assertEquals(coordSystem, chart.getCoordSystem());
    }

    @Test
    public void testAddSeriesRenderer() {
        ChartRenderer renderer = new TestChartRenderer();
        chart.getChartRenderer().put("Renderer1", renderer);
        assertTrue(chart.getChartRenderer().containsKey("Renderer1"));
        assertTrue(chart.getChartRenderer().containsValue(renderer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateSeriesRenderer() {
        ChartRenderer renderer = new TestChartRenderer();
        chart.getChartRenderer().put("Renderer1", renderer);
        chart.getChartRenderer().put("Renderer1", new TestChartRenderer());
    }
}