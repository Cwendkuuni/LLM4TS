import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import org.junit.Test;

public class DefaultChartTest {

    @Test public void testDefaultChart() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Chart");
        assertNotNull(chart.getTitle());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getLegend());
    }

    @Test public void testDefaultChartWithTitle(ChartDataModel model) {
        DefaultChart chart = new DefaultChart(model, "Test Chart");
        Title title = chart.getTitle();
        assertNotNull(title);
        assertEquals("Test Chart", title.getText());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getLegend());
    }

    @Test public void testDefaultChartWithCoordinateSystem(ChartDataModel model) {
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        CoordSystem coordSystem = chart.getCoordSystem();
        assertNotNull(coordSystem);
        assertNotNull(coordSystem.getXAxisUnit());
        assertNotNull(coordSystem.getYAxisUnit());
    }

    @Test(expected = NullPointerException.class) public void testDefaultChartWithNullModel() {
        DefaultChart chart = new DefaultChart(null, "Test Chart");
    }

    @Test public void testDefaultChartWithEmptyTitle() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "");
        Title title = chart.getTitle();
        assertNotNull(title);
        assertEquals("", title.getText());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getLegend());
    }

    @Test(expected = IllegalArgumentException.class) public void testDefaultChartWithInvalidCoordinate() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Chart", 2);
    }
}

//