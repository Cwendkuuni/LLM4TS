package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;

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

    @Test
    public void testDefaultChartConstructor() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart");
        assertNotNull(chart.getTitle());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getLegend());
    }

    @Test
    public void testDefaultChartConstructorWithTitleAndCoordinateSystem() {
        ChartDataModel model = new ChartDataModel();
        model.setData(Collections.singletonList(new ModelData("Test Data", new double[5], new double[5])));
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertNull(chart.getLegend());
    }

    @Test
    public void testDefaultChartConstructorWithTitleTitleAndCoordinateSystemAndAxisLabels() {
        ChartDataModel model = new ChartDataModel();
        model.setData(Collections.singletonList(new ModelData("Test Data", new double[5], new double[5])));
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getLegend());
    }

    @Test
    public void testGetPreferredSize() {
        ChartDataModel model = new ChartDataModel();
        model.setData(Collections.singletonList(new ModelData("Test Data", new double[5], new double[5])));
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRender() {
        ChartDataModel model = new ChartDataModel();
        model.setData(Collections.singletonList(new ModelData("Test Data", new double[5], new double[5])));
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
        Title title = new Title("Test Title");
        Legend legend = new Legend(new RowColorModel(model));
        CoordSystem coordSystem = new CoordSystem(model);

        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordSystem(coordSystem);

        Graphics2D g = mock(Graphics2D.class);
        when(g.getColor()).thenReturn(Color.WHITE);

        chart.render(g);

        verify(g).fillRect(0, 0, anyInt(), anyInt());
        verify(g).setColor(Color.BLACK);

        Title titleRender = mock(Title.class);
        Legend legendRender = mock(Legend.class);
        CoordSystem coordSystemRender = mock(CoordSystem.class);
        AbstractChartRenderer renderer = mock(AbstractChartRenderer.class);

        when(title.getPreferredSize()).thenReturn(new Dimension(100, 50));
        when(legend.getPreferredSize()).thenReturn(new Dimension(50, 50));
        when(coordSystem.getBounds()).thenReturn(new Rectangle2D.Double(0, 50, 500, 500));
        when(renderer.getBounds()).thenReturn(new Rectangle2D.Double(0, 50, 500, 500));

        chart.setTitle(titleRender);
        chart.setLegend(legendRender);
        chart.setCoordSystem(coordSystemRender);
        chart.getChartRenderer().put("Test Renderer", renderer);

        chart.render(g);

        verify(g, times(2)).setColor(Color.BLACK);
        verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
        verify(g).render(titleRender);
        verify(g).render(legendRender);
        verify(g).render(renderer);
        verify(coordSystemRender).render(g);
    }
}