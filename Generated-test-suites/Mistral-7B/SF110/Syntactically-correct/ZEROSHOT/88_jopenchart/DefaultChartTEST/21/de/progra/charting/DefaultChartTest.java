package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelType;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.ChartRenderer;
import de.progra.charting.render.LineChartRenderer;
import de.progra.charting.render.PieChartRenderer;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel(ModelType.LINE);
 model.addSeries("Series1", Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
 model.addSeries("Series2", Arrays.asList(2.0, 3.0, 4.0, 5.0, 6.0));
 RowColorModel colorModel = new RowColorModel(model);
 chart = new DefaultChart(model, "DefaultChart", DefaultChart.LINEAR_X_LINEAR_Y);
 ChartRenderer chartRenderer = new HashMap<String, ChartRenderer>() {{
 put("LineChartRenderer", new LineChartRenderer(colorModel));
 put("PieChartRenderer", new PieChartRenderer(colorModel));
 }};
 chart.setChartRenderer(chartRenderer);
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getChartDataModel());
 assertNull(defaultChart.getTitle());
 assertNull(defaultChart.getLegend());
 assertNull(defaultChart.getCoordSystem());
 }

 @Test
 public void testDefaultChartConstructionWithTitle() {
 DefaultChart defaultChart = new DefaultChart(new ChartDataModel(ModelType.LINE), "DefaultChartWithTitle");
 assertNotNull(defaultChart.getTitle());
 assertEquals("DefaultChartWithTitle", defaultChart.getTitle().getText());
 assertNull(defaultChart.getLegend());
 assertNull(defaultChart.getCoordSystem());
 }

 @Test
 public void testDefaultChartConstructionWithTitleAndCoordinateSystem() {
 DefaultChart defaultChart = new DefaultChart(new ChartDataModel(ModelType.LINE), "DefaultChartWithTitleAndCoordSys", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(defaultChart.getTitle());
 assertEquals("DefaultChartWithTitleAndCoordSys", defaultChart.getTitle().getText());
 assertNotNull(defaultChart.getCoordSystem());
 }

 @Test
 public void testDefaultChartConstructionWithTitleAndCoordinateSystemAndAxisLabels() {
 DefaultChart defaultChart = new DefaultChart(new ChartDataModel(ModelType.LINE), "DefaultChartWithTitleAndCoordSysAndAxisLabels", DefaultChart.LINEAR_X_LINEAR_Y, "X-AxisLabel", "Y-AxisLabel");
 assertNotNull(defaultChart.getTitle());
 assertEquals("DefaultChartWithTitleAndCoordSysAndAxisLabels", defaultChart.getTitle().getText());
 assertNotNull(defaultChart.getCoordSystem());
 assertEquals("X-AxisLabel", defaultChart.getCoordSystem().getXAxisUnit());
 assertEquals("Y-AxisLabel", defaultChart.getCoordSystem().getYAxisUnit());
 }

 @Test
 public void testGetPreferredSize() {
 Dimension size = chart.getPreferredSize();
 assertNotNull(size);
 }

 @Test(expected = NullPointerException.class)
 public void testRenderNullGraphics2D() {
 chart.render(null);
 }

 @Test
 public void testRender() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verify(g).setColor(Color.white);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.black);
 verify(g).setFont(anyFont());
 verify(g).drawString("DefaultChart", width / 2, 10);
 }

 @Test
 public void testRenderWithTitle() {
 Graphics2D g = createMockGraphics2D();
 Title title = new Title("TestTitle");
 chart.setTitle(title);
 chart.render(g);
 verify(g).setColor(Color.white);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.black);
 verify(g).setFont(anyFont());
 verify(g).drawString("TestTitle", width / 2, 10);
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 Graphics2D g = createMockGraphics2D();
 Title title = new Title("TestTitle");
 Legend legend = new Legend();
 ChartDataModel model = new ChartDataModel(ModelType.LINE);
 RowColorModel colorModel = new RowColorModel(model);
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setChartRenderer(new HashMap<String, ChartRenderer>() {{
 put("LineChartRenderer", new LineChartRenderer(colorModel));
 put("PieChartRenderer", new PieChartRenderer(colorModel));
 }});
 chart.render(g);
 verify(g).setColor(Color.white);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.black);
 verify(g).setFont(anyFont());
 verify(g).drawString("TestTitle", width / 2, 10);
 verify(g).drawString("Series1", width - 50, height / 2 - 25);
 verify(g).drawString("Series2", width - 50, height / 2 + 25);
 }

 @Test
 public void testRenderWithTitleAndLegendAndCoordinateSystem() {
 Graphics2D g = createMockGraphics2D();
 Title title = new Title("TestTitle");
 Legend legend = new Legend();
 CoordSystem coordSystem = new CoordSystem(new ChartDataModel(ModelType.LINE));
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setChartRenderer(new HashMap<String, ChartRenderer>() {{
 put("LineChartRenderer", new LineChartRenderer(new RowColorModel(new ChartDataModel(ModelType.LINE))));
 put("PieChartRenderer", new PieChartRenderer(new RowColorModel(new ChartDataModel(ModelType.PIE))));
 }});
 chart.setCoordSystem(coordSystem);
 chart.render(g);
 verify(g).setColor(Color.white);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.black);
 verify(g).setFont(anyFont());
 verify(g).drawString("TestTitle", width / 2, 10);
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getFont()).thenReturn(new Font("Arial", Font.BOLD, 12));
 return g;
 }

}