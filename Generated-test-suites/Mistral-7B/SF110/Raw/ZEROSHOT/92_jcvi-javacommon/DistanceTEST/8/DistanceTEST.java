package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This class contains test methods to test the functionalities of Distance class.
 *
 * @author [Your Name]
 */
public class DistanceTest
{
    @Test
    public void testBuildDistance()
    {
        // Test building distance with provided min, max, mean and stdDev
        final int min = 10;
        final int max = 20;
        final float mean = 15.5f;
        final float stdDev = 3.2f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);

        // Test building distance with provided mean and std Dev
        final float meanValue = 25.1f;
        final float stdDevValue = 4.3f;
        distance = Distance.buildDistance(meanValue, stdDevValue);
        final float delta = Distance.calculateDelta(stdDevValue);
        final int expectedMin = (int)(meanValue - delta);
        final int expectedMax = (int)(meanValue + delta);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(meanValue, distance.getMean(), 0.01);
        assertEquals(stdDevValue, distance.getStdDev(), 0.01);
    }

    @Test
    public void testComputeStandardDeviation()
    {
        // Testing ComputeStandardDeviation method with given min and max
        final int min = 2;
        final int max = 8;
        final float expectedStdDev = (max - min) / 6.0f;
        assertEquals(expectedStdDev, Distance.computeStandardDeviation(min, max), 0.01);
    }

    @Test
    public void testComputeMean()
    {
        // Testing ComputeMean method with given min and max
        final int min = 10;
        final int max = 20;
        final float expectedMean = min / 2.0f + max / 2.0f;
        assertEquals(expectedMean, Distance.computeMean(min, max), 0.01);
    }

    @Test
    public void testBuildDistanceWithMinMax()
    {
        // Test building distance with provided min and max
        final int min = 5;
        final int max = 15;
        Distance distance = Distance.buildDistance(min, max);
        final float mean = Distance.computeMean(min, max);
        final float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance()
    {
        // Test transforming distance into Celera Assembler Distance
        final float mean = 10.5f;
        final float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        if (mean < (3 * stdDev)) {
            final float correctedStdDev = (mean - Distance.MAX_ROUNDING_ERROR) / Distance.SIX;
            final int expectedMin = (int)(mean - correctedStdDev);
            final int expectedMax = (int)(mean + correctedStdDev);
            assertEquals(expectedMin, celeraDistance.getMin());
            assertEquals(expectedMax, celeraDistance.getMax());
            assertEquals(mean, celeraDistance.getMean(), 0.01);
            assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01);
        } else {
            assertEquals(distance.getMin(), celeraDistance.getMin());
            assertEquals(distance.getMax(), celeraDistance.getMax());
            assertEquals(distance.getMean(), celeraDistance.getMean(), 0.01);
            assertEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.01);
        }
    }
}