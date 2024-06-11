package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        int min = 10;
        int max = 50;
        float mean = 30.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        float mean = 30.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        float delta = Distance.calculateDelta(stdDev);
        int expectedMin = (int)(mean - delta);
        int expectedMax = (int)(mean + delta);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        int min = 10;
        int max = 50;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithinLimit() {
        int min = 10;
        int max = 50;
        float mean = 30.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        float transformedMean = distance.getMean() - 1.015f;
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(transformedMean, transformedDistance.getMean(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceOutsideLimit() {
        int min = 10;
        int max = 50;
        float mean = 20.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertFalse(Distance.transformIntoCeleraAssemblerDistance(distance) instanceof Distance);
    }

    @Test
    public void testHashCodeAndEquals() {
        int min1 = 10;
        int max1 = 50;
        float mean1 = 30.5f;
        float stdDev1 = 2.5f;

        int min2 = 10;
        int max2 = 50;
        float mean2 = 30.5f;
        float stdDev2 = 2.5f;

        int min3 = 10;
        int max3 = 50;
        float mean3 = 32.5f;
        float stdDev3 = 2.5f;

        Distance distance1 = Distance.buildDistance(min1, max1, mean1, stdDev1);
        Distance distance2 = Distance.buildDistance(min2, max2, mean2, stdDev2);
        Distance distance3 = Distance.buildDistance(min3, max3, mean3, stdDev1);

        assertEquals(distance1.hashCode(), distance1.hashCode());
        assertEquals(distance1, distance1);

        assertEquals(distance1.hashCode(), distance2.hashCode());
        assertNotEquals(distance1, distance2);

        assertEquals(distance1.hashCode(), distance3.hashCode());
        assertNotEquals(distance1, distance3);
    }
}