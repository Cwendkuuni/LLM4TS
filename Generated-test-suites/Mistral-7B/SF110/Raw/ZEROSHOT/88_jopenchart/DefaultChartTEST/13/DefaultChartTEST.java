package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 model.addSeries(new ModelDataSeries("Series1", Arrays.asList(1d, 2d, 3d)));
 model.addSeries(new ModelDataSeries("Series2", Arrays.asList(4d, 5d, 6d)));
 chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testDefaultChartConstruction() {
 assertEquals("Title", chart.getTitle().getText());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testDefaultChartConstructionWithTitle() {
 ChartDataModel model = new ChartDataModel();
 model.addSeries(new ModelDataSeries("Series1", Arrays.asList(1d, 2d, 3d)));
 model.addSeries(new ModelDataSeries("Series2", Arrays.asList(4d, 5d, 6d)));
 chart = new DefaultChart(model, "Title");
 assertEquals("Title", chart.getTitle().getText());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testDefaultChartConstructionWithTitleAndCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 model.addSeries(new ModelDataSeries("Series1", Arrays.asList(1d, 2d, 3d)));
 model.addSeries(new ModelDataSeries("Series2", Arrays.asList(4d, 5d, 6d)));
 chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "Xaxis", "Yaxis");
 assertEquals("Title", chart.getTitle().getText());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 assertEquals("Xaxis", chart.getCoordSystem().getXAxisUnit());
 assertEquals("Yaxis", chart.getCoordSystem().getYAxisUnit());
 }

 @Test
 public void testGetPreferredSize() {
 assertEquals(0, chart.getPreferredSize().getWidth());
 assertEquals(0, chart.getPreferredSize().getHeight());
 }

 @Test
 public void testRenderWithNoTitleAndNoLegend() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verifyNoMoreInteractions(g);
 }

 @Test
 public void testRenderWithTitle() {
 Graphics2D g = createMockGraphics2D();
 chart.getTitle().setBounds(new Rectangle2D.Double(10, 10, 100, 30));
 chart.render(g);
 verify(g).setColor(Color.WHITE);
 verify(g).fillRect(0, 0, 600, 400);
 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(250, 10, 100, 30);
 // Title rendering
 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(250, 10, 100, 30);
 // Legend rendering is skipped because it has not been set
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 Graphics2D g = createMockGraphics2D();
 Legend legend = new Legend(new RowColorModel(chart.getChartDataModel()));
 legend.setBounds(new Rectangle2D.Double(500, 200, 100, 30));
 chart.setLegend(legend);
 chart.getTitle().setBounds(new Rectangle2D.Double(10, 10, 100, 30));
 chart.render(g);
 verify(g).setColor(Color.WHITE);
 verify(g).fillRect(0, 0, 600, 400);
 // Title rendering
 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(250, 10, 100, 30);
 // Legend rendering
 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(500, 200, 100, 30);
 }

 @Test
 public void testRenderWithTitleLegendAndDataSeries() {
 Graphics2D g = createMockGraphics2D();
 Legend legend = new Legend(new RowColorModel(chart.getChartDataModel()));
 legend.setBounds(new Rectangle2D.Double(500, 200, 100, 30));
 chart.setLegend(legend);
 ChartDataModel model = chart.getChartDataModel();
 ModelDataSeries series1 = model.getSeriesList().get(0);
 series1.setColor(Color.RED);
 model.setSeriesList(Collections.singletonList(series1));
 chart.getTitle().setBounds(new Rectangle2D.Double(10, 10, 100, 30));
 chart.render(g);
 verify(g).setColor(Color.WHITE);
 verify(g).fillRect(0, 0, 600, 400);
 // Title rendering
 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(250, 10, 100, 30);
 // Legend rendering
 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(500, 200, 100, 30);
 // Series rendering
 for (Map.Entry<String, AbstractChartRenderer> entry : chart.getChartRenderer().entrySet()) {
 AbstractChartRenderer renderer = entry.getValue();
 verify(renderer).render(g);
 }
 }

 @Test
 public void testRenderWithTitleLegendAndDataSeriesAndCoordinateSystem() {
 Graphics2D g = createMockGraphics2D();
 Legend legend = new Legend(new RowColorModel(chart.getChartDataModel()));
 legend.setBounds(new Rectangle2D.Double(500, 200, 100, 30));
 ChartDataModel model = chart.getChartDataModel();
 model.addSeries(new ModelDataSeries("Series1", Arrays.asList(1d, 2d, 3d)));
 model.addSeries(new ModelDataSeries("Series2", Arrays.asList(4d, 5d, 6d)));
 ChartDataSeries series1 = model.getSeriesList().get(0);
 series1.setColor(Color.RED);
 model.setSeriesList(Arrays.asList(series1, series2));
 CoordSystem coordSystem = new CoordSystem(model);
 coordSystem.setBounds(new Rectangle2D.Double(10, 100, 580, 290));
 chart.setLegend(legend);
 chart.setCoordSystem(coordSystem);
 chart.getTitle().setBounds(new Rectangle2D.Double(10, 10, 100, 30));
 chart.render(g);
 verify(g).setColor(Color.WHITE);
 verify(g).fillRect(0, 0, 600, 400);
 // Title rendering
 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(250, 10, 100, 30);
 // Legend rendering
 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(500, 200, 100, 30);
 // Series rendering
 for (Map.Entry<String, AbstractChartRenderer> entry : chart.getChartRenderer().entrySet()) {
 AbstractChartRenderer renderer = entry.getValue();
 verify(renderer).render(g);
 }
 // Coordinate system rendering
 verify(coordSystem).render(g);
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 return g;
 }
}