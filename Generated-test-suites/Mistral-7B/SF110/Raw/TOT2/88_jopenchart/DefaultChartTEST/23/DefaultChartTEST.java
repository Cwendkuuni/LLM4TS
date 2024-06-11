
package de.progra.charting.test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartRenderer;
import de.progra.charting.ChartRendererData;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import org.junit.*;
import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;

public class DefaultChartTest {

    @BeforeClass
    public static void setUpClass() {
        // Set up any static things here
    }

    @AfterClass
    public static void tearDownClass() {
        // Clean up any static things here
    }

    @Before
    public void setUp() {
        // Set up any test-specific things here
    }

    @After
    public void tearDown() {
        // Clean up any test-specific things here
    }

    @Test
    public void testDefaultChart() {
        DefaultChart chart = new DefaultChart();
        assertNull(chart.getChartDataModel());
        assertNull(chart.getTitle());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getLegend());
    }

    @Test
    public void testDefaultChartWithTitle() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Chart");
        assertNotNull(chart.getChartDataModel());
        assertEquals("Test Chart", chart.getTitle().getText());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getLegend());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultChartWithInvalidCoordinateSystem() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Chart", 2, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testRenderWithNullGraphics2D() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        Graphics2D g = null;
        chart.render(g);
    }

    @Test(expected = NullPointerException.class)
    public void testRenderWithNullRenderer() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.setChartRenderer(new HashMap<>());
        Graphics2D g = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
        chart.render(g);
        chart.getChartRenderer().put(new ChartRenderer(), new ChartRendererData());
        chart.render(g); // This should throw a NullPointerException since there is no renderer for the given chart.
    }
}
