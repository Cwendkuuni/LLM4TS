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
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5.5f, celeraDistance.getMean());
        assertEquals(celeraDistance.getStdDev(), 1.7195f, 0.001f); // Approx value
        assertEquals(1, celeraDistance.getMin());
        assertEquals(10, celeraDistance.getMax());

        Distance distanceWithSmallMean = Distance.buildDistance(1, 10, 1.5f, 2.5f);
        Distance celeraDistanceWithSmallMean = Distance.transformIntoCeleraAssemblerDistance(distanceWithSmallMean);
        assertEquals(1.515f, celeraDistanceWithSmallMean.getMean(), 0.001f);
        assertEquals(celeraDistanceWithSmallMean.getStdDev(), 1.25f, 0.001f); // Approx value
        assertEquals(1, celeraDistanceWithSmallMean.getMin());
        assertEquals(10, celeraDistanceWithSmallMean.getMax());
    }

    @Test
    public void testCalculateDelta() {
        assertEquals(1.8f, Distance.calculateDelta(2.5f), 0.001f);
        assertEquals(1.8f, Distance.calculateDelta(1.2f), 0.001f);
    }

    @Test
    public void testComputeMean() {
        assertEquals(3.5f, Distance.computeMean(1, 7), 0.001f);
        assertEquals(5.5f, Distance.computeMean(1, 11), 0.001f);
    }

    @Test
    public void testComputeStandardDeviation() {
        assertEquals(1.2f, Distance.computeStandardDeviation(1, 6), 0.001f);
        assertEquals(1.8f, Distance.computeStandardDeviation(1, 11), 0.001f);
    }
}