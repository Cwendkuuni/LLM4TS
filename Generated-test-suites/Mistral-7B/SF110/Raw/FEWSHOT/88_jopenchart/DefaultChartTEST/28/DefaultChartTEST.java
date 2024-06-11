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
import de.progra.charting.ModelData;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {

    @Test
    public void testDefaultChartConstruction() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(new ModelData()), "TestChart");
        assertNotNull(chart.getTitle());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getLegend());
    }

    @Test
    public void testDefaultChartConstructionWithTitleCoordinateSystem() {
        ChartDataModel model = new ChartDataModel(new ModelData());
        DefaultChart chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testDefaultChartConstructionWithTitleCoordinateSystemAndLegend() {
        ChartDataModel model = new ChartDataModel(new ModelData());
        DefaultChart chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y, "XAxis", "YAxis");
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getLegend());
    }

    @Test
    public void testGetPreferredSizeReturnsNull() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(new ModelData()), "TestChart");
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRenderTitleAndLegend() {
        ChartDataModel model = new ChartDataModel(new ModelData());
        Title title = new Title("TestChartTitle");
        Legend legend = new Legend(new RowColorModel(model));

        DefaultChart chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.setTitle(title);
        chart.setLegend(legend);

        Graphics2D g = createMockGraphics2D();
        chart.render(g);

        assertEquals(Color.WHITE.getRGB(), g.getColor().getRGB());
        g.setColor(Color.BLACK);
        title.render(g);
        legend.render(g);

        Rectangle2D titleBounds = title.getBounds();
        Rectangle2D legendBounds = legend.getBounds();

        assertEquals(titleBounds.getX(), chart.getTitle().getX());
        assertEquals(titleBounds.getY(), chart.getTitle().getY());
        assertEquals(titleBounds.getWidth(), title.getWidth());
        assertEquals(titleBounds.getHeight(), title.getHeight());

        assertEquals(legendBounds.getX(), chart.getLegend().getX());
        assertEquals(legendBounds.getY(), chart.getLegend().getY());
        assertEquals(legendBounds.getWidth(), legend.getWidth());
        assertEquals(legendBounds.getHeight(), legend.getHeight());
    }

    @Test
    public void testRenderChartRenderer() {
        ChartDataModel model = new ChartDataModel(new ModelData());
        DefaultChart chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);

        Graphics2D g = createMockGraphics2D();
        AbstractChartRenderer chartRenderer = createMockChartRenderer();
        chart.setChartRenderer(Collections.singletonMap("TestRenderer", chartRenderer));

        chart.render(g);
        chartRenderer.render(g);
    }

    @Test
    public void testRenderCoordinateSystem() {
        ChartDataModel model = new ChartDataModel(new ModelData());
        CoordSystem coordSystem = new CoordSystem(model);
        DefaultChart chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.setCoordSystem(coordSystem);

        Graphics2D g = createMockGraphics2D();
        chart.render(g);
        coordSystem.render(g);
    }

    private Graphics2D createMockGraphics2D() {
        return mock(Graphics2D.class);
    }

    private AbstractChartRenderer createMockChartRenderer() {
        return mock(AbstractChartRenderer.class);
    }
}