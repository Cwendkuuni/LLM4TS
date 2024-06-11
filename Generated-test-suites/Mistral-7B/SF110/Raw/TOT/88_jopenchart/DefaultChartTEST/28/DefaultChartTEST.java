package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
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
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.model.DataSeries;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {

    private DefaultChart chart;
    private ChartDataModel model;
    private List<DataSeries> seriesList;

    @Before
    public void setup() {
        model = new ChartDataModel();
        seriesList = Arrays.asList(
                new DataSeries("Series1", Arrays.asList(1.0, 2.0, 3.0, 4.0)),
                new DataSeries("Series2", Arrays.asList(5.0, 6.0, 7.0, 8.0)));
        chart = new DefaultChart(model, "Test Chart");
    }

    @Test
    public void testDefaultChartConstruction() {
        assertNull(chart.getTitle());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testChartTitle() {
        chart = new DefaultChart(model, "Test Chart");
        Title title = chart.getTitle();
        assertEquals("Test Chart", title.getText());
    }

    @Test
    public void testChartTitleNull() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM);
        Title title = chart.getTitle();
        assertNull(title);
    }

    @Test
    public void testChartLegend() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        Legend legend = chart.getLegend();
        assertNotNull(legend);
    }

    @Test
    public void testChartLegendNull() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM);
        Legend legend = chart.getLegend();
        assertNull(legend);
    }

    @Test
    public void testChartCoordSystem() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        CoordSystem coordSystem = chart.getCoordSystem();
        assertNotNull(coordSystem);
    }

    @Test
    public void testChartCoordSystemNull() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM);
        CoordSystem coordSystem = chart.getCoordSystem();
        assertNull(coordSystem);
    }

    @Test
    public void testSetAndGetChartDataModel() {
        ChartDataModel expectedModel = new ChartDataModel();
        chart.setChartDataModel(expectedModel);
        assertEquals(expectedModel, chart.getChartDataModel());
    }

    @Test
    public void testSetAndGetRowColorModel() {
        RowColorModel expectedRowColorModel = new RowColorModel(model);
        chart.setRowColorModel(expectedRowColorModel);
        assertEquals(expectedRowColorModel, chart.getRowColorModel());
    }

    @Test
    public void testSetAndGetLegend() {
        Legend expectedLegend = new Legend(chart.getRowColorModel());
        chart.setLegend(expectedLegend);
        assertEquals(expectedLegend, chart.getLegend());
    }

    @Test
    public void testSetAndGetTitle() {
        Title expectedTitle = new Title("Test Title");
        chart.setTitle(expectedTitle);
        assertEquals(expectedTitle, chart.getTitle());
    }

    @Test
    public void testGetPreferredSize() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRenderWithTitle() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        Title title = new Title("Test Title");
        chart.setTitle(title);
        Graphics2D g = mock(Graphics2D.class);
        chart.render(g);
        verify(g).setColor(Color.white);
        verify(g).fillRect(0, 0, anyInt(), anyInt());
        verify(g).setColor(Color.black);
        verify(g).drawString("Test Title", width / 2 - (int)(title.getWidth() / 2.0), 0);
        verify(g).setColor(Color.black);
    }

    @Test
    public void testRenderWithoutTitle() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM);
        Graphics2D g = mock(Graphics2D.class);
        chart.render(g);
        verify(g).setColor(Color.white);
        verify(g).fillRect(0, 0, anyInt(), anyInt());
    }

    @Test
    public void testRenderWithLegend() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        Legend legend = new Legend(chart.getRowColorModel());
        chart.setLegend(legend);
        Graphics2D g = mock(Graphics2D.class);
        chart.render(g);
        verify(g).setColor(Color.white);
        verify(g).fillRect(0, 0, anyInt(), anyInt());
        verify(g).setColor(Color.black);
        verify(g).drawString("Series1", (int)(width - legend.getWidth()), (int)(height / 2 - legend.getHeight() / 2.0 + titleheight));
        verify(g).drawString("Series2", (int)(width - legend.getWidth()), (int)(height / 2 - legend.getHeight() / 2.0 + titleheight + 20));
    }

    @Test
    public void testRenderWithoutLegend() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM);
        Graphics2D g = mock(Graphics2D.class);
        chart.render(g);
        verify(g).setColor(Color.white);
        verify(g).fillRect(0, 0, anyInt(), anyInt());
    }

    @Test
    public void testRenderWithData() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.setChartDataModel(model);
        chart.setRowColorModel(new RowColorModel(model));
        for (DataSeries series : seriesList) {
            chart.getChartRenderer().put("Series" + series.getName(), new LineChartRenderer(series));
        }
        Graphics2D g = mock(Graphics2D.class);
        chart.render(g);
        verify(g).setColor(Color.white);
        verify(g).fillRect(0, 0, anyInt(), anyInt());
        verify(g).setColor(Color.black);
        for (DataSeries series : seriesList) {
            verify(g).draw(argThat(new IsLineChartRenderer(series.getName())));
        }
        verify(g).draw(argThat(new IsCoordSystemRenderer()));
    }

    @Test
    public void testRenderWithoutData() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM);
        Graphics2D g = mock(Graphics2D.class);
        chart.render(g);
        verify(g).setColor(Color.white);
        verify(g).fillRect(0, 0, anyInt(), anyInt());
    }

    @Test
    public void testSetAndGetCoordSystem() {
        CoordSystem expectedCoordSystem = new CoordSystem(model);
        chart.setCoordSystem(expectedCoordSystem);
        assertEquals(expectedCoordSystem, chart.getCoordSystem());
    }

    @Test
    public void testSetAndGetCoordSystemNull() {
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNull(chart.getCoordSystem());
    }

    @Test
    public void testSetXAxisUnit() {
        CoordSystem coordSystem = new CoordSystem(model);
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "XUnit1", "YUnit1");
        chart.setCoordSystem(coordSystem);
        coordSystem.setXAxisUnit("XUnit2");
        assertEquals("XUnit2", coordSystem.getXAxisUnit());
    }

    @Test
    public void testSetYAxisUnit() {
        CoordSystem coordSystem = new CoordSystem(model);
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "XUnit1", "YUnit1");
        chart.setCoordSystem(coordSystem);
        coordSystem.setYAxisUnit("YUnit2");
        assertEquals("YUnit2", coordSystem.getYAxisUnit());
    }

    private static ArgMatcher<AbstractChartRenderer> isLineChartRenderer(String seriesName) {
        return new ArgMatcher<AbstractChartRenderer>() {
            @Override
            public boolean matches(AbstractChartRenderer arg) {
                return arg instanceof LineChartRenderer && ((LineChartRenderer) arg).getDataSeries().getName().equals(seriesName);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a LineChartRenderer for the series: " + seriesName);
            }
        };
    }

    private static ArgMatcher<AbstractChartRenderer> isCoordSystemRenderer() {
        return new ArgMatcher<AbstractChartRenderer>() {
            @Override
            public boolean matches(AbstractChartRenderer arg) {
                return arg instanceof CoordSystemRenderer;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a CoordSystemRenderer");
            }
        };
    }
}