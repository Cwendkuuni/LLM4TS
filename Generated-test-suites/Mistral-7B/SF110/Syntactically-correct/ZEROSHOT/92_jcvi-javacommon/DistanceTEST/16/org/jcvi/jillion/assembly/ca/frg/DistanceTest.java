package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DistanceTest
{
    @Test
    public void testBuildDistance()
    {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.3f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testCalculateDelta()
    {
        float stdDev = 3.0f;
        float delta = Distance.calculateDelta(stdDev);
        assertEquals(9.0f, delta, 0.01);
    }

    @Test
    public void testBuildDistanceMeanStdDev()
    {
        float mean = 20.5f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceMinMax()
    {
        int min = 1;
        int max = 10;

        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);

        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance()
    {
        int min = 1;
        int max = 10;
        float mean = 5.0f;
        float stdDev = 2.0f;

        Distance distance = Distance.buildDistance(min, max);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float correctedStdDev = (mean - 1.015f) / 3.0f;

        assertEquals(mean, celeraDistance.getMean(), 0.001);
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.001);
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());

        distance = Distance.buildDistance(min, max, mean, stdDev);
        assertFalse(Distance.transformIntoCeleraAssemblerDistance(distance).equals(distance));
    }
}