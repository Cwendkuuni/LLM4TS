package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;

import org.junit.*;

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
 // Check if DefaultChart is initialized correctly
 assertNotNull(chart);
 assertNotNull(chart.getChartDataModel());
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 @Test
 public void testDefaultChartWithTitleConstructor() {
 // Check if DefaultChart is initialized correctly with title
 ChartDataModel model = new ChartDataModel();
 chart = new DefaultChart(model, "Test Chart");
 Title title = chart.getTitle();
 assertNotNull(title);
 assertSame(title, chart.getTitle());
 }

 @Test
 public void testDefaultChartWithTitleAndCoordinateSystemConstructor() {
 // Check if DefaultChart is initialized correctly with title and coordinate system
 ChartDataModel model = new ChartDataModel();
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 assertSame(coordSystem, chart.getCoordSystem());
 }

 @Test
 public void testDefaultChartWithTitleAndCoordinateSystemAndAxisLabelsConstructor() {
 // Check if DefaultChart is initialized correctly with title, coordinate system and axis labels
 ChartDataModel model = new ChartDataModel();
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis Label", "Y Axis Label");
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 assertEquals("X Axis Label", coordSystem.getXAxisUnit());
 assertEquals("Y Axis Label", coordSystem.getYAxisUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCoordinateSystemValue() {
 ChartDataModel model = new ChartDataModel();
 try {
 chart = new DefaultChart(model, "Test Chart", 2);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRender() {
 // Create a mock Graphics2D for testing
 Graphics2D gMock = mock(Graphics2D.class);
 // Test rendering with no title or legend
 chart.render(gMock);
 // Test rendering with title and legend
 Title title = new Title("Test Title");
 Legend legend = new Legend(new RowColorModel(new ChartDataModel()));
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.render(gMock);
 // Test rendering with title, legend and data
 AbstractChartRenderer renderer = mock(AbstractChartRenderer.class);
 ChartDataModel model = new ChartDataModel();
 model.addSeries(new LineSeries("Series1", new double[][]{{1, 2}, {2, 3}, {3, 4}}, new String[]{"Point1", "Point2", "Point3"}));
 chart.getChartRenderer().put("Series1", renderer);
 chart.render(gMock);
 // Test rendering with title, legend, data and coordinate system
 CoordSystem coordSystem = new CoordSystem(model);
 chart.setCoordSystem(coordSystem);
 chart.render(gMock);
 }

}