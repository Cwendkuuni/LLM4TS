package de.progra.charting.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.Model.ChartDataModel;
import de.progra.charting.Rendering.AbstractChartRenderer;
import de.progra.charting.Rendering.ChartRenderer;
import de.progra.charting.model.RowColorModel;
import de.progra.charting.model.Title;

public class DefaultChartTest {

 private ChartDataModel model;
 private DefaultChart chart;

 @Before
 public void setup() {
 model = new ChartDataModel();
 chart = new DefaultChart(model, "Title");
 }

 @Test
 public void testDefaultChartConstruction() {
 assertNotNull(chart);
 assertTrue(chart instanceof AbstractChart);
 assertTrue(chart.getChartDataModel() instanceof ChartDataModel);
 }

 @Test
 public void testDefaultChartConstructionWithTitle() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Title");
 assertNotNull(chart);
 assertTrue(chart instanceof AbstractChart);
 assertTrue(chart.getChartDataModel() instanceof ChartDataModel);
 assertTrue(chart.getTitle() != null);
 }

 @Test
 public void testDefaultChartConstructionWithTitleAndCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart);
 assertTrue(chart instanceof AbstractChart);
 assertTrue(chart.getChartDataModel() instanceof ChartDataModel);
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testDefaultChartConstructionWithTitleAndCoordinateSystemAndAxes() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "Xaxis", "Yaxis");
 assertNotNull(chart);
 assertTrue(chart instanceof AbstractChart);
 assertTrue(chart.getChartDataModel() instanceof ChartDataModel);
 assertNotNull(chart.getCoordSystem());
 assertTrue(chart.getCoordSystem().getXAxisUnit() != null);
 assertTrue(chart.getCoordSystem().getYAxisUnit() != null);
 }

 @Test
 public void testGetPreferredSize() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Title");
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRender() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Title");
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 assertTrue(chart.getBounds().getWidth() > 0 && chart.getBounds().getHeight() > 0);
 }

 @Test
 public void testRenderWithTitle() {
 ChartDataModel model = new ChartDataModel();
 Title title = new Title("Title");
 DefaultChart chart = new DefaultChart(model, title);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 assertTrue(chart.getBounds().getWidth() > 0 && chart.getBounds().getHeight() > 0);
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 ChartDataModel model = new ChartDataModel();
 Title title = new Title("Title");
 Legend legend = new Legend();
 DefaultChart chart = new DefaultChart(model, title, legend);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 assertTrue(chart.getBounds().getWidth() > 0 && chart.getBounds().getHeight() > 0);
 }

 @Test
 public void testRenderWithTitleAndLegendAndRenderers() {
 ChartDataModel model = new ChartDataModel();
 Title title = new Title("Title");
 Legend legend = new Legend();
 ChartRenderer renderer = new ChartRenderer() {};
 ChartRenderer renderer2 = new ChartRenderer() {};
 Map<String, AbstractChartRenderer> renderers = new HashMap<>();
 renderers.put("renderer1", renderer);
 renderers.put("renderer2", renderer2);
 DefaultChart chart = new DefaultChart(model, title, legend, renderers);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 assertTrue(chart.getBounds().getWidth() > 0 && chart.getBounds().getHeight() > 0);
 }

 @Test
 public void testRenderWithTitleAndLegendAndRenderersAndCoordSystem() {
 ChartDataModel model = new ChartDataModel();
 Title title = new Title("Title");
 Legend legend = new Legend();
 ChartRenderer renderer = new ChartRenderer() {};
 ChartRenderer renderer2 = new ChartRenderer() {};
 Map<String, AbstractChartRenderer> renderers = new HashMap<>();
 renderers.put("renderer1", renderer);
 renderers.put("renderer2", renderer2);
 CoordSystem coordSystem = new CoordSystem(model);
 DefaultChart chart = new DefaultChart(model, title, legend, renderers, coordSystem);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 assertTrue(chart.getBounds().getWidth() > 0 && chart.getBounds().getHeight() > 0);
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 return g;
 }
}