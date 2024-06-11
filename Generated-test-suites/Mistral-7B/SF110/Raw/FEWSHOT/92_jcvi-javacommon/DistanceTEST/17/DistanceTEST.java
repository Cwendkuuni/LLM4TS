package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(5.5f, distance.getMean(), 0.01f);
        assertEquals(2.5f, distance.getStdDev(), 0.01f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 2.5f);
        assertEquals(5.5f, distance.getMean(), 0.01f);
        assertEquals(2.5f, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        Distance distance = Distance.buildDistance(1, 10);
        float mean = Distance.computeMean(1, 10);
        float stdDev = Distance.computeStandardDeviation(1, 10);
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
    }

    @Test
    public void testCalculateDelta() {
        assertEquals(3.0f, Distance.calculateDelta(2.5f), 0.01f);
    }

    @Test
    public void testComputeMean() {
        assertEquals(5.0f, Distance.computeMean(1, 10), 0.01f);
    }

    @Test
    public void testComputeStandardDeviation() {
        assertEquals(1.0f, Distance.computeStandardDeviation(1, 10), 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(4.5f, celeraDistance.getMean(), 0.01f);
        assertEquals(1.5f, celeraDistance.getStdDev(), 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithSameMean() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5.5f, celeraDistance.getMean(), 0.01f);
        assertEquals(2.5f, celeraDistance.getStdDev(), 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithDifferentMean() {
        Distance distance = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotEquals(4.5f, celeraDistance.getMean(), 0.01f);
        assertEquals(1.5f, celeraDistance.getStdDev(), 0.01f);
    }
}