package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 1.2f);
        assertEquals(3.5f, distance.getMean());
        assertEquals(1.2f, distance.getStdDev());
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanStdDev() {
        Distance distance = Distance.buildDistance(4.5f, 2.5f);
        assertEquals(4.5f, distance.getMean());
        assertEquals(2.5f, distance.getStdDev());
        assertNull(distance.getMin());
        assertNull(distance.getMax());
    }

    @Test
    public void testCalculateDelta() {
        assertEquals(3.0f, Distance.calculateDelta(1.2f), 0.01);
    }

    @Test
    public void testComputeMean() {
        assertEquals(11.0f, Distance.computeMean(1, 10), 0.01);
    }

    @Test
    public void testComputeStandardDeviation() {
        assertEquals(1.0f, Distance.computeStandardDeviation(1, 2), 0.01);
    }

    @Test
    public void testBuildDistanceMinMax() {
        Distance distance = Distance.buildDistance(1, 5);
        assertEquals(3.0f, distance.getMean(), 0.01);
        assertEquals(2.5f, distance.getStdDev(), 0.01);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 1.2f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(3.5f, transformedDistance.getMean());
        assertEquals(0.686f, transformedDistance.getStdDev(), 0.01);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(5, transformedDistance.getMax());

        Distance distanceWithMeanBelowDelta = Distance.buildDistance(1, 5, 1.5f, 1.2f);
        Distance transformedDistanceWithMeanBelowDelta = Distance.transformIntoCeleraAssemblerDistance(distanceWithMeanBelowDelta);
        assertEquals(1.5f, transformedDistanceWithMeanBelowDelta.getMean());
        assertEquals(0.424f, transformedDistanceWithMeanBelowDelta.getStdDev(), 0.01);
        assertEquals(1, transformedDistanceWithMeanBelowDelta.getMin());
        assertEquals(5, transformedDistanceWithMeanBelowDelta.getMax());
    }
}