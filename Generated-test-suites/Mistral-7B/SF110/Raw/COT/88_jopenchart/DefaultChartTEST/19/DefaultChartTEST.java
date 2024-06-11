package de.progra.charting.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelFactory;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.test.mock.MockAbstractChartRenderer;
import de.progra.charting.test.mock.MockChartDataModel;
import de.progra.charting.test.mock.MockCoordSystem;
import de.progra.charting.test.mock.MockLegend;
import de.progra.charting.test.mock.MockTitle;

public class DefaultChartTest {

 private DefaultChart chart;
 private AbstractChartRenderer renderer;

 @Before
 public void setUp() {
 chart = new DefaultChart(new MockChartDataModel(), "Test Chart");
 renderer = new MockAbstractChartRenderer();
 chart.getChartRenderer().put(DefaultChart.LINEAR_X_LINEAR_Y, renderer);
 }

 @Test
 public void testDefaultConstructor() {
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getTitle());
 assertNull(defaultChart.getLegend());
 assertNull(defaultChart.getCoordSystem());
 }

 @Test
 public void testChartWithTitleConstructor() {
 ChartDataModel model = new MockChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 assertEquals("Test Chart", chart.getTitle().getText());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemConstructor() {
 ChartDataModel model = new MockChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemAndAxisLabelsConstructor() {
 ChartDataModel model = new MockChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis Label", "Y Axis Label");
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 assertEquals("X Axis Label", coordSystem.getXAxisUnit());
 assertEquals("Y Axis Label", coordSystem.getYAxisUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCoordinateSystemType() {
 ChartDataModel model = new MockChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Test Chart", -1);
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRender() {
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 }

 @Test
 public void testRenderWithTitle() {
 Graphics2D g = mock(Graphics2D.class);
 Title title = new MockTitle("Test Title");
 chart.setTitle(title);
 chart.render(g);
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g).setColor(eq(Color.black));
 }

 @Test
 public void testRenderWithLegend() {
 Graphics2D g = mock(Graphics2D.class);
 Legend legend = new MockLegend();
 chart.setLegend(legend);
 chart.render(g);
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 Graphics2D g = mock(Graphics2D.class);
 Title title = new MockTitle("Test Title");
 Legend legend = new MockLegend();
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.render(g);
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 }

 @Test
 public void testRenderWithTitleLegendAndCoordinateSystem() {
 Graphics2D g = mock(Graphics2D.class);
 Title title = new MockTitle("Test Title");
 Legend legend = new MockLegend();
 CoordSystem coordSystem = new MockCoordSystem();
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(coordSystem);
 chart.render(g);
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 }

 @Test
 public void testRenderWithTitleLegendCoordinateSystemAndRenderer() {
 Graphics2D g = mock(Graphics2D.class);
 Title title = new MockTitle("Test Title");
 Legend legend = new MockLegend();
 CoordSystem coordSystem = new MockCoordSystem();
 AbstractChartRenderer renderer = new MockAbstractChartRenderer();
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(coordSystem);
 chart.getChartRenderer().put(DefaultChart.LINEAR_X_LINEAR_Y, renderer);
 chart.render(g);
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(renderer).render(g);
 }
}