package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5, 2);
        assertEquals(5, distance.getMean());
        assertEquals(2, distance.getStdDev());
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5, 2);
        assertEquals(5, distance.getMean());
        assertEquals(2, distance.getStdDev());
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        Distance distance = Distance.buildDistance(1, 10);
        float expectedMean = 6.0f;
        float expectedStdDev = (float) Math.sqrt(35.0f / 12.0f);
        assertEquals(expectedMean, distance.getMean(), 0.001f);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.001f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
    }

    @Test
    public void testCalculateDelta() {
        assertEquals(3.0f, Distance.calculateDelta(2.0f), 0.001f);
        assertEquals(9.0f, Distance.calculateDelta(3.0f), 0.001f);
    }

    @Test
    public void testComputeStandardDeviation() {
        int min = 1;
        int max = 10;
        assertEquals(1.0f / 6.0f, Distance.computeStandardDeviation(min, max), 0.001f);
    }

    @Test
    public void testComputeMean() {
        assertEquals(3.5f, Distance.computeMean(1, 5), 0.001f);
        assertEquals(7.5f, Distance.computeMean(5, 10), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_MeanLessThanDelta() {
        Distance distance = new Distance(1, 10, 1, 2);
        Distance expectedDistance = new Distance(1, 10, 1.015f, 0.333f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(expectedDistance.getMean(), transformedDistance.getMean(), 0.001f);
        assertEquals(expectedDistance.getStdDev(), transformedDistance.getStdDev(), 0.001f);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_MeanGreaterThanOrEqualDelta() {
        Distance distance = new Distance(1, 10, 5, 2);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance, transformedDistance);
    }

    @Test
    public void testHashCode() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.2f;

        Distance distance1 = new Distance(min, max, mean, stdDev);
        Distance distance2 = new Distance(min, max, mean, stdDev);

        int hashCode1 = distance1.hashCode();
        int hashCode2 = distance2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        int min1 = 1;
        int max1 = 10;
        float mean1 = 5.5f;
        float stdDev1 = 2.2f;

        int min2 = 1;
        int max2 = 10;
        float mean2 = 5.5f;
        float stdDev2 = 2.2f;

        Distance distance1 = new Distance(min1, max1, mean1, stdDev1);
        Distance distance2 = new Distance(min2, max2, mean2, stdDev2);

        assertTrue(distance1.equals(distance2));

        Distance distance3 = new Distance(min1, max1, mean1, stdDev1 + 0.001f);
        assertFalse(distance1.equals(distance3));

        Distance distance4 = new Distance(min1, max1 + 1, mean1, stdDev1);
        assertFalse(distance1.equals(distance4));

        Distance distance5 = new Distance(min1, max1, mean1, stdDev1);
        Distance distance6 = new Distance(-1, max1, mean1, stdDev1);
        assertFalse(distance1.equals(distance6));

        Distance distance7 = new Object();
        assertFalse(distance1.equals(distance7));
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(1, 10, 5.5f, 2.2f);
        String expectedToString = "5.500, std: 2.200";
        assertEquals(expectedToString, distance.toString());
    }
}