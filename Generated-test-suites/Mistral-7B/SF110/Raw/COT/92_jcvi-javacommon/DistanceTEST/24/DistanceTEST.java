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

        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanOnly() {
        float mean = 15.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        float delta = Distance.calculateDelta(stdDev);
        int expectedMin = (int)(mean - delta);
        int expectedMax = (int)(mean + delta);

        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
    }

    @Test
    public void testBuildDistanceMinMax() {
        int min = 5;
        int max = 15;

        Distance distance = Distance.buildDistance(min, max);

        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);

        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceNegativeMin() {
        int min = -5;
        int max = 15;

        Distance.buildDistance(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceZeroMax() {
        int min = 5;
        int max = 0;

        Distance.buildDistance(min, max);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(10, 20, 15.5f, 3.2f);
        Distance expectedDistance = new Distance(12, 19, 14.032f, 1.032f);

        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertEquals(expectedDistance.getMin(), transformedDistance.getMin());
        assertEquals(expectedDistance.getMax(), transformedDistance.getMax());
        assertEquals(expectedDistance.getMean(), transformedDistance.getMean(), 0.001);
        assertEquals(expectedDistance.getStdDev(), transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithinLimit() {
        Distance distance = new Distance(10, 20, 15.5f, 2.5f);

        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceMeanBelowDelta() {
        Distance distance = new Distance(10, 20, 11.5f, 3.2f);

        Distance.transformIntoCeleraAssemblerDistance(distance);
    }
}