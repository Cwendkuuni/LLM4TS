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
    public void testDefaultChartConstruction() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Chart");
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
    }

    @Test
    public void testDefaultChartWithCoordinateSystem() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testGetPreferredSizeReturnsNull() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Chart");
        assertEquals(null, chart.getPreferredSize());
    }

    @Test
    public void testRenderMethod() {
        ChartDataModel model = new ChartDataModel();
        Title title = new Title("Test Title");
        Legend legend = new Legend(new RowColorModel(model));
        CoordSystem coordSystem = new CoordSystem(model);
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordSystem(coordSystem);

        Graphics2D g = mock(Graphics2D.class);

        when(g.getColor()).thenReturn(Color.WHITE);
        when(g.fillRect(anyInt(), anyInt(), anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            int width = invocation.getArgument(2);
            int height = invocation.getArgument(3);
            System.out.println("Filled rectangle: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
            return true;
        });

        chart.render(g);
    }

    @Test
    public void testRenderWithTitle() {
        ChartDataModel model = new ChartDataModel();
        Title title = new Title("Test Title");
        Legend legend = new Legend(new RowColorModel(model));
        CoordSystem coordSystem = new CoordSystem(model);
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordSystem(coordSystem);

        Graphics2D g = mock(Graphics2D.class);

        when(g.getColor()).thenReturn(Color.WHITE);
        when(g.fillRect(anyInt(), anyInt(), anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            int width = invocation.getArgument(2);
            int height = invocation.getArgument(3);
            System.out.println("Filled rectangle: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
            return true;
        });

        when(title.getPreferredSize()).thenReturn(new Dimension(100, 50));
        when(title.getBounds()).thenReturn(new Rectangle(10, 10, 100, 50));

        chart.render(g);
    }

    @Test
    public void testRenderWithLegend() {
        ChartDataModel model = new ChartDataModel();
        Title title = new Title("Test Title");
        Legend legend = new Legend(new RowColorModel(model));
        CoordSystem coordSystem = new CoordSystem(model);
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordSystem(coordSystem);

        Graphics2D g = mock(Graphics2D.class);

        when(g.getColor()).thenReturn(Color.WHITE);
        when(g.fillRect(anyInt(), anyInt(), anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            int width = invocation.getArgument(2);
            int height = invocation.getArgument(3);
            System.out.println("Filled rectangle: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
            return true;
        });

        when(legend.getPreferredSize()).thenReturn(new Dimension(200, 100));
        when(legend.getBounds()).thenReturn(new Rectangle(300, 150, 200, 100));

        chart.render(g);
    }

    @Test
    public void testRenderWithCoordinateSystem() {
        ChartDataModel model = new ChartDataModel();
        Title title = new Title("Test Title");
        Legend legend = new Legend(new RowColorModel(model));
        CoordSystem coordSystem = new CoordSystem(model);
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordSystem(coordSystem);

        Graphics2D g = mock(Graphics2D.class);

        when(g.getColor()).thenReturn(Color.WHITE);
        when(g.fillRect(anyInt(), anyInt(), anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            int width = invocation.getArgument(2);
            int height = invocation.getArgument(3);
            System.out.println("Filled rectangle: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
            return true;
        });

        when(title.getPreferredSize()).thenReturn(new Dimension(100, 50));
        when(title.getBounds()).thenReturn(new Rectangle(10, 10, 100, 50));

        when(legend.getPreferredSize()).thenReturn(new Dimension(200, 100));
        when(legend.getBounds()).thenReturn(new Rectangle(300, 150, 200, 100));

        when(coordSystem.getBounds()).thenReturn(new Rectangle(0, 100, 600, 400));

        chart.render(g);
    }

    @Test
    public void testRenderWithChartRenderer() {
        ChartDataModel model = new ChartDataModel();
        Title title = new Title("Test Title");
        Legend legend = new Legend(new RowColorModel(model));
        CoordSystem coordSystem = new CoordSystem(model);
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordSystem(coordSystem);

        LineChartRenderer lineChartRenderer = mock(LineChartRenderer.class);
        chart.addChartRenderer(lineChartRenderer);

        Graphics2D g = mock(Graphics2D.class);

        when(g.getColor()).thenReturn(Color.WHITE);
        when(g.fillRect(anyInt(), anyInt(), anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            int width = invocation.getArgument(2);
            int height = invocation.getArgument(3);
            System.out.println("Filled rectangle: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
            return true;
        });

        when(title.getPreferredSize()).thenReturn(new Dimension(100, 50));
        when(title.getBounds()).thenReturn(new Rectangle(10, 10, 100, 50));

        when(legend.getPreferredSize()).thenReturn(new Dimension(200, 100));
        when(legend.getBounds()).thenReturn(new Rectangle(300, 150, 200, 100));

        when(coordSystem.getBounds()).thenReturn(new Rectangle(0, 100, 600, 400));

        chart.render(g);

        verify(lineChartRenderer, times(1)).render(g);
    }
}