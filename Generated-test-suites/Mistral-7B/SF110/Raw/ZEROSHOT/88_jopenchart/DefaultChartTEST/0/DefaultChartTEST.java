package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private DefaultChart createChart(ChartDataModel model, String title, int coord, String xaxis, String yaxis) {
 ChartDataModel mockModel = new ChartDataModel();
 mockModel.setData(Collections.singletonMap("Series1", Arrays.asList(new DataPoint("P1", 1.0), new DataPoint("P2", 2.0))));
 DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
 return chart;
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart);
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultChartConstructionWithNullTitle() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), null, DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testGetPreferredSize() {
 DefaultChart chart = createChart(new ChartDataModel(), "Title", DefaultChart.LINEAR_X_LINEAR_Y, "Xaxis", "Yaxis");
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRenderTitle() {
 DefaultChart chart = createChart(new ChartDataModel(), "Title", DefaultChart.LINEAR_X_LINEAR_Y, "Xaxis", "Yaxis");
 Title title = chart.getTitle();
 String expectedTitle = "Title";
 title.setBounds(new Rectangle2D.Double(10, 10, 100, 20));
 Graphics2D g = Mockito.mock(Graphics2D.class);
 chart.render(g);
 Mockito.verify(g).drawString(expectedTitle, (float)15, (float)(15 + 14));
 }

 @Test
 public void testRenderLegend() {
 DefaultChart chart = createChart(new ChartLineChartDataModel(), "Title", DefaultChart.LINEAR_X_LINEAR_Y, "Xaxis", "Yaxis");
 Legend legend = chart.getLegend();
 String expectedLegendText = "Series1";
 legend.setBounds(new Rectangle2D.Double(10, 10, 100, 20));
 Graphics2D g = Mockito.mock(Graphics2D.class);
 chart.render(g);
 Mockito.verify(g).drawString(expectedLegendText, (float)21, (float)(15 + 14));
 }

 @Test
 public void testRenderChartRenderer() {
 DefaultChart chart = createChart(new ChartLineChartDataModel(), "Title", DefaultChart.LINEAR_X_LINEAR_Y, "Xaxis", "Yaxis");
 LineChartRenderer chartRenderer = new LineChartRenderer();
 chart.setChartRenderer(chartRenderer);
 Graphics2D g = Mockito.mock(Graphics2D.class);
 chart.render(g);
 Map<String, AbstractChartRenderer> rendererMap = chart.getChartRenderer();
 assertEquals(1, rendererMap.size());
 Mockito.verify(g).setColor(Color.BLACK);
 }

 @Test
 public void testRenderCoordinateSystem() {
 DefaultChart chart = createChart(new ChartDataModel(), "Title", DefaultChart.LINEAR_X_LINEAR_Y, "Xaxis", "Yaxis");
 CoordSystem coordSystem = chart.getCoordSystem();
 Graphics2D g = Mockito.mock(Graphics2D.class);
 chart.render(g);
 Mockito.verify(g).setColor(Color.BLACK);
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullGraphics() {
 DefaultChart chart = createChart(new ChartDataModel(), "Title", DefaultChart.LINEAR_X_LINEAR_Y, "Xaxis", "Yaxis");
 chart.render(null);
 }

}