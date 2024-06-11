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
import de.progra.charting.Model.DataSeries;
import de.progra.charting.Model.XAxisLabel;
import de.progra.charting.Model.YAxisLabel;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 model.addSeries(new DataSeries("Series1", Arrays.asList(1d, 2d, 3d, 4d, 5d)));
 model.addSeries(new DataSeries("Series2", Arrays.asList(2d, 3d, 4d, 5d, 6d)));
 chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testDefaultChartConstruction() {
 // Test for default constructor
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getChartDataModel());
 assertNull(defaultChart.getTitle());
 assertNull(defaultChart.getLegend());
 assertNull(defaultChart.getCoordSystem());

 // Test for constructor with ChartDataModel and title
 ChartDataModel model = new ChartDataModel();
 chart = new DefaultChart(model, "TestChart");
 assertSame(model, chart.getChartDataModel());
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());

 // Test for construction with ChartDataModel, title, and coordinate system
 ChartDataModel model2 = new ChartDataModel();
 model2.addSeries(new DataSeries("Series1", Arrays.asList(1d, 2d, 3d, 4d, 5d)));
 model2.addSeries(new DataSeries("Series2", Arrays.asList(2d, 3d, 4d, 5d, 6d)));
 chart = new DefaultChart(model2, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertSame(model2, chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testGetPreferredSize() {
 // Test for empty chart
 assertNull(chart.getPreferredSize());

 // Test for chart with title and legend
 chart.setTitle(new Title("Test Title"));
 chart.setLegend(new Legend(new RowColorModel(chart.getChartDataModel())));
 chart.getLegend().addLabel(new XAxisLabel("XLabel1"));
 chart.getLegend().addLabel(new XAxisLabel("XLabel2"));
 chart.getLegend().addLabel(new YAxisLabel("YLabel1"));
 chart.getLegend().addLabel(new YAxisLabel("YLabel2"));
 chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));
 chart.getTitle().setTextColor(Color.BLUE);
 assertNotNull(chart.getPreferredSize());
 }

 @Test
 public void testRender() {
 // Test for empty chart
 Graphics2D g = null;
 assertNull(chart.getPreferredSize());
 chart.render(g);

 // Test for chart with title and legend
 Graphics2D g2 = mock(Graphics2D.class);
 chart.setTitle(new Title("Test Title"));
 chart.setLegend(new Legend(new RowColorModel(chart.getChartDataModel())));
 chart.getLegend().addLabel(new XAxisLabel("XLabel1"));
 chart.getLegend().addLabel(new XAxisLabel("XLabel2"));
 chart.getLegend().addLabel(new YAxisLabel("YLabel1"));
 chart.getLegend().addLabel(new YAxisLabel("YLabel2"));
 chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));
 chart.getTitle().setTextColor(Color.BLUE);
 chart.getCoordSystem().setXAxisUnit("XUnit");
 chart.getCoordSystem().setYAxisUnit("YUnit");
 chart.getCoordSystem().setBounds(new Rectangle(0, 0, 500, 500));
 chart.render(g2);
 }

 @Test
 public void testInvalidCoordinateSystem() {
 // Test for construction with invalid coordinate system
 ChartDataModel model3 = new ChartDataModel();
 model3.addSeries(new DataSeries("Series1", Arrays.asList(1d, 2d, 3d, 4d, 5d)));
 model3.addSeries(new DataSeries("Series2", Arrays.asList(2d, 3d, 4d, 5d, 6d)));chart = new DefaultChart(model3, "TestChart", 2); assertNull(chart.getCoordSystem()); }

 @Test(expected = IllegalArgumentException.class) public void testNullChartDataModel() { ChartDataModel model4 = null; chart = new DefaultChart(model4, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y); }

 @Test(expected = IllegalArgumentException.class) public void testNullTitle() { chart.setChartDataModel(new ChartDataModel()); chart.setTitle(null); }

 @Test(expected = IllegalArgumentException.class) public void testNullLegend() { chart.setChartDataModel(new ChartDataModel()); chart.setLegend(null); }

 @Test(expected = IllegalArgumentException.class) public void testNullCoordinateSystem() { chart.setChartDataModel(new ChartDataModel()); chart.setCoordSystem(null); }

 @Test(expected = NullPointerException.class) public void testNullGraphics2D() { chart.setChartDataModel(new ChartDataModel()); chart.render(null); }
}