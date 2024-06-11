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
import de.progra.charting.Model;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.chartrenderer.AbstractChartRenderer;
import de.progra.charting.model.XYDataModel;

public class DefaultChartTest {

    private DefaultChart chart;
    private ChartDataModel model;
    private Title title;
    private Legend legend;
    private CoordSystem coordSystem;
    private List<AbstractChartRenderer> renderers;

    @Before
    public void setUp() {
        model = new XYDataModel();
        title = new Title("Test Chart Title");
        legend = new Legend(new RowColorModel(model));
        coordSystem = new CoordSystem(model);
        renderers = new ArrayList<>();
        chart = new DefaultChart(model, title, DefaultChart.LINEAR_X_LINEAR_Y, "X Axis Unit", "Y Axis Unit");
        chart.setChartRenderer(new ChartRendererMap(model, Collections.unmodifiableMap(Collections.singletonMap("RendererKey", renderers))));
    }

    @Test
    public void testDefaultChartConstruction() {
        assertNotNull(chart);
        assertEquals(DefaultChart.LINEAR_X_LINEAR_Y, chart.getCoordinateSystem());
        assertNull(chart.getTitle());
        assertNull(chart.getLegend());
    }

    @Test
    public void testChartConstructionWithTitle() {
        DefaultChart chartWithTitle = new DefaultChart(model, title);
        assertNotNull(chartWithTitle);
        assertEquals(DefaultChart.NO_COORDINATE_SYSTEM, chartWithTitle.getCoordinateSystem());
        assertEquals(title, chartWithTitle.getTitle());
        assertNull(chartWithTitle.getLegend());
    }

    @Test
    public void testChartConstructionWithTitleAndCoordinateSystem() {
        DefaultChart chartWithTitleAndCoord = new DefaultChart(model, title, DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chartWithTitleAndCoord);
        assertEquals(DefaultChart.LINEAR_X_LINEAR_Y, chartWithTitleAndCoord.getCoordinateSystem());
        assertEquals(title, chartWithTitleAndCoord.getTitle());
        assertNull(chartWithTitleAndCoord.getLegend());
    }

    @Test
    public void testChartConstructionWithTitleTitleLegendAndCoordinateSystem() {
        DefaultChart chartWithAll = new DefaultChart(model, title, DefaultChart.LINEAR_X_LINEAR_Y, "", "", "");
        assertNotNull(chartWithAll);
        assertEquals(DefaultChart.LINEAR_X_LINEAR_Y, chartWithAll.getCoordinateSystem());
        assertEquals(title, chartWithAll.getTitle());
        assertNotNull(chartWithAll.getLegend());
    }

    @Test
    public void testGetPreferredSize() {
        assertEquals(0, chart.getPreferredSize().getWidth());
        assertEquals(0, chart.getPreferredSize().getHeight());
    }

    @Test
    public void testRender() {
        Graphics2D g = createGraphics2DEmulator();
        chart.render(g);
        assertNotNull(g);
        assertEquals(Color.WHITE.getRGB(), g.getColor().getRGB());
        g.dispose();
    }

    @Test
    public void testRenderWithTitle() {
        Graphics2D g = createGraphics2DEmulator();
        chart.setTitle(title);
        chart.render(g);
        assertNotNull(g);
        assertNotNull(title.getBounds());
        assertEquals(title.getBounds().getX(), (int)(g.getClipBounds().getWidth() / 2.0 - title.getBounds().getWidth() / 2.0));
        assertEquals(title.getBounds().getY(), 0);
        assertEquals(title.getBounds().getWidth(), title.getBounds().getWidth());
        assertEquals(title.getBounds().getHeight(), title.getBounds().getHeight());
        g.dispose();
    }

