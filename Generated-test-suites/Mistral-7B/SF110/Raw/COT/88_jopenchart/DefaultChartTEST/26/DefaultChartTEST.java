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

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private ChartDataModel model;
 private DefaultChart chart;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 model.setTitle("Test Chart");
 model.addSeries("Series 1", Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
 model.addSeries("Series 2", Arrays.asList(6.0, 7.0, 8.0, 9.0, 10.0));
 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart chart = new DefaultChart();
 assertNull(chart.getChartDataModel());
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertEmpty(chart.getChartRenderer().values());
 }

 @Test
 public void testChartConstructionWithTitle() {
 assertEquals("Test Chart", chart.getTitle().getText());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystem() {
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystemAndAxisLabels() {
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "xAxisLabel", "yAxisLabel");
 assertEquals("xAxisLabel", chart.getCoordSystem().getXAxisUnit());
 assertEquals("yAxisLabel", chart.getCoordSystem().getYAxisUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartConstructionWithInvalidCoordinateSystem() {
 chart = new AssertionError("Invalid coordinate system value").withMessage("Invalid coordinate system value: " + 2);
 chart = new DefaultChart(model, "Test Chart", 2);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRender() {
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);

 // Check empty chart
 assertArrayEquals(new int[] { 0, 0, model.getWidth(), model.getHeight() }, new int[] { 0, 0, chart.getBounds().width, chart.getBounds().height });

 // Check title rendering
 chart.getTitle().setText("Test Chart with Title");
 chart.setBounds(new Rectangle(0, 0, 600, 600));
 chart.render(g);
 Rectangle titleRect = new Rectangle(chart.getTitle().getBounds());
 titleRect.x -= 300; // move title to the left for comparison
 assertEquals(titleRect, new Rectangle(new Point(300, 0), new Dimension(chart.getTitle().getBounds().width, chart.getTitle().getBounds().height)));

 // Check legend rendering
 chart.getLegend().setText("Test Chart with Legend");
 chart.setBounds(new Rectangle(0, 0, 800, 600));
 chart.render(g);
 Rectangle legendRect = new Rectangle(chart.getLegend().getBounds());
 legendRect.x -= 700; // move legend to the right for comparison
 assertEquals(legendRect, new Rectangle(new Point(700, 300), new Dimension(chart.getLegend().getBounds().width, chart.getLegend().getBounds().height)));

 // Check coordinate system rendering
 chart.setBounds(new Rectangle(0, 0, 1000, 800));
 chart.render(g);
 Rectangle coordSystemRect = new Rectangle(chart.getCoordSystem().getBounds());
 assertEquals(coordSystemRect, new Rectangle(new Point(0, 330), new Dimension(1000, 470)));

 // Check series rendering
 ChartRenderer seriesRenderer1 = mock(LineChartRenderer.class);
 ChartRenderer seriesRenderer2 = mock(AreaChartRenderer.class);
 chart.getChartRenderer().put("Series 1", seriesRenderer1);
 chart.getChartRenderer().put("Series 2", seriesRenderer2);
 chart.setBounds(new Rectangle(0, 0, 1200, 1200));
 chart.render(g);

 List<AbstractChartRenderer> renderers = Arrays.asList(seriesRenderer1, seriesRenderer2);
 int x = chart.getCoordSystem().getXAxisUnit().getValue(model.getXAxisMinimum());
 int y = chart.getCoordSystem().getYAxisUnit().getValue(model.getYAxisMinimum());
 for (int i = 0; i < model.getSeriesCount(); i++) {
 AbstractChartRenderer renderer = chart.getChartRenderer().get(model.getSeriesName(i));
 renderer.renderSeries(g, model, x, y, model.getSeriesData(i), chart.getTitle(), chart.getLegend());
 x += chart.getCoordSystem().getXAxisUnit().getValue(1);
 }

 for (AbstractChartRenderer renderer : renderers) {
 Rectangle rendererRect = new Rectangle(renderer.getBounds());
 assertEquals(rendererRect, new Rectangle(new Point(0, 330 + i * (seriesRenderer1.getHeight() + 5)), new Dimension(1200, seriesRenderer1.getHeight())));
 }
 }

}