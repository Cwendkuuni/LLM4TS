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
import de.progra.charting.ModelConstants;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.test.model.TestChartDataModel;

public class DefaultChartTest {

    private DefaultChart chart;
    private ChartDataModel model;
    private AbstractChartRenderer renderer;

    @Before
    public void setUp() {
        model = new TestChartDataModel();
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        renderer = new TestChartRenderer();
        chart.getChartRenderer().put(TestChartDataModel.SERIES_1, renderer);
    }

    @Test
    public void testDefaultChartConstruction() {
        assertNotNull(chart);
        assertEquals("Test Chart", chart.getTitle().getText());
        assertNull(chart.getCoordSystem());
    }

    @Test
    public void testChartConstructionWithTitle() {
        DefaultChart chart = new DefaultChart(model, "Test Chart");
        assertNotNull(chart);
        assertEquals("Test Chart", chart.getTitle().getText());
        assertNull(chart.getCoordSystem());
    }

    @Test
    public void testChartConstructionWithTitleAndCoordinateSystem() {
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart);
        assertEquals("Test Chart", chart.getTitle().getText());
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testChartConstructionWithTitleAndCoordinateSystemAndAxes() {
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "xAxis", "yAxis");
        assertNotNull(chart);
        assertEquals("Test Chart", chart.getTitle().getText());
        assertNotNull(chart.getCoordSystem());
        assertEquals("xAxis", chart.getCoordSystem().getXAxisUnit());
        assertEquals("yAxis", chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSizeReturnsNull() {
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRenderWithNoTitleOrLegend() {
        Graphics2D g = createTestGraphics2D();
        chart.render(g);
        Rectangle2D bounds = chart.getBounds();
        assertEquals(bounds.getWidth(), 0);
        assertEquals(bounds.getHeight(), 0);
    }

    @Test
    public void testRenderWithTitle() {
        Title title = new Title("Test Title");
        chart.setTitle(title);
        Graphics2D g = createTestGraphics2D();
        chart.render(g);
        Rectangle2D titleBounds = title.getBounds();
        assertEquals(titleBounds.getX(), width / 2 - (int)(titleBounds.getWidth() / 2.0), 0);
        assertEquals(titleBounds.getWidth(), (int)titleBounds.getWidth());
        assertEquals(titleBounds.getHeight(), (int)titleBounds.getHeight());
    }

    @Test
    public void testRenderWithTitleAndLegend() {
        Title title = new Title("Test Title");
        Legend legend = new Legend();
        chart.setTitle(title);
        chart.setLegend(legend);
        Graphics2D g = createTestGraphics2D();
        chart.render(g);
        Rectangle2D titleBounds = title.getBounds();
        assertEquals(titleBounds.getX(), width / 2 - (int)(titleBounds.getWidth() / 2.0), 0);
        assertEquals(titleBounds.getWidth(), (int)titleBounds.getWidth());
        assertEquals(titleBounds.getHeight(), (int)titleBounds.getHeight());

        Rectangle2D legendBounds = legend.getBounds();
        assertEquals(legendBounds.getX(), width - legendBounds.getWidth(), 0);
        assertEquals(legendBounds.getWidth(), (int)legendBounds.getWidth());
        assertEquals(legendBounds.getHeight(), (int)legendBounds.getHeight());
    }

    @Test
    public void testRenderWithTitleAndLegendAndChartRenderer() {
        Title title = new Title("Test Title");
        Legend legend = new Legend();
        chart.setTitle(title);
        chart.setLegend(legend);
        Graphics2D g = createTestGraphics2D();
        chart.render(g);
        Rectangle2D titleBounds = title.getBounds();
        assertEquals(titleBounds.getX(), width / 2 - (int)(titleBounds.getWidth() / 2.0), 0);
        assertEquals(titleBounds.getWidth(), (int)titleBounds.getWidth());
        assertEquals(titleBounds.getHeight(), (int)titleBounds.getHeight());

        Rectangle2D legendBounds = legend.getBounds();
        assertEquals(legendBounds.getX(), width - legendBounds.getWidth(), 0);
        assertEquals(legendBounds.getWidth(), (int)legendBounds.getWidth());
        assertEquals(legendBounds.getHeight(), (int)legendBounds.getHeight());

        Rectangle2D rendererBounds = renderer.getBounds();
        assertEquals(rendererBounds.getX(), 0, 0);
        assertEquals(rendererBounds.getWidth(), width - legendBounds.getWidth() - titleBounds.getWidth(), 0);
        assertEquals(rendererBounds.getHeight(), height - titleBounds.getHeight() - 5, 0);
    }

    @Test
    public void testRenderWithTitleAndLegendAndChartRendererAndCoordinateSystem() {
        Title title = new Title("Test Title");
        Legend legend = new Legend();
        CoordSystem coordSystem = new CoordSystem(model);
        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordSystem(coordSystem);
        Graphics2D g = createTestGraphics2D();
        chart.render(g);
        Rectangle2D titleBounds = title.getBounds();
        assertEquals(titleBounds.getX(), width / 2 - (int)(titleBounds.getWidth() / 2.0), 0);
        assertEquals(titleBounds.getWidth(), (int)titleBounds.getWidth());
        assertEquals(titleBounds.getHeight(), (int)titleBounds.getHeight());

        Rectangle2D legendBounds = legend.getBounds();
        assertEquals(legendBounds.getX(), width - legendBounds.getWidth(), 0);
        assertEquals(legendBounds.getWidth(), (int)legendBounds.getWidth());
        assertEquals(legendBounds.getHeight(), (int)legendBounds.getHeight());

        Rectangle2D coordSystemBounds = coordSystem.getBounds();
        assertEquals(coordSystemBounds.getX(), 0, titleBounds.getHeight());
        assertEquals(coordSystemBounds.getWidth(), width - legendBounds.getWidth() - titleBounds.getWidth(), 0);
        assertEquals(coordSystemBounds.getHeight(), height - titleBounds.getHeight() - legendBounds.getHeight() - 5, 0);

        Rectangle2D rendererBounds = renderer.getBounds();
        assertEquals(rendererBounds.getX(), 0, titleBounds.getHeight() + coordSystemBounds.getHeight());
        assertEquals(rendererBounds.getWidth(), width - legendBounds.getWidth() - titleBounds.getWidth(), 0);
        assertEquals(rendererBounds.getHeight(), height - titleBounds.getHeight() - coordSystemBounds.getHeight() - legendBounds.getHeight() - 5, 0);
    }

    private Graphics2D createTestGraphics2D() {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        return gc.createGraphics();
    }
}