package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
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

 private AbstractChart chart;
 private ChartDataModel model;
 private RowColorModel rowColorModel;
 private Legend legend;
 private Title title;
 private CoordSystem coordSystem;
 private AbstractChartRenderer chartRenderer;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 rowColorModel = new RowColorModel(model);
 title = new Title("Test Chart");
 legend = new Legend(rowColorModel);
 chartRenderer = new TestChartRenderer();
 chart = new DefaultChart(model, title, DefaultChart.LINEAR_X_LINEAR_Y, "X axis", "Y axis");
 chart.setChartRenderer(new HashMap<String, AbstractChartRenderer>() {{
 put("TestRenderer", chartRenderer);
 }});
 }

 @Test
 public void testDefaultChartConstruction() {
 assertEquals("Title must be set", title.getText(), chart.getTitle().getText());
 assertTrue("Legend must be present", chart.getLegend() instanceof Legend);
 assertNull("Coordinate system must not be set by default", chart.getCoordSystem());
 }

 @Test
 public void testChartWithCoordinateSystem() {
 CoordSystem coordSystem = new CoordSystem(model);
 chart = new DefaultChart(model, title, DefaultChart.LINEAR_X_LINEAR_Y, "X axis", "Y axis", DefaultChart.LINEAR_X_LINEAR_Y, coordSystem);
 assertTrue("Coordinate system must be set", chart.getCoordSystem() instanceof CoordSystem);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartWithInvalidCoordinateSystem() {
 chart = new DefaultChart(model, title, DefaultChart.LINEAR_X_LINEAR_Y, "X axis", "Y axis", DefaultChart.NO_COORDINATE_SYSTEM);
 fail("Illegal argument exception expected");
 }

 @Test
 public void testGetPreferredSize() {
 assertEquals("Preferred size must be null by default", null, chart.getPreferredSize());
 Title title = new Title("Test Title");
 chart.setTitle(title);
 assertEquals("Preferred size must be set when title is added", title.getPreferredSize().width + title.getPreferredSize().height, chart.getPreferredSize().getHeight());
 }

 @Test
 public void testRender() {
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 // Add verification of rendering here
 }

 @Test
 public void testSetChartRenderer() {
 chart.setChartRenderer(new HashMap<String, AbstractChartRenderer>() {{
 put("TestRenderer", new TestChartRenderer());
 put("AnotherRenderer", new AnotherTestChartRenderer());
 }});
 List<AbstractChartRenderer> renderers = new ArrayList<>(chart.getChartRenderer().values());
 assertEquals("Number of renderers must be 2", 2, renderers.size());
 assertTrue("TestRenderer must be present in renderers", renderers.contains(chartRenderer));
 assertTrue("AnotherRenderer must be present in renderers", renderers.contains(new AnotherTestChartRenderer()));
 }

 @Test
 public void testSetChartRendererWithNullMap() {
 chart.setChartRenderer(null);
 assertEquals("Chart renderer must be empty", 0, chart.getChartRenderer().size());
 }

 @Test
 public void testSetChartRendererWithNullRenderer() {
 chart.setChartRenderer(new HashMap<String, AbstractChartRenderer>() {{
 put("TestRenderer", chartRenderer);
 put(null, new AnotherTestChartRenderer());
 }});
 assertEquals("Chart renderer must not contain null renderer", 1, chart.getChartRenderer().size());
 assertFalse("Chart renderer must not contain null renderer", chart.getChartRenderer().containsValue(null));
 }
}

class TestChartRenderer extends AbstractChartRenderer {
 // Implement rendering logic here
}

class AnotherTestChartRenderer extends AbstractChartRenderer {
 // Implement rendering logic here
}