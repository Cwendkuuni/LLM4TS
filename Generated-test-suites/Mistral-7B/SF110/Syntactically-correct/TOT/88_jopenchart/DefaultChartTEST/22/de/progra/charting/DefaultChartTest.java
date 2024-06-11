import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.Model.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel model;
 private AbstractChartRenderer renderer;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 renderer = new TestChartRenderer();
 chart.setChartRenderer(Collections.singletonMap("Test Renderer", renderer));
 }

 @Test
 public void testDefaultChartConstruction() {
 ChartDataModel model = chart.getChartDataModel();
 assertNotNull(model);
 assertEquals("Test Chart", chart.getTitle().getText());
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testChartWithTitle() {
 Title title = chart.getTitle();
 assertEquals("Test Chart", title.getText());
 }

 @Test
 public void testChartWithTitleAndCoordinates() {
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNull(coordSystem);
 }

 @Test
 public void testChartWithTitleAndCoordinatesAndAxis() {
 ChartDataModel model = chart.getChartDataModel();
 assertNotNull(model);
 Title title = chart.getTitle();
 assertEquals("Test Chart", title.getText());
 CoordSystem coordSystem = chart.getCoordSystem();
 assertNotNull(coordSystem);
 assertNull(coordSystem.getXAxisUnit());
 assertNull(coordSystem.getYAxisUnit());
 }

 @Test
 public void testRender() {
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.white);
 verify(g).setColor(Color.black);
 }

 @Test
 public void testRenderWithTitle() {
 Graphics2D g = mock(Graphics2D.class);
 Title title = new Title("Test Title");
 chart.setTitle(title);
 chart.render(g);
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(title).render(g);
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 Graphics2D g = mock(Graphics2D.class);
 Title title = new Title("Test Title");
 Legend legend = new Legend(new RowColorModel(model));
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.render(g);
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(title).render(g);
 verify(legend).render(g);
 }

 @Test
 public void testRenderWithTitleAndLegendAndCoordinates() {
 Graphics2D g = mock(Graphics2D.class);
 Title title = new Title("Test Title");
 Legend legend = new Legend(new RowColorModel(model));
 CoordSystem coordSystem = new CoordSystem(model);
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(coordSystem);
 chart.render(g);
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(title).render(g);
 verify(legend).render(g);
 verify(coordSystem).render(g);
 }

 @Test
 public void testRenderWithData() {
 Graphics2D g = mock(Graphics2D.class);
 ChartDataModel model = chart.getChartDataModel();
 model.addSeries("Series 1", new double[][]{{1, 2}, {3, 4}, {5, 6}});
 chart.render(g);
 verify(g, times(2)).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(g).setColor(Color.WHITE);
 verify(g).setColor(Color.BLACK);
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullGraphics() {
 chart.render(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRenderWithInvalidCoordinateSystem() {
 chart.setCoordSystem(new CoordSystem(model) {
 @Override public void setXAxisUnit(String unit) {
 throw new UnsupportedOperationException("Not supported yet.");
 }
 });
 chart.render(mock(Graphics2D.class));
 }

 @Test
 public void testGetPreferredSize() {
 Dimension size = chart.getPreferredSize();
 assertNull(size);
 }
}

class TestChartRenderer implements AbstractChartRenderer {
 @Override public void render(Graphics2D g) {
 // Empty implementation for testing
 }

 @Override public Rectangle2D getBounds() {
 return new Rectangle2D.Double(0, 0, 100, 100);
 }
}