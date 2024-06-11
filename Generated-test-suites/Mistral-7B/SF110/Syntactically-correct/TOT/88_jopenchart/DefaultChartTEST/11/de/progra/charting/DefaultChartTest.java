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
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.model.DataPoint;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 model.getSeries().add(new Series("Series1", Arrays.asList(new DataPoint(1, 1), new DataPoint(2, 2), new DataPoint(3, 3))));
 model.getSeries().add(new Series("Series2", Arrays.asList(new DataPoint(1, 4), new DataPoint(2, 5), new DataPoint(3, 6))));
 chart = new DefaultChart(model, "TestChart");
 }

 @Test
 public void testDefaultChartConstruction() {
 AbstractChart chart = this.chart;
 assertNotNull(chart);
 assertEquals("TestChart", chart.getTitle().getText());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getLegend());
 }

 @Test
 public void testChartWithTitle() {
 AbstractChart chart = this.chart;
 Title title = chart.getTitle();
 assertNotNull(title);
 assertEquals("TestChart", title.getText());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 model.getSeries().add(new Series("Series1", Arrays.asList(new DataPoint(1, 1), new DataPoint(2, 2), new DataPoint(3, 3))));
 model.getSeries().add(new Series("Series2", Arrays.asList(new DataPoint(1, 4), new DataPoint(2, 5), new DataPoint(3, 6))));
 DefaultChart chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemAndAxisLabels() {
 ChartDataModel model = new ChartDataModel();
 model.getSeries().add(new Series("Series1", Arrays.asList(new DataPoint(1, 1), new DataPoint(2, 2), new DataPoint(3, 3))));
 model.getSeries().add(new Series("Series2", Arrays.asList(new DataPoint(1, 4), new DataPoint(2, 5), new DataPoint(3, 6))));
 DefaultChart chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y, "XaxisLabel", "YaxisLabel");
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 assertEquals("XaxisLabel", coordSystem.getXAxisUnit());
 assertEquals("YaxisLabel", coordSystem.getYAxisUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartWithInvalidCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 model.getSeries().add(new Series("Series1", Arrays.asList(new DataPoint(1, 1), new DataPoint(2, 2), new DataPoint(3, 3))));
 model.getSeries().add(new Series("Series2", Arrays.asList(new DataPoint(1, 4), new DataPoint(2, 5), new DataPoint(3, 6))));
 DefaultChart chart = new DefaultChart(model, "TestChart", 2);
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testRender() {
 AbstractChart chart = this.chart;
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 // Add assertions for the rendered components, e.g. checking the bounds and colors
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullGraphics() {
 AbstractChart chart = this.chart;
 chart.render(null);
 }

 @Test
 public void testSetTitle() {
 AbstractChart chart = this.chart;
 chart.setTitle(new Title("NewTitle"));
 Title title = chart.getTitle();
 assertEquals("NewTitle", title.getText());
 }

 @Test
 public void testSetLegend() {
 AbstractChart chart = this.chart;
 chart.setLegend(new Legend(new RowColorModel(chart.getChartDataModel())));
 Legend legend = chart.getLegend();
 assertNotNull(legend);
 }

 @Test
 public void testSetRowColorModel() {
 ChartDataModel model = new ChartDataSeriesModel();
 model.setSeries(Arrays.asList(new Series("Series1", Arrays.asList(new DataPoint(1, 1), new DataPoint(2, 2), new DataPoint(3, 3)), Color.RED)));
 model.setSeries(Arrays.asList(new Series("Series2", Arrays.asList(new DataPoint(1, 4), new DataPoint(2, 5), new DataPoint(3, 6)), Color.BLUE)));
 DefaultChart chart = new DefaultChart(model, "TestChart");
 RowColorModel rowColorModel = new RowColorModel(model);
 chart.setRowColorModel(rowColorModel);
 assertEquals(rowColorModel, chart.getRowColorModel());
 }

 @Test(expected = NullPointerException.class)
 public void testSetRowColorModelWithNullModel() {
 DefaultChart chart = new DefaultChart(null, "TestChart");
 try {
 chart.setRowColorModel(new RowColorModel(null));
 } catch (NullPointerException e) {
 fail("Expected NullPointerException, but got " + e.getMessage());
 }
 }
}