package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;

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
 public void testDefaultChartConstruction() {
 // Test default constructor
 DefaultChart chart1 = new DefaultChart();
 assertNull(chart1.getChartDataModel());
 assertNull(chart1.getTitle());
 assertNull(chart1.getLegend());
 assertNull(chart1.getCoordSystem());
 assertEmpty(chart1.getChartRenderer().values());

 // Test constructor with ChartDataModel and title
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart2 = new DefaultChart(model, "Test Chart");
 assertEquals(model, chart2.getChartDataModel());
 assertNotNull(chart2.getTitle());
 assertNull(chart2.getLegend());
 assertNull(chart2.getCoordSystem());
 assertEmpty(chart2.getChartRenderer().values());

 // Test constructor with ChartDataModel, title, and coordinate system
 ChartDataModel model2 = new ChartDataModel();
 DefaultChart chart3 = new DefaultChart(model2, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertEquals(model2, chart3.getChartDataModel());
 assertNotNull(chart3.getTitle());
 assertNotNull(chart3.getLegend());
 assertNotNull(chart3.getCoordSystem());
 assertEmpty(chart3.getChartRenderer().values());
 }

 @Test
 public void testGetPreferredSize() {
 // Test getPreferredSize method
 Dimension size = chart.getPreferredSize();
 assertNull(size);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetPreferredSize() {
 // Test setting preferred size throws UnsupportedOperationException
 chart.setPreferredSize(new Dimension(10, 10));
 }

 @Test
 public void testRender() {
 // Test render method with no title or legend
 Graphics2D g = new StubGraphics2D();
 chart.render(g);
 // Check that nothing was rendered
 assertArrayEquals(new int[0], g.getDrawingFunctions());

 // Test render method with title and legend
 Title title = new Title("Test Title");
 Legend legend = new Legend(new RowColorModel(chart.getChartDataModel()));
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.render(g);
 // Check that title and legend were rendered
 int titleIndex = Arrays.asList(g.getDrawingFunctions()).indexOf(DrawingFunction.DRAW_TITLE);
 int legendIndex = Arrays.asList(g.getDrawingFunctions()).indexOf(DrawingFunction.DRAW_LEGEND);
 assertTrue(titleIndex >= 0);
 assertTrue(legendIndex >= 0);
 }

 @Test
 public void testRenderWithCoordinateSystem() {
 // Test render method with title, legend, and coordinate system
 Title title = new Title("Test Title");
 Legend legend = new Legend(new RowColorModel(chart.getChartDataModel()));
 CoordSystem coordSystem = new CoordSystem(chart.getChartDataModel());
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(coordSystem);
 chart.render(g);
 // Check that title, legend, and coordinate system were rendered
 int titleIndex = Arrays.asList(g.getDrawingFunctions()).indexOf(DrawingFunction.DRAW_TITLE);
 int legendIndex = Arrays.asList(g.getDrawingFunctions()).indexOf(DrawingFunction.DRAW_LEGEND);
 int coordSystemIndex = Arrays.asList(g.getDrawingFunctions()).indexOf(DrawingFunction.DRAW_COORDINATE_SYSTEM);
 assertTrue(titleIndex >= 0);
 assertTrue(legendIndex >= 0);
 assertTrue(coordSystemIndex >= 0);
 }

 @Test
 public void testRenderWithChartRenderer() {
 // Test render method with title, legend, coordinate system, and chart renderer
 Title title = new Title("Test Title");
 Legend legend = new Legend(new RowColorModel(chart.getChartDataModel()));
 CoordSystem coordSystem = new CoordSystem(chart.getChartDataModel());
 LineChartRenderer lineChartRenderer = new LineChartRenderer(chart.getChartDataModel().getDataSeries().get(0));
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(coordSystem);
 chart.getChartRenderer().put("LineChartRenderer", lineChartRenderer);
 chart.render(g);
 // Check that title, legend, coordinate system, and chart renderer were rendered
 int titleIndex = Arrays.asList(g.getDrawingFunctions()).indexOf(DrawingFunction.DRAW_TITLE);
 int legendIndex = Arrays.asList(g.getDrawingFunctions()).indexOf(DrawingFunction.DRAW_LEGEND);
 int coordSystemIndex = Arrays.asList(g.getDrawingFunctions()).indexOf(DrawingFunction.DRAW_COORDINATE_SYSTEM);
 int rendererIndex = Arrays.asList(g.getDrawingFunctions()).indexOf(DrawingFunction.DRAW_CHART);
 assertTrue(titleIndex >= 0);
 assertTrue(legendIndex >= 0);
 assertTrue(coordSystemIndex >= 0);
 assertTrue(rendererIndex >= 0);
 }

}

class StubGraphics2D extends Graphics2D {
 private List<DrawingFunction> drawingFunctions = new ArrayList<>();

 @Override
 public void drawString(String str, float x, float y) {
 drawingFunctions.add(DrawingFunction.DRAW_TITLE);
 }

 @Override
 public void drawLine(int x1, int y1, int x2, int y2) {
 drawingFunctions.add(DrawingFunction.DRAW_LEGEND);
 }

 @Override
 public void drawPolyline(int[] xpoints, int[] ypoints, int npoints) {
 drawingFunctions.add(DrawingFunction.DRAW_COORDINATE_SYSTEM);
 }

 @Override
 public void drawRect(int x, int y, int width, int height) {
 drawingFunctions.add(DrawingFunction.DRAW_CHART);
 }

 public List<DrawingFunction> getDrawingFunctions() {
 return drawingFunctions;
 }
}

enum DrawingFunction {
 DRAW_TITLE,
 DRAW_LEGEND,
 DRAW_COORDINATE_SYSTEM,
 DRAW_CHART
}