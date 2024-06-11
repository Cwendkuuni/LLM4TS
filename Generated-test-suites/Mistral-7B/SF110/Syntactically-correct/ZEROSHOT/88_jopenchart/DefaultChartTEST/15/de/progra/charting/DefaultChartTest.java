package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.render.*;
import de.progra.charting.DefaultChart;
import de.progra.charting.AbstractChart;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.Legend;
import de.progra.charting.CoordSystem;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 model.setData(Arrays.asList(new DataPoint("A", 1), new DataPoint("B", 2), new DataPoint("C", 3), new DataPoint("D", 4)));
 model.setXAxisLabel("X-axis");
 model.setYAxisLabel("Y-axis");

 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testDefaultChartConstruction() {
 Assert.assertNotNull(chart);
 Assert.assertNotNull(chart.getTitle());
 Assert.assertNotNull(chart.getLegend());
 Assert.assertNotNull(chart.getCoordSystem());
 Assert.assertEquals(0, chart.getBounds().getWidth());
 Assert.assertEquals(0, chart.getBounds().getHeight());
 }

 @Test
 public void testDefaultChartConstructionWithTitle() {
 Assert.assertNotNull(chart);
 Assert.assertNotNull(chart.getTitle());
 Assert.assertNotNull(chart.getLegend());
 Assert.assertNotNull(chart.getCoordSystem());
 Assert.assertEquals(0, chart.getBounds().getWidth());
 Assert.assertEquals(0, chart.getBounds().getHeight());
 Assert.assertEquals("Test Chart", chart.getTitle().getText());
 }

 @Test
 public void testDefaultChartConstructionWithTitleAndCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 model.setData(Arrays.asList(new DataPoint("A", 1), new DataPoint("B", 2), new DataPoint("C", 3), new DataPoint("D", 4)));
 model.setXAxisLabel("X-axis");
 model.setYAxisLabel("Y-axis");

 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-axis", "Y-axis");

 Assert.assertNotNull(chart);
 Assert.assertNotNull(chart.getTitle());
 Assert.assertNotNull(chart.getLegend());
 Assert.assertNotNull(chart.getCoordSystem());
 Assert.assertEquals("Test Chart", chart.getTitle().getText());
 Assert.assertEquals("X-axis", chart.getCoordSystem().getXAxisUnit());
 Assert.assertEquals("Y-axis", chart.getCoordSystem().getYAxisUnit());
 }

 @Test
 public void testGetPreferredSize() {
 chart.setBounds(new Rectangle(0, 0, 640, 480));
 chart.render(new MockGraphics2D()); // render to check dimensions

 Assert.assertEquals(640, chart.getBounds().getWidth());
 Assert.assertEquals(480, chart.getBounds().getHeight());
 }

 @Test
 public void testRender() {
 ChartDataModel model = new ChartDataModel();
 model.setData(Arrays.asList(new DataPoint("A", 1), new DataPoint("B", 2), new DataPoint("C", 3), new DataPoint("D", 4)));
 model.setXAxisLabel("X-axis");
 model.setYAxisLabel("Y-axis");

 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.setBounds(new Rectangle(0, 0, 640, 480));

 Graphics2D g = new MockGraphics2D();
 chart.render(g);

 assertRenderComponents(g, 5, 4, 640, 476, 0, 0);
 }

 @Test
 public void testRenderWithTitle() {
 ChartDataModel model = new ChartDataModel();
 model.setData(Arrays.asList(new DataPoint("A", 1), new DataPoint("B", 2), new DataPoint("C", 3), new DataPoint("D", 4)));
 model.setXAxisLabel("X-axis");
 model.setYAxisLabel("Y-axis");

 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-axis", "Y-axis");
 chart.setBounds(new Rectangle(0, 0, 640, 480));

 Graphics2D g = new MockGraphics2D();
 chart.render(g);

 assertRenderComponents(g, 6, 4, 640, 472, 0, 0);
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 ChartDataModel model = new ChartDataModel();
 model.setData(Arrays.asList(new DataPoint("A", 1), new DataPoint("B", 2), new DataPoint("C", 3), new DataPoint("D", 4)));
 model.setXAxisLabel("X-axis");
 model.setYAxisLabel("Y-axis");

 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-axis", "Y-axis");
 chart.setBounds(new Rectangle(0, 0, 640, 480));
 ChartRenderer cr = new SimpleLineChartRenderer();
 chart.addChartRenderer(cr);

 Graphics2D g = new MockGraphics2D();
 chart.render(g);

 assertRenderComponents(g, 7, 5, 640, 470, 0, 0);
 }

 @Test
 public void testRenderWithTitleAndLegendAndCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 model.setData(Arrays.asList(new DataPoint("A", 1), new DataPoint("B", 2), new DataPoint("C", 3), new DataPoint("D", 4)));
 model.setXAxisLabel("X-axis");
 model.setYAxisLabel("Y-axis");

 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-axis", "Y-axis");
 chart.setBounds(new Rectangle(0, 0, 640, 480));
 ChartRenderer cr = new SimpleLineChartRenderer();
 chart.addChartRenderer(cr);

 graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

 Graphics2D g = new MockGraphics2D();
 chart.render(g);

 assertRenderComponents(g, 7, 5, 640, 470, 0, 0);
 }

 private void assertRenderComponents(Graphics2D g, int expectedComponents, int expectedRows, int expectedWidth, int expectedHeight, int x, int y) {
 List<AbstractChartRenderer> chartRenderers = chart.getChartRenderer().values();
 int titleHeight = 0;
 int legendWidth = 0;
 int titleX = 0;
 int titleY = 0;
 int legendX = 0;
 int legendY = 0;

 if (chart.getTitle() != null) {
 Title title = (Title)chart.getTitle();
 title.setBounds(new Rectangle(x, y, expectedWidth / 2, titleHeight = (int)title.getPreferredSize().getHeight()));
 title.render(g);
 x += title.getBounds().getWidth();
 }

 if (chart.getLegend() != null) {
 Legend legend = (Legend)chart.getLegend();
 legend.setBounds(new Rectangle(x, y, expectedWidth - titleX - legendWidth, legendHeight = (int)legend.getPreferredSize().getHeight()));
 legend.render(g);
 x += legend.getBounds().getWidth();
 legendWidth = legend.getBounds().getWidth();
 y += titleHeight + legendHeight;
 }

 int chartX = x;
 int chartY = y;
 int chartWidth = expectedWidth - titleX - legendWidth;
 int chartHeight = expectedHeight - titleHeight - legendHeight;

 for (AbstractChartRenderer renderer : chartRenderers) {
 renderer.setBounds(new Rectangle(chartX, chartY, chartWidth, chartHeight));
 renderer.render(g);
 chartY += renderer.getPreferredSize().getHeight() + 5;
 }

 int coordSystemX = 0;
 int coordSystemY = 0;
 int coordSystemWidth = chartWidth;
 int coordSystemHeight = chartHeight;
 if (chart.getCoordSystem() != null) {
 CoordSystem coordSystem = (CoordSystem)chart.getCoordSystem();
 coordSystem.setBounds(new Rectangle(coordSystemX, coordSystemY, coordSystemWidth, coordSystemHeight));
 coordSystem.render(g);
 }

 assertEquals(expectedComponents, g.getRenderCount());
 assertEquals(expectedRows, chartRenderers.size());
 assertEquals(expectedWidth, chart.getBounds().getWidth());
 assertEquals(expectedHeight, chart.getBounds().getHeight());
 }

 private static class MockGraphics2D extends Graphics2D {
 @Override
 public void fillRect(int x, int y, int width, int height) {
 // empty implementation
 }

 @Override
 public void setColor(Color color) {
 // empty implementation
 }

 @Override
 public void setRenderingHint(RenderingHints key, RenderingHints value) {
 // empty implementation
 }

 @Override
 public void dispose() {
 // empty implementation
 }

 @Override
 public int getRenderCount() {
 return 0;
 }

 @Override
 public void translateTransform(float x, float y) {
 // empty implementation
 }

 @Override
 public void setFont(Font font) {
 // empty implementation
 }

 @Override
 public void drawString(String str, float x, float y) {
 // empty implementation
 }

 @Override
 public void drawLine(int x1, int y1, int x2, int y2) {
 // empty implementation
 }

 @Override
 public void drawPolygon(Polygon p) {
 // empty implementation
 }

 @Override
 public void drawRect(int x, int y, int width, int height) {
 // empty implementation
 }

 @Override
 public void drawOval(int x, int y, int width, int height) {
 // empty implementation
 }

 @Override
 public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
 // empty implementation
 }

 @Override
 public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
 // empty implementation
 }
 }
}