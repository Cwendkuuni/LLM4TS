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
 public void setup() {
 ChartDataModel model = new ChartDataModel("Test Data", Arrays.asList(new DataPoint(1, 1), new DataPoint(2, 2), new DataPoint(3, 3)));
 this.chart = new DefaultChart(model, "Default Chart Test");
 }

 @Test
 public void testDefaultChartConstruction() {
 // Test default constructor
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getChartDataModel());
 assertNull(defaultChart.getRowColorModel());
 assertNull(defaultChart.getLegend());
 assertNull(defaultChart.getTitle());
 assertNull(defaultChart.getCoordSystem());

 // Test constructor with ChartDataModel and title
 DefaultChart chartWithModelAndTitle = new DefaultChart(new ChartDataModel("Test Data", Collections.emptyList()), "Test Chart");
 assertNotNull(chartWithModelAndTitle.getChartDataModel());
 assertNull(chartWithModelAndTitle.getRowColorModel());
 assertNull(chartWithModelAndTitle.getLegend());
 assertNotNull(chartWithModelAndTitle.getTitle());
 assertNull(chartWithModelAndTitle.getCoordSystem());

 // Test constructor with ChartDataModel, title, and coordinate system
 ChartDataModel model = new ChartDataModel("Test Data", Arrays.asList(new DataPoint(1, 1), new DataPoint(2, 2), new DataPoint(3, 3)));
 DefaultChart chartWithModelTitleAndCoord = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chartWithModelTitleAndCoord.getChartDataModel());
 assertNotNull(chartWithModelTitleAndCoord.getRowColorModel());
 assertNotNull(chartWithModelTitleAndCoord.getLegend());
 assertNotNull(chartWithModelTitleAndCoord.getTitle());
 assertNotNull(chartWithModelTitleAndCoord.getCoordSystem());
 }

 @Test
 public void testGetPreferredSize() {
 // Test getPreferredSize method
 Rectangle bounds = new Rectangle(0, 0, 500, 400);
 this.chart.setBounds(bounds);
 Dimension size = this.chart.getPreferredSize();
 assertEquals(0, size.width);
 assertEquals(0, size.height);
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 // Test render method with title and legend
 Graphics2D g = this.getMockGraphics2D();
 this.chart.render(g);
 Title title = this.chart.getTitle();
 Legend legend = this.chart.getLegend();
 assertEquals(Color.WHITE, g.getColor());
 g.setColor(Color.BLACK);
 assertTrue(g.isDrawingGray());
 if (title != null) {
 int titleheight = (int)title.getBounds().getHeight();
 int legendwidth = (int)legend.getBounds().getWidth();
 int width = bounds.width;
 int height = bounds.height;
 int x = width / 2 - (int)(title.getWidth() / 2.0);
 int y = 0;
 title.setBounds(new Rectangle(x, y, (int)title.getWidth(), titleheight));
 title.render(g);
 x += title.getWidth();
 x -= legendwidth;
 legend.setBounds(new Rectangle(x, (int)(height / 2 - legend.getHeight() / 2.0), legendwidth, legend.getHeight()));
 legend.render(g);
 assertEquals(titleheight + legend.getHeight(), g.getTranslationTranslation().getY());
 }
 }

 @Test
 public void testRenderWithCoordinateSystem() {
 // Test render method with coordinate system
 Graphics2D g = this.getMockGraphics2D();
 this.chart.setCoordSystem(new CoordSystem(new ChartDataModel("Test Data", Arrays.asList(new DataPoint(1, 1), new DataPoint(2, 2), new DataPoint(3, 3)))));
 this.chart.render(g);
 CoordSystem coordSystem = this.chart.getCoordSystem();
 assertEquals(Color.WHITE, g.getColor());
 g.setColor(Color.BLACK);
 assertTrue(g.isDrawingGray());
 int width = bounds.width;
 int height = bounds.height;
 int x = 0;
 int y = bounds.height - coordSystem.getHeight();
 coordSystem.setBounds(new Rectangle(x, y, width, coordSystem.getHeight()));
 coordSystem.render(g);
 }

 @Test
 public void testRenderWithChartRenderer() {
 // Test render method with chart renderer
 Graphics2D g = this.getMockGraphics2D();
 List<DataPoint> data = Arrays.asList(new DataPoint(1, 1), new DataPoint(2, 2), new DataPoint(3, 3));
 ChartDataModel model = new ChartDataModel("Test Data", data);
 this.chart.setChartDataModel(model);
 this.chart.setCoordSystem(new CoordSystem(model));
 this.chart.setRowColorModel(new RowColorModel(model));
 this.chart.setLegend(new Legend(this.chart.getRowColorModel()));
 this.chart.setTitle(new Title("Default Chart Test"));
 List<AbstractChartRenderer> renderers = Arrays.asList(new LineChartRenderer(), new BarChartRenderer());
 this.chart.setChartRenderer(renderers);
 this.chart.render(g);
 for (AbstractChartRenderer renderer : renderers) {
 renderer.setBounds(new Rectangle(0, 0, width - legendwidth, height - titleheight - 5));
 renderer.render(g);
 }
 }

 private Graphics2D getMockGraphics2D() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.WHITE);
 when(g.isDrawingGray()).thenReturn(false);
 return g;
 }
}