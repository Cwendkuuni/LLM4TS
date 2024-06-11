package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 5;
        int max = 10;
        float mean = 7.5f;
        float stdDev = 1.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceFromMeanAndStdDev() {
        float mean = 7.5f;
        float stdDev = 1.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        Distance expectedDistance = Distance.buildDistance(5, 10);
        assertEquals(expectedDistance.getMean(), distance.getMean(), 0.01f);
        assertEquals(expectedDistance.getStdDev(), distance.getStdDev(), 0.01f);
        assertEquals(expectedDistance.getMin(), distance.getMin());
        assertEquals(expectedDistance.getMax(), distance.getMax());
    }

    @Test
    public void testBuildDistanceFromMinAndMax() {
        int min = 5;
        int max = 10;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 1.5f;
        float expectedDelta = 4.5f;
        float delta = Distance.calculateDelta(stdDev);
        assertEquals(expectedDelta, delta, 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 5;
        int max = 10;
        float mean = 7.5f;
        float stdDev = 1.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        float correctedStdDev = (mean - 1.015f) / 3.0f;
        Distance expectedDistance = Distance.buildDistance(min, max, mean, correctedStdDev);

        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(expectedDistance.getMean(), transformedDistance.getMean(), 0.01f);
        assertEquals(expectedDistance.getStdDev(), transformedDistance.getStdDev(), 0.01f);
        assertEquals(expectedDistance.getMin(), transformedDistance.getMin());
        assertEquals(expectedDistance.getMax(), transformedDistance.getMax());
    }

    @Test
    public void testEquals() {
        int min = 5;
        int max = 10;
        float mean = 7.5f;
        float stdDev = 1.5f;
        Distance distance1 = Distance.buildDistance(min, max, mean, stdDev);
        Distance distance2 = Distance.buildDistance(min, max, mean, stdDev);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testNotEquals() {
        int min1 = 5;
        int max1 = 10;
        float mean1 = 7.5f;
        float stdDev1 = 1.5f;
        int min2 = 6;
        int max2 = 11;
        float mean2 = 7.5f;
        float stdDev2 = 1.5f;
        Distance distance1 = Distance.buildDistance(min1, max1, mean1, stdDev1);
        Distance distance2 = Distance.buildDistance(min2, max2, mean2, stdDev2);
        assertFalse(distance1.equals(distance2));
    }

    @Test
    public void testHashCode() {
        int min = 5;
        int max = 10;
        float mean = 7.5f;
        float stdDev = 1.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        int expectedHashCode = 1669490159;
        int hashCode = distance.hashCode();
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testToString() {
        int min = 5;
        int max = 10;
        float mean = 7.5f;
        float stdDev = 1.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        String expectedString = "7.500, std: 0.500";
        String toString = distance.toString();
        assertEquals(expectedString, toString);
    }
}