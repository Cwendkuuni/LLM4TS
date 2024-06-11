package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 model.setData(new ArrayList<DataPoint>());
 chart = new DefaultChart(model, "Test Chart");
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
 public void testChartWithTitle() {
 Title title = new Title("Test Title");
 chart = new DefaultChart(new ChartDataModel(), "Test Chart", title);
 assertEquals("Test Title", chart.getTitle().getText());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystem() {
 Title title = new Title("Test Title");
 CoordSystem coordSystem = new CoordSystem(new ChartDataModel());
 chart = new DefaultChart(new ChartDataModel(), "Test Chart", title, CoordSystem.LINEAR_X_LINEAR_Y, null, null);
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemAndUnits() {
 Title title = new Title("Test Title");
 CoordSystem coordSystem = new CoordSystem(new ChartDataModel());
 chart = new DefaultChart(new ChartDataModel(), "Test Chart", title, CoordSystem.LINEAR_X_LINEAR_Y, "UnitsX", "UnitsY");
 assertNotNull(chart.getCoordSystem());
 assertEquals("UnitsX", chart.getCoordSystem().getXAxisUnit());
 assertEquals("UnitsY", chart.getCoordSystem().getYAxisUnit());
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test(expected = NullPointerException.class)
 public void testRenderNullGraphics2D() {
 chart.render(null);
 }

 @Test
 public void testRender() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);

 // Verify the chart was rendered correctly
 verify(g).setColor(Color.WHITE);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.BLACK);

 // Verify the title was rendered correctly
 Title title = chart.getTitle();
 if (title != null) {
 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(width / 2 - (int)(title.getPreferredSize().getWidth() / 2.0), 0, (int)title.getPreferredSize().getWidth(), (int)title.getPreferredSize().getHeight());
 verify(title).render(g);
 }

 // Verify the legend was rendered correctly
 Legend legend = chart.getLegend();
 if (legend != null) {
 int legendWidth = legend.getPreferredSize().getWidth();
 int legendHeight = legend.getPreferredSize().getHeight();
 int x = (int)(width - legendWidth);
 int y = height / 2 - legendHeight / 2 + title.getPreferredSize().getHeight();
 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(x, y, legendWidth, legendHeight);
 verify(legend).render(g);
 }

 // Verify the coordinate system was rendered correctly
 CoordSystem coordSystem = chart.getCoordSystem();
 if (coordSystem != null) {
 int x = 0;
 int y = title.getPreferredSize().getHeight();
 int width = width - legendWidth;
 int height = height - title.getPreferredSize().getHeight() - legendHeight - 5;
 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(x, y, width, height);
 verify(coordSystem).render(g);
 }

 // Verify the chart renderers were rendered correctly
 List<AbstractChartRenderer> renderers = new ArrayList<>(chart.getChartRenderer().values());
 for (AbstractChartRenderer renderer : renderers) {
 int x = 0;
 int y = title.getPreferredSize().getHeight();
 int width = width - legendWidth;
 int height = height - title.getPreferredSize().getHeight() - legendHeight - 5;
 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(x, y, width, height);
 verify(renderer).render(g);
 }
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.WHITE);
 when(g.fillRect(anyInt(), anyInt(), anyInt(), anyInt())).thenAnswer(invocation -> {});
 when(g.setColor(any(Color.class))).thenAnswer(invocation -> {});
 return g;
 }
}