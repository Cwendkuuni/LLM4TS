package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelData;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel model;
 private AbstractChartRenderer renderer;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 model.addData(new ModelData("Test1", 1d, 2d, 1));
 model.addData(new ModelData("Test2", 2d, 1d, 2));
 chart = new DefaultChart(model, "Test title", DefaultChart.LINEAR_X_LINEAR_Y);
 renderer = new StubChartRenderer();
 chart.setChartRenderer(Collections.singletonMap("testKey", renderer));
 }

 @Test
 public void testDefaultConstruction() {
 DefaultChart chart = new DefaultChart();
 assertNull(chart.getChartDataModel());
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer().values().iterator().next());
 }

 @Test
 public void testConstructionWithModelAndTitle() {
 assertSame(model, chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertEmpty(chart.getChartRenderer().values());
 }

 @Test
 public void testConstructionWithModelAndTitleAndCoordinateSystem() {
 assertSame(model, chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 assertEmpty(chart.getChartRenderer().values());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTitle() {
 new DefaultChart(model, null, DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRenderTitle() {
 Title title = chart.getTitle();
 Graphics2D g = createGraphics2D(10, 10);
 chart.render(g);
 assertRenderTitle(title, g, 5, 5);
 }

 @Test
 public void testRenderLegend() {
 Legend legend = chart.getLegend();
 Graphics2D g = createGraphics2D(10, 10, 100, 100);
 chart.render(g);
 assertRenderLegend(legend, g, 95, 5, 10);
 }

 @Test
 public void testRenderChartData() {
 Graphics2D g = createGraphics2D(10, 10, 100, 100);
 chart.render(g);
 assertRenderChartData(renderer, g, 5, 5, 90, 5);
 }

 @Test
 public void testRenderCoordinateSystem() {
 CoordSystem coordSystem = chart.getCoordSystem();
 Graphics2D g = createGraphics2D(10, 10, 100, 100);
 chart.render(g);
 assertRenderCoordinateSystem(coordSystem, g, 5, 5, 90, 5, 95, 5, 10);
 }

 @Test
 public void testRenderChartWithAllComponents() {
 Graphics2D g = createGraphics2D(10, 10, 100, 100);
 chart.render(g);
 assertRenderTitle(chart.getTitle(), g, 5, 5);
 assertRenderLegend(chart.getLegend(), g, 95, 5, 10);
 assertRenderChartData(renderer, g, 5, 5, 90, 5);
 assertRenderCoordinateSystem(chart.getCoordSystem(), g, 5, 5, 90, 5, 95, 5, 10);
 }

 private void assertRenderTitle(Title title, Graphics2D g, int x, int y) {
 assertEquals("Test title", title.getText());
 assertTrue(g.getColor().equals(Color.BLACK));
 Rectangle2D titleBounds = title.getBounds();
 assertTrue(titleBounds.getX() == x && titleBounds.getY() == y);
 assertTrue(titleBounds.getWidth() > 0 && titleBounds.getHeight() > 0);
 }

 private void assertRenderLegend(Legend legend, Graphics2D g, int x, int y, int width) {
 assertEquals("Test1", legend.getData(0).getName());
 assertTrue(g.getColor().equals(Color.BLACK));
 Rectangle2D legendBounds = legend.getBounds();
 assertTrue(legendBounds.getX() == x && legendBounds.getY() == y);
 assertTrue(legendBounds.getWidth() == width);
 }

 private void assertRenderChartData(AbstractChartRenderer renderer, Graphics2D g, int x, int y, int width, int height) {
 assertTrue(g.getColor().equals(Color.WHITE));
 Rectangle2D bounds = renderer.getBounds();
 assertTrue(bounds.getX() == x && bounds.getY() == y && bounds.getWidth() == width && bounds.getHeight() == height);
 }

 private void assertRenderCoordinateSystem(CoordSystem coordSystem, Graphics2D g, int x, int y, int width, int height, int xAxisWidth, int yAxisWidth, int legendWidth) {
 // TODO: Implement test for rendering coordinate system
 }

 private Graphics2D createGraphics2D(int width, int height) {
 return createGraphics2D(width, height, width, height);
 }

 private Graphics2D createGraphics2D(int width, int height, int titleWidth, int titleHeight) {
 return createGraphics2D(width, height, titleWidth, titleHeight, width, height - titleHeight);
 }

 private Graphics2D createGraphics2D(int width, int height, int titleWidth, int titleHeight, int legendWidth, int legendHeight) {
 GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenGraphics().getDevice().getDefaultConfiguration();
 return gc.createCompatibleGraphics(width, height);
 }
}

class StubChartRenderer implements AbstractChartRenderer {

 @Override
 public void render(Graphics2D g) {
 // TODO: Implement stub for rendering chart data
 }

 @Override
 public Rectangle2D getBounds() {
 return new Rectangle2D.Double(0, 0, 100, 100);
 }
}