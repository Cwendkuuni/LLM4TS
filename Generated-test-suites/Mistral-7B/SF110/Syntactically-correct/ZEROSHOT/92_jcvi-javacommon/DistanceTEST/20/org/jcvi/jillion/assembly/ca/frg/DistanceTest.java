package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        assertNotNull(distance);
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 10.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        assertNotNull(distance);
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        int min = 5;
        int max = 15;

        Distance distance = Distance.buildDistance(min, max);

        assertNotNull(distance);
        assertEquals(computeMean(min, max), distance.getMean(), 0.01f);
        assertEquals(computeStandardDeviation(min, max), distance.getStdDev(), 0.01f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(min, max);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertEquals(distance.getMin(), celeraDistance.getMin());
        assertEquals(distance.getMax(), celeraDistance.getMax());
        assertNotEquals(distance.getMean(), celeraDistance.getMean());
        assertNotEquals(distance.getStdDev(), celeraDistance.getStdDev());
        assertEquals(mean - (3.0f * stdDev), celeraDistance.getMean(), 0.01f);
        assertEquals(stdDev / 3.0f, celeraDistance.getStdDev(), 0.01f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceNegativeMinOrMax() {
        int min = -5;
        int max = -20;

        Distance.buildDistance(min, max);
    }

    private static float computeMean(final int min, final int max) {
        return min / 2.0f + max / 2.0f;
    }

    private static float computeStandardDeviation(final int min, final int max) {
        return (max - min) / 6.0f;
    }
}