package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01); // Allow for rounding error
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(mean, stdDev);
        int expectedMin = (int)(mean - calculateDelta(stdDev));
        int expectedMax = (int)(mean + calculateDelta(stdDev));
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        int min = 10;
        int max = 20;

        Distance distance = Distance.buildDistance(min, max);
        float expectedMean = computeMean(min, max);
        float expectedStdDev = computeStandardDeviation(min, max);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(expectedMean, distance.getMean(), 0.01);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        float mean = 10.5f;
        float stdDev = 3.0f;
        Distance distance = Distance.buildDistance(mean, stdDev);

        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float correctedStdDev = (mean - 1.015f) / 3.0f;
        int expectedMin = distance.getMin();
        int expectedMax = distance.getMax();
        assertEquals(expectedMin, celeraDistance.getMin());
        assertEquals(expectedMax, celeraDistance.getMax());
        assertEquals(mean, celeraDistance.getMean(), 0.01);
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01);

        Distance notTransformedDistance = Distance.buildDistance(mean - calculateDelta(stdDev), mean + calculateDelta(stdDev));
        assertNotSame(celeraDistance, notTransformedDistance);
    }

    private static float calculateDelta(float stdDev) {
        return 3.0f * stdDev;
    }

    private static float computeMean(int min, int max) {
        return min / 2.0f + max / 2.0f;
    }

    private static float computeStandardDeviation(int min, int max) {
        return (max - min) / 6.0f;
    }
}