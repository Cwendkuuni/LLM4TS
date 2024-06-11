import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        int min = 5;
        int max = 15;
        float mean = 10.5f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 12.5f;
        float stdDev = 4.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);
        int expectedMin = (int)(mean - 3 * stdDev);
        int expectedMax = (int)(mean + 3 * stdDev);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
        int min = 2;
        int max = 8;

        Distance distance = Distance.buildDistance(min, max);
        float expectedMean = (min + max) / 2.0f;
        float expectedStdDev = (max - min) / 6.0f;
        assertEquals(expectedMean, distance.getMean(), 0.01f);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.01f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMinOrMax() {
        int min = -5;
        int max = 5;

        try {
            Distance.buildDistance(min, max);
        } catch (IllegalArgumentException e) {
            assertEquals("Minimum value cannot be negative", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(10, 20, 15, 5);
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMean(), transformed.getMean(), 0.01f);
        assertEquals(distance.getStdDev(), transformed.getStdDev(), 0.01f);
        assertNotEquals(distance.getStdDev(), transformed.getStdDev());
        assertEquals(distance.getMin(), transformed.getMin());
        assertEquals(distance.getMax(), transformed.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanBelowDelta() {
        Distance distance = Distance.buildDistance(5, 15, 3, 5);
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(distance);
        float expectedMean = (distance.getMin() + distance.getMax()) / 2.0f;
        float expectedCorrectedStdDev = (expectedMean - 1.015f) / 3.0f;
        assertEquals(expectedMean, transformed.getMean(), 0.01f);
        assertEquals(expectedCorrectedStdDev, transformed.getStdDev(), 0.01f);
        assertEquals(distance.getMin(), transformed.getMin());
        assertEquals(distance.getMax(), transformed.getMax());
    }
}