    @Test
    public void testRenderWithLegend() {
        Graphics2D g = createGraphics2DEmulator();
        chart.setLegend(legend);
        chart.render(g);
        assertNotNull(g);
        assertNotNull(legend.getBounds());
        assertEquals(legend.getBounds().getX(), (int)(g.getClipBounds().getWidth() - legend.getBounds().getWidth()));
        assertEquals(legend.getBounds().getY(), g.getClipBounds().getHeight() / 2 - legend.getBounds().getHeight() / 2);
        assertEquals(legend.getBounds().getWidth(), legend.getBounds().getWidth());
        assertEquals(legend.getBounds().getHeight(), legend.getBounds().getHeight());
        g.dispose();
    }

    @Test
    public void testRenderWithTitleAndLegend() {
        Graphics2D g = createGraphics2DEmulator();
        chart.setTitle(title);
        chart.setLegend(legend);
        chart.render(g);
        assertNotNull(g);
        assertNotNull(title.getBounds());
        assertNotNull(legend.getBounds());
        assertEquals(title.getBounds().getX(), (int)(g.getClipBounds().getWidth() / 2.0 - title.getBounds().getWidth() / 2.0));
        assertEquals(title.getBounds().getY(), 0);
        assertEquals(title.getBounds().getWidth(), title.getBounds().getWidth());
        assertEquals(title.getBounds().getHeight(), title.getBounds().getHeight());
        assertEquals(legend.getBounds().getX(), (int)(g.getClipBounds().getWidth() - legend.getBounds().getWidth()));
        assertEquals(legend.getBounds().getY(), g.getClipBounds().getHeight() / 2 - legend.getBounds().getHeight() / 2);
        assertEquals(legend.getBounds().getWidth(), legend.getBounds().getWidth());
        assertEquals(legend.getBounds().getHeight(), legend.getBounds().getHeight());
        g.dispose();
    }

    @Test
    public void testRenderWithTitleLegendAndCoordinateSystem() {
        Graphics2D g = createGraphics2DEmulator();
        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordinateSystem(coordSystem);
        chart.render(g);
        assertNotNull(g);
        assertNotNull(title.getBounds());
        assertNotNull(legend.getBounds());
        assertNotNull(coordSystem.getBounds());
        assertEquals(title.getBounds().getX(), (int)(g.getClipBounds().getWidth() / 2.0 - title.getBounds().getWidth() / 2.0));
        assertEquals(title.getBounds().getY(), 0);
        assertEquals(title.getBounds().getWidth(), title.getBounds().getWidth());
        assertEquals(title.getBounds().getHeight(), title.getBounds().getHeight());
        assertEquals(legend.getBounds().getX(), (int)(g.getClipBounds().getWidth() - legend.getBounds().getWidth()));
        assertEquals(legend.getBounds().getY(), g.getClipBounds().getHeight() / 2 - legend.getBounds().getHeight() / 2);
        assertEquals(legend.getBounds().getWidth(), legend.getBounds().getWidth());
        assertEquals(legend.getBounds().getHeight(), legend.getBounds().getHeight());
        assertEquals(coordSystem.getBounds().getX(), 0);
        assertEquals(coordSystem.getBounds().getY(), title.getBounds().getHeight());
        assertEquals(coordSystem.getBounds().getWidth(), g.getClipBounds().getWidth());
        assertEquals(coordSystem.getBounds().getHeight(), g.getClipBounds().getHeight() - title.getBounds().getHeight() - legend.getBounds().getHeight());
        g.dispose();
    }

    @Test
    public void testRenderWithTitleLegendCoordinateSystemAndChartRenderers() {
        Graphics2D g = createGraphics2DEmulator();
        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordinateSystem(coordSystem);
        final AbstractChartRenderer renderer = mock(AbstractChartRenderer.class);
        renderers.add(renderer);
        chart.render(g);
        assertNotNull(g);
        verify(renderer).setBounds(eq(new Rectangle2D.Double(0, title.getBounds().getHeight(), g.getClipBounds().getWidth(), g.getClipBounds().getHeight() - title.getBounds().getHeight() - legend.getBounds().getHeight() - 5)));
        verify(renderer).render(g);
        g.dispose();
    }

    private Graphics2D createGraphics2DEmulator() {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultGraphicsConfiguration();
        return gc.createGraphics();
    }
}