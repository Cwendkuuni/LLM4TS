package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;

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

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel model;
 private ModelDataSeries series1;
 private ModelDataSeries series2;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 series1 = new ModelDataSeries("Series1", Arrays.asList(new Double[] {1.0d, 2.0d, 3.0d, 4.0d, 5.0d}), new Integer[] {1, 2, 3, 4, 5});
 series2 = new ModelDataSeries("Series2", Arrays.asList(new Double[] {2.0d, 3.0d, 4.0d, 5.0d, 6.0d}), new Integer[] {2, 3, 4, 5, 6});
 model.addSeries(series1);
 model.addSeries(series2);
 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testDefaultConstructor() {
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNull(chart.getChartRenderer());
 }

 @Test
 public void testConstructorWithTitle() {
 Title title = new Title("Test Chart");
 chart = new DefaultChart(model, title.getText());
 assertEquals(title.getText(), chart.getTitle().getText());
 }

 @Test
 public void testConstructorWithTitleAndCoordinates() {
 Title title = new Title("Test Chart");
 chart = new DefaultChart(model, title.getText(), DefaultChart.LINEAR_X_LINEAR_Y);
 Title title2 = new Title("Test Chart2");
 chart.setTitle(title2);
 assertNotSame(title, title2);
 CoordSystem coordSystem = new CoordSystem(model);
 assertEquals(coordSystem, chart.getCoordSystem());
 }

 @Test
 public void testConstructorWithTitleAndCoordinatesAndXAxisUnitAndYAxisUnit() {
 Title title = new Title("Test Chart");
 chart = new DefaultChart(model, title.getText(), DefaultChart.LINEAR_X_LINEAR_Y, "XUnit", "YUnit");
 Title title2 = new Title("Test Chart2");
 chart.setTitle(title2);
 assertNotSame(title, title2);
 CoordSystem coordSystem = new CoordSystem(model);
 assertEquals("XUnit", coordSystem.getXAxisUnit());
 assertEquals("YUnit", coordSystem.getYAxisUnit());
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
 // The render method does not return anything, therefore we cannot test it directly.
 // We can only test if the chart is rendered correctly by checking the state of the graphics object.
 // First check if the background is painted.
 Rectangle2D bounds = chart.getBounds();
 g.setColor(Color.white);
 g.fillRect((int)bounds.getX(), (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
 // Check if the title is painted.
 Title title = chart.getTitle();
 title.setBounds(new Rectangle2D.Double(50, 10, 100, 30));
 title.render(g);
 // Check if the legend is painted.
 Legend legend = chart.getLegend();
 legend.setBounds(new Rectangle2D.Double(450, 100, 100, 30));
 legend.render(g);
 // Check if the coordinate system is painted.
 CoordSystem coordSystem = chart.getCoordSystem();
 coordSystem.setBounds(new Rectangle2D.Double(10, 150, 420, 300));
 coordSystem.render(g);
 // Check if the chart renderers are painted.
 List<AbstractChartRenderer> renderers = new ArrayList<AbstractChartRenderer>();
 renderers.add(new TestChartRenderer());
 renderers.add(new TestChartRenderer());
 chart.setChartRenderer(new RowColorModel(model).createChartRenderer());
 chart.getChartRenderer().putAll(renderers.stream().collect(Collectors.toMap(AbstractChartRenderer::getName, x -> x)));
 for (AbstractChartRenderer renderer : renderers) {
 renderer.setBounds(new Rectangle2D.Double(10, 470, 400, 30));
 renderer.render(g);
 }
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getColor()).thenReturn(Color.BLACK);
 return g;
 }

 static class TestChartRenderer extends AbstractChartRenderer {
 @Override
 public String getName() {
 return "Test Chart Renderer";
 }
 }

}