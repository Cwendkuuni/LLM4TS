import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @org.junit.Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @org.junit.Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int min = distance.getMin();
        int max = distance.getMax();
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
        assertEquals(min, Math.min((int)(mean - calculateDelta(stdDev)), Math.min(min, max-min)));
        assertEquals(max, Math.max((int)(mean + calculateDelta(stdDev)), Math.max(min, max-min)));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMean() {
        float mean = -1.5f;
        float stdDev = 2.5f;
        Distance.buildDistance(mean, stdDev);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeStdDev() {
        float mean = 2.5f;
        float stdDev = -2.5f;
        Distance.buildDistance(mean, stdDev);
    }

    @org.junit.Test
    public void testComputeMean() {
        int min = 1;
        int max = 10;
        float expected = 5.5f;
        float actual = Distance.computeMean(min, max);
        assertEquals(expected, actual, 0.01);
    }

    @org.junit.Test
    public void testComputeStandardDeviation() {
        int min = 1;
        int max = 10;
        float expected = 1.6666667f;
        float actual = Distance.computeStandardDeviation(min, max);
        assertEquals(expected, actual, 0.01);
    }

    @org.junit.Test
    public void testTransformIntoCeleraAssemblerDistanceWithValidDistance() {
        float mean = 5.5f;
        float stdDev = 2.5f;
        int min = 1;
        int max = 11;
        Distance distance = new Distance(min, max, mean, stdDev);
        Distance expected = new Distance(min, max, mean, 0.8666667f);
        Distance actual = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(expected.getMean(), actual.getMean(), 0.01);
        assertEquals(expected.getStdDev(), actual.getStdDev(), 0.01);
        assertEquals(expected.getMin(), actual.getMin());
        assertEquals(expected.getMax(), actual.getMax());
    }

    @org.junit.Test
    public void testTransformIntoCeleraAssemblerDistanceWithOutOfRangeDistance() {
        float mean = 1.5f;
        float stdDev = 2.5f;
        int min = 1;
        int max = 10;
        Distance distance = new Distance(min, max, mean, stdDev);
        assertNotEquals(distance, Distance.transformIntoCeleraAssemblerDistance(distance));
    }

    private float calculateDelta(final float stdDev) {
        return 3.0f * stdDev;
    }
}