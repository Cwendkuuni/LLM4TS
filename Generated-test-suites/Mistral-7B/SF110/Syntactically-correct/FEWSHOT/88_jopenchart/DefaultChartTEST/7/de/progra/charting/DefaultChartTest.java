package de.progra.charting.test;

import static org.junit.Assert.*;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.Model.RowColorModel;
import de.progra.charting.Render.AbstractChartRenderer;
import de.progra.charting.Title;

public class DefaultChartTest {

    @Test
    public void testDefaultChartConstruction() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart");
        assertNotNull(chart.getTitle());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getLegend());
        assertFalse(chart.getChartRenderer().isEmpty());
    }

    @Test
    public void testDefaultChartWithCoordinateSystem() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertNull(chart.getLegend());
        assertFalse(chart.getChartRenderer().isEmpty());
    }

    @Test
    public void testDefaultChartWithCoordinateSystemAndLegend() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getLegend());
        assertFalse(chart.getChartRenderer().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testNullTitle() {
        new DefaultChart(new ChartDataModel(), null, DefaultChart.LINEAR_X_LINEAR_Y);
    }

    @Test
    public void testGetPreferredSize() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart");
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRenderMethod() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart");
        ChartDataModel model = new ChartDataModel();
        model.addSeries(new Series("Series1", Arrays.asList(new DataPoint(1, 2), new DataPoint(2, 3), new DataPoint(3, 1))));
        model.addSeries(new Series("Series2", Arrays.asList(new DataPoint(1, 3), new DataPoint(2, 2), new DataPoint(3, 3))));
        AbstractChartRenderer renderer1 = new LineChartRenderer();
        AbstractChartRenderer renderer2 = new BarChartRenderer();
        chart.setChartRenderer(Map.of("Series1", renderer1, "Series2", renderer2));
        Title title = new Title("Test Chart");
        Legend legend = new Legend(new RowColorModel(model));
        ChartRenderer cr1 = spy(renderer1);
        ChartRenderer cr2 = spy(renderer2);
        doNothing().when(cr1).render(any(Graphics2D.class));
        doNothing().when(cr2).render(any(Graphics2D.class));

        chart.setTitle(title);
        chart.setLegend(legend);

        chart.render(mock(Graphics2D.class));

        verify(cr1, times(1)).render(any(Graphics2D.class));
        verify(cr2, times(1)).render(any(Graphics2D.class));
    }
}