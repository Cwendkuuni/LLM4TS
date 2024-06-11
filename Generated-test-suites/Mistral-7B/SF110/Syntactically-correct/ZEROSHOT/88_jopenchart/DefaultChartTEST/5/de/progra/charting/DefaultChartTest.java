package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;

import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private DefaultChart chart;

 @Test
 public void testDefaultChartConstruction() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart defaultChart = new DefaultChart(model, "Default Chart");
 assertNotNull(defaultChart.getTitle());
 assertNotNull(defaultChart.getLegend());
 assertNull(defaultChart.getCoordSystem());
 }

 @Test
 public void testChartWithCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart defaultChart = new DefaultChart(model, "Default Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 CoordSystem coordSystem = defaultChart.getCoordSystem();
 assertNotNull(coordSystem);
 }

 @Test
 public void testChartWithTitleAndCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart defaultChart = new DefaultChart(model, "Default Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
 CoordSystem coordSystem = defaultChart.getCoordSystem();
 assertNotNull(coordSystem);
 assertTrue(defaultChart.getCoordSystem().getXAxisUnit().equals("X Axis"));
 assertTrue(defaultChart.getCoordSystem().getYAxisUnit().equals("Y Axis"));
 }

 @Test
 public void testGetPreferredSize() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart defaultChart = new DefaultChart(model, "Default Chart");
 assertNull(defaultChart.getPreferredSize());
 }

 @Test
 public void testRenderMethod() {
 ChartDataModel model = new ChartDataModel();
 Title title = new Title("Test Chart Title");
 Legend legend = new Legend(new RowColorModel(model));
 DefaultChart defaultChart = new DefaultChart(model, "Default Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 defaultChart.setTitle(title);
 defaultChart.setLegend(legend);

 Graphics2D graphics = createGraphics2D();
 defaultChart.render(graphics);

 Title renderedTitle = defaultChart.getTitle();
 Legend renderedLegend = defaultChart.getLegend();

 assertEquals("Test Chart Title", renderedTitle.getText());
 assertEquals("Default Chart", renderedLegend.getLabel());
 }

 @Test
 public void testRenderMethodWithMultipleRenderers() {
 ChartDataModel model = new ChartDataLineChartDataModel();
 Title title = new Title("Test Chart Title");
 Legend legend = new Legend(new RowColorModel(model));
 DefaultChart defaultChart = new DefaultChart(model, "Default Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 defaultChart.setTitle(title);
 defaultChart.setLegend(legend);

 LineChartRenderer lineChartRenderer = new LineChartRenderer();
 BarChartRenderer barChartRenderer = new BarChartRenderer();
 defaultChart.addChartRenderer(lineChartRenderer);
 defaultChart.addChartRenderer(barChartRenderer);

 Graphics2D graphics = createGraphics2D();
 defaultChart.render(graphics);

 Title renderedTitle = defaultChart.getTitle();
 Legend renderedLegend = defaultChart.getLegend();
 LineChartRenderer renderedLineChartRenderer = (LineChartRenderer) defaultChart.getChartRenderer().values().toArray()[0];
 BarChartRenderer renderedBarChartRenderer = (BarChartRenderer) defaultChart.getChartRenderer().values().toArray()[1];

 assertEquals("Test Chart Title", renderedTitle.getText());
 assertEquals("Default Chart", renderedLegend.getLabel());
 assertEquals(Color.BLUE, renderedLineChartRenderer.getLineColor());
 assertEquals(Color.RED, renderedBarChartRenderer.getFillColor());
 }

 private Graphics2D createGraphics2D() {
 GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
 return gc.createGraphics2D();
 }

}