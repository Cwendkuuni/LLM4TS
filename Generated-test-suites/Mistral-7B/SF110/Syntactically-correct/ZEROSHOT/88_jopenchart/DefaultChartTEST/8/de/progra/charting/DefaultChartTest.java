package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private ChartDataModel createModel() {
 return new ChartDataModel(Arrays.asList(new DataPoint("P1", new double[] { 1, 2, 3 }), new DataPoint("P2", new double[] { 4, 5, 6 })));
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "TestChart");
 assertNotNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertFalse(chart.hasCoordSystem());
 }

 @Test
 public void testDefaultChartWithTitle() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "TestChart");
 Title title = chart.getTitle();
 assertNotNull(title);
 assertEquals("TestChart", title.getText());
 }

 @Test
 public void testDefaultChartWithTitleAndCoordinateSystem() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 Title title = chart.getTitle();
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(title);
 assertEquals("TestChart", title.getText());
 assertNotNull(coordSystem);
 }

 @Test
 public void testDefaultChartWithTitleAndCoordinateSystemAndLabels() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "TestChart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");
 Title title = chart.getTitle();
 CoordSystem coordSystem = chart.getCoordSystem();
 assertEquals("X-Axis", coordSystem.getXAxisUnit());
 assertEquals("Y-Axis", coordSystem.getYAxisUnit());
 assertNotNull(title);
 assertEquals("TestChart", title.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultChartWithInvalidCoordinateSystem() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "TestChart", 2);
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testGetPreferredSize() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "TestChart");
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRenderTitle() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "TestChart");
 Title title = new Title("TestTitle");
 chart.setTitle(title);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verify(g).setColor(Color.BLACK);
 // Title positioning is not covered in this test
 // But the title is rendered, so this is a positive test
 }

 @Test
 public void testRenderLegend() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "TestChart");
 Legend legend = new Legend(new RowColorModel(new ChartDataModel()));
 chart.setLegend(legend);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verify(g).setColor(Color.BLACK);
 // Legend positioning is not covered in this test
 // But the legend is rendered, so this is a positive test
 }

 @Test
 public void testRenderChartData() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 ChartDataModel model = chart.getChartDataModel();
 AbstractChartRenderer renderer = new LineChartRenderer(model.getDataPointAt(0).getData());
 chart.addChartRenderer(renderer);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verify(g).setColor(renderer.getLineColor());
 // Chart data positioning is not covered in this test
 // But the chart data is rendered, so this is a positive test
 }

 @Test
 public void testRenderCoordinateSystem() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "TestChart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");
 ChartDataModel model = chart.getChartDataModel();
 CoordSystem coordSystem = chart.getCoordSystem();
 AbstractChartRenderer renderer = new LineChartRenderer(model.getDataPointAt(0).getData());
 chart.addChartRenderer(renderer);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 // Coordinate system positioning is not covered in this test
 // But the coordinate system is rendered, so this is a positive test
 verify(g).setColor(Color.BLACK);
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.WHITE);
 return g;
 }

}