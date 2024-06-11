package de.progra.charting.test;

import static org.junit.Assert.*;
import de.progra.charting.*;
import de.progra.charting.model.*;
import java.awt.*;
import java.util.*;

public class DefaultChartTest {

    @Test
    public void testDefaultChartConstruction() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "title");
        assertNotNull(chart.getTitle());
        assertEquals("title", chart.getTitle().getText());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getLegend());
    }

    @Test
    public void testDefaultChartConstructionWithCoordinateSystem() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "title", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getTitle());
        assertEquals("title", chart.getTitle().getText());
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testDefaultChartConstructionWithCoordinateSystemAndLegend() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "title", DefaultChart.LINEAR_X_LINEAR_Y, "xaxis", "yaxis");
        assertNotNull(chart.getTitle());
        assertEquals("title", chart.getTitle().getText());
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getLegend());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultChartConstructionWithInvalidCoordinateSystem() {
        new DefaultChart(new ChartDataModel(), "title", 2);
    }

    @Test
    public void testGetPreferredSize() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "title", DefaultChart.LINEAR_X_LINEAR_Y, "xaxis", "yaxis");
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRender() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "title", DefaultChart.LINEAR_X_LINEAR_Y, "xaxis", "yaxis");
        ChartDataModel model = chart.getChartDataModel();
        AbstractChartRenderer renderer = new LineChartRenderer(model);
        chart.setChartRenderer(renderer.getClass().newInstance());

        Title title = new Title("Title");
        Legend legend = new Legend(new RowColorModel(model));
        ChartRenderer renderers = new ArrayList<>();
        renderers.add(renderer);

        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setChartRenderer(renderer);

        int width = 500;
        int height = 500;

        Graphics2D g = mock(Graphics2D.class);
        when(g.getColor()).thenReturn(Color.WHITE);

        Rectangle bound = new Rectangle(0, 0, width, height);
        when(g.getClipBounds()).thenReturn(bound);

        chart.render(g);

        verify(g, times(1)).fillRect(eq(0), eq(0), eq(width), eq(height));
        verify(g, times(1)).setColor(eq(Color.BLACK));
    }

    @Test
    public void testRenderWithTitle() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "title", DefaultChart.LINEAR_X_LINEAR_Y, "xaxis", "yaxis");
        ChartDataModel model = chart.getChartDataModel();
        AbstractChartRenderer renderer = new LineChartRenderer(model);
        chart.setChartRenderer(renderer.getClass().newInstance());

        Title title = new Title("Title");
        ChartRenderer titleRenderer = mock(ChartRenderer.class);
        title.setChartRenderer(titleRenderer);

        Legend legend = new Legend(new RowColorModel(model));
        ChartRenderer legendRenderer = mock(ChartRenderer.class);
        legend.setChartRenderer(legendRenderer);

        ChartRenderer renderers[] = {renderer, titleRenderer, legendRenderer};
        chart.setChartRenderer(renderers);

        int width = 500;
        int height = 500;

        Graphics2D g = mock(Graphics2D.class);
        when(g.getColor()).thenReturn(Color.WHITE);

        Rectangle bound = new Rectangle(0, 0, width, height);
        when(g.getClipBounds()).thenReturn(bound);

        chart.render(g);

        verify(g, times(1)).fillRect(eq(0), eq(0), eq(width), eq(height));
        verify(g, times(1)).setColor(eq(Color.BLACK));
        verify(titleRenderer, times(1)).render(g);
        verify(legendRenderer, times(1)).render(g);
    }

    @Test
    public void testRenderWithTitleAndLegendOutsideChartBounds() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "title", DefaultChart.LINEAR_X_LINEAR_Y, "xaxis", "yaxis");
        ChartDataModel model = chart.getChartDataModel();
        AbstractChartRenderer renderer = new LineChartRenderer(model);
        chart.setChartRenderer(renderer.getClass().newInstance());

        Title title = new Title("Title");
        ChartRenderer titleRenderer = mock(ChartRenderer.class);
        title.setChartRenderer(titleRenderer);

        Legend legend = new Legend(new RowColorModel(model));
        ChartRenderer legendRenderer = mock(ChartRenderer.class);
        legend.setChartRenderer(legendRenderer);

        ChartRenderer renderers[] = {renderer, titleRenderer, legendRenderer};
        chart.setChartRenderer(renderers);

        int width = 500;
        int height = 500;

        Graphics2D g = mock(Graphics2D.class);
        when(g.getColor()).thenReturn(Color.WHITE);

        Rectangle bound = new Rectangle(0, 0, width, height);
        when(g.getClipBounds()).thenReturn(bound);

        title.setBounds(new Rectangle(width + 10, height + 10, 100, 50));
        legend.setBounds(new Rectangle(width + 10, height + 110, 100, 50));

        chart.render(g);

        verify(g, times(1)).fillRect(eq(0), eq(0), eq(width), eq(height));
        verify(g, times(1)).setColor(eq(Color.BLACK));
        verifyNoMoreInteractions(titleRenderer);
        verifyNoMoreInteractions(legendRenderer);
    }
}