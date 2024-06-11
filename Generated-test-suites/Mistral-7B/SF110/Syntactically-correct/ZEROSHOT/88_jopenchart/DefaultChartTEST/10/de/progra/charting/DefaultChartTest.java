package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private ChartDataModel createDummyModel() {
 return new ChartDataModel(Arrays.asList(new DataPoint("A", 1.0, 2.0), new DataPoint("B", 3.0, 4.0), new DataPoint("C", 5.0, 6.0)));
 }

 @Test
 public void testDefaultChartCreation() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart");
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testDefaultChartCreationWithTitle() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testDefaultChartCreationWithTitleAndCoordinateSystem() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "x", "y");
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNotNull(chart.getCoordSystem());
 assertTrue(chart.getCoordSystem().getXAxisUnit().equals("x"));
 assertTrue(chart.getCoordSystem().getYAxisUnit().equals("y"));
 }

 @Test
 public void testDefaultChartEmptyRender() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "x", "y");
 Graphics2D g = createMockGraphics2D();
 chart.render(g);

 // Check background is white
 assertTrue(g.getColor().equals(Color.WHITE));
 // Check title is rendered
 Title title = chart.getTitle();
 Dimension titleSize = title.getPreferredSize();
 Rectangle titleBounds = title.getBounds();
 assertEquals("Test Chart", title.getText());
 assertEquals(titleSize.width, titleBounds.getWidth());
 assertEquals(titleSize.height, titleBounds.getHeight());
 // Check legend is not rendered
 Legend legend = chart.getLegend();
 assertNull(legend);
 // Check coordinate system is rendered
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 // Check chart renderer is empty
 Map<Integer, AbstractChartRenderer> renderers = chart.getChartRenderer();
 assertTrue(renderers.isEmpty());
 }

 @Test
 public void testDefaultChartRenderWithData() {
 DefaultChart chart = new DefaultLineChartRendererTest.DefaultDataChartRendererTest.ChartDataProvider().createChart(new ChartDataModel(Arrays.asList(new DataPoint("A", 1.0, 2.0), new DataPoint("B", 3.0, 4.0), new DataPoint("C", 5.0, 6.0))), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "x", "y");
 Graphics2D g = createMockGraphics2D();
 chart.render(g);

 // Check background is white
 assertTrue(g.getColor().equals(Color.WHITE));
 // Check title is rendered
 Title title = chart.getTitle();
 Dimension titleSize = title.getPreferredSize();
 Rectangle titleBounds = title.getBounds();
 assertEquals("Test Chart", title.getText());
 assertEquals(titleSize.width, titleBounds.getWidth());
 assertEquals(titleSize.height, titleBounds.getHeight());
 // Check legend is rendered
 Legend legend = chart.getLegend();
 Dimension legendSize = legend.getPreferredSize();
 Rectangle legendBounds = legend.getBounds();
 assertEquals("Size: 3", legend.getText());
 assertEquals(legendSize.width, legendBounds.getWidth());
 assertEquals(legendSize.height, legendBounds.getHeight());
 // Check coordinate system is rendered
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 // Check chart renderer is rendered
 Map<Integer, AbstractChartRenderer> renderers = chart.getChartRenderer();
 List<AbstractChartRenderer> chartRendererList = new ArrayList<>(renderers.values());
 assertEquals(1, chartRendererList.size());
 AbstractChartRenderer renderer = chartRendererList.get(0);
 assertTrue(renderer instanceof LineChartRenderer);
 LineChartRenderer lineChartRenderer = (LineChartRenderer) renderer;
 assertEquals(3, lineChartRenderer.getDataPointsSize());
 LineChartRendererTest.assertDataPointsRenderCorrectly(lineChartRenderer.getDataPoints(), g);
 }

 @Test
 public void testDefaultChartRenderWithEmptyData() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "x", "y");
 Graphics2D g = createMockGraphics2D();
 chart.render(g);

 // Check background is white
 assertTrue(g.getColor().equals(Color.WHITE));
 // Check title is rendered
 Title title = chart.getTitle();
 Dimension titleSize = title.getPreferredSize();
 Rectangle titleBounds = title.getBounds();
 assertEquals("Test Chart", title.getText());
 assertEquals(titleSize.width, titleBounds.getWidth());
 assertEquals(titleSize.height, titleBounds.getHeight());
 // Check legend is not rendered
 Legend legend = chart.getLegend();
 assertNull(legend);
 // Check coordinate system is not rendered
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNull(coordSystem);
 // Check chart renderer is empty
 Map<Integer, AbstractChartRenderer> renderers = chart.getChartRenderer();
 assertTrue(renderers.isEmpty());
 }

 @Test
 public void testDefaultChartRenderWithNoCoordinateSystem() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM, "x", "y");
 Graphics2D g = createMockGraphics2D();
 chart.render(g);

 // Check background is white
 assertTrue(g.getColor().equals(Color.WHITE));
 // Check title is rendered
 Title title = chart.getTitle();
 Dimension titleSize = title.getPreferredSize();
 Rectangle titleBounds = title.getBounds();
 assertEquals("Test Chart", title.getText());
 assertEquals(titleSize.width, titleBounds.getWidth());
 assertEquals(titleSize.height, titleBounds.getHeight());
 // Check legend is not rendered
 Legend legend = chart.getLegend();
 assertNull(legend);
 // Check coordinate system is not rendered
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNull(coordSystem);
 // Check chart renderer is empty
 Map<Integer, AbstractChartRenderer> renderers = chart.getChartRenderer();
 assertTrue(renderers.isEmpty());
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.BLACK);
 when(g.setColor(any(Color.class))).thenAnswer(invocation -> {
 Color color = invocation.getArgument(0);
 assertEquals(Color.BLACK, color);
 return g;
 });
 return g;
 }
}