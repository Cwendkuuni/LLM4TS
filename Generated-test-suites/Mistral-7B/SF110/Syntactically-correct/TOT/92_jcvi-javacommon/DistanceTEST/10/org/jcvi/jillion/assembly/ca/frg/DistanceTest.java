package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(5.5f, distance.getMean(), 0.01); // typical usage
        assertEquals(2.5f, distance.getStdDev(), 0.01); // typical usage
        assertEquals(min, distance.getMin()); // edge case: minimum value
        assertEquals(max, distance.getMax()); // edge case: maximum value
        assertFalse(distance.transformIntoCeleraAssemblerDistance(distance).equals(distance)); // error scenario: invalid standard deviation
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int expectedMin = (int) (mean - calculateDelta(stdDev));
        int expectedMax = (int) (mean + calculateDelta(stdDev));
        assertEquals(expectedMin, distance.getMin()); // typical usage
        assertEquals(expectedMax, distance.getMax()); // typical usage
        assertEquals(mean, distance.getMean(), 0.01); // typical usage
        assertEquals(stdDev, distance.getStdDev(), 0.01); // typical usage
        assertNotEquals(expectedMin - 1, distance.getMin()); // edge case: minimum value is one less
        assertNotEquals(expectedMax + 1, distance.getMax()); // edge case: maximum value is one more
        assertFalse(distance.transformIntoCeleraAssemblerDistance(distance).equals(distance)); // error scenario: invalid mean
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        int min = 1;
        int max = 10;
        Distance distance = Distance.buildDistance(min, max);
        float expectedMean = computeMean(min, max);
        float expectedStdDev = computeStandardDeviation(min, max);
        assertEquals(expectedMean, distance.getMean(), 0.01); // typical usage
        assertEquals(expectedStdDev, distance.getStdDev(), 0.01); // typical usage
        assertNotEquals(expectedMean + 0.01, distance.getMean()); // edge case: mean is slightly off
        assertNotEquals(expectedStdDev * 1.5, distance.getStdDev()); // edge case: standard deviation is significantly different
        assertFalse(distance.transformIntoCeleraAssemblerDistance(distance).equals(distance)); // error scenario: invalid mean or standard deviation
    }

    private static float calculateDelta(final float stdDev) {
        return 3.0f * stdDev;
    }

    private static float computeMean(final int min, final int max) {
        return min / 2.0f + max / 2.0f;
    }
}