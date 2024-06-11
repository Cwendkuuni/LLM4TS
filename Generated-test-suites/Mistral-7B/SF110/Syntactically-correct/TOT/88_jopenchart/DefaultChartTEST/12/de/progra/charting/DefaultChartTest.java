package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private ChartDataModel chartDataModel;
 private DefaultChart chart;

 @Before
 public void setup() {
 chartDataModel = new ChartDataModel();
 chartDataModel.addSeries("Series1", Arrays.asList(new DataPoint(1, 2), new DataPoint(2, 4), new DataPoint(3, 1)));
 chart = new DefaultChart(chartDataModel, "Test Chart");
 }

 @Test
 public void testDefaultChartConstruction() {
 // Test empty construction
 DefaultChart emptyChart = new DefaultChart();
 assertNull(emptyChart.getTitle());
 assertNull(emptyChart.getLegend());
 assertNull(emptyChart.getCoordSystem());
 // Test construction with title
 DefaultChart titleChart = new DefaultChart(chartDataModel, "Test Chart");
 assertEquals("Test Chart", titleChart.getTitle().getText());
 assertNotNull(titleChart.getLegend());
 assertNull(titleChart.getCoordSystem());
 // Test construction with title and linear coordinate system
 DefaultChart coordChart = new DefaultChart(chartDataModel, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertEquals("Test Chart", titleChart.getTitle().getText());
 assertNotNull(titleChart.getLegend());
 assertNotNull(titleChart.getCoordSystem());
 }

 @Test
 public void testSetTitle() {
 chart.setTitle(new Title("New Title"));
 assertEquals("New Title", chart.getTitle().getText());
 }

 @Test
 public void testSetLegend() {
 chart.setLegend(new Legend(chart.getRowColorModel()));
 chart.getLegend().setBackgroundColor(Color.RED);
 assertNotNull(chart.getLegend());
 assertSame(chart.getLegend().getBackgroundColor(), Color.RED);
 }

 @Test
 public void testSetCoordSystem() {
 chart = new DefaultChart(chartDataModel, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getCoordSystem());
 chart.setCoordSystem(null);
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testRenderTitle() {
 final int width = 300;
 final int height = 200;
 ChartRenderer chartRenderer = new StubChartRenderer();
 chartRenderer.setBounds(new Rectangle(0, 0, width, height));
 chart.setChartRendererMap(Collections.singletonMap(chartRenderer.getClass(), chartRenderer));
 chart.setBounds(new Rectangle(0, 0, width, height));
 chart.render(mock(Graphics2D.class));
 assertTrue(chart.getTitle().isRendered());
 }

 @Test
 public void testRenderLegend() {
 final int width = 300;
 final int height = 200;
 ChartRenderer chartRenderer = new StubChartRenderer();
 chartRenderer.setBounds(new Rectangle(0, 0, width, height));
 chart.setChartRendererMap(Collections.singletonMap(chartRenderer.getClass(), chartRenderer));
 chart.setBounds(new Rectangle(0, 0, width, height));
 chart.render(mock(Graphics2D.class));
 assertTrue(chart.getLegend().isRendered());
 }

 @Test
 public void testRenderCoordSystem() {
 final int width = 300;
 final int height = 200;
 ChartRenderer chartRenderer = new StubChartRenderer();
 chartRenderer.setBounds(new Rectangle(0, 0, width, height));
 chart.setChartRendererMap(Collections.singletonMap(chartRenderer.getClass(), chartRenderer));
 chart.setBounds(new Rectangle(0, 0, width, height));
 chart.render(mock(Graphics2D.class));
 assertTrue(chart.getCoordSystem().isRendered());
 }

 @Test
 public void testRenderChartRenderer() {
 final int width = 300;
 final int height = 200;
 ChartRenderer chartRenderer = new StubChartRenderer();
 chartRenderer.setBounds(new Rectangle(0, 0, width, height));
 chart.setChartRendererMap(Collections.singletonMap(chartRenderer.getClass(), chartRenderer));
 chart.setBounds(new Rectangle(0, 0, width, height));
 chart.render(mock(Graphics2D.class));
 assertTrue(chart.getChartRenderer().values().iterator().next().isRendered());
 }
}

class StubChartRenderer extends AbstractChartRenderer {

 @Override
 public void render(Graphics2D g) {
 // Empty implementation
 }

 @Override
 public Rectangle getBounds() {
 return new Rectangle(0, 0, 1, 1);
 }
}