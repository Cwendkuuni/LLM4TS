package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DistanceTest
{
    @Test
    public void testBuildDistance()
    {
        final int min = 1;
        final int max = 10;
        final float mean = 5.5f;
        final float stdDev = 2.5f;

        final Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanAndStdDev()
    {
        final float mean = 5.5f;
        final float stdDev = 2.5f;

        final Distance distance = Distance.buildDistance(mean, stdDev);

        final float delta = Distance.calculateDelta(stdDev);
        final int expectedMin = (int)(mean - delta);
        final int expectedMax = (int)(mean + delta);

        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMinAndMax()
    {
        final int min = 1;
        final int max = 10;

        final Distance distance = Distance.buildDistance(min, max);

        final float mean = Distance.computeMean(min, max);
        final float stdDev = Distance.computeStandardDeviation(min, max);

        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testCalculateDelta()
    {
        final float stdDev = 2.5f;

        final float delta = Distance.calculateDelta(stdDev);

        assertEquals(3.0f * stdDev, delta, 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance()
    {
        final float mean = 4.5f;
        final float stdDev = 3.0f;

        final Distance distance = Distance.buildDistance(mean, stdDev);

        final Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        final float correctedStdDev = (mean - 1.015f) / 3.0f;
        final int expectedMin = (int)(mean - 3.0f * correctedStdDev);
        final int expectedMax = (int)(mean + 3.0f * correctedStdDev);

        assertEquals(expectedMin, celeraDistance.getMin());
        assertEquals(expectedMax, celeraDistance.getMax());
        assertFalse(distance.equals(celeraDistance));
        assertTrue(celeraDistance.getMean() < mean);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceNoChange()
    {
        final float mean = 5.5f;
        final float stdDev = 2.5f;

        final Distance distance = Distance.buildDistance(mean, stdDev);

        final Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertEquals(distance, celeraDistance);
    }
}