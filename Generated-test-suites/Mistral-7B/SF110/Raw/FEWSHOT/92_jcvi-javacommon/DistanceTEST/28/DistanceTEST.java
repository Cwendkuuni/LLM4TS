package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.01f);
        assertEquals(1.5f, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceMeanOnly() {
        Distance distance = Distance.buildDistance(4.5f, 1.5f);
        assertEquals(0, distance.getMin());
        assertEquals(9, distance.getMax());
        assertEquals(4.5f, distance.getMean(), 0.01f);
        assertEquals(1.5f, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testCalculateDelta() {
        assertEquals(3.0f, Distance.calculateDelta(1.5f), 0.01f);
        assertEquals(4.5f, Distance.calculateDelta(2.25f), 0.01f);
    }

    @Test
    public void testBuildDistanceFromMinMax() {
        Distance distance = Distance.buildDistance(1, 5);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.01f);
        assertEquals(1.0f, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testComputeStandardDeviation() {
        assertEquals(0.5f, Distance.computeStandardDeviation(1, 6), 0.01f);
        assertEquals(1.0f, Distance.computeStandardDeviation(1, 2), 0.01f);
    }

    @Test
    public void testComputeMean() {
        assertEquals(3.0f, Distance.computeMean(1, 5), 0.01f);
        assertEquals(4.5f, Distance.computeMean(5, 7), 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(1, 5, 3.0f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(5, transformedDistance.getMax());
        assertEquals(2.985f, transformedDistance.getMean(), 0.01f);
        assertEquals(0.4967f, transformedDistance.getStdDev(), 0.01f);

        Distance distance2 = new Distance(1, 5, 2.5f, 1.5f);
        Distance transformedDistance2 = Distance.transformIntoCeleraAssemblerDistance(distance2);
        assertEquals(1, transformedDistance2.getMin());
        assertEquals(5, transformedDistance2.getMax());
        assertEquals(2.0f, transformedDistance2.getMean(), 0.01f);
        assertEquals(0.25f, transformedDistance2.getStdDev(), 0.01f);
    }
}