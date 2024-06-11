package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.render.*;
import de.progra.charting.DefaultChart;
import de.progra.charting.AbstractChart;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel model;

 @Before
 public void setup() {
 model = new ChartDataModel();
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testDefaultChartConstructionWithModelAndTitle() {
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 assertNotNull(chart.getTitle());
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testDefaultChartConstructionWithModelTitleAndCoordinateSystem() {
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "Xaxis", "Yaxis");
 ChartCoordinateSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 assertNotNull(coordSystem.getXAxisUnit());
 assertNotNull(coordSystem.getYAxisUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultChartConstructionWithInvalidCoordinateSystem() {
 DefaultChart chart = new DefaultChart(model, "Test Chart", 2);
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRender() {
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verify(g, times(1)).setColor(Color.white);
 verify(g, times(1)).fillRect(0, 0, anyInt(), anyInt());
 verify(g, times(1)).setColor(Color.black);
 }

 @Test
 public void testSetTitle() {
 Title title = new Title("Test Title");
 chart.setTitle(title);
 Title chartTitle = chart.getTitle();
 assertEquals("Test Title", chartTitle.getText());
 }

 @Test
 public void testSetLegend() {
 Legend legend = new Legend();
 chart.setLegend(legend);
 Legend chartLegend = chart.getLegend();
 assertSame(legend, chartLegend);
 }

 @Test
 public void testSetCoordSystem() {
 ChartCoordinateSystem coordSystem = new ChartCoordinateSystem();
 chart.setCoordSystem(coordSystem);
 ChartCoordinateSystem chartCoordSystem = chart.getCoordSystem();
 assertEquals(coordSystem, chartCoordSystem);
 }

 @Test
 public void testSetChartRenderer() {
 AbstractChartRenderer mockRenderer = mock(AbstractChartRenderer.class);
 chart.setChartRenderer(Collections.singletonMap("TestRenderer", mockRenderer));
 assertSame(mockRenderer, chart.getChartRenderer().get("TestRenderer"));
 }

 @Test
 public void testGetBounds() {
 Rectangle2D expected = new Rectangle2D.Float(0, 0, 100, 100);
 chart.setBounds(expected);
 Rectangle2D actual = chart.getBounds();
 assertEquals(expected, actual);
 }
}