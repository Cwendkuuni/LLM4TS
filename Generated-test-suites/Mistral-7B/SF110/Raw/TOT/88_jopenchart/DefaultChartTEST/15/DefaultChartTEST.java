package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelDataSeries;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.test.mock.MockChartDataModel;
import de.progra.charting.test.mock.MockChartRenderer;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel model;
 private AbstractChartRenderer renderer;

 @Before
 public void setup() {
 model = new MockChartDataModel();
 model.addSeriesData(new ModelDataSeries("Series1", Arrays.asList(1, 2, 3, 4, 5)));
 model.addSeriesData(new ModelDataSeries("Series2", Arrays.asList(2, 3, 4, 5, 6)));

 chart = new DefaultChart(model, "TestChart");
 chart.setChartRenderer(new HashMap<String, AbstractChartRenderer>() {{
 put("Series1", new MockChartRenderer(Color.RED));
 put("Series2", new MockChartRenderer(Color.BLUE));
 }});
 renderer = (AbstractChartRenderer) chart.getChartRenderer().get("Series1");
 }

 @Test
 public void testDefaultChartConstruction() {
 // Test default constructor
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getChartDataModel());
 assertNull(defaultChart.getTitle());
 assertNull(defaultChart.getLegend());
 assertNull(defaultChart.getCoordSystem());
 assertNull(defaultChart.getChartRenderer());
 }

 @Test
 public void testChartConstructorWithTitle() {
 // Test constructor with title
 DefaultChart chartWithTitle = new DefaultChart(model, "TestChart");
 assertNotNull(chartWithTitle.getChartDataModel());
 assertNotNull(chartWithTitle.getTitle());
 assertNull(chartWithTitle.getLegend());
 assertNull(chartWithTitle.getCoordSystem());
 assertNotNull(chartWithTitle.getChartRenderer());
 }

 @Test
 public void testChartConstructorWithTitleAndCoordinateSystem() {
 // Test constructor with title and coordinate system
 DefaultChart chartWithTitleAndCoord = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chartWithTitleAndCoord.getChartDataModel());
 assertNotNull(chartWithTitleAndCoord.getTitle());
 assertNotNull(chartWithTitleAndCoord.getCoordSystem());
 assertNull(chartWithTitleAndCoord.getLegend());
 }

 @Test
 public void testChartConstructorWithTitleAndCoordinateSystemAndAxisLabels() {
 // Test constructor with title, coordinate system and axis labels
 DefaultChart chartWithTitleAndCoordAndAxisLabels = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y, "Xaxis", "Yaxis");
 assertNotNull(chartWithTitleAndCoordAndAxisLabels.getChartDataModel());
 assertNotNull(chartWithTitleAndCoordAndAxisLabels.getTitle());
 assertNotNull(chartWithTitleAndCoordAndAxisLabels.getCoordSystem());
 assertNotNull(chartWithTitleAndCoordAndAxisLabels.getCoordSystem().getXAxisUnit());
 assertNotNull(chartWithTitleAndCoordAndAxisLabels.getCoordSystem().getYAxisUnit());
 }

 @Test
 public void testGetPreferredSize() {
 // Test getPreferredSize method
 assertEquals(0, chart.getPreferredSize().width);
 assertEquals(0, chart.getPreferredSize().height);
 }

 @Test
 public void testRenderMethod() {
 // Test render method with title and legend
 chart.setLegend(new Legend(chart.getRowColorModel()));
 chart.setTitle(new Title("TestTitle"));
 renderTitleAndLegend(g);
 // Test render method with title, legend and chart renderers
 for (AbstractChartRenderer cr : chart.getChartRenderer().values()) {
 cr.setBounds(new Rectangle(0, 0, 100, 100));
 cr.render(g);
 }
 renderChartRenderers(g);
 }

 private void renderTitleAndLegend(Graphics2D g) {
 final Title title = chart.getTitle();
 final Legend legend = chart.getLegend();
 title.render(g);
 legend.render(g);
 }

 private void renderChartRenderers(Graphics2D g) {
 final AbstractChartRenderer renderer1 = (AbstractChartRenderer) chart.getChartRenderer().get("Series1");
 final AbstractChartRenderer renderer2 = (AbstractChartRenderer) chart.getChartRenderer().get("Series2");
 renderer1.render(g);
 renderer2.render(g);
 }

}