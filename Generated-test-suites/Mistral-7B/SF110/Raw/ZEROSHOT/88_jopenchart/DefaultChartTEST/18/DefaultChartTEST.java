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

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.render.*;
import de.progra.charting.DefaultChart;
import de.progra.charting.AbstractChart;
import de.progra.charting.AbstractChartRenderer;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 model.setXAxisData(Arrays.asList(new Double[] { 1.0, 2.0, 3.0, 4.0, 5.0 }));
 model.setYAxisData(Arrays.asList(new Double[] { 1.0, 2.0, 3.0, 4.0, 5.0 }));
 model.setSeriesData(Arrays.asList(new SeriesData[] { new SeriesData(new String[] { "Series1" }, new Double[] { 1.0, 2.0, 3.0, 4.0, 5.0 }) }));

 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testDefaultChartConstruction() {
 assertNotNull(chart);
 assertTrue(chart instanceof DefaultChart);
 assertSame(chart.getChartDataModel(), model);
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 @Test
 public void testChartConstructionWithTitle() {
 Title title = new Title("Test Title");
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, title);
 assertNotNull(title);
 assertTrue(title instanceof Title);
 assertEquals("Test Title", title.getText());
 assertSame(chart.getTitle(), title);
 }

 @Test
 public void testChartConstructionWithTitleAndCoordSystem() {
 Title title = new Title("Test Title");
 CoordSystem coordSystem = new CoordSystem(model);
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, title, coordSystem);
 assertNotNull(title);
 assertTrue(title instanceof Title);
 assertEquals("Test Title", title.getText());
 assertSame(chart.getTitle(), title);
 assertNotNull(coordSystem);
 assertTrue(coordSystem instanceof CoordSystem);
 assertSame(chart.getCoordSystem(), coordSystem);
 }

 @Test
 public void testGetPreferredSize() {
 Dimension size = chart.getPreferredSize();
 assertNull(size);
 }

 @Test(expected = NullPointerException.class)
 public void testRenderNullGraphics() {
 chart.render(null);
 }

 @Test
 public void testRender() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 // Add asserts for rendering components (title, legend, coordSystem, chart renderers) here
 }

 @Test
 public void testStaticInitialization() {
 assertEquals(DefaultChart.LINEAR_X_LINEAR_Y, 0);
 assertEquals(DefaultChart.NO_COORDINATE_SYSTEM, 1);
 }

 @Test
 public void testChartRendererMapInitialization() {
 Map<Class<? extends AbstractChartRenderer>, AbstractChartRenderer> chartRendererMap = chart.getChartRenderer();
 assertTrue(chartRendererMap.isEmpty());
 chartRendererMap = chart.getChartRenderer();
 AbstractChartRenderer renderer = new LineChartRenderer();
 chartRendererMap.put(renderer.getClass(), renderer);
 assertTrue(chartRendererMap.containsKey(renderer.getClass()));
 assertTrue(chartRendererMap.get(renderer.getClass()) instanceof LineChartRenderer);
 }
}