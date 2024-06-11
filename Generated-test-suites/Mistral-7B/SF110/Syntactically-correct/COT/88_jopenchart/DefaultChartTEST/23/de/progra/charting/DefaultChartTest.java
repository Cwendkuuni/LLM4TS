package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel model;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testDefaultChartConstructor() {
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getChartDataModel());
 assertNull(defaultChart.getTitle());
 assertNull(defaultChart.getLegend());
 assertNull(defaultChart.getCoordSystem());
 }

 @Test
 public void testChartWithTitleConstructor() {
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testChartWithTitleAndCoordinatesConstructor() {
 ChartDataModel model = new ChartDataModel();
 model.setXAxisLabel("X Axis");
 model.setYAxisLabel("Y Axis");
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 assertEquals("X Axis", coordSystem.getXAxisUnit());
 assertEquals("Y Axis", coordSystem.getYAxisUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCoordinatesConstructor() {
 ChartDataModel model = new ChartDataModel();
 model.setXAxisLabel("X Axis");
 model.setYAxisLabel("Y Axis");
 chart = new DefaultChart(model, "Test Chart", 2);
 }

 @Test
 public void testGetPreferredSize() {
 ChartDataModel model = new ChartDataModel();
 model.setXAxisLabel("X Axis");
 model.setYAxisLabel("Y Axis");
 ChartRenderer renderer = new LineChartRenderer();
 model.addSeries("Test Series", new LineChartData("Test Series", new double[][]{{0, 1}, {1, 2}, {2, 3}}, new double[]{}));
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.setChartRenderer(Collections.singletonMap("Test Series", renderer));
 assertEquals(500, chart.getPreferredSize().width);
 assertEquals(400, chart.getPreferredSize().height);
 }

 @Test
 public void testRender() {
 Graphics2D g = mock(Graphics2D.class);
 ChartDataModel model = new ChartDataModel();
 model.setXAxisLabel("X Axis");
 model.setYAxisLabel("Y Axis");
 ChartRenderer renderer = new LineChartRenderer();
 model.addSeries("Test Series", new LineChartData("Test Series", new double[][]{{0, 1}, {1, 2}, {2, 3}}, new double[]{}));
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.setChartRenderer(Collections.singletonMap("Test Series", renderer));

 when(g.getColor()).thenReturn(Color.WHITE);
 when(g.fillRect(anyInt(), anyInt(), anyInt(), anyInt())).thenAnswer(i -> i.getArguments()[1]);
 when(g.getColor()).thenReturn(Color.BLACK);
 when(g.fillRect(anyInt(), anyInt(), anyInt(), anyInt())).thenAnswer(i -> i.getArguments()[1].intValue());
 when(g.setColor(any(Color.class))).thenAnswer(i -> i.getArguments()[0]);

 chart.render(g);

 verify(g, times(1)).fillRect(eq(0), eq(0), eq(500), eq(400));
 verify(g, times(1)).setColor(eq(Color.WHITE));
 verify(g, times(1)).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g, times(1)).setColor(eq(Color.BLACK));
 verifyNoMoreInteractions(g);
 }

}