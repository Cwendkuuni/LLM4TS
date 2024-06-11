package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f); // allow for rounding error
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        int expectedMin = (int)(mean - 3.0f * stdDev);
        int expectedMax = (int)(mean + 3.0f * stdDev);

        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f); // allow for rounding error
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
        int min = 10;
        int max = 20;

        Distance distance = Distance.buildDistance(min, max);

        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);

        Distance expectedDistance = new Distance(min, max, mean, stdDev);

        assertEquals(expectedDistance.getMean(), distance.getMean(), 0.001f); // allow for rounding error
        assertEquals(expectedDistance.getStdDev(), distance.getStdDev(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 10;
        int max = 20;
        float mean = 14.5f;
        float stdDev = 2.5f;

        Distance distance = new Distance(min, max, mean, stdDev);

        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        float expectedMean = mean / MAX_ROUNDING_ERROR;
        float expectedStdDev = stdDev / MAX_ROUNDING_ERROR;

        assertEquals(expectedMean, transformedDistance.getMean(), 0.001f); // allow for rounding error
        assertEquals(expectedStdDev, transformedDistance.getStdDev(), 0.001f);
        assertFalse(Distance.transformIntoCeleraAssemblerDistance(distance).equals(distance));
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithSmallStdDev() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 0.5f;

        Distance distance = new Distance(min, max, mean, stdDev);

        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertTrue(Distance.transformIntoCeleraAssemblerDistance(distance).equals(distance));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithNegativeMean() {
        int min = 10;
        int max = 20;
        float mean = -5.5f;
        float stdDev = 2.5f;

        Distance distance = new Distance(min, max, mean, stdDev);

        Distance.transformIntoCeleraAssemblerDistance(distance);
    }
}