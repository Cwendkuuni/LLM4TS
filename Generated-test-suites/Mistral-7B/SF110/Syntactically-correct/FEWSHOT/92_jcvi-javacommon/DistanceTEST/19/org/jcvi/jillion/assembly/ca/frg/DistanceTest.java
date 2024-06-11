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
    public void testBuildDistanceMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 2.5f);
        assertEquals(5.5f, distance.getMean());
        assertEquals(2.5f, distance.getStdDev());
        assertNull(distance.getMin());
        assertNull(distance.getMax());
    }

    @Test
    public void testCalculateDelta() {
        assertEquals(3.0f, Distance.calculateDelta(1.0f), 0.001f);
        assertEquals(9.0f, Distance.calculateDelta(3.0f), 0.001f);
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
        Distance distance = Distance.buildDistance(2.0f, 8.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(2.0f, transformedDistance.getMean(), 0.001f);
        assertEquals(1.667f, transformedDistance.getStdDev(), 0.001f);
        assertEquals(2, transformedDistance.getMin());
        assertEquals(8, transformedDistance.getMax());

        Distance distanceWithMeanBelowDelta = Distance.buildDistance(1.0f, 4.0f);
        Distance transformedDistanceWithMeanBelowDelta = Distance.transformIntoCeleraAssemblerDistance(distanceWithMeanBelowDelta);
        assertEquals(1.015f, transformedDistanceWithMeanBelowDelta.getMean(), 0.001f);
        assertEquals(1.333f, transformedDistanceWithMeanBelowDelta.getStdDev(), 0.001f);
        assertEquals(1, transformedDistanceWithMeanBelowDelta.getMin());
        assertEquals(4, transformedDistanceWithMeanBelowDelta.getMax());
    }
}