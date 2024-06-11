package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;

import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

    @Test
    public void testDefaultChartConstructionEmpty() {
        DefaultChart chart = new DefaultChart();
        assertNull(chart.getTitle());
        assertNull(chart.getLegend());
        assertNull(chart.getChartDataModel());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testDefaultChartConstructionWithTitle() {
        String title = "Test Chart";
        DefaultChart chart = new DefaultChart(new ChartDataModel(), title);
        assertNotNull(chart.getTitle());
        assertEquals(title, chart.getTitle().getText());
        assertNull(chart.getLegend());
        assertNotNull(chart.getChartDataModel());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testDefaultChartConstructionWithTitleAndCoordSystem() {
        String title = "Test Chart";
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, title, DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getTitle());
        assertEquals(title, chart.getTitle().getText());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getCoordSystem());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testDefaultChartConstructionWithTitleAndCoordSystemAndAxes() {
        String title = "Test Chart";
        ChartDataModel model = new ChartDataModel();
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        DefaultChart chart = new DefaultChart(model, title, DefaultChart.LINEAR_X_LINEAR_Y, xaxis, yaxis);
        assertNotNull(chart.getTitle());
        assertEquals(title, chart.getTitle().getText());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getCoordSystem());
        CoordSystem coordSystem = chart.getCoordSystem();
        assertNotNull(coordSystem.getXAxisUnit());
        assertEquals(xaxis, coordSystem.getXAxisUnit().getText());
        assertNotNull(coordSystem.getYAxisUnit());
        assertEquals(yaxis, coordSystem.getYAxisUnit().getText());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testGetPreferredSizeReturnsNull() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart");
        assertEquals(0, chart.getPreferredSize().width);
        assertEquals(0, chart.getPreferredSize().height);
    }

    @Test
    public void testRenderTitle() {
        String title = "Test Chart";
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, title);
        Title titleComponent = chart.getTitle();
        String expectedTitle = "Test Chart";

        ChartRenderer mockRenderer = mock(ChartRenderer.class);
        when(mockRenderer.getBounds()).thenReturn(new Rectangle(0, 0, 100, 20));
        chart.setChartRenderer(Collections.singletonMap("Test Renderer", mockRenderer));

        Point expectedTitlePosition = new Point(chart.getWidth() / 2, 0);
        titleComponent.setBounds(expectedTitlePosition);

        Graphics2D graphicsMock = mock(Graphics2D.class);
        when(graphicsMock.getColor()).thenReturn(Color.WHITE);
        chart.render(graphicsMock);

        verify(graphicsMock).fillRect(0, 0, chart.getWidth(), chart.getHeight());
        verify(graphicsMock).setColor(Color.BLACK);
        verify(titleComponent).render(graphicsMock);
    }

    @Test
    public void testRenderLegend() {
        String title = "Test Chart";
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, title);
        Legend legendComponent = chart.getLegend();
        String expectedLegendText = "Test Legend";

        ChartRenderer mockRenderer = mock(ChartRenderer.class);
        when(mockRenderer.getBounds()).thenReturn(new Rectangle(0, 0, 100, 20));
        chart.setChartRenderer(Collections.singletonMap("Test Renderer", mockRenderer));

        Point expectedLegendPosition = new Point(chart.getWidth() - 100, chart.getHeight() / 2);
        legendComponent.setBounds(expectedLegendPosition);

        Graphics2D graphicsMock = mock(Graphics2D.class);
        when(graphicsMock.getColor()).thenReturn(Color.WHITE);
        chart.render(graphicsMock);

        verify(graphicsMock).fillRect(0, 0, chart.getWidth(), chart.getHeight());
        verify(graphicsMock).setColor(Color.BLACK);
        verify(legendComponent, never()).render(graphicsMock);

        // Set legend text
        legendComponent.setItems(new LegendItem[0]);
        legendComponent.addItem(new LegendItem("Test Legend", new Color(0, 0, 0)));

        verify(legendComponent).setItems(new LegendItem[1]);
        verify(legendComponent).addItem(argThat(l -> l.getText().equals(expectedLegendText)));

        verify(graphicsMock).setColor(Color.BLACK);
        verify(legendComponent).render(graphicsMock);
    }

    @Test
    public void testRenderChartRenderer() {
        String title = "Test Chart";
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, title);
        AbstractChartRenderer chartRendererMock = mock(AbstractChartRenderer.class);
        ChartRenderer chartRendererMap = new ChartRendererMap();
        chartRendererMap.put("Test Renderer", chartRendererMock);
        chart.setChartRenderer(chartRendererMap);

        Point expectedRendererPosition = new Point(0, chart.getHeight() / 2 - 5);
        chartRendererMock.setBounds(expectedRendererPosition);

        Graphics2D graphicsMock = mock(Graphics2D.class);
        when(graphicsMock.getColor()).thenReturn(Color.WHITE);
        chart.render(graphicsMock);

        verify(graphicsMock).fillRect(0, 0, chart.getWidth(), chart.getHeight());
        verify(graphicsMock).setColor(Color.BLACK);
        verify(chartRendererMock).render(graphicsMock);
    }

    @Test
    public void testRenderCoordinateSystem() {
        String title = "Test Chart";
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, title);
        CoordSystem coordSystem = new CoordSystem(model);
        chart.setCoordSystem(coordSystem);

        Point expectedCoordSystemPosition = new Point(0, chart.getHeight() / 2);
        coordSystem.setBounds(expectedCoordSystemPosition);

        Graphics2D graphicsMock = mock(Graphics2D.class);
        when(graphicsMock.getColor()).thenReturn(Color.WHITE);
        chart.render(graphicsMock);

        verify(graphicsMock).fillRect(0, 0, chart.getWidth(), chart.getHeight());
        verify(graphicsMock).setColor(Color.BLACK);
        verify(coordSystem).render(graphicsMock);
    }
}