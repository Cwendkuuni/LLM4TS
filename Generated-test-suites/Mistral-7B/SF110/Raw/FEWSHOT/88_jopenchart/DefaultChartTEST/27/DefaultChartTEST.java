package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;

import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

    @Test
    public void testDefaultChartConstruction() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title");
        assertNotNull(chart.getTitle());
        assertEquals("Title", chart.getTitle().getText());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertFalse(chart.hasCoordinateSystem());
    }

    @Test
    public void testDefaultChartWithTitleAndCoordinateSystem() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getTitle());
        assertEquals("Title", chart.getTitle().getText());
        assertNull(chart.getLegend());
        assertNotNull(chart.getCoordSystem());
        assertTrue(chart.hasCoordinateSystem());
    }

    @Test
    public void testDefaultChartWithTitleAndCoordinateSystemAndLegend() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
        assertNotNull(chart.getTitle());
        assertEquals("Title", chart.getTitle().getText());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getLegend().getTitle());
        assertEquals("Legend", chart.getLegend().getTitle().getText());
        assertNotNull(chart.getCoordSystem());
        assertTrue(chart.hasCoordinateSystem());
    }

    @Test
    public void testGetPreferredSizeReturnsNull() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title");
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRenderWithTitle() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title");

        Graphics2D g = createMockGraphics2D();

        chart.render(g);

        verify(g).fillRect(0, 0, anyInt(), anyInt());
        verify(g).setColor(eq(Color.black));
        verify(g).draw(any(Rectangle2D.class));
        verifyNoMoreInteractions(g);
    }

    @Test
    public void testRenderWithTitleAndLegend() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", "Legend");

        Graphics2D g = createMockGraphics2D();

        chart.render(g);

        verify(g).fillRect(0, 0, anyInt(), anyInt());
        verify(g).setColor(eq(Color.black));
        verify(g).draw(any(Rectangle2D.class));
        verify(g).setColor(eq(Color.WHITE));
        verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
        verify(g).setColor(eq(Color.BLACK));
        verify(g).draw(any(Rectangle2D.class));
        verifyNoMoreInteractions(g);
    }

    @Test
    public void testRenderWithTitleAndCoordinateSystem() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", DefaultChart.LINEAR_X_LINEAR_Y);

        Graphics2D g = createMockGraphics2D();

        chart.render(g);

        verify(g).fillRect(0, 0, anyInt(), anyInt());
        verify(g).setColor(eq(Color.black));
        verify(g).draw(any(Rectangle2D.class));
        verify(g).setColor(eq(Color.WHITE));
        verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
        verify(g).setColor(eq(Color.BLACK));
        verify(g).draw(any(Rectangle2D.class));
        verifyNoMoreInteractions(g);
    }

    @Test
    public void testRenderWithTitleAndCoordinateSystemAndLegend() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");

        Graphics2D g = createMockGraphics2D();

        chart.render(g);

        verify(g).fillRect(0, 0, anyInt(), anyInt());
        verify(g).setColor(eq(Color.black));
        verify(g).draw(any(Rectangle2D.class));
        verify(g).setColor(eq(Color.WHITE));
        verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
        verify(g).setColor(eq(Color.BLACK));
        verify(g).draw(any(Rectangle2D.class));
        verify(g).setColor(eq(Color.BLACK));
        verify(g).draw(any(Rectangle2D.class));
        verifyNoMoreInteractions(g);
    }

    @Test
    public void testRenderWithTitleAndCoordinateSystemAndLegendAndRenderers() {
        ChartDataModel model = new ChartDataModel();
        model.setData(Collections.singletonList(new DataSet("DataSet", DataType.LINE, new double[][]{new double[]{1.0, 2.0, 3.0}, new double[]{4.0, 5.0, 6.0}}, new String[]{"Label 1", "Label 2", "Label 3"})));
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");

        LineChartRenderer renderer = new LineChartRenderer();
        chart.setChartRenderer(Collections.singletonMap("DataSet", renderer));

        Graphics2D g = createMockGraphics2D();

        chart.render(g);

        verify(g).fillRect(0, 0, anyInt(), anyInt());
        verify(g).setColor(eq(Color.black));
        verify(g).draw(any(Rectangle2D.class));
        verify(g).setColor(eq(Color.WHITE));
        verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
        verify(g).setColor(eq(Color.BLACK));
        verify(g).draw(any(Rectangle2D.class));
        verify(g).setColor(eq(Color.BLACK));
        verify(g).draw(any(Rectangle2D.class));
        verify(renderer).render(g);
        verifyNoMoreInteractions(g);
    }
}