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
    public void testBuildDistanceMeanStdDev() {
        Distance distance = Distance.buildDistance(4.5f, 2.5f);
        assertEquals(6, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.01f);
        assertEquals(2.5f, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceMinMax() {
        Distance distance = Distance.buildDistance(1, 5);
        assertEquals(3, distance.getMin());
        assertEquals(5, distance.getMax());
        float mean = (1 + 5) / 2.0f;
        float stdDev = (5 - 1) / 6.0f;
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(2, 6, 4.5f, 2.0f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(2, celeraDistance.getMin());
        assertEquals(6, celeraDistance.getMax());
        float mean = (2 + 6) / 2.0f;
        float stdDevCorrected = (mean - 1.015f) / 3.0f;
        assertEquals(mean, celeraDistance.getMean(), 0.01f);
        assertEquals(stdDevCorrected, celeraDistance.getStdDev(), 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceNotAdjusted() {
        Distance distance = Distance.buildDistance(2, 6, 4.5f, 2.0f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotEquals(2, celeraDistance.getMin()); // The minimum value may vary
        assertNotEquals(6, celeraDistance.getMax()); // The maximum value may vary
        assertEquals(4.5f, celeraDistance.getMean(), 0.01f);
        assertEquals(2.0f, celeraDistance.getStdDev(), 0.01f);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        int hashCode1 = distance1.hashCode();
        int hashCode2 = distance2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        assertTrue(distance1.equals(distance2));

        Distance distance3 = Distance.buildDistance(1, 5, 3.5f, 2.0f);
        assertFalse(distance1.equals(distance3));

        Distance distance4 = new Distance(1, 5, 3.5f, 1.5f);
        assertTrue(distance1.equals(distance4));
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        assertEquals("3.500, std: 1.500", distance.toString());
    }
}