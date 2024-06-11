package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
import de.progra.charting.test.MockChartDataModel;
import de.progra.charting.test.MockChartRenderer;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel dataModel;
 private Title title;
 private Legend legend;
 private CoordSystem coordSystem;
 private AbstractChartRenderer renderer;

 @Before
 public void setUp() {
 dataModel = new MockChartDataModel();
 title = new Title("Test Chart Title");
 legend = new Legend(new RowColorModel(dataModel));
 coordSystem = new CoordSystem(dataModel);
 chart = new DefaultChart(dataModel, title.getText(), DefaultChart.LINEAR_X_LINEAR_Y);
 chart.setChartRenderer(new HashMap<String, AbstractChartRenderer>() {{
 put("testRenderer", new MockChartRenderer(Color.RED, Arrays.asList(new ModelDataSeries("Test Series", Arrays.asList(1.0, 2.0, 3.0))), null, null));
 }} );
 }

 @Test
 public void testDefaultChartConstruction() {
 assertNotNull(chart);
 }

 @Test
 public void testDefaultChartWithTitleConstruction() {
 assertNotNull(chart);
 assertSame(title, chart.getTitle());
 }

 @Test
 public void testDefaultChartWithTitleAndCoordinateSystemConstruction() {
 assertNotNull(chart);
 assertSame(title, chart.getTitle());
 assertSame(coordSystem, chart.getCoordSystem());
 }

 @Test
 public void testDefaultChartWithTitleAndCoordinateSystemAndAxisLabelsConstruction() {
 assertNotNull(chart);
 assertSame(title, chart.getTitle());
 assertSame(coordSystem, chart.getCoordSystem());
 assertSame("X Axis Label", coordSystem.getXAxisUnit());
 assertSame("Y Axis Label", coordSystem.getYAxisUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultChartWithInvalidCoordinateSystemConstruction() {
 chart = new DefaultChart(dataModel, title.getText(), DefaultChart.LINEAR_X_LINEAR_Y, "X Axis Label", "Y Axis Label");
 chart.getCoordSystem(); // Should throw exception
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRenderMethodWithNoTitleOrLegend() {
 final Graphics2D g = MockGraphics2D.create();
 chart.render(g);
 final Rectangle bounds = chart.getBounds();
 assertTrue(bounds.x < 0);
 assertTrue(bounds.y < 0);
 assertTrue(bounds.width > 0);
 assertTrue(bounds.height > 0);
 }

 @Test
 public void testRenderMethodWithTitleButNoLegend() {
 final Graphics2D g = MockGraphics2D.create();
 chart.getTitle().setBounds(new Rectangle(10, 10, 100, 20));
 chart.render(g);
 final Rectangle titleBounds = chart.getTitle().getBounds();
 assertEquals(titleBounds.x, 50);
 assertEquals(titleBounds.y, 5);
 assertEquals(titleBounds.width, 100);
 assertEquals(titleBounds.height, 20);
 }

 @Test
 public void testRenderMethodWithLegendButNoTitle() {
 final Graphics2D g = MockGraphics2D.create();
 chart.getLegend().setBounds(new Rectangle(10, 70, 100, 20));
 chart.render(g);
 final Rectangle legendBounds = chart.getLegend().getBounds();
 assertEquals(legendBounds.x, 50);
 assertEquals(legendBounds.y, 65);
 assertEquals(legendBounds.width, 100);
 assertEquals(legendBounds.height, 20);
 }

 @Test
 public void testRenderMethodWithTitleAndLegend() {
 final Graphics2D g = MockGraphics2D.create();
 chart.getTitle().setBounds(new Rectangle(10, 10, 100, 20));
 chart.getLegend().setBounds(new Rectangle(10, 70, 100, 20));
 chart.render(g);
 final Rectangle titleBounds = chart.getTitle().getBounds();
 assertEquals(titleBounds.x, 50);
 assertEquals(titleBounds.y, 5);
 assertEquals(titleBounds.width, 100);
 assertEquals(titleBounds.height, 20);
 final Rectangle legendBounds = chart.getLegend().getBounds();
 assertEquals(legendBounds.x, 50);
 assertEquals(legendBounds.y, 65);
 assertEquals(legendBounds.width, 100);
 assertEquals(legendBounds.height, 20);
 }

 @Test
 public void testRenderMethodWithTitleAndLegendAndData() {
 final Graphics2D g = MockGraphics2D.create();
 chart.getTitle().setBounds(new Rectangle(10, 10, 100, 20));
 chart.getLegend().setBounds(new Rectangle(10, 70, 100, 20));
 chart.getCoordSystem().setBounds(new Rectangle(10, 35, 180, 140));
 chart.getChartRenderer().get("testRenderer").setBounds(new Rectangle(10, 135, 180, 140));
 chart.render(g);
 final Rectangle titleBounds = chart.getTitle().getBounds();
 assertEquals(titleBounds.x, 50);
 assertEquals(titleBounds.y, 5);
 assertEquals(titleBounds.width, 100);
 assertEquals(titleBounds.height, 20);
 final Rectangle legendBounds = chart.getLegend().getBounds();
 assertEquals(legendBounds.x, 50);
 assertEquals(legendBounds.y, 65);
 assertEquals(legendBounds.width, 100);
 assertEquals(legendBounds.height, 20);
 final Rectangle coordSystemBounds = chart.getCoordSystem().getBounds();
 assertEquals(coordSystemBounds.x, 50);
 assertEquals(coordSystemBounds.y, 35);
 assertEquals(coordSystemBounds.width, 180);
 assertEquals(coordSystemBounds.height, 100);
 final Rectangle rendererBounds = chart.getChartRenderer().get("testRenderer").getBounds();
 assertEquals(rendererBounds.x, 50);
 assertEquals(rendererBounds.y, 135);
 assertEquals(rendererBounds.width, 180);
 assertEquals(rendererBounds.height, 100);
 }
}