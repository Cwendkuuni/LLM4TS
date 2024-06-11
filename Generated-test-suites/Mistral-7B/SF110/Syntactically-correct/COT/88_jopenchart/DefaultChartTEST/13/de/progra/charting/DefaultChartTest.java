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
 public void setup() {
 ChartDataModel model = new ChartDataModel();
 model.setData(new ArrayList<>());
 this.chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart chart = new DefaultChart();
 assertNull(chart.getChartDataModel());
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 @Test
 public void testChartWithTitleConstruction() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart");
 assertNotNull(chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemConstruction() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemAndAxesConstruction() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
 assertNotNull(chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartWithInvalidCoordinateSystemArgument() {
 DefaultChart chart = assertThrows(IllegalArgumentException.class, () -> new DefaultChart(new ChartDataModel(), "Test Chart", -1));
 }

 @Test
 public void testGetPreferredSizeReturnsNull() {
 assertEquals(null, chart.getPreferredSize());
 }

 @Test
 public void testRenderTitleAndLegend() {
 Title title = new Title("Test Title");
 Legend legend = new Legend(new RowColorModel(new ChartDataModel()));
 ChartDataModel model = new ChartDataModel();
 model.setData(new ArrayList<>());
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 Title expectedTitle = new Title("Test Title");
 Legend expectedLegend = new Legend(new RowColorModel(new ChartDataModel()));
 chart.setTitle(title);
 chart.setLegend(legend);
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, eq(chart.getBounds().getWidth()), eq(chart.getBounds().getHeight()));
 verify(g).setColor(eq(Color.white));
 verify(g).fillRect(0, 0, eq(chart.getBounds().getWidth()), eq(chart.getBounds().getHeight()));
 verify(g).setColor(eq(Color.black));
 Title actualTitle = chart.getTitle();
 assertEquals(title, actualTitle);
 Legend actualLegend = chart.getLegend();
 assertEquals(legend, actualLegend);
 }

 @Test
 public void testRenderChartRenderer() {
 ChartDataModel model = new ChartDataModel();
 model.setData(new ArrayList<>());
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 chart.getChartRenderer().put("TestRenderer", new TestChartRenderer());
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, eq(chart.getBounds().getWidth()), eq(chart.getBounds().getHeight()));
 verify(g).setColor(eq(Color.white));
 verify(g).fillRect(0, 0, eq(chart.getBounds().getWidth()), eq(chart.getBounds().getHeight()));
 verify(g).setColor(eq(Color.black));
 TestChartRenderer actualRenderer = chart.getChartRenderer().get("TestRenderer");
 assertNotNull(actualRenderer);
 }

 @Test
 public void testRenderCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 model.setData(new ArrayList<>());
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 chart.setCoordSystem(new CoordSystem(model));
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, eq(chart.getBounds().getWidth()), eq(chart.getBounds().getHeight()));
 verify(g).setColor(eq(Color.white));
 verify(g).fillRect(0, 0, eq(chart.getBounds().getWidth()), eq(chart.getBounds().getHeight()));
 verify(g).setColor(eq(Color.black));
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 }
}