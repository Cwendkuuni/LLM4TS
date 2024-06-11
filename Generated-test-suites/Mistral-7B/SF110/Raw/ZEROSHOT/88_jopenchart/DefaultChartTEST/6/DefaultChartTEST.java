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
 model.setTitle("Test Chart");
 model.setXLabel("X Axis");
 model.setYLabel("Y Axis");
 model.setXAxisMin(0);
 model.setXAxisMax(10);
 model.setYAxisMin(0);
 model.setYAxisMax(5);

 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testDefaultChartConstruction() {
 assertNotNull(chart);
 assertSame(chart.getChartDataModel(), model);
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 @Test
 public void testChartWithTitle() {
 Title title = new Title("Test Title");
 chart = new DefaultChart(model, "Test Chart", title);
 assertNotNull(title);
 assertSame(chart.getTitle(), title);
 }

 @Test
 public void testChartWithTitleAndCoordinateSystem() {
 Title title = new Title("Test Title");
 CoordSystem coordSystem = new CoordSystem(model);
 chart = new DefaultChart(model, "Test Chart", title, DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(title);
 assertSame(chart.getTitle(), title);
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemAndXAxisAndYAxis() {
 Title title = new Title("Test Title");
 CoordSystem coordSystem = new CoordSystem(model);
 coordSystem.setXAxisUnit("Test X Axis");
 coordSystem.setYAxisUnit("Test Y Axis");
 chart = new DefaultChart(model, "Test Chart", title, DefaultChart.LINEAR_X_LINEAR_Y, "Test X Axis", "Test Y Axis");
 assertNotNull(title);
 assertSame(chart.getTitle(), title);
 assertNotNull(chart.getCoordSystem());
 assertSame(chart.getCoordSystem().getXAxisUnit(), "Test X Axis");
 assertSame(chart.getCoordSystem().getYAxisUnit(), "Test Y Axis");
 }

 @Test
 public void testGetPreferredSize() {
 Dimension size = chart.getPreferredSize();
 assertNull(size);
 }

 @Test(expected = NullPointerException.class)
 public void testRenderNullGraphics2D() {
 chart.render(null);
 }

 @Test
 public void testRender() {
 Graphics2D g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
 chart.render(g);
 }
}