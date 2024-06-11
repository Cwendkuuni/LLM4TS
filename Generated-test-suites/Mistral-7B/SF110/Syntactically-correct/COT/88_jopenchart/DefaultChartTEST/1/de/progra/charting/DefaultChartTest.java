package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 model.setTitle("Test Chart");
 ChartDataSeries series1 = new ChartDataSeries();
 series1.setName("Series 1");
 series1.getData().add(new ChartDataPoint(1, 1));
 series1.getData().add(new ChartDataPoint(2, 2));
 series1.getData().add(new ChartDataPoint(3, 3));
 model.setSeries(Collections.singletonList(series1));
 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getTitle());
 assertNull(defaultChart.getLegend());
 assertNull(defaultChart.getCoordSystem());
 assertNull(defaultChart.getChartRenderer());
 }

 @Test
 public void testChartConstructionWithTitle() {
 ChartDataModel model = new ChartDataModel();
 model.setTitle("Test Chart");
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 assertEquals("Test Chart", chart.getTitle().getText());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 model.setTitle("Test Chart");
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystemAndAxisLabels() {
 ChartDataModel model = new ChartDataModel();
 model.setTitle("Test Chart");
 model.setXaxisLabel("X-axis");
 model.setYaxisLabel("Y-axis");
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-axis", "Y-axis");
 assertEquals("X-axis", chart.getCoordSystem().getXAxisUnit());
 assertEquals("Y-axis", chart.getCoordSystem().getYAxisUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartConstructionWithInvalidCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 model.setTitle("Test Chart");
 DefaultChart chart = new DefaultChart(model, "Test Chart", 2);
 fail("Expected IllegalArgumentException, but no exception was thrown.");
 }

 @Test
 public void testGetPreferredSize() {
 Rectangle expectedBounds = new Rectangle(10, 10, 300, 200);
 assertEquals(expectedBounds, chart.getPreferredSize());
 }

 @Test
 public void testRender() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.WHITE);
 chart.render(g);
 verify(g, times(1)).fillRect(0, 0, 300, 200);
 verify(g, times(1)).setColor(Color.BLACK);
 }

 @Test
 public void testRenderWithTitle() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.WHITE);
 Title title = mock(Title.class);
 when(title.getPreferredSize()).thenReturn(new Dimension(100, 20));
 ChartDataModel model = new ChartDataModel();
 model.setTitle(title);
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 chart.render(g);
 verify(g, times(1)).fillRect(0, 0, 300, 200);
 verify(g, times(1)).setColor(Color.BLACK);
 verify(title, times(1)).render(g);
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.WHITE);
 Title title = mock(Title.class);
 Legend legend = mock(Legend.class);
 when(title.getPreferredSize()).thenReturn(new Dimension(100, 20));
 when(legend.getPreferredSize()).thenReturn(new Dimension(50, 100));
 ChartDataModel model = new ChartDataModel();
 model.setTitle(title);
 model.setLegend(legend);
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 chart.render(g);
 verify(g, times(1)).fillRect(0, 0, 300, 200);
 verify(g, times(1)).setColor(Color.BLACK);
 verify(title, times(1)).render(g);
 verify(legend, times(1)).render(g);
 }

 @Test
 public void testRenderWithTitleLegendAndCoordinateSystem() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.WHITE);
 Title title = mock(Title.class);
 Legend legend = mock(Legend.class);
 CoordSystem coordSystem = mock(CoordSystem.class);
 when(title.getPreferredSize()).thenReturn(new Dimension(100, 20));
 when(legend.getPreferredSize()).thenReturn(new Dimension(50, 100));
 when(coordSystem.getBounds()).thenReturn(new Rectangle(0, 0, 250, 150));
 ChartDataModel model = new ChartDataModel();
 model.setTitle(title);
 model.setLegend(legend);
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.render(g);
 verify(g, times(1)).fillRect(0, 0, 300, 200);
 verify(g, times(1)).setColor(Color.BLACK);
 verify(title, times(1)).render(g);
 verify(legend, times(1)).render(g);
 verify(coordSystem, times(1)).render(g);
 }

 @Test
 public void testRenderWithData() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.WHITE);
 ChartDataModel model = new ChartDataModel();
 ChartDataSeries series1 = new ChartDataSeries();
 series1.setName("Series 1");
 series1.getData().add(new ChartDataPoint(1, 1));
 series1.getData().add(new ChartDataPoint(2, 2));
 series1.getData().add(new ChartDataPoint(3, 3));
 model.setSeries(Arrays.asList(series1));
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 chart.render(g);
 for (AbstractChartRenderer renderer : chart.getChartRenderer().values()) {
 renderer.render(g);
 }
 }
}