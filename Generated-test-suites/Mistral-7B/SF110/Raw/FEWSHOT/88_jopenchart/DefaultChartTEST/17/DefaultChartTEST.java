package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.Model;
import de.progra.charting.Render.AbstractChartRenderer;
import de.progra.charting.Title;

public class DefaultChartTest {

    @Test
    public void testDefaultChartCreation() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(new Model[0]), "test title");
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }

    @Test
    public void testDefaultChartCreationWithCoordinateSystem() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(new Model[0]), "test title", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testDefaultChartCreationWithCoordinateSystemAndAxisUnits() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(new Model[0]), "test title", DefaultChart.LINEAR_X_LINEAR_Y, "Xunit", "Yunit");
        assertNotNull(chart.getCoordSystem());
        assertEquals("Xunit", chart.getCoordSystem().getXAxisUnit());
        assertEquals("Yunit", chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSizeReturnsNull() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(new Model[0]), "test title");
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRenderMethod() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(new Model[0]), "test title");
        ChartRenderer chartRenderer = new ChartRendererMock();
        chart.setChartRenderer(new HashMap<AbstractChartRenderer, AbstractChartRenderer>() {{
            put(chartRenderer, chartRenderer);
        }});

        Graphics2D g = mock(Graphics2D.class);
        when(g.getColor()).thenReturn(Color.WHITE);

        chart.render(g);

        verify(g, times(1)).setColor(Color.WHITE);
        verify(g, times(1)).fillRect(0, 0, anyInt(), anyInt());
    }

    @Test
    public void testRenderMethodWithTitle() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(new Model[0]), "test title");
        Title title = mock(Title.class);
        chart.setTitle(title);

        ChartRenderer chartRenderer = new ChartRendererMock();
        chart.setChartRenderer(new HashMap<AbstractChartRenderer, AbstractChartRenderer>() {{
            put(chartRenderer, chartRenderer);
        }});

        Graphics2D g = mock(Graphics2D.class);
        when(g.getColor()).thenReturn(Color.WHITE);

        chart.render(g);

        verify(g, times(1)).setColor(Color.WHITE);
        verify(g, times(1)).fillRect(0, 0, anyInt(), anyInt());
        verify(title, times(1)).getPreferredSize();
        verify(title, times(1)).render(g);
    }

    @Test
    public void testRenderMethodWithLegend() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(new Model[0]), "test title");
        Legend legend = mock(Legend.class);
        chart.setLegend(legend);

        ChartRenderer chartRenderer = new ChartRendererMock();
        chart.setChartRenderer(new HashMap<AbstractChartRenderer, AbstractChartRenderer>() {{
            put(chartRenderer, chartRenderer);
        }});

        Graphics2D g = mock(Graphics2D.class);
        when(g.getColor()).thenReturn(Color.WHITE);

        chart.render(g);

        verify(g, times(1)).setColor(Color.WHITE);
        verify(g, times(1)).fillRect(0, 0, anyInt(), anyInt());
        verify(legend, times(1)).getPreferredSize();
        verify(legend, times(1)).render(g);
    }

    @Test
    public void testRenderMethodWithCoordinateSystem() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(new Model[0]), "test title");
        CoordSystem coordSystem = mock(CoordSystem.class);
        chart.setCoordSystem(coordSystem);

        ChartRenderer chartRenderer = new ChartRendererMock();
        chart.setChartRenderer(new HashMap<AbstractChartRenderer, AbstractChartRenderer>() {{
            put(chartRenderer, chartRenderer);
        }});

        Graphics2D g = mock(Graphics2D.class);
        when(g.getColor()).thenReturn(Color.WHITE);

        chart.render(g);

        verify(g, times(1)).setColor(Color.WHITE);
        verify(g, times(1)).fillRect(0, 0, anyInt(), anyInt());
        verify(coordSystem, times(1)).setBounds(any(Rectangle.class));
    }

    @Test
    public void testRenderMethodWithChartRenderer() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(new Model[0]), "test title");
        AbstractChartRenderer chartRenderer = mock(AbstractChartRenderer.class);
        chart.setChartRenderer(chartRenderer);

        Graphics2D g = mock(Graphics2D.class);
        when(g.getColor()).thenReturn(Color.WHITE);

        chart.render(g);

        verify(g, times(1)).setColor(Color.WHITE);
        verify(g, times(1)).fillRect(0, 0, anyInt(), anyInt());
        verify(chartRenderer, times(1)).setBounds(any(Rectangle.class));
        verify(chartRenderer, times(1)).render(g);
    }

    @Test
    public void testRenderMethodWithAllComponents() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(new Model[0]), "test title");
        Title title = mock(Title.class);
        chart.setTitle(title);
        Legend legend = mock(Legend.class);
        chart.setLegend(legend);
        CoordSystem coordSystem = mock(CoordSystem.class);
        chart.setCoordSystem(coordSystem);

        ChartRenderer chartRenderer = new ChartRendererMock();
        chart.setChartRenderer(new HashMap<AbstractChartRenderer, AbstractChartRenderer>() {{
            put(chartRenderer, chartRenderer);
        }});

        Graphics2D g = mock(Graphics2D.class);
        when(g.getColor()).thenReturn(Color.WHITE);

        chart.render(g);

        verify(g, times(1)).setColor(Color.WHITE);
        verify(g, times(1)).fillRect(0, 0, anyInt(), anyInt());
        verify(title, times(1)).getPreferredSize();
        verify(title, times(1)).render(g);
        verify(legend, times(1)).getPreferredSize();
        verify(legend, times(1)).render(g);
        verify(coordSystem, times(1)).setBounds(any(Rectangle.class));
        verify(chartRenderer, times(1)).setBounds(any(Rectangle.class));
        verify(chartRenderer, times(1)).render(g);
    }
}

class ChartRendererMock implements AbstractChartRenderer {
    @Override
    public void setBounds(Rectangle bounds) {
        // Empty implementation
    }

    @Override
    public void render(Graphics2D g) {
        // Empty implementation
    }
}