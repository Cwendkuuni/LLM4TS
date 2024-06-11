package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;

import org.junit.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 ChartData chartData = new ChartData();
 chartData.addSeries(new ChartSeries("Series1", new Double[]{}));
 model.setChartData(chartData);
 chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testDefaultChartConstruction() {
 assertEquals("TestChart", chart.getTitle().getText());
 assertNull(chart.getCoordSystem());
 assertFalse(chart.getChartRenderer().isEmpty());
 }

 @Test
 public void testDefaultChartConstructionWithTitle() {
 ChartDataModel model = new ChartDataModel();
 ChartData chartData = new ChartData();
 chartData.addSeries(new ChartSeries("Series1", new Double[]{}));
 model.setChartData(chartData);
 chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertEquals("TestChart", chart.getTitle().getText());
 assertNull(chart.getCoordSystem());
 assertFalse(chart.getChartRenderer().isEmpty());
 }

 @Test
 public void testDefaultChartConstructionWithTitleAndCoordSystem() {
 ChartDataModel model = new ChartDataModel();
 ChartData chartData = new ChartData();
 chartData.addSeries(new ChartSeries("Series1", new Double[]{}));
 model.setChartData(chartData);
 chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y, "XAxis", "YAxis");
 assertEquals("TestChart", chart.getTitle().getText());
 assertNotNull(chart.getCoordSystem());
 assertEquals("XAxis", chart.getCoordSystem().getXAxisUnit());
 assertEquals("YAxis", chart.getCoordSystem().getYAxisUnit());
 }

 @Test
 public void testGetPreferredSize() {
 Rectangle bounds = new Rectangle(0, 0, 100, 100);
 chart.setBounds(bounds);
 Dimension preferredSize = chart.getPreferredSize();
 assertEquals(0, preferredSize.getHeight());
 assertEquals(0, preferredSize.getWidth());
 }

 @Test
 public void testRender() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 chart.render(g);
 // Add assertions for render method here
 }

 @After
 public void tearDown() {
 chart = null;
 }
}