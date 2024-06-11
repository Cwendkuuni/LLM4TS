package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        assertNotNull(distance);

        float delta = Distance.calculateDelta(stdDev);
        int expectedMin = (int)(mean - delta);
        int expectedMax = (int)(mean + delta);

        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        int min = 10;
        int max = 20;

        Distance distance = Distance.buildDistance(min, max);

        assertNotNull(distance);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(computeMean(min, max), distance.getMean(), 0.01f);
        assertEquals(computeStandardDeviation(min, max), distance.getStdDev(), 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(min, max);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertNotNull(celeraDistance);
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());
        assertEquals(mean, celeraDistance.getMean(), 0.01f);
        assertNull(celeraDistance.getStdDev());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceMeanBelowDelta() {
        int min = 10;
        int max = 20;
        float mean = 12.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(min, max);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertNotNull(celeraDistance);
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());
        assertEquals(mean - (1.015f * 3), celeraDistance.getMean(), 0.01f);
        assertEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.01f);
    }

    private static float computeMean(final int min, final int max) {
        return min / 2.0f + max / 2.0f;
    }

    private static float computeStandardDeviation(final int min, final int max) {
        return (max - min) / 6.0f;
    }

}