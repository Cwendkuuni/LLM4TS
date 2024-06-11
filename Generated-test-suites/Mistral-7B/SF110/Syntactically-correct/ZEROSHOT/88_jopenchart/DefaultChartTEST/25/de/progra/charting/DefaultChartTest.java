package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
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
 model.addSeries("Series1", Arrays.asList(1d, 2d, 3d, 4d, 5d));
 model.addSeries("Series2", Arrays.asList(2d, 4d, 6d, 8d, 10d));
 RowColorModel rowColorModel = new RowColorModel(model);
 ChartDataModel modelWithUnits = new ChartDataModel();
 modelWithUnits.addSeries("Series1", Arrays.asList(1d, 2d, 3d, 4d, 5d), "UnitX");
 modelWithUnits.addSeries("Series2", Arrays.asList(2d, 4d, 6d, 8d, 10d), "UnitY");

 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.setRowColorModel(rowColorModel);
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
 public void testChartConstructionWithDataModelAndTitle() {
 ChartDataModel model = chart.getChartDataModel();
 assertNotNull(model);
 Title title = chart.getTitle();
 assertNotNull(title);
 assertEquals("Test Chart", title.getText());
 }

 @Test
 public void testChartConstructionWithDataModelTitleAndCoordinateSystem() {
 ChartDataModel model = chart.getChartDataModel();
 Title title = chart.getTitle();
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(model);
 assertNotNull(title);
 assertNotNull(coordSystem);
 }

 @Test
 public void testChartConstructionWithDataModelTitleCoordinateSystemXAxisUnitAndYAxisUnit() {
 ChartDataModel model = chart.getChartDataModel();
 Title title = chart.getTitle();
 CoordSystem coordSystem = chart.getCoordSystem();
 assertEquals("UnitX", coordSystem.getXAxisUnit());
 assertEquals("UnitY", coordSystem.getYAxisUnit());
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

 verify(g).setColor(Color.WHITE);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.BLACK);

 Title title = chart.getTitle();
 Legend legend = chart.getLegend();
 CoordSystem coordSystem = chart.getCoordSystem();
 Map<String, AbstractChartRenderer> renderer = chart.getChartRenderer();

 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(eq(title.getBounds()), any(Graphics2D.class).eq(g));
 verify(title).render(g);

 verify(g).setColor(Color.BLACK);
 verify(g).fillRect(eq(legend.getBounds()), any(Graphics2D.class).eq(g));
 verify(legend).render(g);

 verify(g).setColor(Color.WHITE);
 if (coordSystem != null) {
 verify(g).fillRect(eq(coordSystem.getBounds()), any(Graphics2D.class).eq(g));
 verify(coordSystem).render(g);
 }

 if (!renderer.isEmpty()) {
 for (AbstractChartRenderer cr : renderer.values()) {
 verify(g).setColor(cr.getLineColor());
 cr.render(g);
 }
 }
}

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.WHITE);
 when(g.setColor(any(Color.class))).thenAnswer(invocation -> invocation.getArgument(0));
 when(g.fillRect(any(Rectangle2D.class), any(Graphics2D.class))).thenAnswer(invocation -> {
 Rectangle2D rect = invocation.getArgument(0);
 g.setColor(Color.BLACK);
 g.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
 return true;
 });
 return g;
}

}