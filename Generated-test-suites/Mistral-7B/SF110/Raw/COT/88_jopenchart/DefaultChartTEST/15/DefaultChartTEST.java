import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private ChartDataModel model;
 private DefaultChart chart;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 chart = new DefaultChart(model, "DefaultChart");
 }

 @Test
 public void testDefaultChartConstruction() {
 chart = new DefaultChart();
 assertNull(chart.getChartDataModel());
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 @Test
 public void testChartWithTitleConstruction() {
 assertEquals("DefaultChart", chart.getTitle().getText());
 }

 @Test
 public void testChartWithTitleAndCoordSystemConstruction() {
 chart = new DefaultChart(model, "DefaultChart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getCoordSystem());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartWithInvalidCoordSystemConstruction() {
 chart = new DefaultChart(model, "DefaultChart", 2);
 }

 @Test
 public void testChartWithTitleAndCoordSystemAndAxisLabelsConstruction() {
 chart = new DefaultChart(model, "DefaultChart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis Label", "Y Axis Label");
 assertNotNull(chart.getCoordSystem());
 assertEquals("X Axis Label", chart.getCoordSystem().getXAxisUnit());
 assertEquals("Y Axis Label", chart.getCoordSystem().getYAxisUnit());
 }

 @Test
 public void testGetPreferredSizeMethod() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRenderMethod() {
 //TODO: Add code to test rendering functionality
 }

 @Test(expected = NullPointerException.class)
 public void testRenderMethodWithNullGraphics2D() {
 chart.render(null);
 }
}