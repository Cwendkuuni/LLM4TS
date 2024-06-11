package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceMeanStdDev() {
        float mean = 15.5f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        float delta = Distance.calculateDelta(stdDev);
        int expectedMin = (int)(mean - delta);
        int expectedMax = (int)(mean + delta);

        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
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
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 3.5f;

        float delta = Distance.calculateDelta(stdDev);

        assertEquals(3.0f * stdDev, delta, 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        float correctedStdDev = (mean - 1.015f) / 3.0f;

        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());
        assertEquals(mean, celeraDistance.getMean(), 0.01);
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01);

        // Test when mean is less than calculated delta
        mean -= 2.0f;

        distance = Distance.buildDistance(min, max, mean, stdDev);
        celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertNotEquals(distance, celeraDistance);
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());
        assertEquals(mean, celeraDistance.getMean(), 0.01);
        assertNotEquals(stdDev, celeraDistance.getStdDev(), 0.01);
    }

    @Test
    public void testHashCode() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        int expectedHashCode = 112813275; // Manually calculated hashcode based on given fields

        int actualHashCode = distance.hashCode();

        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testEquals() {
        int min1 = 10;
        int max1 = 20;
        float mean1 = 15.5f;
        float stdDev1 = 3.5f;

        int min2 = 10;
        int max2 = 20;
        float mean2 = 15.5f;
        float stdDev2 = 3.5f;

        Distance distance1 = Distance.buildDistance(min1, max1, mean1, stdDev1);
        Distance distance2 = Distance.buildDistance(min2, max2, mean2, stdDev2);

        Distance distance3 = new Distance(min1, max1, mean1, stdDev1);

        Distance distance4 = new Distance(min1, max1, mean1, stdDev1);

        Distance distance5 = new Distance(min1, max1, mean1, stdDev2);

        Distance distance6 = new Distance(min2, max1, mean1, stdDev1);

        Distance distance7 = new Distance(min1, max1, mean2, stdDev1);

        assertTrue(distance1.equals(distance1));
        assertTrue(distance1.equals(distance3));
        assertTrue(distance1.equals(distance4));

        assertFalse(distance1.equals(null));
        assertFalse(distance1.equals(""));
        assertFalse(distance1.equals(new Object()));

        assertTrue(distance1.equals(distance2));

        assertFalse(distance1.equals(distance3));
        assertFalse(distance1.equals(distance5));
        assertFalse(distance1.equals(distance6));
        assertFalse(distance1.equals(distance7));

        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance1);

        assertFalse(distance1.equals(celeraDistance));
    }

    @Test
    public void testToString() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        String expectedString = String.format("%.3f, std: %.3f", mean, stdDev);

        String actualString = distance.toString();

        assertEquals(expectedString, actualString);
    }
}