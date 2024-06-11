package de.progra.charting.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.Model.RowColorModel;
import de.progra.charting.Render.AbstractChartRenderer;
import de.progra.charting.Render.Title;
import de.progra.charting.exception.ChartInitializationException;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 model.setTitle("Test Chart");
 model.setXAxisLabel("X-Axis");
 model.setYAxisLabel("Y-Axis");
 model.setData(Arrays.asList(new DataPoint[] { new DataPoint(1, 1), new DataPoint(2, 2), new DataPoint(3, 3) }));
 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testDefaultChartInitialization() {
 Assert.assertNotNull(chart.getTitle());
 Assert.assertNotNull(chart.getLegend());
 Assert.assertNotNull(chart.getCoordSystem());
 Assert.assertEquals(3, chart.getChartRenderer().size());
 }

 @Test
 public void testNullTitle() {
 ChartDataModel model = new ChartDataModel();
 model.setTitle(null);
 Assert.assertThrows(ChartInitializationException.class, () -> new DefaultChart(model, "Test Chart"));
 }

 @Test
 public void testNullXAxisUnit() {
 ChartDataModel model = new ChartDataModel();
 model.setXAxisLabel(null);
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 Assert.assertNull(chart.getCoordSystem().getXAxisUnit());
 }

 @Test
 public void testEmptyDataModel() {
 ChartDataModel model = new ChartDataModel();
 model.setData(Collections.emptyList());
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 Assert.assertFalse(chart.getChartRenderer().isEmpty());
 }

 @Test
 public void testCustomCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 model.setTitle("Test Chart");
 model.setXAxisLabel("X-Axis");
 model.setYAxisLabel("Y-Axis");
 model.setData(Arrays.asList(new DataPoint[] { new DataPoint(1, 1), new DataPoint(2, 2), new DataPoint(3, 3) }));
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 Assert.assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testNullRenderer() {
 ChartDataModel model = new ChartDataModel();
 model.setTitle("Test Chart");
 model.setXAxisLabel("X-Axis");
 model.setYAxisLabel("Y-Axis");
 model.setData(Arrays.asList(new DataPoint[] { new DataPoint(1, 1), new DataPoint(2, 2), new DataPoint(3, 3) }));
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 ChartRenderer renderer = null;
 chart.setChartRenderer(renderer);
 Assert.assertThrows(IllegalArgumentException.class, () -> chart.render(null));
 }

 @Test
 public void testRenderChart() {
 Graphics2D g = null;
 assertThrows(NullPointerException.class, () -> chart.render(g));
 g = Mockito.mock(Graphics2D.class);
 chart.render(g);
 // Add assertions for rendered chart elements, e.g. title, legend, axes, data points, etc.
 }
}