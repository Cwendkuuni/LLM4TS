package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithIntsAndMeanAndStdDev() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.2f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 3.2f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int min = (int) Math.round(mean - 3 * stdDev);
        int max = (int) Math.round(mean + 3 * stdDev);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithInts() {
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
    public void testCalculateDelta() {
        float stdDev = 3.2f;
        float delta = Distance.calculateDelta(stdDev);
        assertEquals(9.6f, delta, 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5, 15, 10.5f, 2.5f);
        Distance expected = new Distance(5, 15, 8.0f, 1.667f);
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(expected.getMean(), transformed.getMean(), 0.001f);
        assertEquals(expected.getStdDev(), transformed.getStdDev(), 0.001f);
        assertNotSame(distance, transformed);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithSameDistance() {
        Distance distance = new Distance(5, 15, 10.5f, 2.5f);
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance, transformed);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(5, 15, 10.5f, 2.5f);
        Distance distance2 = new Distance(5, 15, 10.5f, 2.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(5, 15, 10.5f, 2.5f);
        Distance distance2 = new Distance(5, 15, 10.5f, 2.5f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testNotEquals() {
        Distance distance1 = new Distance(5, 15, 10.5f, 2.5f);
        Distance distance2 = new Distance(5, 15, 10.5f, 3.5f);
        assertFalse(distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(5, 15, 10.5f, 2.5f);
        String result = distance.toString();
        String expected = String.format("%.3f, std: %.3f", 10.5f, 2.5f);
        assertEquals(expected, result);
    }
}