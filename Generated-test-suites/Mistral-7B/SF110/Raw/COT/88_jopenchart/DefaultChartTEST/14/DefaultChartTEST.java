package de.progra.charting.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setup() {
 ChartDataModel model = new ChartDataModel();
 this.chart = new DefaultChart(model, "Default Chart");
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart chart = new DefaultChart();
 assertNull(chart.getChartDataModel());
 assertNull(chart.getTitle());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getLegend());
 }

 @Test
 public void testChartWithDataModelAndTitle() {
 assertNotNull(chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getLegend());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartWithInvalidCoordinateSystem() {
 new DefaultChart(new ChartDataModel(), "Default Chart", 2);
 }

 @Test
 public void testChartWithDataModelTitleAndCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 this.chart = new DefaultChart(model, "Default Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNotNull(chart.getCoordSystem());
 assertNull(chart.getLegend());
 }

 @Test
 public void testChartWithDataModelTitleCoordinateSystemAndLegend() {
 ChartDataModel model = new ChartDataModel();
 this.chart = new DefaultChart(model, "Default Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
 assertNotNull(chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNotNull(chart.getCoordSystem());
 assertNotNull(chart.getLegend());
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRenderMethod() {
 // Add some mocks for testing the render method
 Title title = new Title("Test Title");
 Legend legend = new Legend();
 CoordSystem coordSystem = new CoordSystem(new ChartDataModel());
 ChartDataModel model = new ChartDataLineChartDataModel();
 AbstractChartRenderer renderer = new LineChartRenderer(model);
 this.chart = new DefaultChart(model, "Default Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
 this.chart.setTitle(title);
 this.chart.setLegend(legend);
 this.chart.setCoordSystem(coordSystem);
 this.chart.getChartRenderer().put("LineChartRenderer", renderer);

 // Set up mocks for Graphics2D
 Graphics2D graphics = mock(Graphics2D.class);
 when(graphics.getBounds()).thenReturn(new Rectangle(0, 0, 500, 400));

 // Test the render method
 this.chart.render(graphics);

 // Verify the mocks were used correctly
 verify(graphics).setColor(eq(Color.white));
 verify(graphics).fillRect(0, 0, 500, 400);
 verify(graphics).setColor(eq(Color.black));
 verify(title).render(graphics);
 verify(legend).render(graphics);
 verify(coordSystem).render(graphics);
 verify(renderer).render(graphics);
 }

}