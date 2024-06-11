package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 1;
        int max = 5;
        float mean = 3.0f;
        float stdDev = 1.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 3.0f;
        float stdDev = 1.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);
        float delta = Distance.calculateDelta(stdDev);
        int plusDelta = (int)(mean + delta);
        int minusDelta = (int)(mean - delta);
        assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        int min = 1;
        int max = 5;

        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 1;
        int max = 5;
        float mean = 3.0f;
        float stdDev = 1.5f;

        Distance originalDistance = Distance.buildDistance(min, max, mean, stdDev);
        float correctedStdDev = (mean - 1.015f) / 3.0f;
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);

        assertEquals(originalDistance.getMin(), celeraDistance.getMin());
        assertEquals(originalDistance.getMax(), celeraDistance.getMax());
        assertNotEquals(originalDistance.getStdDev(), celeraDistance.getStdDev(), 0.001f);
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.001f);
        assertEquals(mean, celeraDistance.getMean(), 0.001f);
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 1.5f;
        float delta = Distance.calculateDelta(stdDev);
        assertEquals(4.5f, delta, 0.001f);
    }

    @Test
    public void testHashCode() {
        int min = 1;
        int max = 5;
        float mean = 3.0f;
        float stdDev = 1.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance distance2 = Distance.buildDistance(min, max, mean, stdDev);
        assertNotNull(distance.hashCode());
        assertEquals(distance.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        int min = 1;
        int max = 5;
        float mean = 3.0f;
        float stdDev = 1.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance distance2 = Distance.buildDistance(min, max, mean, stdDev);
        Distance distance3 = new Distance(min + 1, max, mean, stdDev);
        Distance distance4 = null;

        assertEquals(distance, distance2);
        assertNotEquals(distance, distance3);
        assertNull(distance.equals(distance4));
    }

    @Test
    public void testToString() {
        int min = 1;
        int max = 5;
        float mean = 3.0f;
        float stdDev = 1.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        String expected = String.format("%.3f, std: %.3f", mean, stdDev);
        assertEquals(expected, distance.toString());
    }
}