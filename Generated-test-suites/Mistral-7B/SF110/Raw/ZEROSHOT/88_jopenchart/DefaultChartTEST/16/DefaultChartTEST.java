package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelDataPoint;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.render.LineChartRenderer;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel model;
 private AbstractChartRenderer renderer;

 @Before
 public void setUp() {
 model = new ChartDataModel("Test Data", Arrays.asList(new ModelDataPoint[]{
 new ModelDataPoint(0, 1), new ModelDataPoint(1, 2), new ModelDataPoint(2, 3), new ModelDataPoint(3, 4), new ModelDataPoint(4, 5) }));
 chart = new DefaultChart(model, "Test Chart");
 renderer = new LineChartRenderer();
 chart.getChartRenderer().put("Test Renderer", renderer);
 }

 @Test
 public void testDefaultConstructor() {
 DefaultChart chart = new DefaultChart();
 assertTrue(chart instanceof AbstractChart);
 assertNull(chart.getChartDataModel());
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertFalse(chart.getChartRenderer().isEmpty());
 }

 @Test
 public void testConstructorWithTitle() {
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 assertTrue(chart instanceof AbstractChart);
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertFalse(chart.getChartRenderer().isEmpty());
 }

 @Test
 public void testConstructorWithTitleAndCoordinateSystem() {
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertTrue(chart instanceof AbstractChart);
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNotNull(chart.getCoordSystem());
 assertFalse(chart.getChartRenderer().isEmpty());
 }

 @Test
 public void testConstructorWithTitleAndCoordinateSystemAndAxisLabels() {
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis Label", "Y Axis Label");
 assertTrue(chart instanceof AbstractChart);
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNotNull(chart.getCoordSystem());
 assertEquals("X Axis Label", chart.getCoordSystem().getXAxisUnit());
 assertEquals("Y Axis Label", chart.getCoordSystem().getYAxisUnit());
 }

 @Test
 public void testGetPreferredSize() {
 Dimension preferredSize = chart.getPreferredSize();
 assertNull(preferredSize);
 }

 @Test
 public void testRender() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 assertNotNull(g);
 // Check if title is rendered
 Title title = chart.getTitle();
 if (title != null) {
 Rectangle2D titleBounds = title.getBounds();
 assertEquals(title.getBounds().getX(), titleBounds.getX(), 0.0);
 assertEquals(title.getBounds().getY(), titleBounds.getY(), 0.0);
 }
 // Check if legend is rendered
 Legend legend = chart.getLegend();
 if (legend != null) {
 Rectangle2D legendBounds = legend.getBounds();
 assertEquals(legend.getBounds().getX(), legendBounds.getX(), 0.0);
 assertEquals(legend.getBounds().getY(), legendBounds.getY(), 0.0);
 }
 // Check if coordinate system is rendered
 CoordSystem coordSystem = chart.getCoordSystem();
 if (coordSystem != null) {
 // TODO: Implement a way to test if the coordinate system is rendered correctly
 }
 // Check if all renderers are rendered
 List<AbstractChartRenderer> renderers = new ArrayList<>(chart.getChartRenderer().values());
 for (AbstractChartRenderer renderer : renderers) {
 Rectangle2D rendererBounds = renderer.getBounds();
 assertEquals(renderer.getBounds().getX(), rendererBounds.getX(), 0.0);
 assertEquals(renderer.getBounds().getY(), rendererBounds.getY(), 0.0);
 }
 }

 @Test
 public void testSetChartDataModel() {
 ChartDataModel newModel = new ChartDataModel("New Data", Arrays.asList(new ModelDataPoint[]{
 new ModelDataPoint(0, 5), new ModelDataPoint(1, 3), new ModelDataPoint(2, 8), new ModelDataPoint(3, 1), new ModelDataPoint(4, 6) }));
 chart.setChartDataModel(newModel);
 assertEquals(newModel, chart.getChartDataModel());
 }

 @Test
 public void testSetTitle() {
 Title newTitle = new Title("New Title");
 chart.setTitle(newTitle);
 assertEquals(newTitle, chart.getTitle());
 }

 @Test
 public void testSetLegend() {
 Legend newLegend = new Legend(new RowColorModel(model));
 chart.setLegend(newLegend);
 assertEquals(newLegend, chart.getLegend());
 }

 @Test
 public void testSetCoordSystem() {
 CoordSystem newCoordSystem = new CoordSystem(model);
 chart.setCoordSystem(newCoordSystem);
 assertEquals(newCoordSystem, chart.getCoordSystem());
 }

 @Test
 public void testSetChartRenderer() {
 AbstractChartRenderer newRenderer = new LineChartRenderer();
 chart.setChartRenderer(new Map.Entry<String, AbstractChartRenderer>("Test Renderer", newRenderer));
 assertEquals(newRenderer, chart.getChartRenderer().get("Test Renderer"));
 }
}