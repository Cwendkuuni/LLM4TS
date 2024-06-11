package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceMeanStdDev() {
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        float delta = calculateDelta(stdDev);
        int plusDelta = (int)(mean + delta);
        int minusDelta = (int)(mean - delta);
        int expectedMin = Math.min(plusDelta, minusDelta);
        int expectedMax = Math.max(plusDelta, minusDelta);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 2.5f;
        float expectedDelta = 3.0f * stdDev;
        assertEquals(expectedDelta, Distance.calculateDelta(stdDev), 0.01f);
    }

    @Test
    public void testBuildDistanceMinMax() {
        int min = 10;
        int max = 20;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        float expectedMean = mean / MAX_ROUNDING_ERROR;
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());
        assertEquals(expectedMean, celeraDistance.getMean(), 0.01f);
        assertEquals(stdDev, celeraDistance.getStdDev(), 0.01f);

        // Test when mean is below the delta limit
        float meanBelowLimit = mean - 3.5f;
        Distance distanceBelowLimit = Distance.buildDistance(min, max, meanBelowLimit, stdDev);
        Distance celeraDistanceBelowLimit = Distance.transformIntoCeleraAssemblerDistance(distanceBelowLimit);
        float correctedStdDevBelowLimit = (meanBelowLimit - 1.015f) / 3.0f;
        assertEquals(min, celeraDistanceBelowLimit.getMin());
        assertEquals(max, celeraDistanceBelowLimit.getMax());
        assertEquals(expectedMean, celeraDistanceBelowLimit.getMean(), 0.01f);
        assertEquals(correctedStdDevBelowLimit, celeraDistanceBelowLimit.getStdDev(), 0.01f);
    }

    @Test
    public void testHashCodeEquals() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance1 = Distance.buildDistance(min, max, mean, stdDev);
        Distance distance2 = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(distance1.hashCode(), distance2.hashCode());
        assertEquals(distance1, distance2);

        // Test different values
        int min2 = 11;
        int max2 = 21;
        float mean2 = 16.0f;
        float stdDev2 = 3.0f;
        Distance distance3 = Distance.buildDistance(min2, max2, mean2, stdDev2);
        assertNotEquals(distance1, distance3);
        assertNotEquals(distance1.hashCode(), distance3.hashCode());

        // Test null
        assertNull(Distance.buildDistance(null, null));
        assertNull(Distance.buildDistance(null, max));
        assertNull(Distance.buildDistance(min, null));
    }

    @Test
    public void testToString() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        String expectedString = String.format("%.3f, std: %.3f", mean, stdDev);
        assertEquals(expectedString, distance.toString());
    }

}