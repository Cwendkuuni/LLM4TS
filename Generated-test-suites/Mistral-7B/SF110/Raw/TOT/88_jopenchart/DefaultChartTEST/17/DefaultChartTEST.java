import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.model.DefaultXYDataPoint;
import de.progra.charting.render.DefaultChartRenderer;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel model;
 private DefaultXYDataPoint dataPoint1;
 private DefaultXYDataPoint dataPoint2;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 dataPoint1 = new DefaultXYDataPoint("Data Point 1", new double[]{1.0, 2.0});
 dataPoint2 = new DefaultXYDataPoint("Data Point 2", new double[]{3.0, 4.0});
 model.addDataPoint(dataPoint1);
 model.addDataPoint(dataPoint2);
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testDefaultChartCreation() {
 // Test empty chart creation
 DefaultChart emptyChart = new DefaultChart();
 assertNull(emptyChart.getTitle());
 assertNull(emptyChart.getLegend());
 assertNull(emptyChart.getCoordSystem());

 // Test chart creation with title
 DefaultChart titleChart = new DefaultChart(model, "Test Chart");
 assertEquals("Test Chart", titleChart.getTitle().getText());

 // Test chart creation with title and coordinate system
 DefaultChart coordChart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(coordChart.getCoordSystem());
 }

 @Test
 public void testRender() {
 // Set up renderer for testing
 DefaultChartRenderer renderer = new DefaultChartRenderer();
 chart.getChartRenderer().put("Renderer", renderer);

 // Test rendering with title and legend
 Graphics2D g = createGraphics2D();
 chart.render(g);
 assertNotNull(chart.getTitle());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 renderer.assertRenderCalled();

 // Test rendering without title and legend
 chart = new DefaultChart(model, "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM);
 g = createGraphics2D();
 chart.render(g);
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());

 // Test rendering with title, legend, and coordinate system
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
 g = createGraphics2D();
 chart.render(g);
 assertNotNull(chart.getTitle());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 renderer.assertRenderCalled();
 }

 @Test
 public void testGetPreferredSize() {
 // Test empty chart preferred size
 DefaultChart emptyChart = new DefaultChart();
 assertEquals(0, emptyChart.getPreferredSize().width);
 assertEquals(0, emptyChart.getPreferredSize().height);

 // Test chart preferred size with title and legend
 assertEquals(100, chart.getPreferredSize().width);
 assertEquals(150, chart.getPreferredSize().height);
 }

 private Graphics2D createGraphics2D() {
 GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
 GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultGraphicsConfiguration();
 return gc.createGraphic();
 }
}