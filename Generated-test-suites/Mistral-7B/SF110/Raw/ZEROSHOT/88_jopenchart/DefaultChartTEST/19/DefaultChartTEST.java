package de.progra.charting.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelType;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {

    private DefaultChart chart;
    private ChartDataModel model;
    private Title title;
    private Legend legend;
    private CoordSystem coordSystem;
    private Map<ModelType, AbstractChartRenderer> chartRenderer;

    @Before
    public void setUp() {
        model = mock(ChartDataModel.class);
        title = mock(Title.class);
        legend = mock(Legend.class);
        coordSystem = mock(CoordSystem.class);
        chartRenderer = new HashMap<>();

        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
    }

    @Test
    public void testDefaultChartConstruction() {
        assertNotNull(chart);
    }

    @Test
    public void testChartConstructionWithTitle() {
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getTitle());
    }

    @Test
    public void testChartConstructionWithTitleAndCoordinateSystem() {
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testChartConstructionWithNullDataModel() {
        DefaultChart chart = new DefaultChart(null, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNull(chart);
    }

    @Test
    public void testGetPreferredSizeReturnsNull() {
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRenderMethod() {
        when(title.getPreferredSize()).thenReturn(new Dimension(100, 50));
        when(legend.getPreferredSize()).thenReturn(new Dimension(200, 100));
        when(coordSystem.getBounds()).thenReturn(new Rectangle(0, 50, 800, 600));

        chart.getChartRenderer().put(ModelType.BAR_CHART, mock(BarChartRenderer.class));

        Graphics2D g = mock(Graphics2D.class);
        chart.render(g);

        verifyCoordinateSystemRendering(g);
        verifyRendererRendering(g);
        verifyTitleRendering(g);
        verifyLegendRendering(g);
    }

    private void verifyCoordinateSystemRendering(Graphics2D g) {
        when(coordSystem.getBounds()).thenReturn(new Rectangle(0, 50, 800, 600));
        chart.getCoordSystem().render(g);
        // Add verification code here for checking the rendering of the coordinate system
    }

    private void verifyRendererRendering(Graphics2D g) {
        AbstractChartRenderer barChartRenderer = chart.getChartRenderer().get(ModelType.BAR_CHART);
        barChartRenderer.setBounds(new Rectangle(0, 50, 800, 600));
        barChartRenderer.render(g);
        // Add verification code here for checking the rendering of the chart renderer
    }

    private void verifyTitleRendering(Graphics2D g) {
        when(title.getPreferredSize()).thenReturn(new Dimension(100, 50));
        title.setBounds(new Rectangle(400, 0, 100, 50));
        title.render(g);
        // Add verification code here for checking the rendering of the title
    }

    private void verifyLegendRendering(Graphics2D g) {
        when(legend.getPreferredSize()).thenReturn(new Dimension(200, 100));
        legend.setBounds(new Rectangle(600, 275, 200, 100));
        legend.render(g);
        // Add verification code here for checking the rendering of the legend
    }
}