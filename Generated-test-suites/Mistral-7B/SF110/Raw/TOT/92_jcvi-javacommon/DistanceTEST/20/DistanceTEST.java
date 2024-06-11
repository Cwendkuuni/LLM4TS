package org.jcvi.jillion.assembly.ca.frg.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.jcvi.jillion.assembly.ca.frg.Distance;

import org.junit.Test;

public class DistanceTest
{
    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev()
    {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanStdDev()
    {
        float mean = 5.5f;
        float stdDev = 2.5f;

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
    public void testBuildDistanceWithMinMax()
    {
        int min = 1;
        int max = 10;

        Distance distance = Distance.buildDistance(min, max);

        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);

        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testCalculateDelta()
    {
        float stdDev = 2.5f;

        float delta = Distance.calculateDelta(stdDev);

        assertEquals(7.5f, delta, 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithValidDistance()
    {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.5f;

        Distance originalDistance = new Distance(min, max, mean, stdDev);

        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);

        assertEquals(originalDistance.getMin(), transformedDistance.getMin());
        assertEquals(originalDistance.getMax(), transformedDistance.getMax());
        assertNotEquals(originalDistance.getStdDev(), transformedDistance.getStdDev());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithInvalidDistance()
    {
        int min = 1;
        int max = 10;
        float mean = 1.5f;
        float stdDev = 2.5f;

        Distance originalDistance = new Distance(min, max, mean, stdDev);

        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);

        assertNotEquals(originalDistance.getMin(), transformedDistance.getMin());
        assertNotEquals(originalDistance.getMax(), transformedDistance.getMax());
        assertNotEquals(originalDistance.getMean(), transformedDistance.getMean());
        assertNotEquals(originalDistance.getStdDev(), transformedDistance.getStdDev());
    }
}