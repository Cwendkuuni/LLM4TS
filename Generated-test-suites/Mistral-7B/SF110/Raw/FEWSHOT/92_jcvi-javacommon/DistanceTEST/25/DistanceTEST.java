package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
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
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceMinMax() {
        int min = 10;
        int max = 20;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float correctedStdDev = (mean - 1.015f) / 3.0f;
        assertEquals(distance.getMin(), celeraDistance.getMin());
        assertEquals(distance.getMax(), celeraDistance.getMax());
        assertEquals(mean, celeraDistance.getMean(), 0.01f);
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01f);

        Distance distanceWithSmallMean = Distance.buildDistance(min, max, mean - 1.015f, stdDev);
        Distance celeraDistanceWithSmallMean = Distance.transformIntoCeleraAssemblerDistance(distanceWithSmallMean);
        assertEquals(distanceWithSmallMean.getMin(), celeraDistanceWithSmallMean.getMin());
        assertEquals(distanceWithSmallMean.getMax(), celeraDistanceWithSmallMean.getMax());
        assertEquals(mean - 1.015f, celeraDistanceWithSmallMean.getMean(), 0.01f);
        assertEquals(stdDev, celeraDistanceWithSmallMean.getStdDev(), 0.01f);
    }
}