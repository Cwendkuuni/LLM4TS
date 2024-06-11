import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;
import java.util.*;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel model;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 chart = new DefaultChart(model, "Default Chart");
 }

 @Test
 public void testDefaultChartConstruction() {
 DefaultChart defaultChart = new DefaultChart();
 assertNull(defaultChart.getChartDataModel());
 assertNull(defaultChart.getTitle());
 assertNull(defaultChart.getLegend());
 assertNull(defaultChart.getCoordSystem());

 defaultChart = new DefaultChart(model, "Default Chart");
 assertEquals(model, defaultChart.getChartDataModel());
 assertNotNull(defaultChart.getTitle());
 assertNotNull(defaultChart.getLegend());
 assertNull(defaultChart.getCoordSystem());

 defaultChart = new DefaultChart(model, "Default Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertEquals(model, defaultChart.getChartDataModel());
 assertNotNull(defaultChart.getTitle());
 assertNotNull(defaultChart.getLegend());
 assertNotNull(defaultChart.getCoordSystem());
 }

 @Test
 public void testRenderTitleAndLegend() {
 chart.setTitle(new Title("Test Title"));
 chart.setLegend(new Legend(new RowColorModel(model)));

 ChartRenderer chartRenderer = new StubChartRenderer();
 chart.setChartRenderer(chartRenderer);

 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);

 verify(g).setColor(eq(Color.white));
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(eq(Color.black));

 Title title = (Title)chart.getTitle();
 Legend legend = (Legend)chart.getLegend();

 assertEquals("Test Title", title.getText());
 assertEquals(title.getBounds().getX(), title.getBounds().getX() + title.getBounds().getWidth() / 2.0, 0.1);
 assertEquals(title.getBounds().getY(), 0);
 assertEquals(title.getBounds().getHeight(), title.getBounds().getHeight() + title.getBounds().getWidth() / 2.0, 0.1);

 assertEquals(chart.getLegend().getBounds().getX(), chart.getLegend().getBounds().getX() + chart.getLegend().getBounds().getWidth(), 0.1);
 assertEquals(chart.getLegend().getBounds().getY(), chart.getBounds().getHeight() / 2 - chart.getLegend().getBounds().getHeight() / 2.0, 0.1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCoordinateSystemOption() {
 chart = new DefaultChart(model, "Default Chart", 2);
 }

 @Test
 public void testRenderCoordinateSystem() {
 chart.setChartRenderer(new StubChartRenderer());
 chart.setCoordSystem(new CoordSystem(model));

 Graphics2D g = mock(Graphics2D.class);
 chart.render(g);

 verify(g).setColor(eq(Color.white));
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(eq(Color.black));

 ChartRenderer chartRenderer = chart.getChartRenderer();
 CoordSystem coordSystem = chart.getCoordSystem();

 assertEquals(chartRenderer.getClass(), StubChartRenderer.class);
 assertEquals(coordSystem.getClass(), CoordSystem.class);
 }
}