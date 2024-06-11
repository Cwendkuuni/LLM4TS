package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.render.*;
import de.progra.charting.DefaultChart;
import de.progra.charting.AbstractChart;
import de.progra.charting.AbstractChartRenderer;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.Title;
import de.progra.charting.RowColorModel;

public class DefaultChartTest {

 private AbstractChart chart;
 private AbstractChartRenderer renderer;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 model.addSeries("Series1", Arrays.asList(1D, 2D, 3D, 4D, 5D));
 model.addSeries("Series2", Arrays.asList(6D, 7D, 8D, 9D, 10D));
 chart = new DefaultChart(model, "Test Chart");
 renderer = new LineChartRenderer();
 chart.setChartRenderer(renderer);
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getChartDataModel());
 assertNull(defaultChart.getRowColorModel());
 assertNull(defaultChart.getLegend());
 assertNull(defaultChart.getTitle());
 assertNull(defaultChart.getCoordSystem());
 }

 @Test
 public void testChartConstructionWithTitle() {
 AbstractChart chart = new DefaultChart(new ChartDataModel(), "Test Chart");
 Title title = chart.getTitle();
 assertEquals("Test Chart", title.getText());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystem() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystemAndLabels() {
 DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "XAxisLabel", "YAxisLabel");
 CoordSystem coordSystem = chart.getCoordSystem();
 assertEquals("XAxisLabel", coordSystem.getXAxisUnit());
 assertEquals("YAxisLabel", coordSystem.getYAxisUnit());
 }

 @Test
 public void testGetPreferredSize() {
 Dimension preferredSize = chart.getPreferredSize();
 assertNotNull(preferredSize);
 }

 @Test(expected = NullPointerException.class)
 public void testRenderNullGraphics() {
 chart.render(null);
 }

 @Test
 public void testRender() {
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, width(chart), height(chart));
 verify(g).setColor(Color.BLACK);
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 Graphics2D g = mock(Graphics2D.class);
 Title title = new Title("Test Chart");
 Legend legend = new Legend(new RowColorModel(new ChartDataModel()));
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.render(g);
 verify(g).fillRect(width(chart) / 2 - (int)(title.getPreferredSize().getWidth() / 2.0), 0, (int)title.getPreferredSize().getWidth(), (int)title.getPreferredSize().getHeight());
 verify(title).render(g);
 verify(g).fillRect((int)(width(chart) - legend.getPreferredSize().getWidth()), (int)(height(chart) / 2 - legend.getPreferredSize().getHeight() / 2.0), (int)legend.getPreferredSize().getWidth(), (int)legend.getPreferredSize().getHeight());
 verify(legend).render(g);
 }

 @Test
 public void testRenderWithTitleAndLegendAndData() {
 Graphics2D g = mock(Graphics2D.class);
 Title title = new Title("Test Chart");
 Legend legend = new Legend(new RowColorModel(new ChartDataModel()));
 CoordSystem coordSystem = new CoordSystem(new ChartDataModel());
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(coordSystem);
 chart.getChartRenderer().put("Renderer1", new LineChartRenderer());
 chart.render(g);
 verify(g).fillRect(width(chart) / 2 - (int)(title.getPreferredSize().getWidth() / 2.0), 0, (int)title.getPreferredSize().getWidth(), (int)title.getPreferredSize().getHeight());
 verify(title).render(g);
 verify(g).fillRect((int)(width(chart) - legend.getPreferredSize().getWidth()), (int)(height(chart) / 2 - legend.getPreferredSize().getHeight() / 2.0), (int)legend.getPreferredSize().getWidth(), (int)legend.getPreferredSize().getHeight());
 verify(legend).render(g);
 verify(g).setColor(Color.WHITE);
 verify(g).fillRect(0, 0, width(chart), height(chart));
 List<AbstractChartRenderer> renderers = Collections.list(chart.getChartRenderer().values());
 for (AbstractChartRenderer renderer : renderers) {
 renderer.render(g);
 }
 verify(g).setColor(Color.BLACK);
 }

}