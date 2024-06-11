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
 ChartData chartData = new ChartData();
 chartData.addSeries(new SeriesData("Series1", Arrays.asList(1.0, 2.0, 3.0)));
 model.setChartData(chartData);
 chart = new DefaultChart(model, "TestChart");
 }

 // Test constructor without arguments
 @Test
 public void testEmptyConstructor() {
 DefaultChart chart = new DefaultChart();
 assertNull(chart.getChartDataModel());
 assertNull(chart.getTitle());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getLegend());
 assertNull(chart.getChartRenderer());
 }

 // Test constructor with title
 @Test
 public void testTitleConstructor() {
 assertEquals("TestChart", chart.getTitle().getText());
 }

 // Test constructor with title and coordinate system
 @Test
 public void testTitleAndCoordinateSystemConstructor() {
 DefaultChart chart = new DefaultChart(chart.getChartDataModel(), "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getCoordSystem());
 }

 // Test constructor with title, coordinate system, xaxis and yaxis
 @Test
 public void testTitleCoordinateSystemXaxisYaxisConstructor() {
 DefaultChart chart = new DefaultChart(chart.getChartDataModel(), "TestChart", DefaultChart.LINEAR_X_LINEAR_Y, "Xaxis", "Yaxis");
 assertEquals("Xaxis", chart.getCoordSystem().getXAxisUnit());
 assertEquals("Yaxis", chart.getCoordSystem().getYAxisUnit());
 }

 // Test getPreferredSize method
 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 // Test render method with no title and legend
 @Test
 public void testRenderWithoutTitleAndLegend() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verify(g, times(1)).fillRect(0, 0, anyInt(), anyInt());
 verify(g, times(1)).setColor(Color.WHITE);
 verify(g, times(1)).setColor(Color.BLACK);
 }

 // Test render method with title and legend
 @Test
 public void testRenderWithTitleAndLegend() {
 Title title = new Title("Test Title");
 Legend legend = new Legend(new RowColorModel(chart.getChartDataModel()));
 chart.setTitle(title);
 chart.setLegend(legend);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verify(g, times(1)).fillRect(0, 0, anyInt(), anyInt());
 verify(g, times(1)).setColor(Color.WHITE);
 verify(g, times(1)).setColor(Color.BLACK);
 verify(g, times(1)).fillRect(width / 2 - (int)(title.getPreferredSize().getWidth() / 2.0), 0, (int)title.getPreferredSize().getWidth(), (int)title.getPreferredSize().getHeight());
 verify(g, times(1)).fillRect((int)(width - legend.getPreferredSize().getWidth()), (int)(height / 2 - legend.getPreferredSize().getHeight() / 2.0), (int)legend.getPreferredSize().getWidth(), (int)legend.getPreferredSize().getHeight());
 }

 // Test exception handling when invalid coordinate system is passed
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCoordinateSystemConstructor() {
 new DefaultChart(chart.getChartDataModel(), "TestChart", 2);
 }

 // Test exception handling when null ChartDataModel is passed
 @Test(expected = IllegalArgumentException.class)
 public void testNullChartDataModelConstructor() {
 new DefaultChart(null, "TestChart");
 }

 // Test exception handling when null title is passed
 @Test(expected = IllegalArgumentException.class)
 public void testNullTitleConstructor() {
 new DefaultChart(chart.getChartDataModel(), null);
 }

}