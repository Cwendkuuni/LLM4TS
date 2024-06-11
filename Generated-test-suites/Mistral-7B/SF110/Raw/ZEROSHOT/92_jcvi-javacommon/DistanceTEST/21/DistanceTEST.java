package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
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
    public void testBuildDistanceWithMeanStdDev() {
        float mean = 15.5f;
        float stdDev = 3.2f;

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
        float mean = 15.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance expectedDistance = Distance.buildDistance(min, max);

        if (mean < (3.0f * stdDev)) {
            Distance correctedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

            float correctedStdDev = (mean - 1.015f) / 3.0f;

            assertEquals(expectedDistance.getMin(), correctedDistance.getMin());
            assertEquals(expectedDistance.getMax(), correctedDistance.getMax());
            assertEquals(mean, correctedDistance.getMean(), 0.001f);
            assertEquals(correctedStdDev, correctedDistance.getStdDev(), 0.001f);
        } else {
            Distance expectedDistance2 = distance;

            Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

            assertEquals(expectedDistance2, transformedDistance);
        }
    }

    @Test
    public void testHashCode() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        int expectedHashCode = 31 * 31 * (min + 0x31) + Float.floatToIntBits(mean) + 31 * (max + 0x31) + Float.floatToIntBits(stdDev);

        int actualHashCode = distance.hashCode();

        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testEquals() {
        int min1 = 10;
        int max1 = 20;
        float mean1 = 15.5f;
        float stdDev1 = 3.2f;

        int min2 = 10;
        int max2 = 20;
        float mean2 = 15.5f;
        float stdDev2 = 3.2f;

        Distance distance1 = Distance.buildDistance(min1, max1, mean1, stdDev1);
        Distance distance2 = Distance.buildDistance(min2, max2, mean2, stdDev2);

        assertTrue(distance1.equals(distance1));
        assertTrue(distance1.equals(distance2));
        assertTrue(distance2.equals(distance1));
        assertTrue(distance1.equals(Distance.buildDistance(min1, max1, mean1, stdDev1)));

        int min3 = 11;
        int max3 = 21;
        float mean3 = 15.5f;
        float stdDev3 = 3.2f;

        Distance distance3 = Distance.buildDistance(min3, max3, mean3, stdDev3);

        assertNotEquals(distance1, distance3);
        assertNotEquals(distance3, distance1);
    }

    @Test
    public void testToString() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        String expectedString = String.format("%.3f, std: %.3f", mean, stdDev);

        assertEquals(expectedString, distance.toString());
    }
}