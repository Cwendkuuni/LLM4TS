import org.junit.Test;
import static org.junit.Assert.*;
import de.progra.charting.*;
import de.progra.charting.model.*;
import java.util.Arrays;

public class DefaultChartTest {

 private DefaultChart chart;

 @Test
 public void testDefaultChartConstruction() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart expectedChart = new DefaultChart(model, "Default Chart");
 assertEquals(expectedChart.getClass(), DefaultChart.class);
 }

 @Test
 public void testChartConstructionWithTitle() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart expectedChart = new DefaultChart(model, "Default Chart");
 assertEquals(expectedChart.getTitle().getText(), "Default Chart");
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart expectedChart = new DefaultChart(model, "Default Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(expectedChart.getCoordSystem());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystemAndAxisUnits() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart expectedChart = new DefaultChart(model, "Default Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis Unit", "Y-Axis Unit");
 assertEquals(expectedChart.getCoordSystem().getXAxisUnit(), "X-Axis Unit");
 assertEquals(expectedChart.getCoordSystem().getYAxisUnit(), "Y-Axis Unit");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChartConstructionWithInvalidCoordinateSystem() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = null;
 try {
 chart = new DefaultChart(model, "Default Chart", 2);
 } catch (IllegalArgumentException e) {
 this.chart = chart;
 }
 assertNull(chart);
 }

 @Test
 public void testGetPreferredSizeReturnsNull() {
 ChartDataModel model = new ChartDataModel();
 DefaultChart chart = new DefaultChart(model, "Default Chart");
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRenderMethod() {
 ChartDataModel model = new ChartDataModel();
 ChartData chartData = new ChartData("Test Data", Arrays.asList(new DataPoint(1.0, 1.0), new DataPoint(2.0, 2.0), new DataPoint(3.0, 3.0)));
 model.setChartData(chartData);
 DefaultChart chart = new DefaultChart(model, "Default Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 Title title = new Title("Test Title");
 chart.setTitle(title);
 Legend legend = new Legend(new RowColorModel(model));
 chart.setLegend(legend);
 CoordSystem coordSystem = new CoordSystem(model);
 chart.setCoordSystem(coordSystem);
 LineChartRenderer lineChartRenderer = new LineChartRenderer(chartData);
 chart.getChartRenderer().put("Line Chart", lineChartRenderer);

 int width = 500;
 int height = 500;
 BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
 Graphics2D g = image.createGraphics();
 chart.render(g);
 g.dispose();

 Title expectedTitle = new Title("Test Title");
 expectedTitle.setBounds(new Rectangle(width / 2 - (int)(expectedTitle.getPreferredSize().getWidth() / 2.0), 0, (int)expectedTitle.getPreferredSize().getWidth(), (int)expectedTitle.getPreferredSize().getHeight()));
 Legend expectedLegend = new Legend(new RowColorModel(model));
 expectedLegend.setBounds(new Rectangle((int)(width - expectedLegend.getPreferredSize().getWidth()), (int)(height / 2 - expectedLegend.getPreferredSize().getHeight() / 2.0), (int)expectedLegend.getPreferredSize().getWidth(), (int)expectedLegend.getPreferredSize().getHeight()));

 // Check title rendering
 assertFalse(title.getBounds().isEmpty());
 assertFalse(expectedTitle.getBounds().isEmpty());
 assertEquals(title.getBounds().getX(), expectedTitle.getBounds().getX());
 assertEquals(title.getBounds().getY(), expectedTitle.getBounds().getY());
 assertEquals(title.getBounds().getWidth(), expectedTitle.getBounds().getWidth());
 assertEquals(title.getBounds().getHeight(), expectedTitle.getBounds().getHeight());

 // Check legend rendering
 assertFalse(legend.getBounds().isEmpty());
 assertFalse(expectedLegend.getBounds().isEmpty());
 assertEquals(legend.getBounds().getX(), expectedLegend.getBounds().getX());
 assertEquals(legend.getBounds().getY(), expectedLegend.getBounds().getY());
 assertEquals(legend.getBounds().getWidth(), expectedLegend.getBounds().getWidth());
 assertEquals(legend.getBounds().getHeight(), expectedLegend.getBounds().getHeight());

 // Check coordinate system rendering
 assertFalse(coordSystem.getBounds().isEmpty());

 // Check chart renderer rendering
 LineChartRenderer lineChartRendererExpected = new LineChartRenderer(chartData);
 lineChartRendererExpected.setBounds(new Rectangle(0, 0, width - expectedLegend.getPreferredSize().getWidth(), height - title.getPreferredSize().getHeight() - 5));
 assertFalse(lineChartRenderer.getBounds().isEmpty());
 assertEquals(lineChartRenderer.getBounds().getX(), lineChartRendererExpected.getBounds().getX());
 assertEquals(lineChartRenderer.getBounds().getY(), lineChartRendererExpected.getBounds().getY());
 assertEquals(lineChartRenderer.getBounds().getWidth(), lineChartRendererExpected.getBounds().getWidth());
 assertEquals(lineChartRenderer.getBounds().getHeight(), lineChartRendererExpected.getBounds().getHeight());
 }

}