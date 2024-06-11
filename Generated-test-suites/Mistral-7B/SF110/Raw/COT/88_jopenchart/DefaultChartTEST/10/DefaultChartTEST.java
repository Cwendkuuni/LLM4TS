package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;

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
 private ChartDataModel model;
 private AbstractChartRenderer renderer;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 model.setXAxisLabel("X-axis");
 model.setYAxisLabel("Y-axis");
 model.setDataPoints(Collections.singletonList(new DataPoint(1, 1)));
 chart = new DefaultChart(model, "Test Chart");
 renderer = new TestChartRenderer();
 chart.setChartRenderer(Collections.singletonMap("TestRenderer", renderer));
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart chart = new DefaultChart();
 assertNull(chart.getTitle());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 @Test
 public void testChartConstructionWithTitle() {
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystem() {
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertSame(chart.getCoordSystem(), new CoordSystem(model));
 assertNull(chart.getTitle());
 assertNull(chart.getChartRenderer());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystemAndAxes() {
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-axis", "Y-axis");
 assertSame(chart.getCoordSystem(), new CoordSystem(model));
 assertSame(chart.getCoordSystem().getXAxisUnit(), "X-axis");
 assertSame(chart.getCoordSystem().getYAxisUnit(), "Y-axis");
 assertNull(chart.getTitle());
 assertNull(chart.getChartRenderer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartConstructionWithInvalidCoordinateSystem() {
 DefaultChart chart = new DefaultChart(model, "Test Chart", 2);
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRenderWithNoTitleOrLegend() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 Rectangle2D titleBounds = new Rectangle2D.Double();
 Rectangle2D legendBounds = new Rectangle2D.Double();
 assertFalse(titleBounds.intersects(chart.getBounds()));
 assertFalse(legendBounds.intersects(chart.getBounds()));
 }

 @Test
 public void testRenderWithTitleAndNoLegend() {
 Graphics2D g = createMockGraphics2D();
 Title title = new Title("Test Title");
 chart.setTitle(title);
 chart.render(g);
 title.render(g);
 Rectangle2D titleBounds = new Rectangle2D.Double(chart.getBounds().getX() + chart.getBounds().getWidth() / 2.0 - title.getBounds().getWidth() / 2.0,
 chart.getBounds().getY(), title.getBounds().getWidth(), title.getBounds().getHeight());
 Rectangle2D legendBounds = new Rectangle2D.Double();
 assertFalse(legendBounds.intersects(chart.getBounds()));
 }

 @Test
 public void testRenderWithLegendAndNoTitle() {
 Graphics2D g = createMockGraphics2D();
 Legend legend = new Legend(new RowColorModel(model));
 chart.setLegend(legend);
 chart.render(g);
 legend.render(g);
 Rectangle2D titleBounds = new Rectangle2D.Double();
 Rectangle2D legendBounds = new Rectangle2D.Double(chart.getBounds().getX() + chart.getBounds().getWidth() - legend.getBounds().getWidth(),
 chart.getBounds().getY() + chart.getBounds().getHeight() / 2.0 - legend.getBounds().getHeight() / 2.0, legend.getBounds().getWidth(), legend.getBounds().getHeight());
 assertFalse(titleBounds.intersects(chart.getBounds()));
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 Graphics2D g = createMockGraphics2D();
 Title title = new Title("Test Title");
 Legend legend = new Legend(new RowColorModel(model));
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.render(g);
 title.render(g);
 legend.render(g);
 Rectangle2D titleBounds = new Rectangle2D.Double(chart.getBounds().getX() + chart.getBounds().getWidth() / 2.0 - title.getBounds().getWidth() / 2.0,
 chart.getBounds().getY(), title.getBounds().getWidth(), title.getBounds().getHeight());
 Rectangle2D legendBounds = new Rectangle2D.Double(chart.getBounds().getX() + chart.getBounds().getWidth() - legend.getBounds().getWidth(),
 chart.getBounds().getY() + chart.getBounds().getHeight() / 2.0 - legend.getBounds().getHeight() / 2.0, legend.getBounds().getWidth(), legend.getBounds().getHeight());
 }

 @Test
 public void testRenderWithTitleAndLegendAndRenderer() {
 Graphics2D g = createMockGraphics2D();
 Title title = new Title("Test Title");
 Legend legend = new Legend(new RowColorModel(model));
 DefaultChartRenderer renderer = new DefaultChartRenderer(model, Color.RED);
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setChartRenderer(Collections.singletonMap("DefaultRenderer", renderer));
 chart.render(g);
 title.render(g);
 legend.render(g);
 renderer.render(g);
 Rectangle2D titleBounds = new Rectangle2D.Double(chart.getBounds().getX() + chart.getBounds().getWidth() / 2.0 - title.getBounds().getWidth() / 2.0,
 chart.getBounds().getY(), title.getBounds().getWidth(), title.getBounds().getHeight());
 Rectangle2D legendBounds = new Rectangle2D.Double(chart.getBounds().getX() + chart.getBounds().getWidth() - legend.getBounds().getWidth(),
 chart.getBounds().getY() + chart.getBounds().getHeight() / 2.0 - legend.getBounds().getHeight() / 2.0, legend.getBounds().getWidth(), legend.getBounds().getHeight());
 Rectangle2D rendererBounds = new Rectangle2D.Double(chart.getBounds().getX(), chart.getBounds().getY() + title.getBounds().getHeight() + legend.getBounds().getHeight(),
 chart.getBounds().getWidth(), chart.getBounds().getHeight() - title.getBounds().getHeight() - legend.getBounds().getHeight() - 5);
 }

 @Test
 public void testRenderWithNullGraphics2D() {
 assertThrows(NullPointerException.class, () -> chart.render(null));
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.WHITE);
 return g;
 }

}