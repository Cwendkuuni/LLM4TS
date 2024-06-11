package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class DefaultChartTest {

    @Test
    public void testDefaultChartConstruction() {
        ChartDataModel mockModel = mock(ChartDataModel.class);
        DefaultChart chart = new DefaultChart(mockModel, "Test Chart Title");
        assertNotNull(chart);
        assertEquals(mockModel, chart.getChartDataModel());
        assertNull(chart.getTitle());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testDefaultChartConstructionWithTitle() {
        ChartDataModel mockModel = mock(ChartDataModel.class);
        Title mockTitle = mock(Title.class);
        when(mockTitle.getPreferredSize()).thenReturn(new Dimension(100, 50));
        DefaultChart chart = new DefaultChart(mockModel, "Test Chart Title", mockTitle);
        assertNotNull(chart);
        assertEquals(mockModel, chart.getChartDataModel());
        assertEquals(mockTitle, chart.getTitle());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testDefaultChartConstructionWithTitleAndLinearXLinearY() {
        ChartDataModel mockModel = mock(ChartDataModel.class);
        Title mockTitle = mock(Title.class);
        when(mockTitle.getPreferredSize()).thenReturn(new Dimension(100, 50));
        CoordSystem mockCoordSystem = mock(CoordSystem.class);
        DefaultChart chart = new DefaultChart(mockModel, "Test Chart Title", mockTitle, DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart);
        assertEquals(mockModel, chart.getChartDataModel());
        assertEquals(mockTitle, chart.getTitle());
        assertEquals(mockCoordSystem, chart.getCoordSystem());
        assertNull(chart.getLegend());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testDefaultChartConstructionWithTitleAndLinearXLinearYAndXaxisAndYaxis() {
        ChartDataModel mockModel = mock(ChartDataModel.class);
        Title mockTitle = mock(Title.class);
        when(mockTitle.getPreferredSize()).thenReturn(new Dimension(100, 50));
        CoordSystem mockCoordSystem = mock(CoordSystem.class);
        when(mockCoordSystem.getXAxisUnit()).thenReturn("XaxisUnit");
        when(mockCoordSystem.getYAxisUnit()).thenReturn("YaxisUnit");
        DefaultChart chart = new DefaultChart(mockModel, "Test Chart Title", mockTitle, DefaultChart.LINEAR_X_LINEAR_Y, "XaxisUnit", "YaxisUnit");
        assertNotNull(chart);
        assertEquals(mockModel, chart.getChartDataModel());
        assertEquals(mockTitle, chart.getTitle());
        assertEquals(mockCoordSystem, chart.getCoordSystem());
        assertNull(chart.getLegend());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testGetPreferredSize() {
        ChartDataModel mockModel = mock(ChartDataModel.class);
        Title mockTitle = mock(Title.class);
        when(mockTitle.getPreferredSize()).thenReturn(new Dimension(100, 50));
        Legend mockLegend = mock(Legend.class);
        when(mockLegend.getPreferredSize()).thenReturn(new Dimension(200, 100));
        CoordSystem mockCoordSystem = mock(CoordSystem.class);
        when(mockCoordSystem.getPreferredSize()).thenReturn(new Dimension(300, 400));
        List<AbstractChartRenderer> mockRenderers = Arrays.asList(mock(AbstractChartRenderer.class));
        DefaultChart chart = new DefaultChart(mockModel, "Test Chart Title", mockTitle, DefaultChart.LINEAR_X_LINEAR_Y, "XaxisUnit", "YaxisUnit");
        chart.setChartRenderer(new HashMap<String, AbstractChartRenderer>(Collections.singletonMap("RendererKey", mockRenderers.get(0))));
        when(mockRenderers.get(0).getPreferredSize()).thenReturn(new Dimension(400, 500));

        Dimension size = chart.getPreferredSize();
        assertTrue(size.getWidth() >= 801);
        assertTrue(size.getHeight() >= 1051);
    }

    @Test
    public void testRender() {
        ChartDataModel mockModel = mock(ChartDataModel.class);
        Title mockTitle = mock(Title.class);
        when(mockTitle.getPreferredSize()).thenReturn(new Dimension(100, 50));
        Legend mockLegend = mock(Legend.class);
        when(mockLegend.getPreferredSize()).thenReturn(new Dimension(200, 100));
        CoordSystem mockCoordSystem = mock(CoordSystem.class);
        when(mockCoordSystem.getXAxisUnit()).thenReturn("XaxisUnit");
        when(mockCoordSystem.getYAxisUnit()).thenReturn("YaxisUnit");
        when(mockCoordSystem.getBounds()).thenReturn(new Rectangle(0, 0, 300, 400));
        List<AbstractChartRenderer> mockRenderers = Arrays.asList(mock(AbstractChartRenderer.class));
        DefaultChart chart = new DefaultChart(mockModel, "Test Chart Title", mockTitle, DefaultChart.LINEAR_X_LINEAR_Y, "XaxisUnit", "YaxisUnit");
        chart.setChartRenderer(new HashMap<String, AbstractChartRenderer>(Collections.singletonMap("RendererKey", mockRenderers.get(0))));
        when(mockRenderers.get(0).getBounds()).thenReturn(new Rectangle(0, 50, 400, 500));

        Graphics2D mockGraphics = mock(Graphics2D.class);
        ChartRenderer mockChartRenderer = mock(ChartRenderer.class);
        when(mockChartRenderer.getBounds()).thenReturn(new Rectangle(0, 50, 400, 500));
        when(mockChartRenderer.render(Mockito.any(Graphics2D.class))).thenAnswer(i -> {});
        chart.setChartRenderer(mockChartRenderer);

        chart.render(mockGraphics);

        verify(mockTitle, times(1)).render(mockGraphics);
        verify(mockLegend, times(1)).render(mockGraphics);
        verify(mockCoordSystem, times(1)).render(mockGraphics);
        verify(mockChartRenderer, times(1)).render(mockGraphics);
    }
}