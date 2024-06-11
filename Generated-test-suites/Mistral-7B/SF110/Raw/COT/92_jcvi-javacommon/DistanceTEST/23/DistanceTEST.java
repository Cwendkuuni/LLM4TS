import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int expectedMin = (int) (mean - calculateDelta(stdDev));
        int expectedMax = (int) (mean + calculateDelta(stdDev));
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 2.5f;
        float delta = Distance.calculateDelta(stdDev);
        assertEquals(3.0f * stdDev, delta, 0.01);
    }

    @Test
    public void testComputeStandardDeviation() {
        int min = 10;
        int max = 20;
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals((max - min) / 6.0f, stdDev, 0.01);
    }

    @Test
    public void testComputeMean() {
        int min = 10;
        int max = 20;
        float mean = Distance.computeMean(min, max);
        assertEquals((min + max) / 2.0f, mean, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidMinMax() {
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;
        Distance.buildDistance(min, max);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(10, 20);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float mean = distance.getMean();
        if (mean < Distance.calculateDelta(distance.getStdDev())) {
            assertEquals(distance.getMin(), celeraDistance.getMin());
            assertEquals(distance.getMax(), celeraDistance.getMax());
            float correctedStdDev = (mean - 1.015f) / 3.0f;
            assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01);
            assertEquals(mean, celeraDistance.getMean(), 0.01);
        } else {
            assertEquals(distance, celeraDistance);
        }
    }

    @Test
    public void testHashcodeEquals() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.5f, 2.5f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.5f, 2.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
        assertEquals(distance1, distance2);
    }
}