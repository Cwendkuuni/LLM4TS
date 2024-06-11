package de.progra.charting.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;
import java.awt.*;
import java.util.*;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 ChartData data = new ChartData("Test Data", new double[][]{{1.0, 2.0, 3.0}});
 model.setChartData(data);
 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart chart = new DefaultChart();
 assertNotNull(chart);
 }

 @Test
 public void testDefaultChartConstructionWithTitle() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart");
 assertNotNull(chart);
 }

 @Test
 public void testDefaultChartConstructionWithTitleAndCoordinateSystem() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart);
 }

 @Test
 public void testDefaultChartConstructionWithTitleAndCoordinateSystemAndAxisLabels() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "x-axis", "y-axis");
 assertNotNull(chart);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCoordinateSystem() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", 2);
 }

 @Test
 public void testGetterSetTitle() {
 String title = "Test Chart";
 chart.setTitle(new Title(title));
 Title titleComponent = chart.getTitle();
 assertTrue(title.equals(titleComponent.getText()));
 }

 @Test
 public void testGetterSetLegend() {
 Legend legend = new Legend();
 chart.setLegend(legend);
 Legend legendComponent = chart.getLegend();
 assertNotNull(legendComponent);
 }

 @Test
 public void testGetterSetCoordinateSystem() {
 CoordSystem coordSystem = new CoordSystem(new ChartDataModel());
 chart.setCoordSystem(coordSystem);
 CoordSystem coordSystemComponent = chart.getCoordSystem();
 assertNotNull(coordSystemComponent);
 }

 @Test
 public void testRenderMethod() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 chart.render(g);
 }

 @Test
 public void testGetPreferredSizeMethod() {
 Dimension dimension = chart.getPreferredSize();
 assertNotNull(dimension);
 }

}