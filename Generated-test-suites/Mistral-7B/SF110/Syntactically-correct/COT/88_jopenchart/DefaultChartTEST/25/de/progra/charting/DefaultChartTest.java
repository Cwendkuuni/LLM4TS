package de.progra.charting.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;
import java.awt.*;
import java.util.*;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getChartDataModel());
 assertNull(defaultChart.getTitle());
 assertNull(defaultChart.getLegend());
 assertNull(defaultChart.getCoordSystem());
 assertEmpty(defaultChart.getChartRenderer().values());
 }

 @Test
 public void testChartWithTitleConstruction() {
 assertEquals("Test Chart", chart.getTitle().getText());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemConstruction() {
 chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemAndAxisLabelsConstruction() {
 chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "xAxisLabel", "yAxisLabel");
 assertNotNull(chart.getCoordSystem());
 assertEquals("xAxisLabel", chart.getCoordSystem().getXAxisUnit());
 assertEquals("yAxisLabel", chart.getCoordSystem().getYAxisUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCoordinateSystemConstruction() {
 chart = new DefaultChart(new ChartDataModel(), "Test Chart", 2);
 assertFalse(chart.getCoordSystem() instanceof CoordSystem);
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRender() {
 Graphics2D g = createMock(Graphics2D.class);
 chart.render(g);
 }

 @Test
 public void testRenderWithTitle() {
 Title title = new Title("Test Title");
 chart.setTitle(title);
 Graphics2D g = createMock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).setColor(eq(Color.black));
 if (title != null) {
 verify(g).render(title);
 }
 }

 @Test
 public void testRenderWithLegend() {
 Legend legend = new Legend();
 chart.setLegend(legend);
 Graphics2D g = createMock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).setColor(eq(Color.black));
 if (legend != null) {
 verify(g).render(legend);
 }
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 Title title = new Title("Test Title");
 Legend legend = new Legend();
 chart.setTitle(title);
 chart.setLegend(legend);
 Graphics2D g = createMock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).setColor(eq(Color.black));
 if (title != null) {
 verify(g).render(title);
 }
 if (legend != null) {
 verify(g).render(legend);
 }
 }

 @Test
 public void testRenderWithTitleAndCoordinateSystem() {
 Title title = new Title("Test Title");
 chart.setTitle(title);
 chart.setCoordSystem(new CoordSystem(new ChartDataModel()));
 Graphics2D g = createMock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).setColor(eq(Color.black));
 if (title != null) {
 verify(g).render(title);
 }
 }

 @Test
 public void testRenderWithTitleLegendAndCoordinateSystem() {
 Title title = new Title("Test Title");
 Legend legend = new Legend();
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(new CoordSystem(new ChartDataModel()));
 Graphics2D g = createMock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).setColor(eq(Color.black));
 if (title != null) {
 verify(g).render(title);
 }
 if (legend != null) {
 verify(g).render(legend);
 }
 }

 @Test
 public void testRenderWithTitleLegendCoordinateSystemAndRenderers() {
 Title title = new Title("Test Title");
 Legend legend = new Legend();
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(new CoordSystem(new ChartDataModel()));
 AbstractChartRenderer renderer = mock(AbstractChartRenderer.class);
 chart.getChartRenderer().put("Test Renderer", renderer);
 Graphics2D g = createMock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).setColor(eq(Color.black));
 if (title != null) {
 verify(g).render(title);
 }
 if (legend != null) {
 verify(g).render(legend);
 }
 if (renderer != null) {
 verify(renderer).render(g);
 }
 }

 @Test
 public void testRenderWithTitleLegendCoordinateSystemAndNoRenderers() {
 Title title = new Title("Test Title");
 Legend legend = new Legend();
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(new CoordSystem(new ChartDataModel()));
 Graphics2D g = createMock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).setColor(eq(Color.black));
 if (title != null) {
 verify(g).render(title);
 }
 if (legend != null) {
 verify(g).render(legend);
 }
 }

 @Test
 public void testRenderWithTitleLegendCoordinateSystemAndEmptyRenderers() {
 Title title = new Title("Test Title");
 Legend legend = new Legend();
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(new CoordSystem(new ChartDataModel()));
 chart.getChartRenderer().clear();
 Graphics2D g = createMock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).setColor(eq(Color.black));
 if (title != null) {
 verify(g).render(title);
 }
 if (legend != null) {
 verify(g).render(legend);
 }
 }

}