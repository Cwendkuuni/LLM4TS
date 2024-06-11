package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelDataSeries;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.DefaultChartRenderer;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 model.addSeries(new ModelDataSeries("Series1", Arrays.asList(1d, 2d, 3d, 4d, 5d)));
 model.addSeries(new ModelDataSeries("Series2", Arrays.asList(6d, 7d, 8d, 9d, 10d)));
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testDefaultChartCreation() {
 assertNotNull(chart);
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testInvalidCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 chart = new DefaultChart(model, "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM);
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testRenderTitleAndLegend() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 Title title = chart.getTitle();
 Legend legend = chart.getLegend();
 assertNotNull(title);
 assertNotNull(legend);
 }

 @Test
 public void testRenderChartWithData() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 assertEquals(Color.white, g.getColor());
 assertNotNull(chart.getCoordSystem());
 List<AbstractChartRenderer> renderers = new ArrayList<>(chart.getChartRenderer().values());
 for (AbstractChartRenderer renderer : renderers) {
 renderer.render(g);
 }
 }

 @Test
 public void testRenderChartWithEmptyData() {
 ChartDataModel model = new ChartDataModel();
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 assertNotNull(chart.getCoordSystem());
 List<AbstractChartRenderer> renderers = new ArrayList<>(chart.getChartRenderer().values());
 for (AbstractChartRenderer renderer : renderers) {
 assertFalse(renderer.isRender());
 }
 }

 @Test
 public void testRenderChartWithNullTitle() {
 ChartDataModel model = new ChartDataModel();
 model.setTitle(null);
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 assertNull(chart.getTitle());
 }

 @Test
 public void testRenderChartWithNullLegend() {
 ChartDataModel model = new ChartDataModel();
 model.setLegend(null);
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 assertNull(chart.getLegend());
 }

 @Test
 public void testRenderChartWithNullCoordSystem() {
 ChartDataModel model = new ChartDataModel();
 model.setXAxisUnit(null);
 model.setYAxisUnit(null);
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 assertNull(chart.getCoordSystem());
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 return g;
 }
}