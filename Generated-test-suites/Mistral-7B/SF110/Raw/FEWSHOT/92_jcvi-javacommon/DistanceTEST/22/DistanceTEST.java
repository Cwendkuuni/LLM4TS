package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        assertEquals(3, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0.01f);
        assertEquals(1.5f, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        Distance distance = Distance.buildDistance(3.5f, 1.5f);
        int expectedMin = (int) Math.round(3.5f - 3.0f * 1.5f);
        int expectedMax = (int) Math.round(3.5f + 3.0f * 1.5f);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0.01f);
        assertEquals(1.5f, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        Distance distance = Distance.buildDistance(1, 5);
        float mean = (float) (1.0f +5.0f) / 2.0f;
        float expectedStdDev = (5.0f - 1.0f) / 6.0f;
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testCalculateDelta() {
        assertEquals(3.0f, Distance.calculateDelta(1.5f), 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(1, 5, 3.5f, 1.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float mean = distance.getMean() - 1.015f;
        Distance expectedDistance = Distance.buildDistance((int) Math.round(mean - 3.0f * (mean - 1.015f) / 3.0f), (int) Math.round(mean + 3.0f * (mean - 1.015f) / 3.0f), mean, (mean - 1.015f) / 3.0f);
        assertEquals(expectedDistance.getMin(), celeraDistance.getMin());
        assertEquals(expectedDistance.getMax(), celeraDistance.getMax());
        assertEquals(mean, celeraDistance.getMean(), 0.01f);
        assertEquals(expectedDistance.getStdDev(), celeraDistance.getStdDev(), 0.01f);
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(1, 5, 3.5f, 1.5f);
        Distance distance2 = new Distance(1, 5, 3.5f, 1.5f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(1, 5, 3.5f, 1.5f);
        Distance distance2 = new Distance(1, 5, 3.5f, 1.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(1, 5, 3.5f, 1.5f);
        String expectedString = String.format("%.3f, std: %.3f", 3.5f, 1.5f);
        assertEquals(expectedString, distance.toString());
    }
}