package de.progra.charting.test;

import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;
import org.junit.*;

public class DefaultChartTest {

 private DefaultChart chart;
 
 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart chart = new DefaultChart();
 assertNotNull(chart);
 }

 @Test
 public void testChartWithTitleConstruction() {
 assertNotNull(chart.getTitle());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemConstruction() {
 chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemAndAxisUnitsConstruction() {
 chart = new DefaultChart(new ChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-axis", "Y-axis");
 assertNotNull(chart.getCoordSystem());
 assertNotNull(chart.getCoordSystem().getXAxisUnit());
 assertNotNull(chart.getCoordSystem().getYAxisUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCoordinateSystemConstruction() {
 chart = new DefaultChart(new ChartDataModel(), "Test Chart", 2);
 assertNull(chart.getCoordSystem());
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRender() {
 // Assuming that there is a stub for Graphics2D and the render methods of Title, Legend, CoordSystem, and AbstractChartRenderer are mocked
 // to return non-null PreferredSize and Dimension objects
 Title title = mock(Title.class);
 Legend legend = mock(Legend.class);
 CoordSystem coordSystem = mock(CoordSystem.class);
 AbstractChartRenderer renderer = mock(AbstractChartRenderer.class);
 when(title.getPreferredSize()).thenReturn(new Dimension(100, 50));
 when(legend.getPreferredSize()).thenReturn(new Dimension(150, 100));
 when(coordSystem.getBounds()).thenReturn(new Rectangle(0, 50, 500, 400));
 when(renderer.getBounds()).thenReturn(new Rectangle(0, 50, 500, 400));
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(coordSystem);
 chart.getChartRenderer().put("Test Renderer", renderer);

 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);

 // Verify that title, legend, coordSystem, and renderer have been rendered
 verify(title).render(g);
 verify(legend).render(g);
 verify(coordSystem).render(g);
 verify(renderer).render(g);
 }

 @After
 public void tearDown() {
 chart = null;
 }

}