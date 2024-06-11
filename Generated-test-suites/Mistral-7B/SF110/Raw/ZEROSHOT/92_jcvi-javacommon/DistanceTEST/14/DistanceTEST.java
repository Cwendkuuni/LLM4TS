package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMeanStdDev() {
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        float delta = Distance.calculateDelta(stdDev);
        int plusDelta = (int)(mean + delta);
        int minusDelta = (int)(mean - delta);
        assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMinMax() {
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
        float mean = 10.5f;
        float stdDev = 3.0f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float correctedStdDev = (mean - 1.015f) / 3.0f;
        assertEquals(distance.getMin(), celeraDistance.getMin());
        assertEquals(distance.getMax(), celeraDistance.getMax());
        assertEquals(mean, celeraDistance.getMean(), 0.001f);
        assertNotEquals(stdDev, celeraDistance.getStdDev());
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceSameMean() {
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance, celeraDistance);
    }
}