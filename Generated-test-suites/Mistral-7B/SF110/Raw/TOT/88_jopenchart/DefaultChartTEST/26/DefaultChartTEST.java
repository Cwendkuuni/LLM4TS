package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelData;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.test.mock.MockChartDataModel;
import de.progra.charting.test.mock.MockChartRenderer;
import de.progra.charting.test.mock.MockTitle;

public class DefaultChartTest {

 private DefaultChart chart;
 private MockChartDataModel model;
 private MockChartRenderer renderer;

 @Before
 public void setup() {
 model = new MockChartDataModel();
 renderer = new MockChartRenderer();
 chart = new DefaultChart(model, "Test Chart");
 chart.getChartRenderer().put(getClass().getName(), renderer);
 }

 @Test
 public void testDefaultChartConstruction() {
 // Test empty constructor
 DefaultChart emptyChart = new DefaultChart();
 assertNull(emptyChart.getChartDataModel());
 assertNull(emptyChart.getTitle());
 assertNull(emptyChart.getLegend());
 assertNull(emptyChart.getCoordSystem());

 // Test constructor with title
 DefaultChart titleChart = new DefaultChart(model, "Test Chart");
 assertEquals("Test Chart", titleChart.getTitle().getText());
 assertNull(titleChart.getLegend());
 assertNull(titleChart.getCoordSystem());

 // Test constructor with title and linear coordinate system
 DefaultChart coordChart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertEquals("Test Chart", titleChart.getTitle().getText());
 assertNotNull(coordChart.getCoordSystem());

 // Test constructor with title, linear coordinate system, and x/y axis labels
 DefaultChart coordLabelChart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Label", "Y Label");
 assertEquals("Test Chart", titleChart.getTitle().getText());
 assertNotNull(coordLabelChart.getCoordSystem());
 assertNotNull(coordLabelChart.getCoordSystem().getXAxisUnit());
 assertNotNull(coordLabelChart.getCoordSystem().getYAxisUnit());
 }

 @Test
 public void testGetPreferredSize() {
 Dimension size = chart.getPreferredSize();
 assertNull(size);
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullGraphics() {
 chart.render(null);
 }

 @Test
 public void testRender() {
 // Test rendering title and legend
 ChartDataModel titleModel = new ChartDataModel(Arrays.asList(new ModelData("Title 1", 1.0, 1.0, 1.0, 1.0, Color.RED), new ModelData("Title 2", 2.0, 2.0, 2.0, 2.0, Color.BLUE)));
 chart = new DefaultChart(titleModel, "Test Chart");
 renderer.setRenderTitle(true);
 chart.render(renderer.getGraphics());
 assertTrue(renderer.isTitleRendered());

 // Test rendering legend
 ChartDataModel legendModel = new ChartDataModel(Arrays.asList(new ModelData("Legend 1", 1.0, 1.0, 1.0, 1.0, Color.RED), new ModelData("Legend 2", 2.0, 2.0, 2.0, 2.0, Color.BLUE)));
 chart = new DefaultChart(legendModel, "Test Chart");
 renderer.setRenderLegend(true);
 chart.render(renderer.getGraphics());
 assertTrue(renderer.isLegendRendered());

 // Test rendering data
 ChartDataModel dataModel = new ChartDataModel(Collections.singletonList(new ModelData("Data", 0.0, 0.0, 1.0, 1.0, Color.GREEN)));
 chart = new DefaultChart(dataModel, "Test Chart");
 chart.render(renderer.getGraphics());
 // Assert data is rendered at correct position
 // ...
 }
}