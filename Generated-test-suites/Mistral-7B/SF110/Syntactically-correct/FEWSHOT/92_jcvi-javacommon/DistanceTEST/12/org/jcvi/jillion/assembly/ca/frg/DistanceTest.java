package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        assertEquals(3, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0.001f);
        assertEquals(1.5f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMeanStdDev() {
        Distance distance = Distance.buildDistance(4.5f, 2.5f);
        assertEquals(3, distance.getMin(), 0.001f);
        assertEquals(6, distance.getMax(), 0.001f);
        assertEquals(4.5f, distance.getMean(), 0.001f);
        assertEquals(2.5f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMinMax() {
        Distance distance = Distance.buildDistance(1, 5);
        float mean = Distance.computeMean(1, 5);
        float stdDev = Distance.computeStandardDeviation(1, 5);
        assertEquals(3, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 2.5f;
        float delta = Distance.calculateDelta(stdDev);
        assertEquals(7.5f, delta, 0.001f);
    }

    @Test
    public void testComputeMean() {
        int min = 1;
        int max = 5;
        float expectedMean = (min + max) / 2.0f;
        float mean = Distance.computeMean(min, max);
        assertEquals(expectedMean, mean, 0.001f);
    }

    @Test
    public void testComputeStandardDeviation() {
        int min = 1;
        int max = 5;
        float expectedStdDev = (max - min) / 6.0f;
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(expectedStdDev, stdDev, 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(3, celeraDistance.getMin());
        assertEquals(5, celeraDistance.getMax());
        float mean = distance.getMean();
        if (mean < 2.015f) {
            float correctedStdDev = (mean - 1.015f) / 3.0f;
            assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.001f);
        } else {
            assertEquals(1.5f, celeraDistance.getStdDev(), 0.001f);
        }
        assertEquals(mean, celeraDistance.getMean(), 0.001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceInvalidMean() {
        Distance distance = Distance.buildDistance(1, 5, 1.5f, 1.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
    }
}