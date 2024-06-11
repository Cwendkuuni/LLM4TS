import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @org.junit.Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.0f;
        float stdDev = 3.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidMeanAndStdDev() {
        float mean = 0.0f;
        float stdDev = 0.0f;
        Distance distance = Distance.buildDistance(mean, stdDev);
    }

    @org.junit.Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 25.5f;
        float stdDev = 5.6f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        float delta = Distance.calculateDelta(stdDev);
        int expectedMin = (int)(mean - delta);
        int expectedMax = (int)(mean + delta);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
    }

    @org.junit.Test
    public void testComputeMean() {
        int min = 10;
        int max = 20;
        float expectedMean = 15.0f;
        float actualMean = Distance.computeMean(min, max);
        assertEquals(expectedMean, actualMean, 0.001);
    }

    @org.junit.Test
    public void testComputeStandardDeviation() {
        int min = 10;
        int max = 20;
        float expectedStdDev = 2.5f;
        float actualStdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(expectedStdDev, actualStdDev, 0.001);
    }

    @org.junit.Test
    public void testTransformIntoCeleraAssemblerDistanceWithinLimit() {
        int min = 10;
        int max = 20;
        float mean = 15.0f;
        float stdDev = 3.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(mean, celeraDistance.getMean(), 0.001);
        assertEquals(stdDev, celeraDistance.getStdDev(), 0.001);
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());
    }

    @org.junit.Test
    public void testTransformIntoCeleraAssemblerDistanceOutOfLimit() {
        int min = 10;
        int max = 20;
        float mean = 12.0f;
        float stdDev = 3.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotEquals(distance, celeraDistance);
        float correctedMean = (mean - 1.015f) / 3.0f;
        assertEquals(correctedMean, celeraDistance.getMean(), 0.001);
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
    }

}