import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private ChartDataModel model;
 private AbstractChartRenderer renderer1;
 private AbstractChartRenderer renderer2;
 private DefaultChart chart;

 @Before
 public void setUp() {
 model = mock(ChartDataModel.class);
 renderer1 = mock(AbstractChartRenderer.class);
 renderer2 = mock(AbstractChartRenderer.class);
 when(model.getName()).thenReturn("Model Name");
 when(renderer1.getName()).thenReturn("Renderer 1");
 when(renderer2.getName()).thenReturn("Renderer 2");
 }

 @Test
 public void testDefaultChartConstruction() {
 // Test empty constructor
 DefaultChart emptyChart = new DefaultChart();
 assertNull(emptyChart.getChartDataModel());
 assertNull(emptyChart.getTitle());
 assertNull(emptyChart.getLegend());
 assertNull(emptyChart.getCoordSystem());
 assertNull(emptyChart.getChartRenderer());

 // Test constructor with model and title
 chart = new DefaultChart(model, "Test Chart Title");
 assertNotNull(chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertNotNull(chart.getChartRenderer());
 assertNotNull(chart.getChartRenderer().size());
 assertFalse(chart.getChartRenderer().isEmpty());

 // Test constructor with model, title, and coordinate system
 chart = new DefaultChart(model, "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getChartDataModel());
 assertNotNull(chart.getTitle());
 assertNotNull(chart.getLegend());
 assertNotNull(chart.getCoordSystem());
 assertNotNull(chart.getChartRenderer());
 assertNotNull(chart.getChartRenderer().size());
 assertFalse(chart.getChartRenderer().isEmpty());
 }

 @Test
 public void testGetPreferredSize() {
 // Test getPreferredSize method with no title, legend, or coordinate system
 chart = new DefaultChart(model, "Test Chart Title");
 assertNull(chart.getPreferredSize());

 // Test getPreferredSize method with title, legend, and coordinate system
 chart = new DefaultChart(model, "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.setTitle(new Title("Test Title"));
 chart.setLegend(new Legend(new RowColorModel(model)));
 chart.setCoordSystem(new CoordSystem(model));
 chart.getTitle().setPreferredSize(new Dimension(100, 50));
 chart.getLegend().setPreferredSize(new Dimension(150, 100));
 chart.getCoordSystem().setPreferredSize(new Dimension(500, 500));
 Dimension preferredSize = chart.getPreferredSize();
 assertEquals(561, preferredSize.height);
 assertEquals(711, preferredSize.width);
 }

 @Test
 public void testRender() {
 // Test render method with no title, legend, or coordinate system
 Graphics2D g = mock(Graphics2D.class);
 chart = new DefaultChart(model, "Test Chart Title");
 chart.render(g);

 // Test render method with title, legend, and coordinate system
 chart = new DefaultChart(model, "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.setTitle(new Title("Test Title"));
 chart.setLegend(new Legend(new RowColorModel(model)));
 chart.setCoordSystem(new CoordSystem(model));
 chart.getTitle().setPreferredSize(new Dimension(100, 50));
 chart.getLegend().setPreferredSize(new Dimension(150, 100));
 chart.getCoordSystem().setPreferredSize(new Dimension(500, 500));
 chart.getChartRenderer().put(renderer1.getName(), renderer1);
 chart.getChartRenderer().put(renderer2.getName(), renderer2);
 chart.render(g);

 // Test render method with exception when coordinate system is not set
 chart = new DefaultChart(model, "Test Chart Title", DefaultChart.NO_COORDINATE_SYSTEM);
 chart.setTitle(new Title("Test Title"));
 chart.setLegend(new Legend(new RowColorModel(model)));
 chart.setChartRenderer(new HashMap<>(Arrays.asList(renderer1, renderer2)));
 try {
 chart.render(g);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullGraphics2D() {
 chart = new DefaultChart(model, "Test Chart Title", DefaultChart.LINEAR_X_LINEAR_Y);
 chart.setTitle(new Title("Test Title"));
 chart.setLegend(new Legend(new RowColorModel(model)));
 chart.setCoordSystem(new CoordSystem(model));
 chart.getChartRenderer().put(renderer1.getName(), renderer1);
 chart.getChartRenderer().put(renderer2.getName(), renderer2);
 chart.render(null);
 }
}