package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelData;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {

 private JUnitRuleMockery context;
 private DefaultChart chart;
 private ChartDataModel model;
 private Title title;
 private Legend legend;
 private CoordSystem coordSystem;
 private Map<String, AbstractChartRenderer> chartRenderers;

 @Before
 public void setUp() {
 this.context = new JUnitRuleMockery();
 this.model = this.context.mock(ChartDataModel.class);
 this.title = new Title("Test Title");
 this.legend = new Legend();
 this.coordSystem = new CoordSystem(this.model);
 this.chartRenderers = new HashMap<>();
 this.chart = new DefaultChart(this.model, this.title.getText(), DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
 }

 @Test
 public void testDefaultChartConstructor() {
 assertNotNull(this.chart);
 assertEquals(this.model, this.chart.getChartDataModel());
 assertNotNull(this.chart.getRowColorModel());
 assertNotNull(this.chart.getLegend());
 assertNotNull(this.chart.getTitle());
 assertNull(this.chart.getCoordSystem());
 }

 @Test
 public void testDefaultChartConstructorWithTitle() {
 assertNotNull(this.chart);
 assertEquals(this.model, this.chart.getChartDataModel());
 assertNotNull(this.chart.getRowColorModel());
 assertNotNull(this.chart.getLegend());
 assertNotNull(this.chart.getTitle());
 assertNull(this.chart.getCoordSystem());
 }

 @Test
 public void testDefaultChartConstructorWithTitleAndCoordinateSystem() {
 assertNotNull(this.chart);
 assertEquals(this.model, this.chart.getChartDataModel());
 assertNotNull(this.chart.getRowColorModel());
 assertNotNull(this.chart.getLegend());
 assertNotNull(this.chart.getTitle());
 assertNotNull(this.chart.getCoordSystem());
 }

 @Test
 public void testGetPreferredSizeReturnsNull() {
 assertNull(this.chart.getPreferredSize());
 }

 @Test
 public void testRenderMethod() {
 final Graphics2D g = this.context.mock(Graphics2D.class);
 this.chart.setChartRenderer(this.chartRenderers);
 this.context.checking(new Expectations() {{
 final Rectangle2D titleBounds = new Rectangle2D.Double(6.0, 0.0, this.title.getPreferredSize().getWidth(), this.title.getPreferredSize().getHeight());
 final Rectangle2D legendBounds = new Rectangle2D.Double(this.chart.getBounds().getWidth() - this.legend.getPreferredSize().getWidth(), this.chart.getBounds().getHeight() / 2.0 - this.legend.getPreferredSize().getHeight() / 2.0, this.legend.getPreferredSize().getWidth(), this.legend.getPreferredSize().getHeight());
 final Rectangle2D coordSystemBounds = new Rectangle2D.Double(0.0, this.title.getPreferredSize().getHeight(), this.chart.getBounds().getWidth() - this.legend.getPreferredSize().getWidth(), this.chart.getBounds().getHeight() - this.title.getPreferredSize().getHeight() - 5.0);
 final Rectangle2D rendererBounds = new Rectangle2D.Double(0.0, this.title.getPreferredSize().getHeight(), this.chart.getBounds().getWidth() - this.legend.getPreferredSize().getWidth(), this.chart.getBounds().getHeight() - this.title.getPreferredSize().getHeight() - 5.0 - this.coordSystemBounds.getHeight());

 this.chart.render(g);

 oneOf(g).setColor(Color.WHITE);
 g.checking(new Expectations() {{
 oneOf(g).fillRect(with(any(Rectangle2D.class)));
 }});

 oneOf(g).setColor(Color.BLACK);
 g.checking(new Expectations() {{
 oneOf(g).fillRect(with(any(Rectangle2D.class)));
 oneOf(g).setColor(Color.BLACK);
 oneOf(this.title).render(g);
 oneOf(this.legend).render(g);
 oneOf(this.coordSystem).render(g);
 oneOf(this.chartRenderers.values().iterator().next()).render(g);
 }});
 }});

 this.context.replay();
 }

 @Test(expected = NullPointerException.class)
 public void testRenderMethodWithNullGraphics2D() {
 this.chart.render(null);
 }
}