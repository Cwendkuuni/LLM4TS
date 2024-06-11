package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 // Test for DefaultChart constructor without arguments
 @Test
 public void testDefaultChartConstruction() {
 DefaultChart chart = new DefaultChart();
 assertNull(chart.getChartDataModel());
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 // Test for DefaultChart constructor with ChartDataModel and title
 @Test
 public void testDefaultChartConstructionWithDataModelAndTitle() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 assertSame(model, chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 // Test for DefaultChart constructor with ChartDataModel, title, and LINEAR_X_LINEAR_Y coordinate system
 @Test
 public void testDefaultChartConstructionWithDataModelAndTitleAndCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertSame(model, chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 assertFalse(chart.getCoordSystem().isVisible());
 }

 // Test for DefaultChart constructor with ChartDataModel, title, LINEAR_X_LINEAR_Y coordinate system, and custom X and Y axis labels
 @Test
 public void testDefaultChartConstructionWithDataModelAndTitleAndCoordinateSystemAndAxisLabels() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-axis label", "Y-axis label");
 assertSame(model, chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 assertFalse(chart.getCoordSystem().isVisible());
 assertEqual("X-axis label", chart.getCoordSystem().getXAxisUnit());
 assertEqual("Y-axis label", chart.getCoordSystem().getYAxisUnit());
 }

 // Test for getPreferredSize method when no title, legend, or coordinate system is set
 @Test
 public void testGetPreferredSizeWithoutTitleLegendOrCoordinateSystem() {
 DefaultChart chart = new DefaultChart(new ChartDataModel());
 assertEquals(0, chart.getPreferredSize().height);
 assertEquals(0, chart.getPreferredSize().width);
 }

 // Test for getPreferredSize method when title is set
 @Test
 public void testGetPreferredSizeWithTitle() {
 ChartDataModel model = new ChartDataModel();
 Title title = new Title("Test Chart Title");
 DefaultChart chart = new DefaultChart(model, title.getText());
 Title expectedTitle = new Title("Test Chart Title");
 expectedTitle.setPreferredSize(new Dimension(100, 50));
 assertSame(expectedTitle, chart.getTitle());
 expectedTitle.setBounds(new Rectangle(10, 10, 100, 50));
 chart.setTitle(expectedTitle);
 assertEquals(120, chart.getPreferredSize().height);
 assertEquals(100, chart.getPreferredSize().width);
 }

 // Test for render method with no title, legend, or coordinate system set
 @Test
 public void testRenderWithoutTitleLegendOrCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model);
 // Create mocks for Title, Legend, and CoordSystem
 Title title = mock(Title.class);
 Legend legend = mock(Legend.class);
 CoordSystem coordSystem = mock(CoordSystem.class);
 // Set up expectations for title, legend, and coordinate system bounds and render methods
 when(title.getPreferredSize()).thenReturn(new Dimension(100, 50));
 when(legend.getPreferredSize()).thenReturn(new Dimension(50, 50));
 when(coordSystem.getBounds()).thenReturn(new Rectangle(0, 0, 500, 500));
 when(title.render(any(Graphics2D.class))).thenReturn(true);
 when(legend.render(any(Graphics2D.class))).thenReturn(true);
 when(coordSystem.render(any(Graphics2D.class))).thenReturn(true);
 // Set up expectations for chart renderer bounds and render method
 List<AbstractChartRenderer> renderers = Arrays.asList(mock(AbstractChartRenderer.class), mock(AbstractChartRenderer.class));
 when(chart.getChartRenderer()).thenReturn(Collections.unmodifiableMap(new HashMap<String, AbstractChartRenderer>(){ { put("Renderer1", renderers.get(0)); put("Renderer2", renderers.get(1)); } }));
 when(renderers.get(0).getBounds()).thenReturn(new Rectangle(0, 0, 500, 500));
 when(renderers.get(1).getBounds()).thenReturn(new Rectangle(0, 0, 500, 500));
 when(renderers.get(0).render(any(Graphics2D.class))).thenReturn(true);
 when(renderers.get(1).render(any(Graphics2D.class))).thenReturn(true);
 // Test render method
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(eq(0), eq(0), eq(500), eq(500));
 verify(g).setColor(eq(Color.white));
 verify(g).setColor(eq(Color.black));
 verify(title).render(g);
 verify(legend).render(g);
 verify(coordSystem).render(g);
 verify(renderers.get(0), times(1)).render(g);
 verify(renderers.get(1), times(1)).render(g);
 }

 // Test for render method with title, legend, and coordinate system set
 @Test
 public void testRenderWithTitleLegendAndCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 Title title = new Title("Test Chart Title");
 DefaultChart chart = new DefaultChart(model, title.getText());
 Legend legend = new Legend();
 ChartCoordinateSystem coordSystem = new ChartCoordinateSystem();
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(coordSystem);
 // Create mocks for Title, Legend, and CoordSystem
 Title expectedTitle = new Title("Test Chart Title");
 expectedTitle.setPreferredSize(new Dimension(100, 50));
 Legend expectedLegend = mock(Legend.class);
 expectedLegend.setPreferredSize(new Dimension(50, 50));
 CoordSystem expectedCoordSystem = mock(CoordSystem.class);
 // Set up expectations for title, legend, and coordinate system bounds and render methods
 when(expectedTitle.getPreferredSize()).thenReturn(new Dimension(100, 50));
 when(expectedLegend.getPreferredSize()).thenReturn(new Dimension(50, 50));
 when(expectedCoordSystem.getBounds()).thenReturn(new Rectangle(0, 0, 500, 500));
 when(expectedTitle.render(any(Graphics2D.class))).thenReturn(true);
 when(expectedLegend.render(any(Graphics2D.class))).thenReturn(true);
 when(expectedCoordSystem.render(any(Graphics2D.class))).thenReturn(true);
 // Set up expectations for chart renderer bounds and render method
 List<AbstractChartRenderer> renderers = Arrays.asList(mock(AbstractChartRenderer.class), mock(AbstractChartRenderer.class));
 when(chart.getChartRenderer()).thenReturn(Collections.unmodifiableMap(new HashMap<String, AbstractChartRenderer>(){ { put("Renderer1", renderers.get(0)); put("Renderer2", renderers.get(1)); } }));
 when(renderers.get(0).getBounds()).thenReturn(new Rectangle(0, 0, 500, 500));
 when(renderers.get(1).getBounds()).thenReturn(new Rectangle(0, 0, 500, 500));
 when(renderers.get(0).render(any(Graphics2D.class))).thenReturn(true);
 when(renderers.get(1).render(any(Graphics2D.class))).thenReturn(true);
 // Test render method
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(eq(0), eq(0), eq(500), eq(500));
 verify(g).setColor(eq(Color.white));
 verify(g).setColor(eq(Color.black));
 verify(expectedTitle).render(g);
 verify(expectedLegend).render(g);
 verify(expectedCoordSystem).render(g);
 verify(renderers.get(0), times(1)).render(g);
 verify(renderers.get(1), times(1)).render(g);
 }
}