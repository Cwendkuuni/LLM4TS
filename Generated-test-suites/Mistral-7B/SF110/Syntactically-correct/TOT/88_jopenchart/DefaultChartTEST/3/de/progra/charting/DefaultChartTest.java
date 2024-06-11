import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;
import java.awt.*;
import java.util.*;

public class DefaultChartTest {

 // Test for empty constructor
 @Test
 public void testEmptyConstructor() {
 DefaultChart chart = new DefaultChart();
 assertNull(chart.getChartDataModel());
 assertNull(chart.getRowColorModel());
 assertNull(chart.getLegend());
 assertNull(chart.getTitle());
 assertNull(chart.getCoordSystem());
 assertFalse(chart.getChartRenderer().isEmpty());
 }

 // Test for constructor with ChartDataModel and title
 @Test
 public void testConstructorWithModelAndTitle() {
 ChartDataModel model = mock(ChartDataModel.class);
 when(model.getSeries()).thenReturn(Arrays.asList(new SeriesData("Series1", Arrays.asList(new DataPoint(1, 1)))));

 DefaultChart chart = new DefaultChart(model, "Test Chart");
 assertEquals(model, chart.getChartDataModel());
 assertSame(new RowColorModel(model), chart.getRowColorModel());
 assertSame(new Legend(chart.getRowColorModel()), chart.getLegend());
 assertSame(new Title("Test Chart"), chart.getTitle());
 assertNull(chart.getCoordSystem());
 assertFalse(chart.getChartRenderer().isEmpty());
 }

 // Test for constructor with ChartDataModel, title, and linear coordinate system
 @Test
 public void testConstructorWithModelTitleAndLinearCoordinateSystem() {
 ChartDataModel model = mock(ChartDataModel.class);
 when(model.getSeries()).thenReturn(Arrays.asList(new SeriesData("Series1", Arrays.asList(new DataPoint(1, 1)))));

 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertEquals(model, chart.getChartDataModel());
 assertSame(new RowColorModel(model), chart.getRowColorModel());
 assertSame(new Legend(chart.getRowColorModel()), chart.getLegend());
 assertSame(new Title("Test Chart"), chart.getTitle());
 assertSame(new CoordSystem(model), chart.getCoordSystem());
 assertFalse(chart.getChartRenderer().isEmpty());
 }

 // Test for constructor with ChartDataModel, title, linear coordinate system, x axis label, and y axis label
 @Test
 public void testConstructorWithModelTitleLinearCoordinateSystemXAxisLabelAndYAxisLabel() {
 ChartDataModel model = mock(ChartDataModel.class);
 when(model.getSeries()).thenReturn(Arrays.asList(new SeriesData("Series1", Arrays.asList(new DataPoint(1, 1)))));

 DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-axis", "Y-axis");
 assertEquals(model, chart.getChartDataModel());
 assertSame(new RowColorModel(model), chart.getRowColorModel());
 assertSame(new Legend(chart.getRowColorModel()), chart.getLegend());
 assertSame(new Title("Test Chart"), chart.getTitle());
 assertSame(new CoordSystem(model), chart.getCoordSystem());
 assertTrue(chart.getCoordSystem().getXAxisUnit().equals("X-axis"));
 assertTrue(chart.getCoordSystem().getYAxisUnit().equals("Y-axis"));
 assertFalse(chart.getChartRenderer().isEmpty());
 }

 // Test for getPreferredSize method
 @Test
 public void testGetPreferredSize() {
 ChartDataModel model = mock(ChartDataModel.class);
 when(model.getSeries()).thenReturn(Arrays.asList(new SeriesData("Series1", Arrays.asList(new DataPoint(1, 1)))));

 DefaultChart chart = new DefaultChart(model, "Test Chart");
 chart.setCoordSystem(new CoordSystem(model));
 chart.getCoordSystem().setXAxisUnit("X-axis");
 chart.getCoordSystem().setYAxisUnit("Y-axis");

 assertNull(chart.getPreferredSize());
}

 // Test for render method with no title and legend
 @Test
 public void testRenderWithoutTitleAndLegend() {
 ChartDataModel model = mock(ChartDataModel.class);
 when(model.getSeries()).thenReturn(Arrays.asList(new SeriesData("Series1", Arrays.asList(new DataPoint(1, 1)))));
 Graphics2D gMock = mock(Graphics2D.class);
 DefaultChart chart = new DefaultChart(model, "Test Chart");

 chart.render(gMock);
 verify(gMock, never()).setColor(any(Color.class));
 verify(gMock, never()).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(gMock, never()).setColor(Color.black);
 }

 // Test for render method with title and legend
 @Test
 public void testRenderWithTitleAndLegend() {
 ChartDataModel model = mock(ChartDataModel.class);
 when(model.getSeries()).thenReturn(Arrays.asList(new SeriesData("Series1", Arrays.asList(new DataPoint(1, 1)))));
 Title title = new Title("Test Title");
 Legend legend = new Legend(new RowColorModel(model));
 when(title.getPreferredSize().height).thenReturn(40);
 when(legend.getPreferredSize().width).thenReturn(150);
 when(legend.getPreferredSize().height).thenReturn(50);
 Graphics2D gMock = mock(Graphics2D.class);
 DefaultChart chart = new DefaultChart(model, "Test Chart");
 chart.setTitle(title);
 chart.setLegend(legend);
 chart.setCoordSystem(new CoordSystem(model));
 chart.getCoordSystem().setXAxisUnit("X-axis");
 chart.getCoordSystem().setYAxisUnit("Y-axis");

 chart.render(gMock);
 verify(gMock).setColor(Color.white);
 verify(gMock).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 verify(gMock).setColor(Color.black);
 verify(gMock).setColor(Color.black);
 verify(gMock).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
 }
}