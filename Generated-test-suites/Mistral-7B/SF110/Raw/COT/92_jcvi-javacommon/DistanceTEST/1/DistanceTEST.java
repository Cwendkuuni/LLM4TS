package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMeanAndStandardDeviation() {
        float mean = 15.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        assertNotNull(distance);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 3.2f;

        float delta = Distance.calculateDelta(stdDev);

        assertEquals(9.6f, delta, 0.001f);
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
        int min = 10;
        int max = 20;

        Distance distance = Distance.buildDistance(min, max);

        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertNotNull(distance.getMean());
        assertNotNull(distance.getStdDev());
    }

    @Test
    public void testComputeStandardDeviation() {
        int min = 10;
        int max = 20;

        float stdDev = Distance.computeStandardDeviation(min, max);

        assertEquals(1.6666667f, stdDev, 0.001f);
    }

    @Test
    public void testComputeMean() {
        int min = 10;
        int max = 20;

        float mean = Distance.computeMean(min, max);

        assertEquals(15.0f, mean, 0.001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMinOrMax() {
        int min = -5;
        int max = -10;

        Distance distance = Distance.buildDistance(min, max);

        // This test case should throw an IllegalArgumentException
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        float mean = 10.5f;
        float stdDev = 3.2f;
        Distance distance = Distance.buildDistance(mean, stdDev);

        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertNotSame(distance, celeraDistance);
        assertEquals(mean, celeraDistance.getMean(), 0.001f);
        assertEquals(stdDev / 3.0f, celeraDistance.getStdDev(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanBelowDelta() {
        float mean = 5.0f;
        float stdDev = 6.0f;
        Distance distance = Distance.buildDistance(mean, stdDev);

        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertNotSame(distance, celeraDistance);
        assertEquals(mean, celeraDistance.getMean(), 0.001f);
        assertEquals(mean / 3.0f, celeraDistance.getStdDev(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanAboveDelta() {
        float mean = 15.0f;
        float stdDev = 6.0f;
        Distance distance = Distance.buildDistance(mean, stdDev);

        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertEquals(distance, celeraDistance);
    }
}