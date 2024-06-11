import static org.junit.Assert.assertEquals;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {
    @Test
    public void testBuildDistance() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance expected = new Distance(min, max, mean, stdDev);
        Distance actual = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(expected.getMin(), actual.getMin());
        assertEquals(expected.getMax(), actual.getMax());
        assertEquals(expected.getMean(), actual.getMean(), 0.001f);
        assertEquals(expected.getStdDev(), actual.getStdDev(), 0.001f);
    }
}