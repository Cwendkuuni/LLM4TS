package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import de.progra.charting.test.mock.MockChartDataModel;
import de.progra.charting.test.mock.MockChartRenderer;
import de.progra.charting.test.mock.MockCoordSystem;
import de.progra.charting.test.mock.MockLegend;
import de.progra.charting.test.mock.MockRowColorModel;
import de.progra.charting.test.mock.MockTitle;

public class DefaultChartTest {

    private DefaultChart chart;
    private ChartDataModel model;
    private AbstractChartRenderer renderer;
    private CoordSystem coordSystem;
    private Legend legend;
    private RowColorModel rowColorModel;
    private Title title;

    @Before
    public void setup() {
        model = new MockChartDataModel();
        model.setType(ModelType.LINE);
        model.setDataPoints(Arrays.asList(new DataPoint(1.0, 2.0), new DataPoint(2.0, 3.0), new DataPoint(3.0, 4.0), new DataPoint(4.0, 5.0)));

        rowColorModel = new MockRowColorModel();
        legend = new MockLegend();
        title = new MockTitle("Test Chart");

        chart = new DefaultChart(model, title);
        chart.setRowColorModel(rowColorModel);
        chart.setLegend(legend);

        renderer = new MockChartRenderer();
        chart.getChartRenderer().put(ModelType.LINE, renderer);

        coordSystem = new MockCoordSystem();
        chart.setCoordSystem(coordSystem);

        chart.setBounds(new Rectangle2D.Double(0, 0, 600, 400));
        chart.setTitle(title);
    }

    @Test
    public void testEmptyConstructor() {
        DefaultChart emptyChart = new DefaultChart();
        assertNull(emptyChart.getTitle());
        assertNull(emptyChart.getLegend());
        assertNull(emptyChart.getCoordSystem());
        assertEquals(Collections.emptyMap(), chart.getChartRenderer());
    }

    @Test
    public void testConstructorWithModel() {
        DefaultChart chartWithModel = new DefaultChart(model, "Test Chart");
        assertEquals(model, chartWithModel.getChartDataModel());
        assertEquals("Test Chart", chartWithModel.getTitle().getText());
        assertNull(chartWithModel.getLegend());
        assertNull(chartWithModel.getCoordSystem());
        assertEquals(Collections.emptyMap(), chartWithModel.getChartRenderer());
    }

    @Test
    public void testConstructorWithModelAndTitle() {
        DefaultChart chartWithModelAndTitle = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertEquals(model, chartWithModelAndTitle.getChartDataModel());
        assertEquals("Test Chart", chartWithModelAndTitle.getTitle().getText());
        assertNotNull(chartWithModelAndTitle.getLegend());
        assertNotNull(chartWithModelAndTitle.getCoordSystem());
        assertEquals(Collections.emptyMap(), chartWithModelAndTitle.getChartRenderer());
    }

    @Test
    public void testConstructorWithModelTitleAndCoordSystem() {
        String xaxis = "Test X-Axis";
        String yaxis = "Test Y-Axis";

        DefaultChart chartWithModelTitleAndCoordSystem = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, xaxis, yaxis);
        assertEquals(model, chartWithModelTitleAndCoordSystem.getChartDataModel());
        assertEquals("Test Chart", chartWithModelTitleAndCoordSystem.getTitle().getText());
        assertNotNull(chartWithModelTitleAndCoordSystem.getLegend());
        assertNotNull(chartWithModelTitleAndCoordSystem.getCoordSystem());
        assertEquals(Collections.emptyMap(), chartWithModelTitleAndCoordSystem.getChartRenderer());
        assertEquals(xaxis, chartWithModelTitleAndCoordSystem.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chartWithModelTitleAndCoordSystem.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testRenderMethod() {
        Graphics2D g = createMockGraphics2D();
        chart.render(g);

        verify(renderer, times(1)).render(g);
        verifyNoMoreInteractions(title);
        verifyNoMoreInteractions(legend);
        verifyNoMoreInteractions(coordSystem);
    }
}