import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.Model.Title;
import de.progra.charting.Render.AbstractChartRenderer;
import de.progra.charting.render.DefaultLineChartRenderer;
import de.progra.charting.render.DefaultScatterChartRenderer;
import de.progra.charting.render.DefaultBarChartRenderer;

public class DefaultChartTest {

    DefaultChart chart;
    AbstractChartRenderer renderer1;
    AbstractChartRenderer renderer2;
    AbstractChartRenderer renderer3;

    @Before
    public void setUp() {
        chart = new DefaultChart(new ChartDataModel(), "Test Chart");
        renderer1 = new DefaultLineChartRenderer();
        renderer2 = new DefaultScatterChartRenderer();
        renderer3 = new DefaultBarChartRenderer();
        chart.getChartRenderer().put("LineChart", renderer1);
        chart.getChartRenderer().put("ScatterChart", renderer2);
        chart.getChartRenderer().put("BarChart", renderer3);
    }

    @Test
    public void TestDefaultChart() {
        DefaultChart chart = new DefaultChart();
        assertNull(chart.getChartDataModel());
        assertNull(chart.getRowColorModel());
        assertNull(chart.getLegend());
        assertNull(chart.getTitle());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getChartRenderer());
        // Test getPreferredSize()
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void TestDefaultChartTitle() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart");
        Title title = chart.getTitle();
        assertNotNull(title);
        assertEquals("Test Chart", title.getText());
        assertNull(chart.getCoordSystem());
    }

    @Test
    public void TestDefaultChartCoordSystem() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        CoordSystem coordSystem = chart.getCoordSystem();
        assertNotNull(coordSystem);
        // Edge case: Test with NO_COORDINATE_SYSTEM
        DefaultChart chart2 = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM);
        assertNull(chart2.getCoordSystem());
    }

    @Test
    public void TestDefaultChartXAxisAndYAxis() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "Test X-Axis", "Test Y-Axis");
        CoordSystem coordSystem = chart.getCoordSystem();
        assertNotNull(coordSystem);
        assertEquals("Test X-Axis", coordSystem.getXAxisUnit());
        assertEquals("Test Y-Axis", coordSystem.getYAxisUnit());
    }

    @Test
    public void TestDefaultChartGetPreferredSize() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void TestDefaultChartGetPreferredSizeWithTitleAndLegend() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.setTitle(new Title("Test Chart Title"));
        chart.setLegend(new Legend("Test Legend"));
        Dimension preferredSize = chart.getPreferredSize();
        // Edge case: Test when title and legend have large size
        Title title = new Title("Test Chart Title with large text");
        title.setPreferredSize(new Dimension(200, 200));
        Legend legend = new Legend("Test Legend with large text");
        legend.setPreferredSize(new Dimension(300, 300));
        chart.setTitle(title);
        chart.setLegend(legend);
        preferredSize = chart.getPreferredSize();
        assertNotNull(preferredSize);
        int expectedWidth = title.getPreferredSize().getWidth() + legend.getPreferredSize().getWidth() + chart.getBounds().getWidth();
        int expectedHeight = title.getPreferredSize().getHeight() + legend.getPreferredSize().getHeight() + chart.getBounds().getHeight();
        assertEquals(expectedWidth, preferredSize.getWidth());
        assertEquals(expectedHeight, preferredSize.getHeight());
    }

    @Test
    public void TestDefaultChartRender() {
        Graphics2D g = null;
        // Test render method when no title, legend, or coord system is set
        chart.render(g);
        // Test render method when title is set
        chart.setTitle(new Title("Test Chart Title"));
        chart.render(g);
        // Test render method when legend is set
        chart.setLegend(new Legend("Test Legend"));
        chart.render(g);
        // Test render method when title, legend, and coord system are set
        chart.setTitle(new Title("Test Chart Title"));
        chart.setLegend(new Legend("Test Legend"));
        chart.setCoordSystem(new CoordSystem(new ChartDataModel()));
        chart.render(g);
    }

    @Test
    public void TestDefaultChartSetTitle() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        Title title = new Title("Test Chart Title");
        chart.setTitle(title);
        Title title1 = chart.getTitle();
        assertNotNull(title1);
        assertEquals("Test Chart Title", title1.getText());
    }

    @Test
    public void TestDefaultChartSetLegend() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        Legend legend = new Legend("Test Legend");
        chart.setLegend(legend);
        Legend legend1 = chart.getLegend();
        assertNotNull(legend1);
        assertEquals("Test Legend", legend1.getText());
    }

    @Test
    public void TestDefaultChartSetCoordSystem() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        CoordSystem coordSystem = new CoordSystem(new ChartDataModel());
        chart.setCoordSystem(coordSystem);
        CoordSystem coordSystem1 = chart.getCoordSystem();
        assertNotNull(coordSystem1);
        // Test when coord system is not set
        DefaultChart chart2 = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNull(chart2.getCoordSystem());
    }

    @Test
    public void TestDefaultChartSetChartRenderer() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        AbstractChartRenderer renderer = new DefaultLineChartRenderer();
        chart.setChartRenderer(renderer);
        AbstractChartRenderer renderer1 = chart.getChartRenderer().get("LineChart");
        assertNotNull(renderer1);
        assertEquals(renderer, renderer1);
    }

    @Test
    public void TestDefaultChartGetChartRenderer() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        AbstractChartRenderer renderer = new DefaultLineChartRenderer();
        chart.setChartRenderer(renderer);
        AbstractChartRenderer renderer1 = chart.getChartRenderer().get("LineChart");
        assertNotNull(renderer1);
        assertEquals(renderer, renderer1);
    }
}