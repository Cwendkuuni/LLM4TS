package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;

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
 model.addSeries(new ChartSeries("Series1", new Double[]{}));
 model.addSeries(new ChartSeries("Series2", new Double[]{}));
 chart = new DefaultChart(model, "DefaultChart", DefaultChart.LINEAR_X_LINEAR_Y);
 }

 @Test
 public void testDefaultChartConstruction() {
 // Test default constructor
 assertNull(chart.getChartDataModel());
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());

 // Test constructor with title
 ChartDataModel model = new ChartLineChartDataModel(new ChartSeries[]{}, new String[]{"X-Axis", "Y-Axis"}, new String[]{"X-Axis unit", "Y-Axis unit"});
 ChartTitle title = new Title("Default Chart Title");
 chart = new DefaultChart(model, title.getText());
 assertEquals(model, chart.getChartDataModel());
 assertEquals(title, chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());

 // Test constructor with title and coordinate system
 ChartDataModel modelWithData = new ChartLineChartDataModel(new ChartSeries[]{}, new String[]{"X-Axis", "Y-Axis"}, new String[]{"X-Axis unit", "Y-Axis unit"}, new double[][]{1.0, 2.0}, new double[]{1.0, 2.0});
 ChartTitle titleWithTitle = new Title("Default Chart Title");
 CoordSystem coordSystem = new CoordSystem();
 chart = new DefaultChart(modelWithData, titleWithTitle.getText(), DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");
 assertEquals(modelWithData, chart.getChartDataModel());
 assertEquals(titleWithTitle, chart.getTitle());
 assertSame(coordSystem, chart.getCoordSystem());
 assertNull(chart.getLegend());
 assertNotNull(chart.getChartRenderer());
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRender() {
 // Test render method with no title or legend
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verifyNoMoreInteractions(g);

 // Test render method with title and legend
 ChartDataModel model = new ChartBarChartDataModel(new ChartSeries[]{}, new String[]{"X-Axis", "Y-Axis"}, new String[]{"X-Axis unit", "Y-Axis unit"}, new int[][]{10, 20}, new int[][]{10, 20});
 ChartTitle title = new Title("Title");
 Legend legend = new Legend();
 chart = new DefaultChart(model, title.getText(), DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");
 chart.setTitle(title);
 chart.setLegend(legend);
 g = createMockGraphics2D();
 chart.render(g);

 // Verify title rendering
 ChartTitle expectedTitle = Mockito.capture(chart.getTitle());
 Mockito.verify(expectedTitle).render(g);

 // Verify legend rendering
 Legend expectedLegend = Mockito.capture(chart.getLegend());
 Mockito.verify(expectedLegend).render(g);

 // Verify coordinate system rendering
 CoordSystem expectedCoordSystem = Mockito.capture(chart.getCoordSystem());
 Mockito.verify(expectedCoordSystem).render(g);

 // Verify chart renderer rendering
 AbstractChartRenderer chartRenderer = Mockito.capture(chart.getChartRenderer().values().iterator().next());
 Mockito.verify(chartRenderer).render(g);
 }

 private Graphics2D createMockGraphics2D() {
 return Mockito.mock(Graphics2D.class);
}

}