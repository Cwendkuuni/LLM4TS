package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 model.setTitle("Test Chart");
 model.setXAxisLabel("X-Axis");
 model.setYAxisLabel("Y-Axis");
 model.setData(Arrays.asList(new DataPoint("A", 1d), new DataPoint("B", 2d), new DataPoint("C", 3d)));

 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testDefaultChartConstructor() {
 DefaultChart chart = new DefaultChart();
 assertNull(chart.getChartDataModel());
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertEmpty(chart.getChartRenderer().values());
 }

 @Test
 public void testChartWithTitleConstructor() {
 assertEquals("Test Chart", chart.getTitle().getText());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemConstructor() {
 DefaultChart chart = new DefaultChart(chart.getChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemAndAxisLabelsConstructor() {
 DefaultChart chart = new DefaultChart(chart.getChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");
 assertEquals("X-Axis", chart.getCoordSystem().getXAxisUnit());
 assertEquals("Y-Axis", chart.getCoordSystem().getYAxisUnit());
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRender() {
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);

 verify(g, times(1)).setColor(Color.WHITE);
 verify(g, times(1)).fillRect(0, 0, anyInt(), anyInt());
 verify(g, times(1)).setColor(Color.BLACK);

 Title title = chart.getTitle();
 Legend legend = chart.getLegend();
 CoordSystem coordSystem = chart.getCoordSystem();
 List<AbstractChartRenderer> renderer = new ArrayList<>(chart.getChartRenderer().values());

 // Title rendering
 verify(g, times(1)).setColor(Color.BLACK);
 Title titleMock = mock(Title.class);
 titleMock.setBounds(any(Rectangle2D.class));
 titleMock.render(g);
 title = titleMock;
 verify(g, times(1)).setFont(any(Font.class));

 // Legend rendering
 verify(g, times(1)).setColor(Color.BLACK);
 Legend legendMock = mock(Legend.class);
 legendMock.setBounds(any(Rectangle2D.class));
 legendMock.render(g);
 legend = legendMock;
 verify(g, times(1)).setFont(any(Font.class));

 // Coordinate system rendering
 verify(g, times(1)).setColor(Color.BLACK);
 CoordSystem coordSystemMock = mock(CoordSystem.class);
 coordSystemMock.setBounds(any(Rectangle2D.class));
 coordSystemMock.render(g);
 coordSystem = coordSystemMock;

 // Renderers rendering
 for (AbstractChartRenderer rendererMock : renderer) {
 rendererMock.setBounds(any(Rectangle2D.class));
 rendererMock.render(g);
 }

 // Coordinate system rendering again
 verify(g, times(1)).setColor(Color.BLACK);
 coordSystemMock.render(g);
 }

}