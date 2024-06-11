package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
 model.setData(Arrays.asList(new DataPoint[]{
 new DataPoint(1, 1), new DataPoint(2, 2), new DataPoint(3, 3), new DataPoint(4, 4)}));
 model.setXAxisLabels(Arrays.asList("Label1", "Label2", "Label3", "Label4"));
 model.setYAxisLabels(Arrays.asList("Label1", "Label2", "Label3", "Label4"));

 AbstractChartRenderer renderer1 = new DummyChartRenderer();
 renderer1.setColor(Color.RED);
 model.setRenderer("Renderer1", renderer1);

 AbstractChartRenderer renderer2 = new DummyChartRenderer();
 renderer2.setColor(Color.BLUE);
 model.setRenderer("Renderer2", renderer2);

 RowColorModel rowColorModel = new RowColorModel(model);

 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.setRowColorModel(rowColorModel);
 }

 @Test
 public void testDefaultChartConstruction() {
 ChartDataModel model = chart.getChartDataModel();
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNotNull(chart.getCoordSystem());
 assertNull(chart.getLegend());
 assertFalse(chart.getChartRenderer().isEmpty());
 assertNotNull(chart.getRenderer("Renderer1"));
 assertNotNull(chart.getRenderer("Renderer2"));
 }

 @Test
 public void testChartConstructionWithTitle() {
 ChartDataModel model = chart.getChartDataModel();
 Title title = (Title) chart.getTitle();
 assertEquals("Test Chart", title.getText());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystem() {
 ChartDataModel model = chart.getChartDataModel();
 Title title = (Title) chart.getTitle();
 CoordSystem coordSystem = (CoordSystem) chart.getCoordSystem();
 assertEquals("Test Chart", title.getText());
 assertEquals("", coordSystem.getXAxisUnit());
 assertEquals("", coordSystem.getYAxisUnit());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystemAndLabels() {
 ChartDataModel model = chart.getChartDataModel();
 Title title = (Title) chart.getTitle();
 CoordSystem coordSystem = (CoordSystem) chart.getCoordSystem();
 List<String> xAxisLabels = chart.getChartDataModel().getXAxisLabels();
 List<String> yAxisLabels = chart.getChartDataModel().getYAxisLabels();
 assertEquals("Test Chart", title.getText());
 assertEquals("", coordSystem.getXAxisUnit());
 assertEquals("", coordSystem.getYAxisUnit());
 assertEquals(Arrays.asList("Label1", "Label2", "Label3", "Label4"), xAxisLabels);
 assertEquals(Arrays.asList("Label1", "Label2", "Label3", "Label4"), yAxisLabels);
 }

 @Test
 public void testRenderMethod() {
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verify(g).setColor(Color.white);
 verify(g).fillRect(0, 0, (int) chart.getBounds().getWidth(), (int) chart.getBounds().getHeight());
 verify(g).setColor(Color.black);
 Title title = (Title) chart.getTitle();
 Legend legend = chart.getLegend();
 CoordSystem coordSystem = (CoordSystem) chart.getCoordSystem();
 List<AbstractChartRenderer> rendererList = new ArrayList<>(chart.getChartRenderer().values());
 for (AbstractChartRenderer renderer : rendererList) {
 renderer.setBounds(new Rectangle(0, 0, (int) chart.getBounds().getWidth() - legend.getBounds().getWidth(), (int) chart.getBounds().getHeight() - title.getBounds().getHeight() - 5));
 verify(g).setColor(renderer.getColor());
 renderer.render(g);
 }
 if (legend != null) {
 legend.render(g);
 }
 if (coordSystem != null) {
 coordSystem.render(g);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCoordinateSystemValue() {
 chart = new DefaultChart(new ChartDataModel(), "Test Chart", 3);
 assertNull(chart.getCoordSystem());
 }

 @Test(expected = IllegalStateException.class)
 public void testRenderMethodWithNullGraphics2D() {
 chart.render(null);
 }

 @Test
 public void testGetPreferredSizeMethod() {
 Dimension preferredSize = chart.getPreferredSize();
 assertNull(preferredSize);
 }
}

 class DummyChartRenderer implements AbstractChartRenderer {

 @Override
 public Color getColor() {
 return Color.BLACK;
 }

 @Override
 public void render(Graphics2D g) {
 // empty implementation
 }

 @Override
 public void setBounds(Rectangle bounds) {
 // empty implementation
 }
}