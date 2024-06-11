package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

    private DefaultChart chart;
    private ChartDataModel model;
    private AbstractChartRenderer renderer;

    @Before
    public void setUp() {
        model = new ChartDataModel();
        renderer = new LineChartRenderer();
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.getChartRenderer().put(LineChartRenderer.class, renderer);
    }

    @Test
    public void testDefaultChartConstruction() {
        assertNotNull(chart);
        assertEquals(model, chart.getChartDataModel());
        assertNull(chart.getTitle());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertEquals(0, chart.getWidth(), 0.1f);
        assertEquals(0, chart.getHeight(), 0.1f);
    }

    @Test
    public void testChartConstructionWithTitle() {
        String title = "Test Chart Title";
        chart = new DefaultChart(model, title);
        Title t = chart.getTitle();
        assertNotNull(t);
        assertEquals(title, t.getText());
    }

    @Test
    public void testChartConstructionWithTitleAndCoordinates() {
        String title = "Test Chart Title";
        chart = new DefaultChart(model, title, DefaultChart.LINEAR_X_LINEAR_Y);
        Title t = chart.getTitle();
        assertNotNull(t);
        assertEquals(title, t.getText());
        CoordSystem cs = chart.getCoordSystem();
        assertNotNull(cs);
    }

    @Test
    public void testChartConstructionWithTitleAndCoordinatesAndLabels() {
        String title = "Test Chart Title";
        String xaxis = "Test X-Axis";
        String yaxis = "Test Y-Axis";
        chart = new DefaultChart(model, title, DefaultChart.LINEAR_X_LINEAR_Y, xaxis, yaxis);
        Title t = chart.getTitle();
        assertNotNull(t);
        assertEquals(title, t.getText());
        CoordSystem cs = chart.getCoordSystem();
        assertNotNull(cs);
        assertEquals(xaxis, cs.getXAxisUnit());
        assertEquals(yaxis, cs.getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        assertEquals(0, chart.getPreferredSize().getWidth(), 0.1f);
        assertEquals(0, chart.getPreferredSize().getHeight(), 0.1f);
    }

    @Test
    public void testRenderTitle() {
        String title = "Test Chart Title";
        ChartDataModel model = new ChartDataModel();
        model.setTitle(new Title(title));
        chart = new DefaultChart(model, title);
        Graphics2D g = createMockGraphics2D();
        chart.render(g);
        verify(g).setColor(Color.black);
        verify(g).fillRect(width / 2 - (int)(title.getWidth() / 2.0), 0, (int)title.getWidth(), (int)title.getHeight());
        verify(g).setColor(Color.white);
        verifyNoMoreInteractions(g);
    }

    @Test
    public void testRenderLegend() {
        String legendText = "Test Legend";
        ChartDataModel model = new ChartDataModel();
        model.setLegend(new Legend(legendText));
        chart = new DefaultChart(model, "Test Chart Title");
        Graphics2D g = createMockGraphics2D();
        chart.render(g);
        verify(g).setColor(Color.black);
        verify(g).fillRect((int)(width - legendText.length() * 10), (int)(height / 2 - 10), (int)legendText.length() * 10, 10);
        verifyNoMoreInteractions(g);
    }

    @Test
    public void testRenderChartRenderer() {
        chart = new DefaultChart(model, "Test Chart Title");
        Graphics2D g = createMockGraphics2D();
        renderer.setBounds(new Rectangle2D.Float(0, 0, 100, 100));
        chart.render(g);
        verify(renderer).render(g);
        verifyNoMoreInteractions(g);
    }

    @Test
    public void testRenderCoordinateSystem() {
        chart = new DefaultChart(model, "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y);
        Graphics2D g = createMockGraphics2D();
        chart.render(g);
        verify(g).setColor(Color.black);
        verify(g).fillRect(0, 0, width, height);
        verify(g).setColor(Color.white);
        verifyNoMoreInteractions(g);
    }

    private Graphics2D createMockGraphics2D() {
        return mock(Graphics2D.class);
    }
}