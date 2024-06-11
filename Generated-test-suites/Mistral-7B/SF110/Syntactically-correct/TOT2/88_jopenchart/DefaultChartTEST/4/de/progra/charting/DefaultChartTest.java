import static org.junit.Assert.assertNull;

public class DefaultChartTest {

    @Test
    public void testGetPreferredSize() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Chart");
        assertNull(chart.getPreferredSize());
    }
}