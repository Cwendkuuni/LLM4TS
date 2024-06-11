package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest
{
    private DefaultChart chart;
    private ChartDataModel model;

    @Before
    public void setUp()
    {
        model = new ChartDataModel();
        model.setTitle("Test Chart");
        model.setXAxis("Test X-Axis");
        model.setYAxis("Test Y-Axis");
        chart = new DefaultChart(model, "Test Chart");
    }

    @Test
    public void testDefaultChartConstruction()
    {
        assertNotNull(chart);
    }

    @Test
    public void testChartConstructionWithTitle()
    {
        DefaultChart chart = new DefaultChart(model, "Test Chart");
        assertEquals("Test Chart", chart.getTitle().getText());
        assertNull(chart.getCoordSystem());
    }

    @Test
    public void testChartConstructionWithTitleAndCoordinateSystem()
    {
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testChartConstructionWithTitleAndCoordinateSystemAndAxes()
    {
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "Test X-Axis", "Test Y-Axis");
        assertNotNull(chart.getCoordSystem());
        assertEquals("Test X-Axis", chart.getCoordSystem().getXAxisUnit());
        assertEquals("Test Y-Axis", chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSizeReturnsNull()
    {
        assertNull(chart.getPreferredSize());
    }

    @Test(expected = NullPointerException.class)
    public void testRenderWithNullGraphics()
    {
        chart.render(null);
    }

    @Test
    public void testRenderWithEmptyRenderer()
    {
        chart.setChartRenderer(new HashMap<Series, AbstractChartRenderer>());
        Graphics2D g = new MockGraphics2D();
        chart.render(g);
    }

    @Test
    public void testRenderWithSingleRenderer()
    {
        LineChartRenderer renderer = new LineChartRenderer();
        Series series = new Series();
        Map<Series, AbstractChartRenderer> renderers = new HashMap<>();
        renderers.put(series, renderer);
        chart.setChartRenderer(renderers);

        Graphics2D g = new MockGraphics2D();
        chart.render(g);

        assertTrue(renderer.isRendered);
        assertEquals(0,renderer.getX(), g.getTransform().getTranslateX());
        assertEquals(0,renderer.getY(), g.getTransform().getTranslateY());
        assertEquals(chart.getBounds().getWidth(), renderer.getWidth(), 0.1);
        assertEquals(chart.getBounds().getHeight() - 5, renderer.getHeight(), 0.1);
    }

    @Test
    public void testRenderWithMultipleRenderers()
    {
        LineChartRenderer renderer1 = new LineChartRenderer();
        LineChartRenderer renderer2 = new LineChartRenderer();
        Series series1 = new Series();
        Series series2 = new Series();
        Map<Series, AbstractChartRenderer> renderers = new HashMap<>();
        renderers.put(series1, renderer1);
        renderers.put(series2, renderer2);
        chart.setChartRenderer(renderers);

        Graphics2D g = new MockGraphics2D();
        chart.render(g);

        assertTrue(renderer1.isRendered);
        assertTrue(renderer2.isRendered);

        assertEquals(0,renderer1.getX(), g.getTransform().getTranslateX());
        assertEquals(0,renderer1.getY(), g.getTransform().getTranslateY());
        assertEquals(chart.getBounds().getWidth()/2, renderer1.getWidth(), 0.1);
        assertEquals(chart.getBounds().getHeight()/2 - 2.5, renderer1.getHeight(), 0.1);

        assertEquals(chart.getBounds().getWidth()/2, renderer2.getX(), g.getTransform().getTranslateX());
        assertEquals(chart.getBounds().getHeight()/2 - 2.5, renderer2.getY(), g.getTransform().getTranslateY());
        assertEquals(chart.getBounds().getWidth()/2, renderer2.getWidth(), 0.1);
        assertEquals(chart.getBounds().getHeight()/2 - 2.5, renderer2.getHeight(), 0.1);
    }

    @Test
    public void testRenderWithTitleAndLegend()
    {
        LineChartRenderer renderer = new LineChartRenderer();
        Series series = new Series();
        Map<Series, AbstractChartRenderer> renderers = new HashMap<>();
        renderers.put(series, renderer);
        chart.setChartRenderer(renderers);

        Title title = new Title("Test Title");
        Legend legend = new Legend(new RowColorModel(model));
        chart.setTitle(title);
        chart.setLegend(legend);

        Graphics2D g = new MockGraphics2D();
        chart.render(g);

        title.setBounds(new Rectangle(10, 10, 100, 30));
        legend.setBounds(new Rectangle(100, 10, 100, 30));
        assertTrue(renderer.isRendered);
        assertTrue(title.isRendered);
        assertTrue(legend.isRendered);
    }

    @Test
    public void testRenderWithTitleAndLegendAndCoordinateSystem()
    {
        LineChartRenderer renderer = new LineChartRenderer();
        Series series = new Series();
        Map<Series, AbstractChartRenderer> renderers = new HashMap<>();
        renderers.put(series, renderer);
        chart.setChartRenderer(renderers);

        Title title = new Title("Test Title");
        Legend legend = new Legend(new RowColorModel(model));
        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordSystem(new CoordSystem(model));

        Graphics2D g = new MockGraphics2D();
        chart.render(g);

        title.setBounds(new Rectangle(10, 10, 100, 30));
        legend.setBounds(new Rectangle(100, 10, 100, 30));
        CoordSystem coordSystem = new CoordSystem(model);
        coordSystem.setBounds(new Rectangle(0, 40, chart.getBounds().getWidth(), chart.getBounds().getHeight() - 40));
        assertTrue(renderer.isRendered);
        assertTrue(title.isRendered);
        assertTrue(legend.isRendered);
        assertTrue(coordSystem.isRendered);
    }
}

class MockGraphics2D extends Graphics2D
{
    private boolean isTransformed = false;
    private double transformX = 0;
    private double transformY = 0;

    @Override
    public void transform(AffineTransform xform)
    {
        isTransformed = true;
        transformX = xform.getTranslateX();
        transformY = xform.getTranslateY();
    }

    public double getTransformX()
    {
        return transformX;
    }

    public double getTransformY()
    {
        return transformY;
    }

    public Rectangle2D getBounds()
    {
        return new Rectangle2D.Double(0, 0, 500, 500);
    }
}