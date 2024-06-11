package de.progra.charting.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.Model.RowColorModel;
import de.progra.charting.Render.AbstractChartRenderer;
import de.progra.charting.Title;

public class DefaultChartTest {

 // Test constructor with no arguments
 @Test
 public void testDefaultChartConstructor() {
 DefaultChart chart = new DefaultChart();
 assertNull(chart.getChartDataModel());
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 // Test constructor with ChartDataModel and title
 @Test
 public void testDefaultChartConstructorWithModelAndTitle() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Test Title");
 assertNotNull(chart.getChartDataModel());
 assertEquals("Test Title", chart.getTitle().getText());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 // Test constructor with ChartDataModel, title, and LINEAR_X_LINEAR_Y coordinate system
 @Test
 public void testDefaultChartConstructorWithModelAndTitleAndLinearCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Test Title", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 // Test constructor with ChartDataModel, title, LINEAR_X_LINEAR_Y coordinate system, xaxis, and yaxis
 @Test
 public void testDefaultChartConstructorWithModelAndTitleAndLinearCoordinateSystemAndAxisLabels() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Test Title", DefaultChart.LINEAR_X_LINEAR_Y, "xaxis", "yaxis");
 assertNotNull(chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 assertNotNull(chart.getCoordSystem().getXAxisUnit());
 assertNotNull(chart.getCoordSystem().getYAxisUnit());
 assertEquals("xaxis", chart.getCoordSystem().getXAxisUnit());
 assertEquals("yaxis", chart.getCoordSystem().getYAxisUnit());
 }

 // Test getPreferredSize method
 @Test
 public void testGetPreferredSize() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Title");
 assertNull(chart.getPreferredSize());
 }

 // Test render method with no title or legend
 @Test
 public void testRenderWithoutTitleOrLegend() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Title");
 Graphics2D g = getMockGraphics2D();
 chart.render(g);
 }

 // Test render method with title but no legend
 @Test
 public void testRenderWithTitleButNoLegend() {
 Title title = new Title("Test Title");
 DefaultChart chart = new DefaultChart(new ChartDataModel(), title);
 Graphics2D g = getMockGraphics2D();
 chart.render(g);
 }

 // Test render method with title and legend
 @Test
 public void testRenderWithTitleAndLegend() {
 Title title = new Title("Test Title");
 Legend legend = new Legend();
 DefaultChart chart = new DefaultChart(new ChartDataModel(), title, legend);
 Graphics2D g = getMockGraphics2D();
 chart.render(g);
 }

 // Test render method with title, legend, and chart renderers
 @Test
 public void testRenderWithTitleAndLegendAndChartRenderers() {
 Title title = new Title("Test Title");
 Legend legend = new Legend();
 AbstractChartRenderer renderer1 = getMockChartRenderer();
 AbstractChartRenderer renderer2 = getMockChartRenderer();
 DefaultChart chart = new DefaultChart(new ChartDataModel(), title, legend, Collections.singletonMap("TestRenderer1", renderer1), Collections.singletonMap("TestRenderer2", renderer2));
 Graphics2D g = getMockGraphics2D();
 chart.render(g);
 }

 // Test render method with title, legend, chart renderers, and coordinate system
 @Test
 public void testRenderWithTitleAndLegendAndChartRenderersAndCoordinateSystem() {
 Title title = new Title("Test Title");
 Legend legend = new Legend();
 AbstractChartRenderer renderer1 = getMockChartRenderer();
 AbstractChartRenderer renderer2 = getMockChartRenderer();
 CoordSystem coordinateSystem = getMockCoordinateSystem();
 DefaultChart chart = new DefaultChart(new ChartDataModel(), title, legend, Collections.singletonMap("TestRenderer1", renderer1), Collections.singletonMap("TestRenderer2", renderer2), coordinateSystem);
 Graphics2D g = getMockGraphics2D();
 chart.render(g);
 }

 private Graphics2D getMockGraphics2D() {
 return mock(Graphics2D.class);
 }

 private Title getMockTitle() {
 return mock(Title.class);
 }

 private Legend getMockLegend() {
 return mock(Legend.class);
 }

 private CoordSystem getMockCoordinateSystem() {
 return mock(CoordSystem.class);
 }

 private AbstractChartRenderer getMockChartRenderer() {
 return mock(AbstractChartRenderer.class);
 }

}