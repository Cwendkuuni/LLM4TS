import static org.junit.Assert.*;
import static de.progra.charting.model.ChartDataModel.*;
import static de.progra.charting.model.ChartType.*;
import static de.progra.charting.model.DataPoint.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartRenderer;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.model.DataSeries;
import de.progra.charting.render.AxisLabelRenderer;
import de.progra.charting.render.DefaultAxisLabelRenderer;
import de.progra.charting.render.DefaultChartRenderer;
import de.progra.charting.render.DefaultLineAndBarRenderer;
import de.progra.charting.render.LineAndBarRenderer;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel model;
 private ChartRenderer renderer;

 @Before
 public void setUp() {
 model = new ChartDataModel(BAR_CHART, new Date(2022, 1, 1), new Date(2022, 12, 31), new String[]{"Series1", "Series2"}, new int[]{LINEAR_X, LINEAR_Y}, new DataSeries[2]);
 chart = new DefaultChart(model, "Default Chart");

 renderer = new DefaultChartRenderer();
 chart.setChartRenderer(renderer);
 }

 @Test
 public void testDefaultChartConstruction() {
 // Test empty DefaultChart construction
 DefaultChart emptyChart = new DefaultChart();
 assertEquals(NO_COORDINATE_SYSTEM, emptyChart.getCoordSystem());

 // Test DefaultChart construction with title
 DefaultChart titleChart = new DefaultChart(model, "Default Chart");
 assertEquals("Default Chart", titleChart.getTitle().getText());

 // Test DefaultChart construction with title and linear coordinate system
 DefaultChart titleLinearChart = new DefaultChart(model, "Default Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(titleLinearChart.getCoordSystem());

 // Test DefaultChart construction with title, linear coordinate system, and axis labels
 DefaultChart titleLinearChartWithLabels = new DefaultChart(model, "Default Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
 ChartRenderer linearChartRenderer = new DefaultChartRenderer();
 linearChartRenderer.setAxisLabelRenderer(new DefaultAxisLabelRenderer());
 titleLinearChartWithLabels.setChartRenderer(linearChartRenderer);
 assertNotNull(titleLinearChartWithLabels.getCoordSystem().getXAxisLabel());
 assertNotNull(titleLinearChartWithLabels.getCoordSystem().getYAxisLabel());
 }

 @Test
 public void testRenderMethod() {
 // Test empty render method
 ChartDataModel emptyModel = new ChartDataModel(BAR_CHART, new Date(2022, 1, 1), new Date(2022, 12, 31), new String[]{"Series1", "Series2"}, new int[]{LINEAR_X, LINEAR_Y}, new DataSeries[2]);
 DefaultChart emptyChart = new DefaultChart(emptyModel, "Empty Chart");
 assertNull(emptyChart.getBounds());

 // Test render method with title and legend
 chart.render(createGraphics2DMock());
 Title title = chart.getTitle();
 Legend legend = chart.getLegend();
 assertTrue(title.isVisible());
 assertTrue(legend.isVisible());

 // Test render method with title, legend, and data
 DataSeries series1 = model.getDataSeries(0);
 series1.addDataPoint(new DataPoint(1, 10));
 series1.addDataPoint(new DataPoint(2, 15));
 series1.addDataPoint(new DataPoint(3, 12));
 DataSeries series2 = model.getDataSeries(1);
 series2.addDataPoint(new DataPoint(1, 5));
 series2.addDataPoint(new DataPoint(2, 8));
 series2.addDataPoint(new DataPoint(3, 12));

 List<AbstractChartRenderer> renderers = new ArrayList<>();
 LineAndBarRenderer barRenderer = new DefaultLineAndBarRenderer();
 renderers.add(barRenderer);
 chart.setChartRenderer(renderers);

 chart.render(createGraphics2DMock());
 assertNotNull(chart.getBounds());
 assertNotNull(title.getBounds());
 assertNotNull(legend.getBounds());

 // Test render method with title, legend, data, and coordinate system
 ChartDataModel coordSystemModel = new ChartDataModel(BAR_CHART, new Date(2022, 1, 1), new Date(2022, 12, 31), new String[]{"Series1", "Series2"}, new int[]{LINEAR_X, LINEAR_Y}, new DataSeries[2]);
 DefaultChart coordSystemChart = new DefaultChart(coordSystemModel, "Coordinate System Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
 DataSeries coordSystemSeries1 = coordSystemModel.getDataSeries(0);
 DataSeries coordSystemSeries2 = coordSystemModel.getDataSeries(1);
 series1.setDataPoints(new ArrayList<>());
 series2.setDataPoints(new ArrayList<>());

 // Add data points to the series
 for (int i = 0; i < 10; i++) {
 series1.addDataPoint(new DataPoint(i, (float)Math.sin(i * 0.1) * 50 + 50));
 series2.addDataPoint(new DataPoint(i, (float)Math.cos(i * 0.1) * 50 + 50));
 }

 // Set up the renderers
 List<AbstractChartRenderer> renderersCS = new ArrayList<>();
 LineAndBarRenderer barRendererCS = new DefaultLineAndBarRenderer();
 renderersCS.add(barRendererCS);
 AxisLabelRenderer axisLabelRendererCS = new DefaultAxisLabelRenderer();
 barRendererCS.setAxisLabelRenderer(axisLabelRendererCS);
 coordSystemChart.setChartRenderer(renderersCS);

 // Render the chart
 coordSystemChart.render(createGraphics2DMock());

 // Test the bounds and coordinates of the title, legend, and renderers
 Rectangle chartBounds = coordSystemChart.getBounds();
 Rectangle titleRect = title.getBounds();
 Rectangle legendRect = legend.getBounds();
 List<Rectangle> rendererRects = new ArrayList<>();
 for (AbstractChartRenderer renderer : renderersCS) {
 rendererRects.add(renderer.getBounds());
 }

 // Verify the bounds and coordinates
 assertEquals(chartBounds.width, titleRect.x + titleRect.width + legendRect.x + legendRect.width + 5 * rendererRects.size());
 assertEquals(chartBounds.height, titleRect.y + titleRect.height + legendRect.y + legendRect.height + 5 * rendererRects.size());

 for (int i = 0; i < rendererRects.size(); i++) {
 assertEquals(rendererRects.get(i).x, 0);
 assertEquals(rendererRects.get(i).y, titleRect.y + titleRect.height);
 }
 }

 private Graphics2D createGraphics2DMock() {
 Map<String, Object> mockProperties = new HashMap<>();
 mockProperties.put(GraphicsEnvironment.class.getName(), mock(GraphicsEnvironment.class));
 mockProperties.put(GraphicsConfiguration.class.getName(), mock(GraphicsConfiguration.class));
 return mock(Graphics2D.class, mockProperties);
 }
}