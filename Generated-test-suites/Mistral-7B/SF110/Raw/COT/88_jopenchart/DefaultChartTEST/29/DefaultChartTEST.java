package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private DefaultChart chart;

 @Test
 public void testDefaultChartConstructor() {
 chart = new DefaultChart(new ChartDataModel(), "Default Chart Title");
 assertNotNull(chart.getTitle());
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testDefaultChartWithCoordinateSystemConstructor() {
 chart = new DefaultChart(new ChartDataModel(), "Default Chart Title", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testDefaultChartWithInvalidCoordinateSystemConstructor() {
 chart = new DefaultChart(new ChartDataModel(), "Default Chart Title", 2);
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testSetTitleMethod() {
 chart = new DefaultChart(new ChartDataModel(), "Default Chart Title");
 chart.setTitle(new Title("New Chart Title"));
 assertNotNull(chart.getTitle());
 assertNotSame(chart.getTitle(), chart.getTitle());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitleArgument() {
 chart = new DefaultChart(new ChartDataModel(), null);
 }

 @Test
 public void testGetPreferredSizeMethod() {
 chart = new DefaultChart(new ChartDataModel(), "Default Chart Title");
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRenderMethodWithTitleAndLegend() {
 chart = new DefaultChart(new ChartDataModel(), "Default Chart Title");
 Title title = new Title("Test Title");
 Legend legend = new Legend();
 chart.setTitle(title);
 chart.setLegend(legend);
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verify(g).setColor(argThat(isA(Color.class)));
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g).setColor(Color.black);
 title.render(g);
 legend.render(g);
 }

 @Test
 public void testRenderMethodWithTitleAndLegendAndCoordinateSystem() {
 chart = new DefaultChart(new ChartDataModel(), "Default Chart Title", DefaultChart.LINEAR_X_LINEAR_Y);
 Title title = new Title("Test Title");
 Legend legend = new Legend();
 CoordSystem coordSystem = mock(CoordSystem.class);
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(coordSystem);
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verify(g).setColor(Color.white);
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g).setColor(Color.black);
 title.render(g);
 legend.render(g);
 coordSystem.render(g);
 }

 @Test
 public void testRenderMethodWithEmptyChartRendererCollection() {
 chart = new DefaultChart(new ChartDataModel(), "Default Chart Title");
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verifyNoMoreInteractions(g);
 }

 @Test
 public void testRenderMethodWithChartRendererCollection() {
 chart = new DefaultChart(new ChartDataModel(), "Default Chart Title");
 AbstractChartRenderer renderer1 = mock(AbstractChartRenderer.class);
 AbstractChartRenderer renderer2 = mock(AbstractChartRenderer.class);
 chart.putChartRenderer("Renderer 1", renderer1);
 chart.putChartRenderer("Renderer 2", renderer2);
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verify(renderer1).setBounds(any(Rectangle.class));
 verify(renderer1).render(g);
 verify(renderer2).setBounds(any(Rectangle.class));
 verify(renderer2).render(g);
 }

}