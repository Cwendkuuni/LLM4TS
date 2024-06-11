import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;
import java.awt.*;
import java.util.*;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel model;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testDefaultChartConstruction() {
 // Test default construction
 assertNotNull(chart);
 assertNull(chart.getTitle());
 assertNull(chart.getLegend());
 assertNull(chart.getCoordSystem());
 assertFalse(chart.hasCoordSystem());
 }

 @Test
 public void testChartWithTitle() {
 // Test chart construction with title
 String title = "Test Chart with Title";
 chart = new DefaultChart(model, title);
 Title t = chart.getTitle();
 assertNotNull(t);
 assertNotNull(t.getText());
 assertEquals(title, t.getText());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystem() {
 // Test chart construction with title and coordinate system
 String title = "Test Chart with Title";
 String xaxis = "Test X Axis";
 String yaxis = "Test Y Axis";
 int coord = DefaultChart.LINEAR_X_LINEAR_Y;
 chart = new DefaultChart(model, title, coord, xaxis, yaxis);
 Title t = chart.getTitle();
 assertNotNull(t);
 assertNotNull(chart.getCoordSystem());
 assertTrue(chart.hasCoordSystem());
 }

 @Test
 public void testChartWithNullTitle() {
 // Test chart construction with null title
 chart = new DefaultChart(model, null);
 assertNull(chart.getTitle());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartWithInvalidCoordinateSystem() {
 // Test chart construction with invalid coordinate system
 int invalidCoord = 2;
 chart = new DefaultChart(model, "Test Chart", invalidCoord);
 fail("Expected IllegalArgumentException with invalid coordinate system");
 }

 @Test
 public void testGetPreferredSize() {
 // Test getPreferredSize method
 Dimension size = chart.getPreferredSize();
 assertEquals(0, size.getWidth());
 assertEquals(0, size.getHeight());
 }

 @Test
 public void testRenderWithTitle() {
 // Test render method with title
 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(eq(Color.white));
 verify(g).setColor(eq(Color.black));
 verifyNoMoreInteractions(g);
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 // Test render method with title and legend
 Graphics2D g = mock(Graphics2D.class);
 Title title = mock(Title.class);
 Legend legend = mock(Legend.class);
 when(title.getPreferredSize()).thenReturn(new Dimension(100, 20));
 when(legend.getPreferredSize()).thenReturn(new Dimension(50, 30));
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.render(g);
 verify(g).fillRect(anyInt(), anyInt(), anyInt(), anyInt()); // Fill background
 verify(title).render(g);
 verify(legend).render(g);
 }

}