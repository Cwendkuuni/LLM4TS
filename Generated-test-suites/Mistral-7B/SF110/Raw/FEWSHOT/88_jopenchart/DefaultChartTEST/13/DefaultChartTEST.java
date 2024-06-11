package de.progra.charting.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collections;

import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelDataSeries;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.test.mock.MockChartDataModel;
import de.progra.charting.test.mock.MockChartRenderer;
import de.progra.charting.test.mock.MockTitle;
import de.progra.charting.test.mock.MockLegend;

public class DefaultChartTest {

    @Test
    public void testDefaultChartInitialization() {
        DefaultChart chart = new DefaultChart(new MockChartDataModel(), "TestChartTitle");
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }

    @Test
    public void testDefaultChartInitializationWithCoordinateSystem() {
        DefaultChart chart = new DefaultChart(new MockChartDataModel(), "TestChartTitle", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testDefaultChartInitializationWithCoordinateSystemAndCustomXAndYAxis() {
        DefaultChart chart = new DefaultChart(new MockChartDataModel(), "TestChartTitle", DefaultChart.LINEAR_X_LINEAR_Y, "XAxisUnit", "YAxisUnit");
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getCoordSystem().getXAxisUnit());
        assertNotNull(chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSizeReturnsNull() {
        DefaultChart chart = new DefaultChart(new MockChartDataModel(), "TestChartTitle");
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRenderTitleAndLegend() {
        DefaultChart chart = new DefaultChart(new MockChartDataModel(), "TestChartTitle");
        Title title = new MockTitle("TestChartTitle");
        Legend legend = new MockLegend();

        chart.setTitle(title);
        chart.setLegend(legend);

        Graphics2D g = null;
        Dimension dimensions = new Dimension(300, 300);
        chart.setBounds(new Rectangle(0, 0, dimensions.width, dimensions.height));

        chart.render(g);

        assertTrue(title.isRendered());
        assertTrue(legend.isRendered());
    }

    @Test
    public void testRenderChartRenderer() {
        DefaultChart chart = new DefaultChart(new MockChartDataModel(), "TestChartTitle");
        ChartDataModel model = new MockChartDataModel();
        model.addSeries(new ModelDataSeries("Series1", new int[] { 1, 2, 3, 4, 5 }, new float[] { 1.1f, 2.2f, 3.3f, 4.4f, 5.5f }));

        AbstractChartRenderer renderer = new MockChartRenderer();
        ChartRenderer chartRenderer = new AbstractChartRenderer() {
            @Override
            public Collection<AbstractChartRenderer> getChartRenderers() {
                return Collections.singletonList(renderer);
            }
        };

        chart.setChartRenderer(chartRenderer);

        Graphics2D g = null;
        Dimension dimensions = new Dimension(300, 300);
        chart.setBounds(new Rectangle(0, 0, dimensions.width, dimensions.height));

        chart.render(g);

        assertTrue(renderer.isRendered());
    }

    @Test
    public void testRenderCoordinateSystem() {
        DefaultChart chart = new DefaultChart(new MockChartDataModel(), "TestChartTitle", DefaultChart.LINEAR_X_LINEAR_Y);
        CoordSystem coordSystem = new CoordSystem(new MockChartDataModel());

        graph.setCoordSystem(coordSystem);

        Graphics2D g = null;
        Dimension dimensions = new Dimension(300, 300);
        chart.setBounds(new Rectangle(0, 0, dimensions.width, dimensions.height));

        chart.render(g);

        assertTrue(coordSystem.isRendered());
    }
}