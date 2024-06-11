package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.render.DefaultLineChartRenderer;
import de.progra.charting.test.dto.TestChartDataModel;
import de.progra.charting.util.RowColorModel;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel dataModel;

 @Before
 public void setUp() {
 dataModel = new TestChartDataModel();
 chart = new DefaultChart(dataModel, "Test Chart");
 }

 @Test
 public void testEmptyChart() {
 // Test for empty chart
 assertEquals(0, chart.getLegend().getItems().size());
 assertEquals(0, chart.getChartRenderer().size());
 }

 @Test
 public void testSimpleChart() {
 // Test for simple chart with linear x and linear y coordinates
 chart = new DefaultChart(dataModel, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 List<Double> xValues = Arrays.asList(1.0, 2.0, 3.0);
 List<Double> yValues1 = Arrays.asList(1.0, 2.0, 3.0);
 List<Double> yValues2 = Arrays.asList(2.0, 3.0, 4.0);
 chart.getChartRenderer().put(0, new DefaultLineChartRenderer("Line 1", xValues, yValues1, new Color(255, 0, 0)));
 chart.getChartRenderer().put(1, new DefaultLineChartRenderer("Line 2", xValues, yValues2, new Color(0, 0, 255)));

 // Test for title and legend
 assertEquals("Test Chart", chart.getTitle().getText());
 assertEquals(2, chart.getLegend().getItems().size());
 // Test for rendering
 ChartPanel panel = new ChartPanel(chart);
 panel.setPreferredSize(new Dimension(640, 480));
 panel.paintComponents(null);
 // Add assertions for rendering here
 }

 @Test
 public void testChartWithNoCoordinateSystem() {
 // Test for chart with no coordinate system
 chart = new DefaultChart(dataModel, "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM);
 List<Double> xValues = Arrays.asList(1.0, 2.0, 3.0);
 List<Double> yValues = Arrays.asList(1.0, 2.0, 3.0);
 chart.getChartRenderer().put(0, new DefaultLineChartRenderer("Line 1", xValues, yValues, new Color(255, 0, 0)));

 // Test for empty title and legend
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartWithInvalidCoordinateSystem() {
 // Test for chart with invalid coordinate system
 chart = new DefaultChart(dataModel, "Test Chart", -1);
 }

 @Test
 public void testSettingXAxisUnit() {
 // Test for setting x axis unit
 chart = new DefaultChart(dataModel, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.getCoordSystem().setXAxisUnit("Test Unit");
 assertEquals("Test Unit", chart.getCoordSystem().getXAxisUnit());
 }

 @Test
 public void testSettingYAxisUnit() {
 // Test for setting y axis unit
 chart = new DefaultChart(dataModel, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.getCoordSystem().setYAxisUnit("%");
 assertEquals("%", chart.getCoordSystem().getYAxisUnit());
 }
}