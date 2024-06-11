package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
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
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
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
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemConstructor() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chartWithCoord = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 CoordSystem coordSystem = new CoordSystem(model);
 chartWithCoord.setCoordSystem(coordSystem);
 assertNotNull(chartWithCoord.getCoordSystem());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemAndAxisUnitsConstructor() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chartWithCoordAndAxis = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Unit", "Y Unit");
 CoordSystem coordSystem = new CoordSystem(model);
 chartWithCoordAndAxis.setCoordSystem(coordSystem);
 chartWithCoordAndAxis.getCoordSystem().setXAxisUnit("X Unit");
 chartWithCoordAndAxis.getCoordSystem().setYAxisUnit("Y Unit");
 assertEquals("X Unit", chartWithCoordAndAxis.getCoordSystem().getXAxisUnit());
 assertEquals("Y Unit", chartWithCoordAndAxis.getCoordSystem().getYAxisUnit());
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRender() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.black);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCoordinateSystemType() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chartWithInvalidCoord = new DefaultChart(model, "Test Chart", 2);
 assertNull(chartWithInvalidCoord.getCoordSystem());
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 Title title = chart.getTitle();
 Legend legend = chart.getLegend();
 verify(g).setColor(Color.black);
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(title).render(g);
 verify(legend).render(g);
 }

 @Test
 public void testRenderWithTitleLegendAndChartRenderer() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 Title title = chart.getTitle();
 Legend legend = chart.getLegend();
 Collection<AbstractChartRenderer> renderers = chart.getChartRenderer().values();
 for (AbstractChartRenderer renderer : renderers) {
 renderer.render(g);
 }
 Title expectedTitle = new Title("Test Chart");
 Legend expectedLegend = new Legend(new RowColorModel(model));
 verify(g).setColor(Color.black);
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(title).render(g);
 verify(legend).render(g);
 for (AbstractChartRenderer renderer : renderers) {
 verify(renderer).render(g);
 }
 }

 @Test
 public void testRenderWithTitleLegendChartRendererAndCoordinateSystem() {
 Graphics2D g = createMockGraphics2D();
 ChartDataModel model = new ChartDataModel();
 DefaultChart chartWithCoord = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 CoordSystem coordSystem = new CoordSystem(model);
 chartWithCoord.setCoordSystem(coordSystem);
 chartWithCoord.render(g);
 Title title = chartWithCoord.getTitle();
 Legend legend = chartWithCoord.getLegend();
 CoordSystem actualCoordSystem = chartWithCoord.getCoordSystem();
 verify(g).setColor(Color.black);
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(title).render(g);
 verify(legend).render(g);
 verify(actualCoordSystem).render(g);
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.WHITE);
 when(g.fillRect(anyInt(), anyInt(), anyInt(), anyInt())).thenAnswer((i) -> {
 i.getArguments()[0];
 i.getArguments()[1];
 i.getArguments()[2];
 i.getArguments()[3];
 return true;
 });
 return g;
 }
}