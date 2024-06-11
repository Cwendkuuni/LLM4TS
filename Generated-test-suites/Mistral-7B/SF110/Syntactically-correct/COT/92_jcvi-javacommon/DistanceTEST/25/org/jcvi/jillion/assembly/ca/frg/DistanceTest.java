package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.jcvi.jillion.assembly.ca.frg.Distance;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        final int min = 10;
        final int max = 20;
        final float mean = 15.5f;
        final float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        final float mean = 15.5f;
        final float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        float delta = Distance.calculateDelta(stdDev);
        int expectedMin = (int) (mean - delta);
        int expectedMax = (int) (mean + delta);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        final int min = 10;
        final int max = 20;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMinOrMax() {
        final int min = -5;
        final int max = 5;
        // Distance constructor should throw an IllegalArgumentException
        Distance.buildDistance(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithMeanBelowCalculatedDelta() {
        final float mean = 5.0f;
        final float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        // transformIntoCeleraAssemblerDistance should throw an IllegalArgumentException
        Distance.transformIntoCeleraAssemblerDistance(distance);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        final int min = 10;
        final int max = 20;
        final float mean = 15.5f;
        final float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(mean, celeraDistance.getMean(), 0.001f);
        assertEquals(stdDev, celeraDistance.getStdDev(), 0.001f);
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());
    }

    @Test
    public void testHashCode() {
        int min1 = 1;
        int max1 = 3;
        float mean1 = 2.5f;
        float stdDev1 = 1.0f;
        Distance distance1 = new Distance(min1, max1, mean1, stdDev1);

        int min2 = 2;
        int max2 = 4;
        float mean2 = 3.5f;
        float stdDev2 = 2.0f;
        Distance distance2 = new Distance(min2, max2, mean2, stdDev2);

        int result1 = distance1.hashCode();
        int result2 = distance2.hashCode();

        // The hashcodes should not be equal since the objects are different
        assertNotEquals(result1, result2);

        // Testing that the hashcodes are consistent with the object's state
        Distance distance3 = new Distance(min1, max1, mean1, stdDev1);
        assertEquals(result1, distance3.hashCode());

        Distance distance4 = new Distance(min2, max2, mean2, stdDev2);
        assertEquals(result2, distance4.hashCode());
    }

    @Test
    public void testEquals() {
        int min1 = 1;
        int max1 = 3;
        float mean1 = 2.5f;
        float stdDev1 = 1.0f;
        Distance distance1 = new Distance(min1, max1, mean1, stdDev1);

        int min2 = 2;
        int max2 = 4;
        float mean2 = 3.5f;
        float stdDev2 = 2.0f;
        Distance distance2 = new Distance(min2, max2, mean2, stdDev2);

        // Two different objects should not be equal
        assertFalse(distance1.equals(distance2));

        // Object should be equal to itself
        assertTrue(distance1.equals(distance1));

        // Objects with equal state should be equal
        Distance distance3 = new Distance(min1, max1, mean1, stdDev1);
        assertTrue(distance1.equals(distance3));

        // Objects with equal state created through different constructors should be equal
        Distance distance4 = new Distance(mean1, stdDev1, min1, max1);
        assertTrue(distance1.equals(distance4));
    }

    @Test
    public void testToString() {
        final int min = 10;
        final int max = 20;
        final float mean = 15.5f;
        final float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        String result = distance.toString();
        String expected = String.format("%.3f, std: %.3f", mean, stdDev);
        assertEquals(expected, result);
    }
}