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
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 3.2f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        float delta = Distance.calculateDelta(stdDev);
        int expectedMin = (int)(mean - delta);
        int expectedMax = (int)(mean + delta);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        int min = 10;
        int max = 20;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.2f;
        Distance distance = Distance.buildDistance(min, max);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float correctedStdDev = (mean - 1.015f) / 3.0f;
        int expectedMin = min;
        int expectedMax = max;
        assertEquals(expectedMin, celeraDistance.getMin());
        assertEquals(expectedMax, celeraDistance.getMax());
        assertEquals(mean, celeraDistance.getMean(), 0.001f);
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.001f);

        Distance distanceWithMeanBelowStdDev = Distance.buildDistance(min, max, mean - 2 * stdDev, stdDev);
        celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distanceWithMeanBelowStdDev);
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());
        assertNotEquals(mean, celeraDistance.getMean(), 0.001f);
        assertNotEquals(stdDev, celeraDistance.getStdDev(), 0.001f);
    }
}