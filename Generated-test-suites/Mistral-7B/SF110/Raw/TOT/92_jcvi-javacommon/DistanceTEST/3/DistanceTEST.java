package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        final int expectedMin = 10;
        final int expectedMax = 20;
        final float expectedMean = 15.5f;
        final float expectedStdDev = 3.25f;

        final Distance distance = Distance.buildDistance(expectedMin, expectedMax, expectedMean, expectedStdDev);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(expectedMean, distance.getMean(), 0.001f);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        final float expectedMean = 10.5f;
        final float expectedStdDev = 2.5f;

        final Distance distance = Distance.buildDistance(expectedMean, expectedStdDev);
        final float delta = Distance.calculateDelta(expectedStdDev);
        final int expectedMin = (int)(expectedMean - delta);
        final int expectedMax = (int)(expectedMean + delta);

        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(expectedMean, distance.getMean(), 0.001f);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        final int expectedMin = 5;
        final int expectedMax = 15;

        final Distance distance = Distance.buildDistance(expectedMin, expectedMax);
        final float expectedMean = Distance.computeMean(expectedMin, expectedMax);
        final float expectedStdDev = Distance.computeStandardDeviation(expectedMin, expectedMax);

        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(expectedMean, distance.getMean(), 0.001f);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithValidDistance() {
        final int min = 10;
        final int max = 20;
        final float mean = 15.5f;
        final float stdDev = 3.25f;

        final Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        final Distance expectedDistance = Distance.buildDistance(min, max);

        final Distance celeraAssemblerDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(expectedDistance, celeraAssemblerDistance);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanBelowDelta() {
        final int min = 10;
        final int max = 20;
        final float mean = 11.5f;
        final float stdDev = 3.25f;

        final Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        final Distance expectedDistance = Distance.buildDistance(min, max);
        expectedDistance.setStdDev(expectedDistance.getStdDev() / 3.0f);

        final Distance celeraAssemblerDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(expectedDistance, celeraAssemblerDistance);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanAboveDelta() {
        final int min = 10;
        final int max = 20;
        final float mean = 19.5f;
        final float stdDev = 3.25f;

        final Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(distance, Distance.transformIntoCeleraAssemblerDistance(distance));
    }
}