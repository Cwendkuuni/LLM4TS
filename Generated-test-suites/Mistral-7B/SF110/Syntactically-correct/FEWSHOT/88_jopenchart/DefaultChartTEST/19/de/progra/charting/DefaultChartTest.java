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

 private ChartDataModel model;
 private DefaultChart chart;
 private AbstractChartRenderer renderer;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 renderer = new LineChartRenderer();
 chart.getChartRenderer().put(renderer.getClass(), renderer);
 }

 @Test
 public void testDefaultChartConstruction() {
 assertNotNull(chart);
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testChartConstructionWithTitle() {
 assertNotNull(chart);
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart);
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystemAndLegend() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-axis", "Y-axis");
 assertNotNull(chart);
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testGetPreferredSizeReturnsNull() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRenderTitle() {
 Title title = new Title("Test Title");
 chart.setTitle(title);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 title.render(g);
 verify(g).drawString("Test Title", width / 2, 0);
 }

 @Test
 public void testRenderLegend() {
 Legend legend = new Legend();
 chart.setLegend(legend);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 legend.render(g);
 verifyNoMoreInteractions(g);
 }

 @Test
 public void testRenderCoordinateSystem() {
 CoordSystem coordSystem = new CoordSystem(model);
 chart.setCoordSystem(coordSystem);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 coordSystem.render(g);
 verifyNoMoreInteractions(g);
 }

 @Test
 public void testRenderChartRenderer() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 for (AbstractChartRenderer chartRenderer : chart.getChartRenderer().values()) {
 chartRenderer.setBounds(new Rectangle2D.Double(0, 0, 100, 100));
 chartRenderer.render(g);
 }
 verify(g, times(chart.getChartRenderer().size())).fillOval(eq(0), eq(0), eq(100), eq(100));
 }

 @Test
 public void testRenderWithNullGraphics2D() {
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.render(null);
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullChartDataModel() {
 DefaultChart chart = new DefaultChart(null, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullGraphics2DAndNullChartDataModel() {
 DefaultChart chart = new DefaultChart(null, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.render(null);
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.WHITE);
 when(g.fillRect(anyInt(), anyInt(), anyInt(), anyInt())).thenAnswer((Answer<Void>) invocation -> {
 // Empty implementation
 return null;
 });
 return g;
 }
}