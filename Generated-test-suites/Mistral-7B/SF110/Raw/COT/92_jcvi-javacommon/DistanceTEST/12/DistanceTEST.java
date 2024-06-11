package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    // Test for buildDistance method with given min, max, mean and stdDev
    @Test
    public void testBuildDistanceWithGivenValues() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    // Test for buildDistance method with given mean and stdDev
    @Test
    public void testBuildDistanceWithGivenMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int min = (int) (mean - 3 * stdDev);
        int max = (int) (mean + 3 * stdDev);
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    // Test for calculateDelta method
    @Test
    public void testCalculateDelta() {
        float stdDev = 2.5f;
        float delta = Distance.calculateDelta(stdDev);
        assertEquals(3 * stdDev, delta, 0.01);
    }

    // Test for buildDistance method with given min and max
    @Test
    public void testBuildDistanceWithGivenMinAndMax() {
        int min = 10;
        int max = 20;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    // Test for transformIntoCeleraAssemblerDistance method when mean is less than corrected stdDev
    @Test
    public void testTransformIntoCeleraAssemblerDistanceWhenMeanLessThanCorrectedStdDev() {
        int min = 10;
        int max = 20;
        float mean = 10.5f;
        float stdDev = 3.5f;
        Distance distance = new Distance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float correctedStdDev = (mean - 1.015f) / 3.0f;
        assertEquals(mean, celeraDistance.getMean(), 0.01);
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01);
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());
    }

    // Test for transformIntoCeleraAssemblerDistance method when mean is not less than corrected stdDev
    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWhenMeanGreaterThanOrEqualCorrectedStdDev() {
        int min = 10;
        int max = 20;
        float mean = 11.5f;
        float stdDev = 3.5f;
        Distance distance = new Distance(min, max, mean, stdDev);
        Distance.transformIntoCeleraAssemblerDistance(distance);
    }

    // Test for hashCode method
    @Test
    public void testHashCode() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance1 = new Distance(min, max, mean, stdDev);
        Distance distance2 = new Distance(min, max, mean, stdDev);
        int hashCode1 = distance1.hashCode();
        int hashCode2 = distance2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    // Test for equals method
    @Test
    public void testEquals() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance1 = new Distance(min, max, mean, stdDev);
        Distance distance2 = new Distance(min, max, mean, stdDev);
        assertTrue(distance1.equals(distance2));

        Distance distance3 = new Distance(min + 1, max, mean, stdDev);
        assertFalse(distance1.equals(distance3));

        Distance distance4 = new Distance(min, max, mean + 0.1f, stdDev);
        assertFalse(distance1.equals(distance4));

        Distance distance5 = new Distance(min, max, mean, stdDev + 0.1f);
        assertFalse(distance1.equals(distance5));
    }

    // Test for toString method
    @Test
    public void testToString() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = new Distance(min, max, mean, stdDev);
        String expected = String.format("%.3f, std: %.3f", mean, stdDev);
        assertEquals(expected, distance.toString());
    }
}