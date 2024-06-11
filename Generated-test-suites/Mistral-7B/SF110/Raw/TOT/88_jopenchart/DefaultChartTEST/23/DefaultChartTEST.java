package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.render.*;
import de.progra.charting.DefaultChart;
import de.progra.charting.AbstractChart;
import de.progra.charting.AbstractChartRenderer;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.Title;
import de.progra.charting.RowColorModel;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel model;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 model.setData(Arrays.asList(new DataPoint[]{
 new DataPoint("A", 1.0),
 new DataPoint("B", 2.0),
 new DataPoint("C", 3.0),
 new DataPoint("D", 4.0)
 }));
 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testEmptyChart() {
 AbstractChart chartWithoutData = new DefaultChart();
 assertNull(chartWithoutData.getTitle());
 assertNull(chartWithoutData.getLegend());
 assertNull(chartWithoutData.getCoordSystem());
 assertFalse(chartWithoutData.getChartRenderer().isEmpty());
 }

 @Test
 public void testChartWithTitle() {
 Title title = new Title("Test Chart Title");
 chart = new DefaultChart(model, title.getText());
 assertEquals(title.getText(), chart.getTitle().getText());
 }

 @Test
 public void testChartWithCoordinateSystem() {
 ChartDataModel modelWithData = new ChartDataModel(model);
 modelWithData.setData(Collections.singletonList(new DataSeries("Series1", Arrays.asList(new DataPoint[]{
 new DataPoint("A", 1.0),
 new DataPoint("B", 2.0),
 new DataPoint("C", 3.0),
 new DataPoint("D", 4.0)
 }))));
 chart = new DefaultChart(modelWithData, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 CoordSystem cs = chart.getCoordSystem();
 assertEquals("", cs.getXAxisUnit());
 assertEquals("", cs.getYAxisUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartWithInvalidCoordinateSystem() {
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "XUnit", "YUnit");
 chart.setCoordSystem(null);
 }

 @Test
 public void testRenderTitleAndLegend() {
 Graphics2D g = null; // Replace this with a mock or a real Graphics2D for rendering
 chart.render(g);
 titleHeightAndLegendWidth();
 titleAndLegendPositions();
 }

 @Test
 public void testRenderChartRenderer() {
 AbstractChartRenderer renderer = new LineChartRenderer();
 chart.getChartRenderer().put("LineRenderer", renderer);
 Graphics2D g = null; // Replace this with a mock or a real Graphics2D for rendering
 chart.render(g);
 chartRendererPosition();
 }

 private void titleHeightAndLegendWidth() {
 int titleHeight = 50; // Replace this with the actual title height from rendering
 int legendWidth = 100; // Replace this with the actual legend width from rendering
 Title title = (Title)chart.getTitle();
 Legend legend = (Legend)chart.getLegend();
 assertEquals(titleHeight, title.getBounds().getHeight());
 assertEquals(legendWidth, legend.getBounds().getWidth());
 }

 private void titleAndLegendPositions() {
 int width = 500;
 int height = 300;
 Title title = new Title("Test Chart Title");
 title.setBounds(new Rectangle(width / 2 - (int)(title.getPreferredSize().getWidth() / 2.0), 0, (int)title.getPreferredSize().getWidth(), (int)title.getPreferredSize().getHeight()));
 Legend legend = new Legend(new RowColorModel(model));
 legend.setBounds(new Rectangle(width - legend.getPreferredSize().getWidth(), (int)(height / 2 - legend.getPreferredSize().getHeight() / 2.0), (int)legend.getPreferredSize().getWidth(), (int)legend.getPreferredSize().getHeight()));
 Title titleInChart = chart.getTitle();
 Legend legendInChart = chart.getLegend();
 assertEquals(title, titleInChart);
 assertEquals(legend, legendInChart);
 }

 private void chartRendererPosition() {
 int width = 500;
 int height = 300;
 int titleHeight = 50;
 int legendWidth = 100;
 int chartRendererHeight = 200;
 CoordSystem cs = new CoordSystem(model);
 AbstractChartRenderer renderer = new LineChartRenderer();
 LineChartRenderer lineChartRenderer = (LineChartRenderer)renderer;
 lineChartRenderer.setBounds(new Rectangle(0, titleHeight, width - legendWidth, height - titleHeight - 5));
 chart.getChartRenderer().put("LineRenderer", renderer);
 Graphics2D g = null; // Replace this with a mock or a real Graphics2D for rendering
 chart.render(g);
 Rectangle bounds = chart.getChartRenderer().get("LineRenderer").getBounds();
 assertEquals(lineChartRenderer.getBounds(), bounds);
 }
}