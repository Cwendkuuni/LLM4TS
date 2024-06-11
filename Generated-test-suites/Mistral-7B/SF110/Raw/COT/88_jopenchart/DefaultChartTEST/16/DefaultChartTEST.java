package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.model.RowColorModel;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.render.CoordSystem;
import de.progra.charting.render.Legend;
import de.progra.charting.render.Title;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setup() {
 ChartDataModel model = new ChartDataModel();
 model.addSeries("Series1", Arrays.asList(1D, 2D, 3D, 4D, 5D));
 model.addSeries("Series2", Arrays.asList(10D, 20D, 30D, 40D, 50D));

 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart chart = new DefaultChart(null, null);
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertEmpty(chart.getChartRenderer().values());
 }

 @Test
 public void testChartConstructionWithTitle() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart");
 assertNotNull(chart.getTitle());
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertEmpty(chart.getChartRenderer().values());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystem() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getTitle());
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNotNull(chart.getCoordSystem());
 assertEmpty(chart.getChartRenderer().values());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystemAndAxes() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
 assertNotNull(chart.getTitle());
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNotNull(chart.getCoordSystem());
 assertNotNull(chart.getCoordSystem().getXAxisUnit());
 assertNotNull(chart.getCoordSystem().getYAxisUnit());
 assertEmpty(chart.getChartRenderer().values());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartConstructionWithInvalidCoordinateSystem() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", 2);
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRender() {
 Graphics2D g = null;
 Rectangle2D bounds = new Rectangle2D.Double(0, 0, 600, 600);
 try {
 g = createMockGraphics2D();
 chart.render(g);
 } finally {
 if (g != null) {
 g.dispose();
 }
 }
 // Perform some visual Verification here, as this is a software testing framework, we don't have access to GUI components for visual verification.

 Map<String, AbstractChartRenderer> expectedRenderer = new HashMap<>();
 AbstractChartRenderer renderer1 = new TestChartRenderer("Series1");
 renderer1.setBounds(new Rectangle2D.Double(0, 0, 575, 555));
 expectedRenderer.put(renderer1.getName(), renderer1);
 AbstractChartRenderer renderer2 = new TestChartRenderer("Series2");
 renderer2.setBounds(new Rectangle2D.Double(575, 0, 575, 555));
 expectedRenderer.put(renderer2.getName(), renderer2);

 assertEquals(expectedRenderer, chart.getChartRenderer());
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 Graphics2D g = null;
 Rectangle2D bounds = new Rectangle2D.Double(0, 0, 600, 600);
 try {
 g = createMockGraphics2D();
 Legend legend = new Legend(new RowColorModel(new ChartDataModel()));
 Title title = new Title("Test Chart");
 ChartDataModel chartDataModel = new ChartDataModel();
 chartDataModel.addSeries("Series1", Arrays.asList(1D, 2D, 3D, 4D, 5D));
 chartDataModel.addSeries("Series2", Arrays.asList(10D, 20D, 30D, 40D, 50D));
 DefaultChart chart = new DefaultChart(chartDataModel, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.setLegend(legend);
 chart.setTitle(title);
 chart.render(g);
 } finally {
 if (g != null) {
 g.dispose();
 }
 }
 // Perform some visual Verification here, as this is a software testing framework, we don't have access to GUI components for visual verification.
 }

 private Graphics2D createMockGraphics2D() {
 GraphicsConfiguration gc = null;
 GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
 try {
 gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
 } finally {
 if (ge == null) {
 throw new RuntimeException("Failed to get Graphics Environment");
 }
 }
 return gc.createGraphics();
 }

 private static class TestChartRenderer extends AbstractChartRenderer {
 private final String seriesName;

 TestChartRenderer(String seriesName) {
 this.seriesName = seriesName;
 }

 @Override
 public String getName() {
 return seriesName;
 }
 }
}