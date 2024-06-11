import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        float mean = 15.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        float calculatedMin = mean - calculateDelta(stdDev);
        float calculatedMax = mean + calculateDelta(stdDev);

        assertEquals(calculatedMin, distance.getMin());
        assertEquals(calculatedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        int min = 10;
        int max = 20;

        Distance distance = Distance.buildDistance(min, max);

        float mean = computeMean(min, max);
        float stdDev = computeStandardDeviation(min, max);

        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeValues() {
        int min = -5;
        int max = -15;

        Distance distance = Distance.buildDistance(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateDeltaWithNegativeStdDev() {
        float stdDev = -5.0f;

        Distance.calculateDelta(stdDev);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithInvalidInput() {
        Distance distance = new Distance(1, 2, 0, 3);

        Distance.transformIntoCeleraAssemblerDistance(distance);
    }

    private static float calculateDelta(float stdDev) {
        return 3.0f * stdDev;
    }

    private static float computeMean(int min, int max) {
        return min / 2.0f + max / 2.0f;
    }
}