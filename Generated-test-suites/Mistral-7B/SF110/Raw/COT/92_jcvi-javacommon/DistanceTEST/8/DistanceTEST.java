package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest
{
    @Test
    public void testBuildDistance()
    {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanStdDev()
    {
        float mean = 15.5f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        float delta = Distance.calculateDelta(stdDev);
        int plusDelta = (int)(mean + delta);
        int minusDelta = (int)(mean - delta);

        int expectedMin = Math.min(plusDelta, minusDelta);
        int expectedMax = Math.max(plusDelta, minusDelta);

        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceMinMax()
    {
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

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceNegativeMin()
    {
        int min = -5;
        int max = 10;
        float mean = 0;
        float stdDev = 3.5f;

        Distance.buildDistance(min, max, mean, stdDev);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceNegativeMax()
    {
        int min = 10;
        int max = -5;
        float mean = 0;
        float stdDev = 3.5f;

        Distance.buildDistance(min, max, mean, stdDev);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceZeroMean()
    {
        int min = 10;
        int max = 20;
        float mean = 0;
        float stdDev = 3.5f;

        Distance.buildDistance(min, max, mean, stdDev);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance()
    {
        float mean = 10.5f;
        float stdDev = 4.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);

        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertEquals(mean, celeraDistance.getMean(), 0.01);
        assertEquals(stdDev, celeraDistance.getStdDev(), 0.01);
        if (mean < Distance.calculateDelta(stdDev)) {
            float correctedStdDev = (mean - 1.015f) / 3.0f;
            float expectedMean = (distance.getMin() + distance.getMax()) / 2.0f;
            float expectedStdDev = (distance.getMax() - distance.getMin()) / 6.0f;
            assertEquals(expectedMean, celeraDistance.getMean(), 0.01);
            assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01);
        } else {
            assertEquals(distance.getMean(), celeraDistance.getMean(), 0.01);
            assertEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.01);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceNegativeMean()
    {
        float mean = -5.5f;
        float stdDev = 4.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);

        Distance.transformIntoCeleraAssemblerDistance(distance);
    }
}