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
 model.addSeries("Series1", new Double[][]{ {1.0, 2.0, 3.0, 4.0}, {5.0, 6.0, 7.0, 8.0} });
 model.addSeries("Series2", new Double[][]{ {2.0, 4.0, 6.0, 8.0}, {3.0, 5.0, 7.0, 9.0} });
 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testDefaultChartConstruction() {
 assertNotNull(chart);
 assertSame(chart.getChartDataModel(), model);
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertFalse(chart.hasCoordSystem());
 }

 @Test
 public void testChartConstructionWithTitle() {
 Title title = new Title("Test Title");
 chart = new DefaultChart(model, "Test Chart", title);
 assertSame(title, chart.getTitle());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystem() {
 Title title = new Title("Test Title");
 Legend legend = new Legend();
 CoordSystem coordSystem = new CoordSystem(model);
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, title, legend, coordSystem);
 assertSame(title, chart.getTitle());
 assertSame(legend, chart.getLegend());
 assertSame(coordSystem, chart.getCoordSystem());
 assertTrue(chart.hasCoordSystem());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartConstructionWithInvalidCoordinateSystem() {
 Title title = new Title("Test Title");
 Legend legend = new Legend();
 CoordSystem coordSystem = new CoordSystem(model);
 chart = new DefaultChart(model, "Test Chart", 2, title, legend, coordSystem);
 fail("Exception not thrown");
 }

 @Test
 public void testGetPreferredSize() {
 Dimension size = chart.getPreferredSize();
 assertNull(size);
 }

 @Test
 public void testRender() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 chart.render(g);
 // This method cannot be tested as it depends on the actual rendering implementation
 // and we don't have access to the actual graphics context or dimensions.
 }

 @Test
 public void testStaticInitialization() {
 assertEquals(0, DefaultChart.LINEAR_X_LINEAR_Y);
 assertEquals(1, DefaultChart.NO_COORDINATE_SYSTEM);
 }
}