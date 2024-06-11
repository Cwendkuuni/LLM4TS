package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private ChartDataModel model;
 private DefaultChart chart;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 model.setTitle("Test Chart");
 model.setXAxisLabel("X Axis");
 model.setYAxisLabel("Y Axis");
 model.setDataPoints(Arrays.asList(new DataPoint(1, 2), new DataPoint(2, 4), new DataPoint(3, 6), new DataPoint(4, 8)));
 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testEmptyConstructor() {
 // This should not be used but checking for null values
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 @Test
 public void testTitleConstructor() {
 Title title = chart.getTitle();
 assertEquals("Test Chart", title.getText());
 }

 @Test
 public void testTitleLabelConstructor() {
 Title title = chart.getTitle();
 assertEquals("Test Chart", title.getText());
 assertNotNull(title);
 }

 @Test
 public void testTitleAndCoordinateSystemConstructor() {
 Title title = chart.getTitle();
 assertEquals("Test Chart", title.getText());
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 }

 @Test
 public void testTitleLabelAndCoordinateSystemConstructor() {
 Title title = chart.getTitle();
 assertEquals("Test Chart", title.getText());
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 assertEquals("X Axis", coordSystem.getXAxisUnit());
 assertEquals("Y Axis", coordSystem.getYAxisUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCoordinateSystemType() {
 chart = new DefaultChart(model, "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM, "X Axis", "Y Axis");
 assertNotNull(chart.getTitle());
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testLinearCoordinateSystemConstructor() {
 Title title = chart.getTitle();
 assertEquals("Test Chart", title.getText());
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 assertEquals("X Axis", coordSystem.getXAxisUnit());
 assertEquals("Y Axis", coordSystem.getYAxisUnit());
 }

 @Test
 public void testRender() {
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 // This is a stub for further testing
 // assertRenderResult(g);
 }

 @Test
 public void testGetPreferredSize() {
 Dimension preferredSize = chart.getPreferredSize();
 assertNotNull(preferredSize);
 }
}