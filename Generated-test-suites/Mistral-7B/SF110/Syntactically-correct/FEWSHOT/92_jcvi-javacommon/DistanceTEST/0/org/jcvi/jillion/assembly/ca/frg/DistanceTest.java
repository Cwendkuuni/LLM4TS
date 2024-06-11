package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.0f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 15.0f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);
        float delta = Distance.calculateDelta(stdDev);
        int expectedMin = (int)(mean - delta);
        int expectedMax = (int)(mean + delta);

        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        int min = 10;
        int max = 20;

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
        int min = 10;
        int max = 20;
        float mean = 12.5f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertTrue(distance.getMean() < Distance.calculateDelta(distance.getStdDev()));
        assertNotEquals(distance, celeraDistance);
        assertEquals(mean, celeraDistance.getMean(), 0.001f);
        assertEquals(stdDev / 3.0f, celeraDistance.getStdDev(), 0.001f);
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());

        distance = Distance.buildDistance(min, max);
        celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNull(celeraDistance);
    }
}