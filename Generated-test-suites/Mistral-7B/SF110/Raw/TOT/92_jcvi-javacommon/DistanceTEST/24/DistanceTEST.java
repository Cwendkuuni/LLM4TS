package org.jcvi.jillion.assembly.ca.frg.test;

import org.jcvi.jillion.assembly.ca.frg.Distance;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01f); // allow for rounding error
        assertEquals(stdDev, distance.getStdDev(), 0.01f); // allow for rounding error
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        int expectedMin = (int)(mean - calculateDelta(stdDev));
        int expectedMax = (int)(mean + calculateDelta(stdDev));

        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01f); // allow for rounding error
        assertEquals(stdDev, distance.getStdDev(), 0.01f); // allow for rounding error
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
        int min = 10;
        int max = 20;

        Distance distance = Distance.buildDistance(min, max);

        float expectedMean = computeMean(min, max);
        float expectedStdDev = computeStandardDeviation(min, max);

        assertEquals(expectedMean, distance.getMean(), 0.01f); // allow for rounding error
        assertEquals(expectedStdDev, distance.getStdDev(), 0.01f); // allow for rounding error
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        float mean = 10.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);

        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertEquals(distance.getMin(), celeraDistance.getMin());
        assertEquals(distance.getMax(), celeraDistance.getMax());
        assertEquals(mean, celeraDistance.getMean(), 0.01f); // allow for rounding error

        float correctedStdDev = (mean - 1.015f) / 3.0f;
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01f); // allow for rounding error
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidMeanAndStdDev() {
        float mean = 10.5f;
        float stdDev = -2.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        // This test should throw an IllegalArgumentException
    }

    private static float computeMean(int min, int max) {
        return (min + max) / 2.0f;
    }

    private static float computeStandardDeviation(int min, int max) {
        return (max - min) / 6.0f;
    }

    private static float calculateDelta(float stdDev) {
        return 3.0f * stdDev;
    }
}