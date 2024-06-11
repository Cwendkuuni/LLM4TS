package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
 model.setTitle("Test Chart");
 ChartData chartData = new ChartData();
 chartData.setModel(model);
 chartData.setData(new double[][]{
 new double[]{1.0, 2.0, 3.0},
 new double[]{4.0, 5.0, 6.0},
 new double[]{7.0, 8.0, 9.0}
 });
 AbstractRenderer renderer1 = new LineRenderer();
 renderer1.setColor(Color.RED);
 AbstractRenderer renderer2 = new LineRenderer();
 renderer2.setColor(Color.BLUE);
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "x-axis", "y-axis");
 chart.setChartRenderer(new HashMap<Integer, AbstractRenderer>() {{
 put(0, renderer1);
 put(1, renderer2);
 }});
 }

 @Test
 public void testDefaultChartConstructor() {
 DefaultChart defaultChart = new DefaultChart();
 assertNotNull(defaultChart);
 }

 @Test
 public void testDefaultChartConstructorWithModelAndTitle() {
 DefaultChart defaultChart = new DefaultChart(new ChartDataModel(), "Test Chart");
 assertNotNull(defaultChart);
 }

 @Test
 public void testDefaultChartConstructorWithModelTitleAndCoordinateSystem() {
 DefaultChart defaultChart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(defaultChart);
 }

 @Test
 public void testDefaultChartConstructorWithModelTitleCoordinateSystemXAxisUnitYAxisUnit() {
 DefaultChart defaultChart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "x-axis", "y-axis");
 assertNotNull(defaultChart);
 }

 @Test
 public void testGetPreferredSize() {
 ChartDataModel model = new ChartDataModel();
 model.setTitle("Test Chart");
 DefaultChart defaultChart = new DefaultChart(model, "Test Chart");
 assertNull(defaultChart.getPreferredSize());
 }

 @Test (expected = NullPointerException.class)
 public void testRenderNullGraphics2D() {
 chart.render(null);
 }

 @Test
 public void testRenderEmptyChart() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 assertTrue(g.getColor() == Color.white);
 }

 @Test
 public void testRenderChartWithTitleAndLegend() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 Title title = chart.getTitle();
 Legend legend = chart.getLegend();
 Rectangle titleBounds = title.getBounds();
 Rectangle legendBounds = legend.getBounds();
 assertEquals(titleBounds.getX(), width / 2 - (int)(title.getPreferredSize().getWidth() / 2.0));
 assertEquals(titleBounds.getY(), 0);
 assertEquals(titleBounds.getWidth(), (int)title.getPreferredSize().getWidth());
 assertEquals(titleBounds.getHeight(), (int)title.getPreferredSize().getHeight());
 assertEquals(legendBounds.getX(), width - legendBounds.getWidth());
 assertEquals(legendBounds.getY(), height / 2 - legendBounds.getHeight() / 2 + title.getPreferredSize().getHeight());
 assertEquals(legendBounds.getWidth(), legend.getPreferredSize().getWidth());
 assertEquals(legendBounds.getHeight(), legend.getPreferredSize().getHeight());
 }

 @Test
 public void testRenderChartWithTitleLegendAndData() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 Title title = chart.getTitle();
 Legend legend = chart.getLegend();
 CoordSystem coordSystem = chart.getCoordSystem();
 Collection<AbstractRenderer> renderers = chart.getChartRenderer().values();

 assertEquals(g.getColor(), Color.white);
 g.setColor(Color.black);

 title.render(g);
 assertNotNull(title.getPreferredSize());
 title.setBounds(new Rectangle(width / 2 - (int)(title.getPreferredSize().getWidth() / 2.0), 0, (int)title.getPreferredSize().getWidth(), (int)title.getPreferredSize().getHeight()));

 legend.render(g);
 assertNotNull(legend.getPreferredSize());
 legend.setBounds(new Rectangle(width - legend.getPreferredSize().getWidth(), height / 2 - legend.getPreferredSize().getHeight() / 2.0 + title.getPreferredSize().getHeight(), (int)legend.getPreferredSize().getWidth(), (int)legend.getPreferredSize().getHeight()));

 coordSystem.render(g);
 assertNotNull(coordSystem);
 coordSystem.setBounds(new Rectangle(0, title.getPreferredSize().getHeight(), width - legend.getPreferredSize().getWidth(), height - title.getPreferredSize().getHeight()));

 for (AbstractRenderer renderer : renderers) {
 renderer.render(g);
 }
 }

 @Test
 public void testRenderChartWithTitleLegendDataAndCoordinateSystem() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 Title title = chart.getTitle();
 Legend legend = chart.getLegend();
 CoordSystem coordSystem = chart.getCoordSystem();
 Collection<AbstractRenderer> renderers = chart.getChartRenderer().values();

 assertEquals(g.getColor(), Color.white);
 g.setColor(Color.black);

 title.render(g);
 assertNotNull(title.getPreferredSize());
 title.setBounds(new Rectangle(width / 2 - (int)(title.getPreferredSize().getWidth() / 2.0), 0, (int)title.getPreferredSize().getWidth(), (int)title.getPreferredSize().getHeight()));

 legend.render(g);
 assertNotNull(legend.getPreferredSize());
 legend.setBounds(new Rectangle(width - legend.getPreferredSize().getWidth(), height / 2 - legend.getPreferredSize().getHeight() / 2.0 + title.getPreferredSize().getHeight(), (int)legend.getPreferredSize().getWidth(), (int)legend.getPreferredSize().getHeight()));

 coordSystem.render(g);
 assertNotNull(coordSystem);
 coordSystem.setBounds(new Rectangle(0, title.getPreferredSize().getHeight(), width - legend.getPreferredSize().getWidth(), height - title.getPreferredSize().getHeight()));

 for (AbstractRenderer renderer : renderers) {
 renderer.render(g);
 }
 }

 @Test
 public void testSetChartDataModelWithNullChartDataModel() {
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getChartDataModel());
 defaultChart.setChartDataModel(null);
 assertNull(defaultChart.getChartDataModel());
 }

 @Test
 public void testSetChartDataModel() {
 DefaultChart defaultChart = new DefaultChart();
 ChartDataModel model = new ChartDataModel();
 defaultChart.setChartDataModel(model);
 assertEquals(defaultChart.getChartDataModel(), model);
 }

 @Test
 public void testSetTitleWithNullTitle() {
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getTitle());
 defaultChart.setTitle(null);
 assertNull(defaultChart.getTitle());
 }

 @Test
 public void testSetTitle() {
 DefaultChart defaultChart = new DefaultChart();
 Title title = new Title("Test Title");
 defaultChart.setTitle(title);
 assertEquals(defaultChart.getTitle(), title);
 }

 @Test
 public void testSetCoordinateSystemWithNullCoordinateSystem() {
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getCoordSystem());
 defaultChart.setCoordinateSystem(null);
 assertNull(defaultChart.getCoordSystem());
 }

 @Test
 public void testSetCoordinateSystem() {
 DefaultChart defaultChart = new DefaultChart();
 CoordSystem coordSystem = new CoordSystem(new ChartDataModel());
 defaultChart.setCoordinateSystem(coordSystem);
 assertEquals(defaultChart.getCoordSystem(), coordSystem);
 }

 @Test
 public void testSetRowColorModelWithNullRowColorModel() {
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getRowColorModel());
 defaultChart.setRowColorModel(null);
 assertNull(defaultChart.getRowColorModel());
 }

 @Test
 public void testSetRowColorModel() {
 DefaultChart defaultChart = new DefaultChart();
 RowColorModel rowColorModel = new RowColorModel(new ChartDataModel());
 defaultChart.setRowColorModel(rowColorModel);
 assertEquals(defaultChart.getRowColorModel(), rowColorModel);
 }

 @Test
 public void testSetLegendWithNullLegend() {
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getLegend());
 defaultChart.setLegend(null);
 assertNull(defaultChart.getLegend());
 }

 @Test
 public void testSetLegend() {
 DefaultChart defaultChart = new DefaultChart();
 Legend legend = new Legend(new RowColorModel(new ChartDataModel()));
 defaultChart.setLegend(legend);
 assertEquals(defaultChart.getLegend(), legend);
 }

 @Test
 public void testGetChartRendererSize() {
 ChartDataModel model = new ChartDataModel();
 ChartData chartData = new ChartData();
 chartData.setModel(model);
 AbstractRenderer renderer1 = new LineRenderer();
 renderer1.setColor(Color.RED);
 AbstractRenderer renderer2 = new LineRenderer();
 renderer2.setColor(Color.BLUE);
 DefaultChart defaultChart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "x-axis", "y-axis");
 defaultChart.setChartRenderer(new HashMap<Integer, AbstractRenderer>() {{
 put(0, renderer1);
 put(1, renderer2);
 }});
 assertEquals(defaultChart.getChartRenderer().size(), 2);
 }

 @Test
 public void testGetChartRendererValueByKey() {
 ChartDataModel model = new ChartDataModel();
 ChartData chartData = new ChartData();
 chartData.setModel(model);
 AbstractRenderer renderer1 = new LineRenderer();
 renderer1.setColor(Color.RED);
 AbstractRenderer renderer2 = new LineRenderer();
 renderer2.setColor(Color.BLUE);
 DefaultChart defaultChart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "x-axis", "y-axis");
 defaultChart.setChartRenderer(new HashMap<Integer, AbstractRenderer>() {{
 put(0, renderer1);
 put(1, renderer2);
 }});
 assertEquals(defaultChart.getChartRenderer().get(0), renderer1);
 assertEquals(defaultChart.getChartRenderer().get(1), renderer2);
 }

 @Test
 public void testGetChartRendererEmptyMap() {
 DefaultChart defaultChart = new DefaultChart();
 assertEquals(defaultChart.getChartRenderer().isEmpty(), true);
 }

 @Test
 public void testGetChartRendererValues() {
 ChartDataModel model = new ChartDataModel();
 ChartData chartData = new ChartData();
 chartData.setModel(model);
 AbstractRenderer renderer1 = new LineRenderer();
 renderer1.setColor(Color.RED);
 AbstractRenderer renderer2 = new LineRenderer();
 renderer2.setColor(Color.BLUE);
 DefaultChart defaultChart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "x-axis", "y-axis");
 defaultChart.setChartRenderer(new HashMap<Integer, AbstractRenderer>() {{
 put(0, renderer1);
 put(1, renderer2);
 }});
 List<AbstractRenderer> renderers = new ArrayList<>(defaultChart.getChartRenderer().values());
 assertEquals(renderers.size(), 2);
 assertEquals(renderers.get(0), renderer1);
 assertEquals(renderers.get(1), renderer2);
 }

 @Test
 public void testGetChartRendererValuesWithEmptyMap() {
 DefaultChart defaultChart = new DefaultChart();
 List<AbstractRenderer> renderers = new ArrayList<>(defaultChart.getChartRenderer().values());
 assertEquals(renderers.size(), 0);
 }

 @Test
 public void testGetChartRendererKeys() {
 ChartDataModel model = new ChartDataModel();
 ChartData chartData = new ChartData();
 chartData.setModel(model);
 AbstractRenderer renderer1 = new LineRenderer();
 renderer1.setColor(Color.RED);
 AbstractRenderer renderer2 = new LineRenderer();
 renderer2.setColor(Color.BLUE);
 DefaultChart defaultChart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "x-axis", "y-axis");
 defaultChart.setChartRenderer(new HashMap<Integer, AbstractRenderer>() {{
 put(0, renderer1);
 put(1, renderer2);
 }});
 List<Integer> keys = new ArrayList<>(defaultChart.getChartRenderer().keySet());
 assertEquals(keys.size(), 2);
 assertEquals(keys.get(0), 0);
 assertEquals(keys.get(1), 1);
 }

 @Test
 public void testGetChartRendererKeysWithEmptyMap() {
 DefaultChart defaultChart = new DefaultChart();
 List<Integer> keys = new ArrayList<>(defaultChart.getChartRenderer().keySet());
 assertEquals(keys.size(), 0);
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = mock(Graphics2D.class);
 return g;
 }
}