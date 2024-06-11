package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(5.5f, distance.getMean());
        assertEquals(2.5f, distance.getStdDev());
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanOnly() {
        Distance distance = Distance.buildDistance(5.5f, 2.5f);
        assertEquals(5.5f, distance.getMean());
        assertNull(distance.getStdDev());
        int min = (int) (5.5f - 3.0f * 2.5f);
        int max = (int) (5.5f + 3.0f * 2.5f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 2.5f;
        float delta = Distance.calculateDelta(stdDev);
        assertEquals(7.5f, delta);
    }

    @Test
    public void testBuildDistanceMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        float mean = Distance.computeMean(1, 10);
        float stdDev = Distance.computeStandardDeviation(1, 10);
        assertEquals(mean, distance.getMean());
        assertEquals(stdDev, distance.getStdDev());
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5.5f, transformed.getMean());
        assertEquals(transformed.getStdDev(), 1.71625f, 0.001f); // Calculated standard deviation for comparison
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceNegativeMean() {
        Distance distance = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        Distance.transformIntoCeleraAssemblerDistance(distance);
    }
}