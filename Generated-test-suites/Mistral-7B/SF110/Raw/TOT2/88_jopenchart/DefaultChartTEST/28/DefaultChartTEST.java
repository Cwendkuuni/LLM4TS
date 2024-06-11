import static org.junit.Assert.*;

import de.progra.charting.model.*;
import de.progra.charting.render.*;
import java.awt.*;
import java.util.*;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class DefaultChartTest {

    @Parameterized.Parameters(name = "Test case for DefaultChart({0})")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                new Object[]{null},
                new Object[]{new ChartDataModel()},
                new Object[]{new ChartDataModel(), "Test Chart Title"},
                new Object[]{new ChartDataModel(), "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y},
                new Object[]{new ChartDataModel(), "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y, "", "Test Y-Axis"},
                new Object[]{new ChartDataModel(), "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y, "Test X-Axis", ""},
                new Object[]{new ChartDataModel(), "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y, "", null},
                new Object[]{new ChartDataModel(), "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y, null, "Test Y-Axis"},
                new Object[]{new ChartDataModel(), "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y, "Test X-Axis", null}
        });
    }

    private ChartDataModel model;
    private String title;
    private int coord;
    private String xaxis;
    private String yaxis;

    public DefaultChartTest(ChartDataModel model, String title, int coord, String xaxis, String yaxis) {
        this.model = model;
        this.title = title;
        this.coord = coord;
        this.xaxis = xaxis;
        this.yaxis = yaxis;
    }

    @Test
    public void testDefaultChart() {
        DefaultChart chart = new DefaultChart();
        assertNull(chart.getChartDataModel());
        assertNull(chart.getTitle());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getChartRenderer().values().iterator().next());
    }

    @Test
    public void testDefaultChartWithTitle(ChartDataModel model) {
        DefaultChart chart = new DefaultChart(model, "Test Chart Title");
        assertNotNull(chart.getChartDataModel());
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getTitle());
        assertEquals("Test Chart Title", chart.getTitle().getText());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertNotNull(chart.getChartRenderer().values().iterator().next());
    }

    @Test
    public void testInvalidCoordinate() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart Title", 3);
    }

    @Test(expected = NullPointerException.class)
    public void testNullChartDataModel() {
        DefaultChart chart = new DefaultChart(null, "Test Chart Title");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStringXAxis() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y, "", "Test Y-Axis");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStringYAxis() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y, "Test X-Axis", "");
    }

    @Test(expected = NullPointerException.class)
    public void testNullXAxis() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y, null, "Test Y-Axis");
    }

    @Test(expected = NullPointerException.class)
    public void testNullYAxis() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y, "Test X-Axis", null);
    }

    @Test
    public void testGetPreferredSize() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y, "Test X-Axis", "Test Y-Axis");
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRender() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y, "Test X-Axis", "Test Y-Axis");

        Graphics2D g = null; //To be implemented

        chart.render(g);
    }
}