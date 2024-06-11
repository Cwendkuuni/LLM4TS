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

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.model.RowColorModel;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.render.CoordSystem;
import de.progra.charting.render.Legend;
import de.progra.charting.render.Title;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 model.getData().add(new DataPoint("A1", 1.0, 2.0));
 model.getData().add(new DataPoint("A2", 3.0, 4.0));
 model.getData().add(new DataPoint("A3", 5.0, 6.0));
 model.getData().add(new DataPoint("A4", 7.0, 8.0));
 model.getData().add(new DataPoint("A5", 9.0, 10.0));

 chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testDefaultChartConstruction() {
 assertNotNull(chart);
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertEmpty(chart.getChartRenderer().values());
 }

 @Test
 public void testChartConstructionWithTitle() {
 Title title = new Title("TestTitle");
 chart = new DefaultChart(new ChartDataModel(), "TestChart", title);
 assertNotNull(title);
 assertEquals("TestTitle", title.getText());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertEmpty(chart.getChartRenderer().values());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystem() {
 Title title = new Title("TestTitle");
 CoordSystem coordSystem = new CoordSystem(new ChartDataModel());
 chart = new DefaultChart(new ChartDataModel(), "TestChart", title, DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(title);
 assertEquals("TestTitle", title.getText());
 assertNotNull(coordSystem);
 assertTrue(coordSystem.isLinearX());
 assertTrue(coordSystem.isLinearY());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystemAndAxisLabels() {
 Title title = new Title("TestTitle");
 CoordSystem coordSystem = new CoordSystem(new ChartDataModel());
 chart = new DefaultChart(new ChartDataModel(), "TestChart", title, DefaultChart.LINEAR_X_LINEAR_Y, "XAxisLabel", "YAxisLabel");
 assertNotNull(title);
 assertEquals("TestTitle", title.getText());
 assertNotNull(coordSystem);
 assertTrue(coordSystem.isLinearX());
 assertTrue(coordSystem.isLinearY());
 assertEquals("XAxisLabel", coordSystem.getXAxisUnit());
 assertEquals("YAxisLabel", coordSystem.getYAxisUnit());
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRenderTitle() {
 Title title = new Title("TestTitle");
 chart = new DefaultChart(new ChartDataModel(), title, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 Graphics2D g = mockGraphics2D();
 chart.render(g);
 verify(g).drawString("TestTitle", (float) (chart.getBounds().getWidth() / 2), 0);
 }

 @Test
 public void testRenderLegend() {
 Legend legend = new Legend();
 ChartDataModel model = new ChartDataModel();
 model.setRowColorModel(new RowColorModel(model));
 chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.setLegend(legend);
 Graphics2D g = mockGraphics2D();
 chart.render(g);
 verify(g).drawString("A1", (float) (chart.getBounds().getWidth() - 50), (float) (chart.getBounds().getHeight() / 2));
 verify(g).drawString("A2", (float) (chart.getBounds().getWidth() - 30), (float) (chart.getBounds().getHeight() / 2 + 20));
 verify(g).drawString("A3", (float) (chart.getBounds().getWidth() - 10), (float) (chart.getBounds().getHeight() / 2 + 40));
 verify(g).drawString("A4", (float) (chart.getBounds().getWidth() - 50), (float) (chart.getBounds().getHeight() / 2 + 60));
 verify(g).drawString("A5", (float) (chart.getBounds().getWidth() - 30), (float) (chart.getBounds().getHeight() / 2 + 80));
 }

 @Test
 public void testRenderCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 model.setRowColorModel(new RowColorModel(model));
 chart = new DefaultChart(model, "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 Graphics2D g = mockGraphics2D();
 chart.render(g);
 // Verify that coordinate system is rendered based on the given width and height
 // and the given axis labels
 verify(g).draw(new Rectangle2D.Double(0, 0, chart.getBounds().getWidth() - 50, chart.getBounds().getHeight() - 50));
 // Add more assertions to verify the rendering of the coordinate system based on the given code
 }

 @Test
 public void testRenderChartRenderer() {
 Map<String, AbstractChartRenderer> renderers = new HashMap<>();
 LineChartRenderer lineChartRenderer = mock(LineChartRenderer.class);
 renderers.put("LineChartRenderer", lineChartRenderer);
 chart = new DefaultChart(new ChartDataModel(), "TestChart", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.setChartRenderer(renderers);
 Graphics2D g = mockGraphics2D();
 chart.render(g);
 // Verify that each chart renderer is rendered in the given area, based on the given code
 verify(lineChartRenderer).render(g);
 }

 private Graphics2D mockGraphics2D() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getGraphicsConfiguration()).thenReturn(mockGraphicsConfiguration());
 return g;
 }

 private GraphicsConfiguration mockGraphicsConfiguration() {
 GraphicsConfiguration gc = mock(GraphicsConfiguration.class);
 when(gc.createCompatibleImage(anyInt(), anyInt())).thenReturn(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
 return gc;
 }

}