package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
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

    private DefaultChart chart;
    private ChartDataModel model;
    private CoordSystem coordSystem;
    private List<AbstractChartRenderer> renderers;

    @Before
    public void setup() {
        model = new ChartDataModel();
        model.addSeries("Series1", new ModelData[] { new ModelData(0, 1), new ModelData(1, 2), new ModelData(2, 3), new ModelData(3, 4) });
        model.addSeries("Series2", new ModelData[] { new ModelData(0, 5), new ModelData(1, 6), new ModelData(2, 7), new ModelData(3, 8) });

        coordSystem = new CoordSystem(model);
        renderers = new ArrayList<>();
        renderers.add(new LineChartRenderer());
        renderers.add(new BarChartRenderer());

        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.setChartRenderer(new ChartRendererMap(renderers.stream().collect(Collectors.toMap(AbstractChartRenderer::getName, renderer -> renderer))));
    }

    @Test
    public void testDefaultChartConstruction() {
        assertNotNull(chart);
        assertEquals("Test Chart", chart.getTitle().getText());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
    }

    @Test
    public void testChartConstructionWithTitle() {
        DefaultChart chart = new DefaultChart(model, "Test Chart");
        assertNotNull(chart);
        assertEquals("Test Chart", chart.getTitle().getText());
        assertNull(chart.getLegend());
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
    public void testChartConstructionWithTitleAndCoordinateSystemAndXAxisUnitAndYAxisUnit() {
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "XUnit", "YUnit");
        assertNotNull(chart);
        assertEquals("Test Chart", chart.getTitle().getText());
        assertNotNull(chart.getCoordSystem());
        assertEquals("XUnit", chart.getCoordSystem().getXAxisUnit());
        assertEquals("YUnit", chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRenderTitle() {
        Title title = chart.getTitle();
        String titleText = "Test Title";
        title.setText(titleText);
        title.setBounds(new Rectangle2D.Double(10, 10, 100, 30));

        Graphics2D g = createGraphics2D();
        chart.render(g);

        Rectangle2D titleBounds = title.getBounds();
        assertEquals(titleText, titleBounds.getUserData());
        assertEquals(10, titleBounds.getX());
        assertEquals(10, titleBounds.getY());
        assertEquals(100, titleBounds.getWidth());
        assertEquals(30, titleBounds.getHeight());
    }

    @Test
    public void testRenderLegend() {
        Legend legend = chart.getLegend();
        String legendText = "Test Legend";
        legend.setText(legendText);
        legend.setBounds(new Rectangle2D.Double(150, 50, 100, 100));

        Graphics2D g = createGraphics2D();
        chart.render(g);

        Rectangle2D legendBounds = legend.getBounds();
        assertEquals(legendText, legendBounds.getUserData());
        assertEquals(150, legendBounds.getX());
        assertEquals(50, legendBounds.getY());
        assertEquals(100, legendBounds.getWidth());
        assertEquals(100, legendBounds.getHeight());
    }

    @Test
    public void testRenderCoordinateSystem() {
        Graphics2D g = createGraphics2D();
        chart.render(g);

        CoordSystem coordSystem = chart.getCoordSystem();
        assertNotNull(coordSystem);
        assertTrue(coordSystem.getBounds().getWidth() > 0);
        assertTrue(coordSystem.getBounds().getHeight() > 0);
    }

    @Test
    public void testRenderChartRenderer() {
        Graphics2D g = createGraphics2D();
        chart.render(g);

        for (AbstractChartRenderer renderer : renderers) {
            assertTrue(renderer.getBounds().getWidth() > 0);
            assertTrue(renderer.getBounds().getHeight() > 0);
        }
    }

    private Graphics2D createGraphics2D() {
        return new StubGraphics2D();
    }
}

class StubGraphics2D extends Graphics2D {
    @Override
    public void setColor(Color c) {
        // Empty implementation
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        // Empty implementation
    }

    @Override
    public void setColor(Color c) {
        // Empty implementation
    }
}

class ChartRendererMap {
    private final Map<String, AbstractChartRenderer> renderers;

    public ChartRendererMap(Map<String, AbstractChartRenderer> renderers) {
        this.renderers = renderers;
    }

    public AbstractChartRenderer getChartRenderer(String name) {
        return renderers.get(name);
    }
}